package com.resmed.refresh.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import com.resmed.refresh.utils.Log;
import java.util.ArrayList;
import java.util.List;

public class SparklePathView
  extends View
  implements Animation.AnimationListener
{
  private static final int ANIMATION_TIME = 3000;
  private static final int NUMBER_OF_POINTS = 18;
  private Animation animation = new Animation()
  {
    protected void applyTransformation(float paramAnonymousFloat, Transformation paramAnonymousTransformation)
    {
      int j = 0;
      int i = j;
      int k;
      if (paramAnonymousFloat <= 0.34D)
      {
        k = 18 - Math.round((1.0F - paramAnonymousFloat * 3.0F) * 18.0F);
        i = j;
        if (k != SparklePathView.this.count)
        {
          i = SparklePathView.this.count;
          if (i < k) {
            break label224;
          }
          SparklePathView.this.count = k;
          i = 1;
        }
      }
      if (paramAnonymousFloat > 0.2D)
      {
        i = 0;
        label89:
        if (i >= SparklePathView.this.items.size()) {
          i = 1;
        }
      }
      else if (paramAnonymousFloat <= 0.05D) {}
      for (i = 0;; i++)
      {
        if (i >= SparklePathView.this.items.size())
        {
          i = 1;
          j = i;
          if (paramAnonymousFloat > 0.66D)
          {
            k = Math.round((1.0F - (1.0F - paramAnonymousFloat) * 3.0F) * 18.0F);
            j = i;
            if (k != SparklePathView.this.count)
            {
              i = 0;
              if (i < 18) {
                break label685;
              }
              SparklePathView.this.count = k;
              j = 1;
            }
          }
          if (j != 0) {
            SparklePathView.this.view.invalidate();
          }
          return;
          label224:
          ((SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i)).visible = true;
          i++;
          break;
          paramAnonymousTransformation = (SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i);
          float f2 = SparklePathView.this.radiusPointMin;
          float f4 = ((SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i)).random;
          float f3 = SparklePathView.this.radiusPointMax;
          f1 = SparklePathView.this.radiusPointMin;
          paramAnonymousTransformation.radiousPoint = (((SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i)).random * ((f3 - f1) * (paramAnonymousFloat - 0.2F)) + f2 * (f4 + 1.0F));
          paramAnonymousTransformation = (SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i);
          f3 = SparklePathView.this.radiusHaloMin;
          f1 = ((SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i)).random;
          f2 = SparklePathView.this.radiusHaloMax;
          f4 = SparklePathView.this.radiusHaloMin;
          paramAnonymousTransformation.radiousHalo = (((SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i)).random * ((f2 - f4) * (paramAnonymousFloat - 0.2F)) + f3 * (f1 + 1.0F));
          i++;
          break label89;
        }
        paramAnonymousTransformation = (SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i);
        paramAnonymousTransformation.directionX += (SparklePathView.this.radiusPointMin / 2.0F - (float)Math.random() * SparklePathView.this.radiusPointMin) / 40000.0F;
        paramAnonymousTransformation = (SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i);
        paramAnonymousTransformation.directionY += (SparklePathView.this.radiusPointMin / 2.0F - (float)Math.random() * SparklePathView.this.radiusPointMin) / 80000.0F;
        paramAnonymousTransformation = (SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i);
        float f1 = paramAnonymousTransformation.x;
        paramAnonymousTransformation.x = ((int)(((SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i)).directionX + f1));
        paramAnonymousTransformation = (SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i);
        f1 = paramAnonymousTransformation.y;
        paramAnonymousTransformation.y = ((int)(((SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i)).directionY + f1));
      }
      label685:
      paramAnonymousTransformation = (SparklePathView.SparkleViewItem)SparklePathView.this.items.get(i);
      if (i >= k) {}
      for (boolean bool = true;; bool = false)
      {
        paramAnonymousTransformation.visible = bool;
        i++;
        break;
      }
    }
    
    public boolean willChangeBounds()
    {
      return true;
    }
  };
  private int count = 0;
  private List<SparkleViewItem> items;
  private OnAnimationEndsListener listener;
  private Paint paintHalo;
  private Paint paintPoint;
  private float radiusHaloMax;
  private float radiusHaloMin;
  private float radiusPointMax;
  private float radiusPointMin;
  private final SparklePathView view = this;
  
  public SparklePathView(Context paramContext)
  {
    super(paramContext);
    intiView();
  }
  
  public SparklePathView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    intiView();
  }
  
  public SparklePathView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    intiView();
  }
  
  private void intiView()
  {
    this.items = new ArrayList(18);
    this.count = 0;
    this.animation.setDuration(3000L);
    this.animation.setInterpolator(new LinearInterpolator());
    setLayerType(1, null);
    this.paintHalo = new Paint();
    this.paintHalo.setAntiAlias(true);
    this.paintHalo.setAlpha(128);
    this.paintHalo.setStrokeWidth(0.0F);
    this.paintHalo.setStyle(Paint.Style.FILL);
    this.paintHalo.setColor(getResources().getColor(2131296314));
    this.paintPoint = new Paint();
    this.paintPoint.setAntiAlias(true);
    this.paintPoint.setAlpha(128);
    this.paintPoint.setStrokeWidth(0.0F);
    this.paintPoint.setStyle(Paint.Style.FILL);
    this.paintPoint.setColor(getResources().getColor(2131296314));
    this.view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        float f4;
        float f3;
        float f5;
        float f1;
        if (Build.VERSION.SDK_INT < 16)
        {
          SparklePathView.this.view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
          f4 = SparklePathView.this.view.getBottom() - SparklePathView.this.view.getTop();
          f3 = SparklePathView.this.view.getRight() - SparklePathView.this.view.getLeft();
          SparklePathView.this.radiusPointMin = SparklePathView.this.getResources().getDimension(2131034253);
          SparklePathView.this.radiusPointMax = SparklePathView.this.getResources().getDimension(2131034254);
          SparklePathView.this.radiusHaloMin = SparklePathView.this.getResources().getDimension(2131034255);
          SparklePathView.this.radiusHaloMax = SparklePathView.this.getResources().getDimension(2131034256);
          f5 = SparklePathView.this.radiusHaloMax * 0.5F;
          f1 = SparklePathView.this.radiusHaloMax;
          Log.d("com.resmed.refresh.ui", "SparklePathView height=" + f4);
          Log.d("com.resmed.refresh.ui", "SparklePathView width=" + f3);
          Log.d("com.resmed.refresh.ui", "SparklePathView xRandomDesviation=" + f5);
          Log.d("com.resmed.refresh.ui", "SparklePathView yRandomDesviation=" + f1);
          Log.d("com.resmed.refresh.ui", "SparklePathView radiusPointMax=" + SparklePathView.this.radiusPointMax);
          Log.d("com.resmed.refresh.ui", "SparklePathView radiusPointMin=" + SparklePathView.this.radiusPointMin);
        }
        for (int i = 0;; i++)
        {
          if (i >= 18)
          {
            return;
            SparklePathView.this.view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            break;
          }
          int k = (int)Math.round(i * (f3 / 18.0F) + (Math.random() - 0.5D) * f5);
          int j = (int)Math.round(4.0F * f4 / 5.0F + f1 * Math.random());
          float f2 = (float)Math.random() / 1.5F;
          SparklePathView.this.items.add(new SparklePathView.SparkleViewItem(SparklePathView.this, k, j, SparklePathView.this.radiusPointMin * (1.0F + f2), SparklePathView.this.radiusHaloMin * (1.0F + f2), f2));
        }
      }
    });
  }
  
  public void onAnimationEnd(Animation paramAnimation)
  {
    if (this.listener != null) {
      this.listener.OnAnimationEnds();
    }
  }
  
  public void onAnimationRepeat(Animation paramAnimation) {}
  
  public void onAnimationStart(Animation paramAnimation) {}
  
  protected void onDraw(Canvas paramCanvas)
  {
    if (!isInEditMode()) {}
    for (int i = 0;; i++)
    {
      if (i >= 18)
      {
        super.onDraw(paramCanvas);
        return;
      }
      if (((SparkleViewItem)this.items.get(i)).visible)
      {
        Object localObject = new int[4];
        localObject[0] = getResources().getColor(2131296304);
        localObject[1] = getResources().getColor(2131296306);
        localObject[2] = getResources().getColor(2131296307);
        localObject[3] = getResources().getColor(2131296308);
        float[] arrayOfFloat = new float[4];
        arrayOfFloat[0] = 0.0F;
        arrayOfFloat[1] = 0.33F;
        arrayOfFloat[2] = 0.66F;
        arrayOfFloat[3] = 1.0F;
        RadialGradient localRadialGradient = new RadialGradient(((SparkleViewItem)this.items.get(i)).x, ((SparkleViewItem)this.items.get(i)).y, ((SparkleViewItem)this.items.get(i)).radiousHalo, (int[])localObject, arrayOfFloat, Shader.TileMode.MIRROR);
        this.paintHalo.setShader(localRadialGradient);
        localObject[0] = getResources().getColor(2131296300);
        localObject[1] = getResources().getColor(2131296301);
        localObject[2] = getResources().getColor(2131296308);
        arrayOfFloat[0] = 0.0F;
        arrayOfFloat[1] = 0.66F;
        arrayOfFloat[2] = 1.0F;
        localObject = new RadialGradient(((SparkleViewItem)this.items.get(i)).x, ((SparkleViewItem)this.items.get(i)).y, ((SparkleViewItem)this.items.get(i)).radiousPoint, (int[])localObject, arrayOfFloat, Shader.TileMode.MIRROR);
        this.paintPoint.setShader((Shader)localObject);
        paramCanvas.drawCircle(((SparkleViewItem)this.items.get(i)).x, ((SparkleViewItem)this.items.get(i)).y, ((SparkleViewItem)this.items.get(i)).radiousPoint, this.paintPoint);
        paramCanvas.drawCircle(((SparkleViewItem)this.items.get(i)).x, ((SparkleViewItem)this.items.get(i)).y, ((SparkleViewItem)this.items.get(i)).radiousHalo, this.paintHalo);
      }
    }
  }
  
  public void startAnimation(OnAnimationEndsListener paramOnAnimationEndsListener)
  {
    this.listener = paramOnAnimationEndsListener;
    this.animation.setAnimationListener(this);
    startAnimation(this.animation);
  }
  
  public static abstract interface OnAnimationEndsListener
  {
    public abstract void OnAnimationEnds();
  }
  
  private class SparkleViewItem
  {
    public float directionX;
    public float directionY;
    public float radiousHalo;
    public float radiousPoint;
    public float random;
    public boolean visible;
    public int x;
    public int y;
    
    public SparkleViewItem(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3)
    {
      this.x = paramInt1;
      this.y = paramInt2;
      this.radiousPoint = paramFloat1;
      this.radiousHalo = paramFloat2;
      this.random = paramFloat3;
      this.directionX = (SparklePathView.this.radiusPointMin * (1.0F - (float)Math.random() * 2.0F) / 40000.0F);
      this.directionY = (SparklePathView.this.radiusPointMin * (1.0F - (float)Math.random() * 2.0F) / 60000.0F);
      this.visible = false;
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\SparklePathView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */