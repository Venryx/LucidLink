package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.UserProfileDataManager;
import com.resmed.refresh.utils.DisplayManager;
import com.resmed.refresh.utils.Log;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ProfilePickerFragment
  extends BaseFragment
  implements NumberPicker.OnValueChangeListener
{
  private final Integer MAX_HEIGHT_CENTIMETERS = Integer.valueOf(244);
  private final Integer MAX_HEIGHT_INCH = Integer.valueOf(96);
  private final Integer MAX_WEIGHT_KILOGRAM = Integer.valueOf(201);
  private final Integer MAX_WEIGHT_POUND = Integer.valueOf(441);
  private final Integer MIN_HEIGHT_CENTIMETERS = Integer.valueOf(70);
  private final Integer MIN_HEIGHT_INCH = Integer.valueOf(28);
  private final Integer MIN_WEIGHT_KILOGRAM = Integer.valueOf(24);
  private final Integer MIN_WEIGHT_POUND = Integer.valueOf(55);
  private TextView buttonPickerCancel;
  private TextView buttonPickerDone;
  private DatePicker datePicker;
  private View fragmentView;
  private NumberPicker numberPicker;
  private LinearLayout pickerContainer;
  private UserProfileDataManager userProfileDataManager;
  private UserProfilePickerButtons userProfilePickerButtons;
  
  private void getAgePicker()
  {
    if (Build.VERSION.SDK_INT >= 11) {}
    try
    {
      this.datePicker.getClass().getMethod("setCalendarViewShown", new Class[] { Boolean.TYPE }).invoke(this.datePicker, new Object[] { Boolean.valueOf(false) });
      Calendar localCalendar = GregorianCalendar.getInstance();
      localCalendar.setTime((Date)this.userProfileDataManager.getCurrentValue());
      int k = localCalendar.get(1);
      int i = localCalendar.get(2);
      int j = localCalendar.get(5);
      this.datePicker.setMaxDate(new Date().getTime());
      this.datePicker.init(Integer.valueOf(k).intValue(), Integer.valueOf(i).intValue(), Integer.valueOf(j).intValue(), new DatePicker.OnDateChangedListener()
      {
        public void onDateChanged(DatePicker paramAnonymousDatePicker, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
        {
          paramAnonymousDatePicker = GregorianCalendar.getInstance();
          paramAnonymousDatePicker.set(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
          paramAnonymousDatePicker = paramAnonymousDatePicker.getTime();
          ProfilePickerFragment.this.userProfileDataManager.setTempValue(paramAnonymousDatePicker);
        }
      });
      return;
    }
    catch (Exception localException)
    {
      for (;;) {}
    }
  }
  
  private void getCollarSizePicker()
  {
    this.numberPicker.setMaxValue(1);
    this.numberPicker.setMinValue(0);
    Object localObject = UserProfileDataManager.COLLAR_SIZE_BIG;
    String str = UserProfileDataManager.COLLAR_SIZE_SMALL;
    this.numberPicker.setDisplayedValues(new String[] { localObject, str });
    localObject = this.numberPicker;
    if (((Boolean)this.userProfileDataManager.getCurrentValue()).booleanValue()) {}
    for (int i = 0;; i = 1)
    {
      ((NumberPicker)localObject).setValue(i);
      this.numberPicker.setWrapSelectorWheel(false);
      return;
    }
  }
  
  private void getGenderPicker()
  {
    this.numberPicker.setMaxValue(1);
    this.numberPicker.setMinValue(0);
    String str2 = UserProfileDataManager.GENDER_MALE;
    String str1 = UserProfileDataManager.GENDER_FEMALE;
    this.numberPicker.setValue(((Integer)this.userProfileDataManager.getCurrentValue()).intValue());
    this.numberPicker.setDisplayedValues(new String[] { str2, str1 });
    this.numberPicker.setWrapSelectorWheel(false);
  }
  
  /* Error */
  private void getHeightPicker()
  {
    // Byte code:
    //   0: invokestatic 212	com/resmed/refresh/model/RefreshModelController:getInstance	()Lcom/resmed/refresh/model/RefreshModelController;
    //   3: invokevirtual 216	com/resmed/refresh/model/RefreshModelController:getUser	()Lcom/resmed/refresh/model/RST_User;
    //   6: invokevirtual 222	com/resmed/refresh/model/RST_User:getSettings	()Lcom/resmed/refresh/model/RST_Settings;
    //   9: invokevirtual 227	com/resmed/refresh/model/RST_Settings:getHeightUnit	()I
    //   12: iconst_1
    //   13: if_icmpne +191 -> 204
    //   16: aload_0
    //   17: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   20: aload_0
    //   21: getfield 54	com/resmed/refresh/ui/fragments/ProfilePickerFragment:MAX_HEIGHT_CENTIMETERS	Ljava/lang/Integer;
    //   24: invokevirtual 156	java/lang/Integer:intValue	()I
    //   27: invokevirtual 172	android/widget/NumberPicker:setMaxValue	(I)V
    //   30: aload_0
    //   31: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   34: aload_0
    //   35: getfield 56	com/resmed/refresh/ui/fragments/ProfilePickerFragment:MIN_HEIGHT_CENTIMETERS	Ljava/lang/Integer;
    //   38: invokevirtual 156	java/lang/Integer:intValue	()I
    //   41: invokevirtual 175	android/widget/NumberPicker:setMinValue	(I)V
    //   44: aload_0
    //   45: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   48: invokevirtual 230	android/widget/NumberPicker:getMaxValue	()I
    //   51: aload_0
    //   52: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   55: invokevirtual 233	android/widget/NumberPicker:getMinValue	()I
    //   58: isub
    //   59: iconst_1
    //   60: iadd
    //   61: anewarray 184	java/lang/String
    //   64: astore_3
    //   65: iconst_0
    //   66: istore_1
    //   67: iload_1
    //   68: aload_3
    //   69: arraylength
    //   70: if_icmplt +88 -> 158
    //   73: aload_0
    //   74: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   77: aload_3
    //   78: invokevirtual 188	android/widget/NumberPicker:setDisplayedValues	([Ljava/lang/String;)V
    //   81: aload_0
    //   82: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   85: aload_0
    //   86: getfield 77	com/resmed/refresh/ui/fragments/ProfilePickerFragment:userProfileDataManager	Lcom/resmed/refresh/ui/utils/UserProfileDataManager;
    //   89: invokevirtual 129	com/resmed/refresh/ui/utils/UserProfileDataManager:getCurrentValue	()Ljava/lang/Object;
    //   92: checkcast 48	java/lang/Integer
    //   95: invokevirtual 156	java/lang/Integer:intValue	()I
    //   98: invokevirtual 195	android/widget/NumberPicker:setValue	(I)V
    //   101: aload_0
    //   102: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   105: iconst_0
    //   106: invokevirtual 199	android/widget/NumberPicker:setWrapSelectorWheel	(Z)V
    //   109: aload_0
    //   110: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   113: invokevirtual 94	java/lang/Object:getClass	()Ljava/lang/Class;
    //   116: ldc -21
    //   118: iconst_1
    //   119: anewarray 98	java/lang/Class
    //   122: dup
    //   123: iconst_0
    //   124: getstatic 104	java/lang/Boolean:TYPE	Ljava/lang/Class;
    //   127: aastore
    //   128: invokevirtual 238	java/lang/Class:getDeclaredMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   131: astore_2
    //   132: aload_2
    //   133: iconst_1
    //   134: invokevirtual 241	java/lang/reflect/Method:setAccessible	(Z)V
    //   137: aload_2
    //   138: aload_0
    //   139: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   142: iconst_1
    //   143: anewarray 90	java/lang/Object
    //   146: dup
    //   147: iconst_0
    //   148: iconst_1
    //   149: invokestatic 111	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   152: aastore
    //   153: invokevirtual 117	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   156: pop
    //   157: return
    //   158: new 243	java/lang/StringBuilder
    //   161: astore_2
    //   162: aload_2
    //   163: aload_0
    //   164: getfield 56	com/resmed/refresh/ui/fragments/ProfilePickerFragment:MIN_HEIGHT_CENTIMETERS	Ljava/lang/Integer;
    //   167: invokevirtual 156	java/lang/Integer:intValue	()I
    //   170: iload_1
    //   171: iadd
    //   172: invokestatic 246	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   175: invokespecial 249	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   178: aload_3
    //   179: iload_1
    //   180: aload_2
    //   181: ldc -5
    //   183: invokevirtual 255	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: invokevirtual 259	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   189: aastore
    //   190: iinc 1 1
    //   193: goto -126 -> 67
    //   196: astore_2
    //   197: aload_2
    //   198: invokevirtual 262	java/lang/Exception:printStackTrace	()V
    //   201: goto -100 -> 101
    //   204: invokestatic 212	com/resmed/refresh/model/RefreshModelController:getInstance	()Lcom/resmed/refresh/model/RefreshModelController;
    //   207: invokevirtual 216	com/resmed/refresh/model/RefreshModelController:getUser	()Lcom/resmed/refresh/model/RST_User;
    //   210: invokevirtual 222	com/resmed/refresh/model/RST_User:getSettings	()Lcom/resmed/refresh/model/RST_Settings;
    //   213: invokevirtual 227	com/resmed/refresh/model/RST_Settings:getHeightUnit	()I
    //   216: ifne -115 -> 101
    //   219: aload_0
    //   220: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   223: aload_0
    //   224: getfield 58	com/resmed/refresh/ui/fragments/ProfilePickerFragment:MAX_HEIGHT_INCH	Ljava/lang/Integer;
    //   227: invokevirtual 156	java/lang/Integer:intValue	()I
    //   230: invokevirtual 172	android/widget/NumberPicker:setMaxValue	(I)V
    //   233: aload_0
    //   234: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   237: aload_0
    //   238: getfield 60	com/resmed/refresh/ui/fragments/ProfilePickerFragment:MIN_HEIGHT_INCH	Ljava/lang/Integer;
    //   241: invokevirtual 156	java/lang/Integer:intValue	()I
    //   244: invokevirtual 175	android/widget/NumberPicker:setMinValue	(I)V
    //   247: aload_0
    //   248: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   251: invokevirtual 230	android/widget/NumberPicker:getMaxValue	()I
    //   254: aload_0
    //   255: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   258: invokevirtual 233	android/widget/NumberPicker:getMinValue	()I
    //   261: isub
    //   262: iconst_1
    //   263: iadd
    //   264: anewarray 184	java/lang/String
    //   267: astore_2
    //   268: iconst_0
    //   269: istore_1
    //   270: iload_1
    //   271: aload_2
    //   272: arraylength
    //   273: if_icmplt +34 -> 307
    //   276: aload_0
    //   277: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   280: aload_2
    //   281: invokevirtual 188	android/widget/NumberPicker:setDisplayedValues	([Ljava/lang/String;)V
    //   284: aload_0
    //   285: getfield 166	com/resmed/refresh/ui/fragments/ProfilePickerFragment:numberPicker	Landroid/widget/NumberPicker;
    //   288: aload_0
    //   289: getfield 77	com/resmed/refresh/ui/fragments/ProfilePickerFragment:userProfileDataManager	Lcom/resmed/refresh/ui/utils/UserProfileDataManager;
    //   292: invokevirtual 129	com/resmed/refresh/ui/utils/UserProfileDataManager:getCurrentValue	()Ljava/lang/Object;
    //   295: checkcast 48	java/lang/Integer
    //   298: invokevirtual 156	java/lang/Integer:intValue	()I
    //   301: invokevirtual 195	android/widget/NumberPicker:setValue	(I)V
    //   304: goto -203 -> 101
    //   307: aload_0
    //   308: getfield 60	com/resmed/refresh/ui/fragments/ProfilePickerFragment:MIN_HEIGHT_INCH	Ljava/lang/Integer;
    //   311: invokevirtual 156	java/lang/Integer:intValue	()I
    //   314: iload_1
    //   315: iadd
    //   316: bipush 12
    //   318: irem
    //   319: ifeq +64 -> 383
    //   322: aload_2
    //   323: iload_1
    //   324: new 243	java/lang/StringBuilder
    //   327: dup
    //   328: aload_0
    //   329: getfield 60	com/resmed/refresh/ui/fragments/ProfilePickerFragment:MIN_HEIGHT_INCH	Ljava/lang/Integer;
    //   332: invokevirtual 156	java/lang/Integer:intValue	()I
    //   335: iload_1
    //   336: iadd
    //   337: bipush 12
    //   339: idiv
    //   340: invokestatic 246	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   343: invokespecial 249	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   346: ldc_w 264
    //   349: invokevirtual 255	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   352: aload_0
    //   353: getfield 60	com/resmed/refresh/ui/fragments/ProfilePickerFragment:MIN_HEIGHT_INCH	Ljava/lang/Integer;
    //   356: invokevirtual 156	java/lang/Integer:intValue	()I
    //   359: iload_1
    //   360: iadd
    //   361: bipush 12
    //   363: irem
    //   364: invokevirtual 267	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   367: ldc_w 269
    //   370: invokevirtual 255	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   373: invokevirtual 259	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   376: aastore
    //   377: iinc 1 1
    //   380: goto -110 -> 270
    //   383: aload_2
    //   384: iload_1
    //   385: new 243	java/lang/StringBuilder
    //   388: dup
    //   389: aload_0
    //   390: getfield 60	com/resmed/refresh/ui/fragments/ProfilePickerFragment:MIN_HEIGHT_INCH	Ljava/lang/Integer;
    //   393: invokevirtual 156	java/lang/Integer:intValue	()I
    //   396: iload_1
    //   397: iadd
    //   398: bipush 12
    //   400: idiv
    //   401: invokestatic 246	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   404: invokespecial 249	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   407: ldc_w 264
    //   410: invokevirtual 255	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   413: invokevirtual 259	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   416: aastore
    //   417: goto -40 -> 377
    //   420: astore_2
    //   421: aload_2
    //   422: invokevirtual 262	java/lang/Exception:printStackTrace	()V
    //   425: goto -268 -> 157
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	428	0	this	ProfilePickerFragment
    //   66	332	1	i	int
    //   131	50	2	localObject	Object
    //   196	2	2	localException1	Exception
    //   267	117	2	arrayOfString1	String[]
    //   420	2	2	localException2	Exception
    //   64	115	3	arrayOfString2	String[]
    // Exception table:
    //   from	to	target	type
    //   16	65	196	java/lang/Exception
    //   67	101	196	java/lang/Exception
    //   158	190	196	java/lang/Exception
    //   109	157	420	java/lang/Exception
  }
  
  private void getWeightPicker()
  {
    this.numberPicker.setDisplayedValues(null);
    Object localObject;
    int i;
    if (RefreshModelController.getInstance().getUser().getSettings().getWeightUnit() == 2)
    {
      this.numberPicker.setMaxValue(this.MAX_WEIGHT_POUND.intValue());
      this.numberPicker.setMinValue(this.MIN_WEIGHT_POUND.intValue());
      localObject = new String[this.numberPicker.getMaxValue() - this.numberPicker.getMinValue() + 1];
      i = 0;
    }
    for (;;)
    {
      if (i >= localObject.length)
      {
        this.numberPicker.setDisplayedValues((String[])localObject);
        this.numberPicker.setValue(((Integer)this.userProfileDataManager.getCurrentValue()).intValue());
        this.numberPicker.setWrapSelectorWheel(false);
      }
      try
      {
        localObject = this.numberPicker.getClass().getDeclaredMethod("changeValueByOne", new Class[] { Boolean.TYPE });
        ((Method)localObject).setAccessible(true);
        ((Method)localObject).invoke(this.numberPicker, new Object[] { Boolean.valueOf(true) });
        return;
        if ((this.MIN_WEIGHT_POUND.intValue() + i) % 14 != 0) {
          localObject[i] = ((this.MIN_WEIGHT_POUND.intValue() + i) / 14 + " st " + (this.MIN_WEIGHT_POUND.intValue() + i) % 14 + " lb");
        }
        for (;;)
        {
          i++;
          break;
          localObject[i] = ((this.MIN_WEIGHT_POUND.intValue() + i) / 14 + " st ");
        }
        if (RefreshModelController.getInstance().getUser().getSettings().getWeightUnit() == 1)
        {
          this.numberPicker.setMaxValue(this.MAX_WEIGHT_KILOGRAM.intValue());
          this.numberPicker.setMinValue(this.MIN_WEIGHT_KILOGRAM.intValue());
          localObject = new String[this.numberPicker.getMaxValue() - this.numberPicker.getMinValue() + 1];
          for (i = 0;; i++)
          {
            if (i >= localObject.length)
            {
              this.numberPicker.setDisplayedValues((String[])localObject);
              this.numberPicker.setValue(((Integer)this.userProfileDataManager.getCurrentValue()).intValue());
              break;
            }
            localObject[i] = (this.MIN_WEIGHT_KILOGRAM.intValue() + i + " kg");
          }
        }
        this.numberPicker.setMaxValue(this.MAX_WEIGHT_POUND.intValue());
        this.numberPicker.setMinValue(this.MIN_WEIGHT_POUND.intValue());
        localObject = new String[this.numberPicker.getMaxValue() - this.numberPicker.getMinValue() + 1];
        for (i = 0;; i++)
        {
          if (i >= localObject.length)
          {
            this.numberPicker.setDisplayedValues((String[])localObject);
            this.numberPicker.setValue(((Integer)this.userProfileDataManager.getCurrentValue()).intValue());
            break;
          }
          localObject[i] = (this.MIN_WEIGHT_POUND.intValue() + i + " lb");
        }
      }
      catch (Exception localException)
      {
        for (;;)
        {
          localException.printStackTrace();
        }
      }
    }
  }
  
  private void hideAllPicker()
  {
    this.numberPicker.setVisibility(8);
    this.datePicker.setVisibility(8);
  }
  
  private void mapGUI(View paramView)
  {
    this.pickerContainer = ((LinearLayout)paramView.findViewById(2131100324));
    this.buttonPickerCancel = ((TextView)paramView.findViewById(2131100326));
    this.buttonPickerDone = ((TextView)paramView.findViewById(2131100327));
    this.numberPicker = ((NumberPicker)paramView.findViewById(2131100329));
    this.datePicker = ((DatePicker)paramView.findViewById(2131100330));
    this.datePicker.setDescendantFocusability(393216);
    this.numberPicker.setOnValueChangedListener(this);
    this.numberPicker.setDescendantFocusability(393216);
  }
  
  private void setDisplayedValues(Integer paramInteger) {}
  
  private void setSize()
  {
    FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)this.pickerContainer.getLayoutParams();
    localLayoutParams.height = (DisplayManager.getScreenHeight(getActivity()).intValue() / 2);
    this.pickerContainer.setLayoutParams(localLayoutParams);
  }
  
  private void setupListeners()
  {
    View.OnClickListener local2 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        switch (paramAnonymousView.getId())
        {
        }
        for (;;)
        {
          return;
          ProfilePickerFragment.this.userProfilePickerButtons.closePicker();
          continue;
          ProfilePickerFragment.this.userProfileDataManager.performDoneButton();
          ProfilePickerFragment.this.userProfilePickerButtons.closePicker();
        }
      }
    };
    this.buttonPickerCancel.setOnClickListener(local2);
    this.buttonPickerDone.setOnClickListener(local2);
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.userProfilePickerButtons = ((UserProfilePickerButtons)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement UserProfilePickerButtons from your Activity ;-) !");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.fragmentView = paramLayoutInflater.inflate(2130903154, paramViewGroup, false);
    this.userProfileDataManager = UserProfileDataManager.getInstance();
    mapGUI(this.fragmentView);
    setupListeners();
    setSize();
    this.fragmentView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        return true;
      }
    });
    return this.fragmentView;
  }
  
  public void onResume()
  {
    super.onResume();
    refreshPicker();
  }
  
  public void onValueChange(NumberPicker paramNumberPicker, int paramInt1, int paramInt2)
  {
    this.userProfileDataManager.setTempValue(Integer.valueOf(paramInt2));
  }
  
  public void refreshPicker()
  {
    hideAllPicker();
    switch (this.userProfileDataManager.getCurrentPicker().intValue())
    {
    }
    for (;;)
    {
      return;
      this.datePicker.setVisibility(0);
      getAgePicker();
      continue;
      this.numberPicker.setVisibility(0);
      getGenderPicker();
      continue;
      this.numberPicker.setVisibility(0);
      getHeightPicker();
      continue;
      this.numberPicker.setVisibility(0);
      getWeightPicker();
      continue;
      this.numberPicker.setVisibility(0);
      getCollarSizePicker();
    }
  }
  
  public class PickerFormatter
    implements NumberPicker.Formatter
  {
    private Integer type;
    
    public PickerFormatter(Integer paramInteger)
    {
      this.type = paramInteger;
    }
    
    public String format(int paramInt)
    {
      Log.d("", "format(" + paramInt + ") type = " + this.type);
      String str;
      if (this.type == UserProfileDataManager.CENTIMETERS) {
        str = paramInt / 100 + "m " + paramInt % 100 + " cm";
      }
      for (;;)
      {
        return str;
        if (this.type == UserProfileDataManager.INCH) {
          str = paramInt / 12 + "' " + paramInt % 12 + "\"";
        } else if (this.type == UserProfileDataManager.KILOGRAM) {
          str = paramInt + " kg";
        } else if (this.type == UserProfileDataManager.POUND) {
          str = paramInt + " lb";
        } else {
          str = String.valueOf(paramInt);
        }
      }
    }
  }
  
  public static abstract interface UserProfilePickerButtons
  {
    public abstract void closePicker();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\ProfilePickerFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */