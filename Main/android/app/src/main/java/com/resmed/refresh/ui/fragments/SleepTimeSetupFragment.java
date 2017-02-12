/*
 * Decompiled with CFR 0_115.
 *
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.graphics.Color
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.support.v4.app.Fragment
 *  android.support.v4.app.FragmentActivity
 *  android.support.v4.app.FragmentManager
 *  android.support.v4.app.FragmentTransaction
 *  android.support.v7.app.ActionBar
 *  android.support.v7.app.ActionBar$LayoutParams
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.widget.Button
 *  android.widget.LinearLayout
 *  android.widget.RelativeLayout
 *  android.widget.ScrollView
 *  com.resmed.refresh.bed.LedsState
 *  com.resmed.refresh.bluetooth.BeDConnectionStatus
 *  com.resmed.refresh.bluetooth.CONNECTION_STATE
 *  com.resmed.refresh.model.RST_CallbackItem
 *  com.resmed.refresh.model.RST_DisplayItem
 *  com.resmed.refresh.model.RST_NightQuestions
 *  com.resmed.refresh.model.RST_QuestionItem
 *  com.resmed.refresh.model.RST_Response
 *  com.resmed.refresh.model.RefreshModelController
 *  com.resmed.refresh.model.json.JsonRPC
 *  com.resmed.refresh.model.json.ResultRPC
 *  com.resmed.refresh.packets.PacketsByteValuesReader
 *  com.resmed.refresh.packets.VLPacketType
 *  com.resmed.refresh.ui.customview.PreSleepQuestionView
 *  com.resmed.refresh.ui.fragments.SensorCaptureFragment
 *  com.resmed.refresh.ui.fragments.SleepButtonFragment
 *  com.resmed.refresh.ui.fragments.SleepTimeSetupFragment
 *  com.resmed.refresh.ui.fragments.SleepTimeSetupFragment$1
 *  com.resmed.refresh.ui.fragments.SleepTimeSetupFragment$2
 *  com.resmed.refresh.ui.fragments.SleepTimeSetupFragment$3
 *  com.resmed.refresh.ui.fragments.SleepTimeSetupFragment$4
 *  com.resmed.refresh.ui.fragments.SleepTimeSetupFragment$5
 *  com.resmed.refresh.ui.fragments.SleepTimeSetupFragment$6
 *  com.resmed.refresh.ui.fragments.SleepTimeSetupFragment$7
 *  com.resmed.refresh.ui.fragments.SleepTimeSetupFragment$SleepTimeSetupListeners
 *  com.resmed.refresh.ui.fragments.SleepTrackFragment
 *  com.resmed.refresh.ui.uibase.app.RefreshApplication
 *  com.resmed.refresh.ui.uibase.base.BaseActivity
 *  com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity
 *  com.resmed.refresh.ui.uibase.base.BaseFragment
 *  com.resmed.refresh.ui.uibase.base.BluetoothDataListener
 *  com.resmed.refresh.ui.utils.RelaxDataManager
 *  com.resmed.refresh.utils.AppFileLog
 *  com.resmed.refresh.utils.Log
 *  org.achartengine.ChartFactory
 *  org.achartengine.GraphicalView
 *  org.achartengine.model.TimeSeries
 *  org.achartengine.model.XYMultipleSeriesDataset
 *  org.achartengine.model.XYSeries
 *  org.achartengine.renderer.SimpleSeriesRenderer
 *  org.achartengine.renderer.XYMultipleSeriesRenderer
 *  org.achartengine.renderer.XYSeriesRenderer
 */
package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.resmed.refresh.bed.LedsState;
import com.resmed.refresh.bluetooth.BeDConnectionStatus;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_DisplayItem;
import com.resmed.refresh.model.RST_NightQuestions;
import com.resmed.refresh.model.RST_QuestionItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.model.json.ResultRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.customview.PreSleepQuestionView;
import com.resmed.refresh.ui.fragments.SensorCaptureFragment;
import com.resmed.refresh.ui.fragments.SleepButtonFragment;
import com.resmed.refresh.ui.fragments.SleepTimeSetupFragment;
import com.resmed.refresh.ui.fragments.SleepTrackFragment;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.ui.utils.RelaxDataManager;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import java.util.ArrayList;
import java.util.List;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/*
 * Exception performing whole class analysis ignored.
 */
public class SleepTimeSetupFragment
		extends BaseFragment
		implements BluetoothDataListener,
		RST_CallbackItem<RST_Response<RST_NightQuestions>> {

	private class QuestionsAnimation extends Animation
	{
		private int height;
		private int scrollY;

		SleepTimeSetupFragment this$0;
		public QuestionsAnimation(final SleepTimeSetupFragment this$0, final int height) {
			this.this$0 = this$0;
			this.height = height;
			this.scrollY = SleepTimeSetupFragment.access$1(this$0).getScrollY();
		}

		protected void applyTransformation(final float n, final Transformation transformation) {
			super.applyTransformation(n, transformation);
			final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)SleepTimeSetupFragment.access$2(this.this$0).getLayoutParams();
			int height;
			if (n == 1.0f) {
				height = this.height;
				SleepTimeSetupFragment.access$1(this.this$0).scrollTo(0, 0);
			}
			else {
				height = Math.round(n * this.height);
				SleepTimeSetupFragment.access$1(this.this$0).scrollTo(0, (int)(this.scrollY - n * this.scrollY));
			}
			Log.e("questions", "animation value=" + height + " interpolatedTime=" + n);
			layoutParams.height = height;
			SleepTimeSetupFragment.access$2(this.this$0).setLayoutParams((ViewGroup.LayoutParams)layoutParams);
		}
	}


	private static /* synthetic */ int[] $SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE;
	private static int AXIS_X_LENGTH = 0;
	private static final int TIMEOUT_TO_REALSTREAM = 1000;
	private int bio_count_current = Integer.MAX_VALUE - AXIS_X_LENGTH;
	private TimeSeries currentTimeSeries;
	private LinearLayout llSleepPartContent;
	private RelativeLayout llSleepSetupLoading;
	private LinearLayout llSleepSetupMainContent;
	private GraphicalView mChartView;
	private XYMultipleSeriesDataset mDataset;
	private XYMultipleSeriesRenderer mRenderer;
	private int maxY = 2800;
	private int minY = -200;
	private int numberOFQuestionsRequests;
	private RelaxDataManager relaxDataManager;
	private ScrollView scrollSleepSetup;
	private SleepTimeSetupListeners sleepTimeSetupListeners;
	private long timeStampLastRealTime;
	private View.OnClickListener trackListener;
	private Button trackingBtn;
	private LinearLayout wrapperSeekBar;
	private LinearLayout wrapperSeekBarAnimation;

	static /* synthetic */ int[] $SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE() {
		int[] arrn;
		int[] arrn2 = $SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE;
		if (arrn2 != null) {
			return arrn2;
		}
		arrn = new int[CONNECTION_STATE.values().length];
		try {
			arrn[CONNECTION_STATE.BLUETOOTH_OFF.ordinal()] = 4;
		}
		catch (NoSuchFieldError var2_13) {}
		try {
			arrn[CONNECTION_STATE.BLUETOOTH_ON.ordinal()] = 3;
		}
		catch (NoSuchFieldError var3_12) {}
		try {
			arrn[CONNECTION_STATE.NIGHT_TRACK_OFF.ordinal()] = 2;
		}
		catch (NoSuchFieldError var4_11) {}
		try {
			arrn[CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()] = 12;
		}
		catch (NoSuchFieldError var5_10) {}
		try {
			arrn[CONNECTION_STATE.REAL_STREAM_OFF.ordinal()] = 1;
		}
		catch (NoSuchFieldError var6_9) {}
		try {
			arrn[CONNECTION_STATE.REAL_STREAM_ON.ordinal()] = 11;
		}
		catch (NoSuchFieldError var7_8) {}
		try {
			arrn[CONNECTION_STATE.SESSION_OPENED.ordinal()] = 10;
		}
		catch (NoSuchFieldError var8_7) {}
		try {
			arrn[CONNECTION_STATE.SESSION_OPENING.ordinal()] = 9;
		}
		catch (NoSuchFieldError var9_6) {}
		try {
			arrn[CONNECTION_STATE.SOCKET_BROKEN.ordinal()] = 5;
		}
		catch (NoSuchFieldError var10_5) {}
		try {
			arrn[CONNECTION_STATE.SOCKET_CONNECTED.ordinal()] = 8;
		}
		catch (NoSuchFieldError var11_4) {}
		try {
			arrn[CONNECTION_STATE.SOCKET_NOT_CONNECTED.ordinal()] = 7;
		}
		catch (NoSuchFieldError var12_3) {}
		try {
			arrn[CONNECTION_STATE.SOCKET_RECONNECTING.ordinal()] = 6;
		}
		catch (NoSuchFieldError var13_2) {}
		$SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE = arrn;
		return arrn;
	}

	static {
		AXIS_X_LENGTH = 300;
	}

	public SleepTimeSetupFragment() {
		this.trackListener = new /* Unavailable Anonymous Inner Class!! */;
	}

	static /* synthetic */ ScrollView access$1(SleepTimeSetupFragment sleepTimeSetupFragment) {
		return sleepTimeSetupFragment.scrollSleepSetup;
	}

	static /* synthetic */ LinearLayout access$10(SleepTimeSetupFragment sleepTimeSetupFragment) {
		return sleepTimeSetupFragment.wrapperSeekBarAnimation;
	}

	static /* synthetic */ LinearLayout access$2(SleepTimeSetupFragment sleepTimeSetupFragment) {
		return sleepTimeSetupFragment.wrapperSeekBar;
	}

	static /* synthetic */ LinearLayout access$3(SleepTimeSetupFragment sleepTimeSetupFragment) {
		return sleepTimeSetupFragment.llSleepSetupMainContent;
	}

	static /* synthetic */ RelativeLayout access$4(SleepTimeSetupFragment sleepTimeSetupFragment) {
		return sleepTimeSetupFragment.llSleepSetupLoading;
	}

	static /* synthetic */ long access$6(SleepTimeSetupFragment sleepTimeSetupFragment) {
		return sleepTimeSetupFragment.timeStampLastRealTime;
	}

	static /* synthetic */ void access$7(SleepTimeSetupFragment sleepTimeSetupFragment) {
		sleepTimeSetupFragment.checkReceivingRealStream();
	}

	static /* synthetic */ void access$8(SleepTimeSetupFragment sleepTimeSetupFragment, RST_NightQuestions rST_NightQuestions, int n) {
		sleepTimeSetupFragment.addQuestionsToView(rST_NightQuestions, n);
	}

	static /* synthetic */ LinearLayout access$9(SleepTimeSetupFragment sleepTimeSetupFragment) {
		return sleepTimeSetupFragment.llSleepPartContent;
	}

	private void addQuestionsToView(RST_NightQuestions rST_NightQuestions, int n) {
		try {
			this.wrapperSeekBarAnimation.removeAllViews();
			ArrayList<PreSleepQuestionView> arrayList = new ArrayList<PreSleepQuestionView>();
			List list = rST_NightQuestions.getQuestions();
			int n2 = 0;
			block3 : do {
				if (n2 >= list.size()) {
					this.wrapperSeekBarAnimation.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)new /* Unavailable Anonymous Inner Class!! */);
					return;
				}
				List list2 = ((RST_QuestionItem)list.get(n2)).getDisplay();
				ArrayList<String> arrayList2 = new ArrayList<String>();
				int n3 = 0;
				do {
					if (n3 >= list2.size()) {
						Log.e((String)"com.resmed.refresh.ui", (String)("addQuestionsToView " + list2.size() + " size, title = " + ((RST_QuestionItem)list.get(n2)).getText()));
						Log.d((String)"com.resmed.refresh.ui", (String)("SleepTimeSetupFragment onResult => refreshPreSleepQuestions " + list2.size() + " size, title = " + ((RST_QuestionItem)list.get(n2)).getText()));
						AppFileLog.addTrace((String)("SDTeam: refreshPreSleepQuestions: number of answer option " + list2.size() + " for title = " + ((RST_QuestionItem)list.get(n2)).getText()));
						PreSleepQuestionView preSleepQuestionView = new PreSleepQuestionView((Context)this.getActivity(), (RST_QuestionItem)list.get(n2));
						preSleepQuestionView.setSeekBarLabel(arrayList2);
						this.wrapperSeekBarAnimation.addView((View)preSleepQuestionView);
						arrayList.add(preSleepQuestionView);
						++n2;
						continue block3;
					}
					arrayList2.add(((RST_DisplayItem)list2.get(n3)).getValue());
					++n3;
				} while (true);
				break;
			} while (true);
		}
		catch (Exception var3_10) {
			var3_10.printStackTrace();
			return;
		}
	}

	private void checkReceivingRealStream() {
		Log.d((String)"com.resmed.refresh.pair", (String)("checkReconnection currentState=" + (Object)RefreshApplication.getInstance().getCurrentConnectionState()));
		new Handler().postDelayed((Runnable)new /* Unavailable Anonymous Inner Class!! */, 1000);
	}

	private void handleBioData(byte[] arrby) {
		if (arrby.length > 0) {
			int[] arrn = PacketsByteValuesReader.readBioData((byte[])arrby);
			if (this.bio_count_current <= 0) {
				this.bio_count_current = Integer.MAX_VALUE - AXIS_X_LENGTH;
				this.currentTimeSeries.clear();
			}
			this.bio_count_current = -1 + this.bio_count_current;
			this.currentTimeSeries.add((double)this.bio_count_current, (double)arrn[0]);
			this.mRenderer.setXAxisMin((double)this.bio_count_current);
			this.mRenderer.setXAxisMax((double)(this.bio_count_current + AXIS_X_LENGTH));
			if (arrn[0] > this.maxY) {
				this.maxY = arrn[0];
				this.mRenderer.setYAxisMax((double)this.maxY);
			} else if (arrn[0] < this.minY) {
				this.minY = arrn[0];
				this.mRenderer.setYAxisMin((double)this.minY);
			}
			this.timeStampLastRealTime = System.currentTimeMillis();
			BaseBluetoothActivity baseBluetoothActivity = (BaseBluetoothActivity)this.getBaseActivity();
			if (baseBluetoothActivity != null && RefreshApplication.getInstance().getCurrentConnectionState() != CONNECTION_STATE.REAL_STREAM_ON) {
				baseBluetoothActivity.handleConnectionStatus(CONNECTION_STATE.REAL_STREAM_ON);
			}
		}
		this.mChartView.repaint();
	}

	private void handleNoteEnv(byte[] arrby) {
	}

	private void scrollBottom() {
		this.scrollSleepSetup.post((Runnable)new /* Unavailable Anonymous Inner Class!! */);
	}

	private void setBtnLabel() {
		if (this.relaxDataManager.getActiveRelax()) {
			this.trackingBtn.setText(2131165859);
			return;
		}
		this.trackingBtn.setText(2131165859);
	}

	public void handleBreathingRate(Bundle bundle) {
	}

	public void handleConnectionStatus(CONNECTION_STATE cONNECTION_STATE) {
		if (this.getBaseActivity() == null) return;
		if (this.getActivity().isFinishing()) {
			return;
		}
		Log.d((String)"com.resmed.refresh.pair", (String)("SleepTimeSetup handleConnectionStatus CONNECTION_STATE : " + CONNECTION_STATE.toString((CONNECTION_STATE)cONNECTION_STATE)));
		this.trackingBtn.setBackgroundResource(RefreshApplication.getInstance().getConnectionStatus().getBackgroundButtonStreaming());
		BaseBluetoothActivity baseBluetoothActivity = (BaseBluetoothActivity)this.getBaseActivity();
		CONNECTION_STATE cONNECTION_STATE2 = RefreshApplication.getInstance().getCurrentConnectionState();
		switch (SleepTimeSetupFragment.$SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE()[cONNECTION_STATE.ordinal()]) {
			case 4:
			case 6: {
				return;
			}
			default: {
				return;
			}
			case 3: {
				baseBluetoothActivity.connectToBeD(true);
				return;
			}
			case 8:
			case 10:
		}
		if (cONNECTION_STATE2.ordinal() >= CONNECTION_STATE.SESSION_OPENED.ordinal()) return;
		baseBluetoothActivity.sendRpcToBed(BaseBluetoothActivity.getRpcCommands().startRealTimeStream());
		if (SensorCaptureFragment.isInSensorCaptureFrgament) {
			SensorCaptureFragment.isInSensorCaptureFrgament = false;
			return;
		}
		baseBluetoothActivity.sendRpcToBed(BaseBluetoothActivity.getRpcCommands().leds(LedsState.GREEN));
	}

	public void handleEnvSample(Bundle bundle) {
	}

	public void handleReceivedRpc(JsonRPC jsonRPC) {
		if (jsonRPC == null) return;
		ResultRPC resultRPC = jsonRPC.getResult();
		if (resultRPC == null) return;
		if (resultRPC.getPayload().contains("TRUE")) {
			AppFileLog.addTrace((String)("IN : " + jsonRPC.getId() + " PAYLOAD : TERM(TRUE)"));
			return;
		}
		AppFileLog.addTrace((String)("IN : " + jsonRPC.getId() + " PAYLOAD ACK"));
	}

	public void handleSessionRecovered(Bundle bundle) {
	}

	public void handleSleepSessionStopped(Bundle bundle) {
	}

	public void handleStreamPacket(Bundle bundle) {
		byte[] arrby = bundle.getByteArray("REFRESH_BED_NEW_DATA");
		byte by = bundle.getByte("REFRESH_BED_NEW_DATA_TYPE");
		if (VLPacketType.PACKET_TYPE_ENV_1.ordinal() == by) {
			this.handleNoteEnv(arrby);
			return;
		}
		if (VLPacketType.PACKET_TYPE_NOTE_BIO_1.ordinal() != by) return;
		this.handleBioData(arrby);
	}

	public void handleUserSleepState(Bundle bundle) {
	}

	public void nextactivitycall() {
		SleepTrackFragment.recoverFromCrash = false;
		Log.d((String)"com.resmed.refresh.pair", (String)"SleepTrack onClick");
		RefreshModelController.getInstance().updateFlurryEvents("PreSleep_Sleep");
		BaseBluetoothActivity baseBluetoothActivity = (BaseBluetoothActivity)this.getBaseActivity();
		if (!BaseBluetoothActivity.CORRECT_FIRMWARE_VERSION) {
			baseBluetoothActivity.finish();
		}
		if (!baseBluetoothActivity.checkBluetoothEnabled(true)) return;
		CONNECTION_STATE cONNECTION_STATE = RefreshApplication.getInstance().getCurrentConnectionState();
		if (baseBluetoothActivity == null) return;
		if (baseBluetoothActivity.isFinishing()) return;
		Intent intent = this.getBaseActivity().getPendingManageDataIntent();
		if (intent != null) {
			Log.d((String)"com.resmed.refresh.pair", (String)"Has data on BeD");
			this.startActivity(intent);
			return;
		}
		if (cONNECTION_STATE.ordinal() >= CONNECTION_STATE.SESSION_OPENED.ordinal()) {
			Log.d((String)"com.resmed.refresh.pair", (String)"Start a new sleep session");
			JsonRPC jsonRPC = BaseBluetoothActivity.getRpcCommands().stopRealTimeStream();
			((BaseBluetoothActivity)this.getActivity()).sendRpcToBed(jsonRPC);
			this.sleepTimeSetupListeners.sleepTrackClick(false);
			if (!RefreshModelController.getInstance().getActiveRelax()) return;
			RefreshModelController.getInstance().storePlayAutoRelax(true);
			return;
		}
		Log.d((String)"com.resmed.refresh.pair", (String)(" Not connected ? connState : " + (Object)cONNECTION_STATE));
		baseBluetoothActivity.connectToBeD(true);
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.sleepTimeSetupListeners = (SleepTimeSetupListeners)activity;
			return;
		}
		catch (ClassCastException var2_2) {
			throw new ClassCastException(String.valueOf(activity.toString()) + " ...you must implement SleepTrackingBtn from your Activity ;-) !");
		}
	}

	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
		View view = layoutInflater.inflate(2130903168, viewGroup, false);
		this.wrapperSeekBar = (LinearLayout)view.findViewById(2131100388);
		this.wrapperSeekBarAnimation = (LinearLayout)view.findViewById(2131100396);
		this.llSleepSetupMainContent = (LinearLayout)view.findViewById(2131100386);
		this.llSleepPartContent = (LinearLayout)view.findViewById(2131100389);
		this.llSleepSetupLoading = (RelativeLayout)view.findViewById(2131100384);
		this.scrollSleepSetup = (ScrollView)view.findViewById(2131100385);
		this.relaxDataManager = RelaxDataManager.getInstance();
		this.trackingBtn = (Button)view.findViewById(2131100395);
		this.trackingBtn.setOnClickListener(this.trackListener);
		Log.e((String)"com.resmed.refresh.ui", (String)("valid questions = " + RefreshModelController.getInstance().validNightQuestions()));
		RST_NightQuestions rST_NightQuestions = RefreshModelController.getInstance().localNightQuestions();
		if (RefreshModelController.getInstance().validNightQuestions()) {
			if (rST_NightQuestions != null) {
				this.addQuestionsToView(RefreshModelController.getInstance().localNightQuestions(), 500);
			}
			this.llSleepSetupLoading.setVisibility(8);
			this.scrollBottom();
			new Handler(Looper.getMainLooper()).post((Runnable)new /* Unavailable Anonymous Inner Class!! */);
		} else {
			RefreshModelController.getInstance().latestNightQuestions((RST_CallbackItem)this);
			this.numberOFQuestionsRequests = 0;
			this.llSleepSetupMainContent.setVisibility(4);
			this.llSleepSetupLoading.setVisibility(0);
			new Handler(Looper.getMainLooper()).postDelayed((Runnable)new /* Unavailable Anonymous Inner Class!! */, 30000);
		}
		FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
		SleepButtonFragment sleepButtonFragment = new SleepButtonFragment();
		Bundle bundle2 = new Bundle();
		bundle2.putBoolean("displayRelax", this.relaxDataManager.getActiveRelax());
		bundle2.putBoolean("sleepTrackFragment", false);
		sleepButtonFragment.setArguments(bundle2);
		fragmentTransaction.add(2131100391, (Fragment)sleepButtonFragment, "SleepButtonFragmentSetup");
		fragmentTransaction.commit();
		return view;
	}

	public void onDestroy() {
		super.onDestroy();
		if (BaseBluetoothActivity.IN_SLEEP_SESSION) return;
		((BaseBluetoothActivity)this.getBaseActivity()).sendRpcToBed(BaseBluetoothActivity.getRpcCommands().stopRealTimeStream());
	}

	public void onResult(RST_Response<RST_NightQuestions> rST_Response) {
		if (this.getBaseActivity() == null) return;
		if (this.getBaseActivity().isFinishing()) return;
		if (!this.getBaseActivity().isAppValidated(rST_Response.getErrorCode())) {
			return;
		}
		if (rST_Response != null && rST_Response.getStatus() && rST_Response.getResponse() != null) {
			Log.d((String)"com.resmed.refresh.ui", (String)"Sleep questions onResult OK");
			this.scrollBottom();
			new Handler(Looper.getMainLooper()).post(()-> {
				if (Build.VERSION.SDK_INT < 16) {
					SleepTimeSetupFragment.access$10(this.this$0).getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
				}
				else {
					SleepTimeSetupFragment.access$10(this.this$0).getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
				}
				final int measuredHeight = SleepTimeSetupFragment.access$10(this.this$0).getMeasuredHeight();
				Log.e("questions", "wrapperSeekBar heigth=" + measuredHeight);
				final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)SleepTimeSetupFragment.access$2(this.this$0).getLayoutParams();
				layoutParams.height = measuredHeight;
				SleepTimeSetupFragment.access$2(this.this$0).setLayoutParams((ViewGroup$LayoutParams)layoutParams);
				final int childCount = SleepTimeSetupFragment.access$10(this.this$0).getChildCount();
				for (int i = 0; i < childCount; ++i) {
					final int n = -1 + (childCount - i);
					final View child = SleepTimeSetupFragment.access$10(this.this$0).getChildAt(n);
					SleepTimeSetupFragment.access$10(this.this$0).removeViewAt(n);
					SleepTimeSetupFragment.access$2(this.this$0).addView(child);
					final Animation loadAnimation = AnimationUtils.loadAnimation((Context)this.this$0.getActivity(), 2130968582);
					loadAnimation.setDuration((long)350);
					loadAnimation.setStartOffset((long)(350 * (i + 2) + this.val$startDelay));
					child.startAnimation(loadAnimation);
				}
				SleepTimeSetupFragment.access$10(this.this$0).setVisibility(8);
				final SleepTimeSetupFragment.QuestionsAnimation sleepTimeSetupFragment$QuestionsAnimation = new SleepTimeSetupFragment.QuestionsAnimation(this.this$0, measuredHeight);
				sleepTimeSetupFragment$QuestionsAnimation.setDuration((long)(350 * childCount));
				sleepTimeSetupFragment$QuestionsAnimation.setStartOffset((long)(350 + this.val$startDelay));
				sleepTimeSetupFragment$QuestionsAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
				SleepTimeSetupFragment.access$2(this.this$0).startAnimation((Animation)sleepTimeSetupFragment$QuestionsAnimation);
				Log.e("questions", "wrapperSeekBar heigth=" + measuredHeight);
			});
			return;
		}
		Log.d((String)"com.resmed.refresh.ui", (String)"Sleep questions onResult Error");
		if (this.numberOFQuestionsRequests < 2) {
			RefreshModelController.getInstance().latestNightQuestions((RST_CallbackItem)this);
			this.numberOFQuestionsRequests = 1 + this.numberOFQuestionsRequests;
			return;
		}
		if (RefreshModelController.getInstance().localNightQuestions() != null) {
			this.addQuestionsToView(RefreshModelController.getInstance().localNightQuestions(), 500);
		}
		this.llSleepSetupMainContent.setVisibility(0);
		this.llSleepSetupLoading.setVisibility(8);
	}

	public void onResume() {
		super.onResume();
		this.setBtnLabel();
		BaseBluetoothActivity baseBluetoothActivity = (BaseBluetoothActivity)this.getBaseActivity();
		Log.d((String)"com.resmed.refresh.pair", (String)("SleepTimeSetupFragment onResume lastState = " + (Object)RefreshApplication.getInstance().getCurrentConnectionState()));
		if (!baseBluetoothActivity.checkBluetoothEnabled()) return;
		if (RefreshApplication.getInstance().getConnectionStatus().getState() != CONNECTION_STATE.REAL_STREAM_ON) return;
		this.checkReceivingRealStream();
		Log.d((String)"com.resmed.refresh.pair", (String)"SleepTimeSetupFragment checkReceivingRealStream");
	}

	public void onViewCreated(View view, Bundle bundle) {
		super.onViewCreated(view, bundle);
		if (this.mChartView == null) {
			LinearLayout linearLayout = (LinearLayout)this.getBaseActivity().findViewById(2131100394);
			this.mDataset = new XYMultipleSeriesDataset();
			this.mRenderer = new XYMultipleSeriesRenderer();
			this.mRenderer.setBackgroundColor(-1);
			this.mRenderer.setXAxisMax((double)this.bio_count_current);
			this.mRenderer.setXAxisMin((double)(this.bio_count_current + AXIS_X_LENGTH));
			this.mRenderer.setYAxisMax(2800.0);
			this.mRenderer.setYAxisMin(-200.0);
			this.mRenderer.setScale(1.0f);
			this.mRenderer.setPanEnabled(false);
			this.mRenderer.setClickEnabled(false);
			this.mRenderer.setApplyBackgroundColor(true);
			this.mRenderer.setBackgroundColor(0);
			this.mRenderer.setMarginsColor(Color.argb((int)0, (int)1, (int)1, (int)1));
			this.mRenderer.setShowAxes(false);
			this.mRenderer.setShowGrid(false);
			this.mRenderer.setShowLabels(false);
			this.mRenderer.setShowLegend(false);
			this.currentTimeSeries = new TimeSeries("");
			this.mDataset.addSeries((XYSeries)this.currentTimeSeries);
			XYSeriesRenderer xYSeriesRenderer = new XYSeriesRenderer();
			xYSeriesRenderer.setColor(-1);
			xYSeriesRenderer.setLineWidth(4.0f);
			xYSeriesRenderer.setFillPoints(true);
			xYSeriesRenderer.setDisplayChartValues(false);
			xYSeriesRenderer.setShowLegendItem(false);
			this.mRenderer.addSeriesRenderer((SimpleSeriesRenderer)xYSeriesRenderer);
			this.mChartView = ChartFactory.getLineChartView((Context)this.getBaseActivity(), (XYMultipleSeriesDataset)this.mDataset, (XYMultipleSeriesRenderer)this.mRenderer);
			this.mChartView.setBackgroundColor(0);
			linearLayout.addView((View)this.mChartView, (ViewGroup.LayoutParams)new ActionBar.LayoutParams(-2, -2));
			return;
		}
		this.mChartView.repaint();
	}
}
