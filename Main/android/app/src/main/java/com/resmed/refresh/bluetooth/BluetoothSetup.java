package com.resmed.refresh.bluetooth;

import com.resmed.cobs.COBS;
import com.resmed.refresh.bed.BedDefaultRPCMapper;
import com.resmed.refresh.bed.LedsState;
import com.resmed.refresh.bluetooth.receivers.*;
import com.resmed.refresh.bluetooth.exception.*;
import android.app.*;

import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.utils.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

import v.lucidlink.LL;
import v.lucidlink.MainActivity;
import v.lucidlink.V;

public class BluetoothSetup implements RefreshBluetoothManager
{
	public class ConnectThread extends Thread {
		private BluetoothSocket mmSocket;
		// $FF: synthetic field
		final BluetoothSetup setup;
		private UUID uuid;

		public ConnectThread(BluetoothSetup var1, BluetoothDevice var2, UUID var3) {
			this.setup = var1;
			this.uuid = var3;
			Log.d("com.resmed.refresh.bluetooth", "PAIRED DEVICE UUID : " + this.uuid);
			this.mmSocket = this.makeSocket(var2, var3);
		}

		private BluetoothSocket makeFallbackSocket(BluetoothSocket var1) throws Exception {
			AppFileLog.addTrace("BluetoothSetup$ConnectThread::makeFallbackSocket()");
			Class var2 = var1.getRemoteDevice().getClass();
			Class[] var3 = new Class[]{Integer.TYPE};
			Method var4 = var2.getMethod("createInsecureRfcommSocket", var3);
			Object[] var5 = new Object[]{Integer.valueOf(1)};
			return (BluetoothSocket)var4.invoke(var1.getRemoteDevice(), var5);
		}

		private BluetoothSocket makeSocket(BluetoothDevice var1, UUID var2) {
			try {
				Log.d("com.resmed.refresh.bluetooth", "Build.VERSION.SDK_INT : " + Build.VERSION.SDK_INT);
				if(Build.VERSION.SDK_INT >= 17) {
					return var1.createRfcommSocketToServiceRecord(var2);
				} else {
					Class var9 = var1.getClass();
					Class[] var10 = new Class[]{Integer.TYPE};
					Method var11 = var9.getMethod("createInsecureRfcommSocket", var10);
					Object[] var12 = new Object[]{Integer.valueOf(1)};
					BluetoothSocket var13 = (BluetoothSocket)var11.invoke(var1, var12);
					return var13;
				}
			} catch (IOException var14) {
				var14.printStackTrace();
				return null;
			} catch (NoSuchMethodException var15) {
				var15.printStackTrace();
				return null;
			} catch (IllegalAccessException var16) {
				var16.printStackTrace();
				return null;
			} catch (IllegalArgumentException var17) {
				var17.printStackTrace();
				return null;
			} catch (InvocationTargetException var18) {
				var18.printStackTrace();
				return null;
			}
		}

		public void cancel() {
			try {
				this.mmSocket.close();
			} catch (IOException var2) {
				;
			}
		}

		public void run() {
			this.setName(this.getName());
			label1:
			try {
				Exception a = null;
				NullPointerException a0 = null;
				boolean b = this.setup.cancelDiscovery();
				com.resmed.refresh.utils.Log.d("com.resmed.refresh.pair", new StringBuilder(" CONNECTING TO PAIRED DEVICE ! , discovery cancelled: ").append(b).append(" mmSocket :").append(this.mmSocket).toString());
				label0:
				try {
					this.mmSocket.connect();
					break label1;
				} catch (NullPointerException a1) {
					com.resmed.refresh.utils.Log.d("com.resmed.refresh.bluetooth", new StringBuilder(" BluetoothSetup::run() mmSocket : ").append((Object) this.mmSocket).append(" whaaaat").toString());
					com.resmed.refresh.utils.AppFileLog.addTrace(new StringBuilder("BluetoothSetup$ConnectThread::run() failed to connect, null pointer : ").append(a1.getMessage()).toString());
					a1.printStackTrace();
					try {
						this.mmSocket = this.makeFallbackSocket(this.mmSocket);
						this.mmSocket.connect();
					} catch (Exception a2) {
						a = a2;
						a0 = a1;
						break label0;
					}
					break label1;
				}
				a.printStackTrace();
				throw new java.io.IOException(a0.getMessage());
			} catch (java.io.IOException a3) {
				a3.printStackTrace();
				com.resmed.refresh.utils.AppFileLog.addTrace(new StringBuilder("BluetoothSetup$ConnectThread failed to connect! reason : ").append(a3.getStackTrace()).toString());
				try {
					this.mmSocket.close();
				} catch (java.io.IOException a4) {
					a4.printStackTrace();
				}
				this.setup.setConnectionStatusAndNotify(com.resmed.refresh.bluetooth.CONNECTION_STATE.SOCKET_BROKEN, false);
				this.setup.manageConnection(null);
				return;
			}

			//com.resmed.refresh.bluetooth.BluetoothSetup a5 = this.setup;
			//monenter(a5);
			try {
				this.setup.mConnectThread = null;
				//monexit(a5);
			} catch (Throwable a6) {
				throw new Error(a6);
			}
			if (this.mmSocket != null) {
				com.resmed.refresh.utils.Log.d("com.resmed.refresh.pair", new StringBuilder(" CONNECTED TO PAIRED DEVICE : ").append(this.mmSocket.isConnected()).toString());
			}
			this.setup.manageConnection(this.mmSocket);
		}

	}

	public class ConnectedThread extends Thread {
		private final int BUF_SIZE;
		private boolean isConnected;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		private final BluetoothSocket mmSocket;
		// $FF: synthetic field
		final BluetoothSetup this$0;

		public ConnectedThread(BluetoothSetup var1, BluetoothSocket var2) throws IOException {
			this.this$0 = var1;
			this.isConnected = false;
			this.BUF_SIZE = 1024;
			this.mmSocket = var2;
			this.mmInStream = var2.getInputStream();
			this.mmOutStream = var2.getOutputStream();
			Log.d("com.resmed.refresh.bluetooth", "Bluetooth ConnectedThread, streams available!");
		}

		private void processNewData(List var1) {
			if(!var1.isEmpty() && var1.size() != 1) {
				Log.d("com.resmed.refresh.bluetooth", "processNewData bluetooth data, COBS, : " + Arrays.toString(var1.toArray()) + " size : " + var1.toArray().length);
				ByteBuffer var3 = ByteBuffer.allocate(var1.size());
				Iterator var4 = var1.iterator();

				while(var4.hasNext()) {
					var3.put(((Byte)var4.next()).byteValue());
				}

				byte[] var6 = var3.array();
				Log.d("com.resmed.refresh.bluetooth", " bCobbed len : " + var6.length);
				byte[] var8 = COBS.getInstance().decode(var6);
				Log.d("com.resmed.refresh.bluetooth", "processNewData bluetooth data, DECOBBED, : " + Arrays.toString(var8) + " size : " + var8.length);
				if(var8.length > 1) {
					ByteBuffer var10 = ByteBuffer.wrap(var8);
					this.this$0.handleNewPacket(var10);
					return;
				}
			}

		}

		public void cancel() {
			try {
				Log.d("com.resmed.refresh.bluetooth", "closing bluetooth connection! ");
				this.mmSocket.close();
			} catch (IOException var2) {
				var2.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				this.setName(this.getName());
				List<Byte> var1_1 = new ArrayList<>();
				byte[] byteBuffer = new byte[1024];
				Log.d("com.resmed.refresh.bluetooth", "bluetooth ConnectedThread, run()!");
				int var9_4 = 0;
				int bytesRead = 0;
				List<Byte> var10_5 = new ArrayList<>();
				block4:
				do {
					block9:
					{
						//try {
						do lbl1000: // 3 sources:
						{
							if (!this.isConnected) {
								this.mmInStream.available();
								this.isConnected = true;
								this.this$0.setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_CONNECTED, true);
								BluetoothSetup.count = 1;
								CancelRepeatingReconnectAlarmWake(this.this$0.bluetoothService.getContext());
							}
							Log.d("com.resmed.refresh.bluetooth", (" BluetoothSetup$ConnectedThread::run() available bytes : " + this.mmInStream.available()));
							bytesRead = this.mmInStream.read(byteBuffer);
							V.Log("After:" + bytesRead);
							if (bytesRead <= 0) {
								try {
									Thread.sleep(10l);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								break lbl1000;
							}
							Log.d("com.resmed.refresh.bluetooth", ("bluetooth ConnectedThread::run() bytes read : " + bytesRead + " byte array :" + Arrays.toString(byteBuffer)));
							AppFileLog.addTrace((" BluetoothSetup$ConnectedThread::run() bytes read : " + bytesRead));
						} while (bytesRead > 1024);
						var9_4 = 0;
						var10_5 = var1_1;
						break block9;
						/*} catch (IOException var4_7) {
						}*/
					}
					do {
						List<Byte> var13_6;
						block10:
						{
							if (var9_4 >= bytesRead) {
								byteBuffer = new byte[1024];
								var1_1 = var10_5;
								continue block4;
							}
							var10_5.add(Byte.valueOf(byteBuffer[var9_4]));
							if (byteBuffer[var9_4] == 0) {
								this.processNewData(var10_5);
								var10_5.clear();
								var13_6 = new ArrayList<>();
								break block10;
							}
							var13_6 = var10_5;
						}
						++var9_4;
						var10_5 = var13_6;
					} while (true);
				} while (true);
			} catch (IOException ex) {
				this.this$0.setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_BROKEN, true);
				Log.d("com.resmed.refresh.bluetooth", ("bluetooth ConnectedThread, IOException :" + ex));
				ex.printStackTrace();
			} finally {
				V.Log("FINISHEDDDDDDDDDDDDDDDDDDDD");
			}
		}

		public void write(byte[] var1) {
			try {
				Log.d("com.resmed.refresh.bluetooth", " bluetooth bytes to write : " + var1);
				this.mmOutStream.write(var1);
				this.mmOutStream.flush();
				Log.d("com.resmed.refresh.bluetooth", " bluetooth bytes written : " + var1);
			} catch (IOException var3) {
				var3.printStackTrace();
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
	public RefreshBluetoothServiceClient bluetoothService;
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

	public BluetoothSetup(final RefreshBluetoothServiceClient bluetoothService) throws BluetoohNotSupportedException {
		this.uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
		this.bluetoothService = bluetoothService;
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		this.deviceFoundReceiver = new BluetoothDeviceFoundReceiver(this);
		this.devicePairedReceiver = new BluetoothDevicePairedReceiver(this);
		this.bluetoothStateChangesReceiver = new BluetoothStateChangesReceiver(this);
		this.alarmWakeReconnectReceiver = new BluetoothSetup.AlarmReconnectReceiver(this, this);
		if (this.bluetoothAdapter == null) {
			throw new BluetoohNotSupportedException();
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
			Context context = this.bluetoothService.getContext().getApplicationContext();
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
					this.bluetoothService.getContext().unregisterReceiver(broadcastReceiver);
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
		CancelRepeatingReconnectAlarmWake(this.bluetoothService.getContext());
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
				(this.mConnectThread = new BluetoothSetup.ConnectThread(this, this.device, this.uuid)).start();
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
		this.bluetoothService.getContext().registerReceiver(this.deviceFoundReceiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
		this.bluetoothService.getContext().registerReceiver(this.bluetoothStateChangesReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
		this.bluetoothService.getContext().registerReceiver(this.devicePairedReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
		this.bluetoothService.getContext().registerReceiver(this.alarmWakeReconnectReceiver, new IntentFilter("BLUETOOTH_ALARM_RECONNECT"));
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
			this.bluetoothService.getContext().sendOrderedBroadcast(intent, (String)null);
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
					this.mConnectedThread = new ConnectedThread(this, bluetoothSocket);
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
			if (newState == CONNECTION_STATE.SOCKET_CONNECTED && this.bluetoothService.getContext() != null) {
				BluetoothSetup.count = 1;
			}
			if (makeStickyBroadcast) {
				final Intent intent = new Intent("ACTION_RESMED_CONNECTION_STATUS");
				intent.putExtra("EXTRA_RESMED_CONNECTION_STATE", newState);
				this.bluetoothService.getContext().sendStickyOrderedBroadcast(intent, null, null, -1, null, null);
			}
			if (newState == CONNECTION_STATE.SOCKET_BROKEN || newState == CONNECTION_STATE.BLUETOOTH_ON) {
				this.reconnection();
			}

			if (this.device != null)
				RefreshBluetoothService.main.ReactToFoundDevice(this.device);

			if (newState == CONNECTION_STATE.SESSION_OPENED || newState == CONNECTION_STATE.SESSION_OPENING
					|| newState == CONNECTION_STATE.SOCKET_CONNECTED || newState == CONNECTION_STATE.SOCKET_RECONNECTING) {
				final JsonRPC leds = BaseBluetoothActivity.getRpcCommands().leds(LedsState.GREEN);
				if (leds != null) {
					MainActivity.main.sendRpcToBed(leds);
				}
			}

			if (newState == CONNECTION_STATE.SOCKET_CONNECTED) {
				V.Log("YAY1!!!");
				//MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().openSession("c63eb080-a864-11e3-a5e2-0800200c9a66"));
				MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().openSession("c63eb080-a864-11e3-a5e2-000000000009"));
				AddReceivers(); // since this section always gets called, use it to set up the receivers for the second and third steps
			} else if (newState == CONNECTION_STATE.SESSION_OPENING) {
				/*V.Log("YAY2!!!");
				MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().clearBuffers());
			} else if (newState == CONNECTION_STATE.SESSION_OPENED) {*/
				V.Log("YAY3!!!");
				MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().startRealTimeStream());
			}
		}
	}
	boolean addedReceivers;
	void AddReceivers() {
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
