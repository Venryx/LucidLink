package com.resmed.refresh.ui.adapters;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.resmed.refresh.model.RST_AdviceItem;
import com.resmed.refresh.model.RST_Button;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.graphs.GraphViewBuilder;
import com.resmed.refresh.ui.activity.SleepHistoryCircleGraphActivity;
import com.resmed.refresh.ui.activity.SleepHistoryDayActivity;
import com.resmed.refresh.ui.activity.SleepHistoryGraphActivity;
import com.resmed.refresh.ui.customview.CustomBattery;
import com.resmed.refresh.ui.fragments.RefreshDialogFragment;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.utils.GraphViewListManager;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import java.util.Date;
import java.util.List;

public class SleepHistoryDayPageAdapter
  extends PagerAdapter
{
  private static final int ANIMATION_TIME = 2000;
  private boolean animate;
  private Context context;
  private RefreshDialogFragment dialog;
  private int feedback;
  private boolean firstLaunch;
  private List<SleepHistoryItem> items;
  private ViewPager viewPager;
  
  public SleepHistoryDayPageAdapter(Context paramContext, ViewPager paramViewPager, List<SleepHistoryItem> paramList, boolean paramBoolean)
  {
    this.context = paramContext;
    this.viewPager = paramViewPager;
    this.items = paramList;
    this.animate = paramBoolean;
    this.firstLaunch = true;
  }
  
  private void loadData(ViewGroup paramViewGroup, int paramInt, boolean paramBoolean)
  {
    RST_SleepSessionInfo localRST_SleepSessionInfo = ((SleepHistoryItem)this.items.get(paramInt)).getSessionInfo();
    Object localObject2;
    final Object localObject1;
    Object localObject3;
    if (paramViewGroup != null)
    {
      localObject2 = (ProgressBar)paramViewGroup.findViewById(2131100619);
      localObject1 = (ScrollView)paramViewGroup.findViewById(2131100620);
      ((ProgressBar)localObject2).setVisibility(4);
      ((ScrollView)localObject1).setVisibility(0);
      localObject2 = (TextView)paramViewGroup.findViewById(2131100621);
      localObject3 = (LinearLayout)paramViewGroup.findViewById(2131100622);
      if (localRST_SleepSessionInfo != null) {
        break label96;
      }
      ((TextView)localObject2).setVisibility(0);
      ((LinearLayout)localObject3).setVisibility(4);
    }
    label96:
    label804:
    for (;;)
    {
      return;
      localObject1 = RefreshModelController.getInstance().localAdvicesForSession(localRST_SleepSessionInfo.getId());
      printRecord(localRST_SleepSessionInfo, paramInt);
      ((TextView)localObject2).setVisibility(4);
      ((LinearLayout)localObject3).setVisibility(0);
      localObject2 = (TextView)paramViewGroup.findViewById(2131100608);
      TextView localTextView1 = (TextView)paramViewGroup.findViewById(2131100627);
      TextView localTextView2 = (TextView)paramViewGroup.findViewById(2131100626);
      CustomBattery localCustomBattery1 = (CustomBattery)paramViewGroup.findViewById(2131100006);
      CustomBattery localCustomBattery2 = (CustomBattery)paramViewGroup.findViewById(2131100008);
      TextView localTextView7 = (TextView)paramViewGroup.findViewById(2131100629);
      TextView localTextView10 = (TextView)paramViewGroup.findViewById(2131100630);
      LinearLayout localLinearLayout2 = (LinearLayout)paramViewGroup.findViewById(2131100623);
      TextView localTextView3 = (TextView)paramViewGroup.findViewById(2131100631);
      TextView localTextView9 = (TextView)paramViewGroup.findViewById(2131100632);
      TextView localTextView4 = (TextView)paramViewGroup.findViewById(2131100633);
      TextView localTextView8 = (TextView)paramViewGroup.findViewById(2131100634);
      TextView localTextView11 = (TextView)paramViewGroup.findViewById(2131100635);
      TextView localTextView6 = (TextView)paramViewGroup.findViewById(2131100636);
      Object localObject4 = (TextView)paramViewGroup.findViewById(2131100637);
      TextView localTextView12 = (TextView)paramViewGroup.findViewById(2131100638);
      TextView localTextView5 = (TextView)paramViewGroup.findViewById(2131100628);
      LinearLayout localLinearLayout1 = (LinearLayout)paramViewGroup.findViewById(2131100624);
      localObject3 = (ListView)paramViewGroup.findViewById(2131100641);
      LinearLayout localLinearLayout3 = (LinearLayout)paramViewGroup.findViewById(2131100640);
      final long l = localRST_SleepSessionInfo.getId();
      GraphViewBuilder localGraphViewBuilder = GraphViewListManager.getInstance().getView(this.context, localRST_SleepSessionInfo);
      localLinearLayout2.removeAllViews();
      localLinearLayout2.addView(localGraphViewBuilder.getChartView());
      localLinearLayout2.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          paramAnonymousView = new Intent(RefreshApplication.getInstance(), SleepHistoryGraphActivity.class);
          paramAnonymousView.putExtra("com.resmed.refresh.ui.uibase.app.sleep_history_id", l);
          paramAnonymousView.setFlags(268435456);
          RefreshApplication.getInstance().startActivity(paramAnonymousView);
        }
      });
      localLinearLayout1.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          paramAnonymousView = new Intent(RefreshApplication.getInstance(), SleepHistoryCircleGraphActivity.class);
          paramAnonymousView.putExtra("com.resmed.refresh.ui.uibase.app.sleep_history_id", l);
          paramAnonymousView.setFlags(268435456);
          RefreshApplication.getInstance().startActivity(paramAnonymousView);
        }
      });
      ((TextView)localObject2).setText(localRST_SleepSessionInfo.getMindScore() + "%");
      localTextView1.setText(localRST_SleepSessionInfo.getBodyScore() + "%");
      if (localRST_SleepSessionInfo.getSleepScore() >= 100) {
        localTextView2.setTextSize(localTextView2.getTextSize() / 3.0F);
      }
      localTextView2.setText(String.valueOf(localRST_SleepSessionInfo.getSleepScore()));
      localCustomBattery1.setPercentage(localRST_SleepSessionInfo.getMindScore(), paramBoolean);
      localCustomBattery2.setPercentage(localRST_SleepSessionInfo.getBodyScore(), paramBoolean);
      localTextView7.setText(RefreshTools.getMinsStringForTime(localRST_SleepSessionInfo.getTotalSleepTime()));
      localTextView10.setText(RefreshTools.getHourStringForTime(localRST_SleepSessionInfo.getTotalSleepTime()));
      paramInt = localRST_SleepSessionInfo.getTotalWakeTime();
      localTextView3.setText(RefreshTools.getMinsStringForTime(paramInt));
      localTextView9.setText(RefreshTools.getHourStringForTime(paramInt));
      localTextView4.setText(RefreshTools.getMinsStringForTime(localRST_SleepSessionInfo.getTotalRem()));
      localTextView8.setText(RefreshTools.getHourStringForTime(localRST_SleepSessionInfo.getTotalRem()));
      localTextView11.setText(RefreshTools.getMinsStringForTime(localRST_SleepSessionInfo.getTotalLightSleep()));
      localTextView6.setText(RefreshTools.getHourStringForTime(localRST_SleepSessionInfo.getTotalLightSleep()));
      ((TextView)localObject4).setText(RefreshTools.getMinsStringForTime(localRST_SleepSessionInfo.getTotalDeepSleep()));
      localTextView12.setText(RefreshTools.getHourStringForTime(localRST_SleepSessionInfo.getTotalDeepSleep()));
      localTextView5.setText(String.valueOf(localRST_SleepSessionInfo.getNumberInterruptions()));
      notifyEndScreenShot(paramViewGroup.findViewById(2131100639));
      if ((localObject1 != null) && (((List)localObject1).size() > 0))
      {
        localLinearLayout3.setVisibility(0);
        localObject4 = (LayoutInflater)this.context.getSystemService("layout_inflater");
        ((ListView)localObject3).setAdapter(new SleepHistoryDayAdvicesListAdapter(this.context, (List)localObject1, (LayoutInflater)localObject4));
        RefreshTools.setListViewHeightBasedOnChildren((ListView)localObject3);
        ((ListView)localObject3).setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
          public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
          {
            SleepHistoryDayPageAdapter.this.showAdviceDialog((RST_AdviceItem)localObject1.get(paramAnonymousInt));
          }
        });
      }
      for (;;)
      {
        if (!paramBoolean) {
          break label804;
        }
        paramViewGroup.startAnimation(new ScoreAnimation(localTextView2, localTextView1, (TextView)localObject2, localRST_SleepSessionInfo.getSleepScore(), localRST_SleepSessionInfo.getBodyScore(), localRST_SleepSessionInfo.getMindScore()));
        break;
        localLinearLayout3.setVisibility(8);
      }
    }
  }
  
  private void notifyEndScreenShot(final View paramView)
  {
    paramView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        if (Build.VERSION.SDK_INT < 16) {
          paramView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        try
        {
          for (;;)
          {
            int[] arrayOfInt = new int[2];
            paramView.getLocationOnScreen(arrayOfInt);
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>("getLocationOnScreen:");
            Log.e("facebook", arrayOfInt[0] + "," + arrayOfInt[1]);
            SleepHistoryDayActivity.setEndPositionForScreenShot(arrayOfInt[1]);
            return;
            paramView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          }
        }
        catch (Exception localException)
        {
          for (;;)
          {
            localException.printStackTrace();
          }
        }
      }
    });
  }
  
  private void printRecord(RST_SleepSessionInfo paramRST_SleepSessionInfo, int paramInt)
  {
    Log.d("com.resmed.refresh.ui", "**** Sleep Record info *****");
    Log.d("com.resmed.refresh.ui", "position " + paramInt);
    Log.d("com.resmed.refresh.ui", "Date " + paramRST_SleepSessionInfo.getStartTime());
    Log.d("com.resmed.refresh.ui", "Sleep Score " + paramRST_SleepSessionInfo.getSleepScore());
    Log.d("com.resmed.refresh.ui", "Mind Score " + paramRST_SleepSessionInfo.getMindScore());
    Log.d("com.resmed.refresh.ui", "Body Score " + paramRST_SleepSessionInfo.getBodyScore());
    Log.d("com.resmed.refresh.ui", "*************************** ");
  }
  
  private void sendFeedback(RST_AdviceItem paramRST_AdviceItem)
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    paramRST_AdviceItem.update();
    localRefreshModelController.save();
    localRefreshModelController.updateFeedback(paramRST_AdviceItem, new RST_CallbackItem()
    {
      public void onResult(RST_Response<Object> paramAnonymousRST_Response)
      {
        if (paramAnonymousRST_Response.isStatus()) {
          Toast.makeText(SleepHistoryDayPageAdapter.this.context, "Feedback updated", 0).show();
        }
      }
    });
  }
  
  private void showAdviceDialog(RST_AdviceItem paramRST_AdviceItem)
  {
    for (;;)
    {
      try
      {
        this.dialog = RefreshDialogFragment.newInstance();
        localFragmentManager = ((FragmentActivity)this.context).getFragmentManager();
        localObject1 = localFragmentManager.findFragmentByTag("dialog");
        if (localObject1 != null) {
          ((DialogFragment)localObject1).dismiss();
        }
        localObject1 = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903129, null);
        this.dialog.setView((View)localObject1);
        ((TextView)((View)localObject1).findViewById(2131099910)).setText(paramRST_AdviceItem.getHeader());
        localObject3 = (TextView)((View)localObject1).findViewById(2131099912);
        ((TextView)localObject3).setText(paramRST_AdviceItem.getContent());
        localObject2 = new android/text/method/ScrollingMovementMethod;
        ((ScrollingMovementMethod)localObject2).<init>();
        ((TextView)localObject3).setMovementMethod((MovementMethod)localObject2);
        localObject3 = (TextView)((View)localObject1).findViewById(2131099911);
        localObject2 = (TextView)((View)localObject1).findViewById(2131099913);
        if (paramRST_AdviceItem.getSubtitle().length() <= 0) {
          continue;
        }
        ((TextView)localObject3).setVisibility(0);
        ((TextView)localObject3).setText(paramRST_AdviceItem.getSubtitle());
        if (paramRST_AdviceItem.getArticleUrl().length() <= 0) {
          continue;
        }
        ((TextView)localObject2).setVisibility(0);
        ((TextView)localObject2).setText(paramRST_AdviceItem.getArticleUrl());
        ((TextView)localObject2).setPaintFlags(((TextView)localObject2).getPaintFlags() | 0x8);
        localObject3 = paramRST_AdviceItem.getArticleUrl();
        localObject4 = new com/resmed/refresh/ui/adapters/SleepHistoryDayPageAdapter$5;
        ((5)localObject4).<init>(this, (String)localObject3);
        ((TextView)localObject2).setOnClickListener((View.OnClickListener)localObject4);
      }
      catch (Exception paramRST_AdviceItem)
      {
        FragmentManager localFragmentManager;
        Object localObject1;
        Object localObject3;
        Object localObject2;
        Object localObject4;
        paramRST_AdviceItem.printStackTrace();
        continue;
        ((TextView)localObject2).setVisibility(8);
        continue;
        ((View)localObject4).setSelected(true);
        continue;
        ((View)localObject3).setSelected(true);
        continue;
        ((View)localObject2).setSelected(true);
        continue;
      }
      localObject4 = ((View)localObject1).findViewById(2131099915);
      localObject3 = ((View)localObject1).findViewById(2131099918);
      localObject2 = ((View)localObject1).findViewById(2131099921);
      if (paramRST_AdviceItem.getButtons().size() >= 3)
      {
        ((TextView)((View)localObject1).findViewById(2131099917)).setText(((RST_Button)paramRST_AdviceItem.getButtons().get(0)).getTitle());
        ((TextView)((View)localObject1).findViewById(2131099920)).setText(((RST_Button)paramRST_AdviceItem.getButtons().get(1)).getTitle());
        ((TextView)((View)localObject1).findViewById(2131099923)).setText(((RST_Button)paramRST_AdviceItem.getButtons().get(2)).getTitle());
      }
      ((View)localObject4).setSelected(false);
      ((View)localObject3).setSelected(false);
      ((View)localObject2).setSelected(false);
      switch (paramRST_AdviceItem.getFeedback())
      {
      default: 
        Object localObject5 = new com/resmed/refresh/ui/adapters/SleepHistoryDayPageAdapter$6;
        ((6)localObject5).<init>(this, (View)localObject4, (View)localObject3, (View)localObject2);
        ((View)localObject4).setOnClickListener((View.OnClickListener)localObject5);
        localObject5 = new com/resmed/refresh/ui/adapters/SleepHistoryDayPageAdapter$7;
        ((7)localObject5).<init>(this, (View)localObject4, (View)localObject3, (View)localObject2);
        ((View)localObject3).setOnClickListener((View.OnClickListener)localObject5);
        localObject5 = new com/resmed/refresh/ui/adapters/SleepHistoryDayPageAdapter$8;
        ((8)localObject5).<init>(this, (View)localObject4, (View)localObject3, (View)localObject2);
        ((View)localObject2).setOnClickListener((View.OnClickListener)localObject5);
        localObject1 = ((View)localObject1).findViewById(2131099925);
        localObject2 = new com/resmed/refresh/ui/adapters/SleepHistoryDayPageAdapter$9;
        ((9)localObject2).<init>(this, paramRST_AdviceItem);
        ((View)localObject1).setOnClickListener((View.OnClickListener)localObject2);
        this.dialog.setCancelable(true);
        this.dialog.show(localFragmentManager, "dialog");
        return;
        ((TextView)localObject3).setVisibility(8);
      }
    }
  }
  
  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    this.viewPager.removeView((View)paramObject);
  }
  
  public int getCount()
  {
    return this.items.size();
  }
  
  public Date getIndicator(int paramInt)
  {
    return ((SleepHistoryItem)this.items.get(paramInt)).getDateIndicator();
  }
  
  public Date getNextIndicator()
  {
    int i = this.viewPager.getCurrentItem();
    if (getCount() == i + 1) {}
    for (Date localDate = ((SleepHistoryItem)this.items.get(i)).getDateIndicator();; localDate = ((SleepHistoryItem)this.items.get(i + 1)).getDateIndicator()) {
      return localDate;
    }
  }
  
  public Date getPreviousIndicator()
  {
    int i = this.viewPager.getCurrentItem();
    if (i == 0) {}
    for (Date localDate = ((SleepHistoryItem)this.items.get(i)).getDateIndicator();; localDate = ((SleepHistoryItem)this.items.get(i - 1)).getDateIndicator()) {
      return localDate;
    }
  }
  
  public final Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
  {
    ViewGroup localViewGroup = (ViewGroup)((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903193, null);
    localViewGroup.setTag(((SleepHistoryItem)this.items.get(paramInt)).getSessionInfo());
    ((ViewPager)paramViewGroup).addView(localViewGroup);
    if ((this.animate) && (this.firstLaunch)) {}
    for (boolean bool = true;; bool = false)
    {
      loadData(localViewGroup, paramInt, bool);
      this.animate = false;
      this.firstLaunch = false;
      return localViewGroup;
    }
  }
  
  public final boolean isViewFromObject(View paramView, Object paramObject)
  {
    if (paramView == (View)paramObject) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public void loadData(int paramInt)
  {
    loadData((ViewGroup)this.viewPager.findViewWithTag(((SleepHistoryItem)this.items.get(paramInt)).getSessionInfo()), paramInt, true);
  }
  
  private class ScoreAnimation
    extends Animation
  {
    private int mBodyValue;
    private int mMindValue;
    private int mScoreValue;
    private TextView tvBody;
    private TextView tvMind;
    private TextView tvScore;
    
    public ScoreAnimation(TextView paramTextView1, TextView paramTextView2, TextView paramTextView3, int paramInt1, int paramInt2, int paramInt3)
    {
      this.mScoreValue = paramInt1;
      this.mBodyValue = paramInt2;
      this.mMindValue = paramInt3;
      if ((this.mBodyValue == 100) || (this.mMindValue == 100))
      {
        paramTextView2.setTextSize(0, SleepHistoryDayPageAdapter.this.context.getResources().getDimension(2131034202));
        paramTextView3.setTextSize(0, SleepHistoryDayPageAdapter.this.context.getResources().getDimension(2131034202));
      }
      this.tvScore = paramTextView1;
      this.tvBody = paramTextView2;
      this.tvMind = paramTextView3;
      setInterpolator(new LinearInterpolator());
      setDuration(2000L);
    }
    
    protected void applyTransformation(float paramFloat, Transformation paramTransformation)
    {
      super.applyTransformation(paramFloat, paramTransformation);
      float f = paramFloat;
      if (paramFloat >= 1.0F) {
        f = 1.0F;
      }
      this.tvScore.setText(String.valueOf(Math.round(this.mScoreValue * f)));
      this.tvBody.setText(Math.round(this.mBodyValue * f) + "%");
      this.tvMind.setText(Math.round(this.mMindValue * f) + "%");
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\adapters\SleepHistoryDayPageAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */