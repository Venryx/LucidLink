package com.resmed.refresh.ui.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class GroteskMdCnd
  extends TextView
{
  private static Typeface mTypeface;
  
  public GroteskMdCnd(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public GroteskMdCnd(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public GroteskMdCnd(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if ((!isInEditMode()) && (mTypeface == null)) {
      mTypeface = Typeface.createFromAsset(paramContext.getAssets(), "AkzidenzGroteskBE-MdCn.otf");
    }
    setTypeface(mTypeface);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\font\GroteskMdCnd.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */