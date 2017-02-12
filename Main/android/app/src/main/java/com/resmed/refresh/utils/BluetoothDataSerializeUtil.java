package com.resmed.refresh.utils;

import android.content.*;
import android.bluetooth.*;
import com.resmed.refresh.packets.*;
import com.google.gson.*;
import java.io.*;

public class BluetoothDataSerializeUtil
{
	public static boolean clearJSONFile(final Context context) {
		new File(RefreshTools.getFilesPath(), context.getString(2131165342)).delete();
		return true;
	}

	public static boolean deleteBulkDataBioFile(final Context context) {
		return new File(RefreshTools.getFilesPath(), context.getString(2131165343)).delete();
	}

	public static boolean deleteJsonFile(final Context context) {
		try {
			new File(RefreshTools.getFilesPath(), context.getString(2131165342)).delete();
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static int[][] readBulkBioDataFile(final Context p0) {
		//
		// This method could not be decompiled.
		//
		// Original Bytecode:
		//
		//     0: ldc             Lcom/resmed/refresh/utils/BluetoothDataSerializeUtil;.class
		//     2: monitorenter
		//     3: new             Ljava/io/File;
		//     6: dup
		//     7: invokestatic    com/resmed/refresh/utils/RefreshTools.getFilesPath:()Ljava/io/File;
		//    10: aload_0
		//    11: ldc             2131165343
		//    13: invokevirtual   android/content/Context.getString:(I)Ljava/lang/String;
		//    16: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
		//    19: astore_1
		//    20: new             Ljava/util/ArrayList;
		//    23: dup
		//    24: invokespecial   java/util/ArrayList.<init>:()V
		//    27: astore_2
		//    28: new             Ljava/util/ArrayList;
		//    31: dup
		//    32: invokespecial   java/util/ArrayList.<init>:()V
		//    35: astore_3
		//    36: aconst_null
		//    37: astore          4
		//    39: new             Ljava/io/FileInputStream;
		//    42: dup
		//    43: aload_1
		//    44: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
		//    47: astore          5
		//    49: new             Ljava/io/InputStreamReader;
		//    52: dup
		//    53: aload           5
		//    55: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
		//    58: astore          6
		//    60: new             Ljava/io/BufferedReader;
		//    63: dup
		//    64: aload           6
		//    66: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
		//    69: astore          7
		//    71: iconst_0
		//    72: istore          8
		//    74: aload           7
		//    76: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
		//    79: astore          21
		//    81: aload           21
		//    83: ifnonnull       176
		//    86: aload           7
		//    88: ifnull          413
		//    91: aload           7
		//    93: invokevirtual   java/io/BufferedReader.close:()V
		//    96: aload_2
		//    97: invokeinterface java/util/List.size:()I
		//   102: newarray        I
		//   104: astore          14
		//   106: aload_3
		//   107: invokeinterface java/util/List.size:()I
		//   112: newarray        I
		//   114: astore          15
		//   116: iconst_0
		//   117: istore          16
		//   119: iload           16
		//   121: aload_2
		//   122: invokeinterface java/util/List.size:()I
		//   127: if_icmplt       343
		//   130: iconst_2
		//   131: newarray        I
		//   133: dup
		//   134: iconst_0
		//   135: iconst_2
		//   136: iastore
		//   137: dup
		//   138: iconst_1
		//   139: aload           14
		//   141: arraylength
		//   142: iastore
		//   143: astore          17
		//   145: getstatic       java/lang/Integer.TYPE:Ljava/lang/Class;
		//   148: aload           17
		//   150: invokestatic    java/lang/reflect/Array.newInstance:(Ljava/lang/Class;[I)Ljava/lang/Object;
		//   153: checkcast       [[I
		//   156: astore          18
		//   158: aload           18
		//   160: iconst_0
		//   161: aload           14
		//   163: aastore
		//   164: aload           18
		//   166: iconst_1
		//   167: aload           15
		//   169: aastore
		//   170: ldc             Lcom/resmed/refresh/utils/BluetoothDataSerializeUtil;.class
		//   172: monitorexit
		//   173: aload           18
		//   175: areturn
		//   176: iinc            8, 1
		//   179: aload           21
		//   181: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
		//   184: ldc             "\\s+"
		//   186: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
		//   189: astore          22
		//   191: aload           22
		//   193: iconst_0
		//   194: aaload
		//   195: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
		//   198: istore          23
		//   200: aload           22
		//   202: iconst_1
		//   203: aaload
		//   204: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
		//   207: istore          24
		//   209: aload_2
		//   210: iload           23
		//   212: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
		//   215: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
		//   220: pop
		//   221: aload_3
		//   222: iload           24
		//   224: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
		//   227: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
		//   232: pop
		//   233: goto            74
		//   236: astore          19
		//   238: aload           7
		//   240: astore          4
		//   242: aload           19
		//   244: invokevirtual   java/io/FileNotFoundException.printStackTrace:()V
		//   247: aload           4
		//   249: ifnull          96
		//   252: aload           4
		//   254: invokevirtual   java/io/BufferedReader.close:()V
		//   257: goto            96
		//   260: astore          20
		//   262: aload           20
		//   264: invokevirtual   java/io/IOException.printStackTrace:()V
		//   267: goto            96
		//   270: astore          11
		//   272: ldc             Lcom/resmed/refresh/utils/BluetoothDataSerializeUtil;.class
		//   274: monitorexit
		//   275: aload           11
		//   277: athrow
		//   278: astore          9
		//   280: aload           9
		//   282: invokevirtual   java/io/IOException.printStackTrace:()V
		//   285: aload           4
		//   287: ifnull          96
		//   290: aload           4
		//   292: invokevirtual   java/io/BufferedReader.close:()V
		//   295: goto            96
		//   298: astore          13
		//   300: aload           13
		//   302: invokevirtual   java/io/IOException.printStackTrace:()V
		//   305: goto            96
		//   308: astore          10
		//   310: aload           4
		//   312: ifnull          320
		//   315: aload           4
		//   317: invokevirtual   java/io/BufferedReader.close:()V
		//   320: aload           10
		//   322: athrow
		//   323: astore          12
		//   325: aload           12
		//   327: invokevirtual   java/io/IOException.printStackTrace:()V
		//   330: goto            320
		//   333: astore          27
		//   335: aload           27
		//   337: invokevirtual   java/io/IOException.printStackTrace:()V
		//   340: goto            413
		//   343: aload           14
		//   345: iload           16
		//   347: aload_2
		//   348: iload           16
		//   350: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
		//   355: checkcast       Ljava/lang/Integer;
		//   358: invokevirtual   java/lang/Integer.intValue:()I
		//   361: iastore
		//   362: aload           15
		//   364: iload           16
		//   366: aload_3
		//   367: iload           16
		//   369: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
		//   374: checkcast       Ljava/lang/Integer;
		//   377: invokevirtual   java/lang/Integer.intValue:()I
		//   380: iastore
		//   381: iinc            16, 1
		//   384: goto            119
		//   387: astore          10
		//   389: aload           7
		//   391: astore          4
		//   393: goto            310
		//   396: astore          9
		//   398: aload           7
		//   400: astore          4
		//   402: goto            280
		//   405: astore          19
		//   407: aconst_null
		//   408: astore          4
		//   410: goto            242
		//   413: goto            96
		//    Exceptions:
		//  Try           Handler
		//  Start  End    Start  End    Type
		//  -----  -----  -----  -----  -------------------------------
		//  3      36     270    278    Any
		//  39     71     405    413    Ljava/io/FileNotFoundException;
		//  39     71     278    280    Ljava/io/IOException;
		//  39     71     308    310    Any
		//  74     81     236    242    Ljava/io/FileNotFoundException;
		//  74     81     396    405    Ljava/io/IOException;
		//  74     81     387    396    Any
		//  91     96     333    343    Ljava/io/IOException;
		//  91     96     270    278    Any
		//  96     116    270    278    Any
		//  119    170    270    278    Any
		//  179    233    236    242    Ljava/io/FileNotFoundException;
		//  179    233    396    405    Ljava/io/IOException;
		//  179    233    387    396    Any
		//  242    247    308    310    Any
		//  252    257    260    270    Ljava/io/IOException;
		//  252    257    270    278    Any
		//  262    267    270    278    Any
		//  280    285    308    310    Any
		//  290    295    298    308    Ljava/io/IOException;
		//  290    295    270    278    Any
		//  300    305    270    278    Any
		//  315    320    323    333    Ljava/io/IOException;
		//  315    320    270    278    Any
		//  320    323    270    278    Any
		//  325    330    270    278    Any
		//  335    340    270    278    Any
		//  343    381    270    278    Any
		//
		// The error that occurred was:
		//
		// java.lang.IndexOutOfBoundsException: Index: 193, Size: 193
		//     at java.util.ArrayList.rangeCheck(Unknown Source)
		//     at java.util.ArrayList.get(Unknown Source)
		//     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
		//     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
		//     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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

	public static BluetoothDevice readJsonFile(final Context p0) {
		//
		// This method could not be decompiled.
		//
		// Original Bytecode:
		//
		//     0: aconst_null
		//     1: astore_1
		//     2: invokestatic    com/resmed/refresh/utils/RefreshTools.getFilesPath:()Ljava/io/File;
		//     5: astore_2
		//     6: new             Ljava/io/FileInputStream;
		//     9: dup
		//    10: new             Ljava/io/File;
		//    13: dup
		//    14: aload_2
		//    15: aload_0
		//    16: ldc             2131165342
		//    18: invokevirtual   android/content/Context.getString:(I)Ljava/lang/String;
		//    21: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
		//    24: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
		//    27: astore_3
		//    28: new             Ljava/lang/StringBuffer;
		//    31: dup
		//    32: ldc             ""
		//    34: invokespecial   java/lang/StringBuffer.<init>:(Ljava/lang/String;)V
		//    37: astore          4
		//    39: sipush          1024
		//    42: newarray        B
		//    44: astore          14
		//    46: aload_3
		//    47: aload           14
		//    49: invokevirtual   java/io/FileInputStream.read:([B)I
		//    52: istore          15
		//    54: iload           15
		//    56: iconst_m1
		//    57: if_icmpne       93
		//    60: new             Lcom/google/gson/Gson;
		//    63: dup
		//    64: invokespecial   com/google/gson/Gson.<init>:()V
		//    67: aload           4
		//    69: invokevirtual   java/lang/StringBuffer.toString:()Ljava/lang/String;
		//    72: ldc             Landroid/bluetooth/BluetoothDevice;.class
		//    74: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;
		//    77: checkcast       Landroid/bluetooth/BluetoothDevice;
		//    80: astore          8
		//    82: aload_3
		//    83: ifnull          233
		//    86: aload_3
		//    87: invokevirtual   java/io/FileInputStream.close:()V
		//    90: aload           8
		//    92: areturn
		//    93: aload           4
		//    95: new             Ljava/lang/String;
		//    98: dup
		//    99: aload           14
		//   101: iconst_0
		//   102: iload           15
		//   104: invokespecial   java/lang/String.<init>:([BII)V
		//   107: invokevirtual   java/lang/StringBuffer.append:(Ljava/lang/String;)Ljava/lang/StringBuffer;
		//   110: pop
		//   111: goto            46
		//   114: astore          12
		//   116: aload_3
		//   117: astore_1
		//   118: aload           12
		//   120: invokevirtual   java/io/FileNotFoundException.printStackTrace:()V
		//   123: aconst_null
		//   124: astore          8
		//   126: aload_1
		//   127: ifnull          90
		//   130: aload_1
		//   131: invokevirtual   java/io/FileInputStream.close:()V
		//   134: aconst_null
		//   135: areturn
		//   136: astore          13
		//   138: aload           13
		//   140: invokevirtual   java/io/IOException.printStackTrace:()V
		//   143: aconst_null
		//   144: areturn
		//   145: astore          5
		//   147: aload           5
		//   149: invokevirtual   java/io/IOException.printStackTrace:()V
		//   152: aconst_null
		//   153: astore          8
		//   155: aload_1
		//   156: ifnull          90
		//   159: aload_1
		//   160: invokevirtual   java/io/FileInputStream.close:()V
		//   163: aconst_null
		//   164: areturn
		//   165: astore          9
		//   167: aload           9
		//   169: invokevirtual   java/io/IOException.printStackTrace:()V
		//   172: aconst_null
		//   173: areturn
		//   174: astore          10
		//   176: aload           10
		//   178: invokevirtual   java/lang/Exception.printStackTrace:()V
		//   181: aconst_null
		//   182: astore          8
		//   184: aload_1
		//   185: ifnull          90
		//   188: aload_1
		//   189: invokevirtual   java/io/FileInputStream.close:()V
		//   192: aconst_null
		//   193: areturn
		//   194: astore          11
		//   196: aload           11
		//   198: invokevirtual   java/io/IOException.printStackTrace:()V
		//   201: aconst_null
		//   202: areturn
		//   203: astore          6
		//   205: aload_1
		//   206: ifnull          213
		//   209: aload_1
		//   210: invokevirtual   java/io/FileInputStream.close:()V
		//   213: aload           6
		//   215: athrow
		//   216: astore          7
		//   218: aload           7
		//   220: invokevirtual   java/io/IOException.printStackTrace:()V
		//   223: goto            213
		//   226: astore          16
		//   228: aload           16
		//   230: invokevirtual   java/io/IOException.printStackTrace:()V
		//   233: aload           8
		//   235: areturn
		//   236: astore          6
		//   238: aload_3
		//   239: astore_1
		//   240: goto            205
		//   243: astore          6
		//   245: aload_3
		//   246: astore_1
		//   247: goto            205
		//   250: astore          10
		//   252: aload_3
		//   253: astore_1
		//   254: goto            176
		//   257: astore          10
		//   259: aload_3
		//   260: astore_1
		//   261: goto            176
		//   264: astore          5
		//   266: aload_3
		//   267: astore_1
		//   268: goto            147
		//   271: astore          5
		//   273: aload_3
		//   274: astore_1
		//   275: goto            147
		//   278: astore          12
		//   280: aconst_null
		//   281: astore_1
		//   282: goto            118
		//   285: astore          12
		//   287: aload_3
		//   288: astore_1
		//   289: goto            118
		//    Exceptions:
		//  Try           Handler
		//  Start  End    Start  End    Type
		//  -----  -----  -----  -----  -------------------------------
		//  6      28     278    285    Ljava/io/FileNotFoundException;
		//  6      28     145    147    Ljava/io/IOException;
		//  6      28     174    176    Ljava/lang/Exception;
		//  6      28     203    205    Any
		//  28     39     285    292    Ljava/io/FileNotFoundException;
		//  28     39     264    271    Ljava/io/IOException;
		//  28     39     250    257    Ljava/lang/Exception;
		//  28     39     236    243    Any
		//  39     46     114    118    Ljava/io/FileNotFoundException;
		//  39     46     271    278    Ljava/io/IOException;
		//  39     46     257    264    Ljava/lang/Exception;
		//  39     46     243    250    Any
		//  46     54     114    118    Ljava/io/FileNotFoundException;
		//  46     54     271    278    Ljava/io/IOException;
		//  46     54     257    264    Ljava/lang/Exception;
		//  46     54     243    250    Any
		//  60     82     114    118    Ljava/io/FileNotFoundException;
		//  60     82     271    278    Ljava/io/IOException;
		//  60     82     257    264    Ljava/lang/Exception;
		//  60     82     243    250    Any
		//  86     90     226    233    Ljava/io/IOException;
		//  93     111    114    118    Ljava/io/FileNotFoundException;
		//  93     111    271    278    Ljava/io/IOException;
		//  93     111    257    264    Ljava/lang/Exception;
		//  93     111    243    250    Any
		//  118    123    203    205    Any
		//  130    134    136    145    Ljava/io/IOException;
		//  147    152    203    205    Any
		//  159    163    165    174    Ljava/io/IOException;
		//  176    181    203    205    Any
		//  188    192    194    203    Ljava/io/IOException;
		//  209    213    216    226    Ljava/io/IOException;
		//
		// The error that occurred was:
		//
		// java.lang.IndexOutOfBoundsException: Index: 153, Size: 153
		//     at java.util.ArrayList.rangeCheck(Unknown Source)
		//     at java.util.ArrayList.get(Unknown Source)
		//     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
		//     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3417)
		//     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
		//     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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

	public static boolean writeBulkBioDataFile(final Context context, final byte[] array) {
		while (true) {
			boolean b = true;
			synchronized (BluetoothDataSerializeUtil.class) {
				final File file = new File(RefreshTools.getFilesPath(), context.getString(2131165343));
				try {
					if (!file.exists()) {
						file.createNewFile();
					}
					final FileWriter fileWriter = new FileWriter(file, true);
					final int[][] bioData = PacketsByteValuesReader.readBioData(array, array.length);
					for (int i = 0; i < bioData[0].length; ++i) {
						fileWriter.append((CharSequence)(String.valueOf(bioData[0][i]) + " " + bioData[1][i] + "\n"));
					}
					fileWriter.flush();
					fileWriter.close();
					return b;
				}
				catch (FileNotFoundException ex) {
					ex.printStackTrace();
				}
				catch (IOException ex2) {
					ex2.printStackTrace();
				}
			}
			b = false;
			return b;
		}
	}

	public static boolean writeJsonFile(final Context context, final BluetoothDevice bluetoothDevice) {
		if (bluetoothDevice == null) {
			return false;
		}
		final String json = new Gson().toJson((Object)bluetoothDevice);
		FileOutputStream fileOutputStream = null;
		try {
			final File file = new File(RefreshTools.getFilesPath(), context.getString(2131165342));
			if (!file.exists()) {
				file.createNewFile();
			}
			final FileOutputStream fileOutputStream2;
			fileOutputStream = (fileOutputStream2 = new FileOutputStream(file));
			final String s = json;
			final byte[] array = s.getBytes();
			fileOutputStream2.write(array);
			final FileOutputStream fileOutputStream3 = fileOutputStream;
			fileOutputStream3.flush();
			final FileOutputStream fileOutputStream4 = fileOutputStream;
			fileOutputStream4.close();
			return true;
		}
		catch (FileNotFoundException ex3) {}
		catch (IOException ex4) {}
		Label_0089_Outer:
		while (true) {
			while (true) {
				try {
					final FileOutputStream fileOutputStream2 = fileOutputStream;
					final String s = json;
					final byte[] array = s.getBytes();
					fileOutputStream2.write(array);
					final FileOutputStream fileOutputStream3 = fileOutputStream;
					fileOutputStream3.flush();
					final FileOutputStream fileOutputStream4 = fileOutputStream;
					fileOutputStream4.close();
					return true;
				}
				catch (FileNotFoundException ex) {
					ex.printStackTrace();
					return false;
				}
				catch (IOException ex2) {
					ex2.printStackTrace();
					return false;
				}
			}
		}
	}
}
