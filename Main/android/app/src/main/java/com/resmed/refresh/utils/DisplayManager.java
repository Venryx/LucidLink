package com.resmed.refresh.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.WindowManager;

public class DisplayManager
{
  public static Integer getScreenHeight(Context paramContext)
  {
    paramContext = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    Point localPoint = new Point();
    if (Build.VERSION.SDK_INT >= 13) {
      paramContext.getSize(localPoint);
    }
    for (paramContext = Integer.valueOf(localPoint.y);; paramContext = Integer.valueOf(paramContext.getHeight())) {
      return paramContext;
    }
  }
  
  public static Integer getScreenWidth(Context paramContext)
  {
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    paramContext = new Point();
    if (Build.VERSION.SDK_INT >= 13) {
      localDisplay.getSize(paramContext);
    }
    for (paramContext = Integer.valueOf(paramContext.x);; paramContext = Integer.valueOf(localDisplay.getWidth())) {
      return paramContext;
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */