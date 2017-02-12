package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.resmed.refresh.bed.LedsState;
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
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
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
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class SleepTimeSetupFragment
  extends BaseFragment
  implements BluetoothDataListener, RST_CallbackItem<RST_Response<RST_NightQuestions>>
{
  private static int AXIS_X_LENGTH = 300;
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
  private int minY = 65336;
  private int numberOFQuestionsRequests;
  private RelaxDataManager relaxDataManager;
  private ScrollView scrollSleepSetup;
  private SleepTimeSetupListeners sleepTimeSetupListeners;
  private long timeStampLastRealTime;
  private View.OnClickListener trackListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      SleepTimeSetupFragment.this.nextactivitycall();
    }
  };
  private Button trackingBtn;
  private LinearLayout wrapperSeekBar;
  private LinearLayout wrapperSeekBarAnimation;
  
  private void addQuestionsToView(RST_NightQuestions paramRST_NightQuestions, int paramInt)
  {
    try
    {
      this.wrapperSeekBarAnimation.removeAllViews();
      localObject1 = new java/util/ArrayList;
      ((ArrayList)localObject1).<init>();
      localList = paramRST_NightQuestions.getQuestions();
      i = 0;
      if (i >= localList.size())
      {
        paramRST_NightQuestions = this.wrapperSeekBarAnimation.getViewTreeObserver();
        localObject1 = new com/resmed/refresh/ui/fragments/SleepTimeSetupFragment$7;
        ((7)localObject1).<init>(this, paramInt);
        paramRST_NightQuestions.addOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)localObject1);
        return;
      }
    }
    catch (Exception paramRST_NightQuestions)
    {
      for (;;)
      {
        Object localObject1;
        List localList;
        int i;
        Object localObject2;
        int j;
        paramRST_NightQuestions.printStackTrace();
      }
    }
    localObject2 = ((RST_QuestionItem)localList.get(i)).getDisplay();
    paramRST_NightQuestions = new java/util/ArrayList;
    paramRST_NightQuestions.<init>();
    for (j = 0;; j++)
    {
      if (j >= ((List)localObject2).size())
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>("addQuestionsToView ");
        Log.e("com.resmed.refresh.ui", ((List)localObject2).size() + " size, title = " + ((RST_QuestionItem)localList.get(i)).getText());
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>("SleepTimeSetupFragment onResult => refreshPreSleepQuestions ");
        Log.d("com.resmed.refresh.ui", ((List)localObject2).size() + " size, title = " + ((RST_QuestionItem)localList.get(i)).getText());
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>("SDTeam: refreshPreSleepQuestions: number of answer option ");
        AppFileLog.addTrace(((List)localObject2).size() + " for title = " + ((RST_QuestionItem)localList.get(i)).getText());
        localObject2 = new com/resmed/refresh/ui/customview/PreSleepQuestionView;
        ((PreSleepQuestionView)localObject2).<init>(getActivity(), (RST_QuestionItem)localList.get(i));
        ((PreSleepQuestionView)localObject2).setSeekBarLabel(paramRST_NightQuestions);
        this.wrapperSeekBarAnimation.addView((View)localObject2);
        ((List)localObject1).add(localObject2);
        i++;
        break;
      }
      paramRST_NightQuestions.add(((RST_DisplayItem)((List)localObject2).get(j)).getValue());
    }
  }
  
  private void checkReceivingRealStream()
  {
    Log.d("com.resmed.refresh.pair", "checkReconnection currentState=" + RefreshApplication.getInstance().getCurrentConnectionState());
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        Log.d("com.resmed.refresh.pair", "checkReconnection currentState=" + RefreshApplication.getInstance().getCurrentConnectionState());
        BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)SleepTimeSetupFragment.this.getActivity();
        long l = System.currentTimeMillis();
        if ((localBaseBluetoothActivity != null) && (!localBaseBluetoothActivity.isFinishing()) && (CONNECTION_STATE.REAL_STREAM_ON == RefreshApplication.getInstance().getCurrentConnectionState()))
        {
          if (SleepTimeSetupFragment.this.timeStampLastRealTime - l >= 2000L) {
            break label89;
          }
          SleepTimeSetupFragment.this.checkReceivingRealStream();
        }
        for (;;)
        {
          return;
          label89:
          Log.d("com.resmed.refresh.pair", "checkReconnection request realstream");
          localBaseBluetoothActivity.sendRpcToBed(BaseBluetoothActivity.getRpcCommands().stopRealTimeStream());
        }
      }
    }, 1000L);
  }
  
  private void handleBioData(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length > 0)
    {
      paramArrayOfByte = PacketsByteValuesReader.readBioData(paramArrayOfByte);
      if (this.bio_count_current <= 0)
      {
        this.bio_count_current = (Integer.MAX_VALUE - AXIS_X_LENGTH);
        this.currentTimeSeries.clear();
      }
      this.bio_count_current -= 1;
      this.currentTimeSeries.add(this.bio_count_current, paramArrayOfByte[0]);
      this.mRenderer.setXAxisMin(this.bio_count_current);
      this.mRenderer.setXAxisMax(this.bio_count_current + AXIS_X_LENGTH);
      if (paramArrayOfByte[0] <= this.maxY) {
        break label163;
      }
      this.maxY = paramArrayOfByte[0];
      this.mRenderer.setYAxisMax(this.maxY);
    }
    for (;;)
    {
      this.timeStampLastRealTime = System.currentTimeMillis();
      paramArrayOfByte = (BaseBluetoothActivity)getBaseActivity();
      if ((paramArrayOfByte != null) && (RefreshApplication.getInstance().getCurrentConnectionState() != CONNECTION_STATE.REAL_STREAM_ON)) {
        paramArrayOfByte.handleConnectionStatus(CONNECTION_STATE.REAL_STREAM_ON);
      }
      this.mChartView.repaint();
      return;
      label163:
      if (paramArrayOfByte[0] < this.minY)
      {
        this.minY = paramArrayOfByte[0];
        this.mRenderer.setYAxisMin(this.minY);
      }
    }
  }
  
  private void handleNoteEnv(byte[] paramArrayOfByte) {}
  
  private void scrollBottom()
  {
    this.scrollSleepSetup.post(new Runnable()
    {
      public void run()
      {
        SleepTimeSetupFragment.this.scrollSleepSetup.fullScroll(130);
      }
    });
  }
  
  private void setBtnLabel()
  {
    if (this.relaxDataManager.getActiveRelax()) {
      this.trackingBtn.setText(2131165859);
    }
    for (;;)
    {
      return;
      this.trackingBtn.setText(2131165859);
    }
  }
  
  public void handleBreathingRate(Bundle paramBundle) {}
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    if ((getBaseActivity() == null) || (getActivity().isFinishing())) {}
    for (;;)
    {
      return;
      Log.d("com.resmed.refresh.pair", "SleepTimeSetup handleConnectionStatus CONNECTION_STATE : " + CONNECTION_STATE.toString(paramCONNECTION_STATE));
      this.trackingBtn.setBackgroundResource(RefreshApplication.getInstance().getConnectionStatus().getBackgroundButtonStreaming());
      BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)getBaseActivity();
      CONNECTION_STATE localCONNECTION_STATE = RefreshApplication.getInstance().getCurrentConnectionState();
      switch (paramCONNECTION_STATE)
      {
      case NIGHT_TRACK_ON: 
      case REAL_STREAM_ON: 
      case REAL_STREAM_OFF: 
      case SESSION_OPENED: 
      case SOCKET_BROKEN: 
      default: 
        break;
      case NIGHT_TRACK_OFF: 
        localBaseBluetoothActivity.connectToBeD(true);
        break;
      case SESSION_OPENING: 
      case SOCKET_CONNECTED: 
        if (localCONNECTION_STATE.ordinal() < CONNECTION_STATE.SESSION_OPENED.ordinal())
        {
          localBaseBluetoothActivity.sendRpcToBed(BaseBluetoothActivity.getRpcCommands().startRealTimeStream());
          if (SensorCaptureFragment.isInSensorCaptureFrgament) {
            SensorCaptureFragment.isInSensorCaptureFrgament = false;
          } else {
            localBaseBluetoothActivity.sendRpcToBed(BaseBluetoothActivity.getRpcCommands().leds(LedsState.GREEN));
          }
        }
        break;
      }
    }
  }
  
  public void handleEnvSample(Bundle paramBundle) {}
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC)
  {
    if (paramJsonRPC != null)
    {
      ResultRPC localResultRPC = paramJsonRPC.getResult();
      if (localResultRPC != null)
      {
        if (!localResultRPC.getPayload().contains("TRUE")) {
          break label56;
        }
        AppFileLog.addTrace("IN : " + paramJsonRPC.getId() + " PAYLOAD : TERM(TRUE)");
      }
    }
    for (;;)
    {
      return;
      label56:
      AppFileLog.addTrace("IN : " + paramJsonRPC.getId() + " PAYLOAD ACK");
    }
  }
  
  public void handleSessionRecovered(Bundle paramBundle) {}
  
  public void handleSleepSessionStopped(Bundle paramBundle) {}
  
  public void handleStreamPacket(Bundle paramBundle)
  {
    byte[] arrayOfByte = paramBundle.getByteArray("REFRESH_BED_NEW_DATA");
    int i = paramBundle.getByte("REFRESH_BED_NEW_DATA_TYPE");
    if (VLPacketType.PACKET_TYPE_ENV_1.ordinal() == i) {
      handleNoteEnv(arrayOfByte);
    }
    for (;;)
    {
      return;
      if (VLPacketType.PACKET_TYPE_NOTE_BIO_1.ordinal() == i) {
        handleBioData(arrayOfByte);
      }
    }
  }
  
  public void handleUserSleepState(Bundle paramBundle) {}
  
  public void nextactivitycall()
  {
    SleepTrackFragment.recoverFromCrash = false;
    Log.d("com.resmed.refresh.pair", "SleepTrack onClick");
    RefreshModelController.getInstance().updateFlurryEvents("PreSleep_Sleep");
    BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)getBaseActivity();
    if (!BaseBluetoothActivity.CORRECT_FIRMWARE_VERSION) {
      localBaseBluetoothActivity.finish();
    }
    CONNECTION_STATE localCONNECTION_STATE;
    Object localObject;
    if (localBaseBluetoothActivity.checkBluetoothEnabled(true))
    {
      localCONNECTION_STATE = RefreshApplication.getInstance().getCurrentConnectionState();
      if ((localBaseBluetoothActivity != null) && (!localBaseBluetoothActivity.isFinishing()))
      {
        localObject = getBaseActivity().getPendingManageDataIntent();
        if (localObject == null) {
          break label95;
        }
        Log.d("com.resmed.refresh.pair", "Has data on BeD");
        startActivity((Intent)localObject);
      }
    }
    for (;;)
    {
      return;
      label95:
      if (localCONNECTION_STATE.ordinal() >= CONNECTION_STATE.SESSION_OPENED.ordinal())
      {
        Log.d("com.resmed.refresh.pair", "Start a new sleep session");
        localObject = BaseBluetoothActivity.getRpcCommands().stopRealTimeStream();
        ((BaseBluetoothActivity)getActivity()).sendRpcToBed((JsonRPC)localObject);
        this.sleepTimeSetupListeners.sleepTrackClick(false);
        if (RefreshModelController.getInstance().getActiveRelax()) {
          RefreshModelController.getInstance().storePlayAutoRelax(true);
        }
      }
      else
      {
        Log.d("com.resmed.refresh.pair", " Not connected ? connState : " + localCONNECTION_STATE);
        localBaseBluetoothActivity.connectToBeD(true);
      }
    }
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.sleepTimeSetupListeners = ((SleepTimeSetupListeners)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement SleepTrackingBtn from your Activity ;-) !");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903168, paramViewGroup, false);
    this.wrapperSeekBar = ((LinearLayout)paramLayoutInflater.findViewById(2131100388));
    this.wrapperSeekBarAnimation = ((LinearLayout)paramLayoutInflater.findViewById(2131100396));
    this.llSleepSetupMainContent = ((LinearLayout)paramLayoutInflater.findViewById(2131100386));
    this.llSleepPartContent = ((LinearLayout)paramLayoutInflater.findViewById(2131100389));
    this.llSleepSetupLoading = ((RelativeLayout)paramLayoutInflater.findViewById(2131100384));
    this.scrollSleepSetup = ((ScrollView)paramLayoutInflater.findViewById(2131100385));
    this.relaxDataManager = RelaxDataManager.getInstance();
    this.trackingBtn = ((Button)paramLayoutInflater.findViewById(2131100395));
    this.trackingBtn.setOnClickListener(this.trackListener);
    Log.e("com.resmed.refresh.ui", "valid questions = " + RefreshModelController.getInstance().validNightQuestions());
    paramViewGroup = RefreshModelController.getInstance().localNightQuestions();
    if (RefreshModelController.getInstance().validNightQuestions())
    {
      if (paramViewGroup != null) {
        addQuestionsToView(RefreshModelController.getInstance().localNightQuestions(), 500);
      }
      this.llSleepSetupLoading.setVisibility(8);
      scrollBottom();
      new Handler(Looper.getMainLooper()).post(new Runnable()
      {
        public void run()
        {
          SleepTimeSetupFragment.this.scrollSleepSetup.fullScroll(130);
          SleepTimeSetupFragment.this.llSleepSetupMainContent.setVisibility(0);
          Log.e("com.resmed.refresh.ui", "llSleepSetupMainContent visible");
        }
      });
    }
    for (;;)
    {
      paramViewGroup = getFragmentManager().beginTransaction();
      SleepButtonFragment localSleepButtonFragment = new SleepButtonFragment();
      paramBundle = new Bundle();
      paramBundle.putBoolean("displayRelax", this.relaxDataManager.getActiveRelax());
      paramBundle.putBoolean("sleepTrackFragment", false);
      localSleepButtonFragment.setArguments(paramBundle);
      paramViewGroup.add(2131100391, localSleepButtonFragment, "SleepButtonFragmentSetup");
      paramViewGroup.commit();
      return paramLayoutInflater;
      RefreshModelController.getInstance().latestNightQuestions(this);
      this.numberOFQuestionsRequests = 0;
      this.llSleepSetupMainContent.setVisibility(4);
      this.llSleepSetupLoading.setVisibility(0);
      new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
      {
        public void run()
        {
          if (SleepTimeSetupFragment.this.llSleepSetupMainContent != null)
          {
            SleepTimeSetupFragment.this.llSleepSetupMainContent.setVisibility(0);
            SleepTimeSetupFragment.this.llSleepSetupLoading.setVisibility(8);
            Log.e("com.resmed.refresh.ui", "llSleepSetupMainContent visible");
          }
        }
      }, 30000L);
    }
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    if (!BaseBluetoothActivity.IN_SLEEP_SESSION) {
      ((BaseBluetoothActivity)getBaseActivity()).sendRpcToBed(BaseBluetoothActivity.getRpcCommands().stopRealTimeStream());
    }
  }
  
  public void onResult(final RST_Response<RST_NightQuestions> paramRST_Response)
  {
    if ((getBaseActivity() == null) || (getBaseActivity().isFinishing()) || (!getBaseActivity().isAppValidated(paramRST_Response.getErrorCode()))) {}
    for (;;)
    {
      return;
      if ((paramRST_Response != null) && (paramRST_Response.getStatus()) && (paramRST_Response.getResponse() != null))
      {
        Log.d("com.resmed.refresh.ui", "Sleep questions onResult OK");
        scrollBottom();
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
          public void run()
          {
            if ((SleepTimeSetupFragment.this.getBaseActivity() != null) && (!SleepTimeSetupFragment.this.getBaseActivity().isFinishing()))
            {
              SleepTimeSetupFragment.this.addQuestionsToView((RST_NightQuestions)paramRST_Response.getResponse(), 1000);
              Animation localAnimation = AnimationUtils.loadAnimation(SleepTimeSetupFragment.this.getActivity(), 2130968582);
              localAnimation.setInterpolator(new AccelerateInterpolator());
              if (SleepTimeSetupFragment.this.llSleepSetupMainContent.getVisibility() != 0)
              {
                SleepTimeSetupFragment.this.llSleepPartContent.startAnimation(localAnimation);
                SleepTimeSetupFragment.this.llSleepSetupMainContent.setVisibility(0);
              }
              SleepTimeSetupFragment.this.llSleepSetupLoading.setVisibility(8);
            }
          }
        });
      }
      else
      {
        Log.d("com.resmed.refresh.ui", "Sleep questions onResult Error");
        if (this.numberOFQuestionsRequests < 2)
        {
          RefreshModelController.getInstance().latestNightQuestions(this);
          this.numberOFQuestionsRequests += 1;
        }
        else
        {
          if (RefreshModelController.getInstance().localNightQuestions() != null) {
            addQuestionsToView(RefreshModelController.getInstance().localNightQuestions(), 500);
          }
          this.llSleepSetupMainContent.setVisibility(0);
          this.llSleepSetupLoading.setVisibility(8);
        }
      }
    }
  }
  
  public void onResume()
  {
    super.onResume();
    setBtnLabel();
    BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)getBaseActivity();
    Log.d("com.resmed.refresh.pair", "SleepTimeSetupFragment onResume lastState = " + RefreshApplication.getInstance().getCurrentConnectionState());
    if ((localBaseBluetoothActivity.checkBluetoothEnabled()) && (RefreshApplication.getInstance().getConnectionStatus().getState() == CONNECTION_STATE.REAL_STREAM_ON))
    {
      checkReceivingRealStream();
      Log.d("com.resmed.refresh.pair", "SleepTimeSetupFragment checkReceivingRealStream");
    }
  }
  
  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    if (this.mChartView == null)
    {
      paramBundle = (LinearLayout)getBaseActivity().findViewById(2131100394);
      this.mDataset = new XYMultipleSeriesDataset();
      this.mRenderer = new XYMultipleSeriesRenderer();
      this.mRenderer.setBackgroundColor(-1);
      this.mRenderer.setXAxisMax(this.bio_count_current);
      this.mRenderer.setXAxisMin(this.bio_count_current + AXIS_X_LENGTH);
      this.mRenderer.setYAxisMax(2800.0D);
      this.mRenderer.setYAxisMin(-200.0D);
      this.mRenderer.setScale(1.0F);
      this.mRenderer.setPanEnabled(false);
      this.mRenderer.setClickEnabled(false);
      this.mRenderer.setApplyBackgroundColor(true);
      this.mRenderer.setBackgroundColor(0);
      this.mRenderer.setMarginsColor(Color.argb(0, 1, 1, 1));
      this.mRenderer.setShowAxes(false);
      this.mRenderer.setShowGrid(false);
      this.mRenderer.setShowLabels(false);
      this.mRenderer.setShowLegend(false);
      this.currentTimeSeries = new TimeSeries("");
      this.mDataset.addSeries(this.currentTimeSeries);
      paramView = new XYSeriesRenderer();
      paramView.setColor(-1);
      paramView.setLineWidth(4.0F);
      paramView.setFillPoints(true);
      paramView.setDisplayChartValues(false);
      paramView.setShowLegendItem(false);
      this.mRenderer.addSeriesRenderer(paramView);
      this.mChartView = ChartFactory.getLineChartView(getBaseActivity(), this.mDataset, this.mRenderer);
      this.mChartView.setBackgroundColor(0);
      paramBundle.addView(this.mChartView, new ActionBar.LayoutParams(-2, -2));
    }
    for (;;)
    {
      return;
      this.mChartView.repaint();
    }
  }
  
  private class QuestionsAnimation
    extends Animation
  {
    private int height;
    private int scrollY;
    
    public QuestionsAnimation(int paramInt)
    {
      this.height = paramInt;
      this.scrollY = SleepTimeSetupFragment.this.scrollSleepSetup.getScrollY();
    }
    
    protected void applyTransformation(float paramFloat, Transformation paramTransformation)
    {
      super.applyTransformation(paramFloat, paramTransformation);
      paramTransformation = (LinearLayout.LayoutParams)SleepTimeSetupFragment.this.wrapperSeekBar.getLayoutParams();
      int i;
      if (paramFloat == 1.0F)
      {
        i = this.height;
        SleepTimeSetupFragment.this.scrollSleepSetup.scrollTo(0, 0);
      }
      for (;;)
      {
        Log.e("questions", "animation value=" + i + " interpolatedTime=" + paramFloat);
        paramTransformation.height = i;
        SleepTimeSetupFragment.this.wrapperSeekBar.setLayoutParams(paramTransformation);
        return;
        i = Math.round(this.height * paramFloat);
        SleepTimeSetupFragment.this.scrollSleepSetup.scrollTo(0, (int)(this.scrollY - this.scrollY * paramFloat));
      }
    }
  }
  
  public static abstract interface SleepTimeSetupListeners
  {
    public abstract void sleepTrackClick(boolean paramBoolean);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SleepTimeSetupFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */