package com.resmed.refresh.bluetooth;

import com.resmed.cobs.COBS;
import com.resmed.refresh.bluetooth.receivers.*;
import com.resmed.refresh.bluetooth.exception.*;
import android.app.*;
import com.resmed.refresh.utils.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.*;
import com.resmed.refresh.packets.*;
import android.bluetooth.*;
import java.util.*;
import com.resmed.refresh.ui.uibase.app.*;
import android.preference.*;
import java.io.*;
import android.os.*;
import android.content.*;
import com.resmed.refresh.sleepsession.*;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build.VERSION;
import com.resmed.refresh.bluetooth.BluetoothSetup;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class BluetoothSetup implements RefreshBluetoothManager
{
	public class ConnectThread extends Thread {
		private BluetoothSocket mmSocket;
		// $FF: synthetic field
		final BluetoothSetup this$0;
		private UUID uuid;

		public ConnectThread(BluetoothSetup var1, BluetoothDevice var2, UUID var3) {
			this.this$0 = var1;
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
			// $FF: Couldn't be decompiled
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

		public void run() {
			// $FF: Couldn't be decompiled
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
		public void onReceive(Context var1, Intent var2) {
			Log.d("com.resmed.refresh.bluetooth", " BluetoothSetup$AlarmReceiver::onReceive()");
			AppFileLog.addTrace(" BluetoothSetup$AlarmReceiver alarm fired to RECONNECT ! ");
			AppFileLog.addTrace("BluetoothSetup$AlarmReceiver alarm fired to RECONNECT ! Bluetooth Status : " + BluetoothAdapter.getDefaultAdapter().isEnabled());
			((PowerManager)var1.getSystemService("power")).newWakeLock(1, "MyWakeLock").acquire(10000L);
			if(BluetoothAdapter.getDefaultAdapter().isEnabled() && !BeDConnectionStatus.getInstance().isSocketConnected()) {
				AppFileLog.addTrace("BluetoothSetup$AlarmReceiver alarm fired to RECONNECT ! Count : " + BluetoothSetup.access$5() + ":isSocketConnected:" + BeDConnectionStatus.getInstance().isSocketConnected());
				BluetoothSetup.access$1(1 + BluetoothSetup.access$5());
				if(BluetoothSetup.access$5() < 31) {
					if(BluetoothSetup.access$5() > 20) {
						BluetoothSetup.access$6(var1, 3600000L);
					} else if(BluetoothSetup.access$5() > 15) {
						BluetoothSetup.access$6(var1, 1800000L);
					} else if(BluetoothSetup.access$5() > 10) {
						BluetoothSetup.access$6(var1, 600000L);
					} else if(BluetoothSetup.access$5() > 5) {
						BluetoothSetup.access$6(var1, 300000L);
					} else if(BluetoothSetup.access$5() > 1) {
						BluetoothSetup.access$6(var1, 60000L);
					}
				}
			} else {
				BluetoothSetup.access$1(1);
			}

			var1.sendBroadcast(new Intent("BLUETOOTH_ALARM_RECONNECT"));
		}
	}


	// $FF: synthetic method
	static void access$0(BluetoothSetup var0, ConnectThread var1) {
		var0.mConnectThread = var1;
	}

	// $FF: synthetic method
	static void access$1(int var0) {
		count = var0;
	}

	// $FF: synthetic method
	static RefreshBluetoothServiceClient access$2(BluetoothSetup var0) {
		return var0.bluetoothService;
	}

	// $FF: synthetic method
	static void access$3(Context var0) {
		CancelRepeatingReconnectAlarmWake(var0);
	}

	// $FF: synthetic method
	static BluetoothDevice access$4(BluetoothSetup var0) {
		return var0.device;
	}

	// $FF: synthetic method
	static int access$5() {
		return count;
	}

	// $FF: synthetic method
	static void access$6(Context var0, long var1) {
		RegisterRepeatingReconnectAlarmWakeWithTimerReset(var0, var1);
	}



	public static final String BLUETOOTH_ALARM_RECONNECT = "BLUETOOTH_ALARM_RECONNECT";
	private static final int BT_RECONNECT_REQUEST_CODE = 6367;
	public static final String BeD_INCOMING_DATA = "RESMED_BED_INCOMING_DATA";
	public static final String BeD_INCOMING_DATA_COBS = "BeD_INCOMING_DATA_COBS";
	public static final String BeD_INCOMING_DATA_EXTRA = "RESMED_BED_INCOMING_DATA_EXTRA";
	public static final String BeD_INCOMING_DATA_TYPE_EXTRA = "RESMED_BED_INCOMING_DATA_TYPE_EXTRA";
	public static final int REQUEST_ENABLE_BT = 1;
	private static int count;
	private BroadcastReceiver alarmWakeReconnectReceiver;
	private BluetoothAdapter bluetoothAdapter;
	private RefreshBluetoothServiceClient bluetoothService;
	private BroadcastReceiver bluetoothStateChangesReceiver;
	private CONNECTION_STATE connectionStatus;
	private BluetoothDevice device;
	private BroadcastReceiver deviceFoundReceiver;
	private BroadcastReceiver devicePairedReceiver;
	private BluetoothSetup.ConnectThread mConnectThread;
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
		this.connectionStatus = CONNECTION_STATE.SOCKET_NOT_CONNECTED;
		this.deviceFoundReceiver = (BroadcastReceiver)new BluetoothDeviceFoundReceiver((RefreshBluetoothManager)this);
		this.devicePairedReceiver = (BroadcastReceiver)new BluetoothDevicePairedReceiver((RefreshBluetoothManager)this);
		this.bluetoothStateChangesReceiver = (BroadcastReceiver)new BluetoothStateChangesReceiver((RefreshBluetoothManager)this);
		this.alarmWakeReconnectReceiver = (BroadcastReceiver)new BluetoothSetup.AlarmReconnectReceiver(this, (RefreshBluetoothManager)this);
		if (this.bluetoothAdapter == null) {
			throw new BluetoohNotSupportedException();
		}
	}

	private static void CancelRepeatingReconnectAlarmWake(final Context context) {
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
		while (true) {
			Label_0230:
			while (true) {
				final Context applicationContext;
				Label_0171: {
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
						applicationContext = this.bluetoothService.getContext().getApplicationContext();
						if (!BeDConnectionStatus.getInstance().isSocketConnected()) {
							AppFileLog.addTrace("BluetoothSetup$AlarmReceiver alarm fired to RECONNECT ! Count : " + BluetoothSetup.count + ":isSocketConnected:" + BeDConnectionStatus.getInstance().isSocketConnected());
							if (BluetoothSetup.count < 31) {
								if (BluetoothSetup.count > 20) {
									RegisterRepeatingReconnectAlarmWakeWithTimerReset(applicationContext, 3600000L);
								}
								else {
									if (BluetoothSetup.count <= 15) {
										break Label_0171;
									}
									RegisterRepeatingReconnectAlarmWakeWithTimerReset(applicationContext, 1800000L);
								}
								++BluetoothSetup.count;
							}
							return;
						}
						break Label_0230;
					}
				}
				if (BluetoothSetup.count > 10) {
					RegisterRepeatingReconnectAlarmWakeWithTimerReset(applicationContext, 600000L);
					continue;
				}
				if (BluetoothSetup.count > 5) {
					RegisterRepeatingReconnectAlarmWakeWithTimerReset(applicationContext, 300000L);
					continue;
				}
				if (BluetoothSetup.count > 1) {
					RegisterRepeatingReconnectAlarmWakeWithTimerReset(applicationContext, 60000L);
					continue;
				}
				RegisterRepeatingReconnectAlarmWake(applicationContext);
				continue;
			}
			BluetoothSetup.count = 1;
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
			Log.d("com.resmed.refresh.bluetooth", "connection status : " + this.connectionStatus);
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
			this.setConnectionStatusAndNotify(this.connectionStatus = CONNECTION_STATE.SOCKET_NOT_CONNECTED, true);
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

	public void discoverResMedDevices(final boolean b) {
		while (true) {
			while (true) {
				final Iterator<BluetoothDevice> iterator;
				Label_0176: {
					synchronized (this) {
						Log.d("com.resmed.refresh.bluetooth", "connection status : " + this.connectionStatus);
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
						Log.d("com.resmed.refresh.bluetooth", " this.connectionStatus : " + this.connectionStatus);
						if (CONNECTION_STATE.SOCKET_NOT_CONNECTED == this.connectionStatus || CONNECTION_STATE.SOCKET_RECONNECTING == this.connectionStatus || !b) {
							iterator = this.queryPairedDevices().iterator();
							if (iterator.hasNext()) {
								break Label_0176;
							}
							this.bluetoothAdapter.startDiscovery();
						}
						return;
					}
				}
				final BluetoothDevice bluetoothDevice = iterator.next();
				Log.d("com.resmed.refresh.bluetooth", "paired device : " + bluetoothDevice.getName() + " address : " + bluetoothDevice.getAddress());
				if (this.device != null && bluetoothDevice.getName().equals(this.device.getName()) && bluetoothDevice.getAddress().equals(this.device.getAddress()) && b) {
					this.connectDevice(bluetoothDevice);
					return;
				}
				final Intent intent = new Intent("ACTION_ALREADY_PAIRED");
				intent.putExtra("android.bluetooth.device.extra.DEVICE", (Parcelable)bluetoothDevice);
				this.bluetoothService.getContext().sendStickyBroadcast(intent);
				continue;
			}
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

	public CONNECTION_STATE getConnectionStatus() {
		synchronized (this) {
			Log.e("com.resmed.refresh.bluetooth", " connection status in getConnectionStatus: " + this.connectionStatus);
			return this.connectionStatus;
		}
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

	public void manageConnection(final BluetoothSocket bluetoothSocket) {
		while (true) {
			synchronized (this) {
				Log.d("com.resmed.refresh.bluetooth", " manage connection ! ");
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
						(this.mConnectedThread = new BluetoothSetup.ConnectedThread(this, bluetoothSocket)).start();
						return;
					}
					catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
			this.setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_RECONNECTING, false);
		}
	}

	public void pairDevice(final BluetoothDevice bluetoothDevice) {
		if (10 == bluetoothDevice.getBondState()) {
			Log.d("com.resmed.refresh.bluetooth", "bluetooth device bonding");
			bluetoothDevice.createBond();
		}
	}

	public Set<BluetoothDevice> queryPairedDevices() {
		return (Set<BluetoothDevice>)this.bluetoothAdapter.getBondedDevices();
	}

	public void setConnectionStatusAndNotify(final CONNECTION_STATE connection_STATE, final boolean b) {
		synchronized (this) {
			Log.d("com.resmed.refresh.bluetooth", " connection status changed to : " + connection_STATE + " current sStatus : " + this.connectionStatus);
			AppFileLog.addTrace(" connection status changed to : " + connection_STATE + " current sStatus : " + this.connectionStatus);
			Log.e("com.resmed.refresh.bluetooth", " connection status in persistant store: " + BeDConnectionStatus.getInstance().getState());
			final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)RefreshApplication.getInstance());
			int n;
			if (connection_STATE == CONNECTION_STATE.SOCKET_BROKEN || connection_STATE == CONNECTION_STATE.BLUETOOTH_ON) {
				AppFileLog.addTrace("setConnectionStatusAndNotify Night track ON : " + defaultSharedPreferences.getInt("PREF_CONNECTION_STATE", -1));
				n = 1;
				this.connectionStatus = connection_STATE;
			}
			else {
				this.connectionStatus = connection_STATE;
				n = 0;
			}
			if (connection_STATE == CONNECTION_STATE.BLUETOOTH_ON) {
				BluetoothSetup.count = 1;
			}
			if (connection_STATE == CONNECTION_STATE.SOCKET_CONNECTED && this.bluetoothService.getContext() != null) {
				BluetoothSetup.count = 1;
			}
			if (b) {
				final Intent intent = new Intent("ACTION_RESMED_CONNECTION_STATUS");
				intent.putExtra("EXTRA_RESMED_CONNECTION_STATE", (Serializable)connection_STATE);
				this.bluetoothService.getContext().sendStickyOrderedBroadcast(intent, (BroadcastReceiver)null, (Handler)null, -1, (String)null, (Bundle)null);
			}
			if (n != 0) {
				this.reconnection();
			}
		}
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
