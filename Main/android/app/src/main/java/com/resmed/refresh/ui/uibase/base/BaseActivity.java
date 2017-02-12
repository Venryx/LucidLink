package com.resmed.refresh.ui.uibase.base;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.resmed.refresh.bluetooth.BeDConnectionStatus;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.activity.DismissAlarmActivty;
import com.resmed.refresh.ui.activity.EmailNotValidatedActivity;
import com.resmed.refresh.ui.activity.ManageDataOnBeDActivity;
import com.resmed.refresh.ui.activity.MenuActivity;
import com.resmed.refresh.ui.activity.RecoverDataOnBeDActivity;
import com.resmed.refresh.ui.activity.SensorSetupActivity;
import com.resmed.refresh.ui.fragments.RefreshDialogFragment;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;
import com.resmed.refresh.ui.utils.RollingBackgroundManager;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RunningActivityManager;

public abstract class BaseActivity
  extends FragmentActivity
  implements RST_CallbackItem<RST_Response<RST_UserProfile>>
{
  public static final int BUTTON_DONE = 2130837770;
  public static final int BUTTON_EDIT = 2130837773;
  public static final int BUTTON_SAVE = 2130837782;
  public static final int BUTTON_SHARE = 2130837785;
  protected Typeface akzidenzBold;
  protected Typeface akzidenzLight;
  private BaseActivity baseActivity;
  private BroadcastReceiver connectionStatusReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = (CONNECTION_STATE)paramAnonymousIntent.getExtras().get("EXTRA_RESMED_CONNECTION_STATE");
      Log.d("com.resmed.refresh.pair", " onReceive()  CONNECTION_STATE " + CONNECTION_STATE.toString(paramAnonymousContext));
      Log.d("com.resmed.refresh.ui", " onReceive()  CONNECTION_STATE " + CONNECTION_STATE.toString(paramAnonymousContext));
      BaseActivity.this.updateConnectionIcon();
    }
  };
  private int currentRigthIcon;
  private RelativeLayout headerLayout;
  protected boolean isAvailable;
  protected boolean isModalActivity;
  protected boolean isRemoveProfileFragment;
  protected boolean isShowingBlockDialog;
  private ImageView ivBackground;
  private ImageView ivHeaderLeftIcon;
  private ImageView ivHeaderRightButton;
  private ImageView ivHeaderSensor;
  private RelativeLayout layoutContentFragment;
  public ProgressBar progressBarpercentage;
  protected boolean rollingBackground = true;
  private RollingBackgroundManager rollingBackgroundManager;
  public RelativeLayout rr_progressBarpercentage;
  public TextView textviewpercentage;
  private TextView tvHeaderTitle;
  protected TypeBar typeBar;
  
  private void saveData()
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    localRefreshModelController.save();
    localRefreshModelController.updateUserProfile(this);
  }
  
  private void showDialog(CustomDialogBuilder paramCustomDialogBuilder, boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      Object localObject1 = new java.lang.StringBuilder;
      ((StringBuilder)localObject1).<init>("BaseActivity showDialog cancelable = ");
      Log.d("com.resmed.refresh.dialog", paramBoolean1);
      if (isActivityReadyToCommit())
      {
        localObject1 = getFragmentManager().beginTransaction();
        Object localObject2 = getFragmentManager().findFragmentByTag("dialog");
        if (localObject2 != null)
        {
          Log.d("com.resmed.refresh.dialog", "BaseActivity dismiss prev dialog");
          ((DialogFragment)localObject2).dismiss();
        }
        getFragmentManager().executePendingTransactions();
        ((FragmentTransaction)localObject1).addToBackStack(null);
        localObject2 = RefreshDialogFragment.newInstance(paramBoolean2);
        ((RefreshDialogFragment)localObject2).setView(paramCustomDialogBuilder.createView());
        ((RefreshDialogFragment)localObject2).setCancelable(paramBoolean1);
        ((RefreshDialogFragment)localObject2).show((FragmentTransaction)localObject1, "dialog");
        Log.d("com.resmed.refresh.dialog", "BaseActivity dialogFragment.show");
      }
      return;
    }
    finally {}
  }
  
  private void updateBEDIcon(int paramInt)
  {
    if (this.ivHeaderSensor != null)
    {
      if (!BaseBluetoothActivity.IN_SLEEP_SESSION) {
        break label38;
      }
      this.ivHeaderSensor.setEnabled(false);
    }
    for (;;)
    {
      this.ivHeaderSensor.setAnimation(null);
      this.ivHeaderSensor.setImageResource(paramInt);
      return;
      label38:
      this.ivHeaderSensor.setEnabled(true);
    }
  }
  
  protected void backBtnPressed()
  {
    this.baseActivity.finish();
    if (this.isModalActivity) {
      overridePendingTransition(2130968586, 2130968588);
    }
  }
  
  public void changeRefreshBarVisibility(boolean paramBoolean1, boolean paramBoolean2)
  {
    int i;
    if (paramBoolean1)
    {
      if (this.headerLayout.getVisibility() == 0) {
        return;
      }
      i = 0;
    }
    for (Animation localAnimation = AnimationUtils.loadAnimation(this.baseActivity, 2130968587);; localAnimation = AnimationUtils.loadAnimation(this.baseActivity, 2130968598))
    {
      this.headerLayout.setVisibility(i);
      if (!paramBoolean2) {
        break;
      }
      this.headerLayout.startAnimation(localAnimation);
      break;
      if (this.headerLayout.getVisibility() != 0) {
        break;
      }
      i = 8;
    }
  }
  
  public void dismissDialog()
  {
    try
    {
      Log.d("com.resmed.refresh.dialog", "BaseActivity dismissDialog");
      if (isActivityReadyToCommit())
      {
        this.isShowingBlockDialog = false;
        Fragment localFragment = getFragmentManager().findFragmentByTag("dialog");
        if (localFragment != null) {
          ((DialogFragment)localFragment).dismiss();
        }
        getFragmentManager().executePendingTransactions();
      }
      return;
    }
    finally {}
  }
  
  public void dismissProgressDialog()
  {
    if (isActivityReadyToCommit())
    {
      Fragment localFragment = getFragmentManager().findFragmentByTag("dialog");
      if ((localFragment != null) && (localFragment.getClass().isAssignableFrom(RefreshDialogFragment.class))) {
        ((RefreshDialogFragment)localFragment).dismiss();
      }
      Log.d("com.resmed.refresh.dialog", "BaseActivity dismissProgressDialog");
    }
  }
  
  public Intent getPendingManageDataIntent()
  {
    int i = RefreshApplication.getInstance().getConnectionStatus().getBeDDataFlag();
    Intent localIntent = null;
    if (i == VLPacketType.PACKET_TYPE_NOTE_STORE_LOCAL.ordinal())
    {
      localIntent = new Intent(this.baseActivity, RecoverDataOnBeDActivity.class);
      localIntent.putExtra("com.resmed.refresh.ui.uibase.app.activity_modal", true);
    }
    for (;;)
    {
      if (localIntent != null)
      {
        localIntent.putExtra("com.resmed.refresh.ui.uibase.app.activity_modal", true);
        this.baseActivity.startActivity(localIntent);
        this.baseActivity.overridePendingTransition(2130968597, 2130968586);
      }
      return localIntent;
      if (i == VLPacketType.PACKET_TYPE_NOTE_STORE_FOREIGN.ordinal())
      {
        localIntent = new Intent(this.baseActivity, ManageDataOnBeDActivity.class);
        localIntent.putExtra("com.resmed.refresh.ui.uibase.app.activity_modal", true);
      }
    }
  }
  
  public void hideLeftButton()
  {
    if (this.ivHeaderLeftIcon.getVisibility() == 0)
    {
      Animation localAnimation = AnimationUtils.loadAnimation(this, 2130968585);
      localAnimation.setAnimationListener(new Animation.AnimationListener()
      {
        public void onAnimationEnd(Animation paramAnonymousAnimation)
        {
          BaseActivity.this.ivHeaderLeftIcon.setVisibility(8);
        }
        
        public void onAnimationRepeat(Animation paramAnonymousAnimation) {}
        
        public void onAnimationStart(Animation paramAnonymousAnimation) {}
      });
      this.ivHeaderLeftIcon.startAnimation(localAnimation);
    }
  }
  
  public void hideRightButton()
  {
    if (this.ivHeaderRightButton.getVisibility() == 0)
    {
      this.ivHeaderRightButton.startAnimation(AnimationUtils.loadAnimation(this, 2130968585));
      this.ivHeaderRightButton.setVisibility(8);
      if ((this.typeBar != TypeBar.NO_BED) || (this.typeBar != TypeBar.PROFILE_QUESTIONAIRE))
      {
        this.ivHeaderSensor.startAnimation(AnimationUtils.loadAnimation(this, 2130968584));
        this.ivHeaderSensor.setVisibility(0);
      }
    }
  }
  
  public boolean isActivityReadyToCommit()
  {
    boolean bool2 = true;
    if ((this != null) && (!isFinishing()) && (this.isAvailable))
    {
      Log.d("com.resmed.refresh.dialog", "isActivityAvaible = true");
      if ((this == null) || (isFinishing()) || (!this.isAvailable)) {
        break label135;
      }
    }
    label68:
    label130:
    label135:
    for (boolean bool1 = bool2;; bool1 = false)
    {
      return bool1;
      StringBuilder localStringBuilder = new StringBuilder("isActivityAvaible = false (");
      if (this != null)
      {
        bool1 = true;
        localStringBuilder = localStringBuilder.append(bool1).append(", ");
        if (!isFinishing()) {
          break label130;
        }
      }
      for (bool1 = false;; bool1 = true)
      {
        Log.d("com.resmed.refresh.dialog", bool1 + ", " + this.isAvailable + ")");
        break;
        bool1 = false;
        break label68;
      }
    }
  }
  
  public boolean isAppValidated(int paramInt)
  {
    return isAppValidated(paramInt, true);
  }
  
  public boolean isAppValidated(int paramInt, boolean paramBoolean)
  {
    Intent localIntent = null;
    boolean bool = true;
    if (paramInt == 601)
    {
      localIntent = new Intent(this, EmailNotValidatedActivity.class);
      RefreshModelController.getInstance().setHasToValidateEmail(true);
      bool = false;
    }
    for (;;)
    {
      if ((paramBoolean) && (localIntent != null))
      {
        localIntent = new Intent(this, EmailNotValidatedActivity.class);
        localIntent.setFlags(268468224);
        startActivity(localIntent);
        overridePendingTransition(2130968597, 2130968586);
      }
      return bool;
      if (paramInt == 602)
      {
        localIntent = new Intent(this, EmailNotValidatedActivity.class);
        RefreshModelController.getInstance().setHasToValidateEmail(true);
        bool = false;
      }
      else
      {
        RefreshModelController.getInstance().setHasToValidateEmail(false);
      }
    }
  }
  
  public boolean isRollingBackground()
  {
    return this.rollingBackground;
  }
  
  public void onBackPressed()
  {
    super.onBackPressed();
    if (this.isRemoveProfileFragment)
    {
      saveData();
      this.baseActivity.finish();
      this.baseActivity.overridePendingTransition(2130968591, 2130968596);
    }
    if (this.isModalActivity)
    {
      System.out.println("backpress called");
      overridePendingTransition(2130968586, 2130968588);
    }
  }
  
  public void onBackgroundChange(String paramString)
  {
    int i = getResources().getIdentifier(paramString, "drawable", getPackageName());
    this.ivBackground.setBackgroundResource(i);
    paramString = AnimationUtils.loadAnimation(this, 2130968582);
    this.ivBackground.startAnimation(paramString);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.baseActivity = this;
    this.akzidenzBold = Typeface.createFromAsset(getAssets(), "AkzidenzGroteskBE-Bold.otf");
    this.akzidenzLight = Typeface.createFromAsset(getAssets(), "AkzidenzGroteskBE-Light.otf");
    Log.d("com.resmed.refresh.pair", " onCreate ref : " + this);
    this.isAvailable = true;
    this.isShowingBlockDialog = false;
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    RunningActivityManager.getInstance().unregisterCurrentActivity(this);
    try
    {
      if ((this.rollingBackground) && (this.rollingBackgroundManager != null)) {
        this.rollingBackgroundManager.unRegisterActivity(this);
      }
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;) {}
    }
  }
  
  protected void onPause()
  {
    super.onPause();
    RefreshApplication.getInstance().decreaseActivitiesInForeground();
    try
    {
      unregisterReceiver(this.connectionStatusReceiver);
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;) {}
    }
  }
  
  protected void onPostResume()
  {
    this.isAvailable = true;
    super.onPostResume();
  }
  
  public void onResult(RST_Response<RST_UserProfile> paramRST_Response)
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    if (paramRST_Response.isStatus()) {}
    for (boolean bool = false;; bool = true)
    {
      localRefreshModelController.setProfileUpdatePending(bool);
      Log.i("Http Response=> ", paramRST_Response.isStatus() + "  result=> " + paramRST_Response.getResponse());
      return;
    }
  }
  
  protected void onResume()
  {
    super.onResume();
    RefreshApplication.getInstance().increaseActivitiesInForeground();
    registerReceiver(this.connectionStatusReceiver, new IntentFilter("ACTION_RESMED_CONNECTION_STATUS"));
    RunningActivityManager.getInstance().registerCurrentActivity(this);
    updateConnectionIcon();
  }
  
  protected void onResumeFragments()
  {
    this.isAvailable = true;
    super.onResumeFragments();
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    this.isAvailable = false;
    super.onSaveInstanceState(paramBundle);
    Log.d("com.resmed.refresh.dialog", "BaseActivity onSaveInstanceState " + this);
  }
  
  protected void onStop()
  {
    this.isAvailable = false;
    super.onStop();
  }
  
  protected void rightBtnPressed() {}
  
  public void setContentView(int paramInt)
  {
    super.setContentView(2130903064);
    this.layoutContentFragment = ((RelativeLayout)findViewById(2131099765));
    View localView = getLayoutInflater().inflate(paramInt, null);
    this.layoutContentFragment.addView(localView);
    try
    {
      Object localObject = new android/widget/RelativeLayout$LayoutParams;
      ((RelativeLayout.LayoutParams)localObject).<init>(-1, -1);
      localView.setLayoutParams((ViewGroup.LayoutParams)localObject);
      this.headerLayout = ((RelativeLayout)findViewById(2131099986));
      this.tvHeaderTitle = ((TextView)findViewById(2131100595));
      this.ivHeaderLeftIcon = ((ImageView)findViewById(2131100592));
      this.ivBackground = ((ImageView)findViewById(2131099764));
      this.ivHeaderSensor = ((ImageView)findViewById(2131100593));
      this.ivHeaderRightButton = ((ImageView)findViewById(2131100594));
      this.rr_progressBarpercentage = ((RelativeLayout)findViewById(2131100596));
      this.progressBarpercentage = ((ProgressBar)findViewById(2131100597));
      this.textviewpercentage = ((TextView)findViewById(2131100598));
      this.ivHeaderRightButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          BaseActivity.this.rightBtnPressed();
        }
      });
      this.ivHeaderSensor.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          boolean bool = RefreshApplication.getInstance().getConnectionStatus().isSocketConnected();
          paramAnonymousView = BaseActivity.this.getPendingManageDataIntent();
          if ((paramAnonymousView == null) || (!bool)) {
            paramAnonymousView = new Intent(BaseActivity.this.baseActivity, SensorSetupActivity.class);
          }
          paramAnonymousView.putExtra("com.resmed.refresh.ui.uibase.app.activity_modal", true);
          BaseActivity.this.baseActivity.startActivity(paramAnonymousView);
          BaseActivity.this.baseActivity.overridePendingTransition(2130968597, 2130968586);
        }
      });
      this.ivHeaderLeftIcon.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          paramAnonymousView = new Intent(BaseActivity.this.baseActivity, MenuActivity.class);
          paramAnonymousView.putExtra("com.resmed.refresh.ui.uibase.app.activity_modal", true);
          BaseActivity.this.baseActivity.startActivity(paramAnonymousView);
          BaseActivity.this.baseActivity.overridePendingTransition(2130968597, 2130968586);
        }
      });
      setTypeRefreshBar(TypeBar.DEFAULT);
      updateConnectionIcon();
      if (this.rollingBackground)
      {
        this.rollingBackgroundManager = RollingBackgroundManager.getInstance(getApplicationContext());
        localObject = this.rollingBackgroundManager.registerActivity(this);
        paramInt = getResources().getIdentifier((String)localObject, "drawable", getPackageName());
        this.ivBackground.setBackgroundResource(paramInt);
      }
      return;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        localException.printStackTrace();
      }
    }
  }
  
  public void setDialogProgress(int paramInt)
  {
    Object localObject;
    if (isActivityReadyToCommit())
    {
      localObject = getFragmentManager().findFragmentByTag("dialog");
      if ((localObject != null) && (localObject.getClass().isAssignableFrom(RefreshDialogFragment.class)))
      {
        localObject = (RefreshDialogFragment)localObject;
        if (paramInt != 100) {
          break label71;
        }
        this.isShowingBlockDialog = false;
        Log.d("com.resmed.refresh.dialog", "BaseActivity progress 100 dismissProgresDialog");
        ((RefreshDialogFragment)localObject).dismiss();
        getFragmentManager().executePendingTransactions();
      }
    }
    for (;;)
    {
      return;
      label71:
      ((RefreshDialogFragment)localObject).updateProgress(paramInt);
    }
  }
  
  protected void setIsModalActivity(boolean paramBoolean)
  {
    this.isModalActivity = paramBoolean;
  }
  
  protected void setIsRemoveProfileFragment(boolean paramBoolean)
  {
    this.isRemoveProfileFragment = paramBoolean;
  }
  
  public void setProgressBarpercentage(int paramInt)
  {
    this.progressBarpercentage.setProgress(paramInt);
    this.textviewpercentage.setText(paramInt + "%");
  }
  
  public void setRollingBackground(ImageView paramImageView)
  {
    if (paramImageView != null)
    {
      String str = RollingBackgroundManager.getInstance(getApplicationContext()).getCurrentBackground();
      paramImageView.setBackgroundResource(getResources().getIdentifier(str, "drawable", getPackageName()));
    }
  }
  
  public void setTitle(int paramInt)
  {
    super.setTitle(paramInt);
    if (this.tvHeaderTitle != null) {
      this.tvHeaderTitle.setText(paramInt);
    }
  }
  
  protected void setTypeRefreshBar(TypeBar paramTypeBar)
  {
    this.typeBar = paramTypeBar;
    switch (paramTypeBar)
    {
    }
    for (;;)
    {
      return;
      this.ivHeaderLeftIcon.setVisibility(8);
      this.rr_progressBarpercentage.setVisibility(8);
      continue;
      this.ivHeaderLeftIcon.setVisibility(0);
      this.rr_progressBarpercentage.setVisibility(8);
      continue;
      this.ivHeaderLeftIcon.setVisibility(8);
      this.ivHeaderSensor.setVisibility(8);
      this.ivHeaderSensor.setImageResource(0);
      this.rr_progressBarpercentage.setVisibility(8);
      continue;
      this.ivHeaderLeftIcon.setVisibility(8);
      this.ivHeaderSensor.setVisibility(8);
      this.ivHeaderSensor.setImageResource(0);
      this.rr_progressBarpercentage.setVisibility(0);
    }
  }
  
  public void showBlockingDialog(CustomDialogBuilder paramCustomDialogBuilder)
  {
    this.isShowingBlockDialog = true;
    showDialog(paramCustomDialogBuilder, false, false);
  }
  
  public void showDialog(CustomDialogBuilder paramCustomDialogBuilder)
  {
    if (this.isShowingBlockDialog) {}
    for (;;)
    {
      return;
      showDialog(paramCustomDialogBuilder, true);
    }
  }
  
  public void showDialog(CustomDialogBuilder paramCustomDialogBuilder, boolean paramBoolean)
  {
    if (this.isShowingBlockDialog) {}
    for (;;)
    {
      return;
      showDialog(paramCustomDialogBuilder, paramBoolean, false);
    }
  }
  
  public void showErrorDialog(RST_Response<?> paramRST_Response)
  {
    showErrorDialog(paramRST_Response.getErrorMessage());
  }
  
  public void showErrorDialog(String paramString)
  {
    showDialog(new CustomDialogBuilder(this).title(2131165301).description(paramString).setPositiveButton(2131165296, null));
  }
  
  public void showFullScreenDialog(CustomDialogBuilder paramCustomDialogBuilder, boolean paramBoolean)
  {
    if (this.isShowingBlockDialog) {}
    for (;;)
    {
      return;
      showDialog(paramCustomDialogBuilder, paramBoolean, true);
    }
  }
  
  public void showLeftButton()
  {
    if (this.ivHeaderLeftIcon.getVisibility() == 8)
    {
      this.ivHeaderLeftIcon.startAnimation(AnimationUtils.loadAnimation(this, 2130968584));
      this.ivHeaderLeftIcon.setVisibility(0);
    }
  }
  
  public void showProgressDialog(int paramInt)
  {
    showDialog(new CustomDialogBuilder(this).progress(getString(paramInt)), false);
  }
  
  public void showProgressDialog(String paramString)
  {
    showDialog(new CustomDialogBuilder(this).progress(paramString), false);
  }
  
  public void showRightButton(int paramInt)
  {
    if (this.ivHeaderRightButton.getVisibility() != 0)
    {
      this.ivHeaderRightButton.startAnimation(AnimationUtils.loadAnimation(this, 2130968584));
      this.ivHeaderSensor.startAnimation(AnimationUtils.loadAnimation(this, 2130968585));
      this.ivHeaderRightButton.setVisibility(0);
      this.ivHeaderSensor.setVisibility(8);
      this.ivHeaderRightButton.setImageResource(paramInt);
    }
    for (;;)
    {
      return;
      if (paramInt != this.currentRigthIcon)
      {
        this.currentRigthIcon = paramInt;
        Animation localAnimation = AnimationUtils.loadAnimation(this, 2130968583);
        localAnimation.setDuration(250L);
        localAnimation.setAnimationListener(new Animation.AnimationListener()
        {
          public void onAnimationEnd(Animation paramAnonymousAnimation)
          {
            BaseActivity.this.ivHeaderRightButton.setImageResource(BaseActivity.this.currentRigthIcon);
            paramAnonymousAnimation = AnimationUtils.loadAnimation(BaseActivity.this.getApplicationContext(), 2130968582);
            paramAnonymousAnimation.setDuration(250L);
            BaseActivity.this.ivHeaderRightButton.startAnimation(paramAnonymousAnimation);
          }
          
          public void onAnimationRepeat(Animation paramAnonymousAnimation) {}
          
          public void onAnimationStart(Animation paramAnonymousAnimation) {}
        });
        this.ivHeaderRightButton.startAnimation(localAnimation);
      }
    }
  }
  
  public void startActivity(Intent paramIntent)
  {
    if (RefreshApplication.getInstance().isPlayingAlarm()) {
      super.startActivities(new Intent[] { paramIntent, new Intent(this, DismissAlarmActivty.class) });
    }
    for (;;)
    {
      return;
      super.startActivity(paramIntent);
    }
  }
  
  protected void updateConnectionIcon()
  {
    BeDConnectionStatus localBeDConnectionStatus = RefreshApplication.getInstance().getConnectionStatus();
    if (localBeDConnectionStatus.getState() == null) {
      return;
    }
    Log.d("com.resmed.refresh.pair", " updateConnectionIcon ref : " + this);
    StringBuilder localStringBuilder = new StringBuilder(" updateConnectionIcon state : ").append(localBeDConnectionStatus.getState()).append(" ivHeaderSensor null?");
    if (this.ivHeaderSensor == null) {}
    for (boolean bool = true;; bool = false)
    {
      Log.d("com.resmed.refresh.pair", bool);
      updateBEDIcon(localBeDConnectionStatus.getBeDIconConnected());
      break;
    }
  }
  
  public void updateDataStoredFlag(int paramInt)
  {
    RefreshApplication.getInstance().getConnectionStatus().updateBeDDataFlag(paramInt);
    updateConnectionIcon();
  }
  
  public static enum TypeBar
  {
    DEFAULT,  HOME_BAR,  NO_BED,  PROFILE_QUESTIONAIRE;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\uibase\base\BaseActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */