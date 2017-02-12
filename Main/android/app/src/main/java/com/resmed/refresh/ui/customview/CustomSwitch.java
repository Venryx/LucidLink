package com.resmed.refresh.ui.customview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.CompoundButton;

public class CustomSwitch
  extends CompoundButton
{
  private static final int[] CHECKED_STATE_SET = { 16842912 };
  private static final int HORIZONTAL = 1;
  private static final int MONOSPACE = 3;
  private static final int SANS = 1;
  private static final int SERIF = 2;
  private static final int TOUCH_MODE_DOWN = 1;
  private static final int TOUCH_MODE_DRAGGING = 2;
  private static final int TOUCH_MODE_IDLE = 0;
  private static final int VERTICAL = 0;
  private Canvas backingLayer;
  private final Rect canvasClipBounds = new Rect();
  private boolean clickDisabled = false;
  private boolean fixed = false;
  private Bitmap leftBitmap;
  private Drawable mDrawableOff;
  private Drawable mDrawableOn;
  private Drawable mLeftBackground;
  private Drawable mMaskDrawable;
  private int mMinFlingVelocity;
  private Layout mOffLayout;
  private OnChangeAttemptListener mOnChangeAttemptListener;
  private Layout mOnLayout;
  private int mOrientation = 1;
  private boolean mPushStyle;
  private Drawable mRightBackground;
  private int mSwitchBottom;
  private int mSwitchHeight;
  private int mSwitchLeft;
  private int mSwitchMinHeight;
  private int mSwitchMinWidth;
  private int mSwitchPadding;
  private int mSwitchRight;
  private int mSwitchTop;
  private int mSwitchWidth;
  private ColorStateList mTextColors;
  private CharSequence mTextOff;
  private CharSequence mTextOn;
  private boolean mTextOnThumb;
  private TextPaint mTextPaint;
  private final Rect mThPad = new Rect();
  private Drawable mThumbDrawable;
  private int mThumbExtraMovement;
  private int mThumbHeight;
  private float mThumbPosition = 0.0F;
  private int mThumbTextPadding;
  private int mThumbWidth;
  private int mTouchMode;
  private int mTouchSlop;
  private float mTouchX;
  private float mTouchY;
  private Drawable mTrackDrawable;
  private final Rect mTrackPaddingRect = new Rect();
  private int mTrackTextPadding;
  private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
  private Bitmap maskBitmap;
  private Bitmap pushBitmap;
  private Bitmap rightBitmap;
  private Bitmap tempBitmap;
  private Paint xferPaint;
  
  public CustomSwitch(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public CustomSwitch(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 2130772192);
  }
  
  public CustomSwitch(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if (!isInEditMode())
    {
      this.mTextPaint = new TextPaint(1);
      Object localObject = getResources();
      this.mTextPaint.density = ((Resources)localObject).getDisplayMetrics().density;
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MySwitch, paramInt, 0);
      this.mLeftBackground = localTypedArray.getDrawable(16);
      this.mRightBackground = localTypedArray.getDrawable(17);
      this.mOrientation = localTypedArray.getInteger(15, 1);
      this.mThumbDrawable = localTypedArray.getDrawable(0);
      this.mTrackDrawable = localTypedArray.getDrawable(1);
      this.mTextOn = localTypedArray.getText(2);
      this.mTextOff = localTypedArray.getText(3);
      this.mDrawableOn = localTypedArray.getDrawable(4);
      this.mDrawableOff = localTypedArray.getDrawable(5);
      this.mPushStyle = localTypedArray.getBoolean(6, false);
      this.mTextOnThumb = localTypedArray.getBoolean(7, false);
      this.mThumbExtraMovement = localTypedArray.getDimensionPixelSize(8, 0);
      this.mThumbTextPadding = localTypedArray.getDimensionPixelSize(9, 0);
      this.mTrackTextPadding = localTypedArray.getDimensionPixelSize(10, 0);
      this.mSwitchMinWidth = localTypedArray.getDimensionPixelSize(12, 0);
      this.mSwitchMinHeight = localTypedArray.getDimensionPixelSize(13, 0);
      this.mSwitchPadding = localTypedArray.getDimensionPixelSize(14, 0);
      this.mTrackDrawable.getPadding(this.mTrackPaddingRect);
      this.mThumbDrawable.getPadding(this.mThPad);
      this.mMaskDrawable = localTypedArray.getDrawable(18);
      paramAttributeSet = null;
      if (this.mLeftBackground == null)
      {
        localObject = paramAttributeSet;
        if (this.mRightBackground == null) {}
      }
      else
      {
        localObject = paramAttributeSet;
        if (this.mMaskDrawable == null) {
          localObject = new IllegalArgumentException(localTypedArray.getPositionDescription() + " if left/right background is given, then a mask has to be there");
        }
      }
      if (this.mLeftBackground != null)
      {
        paramInt = 1;
        if (this.mRightBackground == null) {
          break label595;
        }
      }
      label595:
      for (int i = 1;; i = 0)
      {
        paramAttributeSet = (AttributeSet)localObject;
        if ((paramInt ^ i) != 0)
        {
          paramAttributeSet = (AttributeSet)localObject;
          if (this.mMaskDrawable == null) {
            paramAttributeSet = new IllegalArgumentException(localTypedArray.getPositionDescription() + " left and right background both should be there. only one is not allowed ");
          }
        }
        localObject = paramAttributeSet;
        if (this.mTextOnThumb)
        {
          localObject = paramAttributeSet;
          if (this.mPushStyle) {
            localObject = new IllegalArgumentException(localTypedArray.getPositionDescription() + " Text On Thumb and Push Stype are mutually exclusive. Only one can be present ");
          }
        }
        this.xferPaint = new Paint(1);
        this.xferPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paramInt = localTypedArray.getResourceId(11, 0);
        if (paramInt != 0) {
          setSwitchTextAppearance(paramContext, paramInt);
        }
        localTypedArray.recycle();
        if (localObject == null) {
          break label601;
        }
        throw ((Throwable)localObject);
        paramInt = 0;
        break;
      }
      label601:
      paramAttributeSet = ViewConfiguration.get(paramContext);
      this.mTouchSlop = paramAttributeSet.getScaledTouchSlop();
      this.mMinFlingVelocity = paramAttributeSet.getScaledMinimumFlingVelocity();
      refreshDrawableState();
      setChecked(isChecked());
      setClickable(true);
      setSwitchTypeface(Typeface.createFromAsset(paramContext.getAssets(), "AkzidenzGroteskBE-Light.otf"), 1);
    }
  }
  
  private void animateThumbToCheckedState(boolean paramBoolean)
  {
    setChecked(paramBoolean);
  }
  
  private void cancelSuperTouch(MotionEvent paramMotionEvent)
  {
    paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
    paramMotionEvent.setAction(3);
    super.onTouchEvent(paramMotionEvent);
    paramMotionEvent.recycle();
  }
  
  private boolean getTargetCheckedState()
  {
    if (this.mThumbPosition >= getThumbScrollRange() / 2) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  private int getThumbScrollRange()
  {
    int i;
    if (this.mTrackDrawable == null) {
      i = 0;
    }
    for (;;)
    {
      return i;
      int j = 0;
      if (this.mOrientation == 0) {
        j = this.mSwitchHeight - this.mThumbHeight - this.mTrackPaddingRect.top - this.mTrackPaddingRect.bottom + this.mThumbExtraMovement * 2;
      }
      if (this.mOrientation == 1) {
        j = this.mSwitchWidth - this.mThumbWidth - this.mTrackPaddingRect.left - this.mTrackPaddingRect.right + this.mThumbExtraMovement * 2;
      }
      i = j;
      if (this.mPushStyle) {
        i = j + this.mTrackTextPadding * 2;
      }
    }
  }
  
  private boolean hitThumb(float paramFloat1, float paramFloat2)
  {
    boolean bool = true;
    int n;
    int i;
    int i1;
    int k;
    int i2;
    int j;
    int m;
    if (this.mOrientation == 1)
    {
      n = this.mSwitchTop;
      i = this.mTouchSlop;
      i1 = this.mSwitchLeft + (int)(this.mThumbPosition + 0.5F) - this.mTouchSlop;
      k = this.mThumbWidth;
      i2 = this.mTouchSlop;
      j = this.mSwitchBottom;
      m = this.mTouchSlop;
      if ((paramFloat1 <= i1) || (paramFloat1 >= k + i1 + i2) || (paramFloat2 <= n - i) || (paramFloat2 >= j + m)) {}
    }
    for (;;)
    {
      return bool;
      bool = false;
      continue;
      if (this.mSwitchHeight > 150)
      {
        k = this.mSwitchLeft;
        i = this.mTouchSlop;
        m = this.mSwitchTop + (int)(this.mThumbPosition + 0.5F) - this.mTouchSlop;
        i2 = this.mThumbHeight;
        j = this.mTouchSlop;
        n = this.mSwitchRight;
        i1 = this.mTouchSlop;
        if ((paramFloat1 <= k - i) || (paramFloat1 >= n + i1) || (paramFloat2 <= m) || (paramFloat2 >= i2 + m + j)) {
          bool = false;
        }
      }
      else if ((paramFloat1 <= this.mSwitchLeft) || (paramFloat1 >= this.mSwitchRight) || (paramFloat2 <= this.mSwitchTop) || (paramFloat2 >= this.mSwitchBottom))
      {
        bool = false;
      }
    }
  }
  
  private Layout makeLayout(CharSequence paramCharSequence)
  {
    return new StaticLayout(paramCharSequence, this.mTextPaint, (int)FloatMath.ceil(Layout.getDesiredWidth(paramCharSequence, this.mTextPaint)), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
  }
  
  private void setSwitchTypefaceByIndex(int paramInt1, int paramInt2)
  {
    Typeface localTypeface = null;
    switch (paramInt1)
    {
    }
    for (;;)
    {
      setSwitchTypeface(localTypeface, paramInt2);
      return;
      localTypeface = Typeface.SANS_SERIF;
      continue;
      localTypeface = Typeface.SERIF;
      continue;
      localTypeface = Typeface.MONOSPACE;
    }
  }
  
  private void stopDrag(MotionEvent paramMotionEvent)
  {
    boolean bool2 = false;
    this.mTouchMode = 0;
    int i;
    label38:
    float f;
    boolean bool1;
    if ((paramMotionEvent.getAction() == 1) && (isEnabled()))
    {
      i = 1;
      if ((i == 0) || (this.fixed)) {
        break label123;
      }
      i = 1;
      cancelSuperTouch(paramMotionEvent);
      if (i == 0) {
        break label206;
      }
      this.mVelocityTracker.computeCurrentVelocity(1000);
      if (this.mOrientation != 1) {
        break label143;
      }
      f = this.mVelocityTracker.getXVelocity();
      if (Math.abs(f) <= this.mMinFlingVelocity) {
        break label134;
      }
      if (f <= 0.0F) {
        break label128;
      }
      bool1 = true;
      label95:
      if (!this.mTextOnThumb) {
        break label197;
      }
      if (!bool1) {
        break label191;
      }
      bool1 = bool2;
      label111:
      animateThumbToCheckedState(bool1);
    }
    for (;;)
    {
      return;
      i = 0;
      break;
      label123:
      i = 0;
      break label38;
      label128:
      bool1 = false;
      break label95;
      label134:
      bool1 = getTargetCheckedState();
      break label95;
      label143:
      f = this.mVelocityTracker.getYVelocity();
      if (Math.abs(f) > this.mMinFlingVelocity)
      {
        if (f > 0.0F) {}
        for (bool1 = true;; bool1 = false) {
          break;
        }
      }
      bool1 = getTargetCheckedState();
      break label95;
      label191:
      bool1 = true;
      break label111;
      label197:
      animateThumbToCheckedState(bool1);
      continue;
      label206:
      animateThumbToCheckedState(isChecked());
      if ((this.fixed) && (this.mOnChangeAttemptListener != null)) {
        this.mOnChangeAttemptListener.onChangeAttempted(isChecked());
      }
    }
  }
  
  public void disableClick()
  {
    this.clickDisabled = true;
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    if (this.mThumbDrawable != null) {
      this.mThumbDrawable.setState(arrayOfInt);
    }
    if (this.mTrackDrawable != null) {
      this.mTrackDrawable.setState(arrayOfInt);
    }
    invalidate();
  }
  
  public void enableClick()
  {
    this.clickDisabled = false;
  }
  
  public void fixate(boolean paramBoolean)
  {
    this.fixed = paramBoolean;
  }
  
  public void fixate(boolean paramBoolean1, boolean paramBoolean2)
  {
    fixate(paramBoolean1);
    if (paramBoolean2) {
      setChecked(true);
    }
  }
  
  public int getCompoundPaddingRight()
  {
    int j = super.getCompoundPaddingRight() + this.mSwitchWidth;
    int i = j;
    if (!TextUtils.isEmpty(getText())) {
      i = j + this.mSwitchPadding;
    }
    return i;
  }
  
  public int getCompoundPaddingTop()
  {
    int j = super.getCompoundPaddingTop() + this.mSwitchHeight;
    int i = j;
    if (!TextUtils.isEmpty(getText())) {
      i = j + this.mSwitchPadding;
    }
    return i;
  }
  
  public CharSequence getCurrentText()
  {
    if (isChecked()) {}
    for (CharSequence localCharSequence = this.mTextOn;; localCharSequence = this.mTextOff) {
      return localCharSequence;
    }
  }
  
  public CharSequence getText(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (CharSequence localCharSequence = this.mTextOn;; localCharSequence = this.mTextOff) {
      return localCharSequence;
    }
  }
  
  public CharSequence getTextOff()
  {
    return this.mTextOff;
  }
  
  public CharSequence getTextOn()
  {
    return this.mTextOn;
  }
  
  public boolean isFixed()
  {
    return this.fixed;
  }
  
  protected int[] onCreateDrawableState(int paramInt)
  {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    if (isChecked()) {
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET);
    }
    return arrayOfInt;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    int i4;
    int m;
    int i3;
    int i1;
    int i2;
    int k;
    int j;
    int i;
    label180:
    int i7;
    int i6;
    int i5;
    if (!isInEditMode())
    {
      i4 = this.mSwitchLeft + this.mTrackPaddingRect.left;
      m = this.mSwitchTop + this.mTrackPaddingRect.top;
      i3 = this.mSwitchRight - this.mTrackPaddingRect.right;
      i1 = this.mSwitchBottom - this.mTrackPaddingRect.bottom;
      i2 = getThumbScrollRange();
      int n = (int)(this.mThumbPosition + 0.5F);
      k = this.mTextPaint.getAlpha();
      this.mTextPaint.drawableState = getDrawableState();
      if (this.mOrientation == 1)
      {
        j = i4 + this.mThumbWidth;
        if (!this.mTextOnThumb) {
          break label664;
        }
        i = (i4 + j) / 2 - this.mOnLayout.getWidth() / 2 + this.mTrackTextPadding - this.mThumbTextPadding;
        if (!this.mTextOnThumb) {
          break label675;
        }
        j = (i4 + i2 + (j + i2)) / 2 - this.mOffLayout.getWidth() / 2;
        i7 = (m + i1) / 2;
        i6 = i4 + n - this.mThumbExtraMovement;
        i5 = i4 + n + this.mThumbWidth - this.mThumbExtraMovement;
        if (!this.mPushStyle) {
          break label694;
        }
        i = Math.max(this.mOffLayout.getWidth(), this.mOnLayout.getWidth());
        this.backingLayer.save();
        this.backingLayer.translate(-i2 + n, 0.0F);
        this.backingLayer.drawBitmap(this.pushBitmap, 0.0F, 0.0F, null);
        this.backingLayer.restore();
        this.backingLayer.drawBitmap(this.maskBitmap, 0.0F, 0.0F, this.xferPaint);
        paramCanvas.drawBitmap(this.tempBitmap, 0.0F, 0.0F, null);
        this.mTrackDrawable.draw(paramCanvas);
        this.backingLayer.drawColor(16777216, PorterDuff.Mode.DST_IN);
        this.backingLayer.save();
        this.backingLayer.translate(-i2 + n, 0.0F);
        this.backingLayer.translate(this.mTrackPaddingRect.left, 0.0F);
        this.backingLayer.save();
        this.backingLayer.translate(this.mOnLayout.getWidth(), i7 - this.mOnLayout.getHeight() / 2);
        this.mOnLayout.draw(this.backingLayer);
        if (this.mDrawableOff != null) {
          this.mDrawableOff.draw(this.backingLayer);
        }
        this.backingLayer.restore();
        this.backingLayer.translate(this.mOnLayout.getWidth() + i + this.mOffLayout.getWidth() + this.mThumbWidth + i, i7 - this.mOffLayout.getHeight() / 2);
        this.mOffLayout.draw(this.backingLayer);
        if (this.mDrawableOn != null) {
          this.mDrawableOn.draw(this.backingLayer);
        }
        this.backingLayer.restore();
        this.backingLayer.drawBitmap(this.maskBitmap, 0.0F, 0.0F, this.xferPaint);
        paramCanvas.drawBitmap(this.tempBitmap, 0.0F, 0.0F, null);
        this.mThumbDrawable.setBounds(i6, this.mSwitchTop, i5, this.mSwitchBottom);
        this.mThumbDrawable.draw(paramCanvas);
        if (this.mTextOnThumb)
        {
          this.mTextPaint.setAlpha(k);
          if (!getTargetCheckedState()) {
            break label1392;
          }
        }
      }
    }
    label664:
    label675:
    label694:
    label1392:
    for (Object localObject = this.mOffLayout;; localObject = this.mOnLayout)
    {
      paramCanvas.save();
      paramCanvas.translate((i6 + i5) / 2 - ((Layout)localObject).getWidth() / 2, (m + i1) / 2 - ((Layout)localObject).getHeight() / 2);
      ((Layout)localObject).draw(paramCanvas);
      paramCanvas.restore();
      return;
      i = i4 + this.mTrackTextPadding;
      break;
      j = i3 - this.mOffLayout.getWidth() - this.mTrackTextPadding;
      break label180;
      if (this.rightBitmap != null)
      {
        paramCanvas.save();
        if (paramCanvas.getClipBounds(this.canvasClipBounds))
        {
          localObject = this.canvasClipBounds;
          ((Rect)localObject).left = ((int)(((Rect)localObject).left + (this.mThumbPosition + this.mThumbWidth / 2)));
          paramCanvas.clipRect(this.canvasClipBounds);
        }
        paramCanvas.drawBitmap(this.rightBitmap, 0.0F, 0.0F, null);
        paramCanvas.restore();
      }
      if (this.leftBitmap != null)
      {
        paramCanvas.save();
        if (paramCanvas.getClipBounds(this.canvasClipBounds))
        {
          localObject = this.canvasClipBounds;
          ((Rect)localObject).right = ((int)(((Rect)localObject).right - (i2 - this.mThumbPosition + this.mThumbWidth / 2)));
          paramCanvas.clipRect(this.canvasClipBounds);
        }
        paramCanvas.drawBitmap(this.leftBitmap, 0.0F, 0.0F, null);
        paramCanvas.restore();
      }
      this.mTrackDrawable.draw(paramCanvas);
      paramCanvas.save();
      paramCanvas.clipRect(i4, this.mSwitchTop, i3, this.mSwitchBottom);
      if (this.mTextColors != null) {
        this.mTextPaint.setColor(this.mTextColors.getColorForState(getDrawableState(), this.mTextColors.getDefaultColor()));
      }
      this.mTextPaint.setAlpha(k / 4);
      if (getTargetCheckedState())
      {
        paramCanvas.save();
        paramCanvas.translate(j, i7 - this.mOffLayout.getHeight() / 2);
        if (paramCanvas.getClipBounds(this.canvasClipBounds))
        {
          localObject = this.canvasClipBounds;
          ((Rect)localObject).left = ((int)(((Rect)localObject).left + (this.mThumbPosition + this.mThumbWidth / 2)));
          paramCanvas.clipRect(this.canvasClipBounds);
        }
        this.mOffLayout.draw(paramCanvas);
        if (this.mDrawableOn != null) {
          this.mDrawableOn.draw(paramCanvas);
        }
        paramCanvas.restore();
        if (!this.mTextOnThumb) {
          this.mTextPaint.setAlpha(k);
        }
        paramCanvas.save();
        paramCanvas.translate(i, i7 - this.mOnLayout.getHeight() / 2);
        if (paramCanvas.getClipBounds(this.canvasClipBounds))
        {
          localObject = this.canvasClipBounds;
          ((Rect)localObject).right = ((int)(((Rect)localObject).right - (i2 - this.mThumbPosition + this.mThumbWidth / 2)));
          paramCanvas.clipRect(this.canvasClipBounds);
        }
        this.mOnLayout.draw(paramCanvas);
        if (this.mDrawableOff != null) {
          this.mDrawableOff.draw(paramCanvas);
        }
        paramCanvas.restore();
      }
      for (;;)
      {
        paramCanvas.restore();
        break;
        paramCanvas.save();
        paramCanvas.translate(i, i7 - this.mOnLayout.getHeight() / 2);
        if (paramCanvas.getClipBounds(this.canvasClipBounds))
        {
          localObject = this.canvasClipBounds;
          ((Rect)localObject).right = ((int)(((Rect)localObject).right - (i2 - this.mThumbPosition + this.mThumbWidth / 2)));
          paramCanvas.clipRect(this.canvasClipBounds);
        }
        this.mOnLayout.draw(paramCanvas);
        if (this.mDrawableOff != null) {
          this.mDrawableOff.draw(paramCanvas);
        }
        paramCanvas.restore();
        if (!this.mTextOnThumb) {
          this.mTextPaint.setAlpha(k);
        }
        paramCanvas.save();
        paramCanvas.translate(j, i7 - this.mOffLayout.getHeight() / 2);
        if (paramCanvas.getClipBounds(this.canvasClipBounds))
        {
          localObject = this.canvasClipBounds;
          ((Rect)localObject).left = ((int)(((Rect)localObject).left + (this.mThumbPosition + this.mThumbWidth / 2)));
          paramCanvas.clipRect(this.canvasClipBounds);
        }
        this.mOffLayout.draw(paramCanvas);
        if (this.mDrawableOn != null) {
          this.mDrawableOn.draw(paramCanvas);
        }
        paramCanvas.restore();
      }
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (!isInEditMode()) {
      switch (getGravity() & 0x70)
      {
      default: 
        getPaddingTop();
        paramInt1 = this.mSwitchHeight;
        this.mSwitchBottom = (this.mSwitchHeight - getPaddingBottom());
        this.mSwitchTop = (this.mSwitchBottom - this.mSwitchHeight);
        this.mSwitchRight = (this.mSwitchWidth - getPaddingRight());
        this.mSwitchLeft = (this.mSwitchRight - this.mSwitchWidth);
        if (!this.mTextOnThumb) {
          break label595;
        }
        if (!isChecked()) {
          break;
        }
      }
    }
    for (paramInt1 = 0;; paramInt1 = getThumbScrollRange())
    {
      this.mThumbPosition = paramInt1;
      this.mTrackDrawable.setBounds(this.mSwitchLeft, this.mSwitchTop, this.mSwitchRight, this.mSwitchBottom);
      if (this.mDrawableOn != null) {
        this.mDrawableOn.setBounds(0, 0, this.mDrawableOn.getIntrinsicWidth(), this.mDrawableOn.getIntrinsicHeight());
      }
      if (this.mDrawableOff != null) {
        this.mDrawableOff.setBounds(0, 0, this.mDrawableOff.getIntrinsicWidth(), this.mDrawableOff.getIntrinsicHeight());
      }
      if (this.mLeftBackground != null) {
        this.mLeftBackground.setBounds(this.mSwitchLeft, this.mSwitchTop, this.mSwitchRight, this.mSwitchBottom);
      }
      if (this.mRightBackground != null) {
        this.mRightBackground.setBounds(this.mSwitchLeft, this.mSwitchTop, this.mSwitchRight, this.mSwitchBottom);
      }
      if (this.mMaskDrawable != null)
      {
        this.tempBitmap = Bitmap.createBitmap(this.mSwitchRight - this.mSwitchLeft, this.mSwitchBottom - this.mSwitchTop, Bitmap.Config.ARGB_8888);
        this.backingLayer = new Canvas(this.tempBitmap);
        this.mMaskDrawable.setBounds(this.mSwitchLeft, this.mSwitchTop, this.mSwitchRight, this.mSwitchBottom);
        this.mMaskDrawable.draw(this.backingLayer);
        this.maskBitmap = Bitmap.createBitmap(this.mSwitchRight - this.mSwitchLeft, this.mSwitchBottom - this.mSwitchTop, Bitmap.Config.ARGB_8888);
        paramInt3 = this.tempBitmap.getWidth();
        paramInt4 = this.tempBitmap.getHeight();
        paramInt1 = 0;
        if (paramInt1 < paramInt3) {
          break label621;
        }
        this.maskBitmap = this.tempBitmap.extractAlpha();
        if (this.mLeftBackground != null)
        {
          this.mLeftBackground.draw(this.backingLayer);
          this.backingLayer.drawBitmap(this.maskBitmap, 0.0F, 0.0F, this.xferPaint);
          this.leftBitmap = this.tempBitmap.copy(this.tempBitmap.getConfig(), true);
        }
        if (this.mRightBackground != null)
        {
          this.mRightBackground.draw(this.backingLayer);
          this.backingLayer.drawBitmap(this.maskBitmap, 0.0F, 0.0F, this.xferPaint);
          this.rightBitmap = this.tempBitmap.copy(this.tempBitmap.getConfig(), true);
        }
      }
      return;
      paramInt1 = (getPaddingTop() + getHeight() - getPaddingBottom()) / 2;
      paramInt1 = this.mSwitchHeight / 2;
      paramInt1 = this.mSwitchHeight;
      break;
      getHeight();
      getPaddingBottom();
      paramInt1 = this.mSwitchHeight;
      break;
    }
    label595:
    if (isChecked()) {}
    for (paramInt1 = getThumbScrollRange();; paramInt1 = 0)
    {
      this.mThumbPosition = paramInt1;
      break;
    }
    label621:
    for (paramInt2 = 0;; paramInt2++)
    {
      if (paramInt2 >= paramInt4)
      {
        paramInt1++;
        break;
      }
      this.maskBitmap.setPixel(paramInt1, paramInt2, this.tempBitmap.getPixel(paramInt1, paramInt2) & 0xFF000000);
    }
  }
  
  public void onMeasure(int paramInt1, int paramInt2)
  {
    if (isInEditMode())
    {
      super.onMeasure(paramInt1, paramInt2);
      return;
    }
    int j = View.MeasureSpec.getMode(paramInt1);
    int m = View.MeasureSpec.getMode(paramInt2);
    int n = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getSize(paramInt2);
    if (this.mOffLayout == null) {
      this.mOffLayout = makeLayout(this.mTextOff);
    }
    if (this.mOnLayout == null) {
      this.mOnLayout = makeLayout(this.mTextOn);
    }
    int i2 = Math.max(this.mOffLayout.getWidth(), this.mOnLayout.getWidth());
    int i1 = Math.max(this.mOffLayout.getHeight(), this.mOnLayout.getHeight());
    this.mThumbWidth = (this.mThumbTextPadding * 2 + i2 + this.mThPad.left + this.mThPad.right);
    this.mThumbWidth = Math.max(this.mThumbWidth, this.mThumbDrawable.getIntrinsicWidth());
    if (!this.mTextOnThumb)
    {
      this.mThumbWidth = this.mThumbDrawable.getIntrinsicWidth();
      if (this.mThumbWidth < 15) {
        this.mThumbWidth = 15;
      }
    }
    this.mThumbHeight = (this.mThumbTextPadding * 2 + i1 + this.mThPad.bottom + this.mThPad.top);
    this.mThumbHeight = Math.max(this.mThumbHeight, this.mThumbDrawable.getIntrinsicHeight());
    if (!this.mTextOnThumb)
    {
      this.mThumbHeight = this.mThumbDrawable.getIntrinsicHeight();
      if (this.mThumbHeight < 15) {
        this.mThumbHeight = 15;
      }
    }
    this.mThumbHeight = this.mThumbWidth;
    int i;
    if (this.mOrientation == 1)
    {
      i = Math.max(this.mSwitchMinWidth, i2 * 2 + this.mThumbTextPadding * 2 + this.mTrackTextPadding * 2 + this.mTrackPaddingRect.left + this.mTrackPaddingRect.right);
      if (!this.mTextOnThumb) {
        i = Math.max(this.mThumbWidth + i2 + this.mTrackTextPadding * 2 + (this.mTrackPaddingRect.right + this.mTrackPaddingRect.left) / 2, this.mSwitchMinWidth);
      }
      if (this.mPushStyle) {
        i = Math.max(this.mSwitchMinWidth, this.mThumbWidth + i2 + this.mTrackTextPadding + (this.mTrackPaddingRect.left + this.mTrackPaddingRect.right) / 2);
      }
      label424:
      i2 = Math.max(this.mSwitchMinWidth, i);
      i = this.mTrackDrawable.getIntrinsicHeight();
      int i3 = this.mThumbDrawable.getIntrinsicHeight();
      i = Math.max(Math.max(i, Math.max(this.mSwitchMinHeight, i1)), i3);
      if (this.mOrientation == 0)
      {
        i = this.mOffLayout.getHeight() + this.mOnLayout.getHeight() + this.mThumbTextPadding * 2 + this.mThPad.top + this.mThPad.bottom + this.mTrackPaddingRect.bottom + this.mTrackPaddingRect.top + this.mTrackTextPadding * 2;
        if (!this.mTextOnThumb) {
          i = Math.max(this.mThumbHeight + i1 + (this.mTrackPaddingRect.bottom + this.mTrackPaddingRect.top) / 2 + this.mTrackTextPadding * 2, this.mSwitchMinHeight);
        }
        if (this.mPushStyle) {
          i = Math.max(this.mSwitchMinHeight, this.mThumbHeight + i1 + this.mTrackTextPadding + (this.mTrackPaddingRect.top + this.mTrackPaddingRect.bottom) / 2);
        }
      }
      switch (j)
      {
      default: 
        label660:
        switch (m)
        {
        }
        break;
      }
    }
    for (;;)
    {
      this.mSwitchWidth = i2;
      this.mSwitchHeight = i;
      super.onMeasure(paramInt1, paramInt2);
      paramInt1 = getMeasuredHeight();
      paramInt2 = getMeasuredWidth();
      if (paramInt1 < i) {
        setMeasuredDimension(getMeasuredWidth(), i);
      }
      if (paramInt2 >= i2) {
        break;
      }
      setMeasuredDimension(i2, getMeasuredHeight());
      break;
      i = Math.max(this.mThumbTextPadding * 2 + i2 + this.mThPad.left + this.mThPad.right, this.mThumbWidth);
      if ((!this.mPushStyle) && (this.mTextOnThumb)) {
        break label424;
      }
      i = Math.max(this.mTrackTextPadding * 2 + i2 + this.mTrackPaddingRect.left + this.mTrackPaddingRect.right, this.mThumbWidth);
      break label424;
      Math.min(n, i2);
      break label660;
      break label660;
      Math.min(k, i);
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool2 = true;
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (paramMotionEvent.getActionMasked())
    {
    }
    for (;;)
    {
      boolean bool1 = super.onTouchEvent(paramMotionEvent);
      for (;;)
      {
        return bool1;
        float f1 = paramMotionEvent.getX();
        float f2 = paramMotionEvent.getY();
        if ((!isEnabled()) || (!hitThumb(f1, f2))) {
          break;
        }
        this.mTouchMode = 1;
        this.mTouchX = f1;
        this.mTouchY = f2;
        break;
        switch (this.mTouchMode)
        {
        case 0: 
        default: 
          break;
        case 1: 
          f2 = paramMotionEvent.getX();
          f1 = paramMotionEvent.getY();
          if ((Math.abs(f2 - this.mTouchX) <= this.mTouchSlop / 2) && (Math.abs(f1 - this.mTouchY) <= this.mTouchSlop / 2)) {
            break;
          }
          this.mTouchMode = 2;
          if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
          }
          this.mTouchX = f2;
          this.mTouchY = f1;
          bool1 = bool2;
          break;
        case 2: 
          f2 = paramMotionEvent.getX();
          float f4 = this.mTouchX;
          f1 = paramMotionEvent.getY();
          float f3 = this.mTouchY;
          if (this.mOrientation == 1)
          {
            f1 = Math.max(0.0F, Math.min(this.mThumbPosition + (f2 - f4), getThumbScrollRange()));
            bool1 = bool2;
            if (f1 != this.mThumbPosition)
            {
              this.mThumbPosition = f1;
              this.mTouchX = f2;
              invalidate();
              bool1 = bool2;
            }
          }
          else
          {
            if (this.mOrientation != 0) {
              break;
            }
            f2 = Math.max(0.0F, Math.min(this.mThumbPosition + (f1 - f3), getThumbScrollRange()));
            bool1 = bool2;
            if (f2 != this.mThumbPosition)
            {
              this.mThumbPosition = f2;
              this.mTouchY = f1;
              invalidate();
              bool1 = bool2;
              continue;
              if (this.mTouchMode != 2) {
                break label389;
              }
              stopDrag(paramMotionEvent);
              bool1 = bool2;
            }
          }
          break;
        }
      }
      label389:
      this.mTouchMode = 0;
      this.mVelocityTracker.clear();
    }
  }
  
  public boolean performClick()
  {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (!this.clickDisabled)
    {
      if (this.fixed) {
        break label25;
      }
      bool1 = super.performClick();
    }
    for (;;)
    {
      return bool1;
      label25:
      bool1 = bool2;
      if (this.mOnChangeAttemptListener != null)
      {
        this.mOnChangeAttemptListener.onChangeAttempted(isChecked());
        bool1 = bool2;
      }
    }
  }
  
  public void setChecked(boolean paramBoolean)
  {
    int i = 0;
    boolean bool = paramBoolean;
    if (!this.mTextOnThumb)
    {
      if (paramBoolean) {
        bool = false;
      }
    }
    else
    {
      super.setChecked(paramBoolean);
      if (!bool) {
        break label42;
      }
    }
    for (;;)
    {
      this.mThumbPosition = i;
      invalidate();
      return;
      bool = true;
      break;
      label42:
      i = getThumbScrollRange();
    }
  }
  
  public void setOnChangeAttemptListener(OnChangeAttemptListener paramOnChangeAttemptListener)
  {
    this.mOnChangeAttemptListener = paramOnChangeAttemptListener;
  }
  
  public void setSwitchTextAppearance(Context paramContext, int paramInt)
  {
    paramContext = paramContext.obtainStyledAttributes(paramInt, R.styleable.mySwitchTextAppearanceAttrib);
    ColorStateList localColorStateList = paramContext.getColorStateList(0);
    if (localColorStateList != null) {}
    for (this.mTextColors = localColorStateList;; this.mTextColors = getTextColors())
    {
      paramInt = paramContext.getDimensionPixelSize(1, 0);
      if ((paramInt != 0) && (paramInt != this.mTextPaint.getTextSize()))
      {
        this.mTextPaint.setTextSize(paramInt);
        requestLayout();
      }
      setSwitchTypefaceByIndex(paramContext.getInt(3, -1), paramContext.getInt(2, -1));
      paramContext.recycle();
      return;
    }
  }
  
  public void setSwitchTypeface(Typeface paramTypeface)
  {
    if (this.mTextPaint.getTypeface() != paramTypeface)
    {
      this.mTextPaint.setTypeface(paramTypeface);
      requestLayout();
      invalidate();
    }
  }
  
  public void setSwitchTypeface(Typeface paramTypeface, int paramInt)
  {
    boolean bool = false;
    int i;
    label31:
    float f;
    if (paramInt > 0) {
      if (paramTypeface == null)
      {
        paramTypeface = Typeface.defaultFromStyle(paramInt);
        setSwitchTypeface(paramTypeface);
        if (paramTypeface == null) {
          break label88;
        }
        i = paramTypeface.getStyle();
        paramInt &= (i ^ 0xFFFFFFFF);
        paramTypeface = this.mTextPaint;
        if ((paramInt & 0x1) != 0) {
          bool = true;
        }
        paramTypeface.setFakeBoldText(bool);
        paramTypeface = this.mTextPaint;
        if ((paramInt & 0x2) == 0) {
          break label94;
        }
        f = -0.25F;
        label73:
        paramTypeface.setTextSkewX(f);
      }
    }
    for (;;)
    {
      return;
      paramTypeface = Typeface.create(paramTypeface, paramInt);
      break;
      label88:
      i = 0;
      break label31;
      label94:
      f = 0.0F;
      break label73;
      this.mTextPaint.setFakeBoldText(false);
      this.mTextPaint.setTextSkewX(0.0F);
      setSwitchTypeface(paramTypeface);
    }
  }
  
  public void setTextOff(CharSequence paramCharSequence)
  {
    this.mTextOff = paramCharSequence;
    this.mOnLayout = null;
    requestLayout();
  }
  
  public void setTextOn(CharSequence paramCharSequence)
  {
    this.mTextOn = paramCharSequence;
    this.mOffLayout = null;
    requestLayout();
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    if ((!super.verifyDrawable(paramDrawable)) && (paramDrawable != this.mThumbDrawable) && (paramDrawable != this.mTrackDrawable)) {}
    for (boolean bool = false;; bool = true) {
      return bool;
    }
  }
  
  public static abstract interface OnChangeAttemptListener
  {
    public abstract void onChangeAttempted(boolean paramBoolean);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\CustomSwitch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */