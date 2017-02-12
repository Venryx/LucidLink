package com.resmed.refresh;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SoundRecordingActivty
		extends BaseActivity
		implements View.OnClickListener {
	private static final String TEMP_FILE = RefreshTools.getFilesPath() + "/refresh/sound/temp.3gp";
	private boolean isRecording = false;
	private ImageView ivSoundRecord;
	private Animation lightAnim;
	private MediaRecorder recorder;
	private File soundFileLog;
	private ThreadAmplitude thread;
	private TextView tvSoundButton;
	private TextView tvSoundValue;

	private void setupAudioRecorderFile() {
		Object localObject = Calendar.getInstance();
		localObject = new SimpleDateFormat("yyyyMMdd_HHmm").format(((Calendar) localObject).getTime());
		this.soundFileLog = new File(RefreshTools.getFilesPath() + "/refresh/sound/soundlog_" + (String) localObject + ".txt");
		RefreshTools.createDirectories(this.soundFileLog.getAbsolutePath());
	}

	private void startRecording() {
		this.recorder = new MediaRecorder();
		this.recorder.setAudioSource(1);
		this.recorder.setOutputFormat(1);
		this.recorder.setOutputFile(TEMP_FILE);
		this.recorder.setAudioEncoder(1);
		try {
			this.recorder.prepare();
			this.recorder.start();
			return;
		} catch (IOException localIOException) {
			for (; ; ) {
				Log.e("com.resmed.refresh.sound", "prepare() failed");
			}
		}
	}

	private void stopRecording() {
		this.tvSoundValue.setText("0\n0.0dB");
		try {
			if (this.recorder != null) {
				this.recorder.stop();
				this.recorder.release();
				this.recorder = null;
			}
			return;
		} catch (Exception localException) {
			for (; ; ) {
				localException.printStackTrace();
			}
		}
	}

	private void updateState() {
		if (this.isRecording) {
			startRecording();
			this.ivSoundRecord.startAnimation(this.lightAnim);
			this.ivSoundRecord.setVisibility(0);
			this.tvSoundButton.setText(2131165428);
			this.tvSoundButton.setBackgroundResource(2130837879);
		}
		for (; ; ) {
			return;
			stopRecording();
			this.ivSoundRecord.clearAnimation();
			this.ivSoundRecord.setVisibility(8);
			this.tvSoundButton.setText(2131165427);
			this.tvSoundButton.setBackgroundResource(2130837878);
		}
	}

	private void write(String paramString) {
		try {
			StringBuffer localStringBuffer = new java.lang.StringBuffer();
			Calendar localCalendar = Calendar.getInstance();
			Object localObject = new java.text.SimpleDateFormat("dd-MM HH:mm:ss.SSS");
			localStringBuffer.append(((SimpleDateFormat) localObject).format(localCalendar.getTime()));
			localStringBuffer.append("\t");
			StringBuilder builder = new java.lang.StringBuilder("sound value:");
			localStringBuffer.append(paramString);
			localStringBuffer.append("\n");
			FileWriter writer = new java.io.FileWriter(this.soundFileLog, true);
			writer.append(localStringBuffer);
			writer.close();
			return;
		} catch (Exception ex) {
			for (; ; ) {
				ex.printStackTrace();
			}
		}
	}

	/*public void onClick(View paramView) {
		switch (paramView.getId()) {
			default:
				return;
		}
		if (this.isRecording) {
		}
		for (boolean bool = false; ; bool = true) {
			this.isRecording = bool;
			updateState();
			break;
		}
	}*/

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(2130903097);
		setTitle("Sound Record Test");
		this.tvSoundButton = ((TextView) findViewById(2131099840));
		this.ivSoundRecord = ((ImageView) findViewById(2131099841));
		this.tvSoundValue = ((TextView) findViewById(2131099839));
		this.tvSoundValue.setText("0\n0.0dB");
		this.lightAnim = new AlphaAnimation(0.6F, 1.0F);
		this.lightAnim.setDuration(500L);
		this.lightAnim.setStartOffset(20L);
		this.lightAnim.setRepeatMode(2);
		this.lightAnim.setRepeatCount(-1);
		this.ivSoundRecord.startAnimation(this.lightAnim);
		this.ivSoundRecord.setVisibility(8);
		this.tvSoundButton.setOnClickListener(this);
		setupAudioRecorderFile();
		this.thread = new ThreadAmplitude();
		this.thread.start();
	}

	protected void onPause() {
		super.onPause();
		this.isRecording = false;
		this.thread.finish();
		this.thread = null;
		updateState();
	}

	protected void onResume() {
		super.onResume();
		if (this.thread == null) {
			this.thread = new ThreadAmplitude();
			this.thread.start();
		}
	}

	private class ThreadAmplitude
			extends Thread {
		private boolean finish = false;

		public ThreadAmplitude() {
		}

		public void finish() {
			this.finish = true;
		}

		public void run() {
			super.run();
			for (; ; ) {
				if (this.finish) {
					return;
				}
				if ((SoundRecordingActivty.this.isRecording) && (SoundRecordingActivty.this.recorder != null)) {
					SoundRecordingActivty.this.runOnUiThread(new Runnable() {
						public void run() {
							int i = SoundRecordingActivty.this.recorder.getMaxAmplitude();
							float f2 = (float) (20.0D * Math.log10(i));
							float f1 = f2;
							if (f2 == Float.NEGATIVE_INFINITY) {
								f1 = 0.0F;
							}
							SoundRecordingActivty.this.tvSoundValue.setText(i + "\n" + String.format("%.1f", new Object[]{Float.valueOf(f1)}) + "dB");
							SoundRecordingActivty.this.write("value " + i + "\t\t" + String.format("%.1f", new Object[]{Float.valueOf(f1)}) + "dB");
						}
					});
				}
				try {
					Thread.sleep(1000L);
				} catch (Exception localException) {
					localException.printStackTrace();
				}
			}
		}
	}
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */