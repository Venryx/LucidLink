package com.resmed.refresh.model;

		import android.app.*;
		import com.resmed.refresh.sleepsession.*;
		import android.net.*;
		import com.resmed.refresh.ui.uibase.app.*;
		import com.resmed.refresh.utils.*;
		import java.util.*;
		import java.io.*;
		import android.os.*;
		import android.content.*;

public class RefreshSyncService extends Service
{
	class RefreshSyncService$1 implements RST_CallbackItem<RST_Response<Object>> {
		public void onResult(final RST_Response<Object> rst_Response) {
			Log.d("com.resmed.refresh.sync", "uploadRecords service onResult! " + rst_Response.isStatus() + " " + rst_Response.getErrorCode() + " " + rst_Response.getErrorMessage());
			new Thread((Runnable)new RefreshSyncService$1$1()).start();
		}
	}
	class RefreshSyncService$1$1 implements Runnable {
		@Override
		public void run() {
			RefreshSyncService.access$0(access$1(this.this$1));
		}
	}
	class RefreshSyncService$2$1 implements Runnable {
		@Override
		public void run() {
			RefreshSyncService.access$1(access$1(this.this$1));
		}
	}
	class RefreshSyncService$2 implements RST_CallbackItem<RST_Response<RST_UserProfile>> {
		public void onResult(final RST_Response<RST_UserProfile> rst_Response) {
			Log.d("com.resmed.refresh.sync", "uploadProfile service onResult! " + rst_Response.isStatus() + " " + rst_Response.getErrorCode() + " " + rst_Response.getErrorMessage());
			new Thread((Runnable)new RefreshSyncService$2$1(this)).start();
		}
	}
	class RefreshSyncService$3$1 implements Runnable {
		@Override
		public void run() {
			RefreshSyncService.access$2(RefreshSyncService$3.access$1(this.this$1));
		}
	}
	class RefreshSyncService$3 implements RST_CallbackItem<RST_Response<Object>> {
		public void onResult(final RST_Response<Object> rst_Response) {
			Log.d("com.resmed.refresh.sync", "SyncData service onResult! " + rst_Response.isStatus() + " " + rst_Response.getErrorCode() + " " + rst_Response.getErrorMessage());
			new Thread((Runnable)new RefreshSyncService$3$1(this)).start();
		}
	}
	class RefreshSyncService$4 implements RST_CallbackItem<RST_Response<Object>> {
		public void onResult(final RST_Response<Object> rst_Response) {
			Log.d("com.resmed.refresh.sync", "callback service onResult! " + rst_Response.isStatus() + " " + rst_Response.getErrorCode() + " " + rst_Response.getErrorMessage());
		}
	}
	class RefreshSyncService$5 implements Runnable {
		@Override
		public void run() {
			RefreshSyncService.access$3(this.this$0);
		}
	}
	class RefreshSyncService$6 implements FilenameFilter {
		@Override
		public boolean accept(final File file, final String s) {
			return s.contains(".lz4");
		}
	}

	private RST_CallbackItem<RST_Response<Object>> callback;
	private RST_CallbackItem<RST_Response<Object>> syncData;
	private RST_CallbackItem<RST_Response<RST_UserProfile>> uploadFiles;
	private RST_CallbackItem<RST_Response<Object>> uploadProfile;

	public RefreshSyncService() {
		this.uploadProfile = (RST_CallbackItem<RST_Response<Object>>)new RefreshSyncService.RefreshSyncService$1();
		this.uploadFiles = (RST_CallbackItem<RST_Response<RST_UserProfile>>)new RefreshSyncService.RefreshSyncService$2();
		this.syncData = (RST_CallbackItem<RST_Response<Object>>)new RefreshSyncService.RefreshSyncService$3();
		this.callback = (RST_CallbackItem<RST_Response<Object>>)new RefreshSyncService.RefreshSyncService$4();
	}

	private boolean availablebleToConnect() {
		final RefreshModelController instance = RefreshModelController.getInstance();
		final RST_SleepSession instance2 = RST_SleepSession.getInstance();
		final boolean b = !this.isAppInForeground() && instance.isLoggedIn() && instance.getUser() != null && this.checkConnections() && !instance2.isSessionRunning();
		if (!b) {
			String s = "Not avaiable to connect due to";
			if (this.isAppInForeground()) {
				s = String.valueOf(s) + "\tApp in foreground";
			}
			if (!instance.isLoggedIn()) {
				s = String.valueOf(s) + "\tNot LoggedIn";
			}
			if (instance.getUser() == null) {
				s = String.valueOf(s) + "\tUser is null";
			}
			if (!this.checkConnections()) {
				s = String.valueOf(s) + "\tNot connected to Internet";
			}
			if (instance2.isSessionRunning()) {
				s = String.valueOf(s) + "\tSleep session running";
			}
			Log.d("com.resmed.refresh.sync", s);
		}
		return b;
	}

	private boolean checkConnections() {
		try {
			final NetworkInfo activeNetworkInfo = ((ConnectivityManager)this.getSystemService("connectivity")).getActiveNetworkInfo();
			boolean b = false;
			if (activeNetworkInfo != null) {
				final boolean connected = activeNetworkInfo.isConnected();
				b = false;
				if (connected) {
					b = true;
				}
			}
			return b;
		}
		catch (Exception ex) {
			return false;
		}
	}

	private boolean checkWiFiConnection() {
		try {
			final NetworkInfo networkInfo = ((ConnectivityManager)this.getSystemService("connectivity")).getNetworkInfo(1);
			return networkInfo != null && networkInfo.isConnected();
		}
		catch (Exception ex) {
			return false;
		}
	}

	private void executeCallback(final RST_CallbackItem<RST_Response<Object>> rst_CallbackItem) {
		final RST_Response rst_Response = new RST_Response();
		rst_Response.setStatus(false);
		rst_CallbackItem.onResult((Object)rst_Response);
	}

	private boolean isAppInForeground() {
		return RefreshApplication.getInstance().isAppInForeground();
	}

	private void syncData() {
		Log.d("com.resmed.refresh.sync", " syncData synchroniseLatestAll");
		if (this.availablebleToConnect()) {
			RefreshModelController.getInstance().synchroniseLatestAll((RST_CallbackItem)this.callback, true);
		}
		Log.d("com.resmed.refresh.sync", "//////////////////////////////");
	}

	private void uploadFiles() {
		Log.d("com.resmed.refresh.sync", "Uploading files availablebleToConnect=" + this.availablebleToConnect() + " checkWiFiConnection=" + this.checkWiFiConnection());
		if (!this.availablebleToConnect() || !this.checkWiFiConnection()) {
			Log.d("com.resmed.refresh.sync", "uploadFiles not ready");
			this.syncData();
		}
		else {
			final File[] listFiles = RefreshTools.getFilesPath().listFiles((FilenameFilter)new RefreshSyncService.RefreshSyncService$6(this));
			if (listFiles == null || listFiles.length == 0) {
				this.syncData();
				return;
			}
			Log.d("com.resmed.refresh.sync", String.valueOf(listFiles.length) + " files to upload");
			for (int i = 0; i < listFiles.length; ++i) {
				final File file = listFiles[i];
				Log.d("com.resmed.refresh.sync", "File to upload = " + file.getName() + ", date = " + new Date(file.lastModified()) + ", size: " + file.length() + " bytes");
				try {
					final long long1 = Long.parseLong(file.getName().split("_")[2].replace(".lz4", ""));
					Log.d("com.resmed.refresh.sync", "recordId of filename=" + long1);
					final RST_SleepSessionInfo localSleepSessionForId = RefreshModelController.getInstance().localSleepSessionForId(long1);
					if (localSleepSessionForId != null && localSleepSessionForId.getUploaded()) {
						Log.d("com.resmed.refresh.sync", "Uploading file =" + file.getName());
						RST_CallbackItem<RST_Response<Object>> rst_CallbackItem;
						if (i == file.length() - 1L) {
							rst_CallbackItem = this.syncData;
						}
						else {
							rst_CallbackItem = this.callback;
						}
						RefreshModelController.getInstance().serviceUpdateFileRecord(file.getAbsolutePath(), localSleepSessionForId, (RST_CallbackItem)rst_CallbackItem);
					}
					else {
						if (i == file.length() - 1L) {
							this.executeCallback(this.syncData);
						}
						Log.d("com.resmed.refresh.sync", "Record " + long1 + " is not uploaded. Stop upload of file " + file.getName());
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
					this.executeCallback(this.syncData);
				}
			}
		}
	}

	private void uploadProfile() {
		Log.d("com.resmed.refresh.sync", " Upload Profile");
		if (this.availablebleToConnect() && RefreshModelController.getInstance().isProfileUpdatePending()) {
			Log.d("com.resmed.refresh.sync", "Controller.uploadProfile");
			RefreshModelController.getInstance().updateUserProfile((RST_CallbackItem)this.uploadFiles);
			return;
		}
		this.uploadFiles();
	}

	private void uploadRecords() {
		//
		// This method could not be decompiled.
		//
		// Original Bytecode:
		//
		//     0: ldc             "com.resmed.refresh.sync"
		//     2: ldc_w           " RefreshSyncService::uploadRecords() deleting files!"
		//     5: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//     8: pop
		//     9: invokestatic    com/resmed/refresh/utils/RefreshTools.getFilesPath:()Ljava/io/File;
		//    12: astore_2
		//    13: ldc             "com.resmed.refresh.sync"
		//    15: new             Ljava/lang/StringBuilder;
		//    18: dup
		//    19: ldc_w           " RefreshSyncService::uploadRecords() Folder Name : "
		//    22: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//    25: aload_2
		//    26: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
		//    29: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//    32: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//    35: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//    38: pop
		//    39: aload_2
		//    40: invokevirtual   java/io/File.listFiles:()[Ljava/io/File;
		//    43: astore          4
		//    45: aload           4
		//    47: arraylength
		//    48: istore          5
		//    50: iconst_0
		//    51: istore          6
		//    53: iload           6
		//    55: iload           5
		//    57: if_icmplt       81
		//    60: ldc             "com.resmed.refresh.sync"
		//    62: ldc_w           "SyncService uploaing records"
		//    65: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//    68: pop
		//    69: aload_0
		//    70: invokespecial   com/resmed/refresh/model/RefreshSyncService.availablebleToConnect:()Z
		//    73: ifne            261
		//    76: aload_0
		//    77: invokespecial   com/resmed/refresh/model/RefreshSyncService.uploadProfile:()V
		//    80: return
		//    81: aload           4
		//    83: iload           6
		//    85: aaload
		//    86: astore          7
		//    88: ldc             "com.resmed.refresh.sync"
		//    90: new             Ljava/lang/StringBuilder;
		//    93: dup
		//    94: ldc_w           " RefreshSyncService::uploadRecords() file.getName() : "
		//    97: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//   100: aload           7
		//   102: invokevirtual   java/io/File.getName:()Ljava/lang/String;
		//   105: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//   108: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//   111: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//   114: pop
		//   115: aload           7
		//   117: invokevirtual   java/io/File.getName:()Ljava/lang/String;
		//   120: ldc             ".lz4"
		//   122: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
		//   125: ifne            142
		//   128: aload           7
		//   130: invokevirtual   java/io/File.getName:()Ljava/lang/String;
		//   133: ldc_w           ".edf"
		//   136: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
		//   139: ifeq            255
		//   142: new             Ljava/util/Date;
		//   145: dup
		//   146: invokespecial   java/util/Date.<init>:()V
		//   149: invokevirtual   java/util/Date.getTime:()J
		//   152: aload           7
		//   154: invokevirtual   java/io/File.lastModified:()J
		//   157: lsub
		//   158: lstore          9
		//   160: ldc             "com.resmed.refresh.sync"
		//   162: new             Ljava/lang/StringBuilder;
		//   165: dup
		//   166: ldc_w           " RefreshSyncService::uploadRecords() diff : "
		//   169: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//   172: lload           9
		//   174: ldc2_w          86400000
		//   177: ldiv
		//   178: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
		//   181: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//   184: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//   187: pop
		//   188: lload           9
		//   190: ldc2_w          864000000
		//   193: lcmp
		//   194: ifle            255
		//   197: ldc             "com.resmed.refresh.sync"
		//   199: new             Ljava/lang/StringBuilder;
		//   202: dup
		//   203: ldc_w           " RefreshSyncService::uploadRecords() deleting file : "
		//   206: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//   209: aload           7
		//   211: invokevirtual   java/io/File.getName:()Ljava/lang/String;
		//   214: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//   217: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//   220: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//   223: pop
		//   224: aload           7
		//   226: invokevirtual   java/io/File.delete:()Z
		//   229: istore          13
		//   231: ldc             "com.resmed.refresh.sync"
		//   233: new             Ljava/lang/StringBuilder;
		//   236: dup
		//   237: ldc_w           " RefreshSyncService::uploadRecords() wasDeleted : "
		//   240: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//   243: iload           13
		//   245: invokevirtual   java/lang/StringBuilder.append:(Z)Ljava/lang/StringBuilder;
		//   248: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//   251: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//   254: pop
		//   255: iinc            6, 1
		//   258: goto            53
		//   261: invokestatic    com/resmed/refresh/model/RefreshModelController.getInstance:()Lcom/resmed/refresh/model/RefreshModelController;
		//   264: astore          18
		//   266: aload           18
		//   268: invokevirtual   com/resmed/refresh/model/RefreshModelController.localSessionsForUpload:()Ljava/util/List;
		//   271: astore          19
		//   273: aload           19
		//   275: ifnull          288
		//   278: aload           19
		//   280: invokeinterface java/util/List.size:()I
		//   285: ifne            311
		//   288: aload_0
		//   289: invokespecial   com/resmed/refresh/model/RefreshSyncService.uploadProfile:()V
		//   292: return
		//   293: astore          16
		//   295: aload           16
		//   297: invokevirtual   java/lang/Exception.printStackTrace:()V
		//   300: ldc2_w          1500
		//   303: invokestatic    java/lang/Thread.sleep:(J)V
		//   306: aload_0
		//   307: invokespecial   com/resmed/refresh/model/RefreshSyncService.uploadProfile:()V
		//   310: return
		//   311: ldc             "com.resmed.refresh.sync"
		//   313: new             Ljava/lang/StringBuilder;
		//   316: dup
		//   317: aload           19
		//   319: invokeinterface java/util/List.size:()I
		//   324: invokestatic    java/lang/String.valueOf:(I)Ljava/lang/String;
		//   327: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//   330: ldc_w           " records to upload"
		//   333: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//   336: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//   339: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//   342: pop
		//   343: iconst_0
		//   344: istore          21
		//   346: iload           21
		//   348: aload           19
		//   350: invokeinterface java/util/List.size:()I
		//   355: if_icmpge       80
		//   358: new             Ljava/lang/StringBuilder;
		//   361: dup
		//   362: ldc_w           "Uploading record "
		//   365: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//   368: astore          22
		//   370: ldc             "com.resmed.refresh.sync"
		//   372: aload           22
		//   374: aload           19
		//   376: iload           21
		//   378: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
		//   383: checkcast       Lcom/resmed/refresh/model/RST_SleepSessionInfo;
		//   386: invokevirtual   com/resmed/refresh/model/RST_SleepSessionInfo.getId:()J
		//   389: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
		//   392: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//   395: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//   398: pop
		//   399: iload           21
		//   401: iconst_m1
		//   402: aload           19
		//   404: invokeinterface java/util/List.size:()I
		//   409: iadd
		//   410: if_icmpne       437
		//   413: aload           18
		//   415: aload           19
		//   417: iload           21
		//   419: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
		//   424: checkcast       Lcom/resmed/refresh/model/RST_SleepSessionInfo;
		//   427: aload_0
		//   428: getfield        com/resmed/refresh/model/RefreshSyncService.uploadProfile:Lcom/resmed/refresh/model/RST_CallbackItem;
		//   431: invokevirtual   com/resmed/refresh/model/RefreshModelController.serviceUpdateRecord:(Lcom/resmed/refresh/model/RST_SleepSessionInfo;Lcom/resmed/refresh/model/RST_CallbackItem;)V
		//   434: goto            471
		//   437: aload           18
		//   439: aload           19
		//   441: iload           21
		//   443: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
		//   448: checkcast       Lcom/resmed/refresh/model/RST_SleepSessionInfo;
		//   451: aload_0
		//   452: getfield        com/resmed/refresh/model/RefreshSyncService.callback:Lcom/resmed/refresh/model/RST_CallbackItem;
		//   455: invokevirtual   com/resmed/refresh/model/RefreshModelController.serviceUpdateRecord:(Lcom/resmed/refresh/model/RST_SleepSessionInfo;Lcom/resmed/refresh/model/RST_CallbackItem;)V
		//   458: goto            471
		//   461: astore          17
		//   463: aload           17
		//   465: invokevirtual   java/lang/Exception.printStackTrace:()V
		//   468: goto            306
		//   471: iinc            21, 1
		//   474: goto            346
		//    Exceptions:
		//  Try           Handler
		//  Start  End    Start  End    Type
		//  -----  -----  -----  -----  ---------------------
		//  69     80     293    311    Ljava/lang/Exception;
		//  261    273    293    311    Ljava/lang/Exception;
		//  278    288    293    311    Ljava/lang/Exception;
		//  288    292    293    311    Ljava/lang/Exception;
		//  300    306    461    471    Ljava/lang/Exception;
		//  311    343    293    311    Ljava/lang/Exception;
		//  346    434    293    311    Ljava/lang/Exception;
		//  437    458    293    311    Ljava/lang/Exception;
		//
		// The error that occurred was:
		//
		// java.lang.IllegalStateException: Expression is linked from several locations: Label_0306:
		//     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
		//     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
		//     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
		//     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
		//     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
		//     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
		//     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
		//     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
		//     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.decompileClassNode(ProcyonDecompiler.java:120)
		//     at the.bytecode.club.bytecodeviewer.gui.ClassViewer$13.doShit(ClassViewer.java:624)
		//     at the.bytecode.club.bytecodeviewer.gui.PaneUpdaterThread.run(PaneUpdaterThread.java:16)
		//
		throw new IllegalStateException("An error occurred while decompiling this method.");
	}

	public IBinder onBind(final Intent intent) {
		Log.d("com.resmed.refresh.sync", " onBind! ");
		return null;
	}

	public void onRebind(final Intent intent) {
		Log.d("com.resmed.refresh.sync", " onRebind! ");
		super.onRebind(intent);
	}

	public int onStartCommand(final Intent intent, final int n, final int n2) {
		Log.d("com.resmed.refresh.sync", "//////////////////////////////");
		Log.d("com.resmed.refresh.sync", " onStartCommand! ");
		new Thread((Runnable)new RefreshSyncService.RefreshSyncService$5(this)).start();
		return 2;
	}

	public void unbindService(final ServiceConnection serviceConnection) {
		Log.d("com.resmed.refresh.sync", " unbindService! ");
		super.unbindService(serviceConnection);
	}
}
