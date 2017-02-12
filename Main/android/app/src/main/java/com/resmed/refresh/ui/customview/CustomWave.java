package com.resmed.refresh.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.Log;

public class CustomWave
  extends View
{
  private static final int NUMER_OF_PATHS = 22;
  private Paint eraser;
  private float height;
  private Paint paint;
  private RectF rect;
  private float sinHeigth;
  private float sinWidth;
  private float sinXStart;
  private float sinYStart;
  private Path sinePath;
  private float step;
  private int strokeWidth;
  private float translation;
  private CustomWave wave = this;
  private float width;
  
  public CustomWave(Context paramContext)
  {
    super(paramContext);
    if (!isInEditMode()) {
      intiView();
    }
  }
  
  public CustomWave(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    if (!isInEditMode()) {
      intiView();
    }
  }
  
  public CustomWave(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if (!isInEditMode()) {
      intiView();
    }
  }
  
  private void intiView()
  {
    setLayerType(1, null);
    this.paint = new Paint();
    this.paint.setAntiAlias(true);
    this.paint.setStrokeWidth((int)RefreshApplication.getInstance().getResources().getDimension(2131034237));
    this.paint.setStyle(Paint.Style.STROKE);
    this.paint.setColor(-1);
    this.strokeWidth = ((int)RefreshApplication.getInstance().getResources().getDimension(2131034239));
    this.eraser = new Paint();
    this.eraser.setStyle(Paint.Style.STROKE);
    this.eraser.setAntiAlias(true);
    this.eraser.setStrokeWidth(this.strokeWidth);
    this.eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    this.wave.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        if (Build.VERSION.SDK_INT < 16) {
          CustomWave.this.wave.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        for (;;)
        {
          CustomWave.this.width = CustomWave.this.wave.getWidth();
          CustomWave.this.height = CustomWave.this.wave.getHeight();
          CustomWave.this.sinXStart = 0.0F;
          CustomWave.this.sinYStart = (CustomWave.this.height * 3.0F / 4.0F);
          CustomWave.this.sinHeigth = (CustomWave.this.height / 4.0F);
          CustomWave.this.sinWidth = (CustomWave.this.width / 22.0F);
          CustomWave.this.step = (CustomWave.this.sinWidth / 4.0F);
          CustomWave.this.rect = new RectF(-CustomWave.this.strokeWidth / 2, -CustomWave.this.strokeWidth / 2, (int)(CustomWave.this.width + CustomWave.this.strokeWidth / 2), (int)(CustomWave.this.height + CustomWave.this.strokeWidth / 2));
          Log.d("com.resmed.refresh.ui", "CustomWave width=" + CustomWave.this.width);
          Log.d("com.resmed.refresh.ui", "CustomWave heigth=" + CustomWave.this.height);
          return;
          CustomWave.this.wave.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
      }
    });
  }
  
  private void updatePath()
  {
    this.sinePath = new Path();
    this.sinePath.moveTo(this.sinXStart, this.sinYStart);
    int i = 0;
    if (i >= 13) {
      return;
    }
    if (i % 2 == 0) {}
    for (int j = -1;; j = 1)
    {
      this.sinePath.quadTo((i * 2 + 1) * this.sinWidth, this.sinYStart + j * this.sinHeigth, (i * 2 + 2) * this.sinWidth, this.sinYStart);
      i++;
      break;
    }
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    paramCanvas.translate(this.translation, 0.0F);
    updatePath();
    paramCanvas.drawPath(this.sinePath, this.paint);
    paramCanvas.translate(-this.translation, 0.0F);
    paramCanvas.drawArc(this.rect, 0.0F, 360.0F, false, this.eraser);
    super.onDraw(paramCanvas);
    new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
    {
      public void run()
      {
        CustomWave localCustomWave = CustomWave.this;
        localCustomWave.translation += CustomWave.this.step;
        if (CustomWave.this.translation >= 0.0F) {
          CustomWave.this.translation = (-4.0F * CustomWave.this.sinWidth);
        }
        CustomWave.this.wave.invalidate();
      }
    }, 75L);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\CustomWave.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */