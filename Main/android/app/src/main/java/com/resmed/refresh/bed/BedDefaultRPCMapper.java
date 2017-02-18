package com.resmed.refresh.bed;

import android.content.Intent;

import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.utils.LOGGER;
import com.resmed.refresh.utils.Log;

import java.util.LinkedHashMap;
import java.util.Map;

import v.lucidlink.LL;

public class BedDefaultRPCMapper implements BedCommandsRPCMapper {
	private static BedDefaultRPCMapper Instance = null;
	private BaseBluetoothActivity btContext;
	private String lastGuid;
	private int rpcId = 115;

	private void broadcastState(CONNECTION_STATE state) {
		Intent localIntent = new Intent("ACTION_RESMED_CONNECTION_STATUS");
		localIntent.putExtra("EXTRA_RESMED_CONNECTION_STATE", state);
		Log.d("com.resmed.refresh.pair", "broadcastState broadcastState : " + CONNECTION_STATE.toString(state));
		this.btContext.sendStickyOrderedBroadcast(localIntent, null, null, -1, null, null);
	}

	private int calculateChecksum(byte[] paramArrayOfByte) {
		int j = 0;
		int k = paramArrayOfByte.length;
		for (int i = 0; ; i++) {
			if (i >= k) {
				return (j ^ 0xFFFFFFFF) + 1;
			}
			j += (paramArrayOfByte[i] & 0xFF);
		}
	}

	public static BedDefaultRPCMapper getInstance() {
		if (Instance == null) {
			Instance = new BedDefaultRPCMapper();
		}
		return Instance;
	}

	public JsonRPC clearBuffers() {
		JsonRPC localJsonRPC = new JsonRPC("clearBuffers", null, this.rpcId);
		if (this.btContext != null) {
			localJsonRPC.setRPCallback(new JsonRPC.RPCallback() {
				public void preExecute() {}
				public void execute() {
					BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SESSION_OPENED);
				}
				public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
			});
		}
		return localJsonRPC;
	}

	public JsonRPC closeSession() {
		JsonRPC localJsonRPC = new JsonRPC("closeSession", null, this.rpcId);
		if (this.btContext != null) {
			localJsonRPC.setRPCallback(new JsonRPC.RPCallback() {
				public void preExecute() {}
				public void execute() {
					BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SOCKET_CONNECTED);
				}
				public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
			});
		}
		return localJsonRPC;
	}

	public JsonRPC fillBuffer(int paramInt) {
		LinkedHashMap localLinkedHashMap = new LinkedHashMap();
		localLinkedHashMap.put("timeInSecs", paramInt);
		return new JsonRPC("fillBuffer", localLinkedHashMap, this.rpcId);
	}

	public JsonRPC getBioSensorSerialNumber() {
		return new JsonRPC("getSerialNumberSensor", null, this.rpcId);
	}

	public JsonRPC getOperationalStatus() {
		return new JsonRPC("getOperationalStatus", null, this.rpcId);
	}

	public int getRPCid() {
		return this.rpcId;
	}

	public JsonRPC getSampleNumber(boolean paramBoolean) {
		LinkedHashMap localLinkedHashMap = new LinkedHashMap();
		String str = "BUF_ENV";
		if (!paramBoolean) {
			str = "BUFF_BIO";
		}
		localLinkedHashMap.put("buffID", str);
		return new JsonRPC("getSampleNumUnsent", localLinkedHashMap, this.rpcId);
	}

	public JsonRPC getSerialNumber() {
		return new JsonRPC("getSerialNumberBeD", null, this.rpcId);
	}

	public JsonRPC leds(LedsState paramLedsState) {
		LinkedHashMap localLinkedHashMap = new LinkedHashMap();
		localLinkedHashMap.put("ledName", "LED_STATUS");
		localLinkedHashMap.put("state", paramLedsState.toString());
		return new JsonRPC("leds", localLinkedHashMap, this.rpcId);
	}

	public JsonRPC openSession(String guid) {
		this.lastGuid = guid;
		Map<String, Object> params = new LinkedHashMap();
		params.put("guid", guid);
		JsonRPC jsonRPC = new JsonRPC("requestSession", params, Integer.valueOf(this.rpcId));
		jsonRPC.setRPCallback(new JsonRPC.RPCallback() {
			public void preExecute() {
				Log.d(LOGGER.TAG_PAIR, "**************** Sending openning when state is " + LL.main.connectionState.toString());
				BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SESSION_OPENING);
			}
			public void execute() {
				BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SESSION_OPENED);
			}
			public void onError(JsonRPC.ErrorRpc errRpc) {
				if (-19 == errRpc.getCode().intValue()) {
					BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SESSION_OPENED);
				}
			}
		});
		return jsonRPC;
	}


	public JsonRPC putSerialNumber(String paramString) {
		LinkedHashMap localLinkedHashMap = new LinkedHashMap();
		localLinkedHashMap.put("serialID", paramString);
		return new JsonRPC("putSerialNumber", localLinkedHashMap, this.rpcId);
	}

	public JsonRPC reset() {
		return new JsonRPC("reset", null, this.rpcId);
	}

	public JsonRPC resetEngineeringMode() {
		return new JsonRPC("resetEngineering", null, this.rpcId);
	}

	public JsonRPC setBioSensorSerialNumber(String paramString) {
		LinkedHashMap localLinkedHashMap = new LinkedHashMap();
		localLinkedHashMap.put("serialSensorID", paramString);
		return new JsonRPC("setSerialNumberSensor", localLinkedHashMap, this.rpcId);
	}

	public void setContextBroadcaster(BaseBluetoothActivity paramBaseBluetoothActivity) {
		this.btContext = paramBaseBluetoothActivity;
	}

	public void setRPCid(int paramInt) {
		this.rpcId = paramInt;
	}

	public JsonRPC startRealTimeStream() {
		final Map map = new LinkedHashMap();
		map.put("src", "REAL");
		map.put("nTicks", 100000);
		JsonRPC startStreamRPC = new JsonRPC("startStream", map, this.rpcId);
		if (this.btContext != null) {
			startStreamRPC.setRPCallback(new JsonRPC.RPCallback() {
				public void preExecute() {}
				public void execute() {
					BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.REAL_STREAM_ON);
				}
				public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {
					if (paramAnonymousErrorRpc == null) return;
					if (-18 == paramAnonymousErrorRpc.getCode()) {
						JsonRPC rpcToDevice = BedDefaultRPCMapper.this.openSession(BedDefaultRPCMapper.this.lastGuid);
						rpcToDevice.setRPCallback(new JsonRPC.RPCallback() {
							public void preExecute() {}
							public void execute() {
								JsonRPC localJsonRPC = new JsonRPC(startStreamRPC.getMethod(), startStreamRPC.getParams(), BedDefaultRPCMapper.this.getRPCid());
								BedDefaultRPCMapper.this.btContext.sendRpcToBed(localJsonRPC);
							}
							public void onError(JsonRPC.ErrorRpc paramAnonymous2ErrorRpc) {}
						});
						BedDefaultRPCMapper.this.btContext.sendRpcToBed(rpcToDevice);
					}
				}
			});
		}
		return startStreamRPC;
	}
	public JsonRPC stopRealTimeStream() {
		JsonRPC localJsonRPC = new JsonRPC("stopStream", null, this.rpcId);
		if (this.btContext != null) {
			localJsonRPC.setRPCallback(new JsonRPC.RPCallback() {
				public void preExecute() {}
				public void execute() {
					BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SESSION_OPENED);
				}
				public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
			});
		}
		return localJsonRPC;
	}

	public JsonRPC startNightTracking() {
		final Map map = new LinkedHashMap();
		map.put("channels", "ALL");
		map.put("src", "REAL");
		map.put("nTicks", 3456000);
		map.put("bandWidth", 8);
		JsonRPC startNightTrackingRPC = new JsonRPC("startSample", map, this.rpcId);
		if (this.btContext != null) {
			startNightTrackingRPC.setRPCallback(new JsonRPC.RPCallback() {
				public void preExecute() {}
				public void execute() {
					BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.NIGHT_TRACK_ON);
				}
				public void onError(JsonRPC.ErrorRpc errorRpc) {
					if (errorRpc == null) return;
					if (-18 == errorRpc.getCode()) {
						JsonRPC rpcToDevice = BedDefaultRPCMapper.this.openSession(BedDefaultRPCMapper.this.lastGuid);
						rpcToDevice.setRPCallback(new JsonRPC.RPCallback() {
							public void execute() {
								JsonRPC localJsonRPC = new JsonRPC(startNightTrackingRPC.getMethod(), startNightTrackingRPC.getParams(), BedDefaultRPCMapper.this.getRPCid());
								BedDefaultRPCMapper.this.btContext.sendRpcToBed(localJsonRPC);
							}
							public void onError(JsonRPC.ErrorRpc paramAnonymous2ErrorRpc) {}
							public void preExecute() {}
						});
						BedDefaultRPCMapper.this.btContext.sendRpcToBed(rpcToDevice);
					} else if (-13 == errorRpc.getCode()) {
						BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.NIGHT_TRACK_ON);
					}
				}
			});
		}
		return startNightTrackingRPC;
	}
	public JsonRPC stopNightTimeTracking() {
		JsonRPC localJsonRPC = new JsonRPC("stopSample", null, this.rpcId);
		if (this.btContext != null) {
			localJsonRPC.setRPCallback(new JsonRPC.RPCallback() {
				public void preExecute() {}
				public void execute() {
					BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SESSION_OPENED);
				}
				public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
			});
		}
		return localJsonRPC;
	}

	public JsonRPC transmitPacket(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
		String str = "BUF_ENV";
		if (!paramBoolean1) {
			str = "BUF_BIO";
		}
		LinkedHashMap localLinkedHashMap = new LinkedHashMap();
		localLinkedHashMap.put("nSamples", paramInt);
		localLinkedHashMap.put("buffID", str);
		str = "transmitPacket";
		if (paramBoolean2) {
			str = "transmitPacketOldest";
		}
		return new JsonRPC(str, localLinkedHashMap, this.rpcId);
	}

	public JsonRPC upgradeFirmware(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean) {
		LinkedHashMap localLinkedHashMap = new LinkedHashMap();
		if (paramInt == 0) {
			localLinkedHashMap.put("src", "image1");
		}
		localLinkedHashMap.put("opt", "upgrade");
		paramInt = -1;
		if (!paramBoolean) {
			paramInt = calculateChecksum(paramArrayOfByte);
		}
		localLinkedHashMap.put("checksum", paramInt);
		if (1 == paramInt) {
			localLinkedHashMap.put("src", "image2");
		}
		return new JsonRPC("putApplication", localLinkedHashMap, this.rpcId);
	}
}