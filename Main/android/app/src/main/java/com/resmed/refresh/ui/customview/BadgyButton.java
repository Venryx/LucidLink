package com.resmed.refresh.ui.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class BadgyButton
  extends TextView
{
  private Integer badgeColor;
  private Integer badgeCount;
  
  public BadgyButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    if (!isInEditMode())
    {
      setTypeface(Typeface.createFromAsset(paramContext.getAssets(), "AkzidenzGroteskBE-Light.otf"));
      paramContext = paramContext.getTheme().obtainStyledAttributes(paramAttributeSet, R.styleable.BadgyButton, 0, 0);
    }
    try
    {
      setBadgeCount(Integer.valueOf(paramContext.getInteger(0, 0)));
      setBadgeColor(Integer.valueOf(paramContext.getInteger(1, -65536)));
      setBackgroundResource(2130837641);
      return;
    }
    finally
    {
      paramContext.recycle();
    }
  }
  
  public Integer getBadgeColor()
  {
    return this.badgeColor;
  }
  
  public Integer getBadgeCount()
  {
    return this.badgeCount;
  }
  
  public void setBadgeColor(Integer paramInteger)
  {
    this.badgeColor = paramInteger;
    setBackgroundColor(paramInteger.intValue());
  }
  
  public void setBadgeCount(Integer paramInteger)
  {
    this.badgeCount = paramInteger;
    setText(Integer.toString(this.badgeCount.intValue()));
    if (this.badgeCount.intValue() == 0) {
      setVisibility(4);
    }
    for (;;)
    {
      return;
      setVisibility(0);
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\BadgyButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */