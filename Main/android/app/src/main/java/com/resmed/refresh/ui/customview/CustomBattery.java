package com.resmed.refresh.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

public class CustomBattery
  extends ImageView
{
  private static final int ANIMATION_TIME = 2500;
  private static final float ARC_BOTTOM_MARGIN = 0.023936F;
  private static final float ARC_TOP_MARGIN = 0.399F;
  private static final float ARC_TRANS_Y = 0.212766F;
  private static final float BOTTOM_MARGIN = 0.0425532F;
  private static final float LEFT_MARGIN = 0.04762F;
  private static final float RIGHT_MARGIN = 0.04762F;
  private static final float TOP_MARGIN = 0.11702F;
  private Animation animBattery = new Animation()
  {
    protected void applyTransformation(float paramAnonymousFloat, Transformation paramAnonymousTransformation)
    {
      CustomBattery.this.count = Math.round(100.0F * paramAnonymousFloat);
      CustomBattery.this.view.invalidate();
    }
    
    public boolean willChangeBounds()
    {
      return true;
    }
  };
  private float bottom;
  private float bottomArc;
  private int count;
  private Paint eraser;
  private Paint eraserTop;
  private float left;
  private float leftArc;
  private Paint paint;
  private int percentage;
  private float right;
  private float rightArc;
  private float top;
  private float topArc;
  private float transArcY;
  private final ImageView view = this;
  
  public CustomBattery(Context paramContext)
  {
    super(paramContext);
    intiView();
  }
  
  public CustomBattery(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    intiView();
  }
  
  public CustomBattery(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    intiView();
  }
  
  private void intiView()
  {
    setLayerType(1, null);
    this.paint = new Paint();
    this.paint.setAntiAlias(true);
    this.paint.setStrokeWidth(0.0F);
    this.paint.setStyle(Paint.Style.FILL);
    this.paint.setColor(getResources().getColor(2131296336));
    this.eraser = new Paint();
    this.eraser.setStyle(Paint.Style.STROKE);
    this.eraser.setAntiAlias(true);
    this.eraser.setStrokeWidth(getResources().getDimension(2131034240));
    this.eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    this.eraserTop = new Paint(this.eraser);
    this.eraserTop.setStyle(Paint.Style.FILL);
    this.animBattery.setDuration(2500L);
    this.view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        if (Build.VERSION.SDK_INT < 16) {
          CustomBattery.this.view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        for (;;)
        {
          float[] arrayOfFloat = new float[9];
          CustomBattery.this.view.getImageMatrix().getValues(arrayOfFloat);
          float f1 = CustomBattery.this.view.getBottom() - CustomBattery.this.view.getTop();
          float f2 = CustomBattery.this.view.getRight() - CustomBattery.this.view.getLeft();
          CustomBattery.this.left = (0.04762F * f2 + arrayOfFloat[2]);
          CustomBattery.this.right = (f2 - 0.04762F * f2 - arrayOfFloat[2]);
          CustomBattery.this.top = (0.11702F * f1);
          CustomBattery.this.bottom = (f1 - 0.0425532F * f1);
          CustomBattery.this.leftArc = (CustomBattery.this.left - f1 / 6.0F);
          CustomBattery.this.rightArc = (CustomBattery.this.right + f1 / 6.0F);
          CustomBattery.this.topArc = (0.399F * f1);
          CustomBattery.this.bottomArc = (0.976064F * f1);
          CustomBattery.this.transArcY = (0.212766F * f1);
          return;
          CustomBattery.this.view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
      }
    });
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    if (this.count > this.percentage) {
      this.count = this.percentage;
    }
    int i = Math.round(this.bottom - this.count / 100.0F * (this.bottom - this.top));
    paramCanvas.drawRect(this.left, i, this.right, this.bottom, this.paint);
    for (i = 0;; i++)
    {
      if (i >= 5)
      {
        i = Math.round(this.count / 100.0F * (this.bottom - this.top));
        paramCanvas.drawArc(new RectF(this.leftArc, this.topArc - i, this.rightArc, this.bottomArc - i), 0.0F, 180.0F, false, this.eraserTop);
        super.onDraw(paramCanvas);
        return;
      }
      paramCanvas.drawArc(new RectF(this.leftArc, this.topArc - this.transArcY * i, this.rightArc, this.bottomArc - this.transArcY * i), 0.0F, 180.0F, false, this.eraser);
    }
  }
  
  public void setPercentage(int paramInt, boolean paramBoolean)
  {
    this.percentage = paramInt;
    if (paramBoolean) {
      startAnimation(this.animBattery);
    }
    for (;;)
    {
      return;
      this.count = paramInt;
      invalidate();
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\CustomBattery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */