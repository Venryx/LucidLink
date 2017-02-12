package com.resmed.refresh.ui.utils;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.utils.Log;

public class CustomDialogBuilder
{
  private BaseActivity activity;
  protected Typeface akzidenzLight;
  private ImageView bDialogClose;
  private View.OnClickListener closeListener;
  private Context context;
  private RelativeLayout customDialogContent;
  private LinearLayout customDialogLayout;
  private String description;
  private boolean hasCloseButton;
  private boolean hasNegativeButton;
  private boolean hasPositiveButton;
  private View.OnClickListener negativeListener;
  private String negativeText;
  private View.OnClickListener positiveListener;
  private String positiveText;
  private String progressText;
  private String title;
  private boolean useProgressBar;
  
  public CustomDialogBuilder(BaseActivity paramBaseActivity)
  {
    this.context = paramBaseActivity.getBaseContext();
    this.activity = paramBaseActivity;
    this.hasPositiveButton = false;
    this.hasNegativeButton = false;
    this.hasCloseButton = false;
    this.useProgressBar = false;
    this.akzidenzLight = Typeface.createFromAsset(this.context.getAssets(), "AkzidenzGroteskBE-Light.otf");
  }
  
  public View createView()
  {
    View localView1 = LayoutInflater.from(this.context).inflate(2130903136, null);
    TextView localTextView3 = (TextView)localView1.findViewById(2131099951);
    TextView localTextView1 = (TextView)localView1.findViewById(2131099949);
    localTextView1.setMovementMethod(new ScrollingMovementMethod());
    TextView localTextView2 = (TextView)localView1.findViewById(2131099948);
    Button localButton1 = (Button)localView1.findViewById(2131099956);
    Button localButton2 = (Button)localView1.findViewById(2131099958);
    this.bDialogClose = ((ImageView)localView1.findViewById(2131099952));
    View localView2 = localView1.findViewById(2131099946);
    View localView3 = localView1.findViewById(2131099955);
    Object localObject = localView1.findViewById(2131099957);
    ProgressBar localProgressBar = (ProgressBar)localView1.findViewById(2131099954);
    this.customDialogContent = ((RelativeLayout)localView1.findViewById(2131099908));
    this.customDialogLayout = ((LinearLayout)localView1.findViewById(2131099909));
    if ((this.title != null) && (this.title.length() > 0))
    {
      localTextView3.setText(this.title);
      if (this.hasCloseButton)
      {
        Resources localResources = this.context.getResources();
        localTextView3.setPadding((int)localResources.getDimension(2131034184), 0, (int)localResources.getDimension(2131034184), 0);
      }
    }
    for (;;)
    {
      if ((this.description != null) && (this.description.length() > 0))
      {
        localTextView1.setText(this.description);
        label244:
        if ((this.progressText == null) || (this.progressText.length() <= 0)) {
          break label465;
        }
        localTextView2.setText(this.progressText);
        localView2.setVisibility(0);
        label276:
        if (!this.hasPositiveButton) {
          break label475;
        }
        localButton2.setText(this.positiveText);
        localButton2.setOnClickListener(new CustomDialogListener(this.positiveListener));
        localButton2.setTypeface(this.akzidenzLight);
        label318:
        if (!this.hasNegativeButton) {
          break label484;
        }
        localButton1.setText(this.negativeText);
        localButton1.setOnClickListener(new CustomDialogListener(this.negativeListener));
        localButton1.setTypeface(this.akzidenzLight);
        label357:
        if (!this.hasCloseButton) {
          break label494;
        }
        this.bDialogClose.setOnClickListener(new CustomDialogListener(this.closeListener));
        label383:
        if (!this.useProgressBar) {
          break label506;
        }
        localProgressBar.setProgress(0);
      }
      try
      {
        for (;;)
        {
          localObject = new android/widget/RelativeLayout$LayoutParams;
          ((RelativeLayout.LayoutParams)localObject).<init>(-1, -1);
          localView1.setLayoutParams((ViewGroup.LayoutParams)localObject);
          this.customDialogLayout.setOnClickListener(new CustomDialogListener(null));
          this.customDialogContent.setOnClickListener(new CustomDialogListener(null));
          return localView1;
          localTextView3.setVisibility(8);
          break;
          localTextView1.setVisibility(8);
          break label244;
          label465:
          localView2.setVisibility(8);
          break label276;
          label475:
          ((View)localObject).setVisibility(8);
          break label318;
          label484:
          localView3.setVisibility(8);
          break label357;
          label494:
          this.bDialogClose.setVisibility(8);
          break label383;
          label506:
          localProgressBar.setVisibility(8);
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
  }
  
  public CustomDialogBuilder description(int paramInt)
  {
    this.description = this.context.getResources().getString(paramInt);
    return this;
  }
  
  public CustomDialogBuilder description(String paramString)
  {
    this.description = paramString;
    return this;
  }
  
  public ImageView getCloseButton()
  {
    if (this.hasCloseButton) {}
    for (ImageView localImageView = this.bDialogClose;; localImageView = null) {
      return localImageView;
    }
  }
  
  public CustomDialogBuilder progress(String paramString)
  {
    this.progressText = paramString;
    return this;
  }
  
  public CustomDialogBuilder setCloseButton(View.OnClickListener paramOnClickListener)
  {
    this.closeListener = paramOnClickListener;
    this.hasCloseButton = true;
    return this;
  }
  
  public CustomDialogBuilder setNegativeButton(int paramInt, View.OnClickListener paramOnClickListener)
  {
    return setNegativeButton(this.context.getResources().getString(paramInt), paramOnClickListener);
  }
  
  public CustomDialogBuilder setNegativeButton(String paramString, View.OnClickListener paramOnClickListener)
  {
    this.negativeText = paramString;
    this.negativeListener = paramOnClickListener;
    this.hasNegativeButton = true;
    return this;
  }
  
  public CustomDialogBuilder setPositiveButton(int paramInt, View.OnClickListener paramOnClickListener)
  {
    return setPositiveButton(this.context.getResources().getString(paramInt), paramOnClickListener);
  }
  
  public CustomDialogBuilder setPositiveButton(String paramString, View.OnClickListener paramOnClickListener)
  {
    this.positiveText = paramString;
    this.positiveListener = paramOnClickListener;
    this.hasPositiveButton = true;
    return this;
  }
  
  public CustomDialogBuilder title(int paramInt)
  {
    this.title = this.context.getResources().getString(paramInt);
    return this;
  }
  
  public CustomDialogBuilder title(String paramString)
  {
    this.title = paramString;
    return this;
  }
  
  public CustomDialogBuilder useProgressBar()
  {
    this.useProgressBar = true;
    return this;
  }
  
  private class CustomDialogListener
    implements View.OnClickListener
  {
    private View.OnClickListener delegate;
    
    public CustomDialogListener(View.OnClickListener paramOnClickListener)
    {
      this.delegate = paramOnClickListener;
    }
    
    public void onClick(View paramView)
    {
      if (this.delegate != null) {
        this.delegate.onClick(paramView);
      }
      Log.d("com.resmed.refresh.dialog", "CustomDialogBuilder dismissing dialog");
      FragmentManager localFragmentManager = CustomDialogBuilder.this.activity.getFragmentManager();
      paramView = localFragmentManager.findFragmentByTag("dialog");
      if (paramView != null) {
        ((DialogFragment)paramView).dismiss();
      }
      localFragmentManager.executePendingTransactions();
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */