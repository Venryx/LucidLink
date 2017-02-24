package com.resmed.refresh.sleepsession;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;

import com.google.gson.Gson;
import com.resmed.edflib.EdfLibCallbackHandler;
import com.resmed.edflib.FileEdfInterface;
import com.resmed.edflib.RstEdfMetaData;
import com.resmed.edflib.RstEdfMetaData.Enum_EDF_Meta;
import com.resmed.refresh.bed.BedDefaultRPCMapper;
import com.resmed.refresh.bluetooth.RefreshBluetoothService;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.LOGGER;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.rm20.RM20Callbacks;
import com.resmed.rm20.RM20DefaultManager;
import com.resmed.rm20.SleepParams;
import com.resmed.rm20.SmartAlarmInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import SPlus.SPlusModule;
import v.lucidlink.LL;
import v.lucidlink.LucidLinkModule;
import v.lucidlink.MainActivity;
import v.lucidlink.MainApplication;
import v.lucidlink.V;

public class SleepSessionManager implements EdfLibCallbackHandler, RM20Callbacks {
	public static String ParamAlarmFireEpoch = "alarmFireEpoch";
	public static String ParamNumberOfBioSamples = "nrOfBioSamples";
	public static String ParamSParamsJson = "sParamsJson";
	public static String ParamSessionId;
	public static String ParamsecondsElapsed = "secondsElapsed";
	public int alarmFireEpoch = 0;
	//private FileEdfInterface edfManager;
	private String fileName;
	private File filesFolder;
	public boolean isActive = false;
	private long lengthOfSession;
	private int nrOfBioSamples = 0;
	private int nrOfEnvSamples = 0;
	public RM20DefaultManager rm20Manager;
	private List<BioSample> sampleBuffer;
	private RefreshBluetoothService serviceHandler;
	private long sessionId;
	private long startTimeStamp;
	public Context unitTestContext;

	static {
		ParamSessionId = "sessionId";
	}

	public SleepSessionManager(RefreshBluetoothService paramRefreshBluetoothServiceClient) {
		this.serviceHandler = paramRefreshBluetoothServiceClient;
		this.sampleBuffer = new ArrayList();
		this.filesFolder = RefreshTools.getFilesPath();

		/*Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){@Override public void run() {
			//SleepSessionManager.this.requestUserSleepState();
			V.Log("Data2:" + SleepSessionManager.this.rm20Manager.getRealTimeSleepState());
			//SleepSessionManager.this.rm20Manager.startRespRateCallbacks(true);

			SleepSessionManager.this.stopCalculateAndSendResults();
			SleepSessionManager.this.StartSession(71, 20, 1);
		}}, 10000, 10000);*/

		/*SleepSessionConnector connector = new SleepSessionConnector(MainActivity.main, 1, false);
		connector.init(true);*/

		//SleepSessionManager.this.StartSession(71, 20, 1);
	}

	private void didFinishEdfRecovery() {
		long l = this.rm20Manager.getEpochCount() * 30 * 1000;
		this.startTimeStamp = (System.currentTimeMillis() - l);
		Log.d("com.resmed.refresh.bluetooth", " SleepSessionManager::didFinishEdfRecovery() ");
		Message localMessage = new Message();
		localMessage.what = 21;
		this.serviceHandler.sendMessageToClient(localMessage);
	}

	private String fileNameForSessionId(long paramLong) {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
		localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return localSimpleDateFormat.format(localCalendar.getTime()) + "_" + paramLong + ".edf";
	}

	private static String getHexString(int paramInt) {
		String str = Integer.toHexString(paramInt);
		for (paramInt = str.length(); ; paramInt++) {
			if (paramInt >= 4) {
				str = str.toUpperCase();
				return String.valueOf(str.charAt(2)) + str.charAt(3) + str.charAt(0) + str.charAt(1);
			}
			str = "0" + str;
		}
	}

	private void setMetaData(final RstEdfMetaData rstEdfMetaData) {
		rstEdfMetaData.addMetaField(RstEdfMetaData.Enum_EDF_Meta.startDateandTime, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()));
		rstEdfMetaData.addMetaField(RstEdfMetaData.Enum_EDF_Meta.recordingId, new StringBuilder().append(this.sessionId).toString());
		rstEdfMetaData.addMetaField(RstEdfMetaData.Enum_EDF_Meta.unitType, "ReFresh");
		rstEdfMetaData.addMetaField(RstEdfMetaData.Enum_EDF_Meta.autoStop, "no");
		rstEdfMetaData.addMetaField(RstEdfMetaData.Enum_EDF_Meta.rstEdfLibVer, this.rm60Version());
		rstEdfMetaData.addMetaField(RstEdfMetaData.Enum_EDF_Meta.rm20LibVer, this.rm20Version());
	}

	public void stopCalculateAndSendResults() {
		this.rm20Manager.startRespRateCallbacks(false);
		this.rm20Manager.stopAndCalculate();
		SleepParams sParams = this.rm20Manager.resultsForSession();
		if (sParams != null) {
			if (sParams.sleepScore >= MotionEventCompat.ACTION_MASK) {
				sParams.sleepScore = 0;
			}
			if (sParams.timeToSleep >= 65535) {
				sParams.timeToSleep = 0;
			}
			if (sParams.sleepOnset >= 65535) {
				sParams.sleepOnset = 0;
			}
			if (sParams.timeInBed >= 65535) {
				sParams.timeInBed = 0;
			}
			if (sParams.finalWakeTime >= 65535) {
				sParams.finalWakeTime = 0;
			}
			if (sParams.numAwakenings >= 65535) {
				sParams.numAwakenings = 0;
			}
			if (sParams.deepEpochs >= 65535) {
				sParams.deepEpochs = 0;
			}
			if (sParams.lightEpochs >= 65535) {
				sParams.lightEpochs = 0;
			}
			if (sParams.remEpochs >= 65535) {
				sParams.remEpochs = 0;
			}
			if (sParams.wakeEpochs >= 65535) {
				sParams.wakeEpochs = 0;
			}
			if (sParams.waso >= 65535) {
				sParams.waso = 0;
			}
		}

		V.Log("Sleep results: " + (sParams != null ? sParams.toString() : null));

		Message msgSessionEnded = new Message();
		msgSessionEnded.what = RefreshBluetoothService.MessageType.SLEEP_SESSION_STOP;
		Bundle data = new Bundle();
		String sParamsJson = new Gson().toJson(sParams);
		data.putInt(ParamNumberOfBioSamples, this.nrOfBioSamples);
		data.putLong(ParamsecondsElapsed, this.lengthOfSession);
		data.putLong(ParamSessionId, this.sessionId);
		data.putInt(ParamAlarmFireEpoch, this.alarmFireEpoch);
		data.putString(ParamSParamsJson, sParamsJson);
		msgSessionEnded.setData(data);
		Log.d(LOGGER.TAG_FINISH_SESSION, " SleepSessionManager send MSG_SLEEP_SESSION_STOP");
		this.serviceHandler.sendMessageToClient(msgSessionEnded);
	}


	public void addBioData(final byte[] array, final byte b) {
		if (!this.isActive) return;
		final int length = array.length;
		Log.d("com.resmed.refresh.finish", "SleepTrackingFragment::handleBioData() size : " + length);
		Log.d("com.resmed.refresh.ui", "SleepTrackingFragment::handleBioData() decobbed for readBioData : " + Arrays.toString(array));
		final int[][] bioData = PacketsByteValuesReader.readBioData(array, length);
		Log.d("com.resmed.refresh.ui", "SleepTrackingFragment::handleBioData() bioMIData : " + Arrays.toString(bioData[0]));
		Log.d("com.resmed.refresh.ui", "SleepTrackingFragment::handleBioData() bioMQData : " + Arrays.toString(bioData[1]));
		this.addSamplesMiMq(bioData[0], bioData[1]);
	}

	public void addEnvData(final byte[] decobbed, final boolean persistData) {
		if (persistData && this.isActive) {
			final float[] temperatureValues = PacketsByteValuesReader.readTemperatureValues(decobbed);
			final int[] illuminanceValues = PacketsByteValuesReader.readIlluminanceValues(decobbed);
			for (int i = 0; i < illuminanceValues.length; ++i) {
				++this.nrOfEnvSamples;
				Log.d("com.resmed.refresh.env", "SleepSessionManager::handleEnvData() temp[" + i + "]=" + temperatureValues[i] + "  light[" + i + "]=" + illuminanceValues[i]);
				if (this.nrOfEnvSamples % 30 == 0) {
					Log.d("com.resmed.refresh.bluetooth", "SleepSessionManager nrOfEnvSamples = " + this.nrOfEnvSamples + " temp = " + temperatureValues[i] + "  light = " + illuminanceValues[i]);
					final Message message = new Message();
					message.what = RefreshBluetoothService.MessageType.SLEEP_ENV_SAMPLE;
					final Bundle data = new Bundle();
					data.putFloat("tempArray", temperatureValues[i]);
					data.putInt("lightArray", illuminanceValues[i]);
					message.setData(data);
					Log.d("com.resmed.refresh.finish", "Sending temp and light");
					this.serviceHandler.sendMessageToClient(message);
				}
			}
			Log.d("com.resmed.refresh.bluetooth", "nrOfEnvSamples = " + this.nrOfEnvSamples);
		}
	}

	public void addSamplesMiMq(File bulkDataFile) {
		FileNotFoundException e;
		IOException e2;
		Throwable th;
		BufferedReader br = null;
		try {
			BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(bulkDataFile)));
			int n = 0;
			while (true) {
				try {
					String strLine = br2.readLine();
					if (strLine == null) {
						break;
					}
					n++;
					String[] tokens = strLine.trim().split("\\s+");
					int mi = Integer.parseInt(tokens[0]);
					int mq = Integer.parseInt(tokens[1]);
					addSamplesMiMq(new int[]{mi}, new int[]{mq});
				} catch (FileNotFoundException e3) {
					e = e3;
					br = br2;
				} catch (IOException e4) {
					e2 = e4;
					br = br2;
				} catch (Throwable th2) {
					th = th2;
					br = br2;
				}
			}
			if (br2 != null) {
				try {
					br2.close();
					br = br2;
				} catch (IOException e22) {
					e22.printStackTrace();
				} catch (Throwable th3) {
					th = th3;
					br = br2;
					throw new Error(th);
				}
			}
			br = br2;
		} catch (FileNotFoundException e5) {
			e = e5;
			try {
				e.printStackTrace();
				if (br != null) {
					try {
						br.close();
					} catch (IOException e222) {
						e222.printStackTrace();
					} catch (Throwable th4) {
						th = th4;
						throw th;
					}
				}
			} catch (Throwable th5) {
				th = th5;
				if (br != null) {
					try {
						br.close();
					} catch (IOException e2222) {
						e2222.printStackTrace();
					}
				}
				throw new Error(th);
			}
		} /*catch (IOException e6) {
			e6.printStackTrace();
			if (br != null) {
				try {
					br.close();
				} catch (IOException e7) {
					e7.printStackTrace();
				}
			}
		}*/
	}

	/*public void addSamplesMiMq(int[] mi, int[] mq) {
		if (!this.isActive) return;
		int i = 0;
		while (i < mi.length) {
			if (4095 < mi[i] || 4095 < mq[i]) {
				Log.d(LOGGER.TAG_MQMI, "ignoring samples; mi=" + mi[i] + " mq=" + mq[i]);
			} else {
				if (this.rm20Manager != null) {
					//Log.d(LOGGER.TAG_SLEEP_FRAGMENT, " SleepSessionManager::addSamplesMiMq RM20 writing samples!");
					this.rm20Manager.writeSampleData(mi[i], mq[i]);
				}
				this.sampleBuffer.add(new BioSample(mi[i], mq[i]));
				if (this.sampleBuffer.size() >= 16) {
					this.nrOfBioSamples += 16;
					int[] cMiBuf = new int[this.sampleBuffer.size()];
					int[] cMqBuf = new int[this.sampleBuffer.size()];
					int j = 0;
					for (BioSample aSample : this.sampleBuffer) {
						cMiBuf[j] = aSample.getMiValue();
						cMqBuf[j] = aSample.getMqValue();
						String sMI = getHexString(cMiBuf[j]);
						Log.d(LOGGER.TAG_MQMI, new StringBuilder(String.valueOf(this.nrOfBioSamples + j)).append("\tEDF cMiBuf[").append(j).append("]=").append(cMiBuf[j]).append("(").append(sMI).append(")  cMqBuf[").append(j).append("]=").append(cMqBuf[j]).append("(").append(getHexString(cMqBuf[j])).append(")").toString());
						j++;
					}
					this.nrOfBioSamples += 16;
					if (this.edfManager != null) {
						this.edfManager.writeDigitalSamples(cMiBuf, cMqBuf);
					}
					this.sampleBuffer.clear();
				} else {
					continue;
				}
			}
			i++;
		}
	}*/
	/*public void addSamplesMiMq(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
	{
		for (;;)
		{
			int[] buffer1;
			int j;
			try
			{
				boolean bool = this.isActive;
				if (!bool) {
					return;
				}
				int i = 0;
				if (i < paramArrayOfInt1.length)
				{
					if ((4095 < paramArrayOfInt1[i]) || (4095 < paramArrayOfInt2[i]))
					{
						StringBuilder builder = new StringBuilder("ignoring samples; mi=");
						Log.d("com.resmed.refresh.mqmi", paramArrayOfInt1[i] + " mq=" + paramArrayOfInt2[i]);
						i++;
					}
				}
				else {
					continue;
				}
				if (this.rm20Manager != null)
				{
					Log.d("com.resmed.refresh.sleepFragment", " SleepSessionManager::addSamplesMiMq RM20 writting samples!");
					this.rm20Manager.writeSampleData(paramArrayOfInt1[i], paramArrayOfInt2[i]);
				}
				BioSample sample = new BioSample(paramArrayOfInt1[i], paramArrayOfInt2[i]);
				this.sampleBuffer.add(sample);
				if (this.sampleBuffer.size() < 16) {
					continue;
				}
				this.nrOfBioSamples += 16;
				buffer1 = new int[this.sampleBuffer.size()];
				int[] buffer2 = new int[this.sampleBuffer.size()];
				j = 0;
				Iterator localIterator = this.sampleBuffer.iterator();
				if (!localIterator.hasNext())
				{
					this.nrOfBioSamples += 16;
					if (this.edfManager != null) {
						this.edfManager.writeDigitalSamples(buffer1, buffer2);
					}
					this.sampleBuffer.clear();
					continue;
				}
				BioSample sample2 = (BioSample)localIterator.next();

				buffer1[j] = sample2.getMiValue();
				buffer2[j] = sample2.getMqValue();
				String str1 = getHexString(buffer1[j]);
				String str2 = getHexString(buffer2[j]);
				Object localObject2 = new StringBuilder(String.valueOf(this.nrOfBioSamples + j));
				Log.d("com.resmed.refresh.mqmi", "\tEDF cMiBuf[" + j + "]=" + buffer1[j] + "(" + str1 + ")  cMqBuf[" + j + "]=" + buffer2[j] + "(" + str2 + ")");
				j++;
			}
			finally {}
		}
	}*/
	public void addSamplesMiMq(int[] mi, int[] mq) {
		synchronized (this) {
			boolean bl = this.isActive;
			if (!bl) {
				return;
			}
			int n = 0;
			while (n < mi.length) {
				if (4095 < mi[n] || 4095 < mq[n]) {
					Log.d("com.resmed.refresh.mqmi", "ignoring samples; mi=" + mi[n] + " mq=" + mq[n]);
				} else {
					if (this.rm20Manager != null) {
						//Log.d("com.resmed.refresh.sleepFragment", " SleepSessionManager::addSamplesMiMq RM20 writing samples!");
						this.rm20Manager.writeSampleData(mi[n], mq[n]);
					}
					BioSample bioSample = new BioSample(mi[n], mq[n]);
					this.sampleBuffer.add(bioSample);
					if (this.sampleBuffer.size() >= 16) {
						this.nrOfBioSamples = 16 + this.nrOfBioSamples;
						int[] cMiBuf = new int[this.sampleBuffer.size()];
						int[] cMqBuf = new int[this.sampleBuffer.size()];
						int n2 = 0;
						Iterator iterator = this.sampleBuffer.iterator();
						do {
							if (!iterator.hasNext()) {
								this.nrOfBioSamples = 16 + this.nrOfBioSamples;
								/*if (this.edfManager != null) {
									this.edfManager.writeDigitalSamples(cMiBuf, cMqBuf);
								}*/
								this.sampleBuffer.clear();
								break;
							}
							BioSample bioSample2 = (BioSample)iterator.next();
							cMiBuf[n2] = bioSample2.getMiValue();
							cMqBuf[n2] = bioSample2.getMqValue();
							/*String string = SleepSessionManager.getHexString(cMiBuf[n2]);
							String string2 = SleepSessionManager.getHexString(cMqBuf[n2]);
							Log.d("com.resmed.refresh.mqmi", (String.valueOf(n2 + this.nrOfBioSamples)
								+ "\tEDF cMiBuf[" + n2 + "]=" + cMiBuf[n2] + "(" + string + ")  cMqBuf[" + n2 + "]=" + cMqBuf[n2] + "(" + string2 + ")"));*/
							++n2;
						} while (true);
					}
				}
				++n;
			}
		}
	}

	public void onDigitalSamplesRead(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
		for (int i = 0; ; i++) {
			if (i >= paramArrayOfInt2.length) {
				Log.d("com.resmed.refresh.bluetooth", " SleepSessionManager:: ReadEdfFile: " + paramArrayOfInt1.length + " samples");
				return;
			}
			this.nrOfBioSamples += 1;
			this.rm20Manager.writeSampleData(paramArrayOfInt1[i], paramArrayOfInt2[i]);
		}
	}

	public void onFileClosed() {}

	public void onFileCompressed(int paramInt) {
		/*Log.d("com.resmed.refresh.finish", " SleepSessionManager edf onFileCompressed");
		boolean bool = this.edfManager.deleteFile();
		Log.d("com.resmed.refresh.finish", " SleepSessionManager edf file wasDeleted :" + bool);
		stopCalculateAndSendResults();*/
	}

	public void onFileFixed() {}
	public void onFileOpened() {}

	public void onRm20RealTimeSleepState(int sleepState, int epochIndex) {
		Message localMessage = new Message();
		localMessage.what = RefreshBluetoothService.MessageType.SLEEP_USER_SLEEP_STATE;
		Bundle localBundle = new Bundle();
		localBundle.putInt("BUNDLE_SLEEP_STATE", sleepState);
		localBundle.putInt("BUNDLE_SLEEP_EPOCH_INDEX", epochIndex);
		localMessage.setData(localBundle);
		//this.serviceHandler.sendMessageToClient(localMessage);
		SPlusModule.main.sessionConnector.service.sendMessageToClient(localMessage);
	}

	public void onRm20ValidBreathingRate(float rate, int secIndex) {
		V.Log("Got breathing-rate: " + rate + " @secIndex:" + secIndex);
		Message localMessage = new Message();
		localMessage.what = RefreshBluetoothService.MessageType.SLEEP_BREATHING_RATE;
		Bundle localBundle = new Bundle();
		localBundle.putFloat("BUNDLE_BREATHING_RATE", rate);
		localBundle.putInt("BUNDLE_BREATHING_SECINDEX", secIndex);
		localMessage.setData(localBundle);
		//this.serviceHandler.sendMessageToClient(localMessage);
		SPlusModule.main.sessionConnector.service.sendMessageToClient(localMessage);
	}

	public void onWroteDigitalSamples() {}
	public void onWroteMetadata() {}

	/*public void recoverSession(final long sessionId, final int n, final int n2) {
		this.nrOfBioSamples = 0;
		this.sessionId = sessionId;
		this.isActive = true;
		//this.setRM20AlarmTime(new Date(SmartAlarmDataManager.getInstance().getAlarmDateTime()), SmartAlarmDataManager.getInstance().getWindowValue());

		this.rm20Manager = new RM20DefaultManager(this.filesFolder, this, LL.main.reactContext);
		this.rm20Manager.startupLibrary(n, n2);
		this.rm20Manager.startRespRateCallbacks(true);

		Log.d("com.resmed.refresh.bluetooth", " SleepSessionManager::recoverSession(" + sessionId + ")");
		final RstEdfMetaData metaData = new RstEdfMetaData();
		final File fileByName = RefreshTools.findFileByName(this.filesFolder, "_" + sessionId + ".edf");
		if (fileByName != null && sessionId != -1L) {
			this.fileName = fileByName.getName();
			Log.d("com.resmed.refresh.bluetooth", " SleepSessionManager edf file : " + this.fileName);
			AppFileLog.addTrace("Found file to recover" + sessionId + " length : " + fileByName.length());
			this.edfManager = new EdfFileManager(this.filesFolder, this.fileName, metaData.toArray(), this, LL.main.reactContext);
			if (fileByName.length() < 12000L) {
				if (fileByName.delete()) {
					this.edfManager.openFileForMode("w");
				}
			} else {
				this.edfManager.fixEdfFile();
				Log.d("com.resmed.refresh.bluetooth", " SleepSessionManager::preparing to read");
				if (this.edfManager.readDigitalSamples() == 0) {
					this.edfManager.openFileForMode("a");
				} else {
					fileByName.delete();
					this.edfManager.openFileForMode("w");
				}
			}
			this.didFinishEdfRecovery();
			return;
		}
		Log.d("com.resmed.refresh.bluetooth", " SleepSessionManager Failed to find a file for sleep session: : " + sessionId);
		AppFileLog.addTrace("SleepSessionManager Failed to find file to recover - creating new one " + sessionId);
		this.fileName = this.fileNameForSessionId(sessionId);
		this.setMetaData(metaData);
		(this.edfManager = new EdfFileManager(this.filesFolder, this.fileName, metaData.toArray(), this, LL.main.reactContext)).openFileForMode("w");
		this.didFinishEdfRecovery();
	}*/

	public void requestUserSleepState() {
		Log.d("com.resmed.refresh.rm20_callback", " requestUserSleepState");
		if (this.rm20Manager != null) {
			this.rm20Manager.getRealTimeSleepState();
		}
	}

	public String rm20Version() {
		return "0";
	}
	public String rm60Version() {
		/*if (this.edfManager != null) {
			return String.valueOf(this.edfManager.getRM60LibVersion()) + "_" + "1.0.0";
		}*/
		return "0";
	}

	public void StartSession(long sessionId, int age, int gender) {
		if (this.isActive) return;
		Context context;
		this.nrOfBioSamples = 0;
		this.sessionId = sessionId;
		this.isActive = true;
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		new SimpleDateFormat("yyyyMMdd_HHmm").setTimeZone(TimeZone.getTimeZone("UTC"));
		this.startTimeStamp = cal.getTime().getTime();
		this.fileName = fileNameForSessionId(sessionId);
		/*if (!Consts.UNIT_TEST_MODE) {
			this.rm20Alarmtime = new Date(SmartAlarmDataManager.getInstance().getAlarmDateTime());
			this.rm20AlarmWindow = SmartAlarmDataManager.getInstance().getWindowValue();
		}*/
		RstEdfMetaData pMeta = new RstEdfMetaData();
		setMetaData(pMeta);
		if (Consts.UNIT_TEST_MODE) {
			context = this.unitTestContext;
		} else {
			context = LL.main.reactContext;
		}
		this.rm20Manager = new RM20DefaultManager(this.filesFolder, this, context);
		/*this.edfManager = new EdfFileManager(this.filesFolder, this.fileName, pMeta.toArray(), this, context);
		this.edfManager.openFileForMode("w");*/
		this.rm20Manager.startupLibrary(age, gender);
		this.rm20Manager.startRespRateCallbacks(true);

		/*String boardVersion = RefreshModelController.getInstance().getBoardVersion();
		String firmwareVersion = RefreshModelController.getInstance().getFirmwareVersion();
		RefreshModelController.getInstance().saveRM20LibraryVersion(rm20Version());
		Log.d(LOGGER.TAG_BLUETOOTH, "SleepTrackFragment::StartNewSession boardVersion : " + boardVersion + " firmwareVersion:" + firmwareVersion);*/
		pMeta.addMetaField(Enum_EDF_Meta.unitVer, BaseBluetoothActivity.boardVersion);
		pMeta.addMetaField(Enum_EDF_Meta.rstEdfLibVer, BaseBluetoothActivity.firmwareVersion);

		AppFileLog.addTrace("SleepSessionManager::StartSession() battery level : " + RefreshTools.getBatteryLevel(context));
		//RefreshModelController.getInstance().updateLocation();

		// use static user-id different than S+ app's one
		MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().openSession("c63eb080-a864-11e3-a5e2-000000000001"));
	}


	public void stop() {
		Log.d("com.resmed.refresh.finish", " SleepSessionManager isActive : " + this.isActive); // + " edfManager : " + this.edfManager);
		if (!this.isActive) { // || this.edfManager == null) {
			this.stopCalculateAndSendResults();
			return;
		}
		AppFileLog.addTrace("STOP SleepSessionManager in Service");
		AppFileLog.addTrace("SleepSessionManager::stop() battery level : " + RefreshTools.getBatteryLevel(LL.main.reactContext));
		Log.d("com.resmed.refresh.finish", " SleepSessionManager stopped");
		this.lengthOfSession = (System.currentTimeMillis() - this.startTimeStamp) / 1000L;
		Log.d("com.resmed.refresh.finish", " secondsElapsed : " + this.lengthOfSession);
		//this.edfManager.closeFile();
		if (this.nrOfBioSamples < Consts.MIN_SAMPLES_TO_SAVE_RECORD) {
			this.stopCalculateAndSendResults();
			//Log.d("com.resmed.refresh.finish", " edf file isDeleted : " + this.edfManager.deleteFile());
		} else {
			final String string = String.valueOf(this.filesFolder.getAbsolutePath()) + "/" + this.fileName.replace(".edf", ".lz4");
			Log.d("com.resmed.refresh.finish", "compressed File : " + string);
			//this.edfManager.compressFile(string);
		}
		this.isActive = false;
	}

	private class BioSample {
		private int miValue;
		private int mqValue;

		public BioSample(int paramInt1, int paramInt2) {
			this.miValue = paramInt1;
			this.mqValue = paramInt2;
		}

		public int getMiValue() {
			return this.miValue;
		}
		public int getMqValue() {
			return this.mqValue;
		}
	}
}