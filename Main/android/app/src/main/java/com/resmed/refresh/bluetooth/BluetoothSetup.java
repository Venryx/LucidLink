package com.resmed.refresh.bluetooth;

import com.resmed.cobs.COBS;
import com.resmed.refresh.bed.LedsState;
import com.resmed.refresh.bluetooth.receivers.*;
import com.resmed.refresh.bluetooth.exception.*;
import android.app.*;

import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.utils.*;

import java.lang.reflect.InvocationTargetException;
import java.nio.*;
import com.resmed.refresh.packets.*;
import android.bluetooth.*;
import java.util.*;
import java.io.*;
import android.os.*;
import android.content.*;
import com.resmed.refresh.sleepsession.*;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import java.io.IOException;
import java.util.UUID;

import SPlus.SPlusModule;
import v.lucidlink.LL;
import v.lucidlink.MainActivity;
import v.lucidlink.V;

public class BluetoothSetup implements RefreshBluetoothManager
{
	public class ConnectThread extends Thread {
		private BluetoothSocket mmSocket;
		private UUID uuid;

		public ConnectThread(BluetoothDevice device, UUID uuid) {
			this.uuid = uuid;
			Log.d(LOGGER.TAG_BLUETOOTH, "PAIRED DEVICE UUID : " + this.uuid);
			this.mmSocket = makeSocket(device, uuid);
		}

		public void run() {
			setName(getName());
			try {
				Log.d(LOGGER.TAG_PAIR, " CONNECTING TO PAIRED DEVICE ! , discovery cancelled: " + BluetoothSetup.this.cancelDiscovery() + " mmSocket :" + this.mmSocket);
				try {
					this.mmSocket.connect();
				} catch (NullPointerException e) {
					Log.d(LOGGER.TAG_BLUETOOTH, " BluetoothSetup::run() mmSocket : " + this.mmSocket + " whaaaat");
					AppFileLog.addTrace("BluetoothSetup$ConnectThread::run() failed to connect, null pointer : " + e.getMessage());
					e.printStackTrace();
					this.mmSocket = makeFallbackSocket(this.mmSocket);
					this.mmSocket.connect();
				}
				synchronized (BluetoothSetup.this) {
					BluetoothSetup.this.mConnectThread = null;
				}
				if (this.mmSocket != null) {
					Log.d(LOGGER.TAG_PAIR, " CONNECTED TO PAIRED DEVICE : " + this.mmSocket.isConnected());
				}
				BluetoothSetup.this.manageConnection(this.mmSocket);
			} catch (IOException connectException) {
				connectException.printStackTrace();
				AppFileLog.addTrace("BluetoothSetup$ConnectThread failed to connect! reason : " + connectException.getStackTrace());
				try {
					this.mmSocket.close();
				} catch (IOException closeException) {
					closeException.printStackTrace();
				}
				BluetoothSetup.this.setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_BROKEN, false);
				BluetoothSetup.this.manageConnection(null);
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new Error(e1);
			}
		}

		public void cancel() {
			try {
				this.mmSocket.close();
			} catch (IOException e) {
			}
		}

		private BluetoothSocket makeSocket(BluetoothDevice device, UUID uuid) {
			try {
				Log.d(LOGGER.TAG_BLUETOOTH, "Build.VERSION.SDK_INT : " + Build.VERSION.SDK_INT);
				if (Build.VERSION.SDK_INT >= 17) {
					return device.createRfcommSocketToServiceRecord(uuid);
				}
				return (BluetoothSocket) device.getClass().getMethod("createInsecureRfcommSocket", new Class[]{Integer.TYPE}).invoke(device, new Object[]{Integer.valueOf(1)});
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (NoSuchMethodException e2) {
				e2.printStackTrace();
				return null;
			} catch (IllegalAccessException e3) {
				e3.printStackTrace();
				return null;
			} catch (IllegalArgumentException e4) {
				e4.printStackTrace();
				return null;
			} catch (InvocationTargetException e5) {
				e5.printStackTrace();
				return null;
			}
		}

		private BluetoothSocket makeFallbackSocket(BluetoothSocket tmp) throws Exception {
			AppFileLog.addTrace("BluetoothSetup$ConnectThread::makeFallbackSocket()");
			return (BluetoothSocket) tmp.getRemoteDevice().getClass().getMethod("createInsecureRfcommSocket", new Class[]{Integer.TYPE}).invoke(tmp.getRemoteDevice(), new Object[]{Integer.valueOf(1)});
		}
	}

	public class ConnectedThread extends Thread {
		private final int BUF_SIZE = 1024;
		private boolean isConnected = false;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		private final BluetoothSocket mmSocket;

		public ConnectedThread(BluetoothSocket socket) throws IOException {
			this.mmSocket = socket;
			this.mmInStream = socket.getInputStream();
			this.mmOutStream = socket.getOutputStream();
			Log.d(LOGGER.TAG_BLUETOOTH, "Bluetooth ConnectedThread, streams available!");
		}

		public void run() {
			IOException e;
			setName(getName());
			List<Byte> toProcess = new ArrayList();
			byte[] buffer = new byte[BUF_SIZE];
			Log.d(LOGGER.TAG_BLUETOOTH, "bluetooth ConnectedThread, run()!");
			while (true) {
				try {
					if (!this.isConnected) {
						this.mmInStream.available();
						this.isConnected = true;
						BluetoothSetup.this.setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_CONNECTED, true);
						BluetoothSetup.count = 1;
						BluetoothSetup.CancelRepeatingReconnectAlarmWake(LL.main.reactContext);
					}
					Log.d(LOGGER.TAG_BLUETOOTH, " BluetoothSetup$ConnectedThread::run() available bytes : " + this.mmInStream.available());
					int nrBytesRead = this.mmInStream.read(buffer);
					if (nrBytesRead > 0) {
						Log.d(LOGGER.TAG_BLUETOOTH, "bluetooth ConnectedThread::run() bytes read : " + nrBytesRead + " byte array :" + Arrays.toString(buffer));
						AppFileLog.addTrace(" BluetoothSetup$ConnectedThread::run() bytes read : " + nrBytesRead);
						if (nrBytesRead <= BUF_SIZE) {
							int i = 0;
							List<Byte> toProcess2 = toProcess;
							while (i < nrBytesRead) {
								toProcess2.add(Byte.valueOf(buffer[i]));
								if (buffer[i] == (byte) 0) {
									processNewData(toProcess2);
									toProcess2.clear();
									toProcess = new ArrayList();
								} else {
									toProcess = toProcess2;
								}
								i++;
								toProcess2 = toProcess;
							}
							//try {
								buffer = new byte[BUF_SIZE];
								toProcess = toProcess2;
							/*} catch (IOException e2) {
								e = e2;
								toProcess = toProcess2;
							}*/
						} else {
							continue;
						}
					}
				} catch (IOException e3) {
					e = e3;
					break;
				}
			}
			BluetoothSetup.this.setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_BROKEN, true);
			Log.d(LOGGER.TAG_BLUETOOTH, "bluetooth ConnectedThread, IOException :" + e);
			e.printStackTrace();
		}

		private void processNewData(List<Byte> toProcess) {
			if (!toProcess.isEmpty() && toProcess.size() != 1) {
				Log.d(LOGGER.TAG_BLUETOOTH, "processNewData bluetooth data, COBS, : " + Arrays.toString(toProcess.toArray()) + " size : " + toProcess.toArray().length);
				ByteBuffer bBuffer = ByteBuffer.allocate(toProcess.size());
				for (Byte b : toProcess) {
					bBuffer.put(b.byteValue());
				}
				byte[] bCobbed = bBuffer.array();
				Log.d(LOGGER.TAG_BLUETOOTH, " bCobbed len : " + bCobbed.length);
				byte[] bDecoded = COBS.getInstance().decode(bCobbed);
				Log.d(LOGGER.TAG_BLUETOOTH, "processNewData bluetooth data, DECOBBED, : " + Arrays.toString(bDecoded) + " size : " + bDecoded.length);
				if (bDecoded.length > 1) {
					BluetoothSetup.this.handleNewPacket(ByteBuffer.wrap(bDecoded));
				}
			}
		}

		public void write(byte[] bytes) {
			try {
				Log.d(LOGGER.TAG_BLUETOOTH, " bluetooth bytes to write : " + bytes);
				this.mmOutStream.write(bytes);
				this.mmOutStream.flush();
				Log.d(LOGGER.TAG_BLUETOOTH, " bluetooth bytes written : " + bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void cancel() {
			try {
				Log.d(LOGGER.TAG_BLUETOOTH, "closing bluetooth connection! ");
				this.mmSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public class AlarmReconnectReceiver extends BroadcastReceiver {
		private RefreshBluetoothManager bluetoothManager;
		// $FF: synthetic field
		final BluetoothSetup this$0;

		public AlarmReconnectReceiver(BluetoothSetup var1, RefreshBluetoothManager var2) {
			this.this$0 = var1;
			this.bluetoothManager = var2;
		}

		public void onReceive(Context var1, Intent var2) {
			Log.d("com.resmed.refresh.bluetooth.alarm", "BluetoothSetup$AlarmReceiverReconnect::onReceive()");
			if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
				this.bluetoothManager.connectDevice(BluetoothSetup.access$4(this.this$0));
			}
		}
	}

	public class AlarmReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			Log.d("com.resmed.refresh.bluetooth", " BluetoothSetup$AlarmReceiver::onReceive()");
			AppFileLog.addTrace(" BluetoothSetup$AlarmReceiver alarm fired to RECONNECT ! ");
			AppFileLog.addTrace("BluetoothSetup$AlarmReceiver alarm fired to RECONNECT ! Bluetooth Status : " + BluetoothAdapter.getDefaultAdapter().isEnabled());
			((PowerManager)context.getSystemService("power")).newWakeLock(1, "MyWakeLock").acquire(10000L);
			if(BluetoothAdapter.getDefaultAdapter().isEnabled() && !BeDConnectionStatus.getInstance().isSocketConnected()) {
				AppFileLog.addTrace("BluetoothSetup$AlarmReceiver alarm fired to RECONNECT ! Count : " + BluetoothSetup.access$5() + ":isSocketConnected:" + BeDConnectionStatus.getInstance().isSocketConnected());
				BluetoothSetup.count = 1 + BluetoothSetup.access$5();
				if(BluetoothSetup.access$5() < 31) {
					if(BluetoothSetup.access$5() > 20) {
						RegisterRepeatingReconnectAlarmWakeWithTimerReset(context, 3600000L);
					} else if(BluetoothSetup.access$5() > 15) {
						RegisterRepeatingReconnectAlarmWakeWithTimerReset(context, 1800000L);
					} else if(BluetoothSetup.access$5() > 10) {
						RegisterRepeatingReconnectAlarmWakeWithTimerReset(context, 600000L);
					} else if(BluetoothSetup.access$5() > 5) {
						RegisterRepeatingReconnectAlarmWakeWithTimerReset(context, 300000L);
					} else if(BluetoothSetup.access$5() > 1) {
						RegisterRepeatingReconnectAlarmWakeWithTimerReset(context, 60000L);
					}
				}
			} else {
				BluetoothSetup.count = 1;
			}

			context.sendBroadcast(new Intent("BLUETOOTH_ALARM_RECONNECT"));
		}
	}


	// $FF: synthetic method
	static BluetoothDevice access$4(BluetoothSetup var0) {
		return var0.device;
	}

	// $FF: synthetic method
	static int access$5() {
		return count;
	}

	public static final String BLUETOOTH_ALARM_RECONNECT = "BLUETOOTH_ALARM_RECONNECT";
	private static final int BT_RECONNECT_REQUEST_CODE = 6367;
	public static final String BeD_INCOMING_DATA = "RESMED_BED_INCOMING_DATA";
	public static final String BeD_INCOMING_DATA_COBS = "BeD_INCOMING_DATA_COBS";
	public static final String BeD_INCOMING_DATA_EXTRA = "RESMED_BED_INCOMING_DATA_EXTRA";
	public static final String BeD_INCOMING_DATA_TYPE_EXTRA = "RESMED_BED_INCOMING_DATA_TYPE_EXTRA";
	public static final int REQUEST_ENABLE_BT = 1;
	public static int count;
	private BroadcastReceiver alarmWakeReconnectReceiver;
	private BluetoothAdapter bluetoothAdapter;
	public RefreshBluetoothService bluetoothService;
	private BroadcastReceiver bluetoothStateChangesReceiver;
	public BluetoothDevice device;
	private BroadcastReceiver deviceFoundReceiver;
	private BroadcastReceiver devicePairedReceiver;
	public BluetoothSetup.ConnectThread mConnectThread;
	private BluetoothSetup.ConnectedThread mConnectedThread;
	private Thread mReconnectionThread;
	private UUID uuid;

	static {
		BluetoothSetup.count = 1;
	}

	public BluetoothSetup(final RefreshBluetoothService bluetoothService) throws BluetoothNotSupportedException {
		this.uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
		this.bluetoothService = bluetoothService;
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		this.deviceFoundReceiver = new BluetoothDeviceFoundReceiver(this);
		this.devicePairedReceiver = new BluetoothDevicePairedReceiver(this);
		this.bluetoothStateChangesReceiver = new BluetoothStateChangesReceiver(this);
		this.alarmWakeReconnectReceiver = new BluetoothSetup.AlarmReconnectReceiver(this, this);
		if (this.bluetoothAdapter == null) {
			throw new BluetoothNotSupportedException();
		}
	}

	public static void CancelRepeatingReconnectAlarmWake(final Context context) {
		if (context != null) {
			((AlarmManager)context.getSystemService("alarm")).cancel(PendingIntent.getBroadcast(context, 6367, new Intent(context, (Class)BluetoothSetup.AlarmReceiver.class), 0));
		}
	}

	private static void RegisterRepeatingReconnectAlarmWake(final Context context) {
		AppFileLog.addTrace(" BluetoothSetup::RegisterRepeatingReconnectAlarmWake ALARM FOR RECONNECTION to RUN IN : 15000");
		Log.d("com.resmed.refresh.bluetooth", " BluetoothSetup::RegisterRepeatingReconnectAlarmWake AlarmReceiver::context :" + context);
		if (context != null) {
			BluetoothSetup.count = 1;
			((AlarmManager)context.getSystemService("alarm")).set(2, 15000L + SystemClock.elapsedRealtime(), PendingIntent.getBroadcast(context, 6367, new Intent(context, (Class)BluetoothSetup.AlarmReceiver.class), 0));
		}
	}

	private static void RegisterRepeatingReconnectAlarmWakeWithTimerReset(final Context context, final long n) {
		AppFileLog.addTrace(" BluetoothSetup::RegisterRepeatingReconnectAlarmWakeWithTimerReset ALARM FOR RECONNECTION to RUN IN : 15000");
		Log.d("com.resmed.refresh.bluetooth", " BluetoothSetup::RegisterRepeatingReconnectAlarmWakeWithTimerReset AlarmReceiver::context :" + context);
		if (context != null) {
			((AlarmManager)context.getSystemService("alarm")).set(2, n + SystemClock.elapsedRealtime(), PendingIntent.getBroadcast(context, 6367, new Intent(context, (Class)BluetoothSetup.AlarmReceiver.class), 0));
		}
	}

	private void reconnection() {
		synchronized (this) {
			Log.d("com.resmed.refresh.bluetooth", " RECONNECTION : ");
			this.setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_RECONNECTING, true);
			if (this.mReconnectionThread != null) {
				this.mReconnectionThread.interrupt();
				this.mReconnectionThread = null;
			}
			if (this.mConnectThread != null) {
				this.mConnectThread.interrupt();
				this.mConnectThread = null;
			}
			Context context = LL.main.reactContext;
			if (!BeDConnectionStatus.getInstance().isSocketConnected()) {
				AppFileLog.addTrace(("BluetoothSetup$AlarmReceiver alarm fired to RECONNECT ! Count : " + count + ":isSocketConnected:" + BeDConnectionStatus.getInstance().isSocketConnected()));
				if (count >= 31) return;
				if (count > 20) {
					BluetoothSetup.RegisterRepeatingReconnectAlarmWakeWithTimerReset(context, (long)3600000);
				} else if (count > 15) {
					BluetoothSetup.RegisterRepeatingReconnectAlarmWakeWithTimerReset(context, (long)1800000);
				} else if (count > 10) {
					BluetoothSetup.RegisterRepeatingReconnectAlarmWakeWithTimerReset(context, (long)600000);
				} else if (count > 5) {
					BluetoothSetup.RegisterRepeatingReconnectAlarmWakeWithTimerReset(context, (long)300000);
				} else if (count > 1) {
					BluetoothSetup.RegisterRepeatingReconnectAlarmWakeWithTimerReset(context, (long)60000);
				} else {
					BluetoothSetup.RegisterRepeatingReconnectAlarmWake(context);
				}
				count = 1 + count;
			} else {
				count = 1;
			}
			return;
		}
	}


	private void unregisterClientReceivers(final BroadcastReceiver... array) {
		final int length = array.length;
		int i = 0;
		while (i < length) {
			final BroadcastReceiver broadcastReceiver = array[i];
			while (true) {
				try {
					LL.main.reactContext.unregisterReceiver(broadcastReceiver);
					Log.d("com.resmed.refresh.bluetooth", "receivers unregistered");
					++i;
				}
				catch (IllegalArgumentException ex) {
					ex.printStackTrace();
					Log.d("com.resmed.refresh.bluetooth", "receivers unregistered");
					continue;
				}
				finally {
					Log.d("com.resmed.refresh.bluetooth", "receivers unregistered");
				}
				break;
			}
		}
	}

	public void bluetoothOff() {
		if (this.bluetoothAdapter != null) {
			this.bluetoothAdapter.disable();
		}
	}

	public boolean cancelDiscovery() {
		return this.bluetoothAdapter.cancelDiscovery();
	}

	public void cancelReconnection() {
		BluetoothSetup.count = 1;
		CancelRepeatingReconnectAlarmWake(LL.main.reactContext);
	}

	public void connectDevice(final BluetoothDevice device) {
		synchronized (this) {
			Log.d("com.resmed.refresh.bluetooth", "connection status : " + LL.main.connectionState);
			AppFileLog.addTrace("BluetoothSetup::connectDevice() device : " + device + " mConnectThread :" + this.mConnectThread + " mConnectedThread :" + this.mConnectedThread);
			if (this.mConnectThread != null) {
				AppFileLog.addTrace("BluetoothSetup::connectDevice() mConnectThread is alive : " + this.mConnectThread.isAlive());
			}
			if (this.mConnectedThread != null) {
				AppFileLog.addTrace("BluetoothSetup::connectDevice() mConnectedThread is alive : " + this.mConnectedThread.isAlive());
			}
			if ((this.mConnectedThread == null || !this.mConnectedThread.isAlive()) && device != null && (this.mConnectThread == null || !this.mConnectThread.isAlive())) {
				this.device = device;
				if (this.mConnectThread != null && !this.mConnectThread.isAlive()) {
					this.mConnectThread.cancel();
					this.mConnectThread.interrupt();
					this.mConnectThread = null;
				}
				if (this.mConnectedThread != null) {
					this.mConnectedThread.cancel();
					this.mConnectedThread.interrupt();
					this.mConnectedThread = null;
				}
				if (this.mReconnectionThread != null) {
					this.mReconnectionThread.interrupt();
					this.mReconnectionThread = null;
				}
				Log.d("com.resmed.refresh.bluetooth", "BluetoothSetup::connectDevice(device) isPaired : " + this.isDevicePaired(device));
				(this.mConnectThread = new BluetoothSetup.ConnectThread(this.device, this.uuid)).start();
			}
		}
	}

	public void disable() {
		synchronized (this) {
			this.unregisterClientReceivers(this.deviceFoundReceiver, this.devicePairedReceiver, this.bluetoothStateChangesReceiver);
			this.setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_NOT_CONNECTED, true);
			if (this.mConnectThread != null) {
				this.mConnectThread.cancel();
				this.mConnectThread.interrupt();
				this.mConnectThread = null;
			}
			if (this.mConnectedThread != null) {
				this.mConnectedThread.cancel();
				this.mConnectedThread.interrupt();
				this.mConnectedThread = null;
			}
			if (this.mReconnectionThread != null) {
				this.mReconnectionThread.interrupt();
				this.mReconnectionThread = null;
			}
			this.bluetoothAdapter.cancelDiscovery();
		}
	}

	public void discoverResMedDevices(boolean something) {
		V.JavaLog("Looking for resmed devices:" + something);
		synchronized (this) {
			CONNECTION_STATE cONNECTION_STATE;
			CONNECTION_STATE cONNECTION_STATE2;
			Log.d("com.resmed.refresh.bluetooth", ("connection status : " + LL.main.connectionState));
			if (this.mReconnectionThread != null) {
				this.mReconnectionThread.interrupt();
				this.mReconnectionThread = null;
			}
			if (this.mConnectThread != null) {
				this.mConnectThread.cancel();
				this.mConnectThread.interrupt();
				this.mConnectThread = null;
			}
			this.cancelDiscovery();
			Log.d("com.resmed.refresh.bluetooth", (" this.connectionStatus : " + LL.main.connectionState));
			/*if (CONNECTION_STATE.SOCKET_NOT_CONNECTED != this.connectionStatus && (cONNECTION_STATE = CONNECTION_STATE.SOCKET_RECONNECTING) != (cONNECTION_STATE2 = this.connectionStatus)) {
				if (something) return;
			}*/
			Iterator iterator = this.queryPairedDevices().iterator();
			do {
				if (!iterator.hasNext()) {
					this.bluetoothAdapter.startDiscovery();
					break;
				}
				BluetoothDevice bluetoothDevice = (BluetoothDevice)iterator.next();
				Log.d("com.resmed.refresh.bluetooth", ("paired device : " + bluetoothDevice.getName() + " address : " + bluetoothDevice.getAddress()));
				//if (this.device != null && bluetoothDevice.getName().equals(this.device.getName()) && bluetoothDevice.getAddress().equals(this.device.getAddress()) && something) {
					this.connectDevice(bluetoothDevice);
					break;

				/*Intent intent = new Intent("ACTION_ALREADY_PAIRED");
				intent.putExtra("android.bluetooth.device.extra.DEVICE", bluetoothDevice);
				this.bluetoothService.getContext().sendStickyBroadcast(intent);*/
			} while (true);
		}
	}


	public void enable() {
		LL.main.reactContext.registerReceiver(this.deviceFoundReceiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
		LL.main.reactContext.registerReceiver(this.bluetoothStateChangesReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
		LL.main.reactContext.registerReceiver(this.devicePairedReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
		LL.main.reactContext.registerReceiver(this.alarmWakeReconnectReceiver, new IntentFilter("BLUETOOTH_ALARM_RECONNECT"));
	}

	public BluetoothAdapter getBluetoothAdapter() {
		return this.bluetoothAdapter;
	}

	public void handleNewPacket(final ByteBuffer byteBuffer) {
		Log.d("com.resmed.refresh.bluetooth", "BluetoothSetup::handleNewPacket() , decobbed data size : " + byteBuffer.array().length + "to depacktize :" + Arrays.toString(byteBuffer.array()));
		final VLP.VLPacket depacketize = VLP.getInstance().Depacketize(byteBuffer);
		Log.d("com.resmed.refresh.bluetooth", "BluetoothSetup::handleNewPacket() processed new bluetooth data : " + new String(depacketize.buffer) + "packet type : " + depacketize.packetType + " size : " + depacketize.buffer.length);
		if (VLPacketType.PACKET_TYPE_RETURN.ordinal() == depacketize.packetType) {
			Log.d("com.resmed.refresh.bluetooth", VLPacketType.PACKET_TYPE_RETURN + " ordinal : " + VLPacketType.PACKET_TYPE_RETURN.ordinal());
			final Intent intent = new Intent("RESMED_BED_INCOMING_DATA");
			intent.putExtra("RESMED_BED_INCOMING_DATA_TYPE_EXTRA", depacketize.packetType);
			intent.putExtra("RESMED_BED_INCOMING_DATA_EXTRA", depacketize.buffer);
			intent.putExtra("BeD_INCOMING_DATA_COBS", byteBuffer.array());
			LL.main.reactContext.sendOrderedBroadcast(intent, (String)null);
		}
		this.bluetoothService.handlePacket(depacketize);
	}

	public boolean isBluetoothEnabled() {
		return this.bluetoothAdapter.isEnabled();
	}

	public boolean isDevicePaired(final BluetoothDevice bluetoothDevice) {
		if (bluetoothDevice == null) {
			return false;
		}
		final Iterator<BluetoothDevice> iterator = this.queryPairedDevices().iterator();
		while (true) {
			BluetoothDevice bluetoothDevice2;
			do {
				final boolean hasNext = iterator.hasNext();
				final boolean b = false;
				if (!hasNext) {
					Log.d("com.resmed.refresh.bluetooth", " RefreshBluetoothService device : " + bluetoothDevice + " is Paired : " + b);
					return b;
				}
				bluetoothDevice2 = iterator.next();
				Log.d("com.resmed.refresh.bluetooth", "paired device : " + bluetoothDevice2.getName() + " address : " + bluetoothDevice2.getAddress());
			} while (bluetoothDevice == null || !bluetoothDevice2.getName().equals(bluetoothDevice.getName()) || !bluetoothDevice2.getAddress().equals(bluetoothDevice.getAddress()));
			final boolean b = true;
			continue;
		}
	}

	public void manageConnection(BluetoothSocket bluetoothSocket) {
		synchronized (this) {
			Log.d((String)"com.resmed.refresh.bluetooth", " manage connection ! ");
			if (this.mConnectThread != null) {
				this.mConnectThread.cancel();
				this.mConnectThread.interrupt();
				this.mConnectThread = null;
			}
			if (this.mConnectedThread != null) {
				this.mConnectedThread.cancel();
				this.mConnectedThread.interrupt();
				this.mConnectedThread = null;
			}
			if (this.mReconnectionThread != null) {
				this.mReconnectionThread.interrupt();
				this.mReconnectionThread = null;
			}
			if (bluetoothSocket != null) {
				try {
					this.mConnectedThread = new ConnectedThread(bluetoothSocket);
					this.mConnectedThread.start();
				}
				catch (IOException var4_2) {
					var4_2.printStackTrace();
				}
			} else {
				this.setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_RECONNECTING, false);
			}
			return;
		}
	}


	public void pairDevice(final BluetoothDevice bluetoothDevice) {
		if (10 == bluetoothDevice.getBondState()) {
			Log.d("com.resmed.refresh.bluetooth", "bluetooth device bonding");
			bluetoothDevice.createBond();
		}
	}

	public Set<BluetoothDevice> queryPairedDevices() {
		return this.bluetoothAdapter.getBondedDevices();
	}

	public void setConnectionStatusAndNotify(final CONNECTION_STATE newState, final boolean makeStickyBroadcast) {
		synchronized (this) {
			LL.main.connectionState = newState;

			Log.d("com.resmed.refresh.bluetooth", " connection status changed to : " + newState + " current sStatus : " + LL.main.connectionState);
			AppFileLog.addTrace("connection status changed to : " + newState + " current sStatus : " + LL.main.connectionState);

			if (newState == CONNECTION_STATE.BLUETOOTH_ON) {
				BluetoothSetup.count = 1;
			}
			if (newState == CONNECTION_STATE.SOCKET_CONNECTED && LL.main.reactContext != null) {
				BluetoothSetup.count = 1;
			}
			if (makeStickyBroadcast) {
				final Intent intent = new Intent("ACTION_RESMED_CONNECTION_STATUS");
				intent.putExtra("EXTRA_RESMED_CONNECTION_STATE", newState);
				LL.main.reactContext.sendStickyOrderedBroadcast(intent, null, null, -1, null, null);
			}
			if (newState == CONNECTION_STATE.SOCKET_BROKEN || newState == CONNECTION_STATE.BLUETOOTH_ON) {
				this.reconnection();
			}

			if (this.device != null)
				SPlusModule.main.sessionConnector.service.ReactToFoundDevice(this.device);

			if (newState == CONNECTION_STATE.SESSION_OPENED || newState == CONNECTION_STATE.SESSION_OPENING
					|| newState == CONNECTION_STATE.SOCKET_CONNECTED || newState == CONNECTION_STATE.SOCKET_RECONNECTING) {
				final JsonRPC leds = BaseBluetoothActivity.getRpcCommands().leds(LedsState.GREEN);
				if (leds != null) {
					MainActivity.main.sendRpcToBed(leds);
				}
			}

			// handling moved to BaseBluetoothActivity

			/*if (newState == CONNECTION_STATE.SOCKET_CONNECTED) {
				V.Log("YAY1!!!");
				//MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().openSession("c63eb080-a864-11e3-a5e2-0800200c9a66"));
				MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().openSession("c63eb080-a864-11e3-a5e2-000000000009"));
				AddReceivers(); // since this section always gets called, use it to set up the receivers for the second and third steps
			} else if (newState == CONNECTION_STATE.SESSION_OPENING) {
				V.Log("YAY2!!!");
				MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().clearBuffers());
			} else*/

			// session is auto-started
			/*if (newState == CONNECTION_STATE.SESSION_OPENED) {
				V.Log("YAY3!!!");
				//MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().startNightTracking());
				MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().startRealTimeStream());

				/*Timer timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask(){@Override public void run() {
					//SleepSessionManager.this.requestUserSleepState();
					V.Log("Stage:" + SPlusModule.main.baseManager.rm20Manager.getRealTimeSleepState());
					//SPlusModule.main.baseManager.rm20Manager.startRespRateCallbacks(true);

					/*SPlusModule.main.baseManager.stopCalculateAndSendResults();
					SPlusModule.main.baseManager.start(71, 20, 1);*#/
				}}, 10000, 10000);*#/
			}*/
		}
	}
	boolean addedReceivers;
	public void AddReceivers() {
		if (addedReceivers) return;
		addedReceivers = true;

		MainActivity.main.registerReceiver(new BroadcastReceiver() {
			public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
				CONNECTION_STATE state = (CONNECTION_STATE)paramAnonymousIntent.getExtras().get("EXTRA_RESMED_CONNECTION_STATE");
				setConnectionStatusAndNotify(state, false);
			}
		}, new IntentFilter("ACTION_RESMED_CONNECTION_STATUS"));
	}

	public void testStreamData(final SleepSessionManager sleepSessionManager) {
	}

	public void unpairAll(final String s) {
		for (final BluetoothDevice bluetoothDevice : this.queryPairedDevices()) {
			Log.d("com.resmed.refresh.bluetooth", String.valueOf(bluetoothDevice.getName()) + " to unpair  : " + bluetoothDevice.getName());
			if (bluetoothDevice.getName().contains(s)) {
				Log.d("com.resmed.refresh.bluetooth", String.valueOf(bluetoothDevice.getName()) + " was unpaired ? : " + this.unpairDevice(bluetoothDevice));
			}
		}
	}

	public boolean unpairDevice(final BluetoothDevice bluetoothDevice) {
		Boolean value = false;
		try {
			value = (Boolean)bluetoothDevice.getClass().getMethod("removeBond", (Class<?>[])null).invoke(bluetoothDevice, (Object[])null);
			return value;
		}
		catch (Exception ex) {
			Log.d("com.resmed.refresh.bluetooth", ex.getMessage());
			return value;
		}
	}

	public void writeData(final byte[] array) {
		synchronized (this) {
			if (this.mConnectedThread != null) {
				this.mConnectedThread.write(array);
			}
		}
	}
}
