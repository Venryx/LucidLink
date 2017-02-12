package com.resmed.refresh.ui.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class GroteskLightEditText
  extends EditText
{
  private static Typeface mTypeface;
  
  public GroteskLightEditText(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public GroteskLightEditText(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public GroteskLightEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if (mTypeface == null) {
      mTypeface = Typeface.createFromAsset(paramContext.getAssets(), "AkzidenzGroteskBE-Light.otf");
    }
    setTypeface(mTypeface);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\font\GroteskLightEditText.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */