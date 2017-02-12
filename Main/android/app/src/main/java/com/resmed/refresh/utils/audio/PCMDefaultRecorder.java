package com.resmed.refresh.utils.audio;

import android.media.*;
import com.resmed.refresh.utils.*;
import java.io.*;

public class PCMDefaultRecorder implements AudioRecorder
{
	private static final int RECORDER_AUDIO_ENCODING = 2;
	private static final int RECORDER_CHANNELS = 16;
	private static final int RECORDER_SAMPLERATE = 44100;
	private static PCMDefaultRecorder instance;
	private int BufferElements2Rec;
	private int BytesPerElement;
	private Thread amplitudeHandlerThread;
	private Integer byteWrote;
	private Integer duration;
	private String fileName;
	private boolean isConverted;
	private boolean isPause;
	private boolean isRecording;
	private Double maxSoundLevel;
	private FileOutputStream os;
	private AudioRecord recorder;
	private Thread recordingThread;
	private AudioDefaultRecorder.AudioSampleReceiver sampleReceiver;
	private String tempFileName;
	private int timeIntervalSamples;
	private File wavTarget;

	private PCMDefaultRecorder() {
		this.recorder = null;
		this.recordingThread = null;
		this.BufferElements2Rec = 2048;
		this.BytesPerElement = 2;
		this.amplitudeHandlerThread = null;
		this.BufferElements2Rec = AudioRecord.getMinBufferSize(44100, 16, 2);
	}

	public static PCMDefaultRecorder getInstance() {
		if (PCMDefaultRecorder.instance == null) {
			PCMDefaultRecorder.instance = new PCMDefaultRecorder();
		}
		return PCMDefaultRecorder.instance;
	}

	private byte[] short2byte(final short[] array) {
		final int length = array.length;
		final byte[] array2 = new byte[length * 2];
		for (int i = 0; i < length; ++i) {
			array2[i * 2] = (byte)(0xFF & array[i]);
			array2[1 + i * 2] = (byte)(array[i] >> 8);
			array[i] = 0;
		}
		return array2;
	}

	private void writeAudioDataToFile() {
		//
		// This method could not be decompiled.
		//
		// Original Bytecode:
		//
		//     0: aload_0
		//     1: monitorenter
		//     2: aload_0
		//     3: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.BufferElements2Rec:I
		//     6: newarray        S
		//     8: astore_3
		//     9: aload_0
		//    10: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.isRecording:Z
		//    13: ifne            68
		//    16: aload_0
		//    17: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.recordingThread:Ljava/lang/Thread;
		//    20: ifnull          51
		//    23: ldc             "com.resmed.refresh.recorder"
		//    25: new             Ljava/lang/StringBuilder;
		//    28: dup
		//    29: ldc             "destroy thread "
		//    31: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//    34: aload_0
		//    35: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.recordingThread:Ljava/lang/Thread;
		//    38: invokevirtual   java/lang/Thread.getName:()Ljava/lang/String;
		//    41: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//    44: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//    47: invokestatic    com/resmed/refresh/utils/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
		//    50: pop
		//    51: aload_0
		//    52: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.os:Ljava/io/FileOutputStream;
		//    55: ifnull          65
		//    58: aload_0
		//    59: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.os:Ljava/io/FileOutputStream;
		//    62: invokevirtual   java/io/FileOutputStream.close:()V
		//    65: aload_0
		//    66: monitorexit
		//    67: return
		//    68: ldc             "com.resmed.refresh.player"
		//    70: new             Ljava/lang/StringBuilder;
		//    73: dup
		//    74: ldc             "recording thread -> current thread in execution: "
		//    76: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//    79: aload_0
		//    80: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.recordingThread:Ljava/lang/Thread;
		//    83: invokevirtual   java/lang/Thread.getName:()Ljava/lang/String;
		//    86: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//    89: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//    92: invokestatic    com/resmed/refresh/utils/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
		//    95: pop
		//    96: aload_0
		//    97: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.isPause:Z
		//   100: ifne            282
		//   103: aload_0
		//   104: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.recorder:Landroid/media/AudioRecord;
		//   107: aload_3
		//   108: iconst_0
		//   109: aload_0
		//   110: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.BufferElements2Rec:I
		//   113: invokevirtual   android/media/AudioRecord.read:([SII)I
		//   116: istore          6
		//   118: dconst_0
		//   119: dstore          7
		//   121: iconst_0
		//   122: istore          9
		//   124: iload           9
		//   126: iload           6
		//   128: if_icmplt       262
		//   131: dload           7
		//   133: iload           6
		//   135: i2d
		//   136: ddiv
		//   137: invokestatic    java/lang/Math.abs:(D)D
		//   140: invokestatic    java/lang/Double.valueOf:(D)Ljava/lang/Double;
		//   143: astore          10
		//   145: aload           10
		//   147: invokevirtual   java/lang/Double.doubleValue:()D
		//   150: aload_0
		//   151: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.maxSoundLevel:Ljava/lang/Double;
		//   154: invokevirtual   java/lang/Double.doubleValue:()D
		//   157: dcmpl
		//   158: ifle            167
		//   161: aload_0
		//   162: aload           10
		//   164: putfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.maxSoundLevel:Ljava/lang/Double;
		//   167: getstatic       java/lang/System.out:Ljava/io/PrintStream;
		//   170: new             Ljava/lang/StringBuilder;
		//   173: dup
		//   174: ldc             "Short wirting to file"
		//   176: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//   179: aload_3
		//   180: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
		//   183: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//   186: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//   189: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
		//   192: aload_0
		//   193: aload_3
		//   194: invokespecial   com/resmed/refresh/utils/audio/PCMDefaultRecorder.short2byte:([S)[B
		//   197: astore          12
		//   199: aload_0
		//   200: aload_0
		//   201: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.byteWrote:Ljava/lang/Integer;
		//   204: invokevirtual   java/lang/Integer.intValue:()I
		//   207: aload           12
		//   209: arraylength
		//   210: iadd
		//   211: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
		//   214: putfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.byteWrote:Ljava/lang/Integer;
		//   217: aload_0
		//   218: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.os:Ljava/io/FileOutputStream;
		//   221: aload           12
		//   223: iconst_0
		//   224: aload_0
		//   225: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.BufferElements2Rec:I
		//   228: aload_0
		//   229: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.BytesPerElement:I
		//   232: imul
		//   233: invokevirtual   java/io/FileOutputStream.write:([BII)V
		//   236: goto            9
		//   239: astore          11
		//   241: aload           11
		//   243: invokevirtual   java/io/IOException.printStackTrace:()V
		//   246: goto            9
		//   249: astore_2
		//   250: aload_2
		//   251: invokevirtual   java/lang/Exception.printStackTrace:()V
		//   254: goto            65
		//   257: astore_1
		//   258: aload_0
		//   259: monitorexit
		//   260: aload_1
		//   261: athrow
		//   262: aload_3
		//   263: iload           9
		//   265: saload
		//   266: istore          13
		//   268: dload           7
		//   270: iload           13
		//   272: i2d
		//   273: dadd
		//   274: dstore          7
		//   276: iinc            9, 1
		//   279: goto            124
		//   282: ldc2_w          200
		//   285: invokestatic    java/lang/Thread.sleep:(J)V
		//   288: goto            9
		//   291: astore          5
		//   293: aload           5
		//   295: invokevirtual   java/lang/InterruptedException.printStackTrace:()V
		//   298: goto            9
		//   301: astore          14
		//   303: aload           14
		//   305: invokevirtual   java/io/IOException.printStackTrace:()V
		//   308: goto            65
		//    Exceptions:
		//  Try           Handler
		//  Start  End    Start  End    Type
		//  -----  -----  -----  -----  --------------------------------
		//  2      9      249    257    Ljava/lang/Exception;
		//  2      9      257    262    Any
		//  9      51     249    257    Ljava/lang/Exception;
		//  9      51     257    262    Any
		//  51     65     301    311    Ljava/io/IOException;
		//  51     65     249    257    Ljava/lang/Exception;
		//  51     65     257    262    Any
		//  68     118    249    257    Ljava/lang/Exception;
		//  68     118    257    262    Any
		//  131    167    249    257    Ljava/lang/Exception;
		//  131    167    257    262    Any
		//  167    192    249    257    Ljava/lang/Exception;
		//  167    192    257    262    Any
		//  192    236    239    249    Ljava/io/IOException;
		//  192    236    249    257    Ljava/lang/Exception;
		//  192    236    257    262    Any
		//  241    246    249    257    Ljava/lang/Exception;
		//  241    246    257    262    Any
		//  250    254    257    262    Any
		//  262    268    249    257    Ljava/lang/Exception;
		//  262    268    257    262    Any
		//  282    288    291    301    Ljava/lang/InterruptedException;
		//  282    288    249    257    Ljava/lang/Exception;
		//  282    288    257    262    Any
		//  293    298    249    257    Ljava/lang/Exception;
		//  293    298    257    262    Any
		//  303    308    249    257    Ljava/lang/Exception;
		//  303    308    257    262    Any
		//
		// The error that occurred was:
		//
		// java.lang.IllegalStateException: Expression is linked from several locations: Label_0051:
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

	public Integer getDuration() {
		return 1000 * this.duration;
	}

	public String getFilePath() {
		Log.e("", "MindClear fileName:" + this.fileName);
		return this.fileName;
	}

	public String getTempFilePath() {
		return this.tempFileName;
	}

	public void pause() {
		this.isPause = true;
		Log.i("com.resmed.refresh.recorder", "recorder on pause -> byte wrote: " + this.byteWrote.toString());
	}

	public void play() {
		this.isPause = false;
	}

	public void setupRecorder(final String s, final String s2, final int timeIntervalSamples, final AudioDefaultRecorder.AudioSampleReceiver sampleReceiver) {
		final File filesPath = RefreshTools.getFilesPath();
		this.tempFileName = String.valueOf(filesPath.getAbsolutePath()) + "/" + s2;
		this.timeIntervalSamples = timeIntervalSamples;
		this.isRecording = false;
		this.sampleReceiver = sampleReceiver;
		this.byteWrote = 0;
		this.os = null;
		this.isPause = false;
		this.isConverted = false;
		this.wavTarget = new File(filesPath.getAbsolutePath(), s);
		this.fileName = String.valueOf(filesPath.getAbsolutePath()) + "/" + s;
		this.maxSoundLevel = 0.0;
	}

	public void startRecording() {
		//
		// This method could not be decompiled.
		//
		// Original Bytecode:
		//
		//     0: aload_0
		//     1: new             Landroid/media/AudioRecord;
		//     4: dup
		//     5: iconst_1
		//     6: ldc             44100
		//     8: bipush          16
		//    10: iconst_2
		//    11: aload_0
		//    12: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.BufferElements2Rec:I
		//    15: aload_0
		//    16: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.BytesPerElement:I
		//    19: imul
		//    20: invokespecial   android/media/AudioRecord.<init>:(IIIII)V
		//    23: putfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.recorder:Landroid/media/AudioRecord;
		//    26: aload_0
		//    27: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.recorder:Landroid/media/AudioRecord;
		//    30: invokevirtual   android/media/AudioRecord.startRecording:()V
		//    33: aload_0
		//    34: invokevirtual   com/resmed/refresh/utils/audio/PCMDefaultRecorder.play:()V
		//    37: aload_0
		//    38: iconst_1
		//    39: putfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.isRecording:Z
		//    42: new             Ljava/io/File;
		//    45: dup
		//    46: aload_0
		//    47: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.tempFileName:Ljava/lang/String;
		//    50: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
		//    53: astore_2
		//    54: aload_2
		//    55: invokevirtual   java/io/File.exists:()Z
		//    58: istore          4
		//    60: iload           4
		//    62: ifne            95
		//    65: aload_2
		//    66: invokevirtual   java/io/File.createNewFile:()Z
		//    69: istore          6
		//    71: ldc             "com.resmed.refresh.recorder"
		//    73: new             Ljava/lang/StringBuilder;
		//    76: dup
		//    77: ldc_w           "File created? "
		//    80: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//    83: iload           6
		//    85: invokevirtual   java/lang/StringBuilder.append:(Z)Ljava/lang/StringBuilder;
		//    88: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//    91: invokestatic    com/resmed/refresh/utils/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
		//    94: pop
		//    95: aload_0
		//    96: new             Ljava/io/FileOutputStream;
		//    99: dup
		//   100: aload_0
		//   101: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.tempFileName:Ljava/lang/String;
		//   104: invokespecial   java/io/FileOutputStream.<init>:(Ljava/lang/String;)V
		//   107: putfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.os:Ljava/io/FileOutputStream;
		//   110: aload_0
		//   111: new             Ljava/lang/Thread;
		//   114: dup
		//   115: new             Lcom/resmed/refresh/utils/audio/PCMDefaultRecorder$1;
		//   118: dup
		//   119: aload_0
		//   120: invokespecial   com/resmed/refresh/utils/audio/PCMDefaultRecorder$1.<init>:(Lcom/resmed/refresh/utils/audio/PCMDefaultRecorder;)V
		//   123: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;)V
		//   126: putfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.recordingThread:Ljava/lang/Thread;
		//   129: aload_0
		//   130: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.recordingThread:Ljava/lang/Thread;
		//   133: invokevirtual   java/lang/Thread.start:()V
		//   136: aload_0
		//   137: new             Ljava/lang/Thread;
		//   140: dup
		//   141: new             Lcom/resmed/refresh/utils/audio/PCMDefaultRecorder$2;
		//   144: dup
		//   145: aload_0
		//   146: invokespecial   com/resmed/refresh/utils/audio/PCMDefaultRecorder$2.<init>:(Lcom/resmed/refresh/utils/audio/PCMDefaultRecorder;)V
		//   149: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;)V
		//   152: putfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.amplitudeHandlerThread:Ljava/lang/Thread;
		//   155: aload_0
		//   156: getfield        com/resmed/refresh/utils/audio/PCMDefaultRecorder.amplitudeHandlerThread:Ljava/lang/Thread;
		//   159: invokevirtual   java/lang/Thread.start:()V
		//   162: return
		//   163: astore_1
		//   164: aload_1
		//   165: invokevirtual   java/lang/Exception.printStackTrace:()V
		//   168: goto            33
		//   171: astore          5
		//   173: aload           5
		//   175: invokevirtual   java/io/IOException.printStackTrace:()V
		//   178: goto            95
		//   181: astore_3
		//   182: aload_3
		//   183: invokevirtual   java/io/FileNotFoundException.printStackTrace:()V
		//   186: goto            110
		//    Exceptions:
		//  Try           Handler
		//  Start  End    Start  End    Type
		//  -----  -----  -----  -----  -------------------------------
		//  26     33     163    171    Ljava/lang/Exception;
		//  42     60     181    189    Ljava/io/FileNotFoundException;
		//  65     95     171    181    Ljava/io/IOException;
		//  65     95     181    189    Ljava/io/FileNotFoundException;
		//  95     110    181    189    Ljava/io/FileNotFoundException;
		//  173    178    181    189    Ljava/io/FileNotFoundException;
		//
		// The error that occurred was:
		//
		// java.lang.IllegalStateException: Expression is linked from several locations: Label_0095:
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

	public void stop(final boolean b) {
		if (this.recorder != null) {
			this.isRecording = false;
			this.recorder.stop();
			this.recorder.release();
			this.recorder = null;
			if (this.recordingThread != null) {
				this.recordingThread.interrupt();
				this.recordingThread = null;
				System.gc();
			}
			if (this.amplitudeHandlerThread != null) {
				this.amplitudeHandlerThread.interrupt();
				this.amplitudeHandlerThread = null;
				System.gc();
			}
		}
		Log.i("com.resmed.refresh.recorder", "byte wrote: " + this.byteWrote.toString());
		this.duration = this.byteWrote / 1 / 2 / 44100;
		Log.i("com.resmed.refresh.recorder", "audio duration: " + this.duration);
		if (this.isConverted) {
			return;
		}
		final WavAudioFormat.Builder wavAudioFormat$Builder = new WavAudioFormat.Builder();
		wavAudioFormat$Builder.sampleRate(44100);
		wavAudioFormat$Builder.channels(1);
		wavAudioFormat$Builder.sampleSizeInBits(16);
		final WavAudioFormat build = wavAudioFormat$Builder.build();
		final File file = new File(this.tempFileName);
		while (true) {
			try {
				PcmAudioHelper.convertRawToWav(build, file, this.wavTarget);
				if (file.exists()) {
					file.delete();
				}
				this.isConverted = true;
			}
			catch (IOException ex) {
				ex.printStackTrace();
				continue;
			}
			break;
		}
	}
}
