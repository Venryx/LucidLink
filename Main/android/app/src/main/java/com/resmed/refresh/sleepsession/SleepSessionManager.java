package com.resmed.refresh.sleepsession;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.google.gson.Gson;
import com.resmed.edflib.EdfFileManager;
import com.resmed.edflib.EdfLibCallbackHandler;
import com.resmed.edflib.FileEdfInterface;
import com.resmed.edflib.RstEdfMetaData;
import com.resmed.edflib.RstEdfMetaData.Enum_EDF_Meta;
import com.resmed.refresh.bluetooth.RefreshBluetoothService;
import com.resmed.refresh.bluetooth.RefreshBluetoothServiceClient;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.rm20.RM20Callbacks;
import com.resmed.rm20.RM20DefaultManager;
import com.resmed.rm20.RM20Manager;
import com.resmed.rm20.SleepParams;
import com.resmed.rm20.SmartAlarmInfo;

import java.io.File;
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
	private FileEdfInterface edfManager;
	private String fileName;
	private File filesFolder;
	public boolean isActive = false;
	private long lengthOfSession;
	private int nrOfBioSamples = 0;
	private int nrOfEnvSamples = 0;
	private int rm20AlarmWindow;
	private Date rm20Alarmtime;
	public RM20DefaultManager rm20Manager;
	private List<BioSample> sampleBuffer;
	private RefreshBluetoothServiceClient serviceHandler;
	private long sessionId;
	private long startTimeStamp;
	public Context unitTestContext;

	static {
		ParamSessionId = "sessionId";
	}

	public SleepSessionManager(RefreshBluetoothServiceClient paramRefreshBluetoothServiceClient) {
		this.serviceHandler = paramRefreshBluetoothServiceClient;
		this.sampleBuffer = new ArrayList();
		this.filesFolder = RefreshTools.getFilesPath();

		/*Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){@Override public void run() {
			//SleepSessionManager.this.requestUserSleepState();
			V.Log("Data2:" + SleepSessionManager.this.rm20Manager.getRealTimeSleepState());
			//SleepSessionManager.this.rm20Manager.startRespRateCallbacks(true);

			SleepSessionManager.this.stopCalculateAndSendResults();
			SleepSessionManager.this.start(71, 20, 1);
		}}, 10000, 10000);*/

		/*SleepSessionConnector connector = new SleepSessionConnector(MainActivity.main, 1, false);
		connector.init(true);*/

		//SleepSessionManager.this.start(71, 20, 1);
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

	private boolean isValidWindow() {
		long l2 = Calendar.getInstance().getTimeInMillis();
		long l1 = this.rm20Alarmtime.getTime();
		long l3 = this.rm20AlarmWindow * 60 * 1000;
		if ((l2 < this.rm20Alarmtime.getTime()) && (l2 > l1 - l3)) {
		}
		for (boolean bool = true; ; bool = false) {
			return bool;
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
		Object localObject = this.rm20Manager.resultsForSession();
		if (localObject != null) {
			if (((SleepParams) localObject).sleepScore >= 255) {
				((SleepParams) localObject).sleepScore = 0;
			}
			if (((SleepParams) localObject).timeToSleep >= 65535) {
				((SleepParams) localObject).timeToSleep = 0;
			}
			if (((SleepParams) localObject).sleepOnset >= 65535) {
				((SleepParams) localObject).sleepOnset = 0;
			}
			if (((SleepParams) localObject).timeInBed >= 65535) {
				((SleepParams) localObject).timeInBed = 0;
			}
			if (((SleepParams) localObject).finalWakeTime >= 65535) {
				((SleepParams) localObject).finalWakeTime = 0;
			}
			if (((SleepParams) localObject).numAwakenings >= 65535) {
				((SleepParams) localObject).numAwakenings = 0;
			}
			if (((SleepParams) localObject).deepEpochs >= 65535) {
				((SleepParams) localObject).deepEpochs = 0;
			}
			if (((SleepParams) localObject).lightEpochs >= 65535) {
				((SleepParams) localObject).lightEpochs = 0;
			}
			if (((SleepParams) localObject).remEpochs >= 65535) {
				((SleepParams) localObject).remEpochs = 0;
			}
			if (((SleepParams) localObject).wakeEpochs >= 65535) {
				((SleepParams) localObject).wakeEpochs = 0;
			}
			if (((SleepParams) localObject).waso >= 65535) {
				((SleepParams) localObject).waso = 0;
			}
		}
		Message localMessage = new Message();
		localMessage.what = 14;
		Bundle localBundle = new Bundle();
		localObject = new Gson().toJson(localObject);
		localBundle.putInt(ParamNumberOfBioSamples, this.nrOfBioSamples);
		localBundle.putLong(ParamsecondsElapsed, this.lengthOfSession);
		localBundle.putLong(ParamSessionId, this.sessionId);
		localBundle.putInt(ParamAlarmFireEpoch, this.alarmFireEpoch);
		localBundle.putString(ParamSParamsJson, (String) localObject);
		localMessage.setData(localBundle);
		Log.d("com.resmed.refresh.finish", " SleepSessionManager send MSG_SLEEP_SESSION_STOP");
		this.serviceHandler.sendMessageToClient(localMessage);
	}

	public void addBioData(final byte[] array, final byte b) {
		if (!this.isActive) {
			return;
		}
		final int length = array.length;
		Log.d("com.resmed.refresh.finish", "SleepTrackingFragment::handleBioData() size : " + length);
		Log.d("com.resmed.refresh.ui", "SleepTrackingFragment::handleBioData() decobbed for readBioData : " + Arrays.toString(array));
		final int[][] bioData = PacketsByteValuesReader.readBioData(array, length);
		Log.d("com.resmed.refresh.ui", "SleepTrackingFragment::handleBioData() bioMIData : " + Arrays.toString(bioData[0]));
		Log.d("com.resmed.refresh.ui", "SleepTrackingFragment::handleBioData() bioMQData : " + Arrays.toString(bioData[1]));
		this.addSamplesMiMq(bioData[0], bioData[1]);
	}

	public void addEnvData(final byte[] array, final boolean b) {
		if (b && this.isActive) {
			final float[] temperatureValues = PacketsByteValuesReader.readTemperatureValues(array);
			final int[] illuminanceValues = PacketsByteValuesReader.readIlluminanceValues(array);
			for (int i = 0; i < illuminanceValues.length; ++i) {
				++this.nrOfEnvSamples;
				Log.d("com.resmed.refresh.env", "SleepSessionManager::handleEnvData() temp[" + i + "]=" + temperatureValues[i] + "  light[" + i + "]=" + illuminanceValues[i]);
				if (this.nrOfEnvSamples % 30 == 0) {
					Log.d("com.resmed.refresh.bluetooth", "SleepSessionManager nrOfEnvSamples = " + this.nrOfEnvSamples + " temp = " + temperatureValues[i] + "  light = " + illuminanceValues[i]);
					final Message message = new Message();
					message.what = 15;
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

	/* Error */
	public void addSamplesMiMq(File paramFile) {
		// Byte code:
		//   0: aload_0
		//   1: monitorenter
		//   2: aconst_null
		//   3: astore 7
		//   5: aconst_null
		//   6: astore 6
		//   8: aconst_null
		//   9: astore 8
		//   11: aload 6
		//   13: astore 5
		//   15: new 431	java/io/FileInputStream
		//   18: astore 9
		//   20: aload 6
		//   22: astore 5
		//   24: aload 9
		//   26: aload_1
		//   27: invokespecial 433	java/io/FileInputStream:<init>	(Ljava/io/File;)V
		//   30: aload 6
		//   32: astore 5
		//   34: new 435	java/io/BufferedReader
		//   37: astore_1
		//   38: aload 6
		//   40: astore 5
		//   42: new 437	java/io/InputStreamReader
		//   45: astore 10
		//   47: aload 6
		//   49: astore 5
		//   51: aload 10
		//   53: aload 9
		//   55: invokespecial 440	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
		//   58: aload 6
		//   60: astore 5
		//   62: aload_1
		//   63: aload 10
		//   65: invokespecial 443	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
		//   68: iconst_0
		//   69: istore_2
		//   70: aload_1
		//   71: invokevirtual 446	java/io/BufferedReader:readLine	()Ljava/lang/String;
		//   74: astore 5
		//   76: aload 5
		//   78: ifnonnull +14 -> 92
		//   81: aload_1
		//   82: ifnull +163 -> 245
		//   85: aload_1
		//   86: invokevirtual 449	java/io/BufferedReader:close	()V
		//   89: aload_0
		//   90: monitorexit
		//   91: return
		//   92: iinc 2 1
		//   95: aload 5
		//   97: invokevirtual 452	java/lang/String:trim	()Ljava/lang/String;
		//   100: ldc_w 454
		//   103: invokevirtual 458	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
		//   106: astore 5
		//   108: aload 5
		//   110: iconst_0
		//   111: aaload
		//   112: invokestatic 462	java/lang/Integer:parseInt	(Ljava/lang/String;)I
		//   115: istore_3
		//   116: aload 5
		//   118: iconst_1
		//   119: aaload
		//   120: invokestatic 462	java/lang/Integer:parseInt	(Ljava/lang/String;)I
		//   123: istore 4
		//   125: aload_0
		//   126: iconst_1
		//   127: newarray <illegal type>
		//   129: dup
		//   130: iconst_0
		//   131: iload_3
		//   132: iastore
		//   133: iconst_1
		//   134: newarray <illegal type>
		//   136: dup
		//   137: iconst_0
		//   138: iload 4
		//   140: iastore
		//   141: invokevirtual 385	com/resmed/refresh/sleepsession/SleepSessionManager:addSamplesMiMq	([I[I)V
		//   144: goto -74 -> 70
		//   147: astore 6
		//   149: aload_1
		//   150: astore 5
		//   152: aload 6
		//   154: invokevirtual 465	java/io/FileNotFoundException:printStackTrace	()V
		//   157: aload_1
		//   158: ifnull -69 -> 89
		//   161: aload_1
		//   162: invokevirtual 449	java/io/BufferedReader:close	()V
		//   165: goto -76 -> 89
		//   168: astore_1
		//   169: aload_1
		//   170: invokevirtual 466	java/io/IOException:printStackTrace	()V
		//   173: goto -84 -> 89
		//   176: astore_1
		//   177: aload_0
		//   178: monitorexit
		//   179: aload_1
		//   180: athrow
		//   181: astore 6
		//   183: aload 7
		//   185: astore_1
		//   186: aload_1
		//   187: astore 5
		//   189: aload 6
		//   191: invokevirtual 466	java/io/IOException:printStackTrace	()V
		//   194: aload_1
		//   195: ifnull -106 -> 89
		//   198: aload_1
		//   199: invokevirtual 449	java/io/BufferedReader:close	()V
		//   202: goto -113 -> 89
		//   205: astore_1
		//   206: aload_1
		//   207: invokevirtual 466	java/io/IOException:printStackTrace	()V
		//   210: goto -121 -> 89
		//   213: astore_1
		//   214: aload 5
		//   216: astore 6
		//   218: aload 6
		//   220: ifnull +8 -> 228
		//   223: aload 6
		//   225: invokevirtual 449	java/io/BufferedReader:close	()V
		//   228: aload_1
		//   229: athrow
		//   230: astore 5
		//   232: aload 5
		//   234: invokevirtual 466	java/io/IOException:printStackTrace	()V
		//   237: goto -9 -> 228
		//   240: astore_1
		//   241: aload_1
		//   242: invokevirtual 466	java/io/IOException:printStackTrace	()V
		//   245: goto -156 -> 89
		//   248: astore 5
		//   250: aload_1
		//   251: astore 6
		//   253: aload 5
		//   255: astore_1
		//   256: goto -38 -> 218
		//   259: astore 5
		//   261: aload 5
		//   263: astore 6
		//   265: goto -79 -> 186
		//   268: astore 6
		//   270: aload 8
		//   272: astore_1
		//   273: goto -124 -> 149
		//   276: astore_1
		//   277: goto -100 -> 177
		// Local variable table:
		//   start	length	slot	name	signature
		//   0	280	0	this	SleepSessionManager
		//   0	280	1	paramFile	File
		//   69	24	2	i	int
		//   115	17	3	j	int
		//   123	16	4	k	int
		//   13	202	5	localObject1	Object
		//   230	3	5	localIOException1	java.io.IOException
		//   248	6	5	localObject2	Object
		//   259	3	5	localIOException2	java.io.IOException
		//   6	53	6	localObject3	Object
		//   147	6	6	localFileNotFoundException1	java.io.FileNotFoundException
		//   181	9	6	localIOException3	java.io.IOException
		//   216	48	6	localObject4	Object
		//   268	1	6	localFileNotFoundException2	java.io.FileNotFoundException
		//   3	181	7	localObject5	Object
		//   9	262	8	localObject6	Object
		//   18	36	9	localFileInputStream	java.io.FileInputStream
		//   45	19	10	localInputStreamReader	java.io.InputStreamReader
		// Exception table:
		//   from	to	target	type
		//   70	76	147	java/io/FileNotFoundException
		//   95	144	147	java/io/FileNotFoundException
		//   161	165	168	java/io/IOException
		//   161	165	176	finally
		//   169	173	176	finally
		//   198	202	176	finally
		//   206	210	176	finally
		//   223	228	176	finally
		//   228	230	176	finally
		//   232	237	176	finally
		//   15	20	181	java/io/IOException
		//   24	30	181	java/io/IOException
		//   34	38	181	java/io/IOException
		//   42	47	181	java/io/IOException
		//   51	58	181	java/io/IOException
		//   62	68	181	java/io/IOException
		//   198	202	205	java/io/IOException
		//   15	20	213	finally
		//   24	30	213	finally
		//   34	38	213	finally
		//   42	47	213	finally
		//   51	58	213	finally
		//   62	68	213	finally
		//   152	157	213	finally
		//   189	194	213	finally
		//   223	228	230	java/io/IOException
		//   85	89	240	java/io/IOException
		//   70	76	248	finally
		//   95	144	248	finally
		//   70	76	259	java/io/IOException
		//   95	144	259	java/io/IOException
		//   15	20	268	java/io/FileNotFoundException
		//   24	30	268	java/io/FileNotFoundException
		//   34	38	268	java/io/FileNotFoundException
		//   42	47	268	java/io/FileNotFoundException
		//   51	58	268	java/io/FileNotFoundException
		//   62	68	268	java/io/FileNotFoundException
		//   85	89	276	finally
		//   241	245	276	finally
	}

	public void addSamplesMiMq(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
	/*for (;;)
    {
      int[] arrayOfInt;
      int j;
		BioSample localObject2;
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
            Object localObject1 = new java.lang.StringBuilder("ignoring samples; mi=");
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
		  BioSample localObject1 = new BioSample(paramArrayOfInt1[i], paramArrayOfInt2[i]);
        this.sampleBuffer.add(localObject1);
        if (this.sampleBuffer.size() < 16) {
          continue;
        }
        this.nrOfBioSamples += 16;
        arrayOfInt = new int[this.sampleBuffer.size()];
		  int[] array = new int[this.sampleBuffer.size()];
        j = 0;
        Iterator localIterator = this.sampleBuffer.iterator();
        if (!localIterator.hasNext())
        {
          this.nrOfBioSamples += 16;
          if (this.edfManager != null) {
            this.edfManager.writeDigitalSamples(arrayOfInt, (int[])array);
          }
          this.sampleBuffer.clear();
          continue;
        }
		  localObject2 = (BioSample)localIterator.next();
      }
      finally {}
      arrayOfInt[j] = ((BioSample)localObject2).getMiValue();
      localObject1[j] = ((BioSample)localObject2).getMqValue();
      String str1 = getHexString(arrayOfInt[j]);
      String str2 = getHexString(localObject1[j]);
      Object end = new java.lang.StringBuilder(String.valueOf(this.nrOfBioSamples + j));
      Log.d("com.resmed.refresh.mqmi", "\tEDF cMiBuf[" + j + "]=" + arrayOfInt[j] + "(" + str1 + ")  cMqBuf[" + j + "]=" + localObject1[j] + "(" + str2 + ")");
      j++;
    }*/
	}

	public long getSessionId() {
		return this.sessionId;
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

	public void onFileClosed() {
	}

	public void onFileCompressed(int paramInt) {
		Log.d("com.resmed.refresh.finish", " SleepSessionManager edf onFileCompressed");
		boolean bool = this.edfManager.deleteFile();
		Log.d("com.resmed.refresh.finish", " SleepSessionManager edf file wasDeleted :" + bool);
		stopCalculateAndSendResults();
	}

	public void onFileFixed() {
	}

	public void onFileOpened() {
	}

	public void onRm20RealTimeSleepState(int sleepState, int epochIndex) {
		V.Log("Got sleep-state data!" + sleepState + ";" + epochIndex);
		//V.Toast("Got sleep-state data!" + sleepState + ";" + epochIndex);
		Message localMessage = new Message();
		localMessage.what = 17;
		Bundle localBundle = new Bundle();
		localBundle.putInt("BUNDLE_SLEEP_STATE", sleepState);
		localBundle.putInt("BUNDLE_SLEEP_EPOCH_INDEX", epochIndex);
		localMessage.setData(localBundle);
		//this.serviceHandler.sendMessageToClient(localMessage);
		RefreshBluetoothService.main.sendMessageToClient(localMessage);
	}

	public void onRm20ValidBreathingRate(float paramFloat, int paramInt) {
		V.Log("Got breathing-rate data!" + paramFloat + ";" + paramInt);
		Message localMessage = new Message();
		localMessage.what = 18;
		Bundle localBundle = new Bundle();
		localBundle.putFloat("BUNDLE_BREATHING_RATE", paramFloat);
		localBundle.putInt("BUNDLE_BREATHING_SECINDEX", paramInt);
		localMessage.setData(localBundle);
		//this.serviceHandler.sendMessageToClient(localMessage);
		RefreshBluetoothService.main.sendMessageToClient(localMessage);
	}

	public void onWroteDigitalSamples() {
	}

	public void onWroteMetadata() {
	}

	public void recoverSession(final long sessionId, final int n, final int n2) {
		this.nrOfBioSamples = 0;
		this.sessionId = sessionId;
		this.isActive = true;
		//this.setRM20AlarmTime(new Date(SmartAlarmDataManager.getInstance().getAlarmDateTime()), SmartAlarmDataManager.getInstance().getWindowValue());

		this.rm20Manager = new RM20DefaultManager(this.filesFolder, this, this.serviceHandler.getContext().getApplicationContext());
		this.rm20Manager.startupLibrary(n, n2);
		this.rm20Manager.startRespRateCallbacks(true);

		Log.d("com.resmed.refresh.bluetooth", " SleepSessionManager::recoverSession(" + sessionId + ")");
		final RstEdfMetaData metaData = new RstEdfMetaData();
		final File fileByName = RefreshTools.findFileByName(this.filesFolder, "_" + sessionId + ".edf");
		if (fileByName != null && sessionId != -1L) {
			this.fileName = fileByName.getName();
			Log.d("com.resmed.refresh.bluetooth", " SleepSessionManager edf file : " + this.fileName);
			AppFileLog.addTrace("Found file to recover" + sessionId + " length : " + fileByName.length());
			this.edfManager = (FileEdfInterface) new EdfFileManager(this.filesFolder, this.fileName, metaData.toArray(), (EdfLibCallbackHandler) this, this.serviceHandler.getContext().getApplicationContext());
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
		(this.edfManager = (FileEdfInterface) new EdfFileManager(this.filesFolder, this.fileName, metaData.toArray(), (EdfLibCallbackHandler) this, this.serviceHandler.getContext().getApplicationContext())).openFileForMode("w");
		this.didFinishEdfRecovery();
	}

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
		if (this.edfManager != null) {
			return String.valueOf(this.edfManager.getRM60LibVersion()) + "_" + "1.0.0";
		}
		return "0";
	}

	public void setRM20AlarmTime(final Date rm20Alarmtime, final int rm20AlarmWindow) {
		if (this.rm20Manager != null) {
			Log.d("com.resmed.refresh.bluetooth", "Rm20alarm did setAlarmTime, date : " + rm20Alarmtime + " alarm window : " + rm20AlarmWindow);
			Log.d("com.resmed.refresh.smartAlarm", "SleepSessionManager setRM20AlarmTime(" + rm20Alarmtime + "," + rm20AlarmWindow + ")");
			AppFileLog.addTrace("SmartAlarm SleepSessionManager setRM20AlarmTime(" + rm20Alarmtime + "," + rm20AlarmWindow + ")");
			if (rm20AlarmWindow < 0 || rm20Alarmtime.getTime() < System.currentTimeMillis()) {
				AppFileLog.addTrace("SmartAlarm setRM20AlarmTime data not valid");
				Log.d("com.resmed.refresh.smartAlarm", "SmartAlarm setRM20AlarmTime data not valid");
			} else {
				this.rm20Alarmtime = rm20Alarmtime;
				this.rm20AlarmWindow = rm20AlarmWindow;
				this.rm20Manager.setSmartAlarm(rm20Alarmtime, rm20AlarmWindow, true);
				final SmartAlarmInfo smartAlarm = this.rm20Manager.getSmartAlarm();
				if (smartAlarm != null) {
					Log.d("com.resmed.refresh.bluetooth", "Rm20alarm start,end: " + smartAlarm.alarmWinStart + "," + smartAlarm.alarmWinEnd);
					Log.d("com.resmed.refresh.smartAlarm", "SleepSessionManager alarm settings updated during a sleep session OK!");
					AppFileLog.addTrace("SmartAlarm SleepSessionManager alarm settings updated during a sleep session OK!");
					return;
				}
				Log.d("com.resmed.refresh.smartAlarm", "SleepSessionManager alarm settings updated during a sleep session KO!!!");
				AppFileLog.addTrace("SmartAlarm SleepSessionManager alarm settings updated during a sleep session KO!!!!");
			}
		}
	}

	public void start(long sessionId, int age, int gender) {
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
			context = this.serviceHandler.getContext().getApplicationContext();
		}
		this.rm20Manager = new RM20DefaultManager(this.filesFolder, this, context);
		this.edfManager = new EdfFileManager(this.filesFolder, this.fileName, pMeta.toArray(), this, context);
		this.edfManager.openFileForMode("w");
		this.rm20Manager.startupLibrary(age, gender);
		this.rm20Manager.startRespRateCallbacks(true);
		/*SmartAlarmDataManager smartAlarmManager = SmartAlarmDataManager.getInstance();
		if (smartAlarmManager.getActiveAlarm()) {
			setRM20AlarmTime(new Date(smartAlarmManager.getAlarmDateTime()), smartAlarmManager.getWindowValue() * 60);
		}*/
		/*String boardVersion = RefreshModelController.getInstance().getBoardVersion();
		String firmwareVersion = RefreshModelController.getInstance().getFirmwareVersion();
		RefreshModelController.getInstance().saveRM20LibraryVersion(rm20Version());
		Log.d(LOGGER.TAG_BLUETOOTH, "SleepTrackFragment::startSleepSession boardVersion : " + boardVersion + " firmwareVersion:" + firmwareVersion);
		pMeta.addMetaField(Enum_EDF_Meta.unitVer, boardVersion);
		pMeta.addMetaField(Enum_EDF_Meta.rstEdfLibVer, firmwareVersion);*/
		AppFileLog.addTrace("SleepSessionManager::start() battery level : " + RefreshTools.getBatteryLevel(context));
		//RefreshModelController.getInstance().updateLocation();
	}


	public void stop() {
		Log.d("com.resmed.refresh.finish", " SleepSessionManager isActive : " + this.isActive + " edfManager : " + this.edfManager);
		if (!this.isActive || this.edfManager == null) {
			this.stopCalculateAndSendResults();
			return;
		}
		AppFileLog.addTrace("STOP SleepSessionManager in Service");
		AppFileLog.addTrace("SleepSessionManager::stop() battery level : " + RefreshTools.getBatteryLevel(this.serviceHandler.getContext().getApplicationContext()));
		Log.d("com.resmed.refresh.finish", " SleepSessionManager stopped");
		this.lengthOfSession = (System.currentTimeMillis() - this.startTimeStamp) / 1000L;
		Log.d("com.resmed.refresh.finish", " secondsElapsed : " + this.lengthOfSession);
		this.edfManager.closeFile();
		if (this.nrOfBioSamples < Consts.MIN_SAMPLES_TO_SAVE_RECORD) {
			this.stopCalculateAndSendResults();
			Log.d("com.resmed.refresh.finish", " edf file isDeleted : " + this.edfManager.deleteFile());
		} else {
			final String string = String.valueOf(this.filesFolder.getAbsolutePath()) + "/" + this.fileName.replace(".edf", ".lz4");
			Log.d("com.resmed.refresh.finish", "compressed File : " + string);
			this.edfManager.compressFile(string);
		}
		this.isActive = false;
	}

	public void updateAlarmSettings(long paramLong, int paramInt) {
		Log.d("com.resmed.refresh.smartAlarm", "SleepSessionManager updateAlarmSettings");
		if (this.rm20Manager != null) {
			setRM20AlarmTime(new Date(paramLong), paramInt * 60);
		}
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


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */