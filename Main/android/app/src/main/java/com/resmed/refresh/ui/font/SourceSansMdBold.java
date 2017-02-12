package com.resmed.refresh.ui.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class SourceSansMdBold
  extends TextView
{
  private static Typeface mTypeface;
  
  public SourceSansMdBold(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SourceSansMdBold(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SourceSansMdBold(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if ((!isInEditMode()) && (mTypeface == null)) {
      mTypeface = Typeface.createFromAsset(paramContext.getAssets(), "SourceSansPro-Semibold.ttf");
    }
    setTypeface(mTypeface);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\font\SourceSansMdBold.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */