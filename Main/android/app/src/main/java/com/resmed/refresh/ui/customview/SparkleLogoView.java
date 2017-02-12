package com.resmed.refresh.ui.customview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.resmed.refresh.utils.Log;
import java.util.Random;

public class SparkleLogoView
  extends RelativeLayout
{
  private static final int ANIMATION_BRIGTH = 500;
  private static final int ANIMATION_MOVE = 2000;
  private static final float[] X = { 27.78F, 27.78F, 27.78F, 29.91F, 28.33F, 36.67F, 32.78F, 40.65F, 44.35F, 47.22F, 49.26F, 50.46F, 50.83F, 50.83F, 50.83F, 50.83F, 50.0F, 47.87F, 45.46F, 42.87F, 40.19F, 37.5F, 35.28F, 32.96F, 30.37F, 28.61F, 28.06F, 27.96F, 27.96F, 27.96F, 27.96F, 28.7F, 30.46F, 32.78F, 35.74F, 38.43F, 41.2F, 44.07F, 46.48F, 48.61F, 50.09F, 50.65F, 50.65F, 50.65F, 50.65F, 62.13F, 62.13F, 62.13F, 62.13F, 62.13F, 62.13F, 62.13F, 62.13F, 62.13F, 58.8F, 55.83F, 52.87F, 64.72F, 67.69F, 70.65F, 73.61F };
  private static final float[] Y = { 70.09F, 67.5F, 64.53F, 75.74F, 72.87F, 78.79F, 77.69F, 78.89F, 78.24F, 76.67F, 74.54F, 71.94F, 68.89F, 65.65F, 62.31F, 58.89F, 55.56F, 53.06F, 51.2F, 49.81F, 48.52F, 47.31F, 46.02F, 44.81F, 43.06F, 40.83F, 37.69F, 34.35F, 31.11F, 28.43F, 25.74F, 23.24F, 21.3F, 19.81F, 19.07F, 18.7F, 18.7F, 19.17F, 20.28F, 21.57F, 23.33F, 26.11F, 28.98F, 31.94F, 34.91F, 31.85F, 35.28F, 38.7F, 42.13F, 45.56F, 48.98F, 52.41F, 55.83F, 58.86F, 45.37F, 45.37F, 45.37F, 45.37F, 45.37F, 45.37F, 45.37F };
  private static float[] endX = new float[X.length];
  private static float[] endY = new float[Y.length];
  private static float[] initX = new float[X.length];
  private static float[] initY = new float[Y.length];
  float height;
  private OnAnimationEndsListener listener;
  private Paint paintHalo;
  private Paint paintPoint;
  private float radiusHaloMax;
  private float radiusHaloMin;
  private boolean readyAnimation = false;
  private boolean readyView = false;
  private float scale = 1.0F;
  private final SparkleLogoView view = this;
  float width;
  
  public SparkleLogoView(Context paramContext)
  {
    super(paramContext);
    intiView();
  }
  
  public SparkleLogoView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    intiView();
  }
  
  public SparkleLogoView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    intiView();
  }
  
  private void intiView()
  {
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
    this.radiusHaloMin = getResources().getDimension(2131034255);
    this.radiusHaloMax = getResources().getDimension(2131034256);
    this.scale = (this.radiusHaloMax / this.radiusHaloMin);
    this.view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        if (Build.VERSION.SDK_INT < 16) {
          SparkleLogoView.this.view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        for (;;)
        {
          SparkleLogoView.this.height = (SparkleLogoView.this.view.getBottom() - SparkleLogoView.this.view.getTop());
          SparkleLogoView.this.width = (SparkleLogoView.this.view.getRight() - SparkleLogoView.this.view.getLeft());
          Log.d("com.resmed.refresh.ui", "SparkleLogoView height=" + SparkleLogoView.this.height);
          Log.d("com.resmed.refresh.ui", "SparkleLogoView width=" + SparkleLogoView.this.width);
          SparkleLogoView.this.readyView = true;
          if ((SparkleLogoView.this.readyAnimation) && (SparkleLogoView.this.readyView)) {
            SparkleLogoView.this.start();
          }
          return;
          SparkleLogoView.this.view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
      }
    });
  }
  
  private void start()
  {
    Random localRandom = new Random();
    for (int i = 0;; i++)
    {
      if (i >= X.length) {
        return;
      }
      initX[i] = (localRandom.nextFloat() * this.width);
      initY[i] = (localRandom.nextFloat() * this.height);
      if (initY[i] > this.radiusHaloMax)
      {
        localObject1 = initY;
        localObject1[i] -= this.radiusHaloMax;
      }
      endX[i] = (X[i] * this.width / 100.0F);
      endY[i] = (Y[i] * this.height / 100.0F);
      int j = (int)(2.0F * this.radiusHaloMax);
      final Object localObject1 = new SparklePointView(getContext());
      Object localObject2 = new RelativeLayout.LayoutParams(j, j);
      this.view.addView((View)localObject1, (ViewGroup.LayoutParams)localObject2);
      ((View)localObject1).setX(initX[i]);
      ((View)localObject1).setY(initY[i]);
      ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(localObject1, "x", new float[] { endX[i] });
      ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(localObject1, "y", new float[] { endY[i] });
      localObject2 = new AnimatorSet();
      ((AnimatorSet)localObject2).playTogether(new Animator[] { localObjectAnimator2, localObjectAnimator1 });
      ((AnimatorSet)localObject2).setDuration(2000L);
      ((AnimatorSet)localObject2).setInterpolator(new AccelerateDecelerateInterpolator());
      ((AnimatorSet)localObject2).start();
      ((AnimatorSet)localObject2).addListener(new Animator.AnimatorListener()
      {
        public void onAnimationCancel(Animator paramAnonymousAnimator) {}
        
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          localObject1.animate().scaleX(SparkleLogoView.this.scale).scaleY(SparkleLogoView.this.scale).setDuration(500L).setInterpolator(new LinearInterpolator()).setListener(new Animator.AnimatorListener()
          {
            public void onAnimationCancel(Animator paramAnonymous2Animator) {}
            
            public void onAnimationEnd(Animator paramAnonymous2Animator)
            {
              if (SparkleLogoView.this.listener != null) {
                SparkleLogoView.this.listener.onAnimationEnds();
              }
            }
            
            public void onAnimationRepeat(Animator paramAnonymous2Animator) {}
            
            public void onAnimationStart(Animator paramAnonymous2Animator) {}
          });
        }
        
        public void onAnimationRepeat(Animator paramAnonymousAnimator) {}
        
        public void onAnimationStart(Animator paramAnonymousAnimator) {}
      });
    }
  }
  
  public void setOnAnimationEndsListener(OnAnimationEndsListener paramOnAnimationEndsListener)
  {
    this.listener = paramOnAnimationEndsListener;
  }
  
  public void startAnimation()
  {
    this.readyAnimation = true;
    if ((this.readyAnimation) && (this.readyView)) {
      start();
    }
  }
  
  public static abstract interface OnAnimationEndsListener
  {
    public abstract void onAnimationEnds();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\SparkleLogoView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */