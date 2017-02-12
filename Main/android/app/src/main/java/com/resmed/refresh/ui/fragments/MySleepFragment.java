package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.customview.CustomSeekBar;
import com.resmed.refresh.ui.customview.CustomSemiSliderSeekBar;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.utils.RefreshTools;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class MySleepFragment
  extends BaseFragment
  implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, RST_CallbackItem<RST_Response<RST_UserProfile>>
{
  private CheckBox cbAboutMysleepQ14A1;
  private CheckBox cbAboutMysleepQ14A2;
  private CheckBox cbAboutMysleepQ14A3;
  private CheckBox cbAboutMysleepQ14A4;
  private CheckBox cbAboutMysleepQ14A5;
  private CheckBox cbAboutMysleepQ14A6;
  private CheckBox cbAboutMysleepQ14A7;
  private CheckBox cbAboutMysleepQ15A1;
  private CheckBox cbAboutMysleepQ15A2;
  private CheckBox cbAboutMysleepQ15A3;
  private CheckBox cbAboutMysleepQ15A4;
  private CheckBox cbAboutMysleepQ15A5;
  private CheckBox cbAboutMysleepQ15A6;
  private CustomSeekBar csbSetupMySleep_Q11;
  private CustomSeekBar csbSetupMySleep_Q12;
  private CustomSeekBar csbSetupMySleep_Q14d;
  private CustomSeekBar csbSetupMySleep_Q14g;
  private CustomSeekBar csbSetupMySleep_Q7A;
  private CustomSeekBar csbSetupMySleep_Q7B;
  private CustomSeekBar csbSetupMySleep_Q8;
  private CustomSemiSliderSeekBar csssbSetupMySleepQ13;
  private CustomSemiSliderSeekBar csssbSetupMySleepQ14b;
  private CustomSemiSliderSeekBar csssbSetupMySleepQ14c;
  private CustomSemiSliderSeekBar csssbSetupMySleepQ14h;
  private CustomSemiSliderSeekBar csssbSetupMySleepQ14i;
  private CustomSemiSliderSeekBar csssbSetupMySleepQ14j;
  private CustomSemiSliderSeekBar csssbSetupMySleepQ14k;
  private CustomSemiSliderSeekBar csssbSetupMySleepQ14l;
  private CustomSemiSliderSeekBar csssbSetupMySleepQ14m;
  private CustomSemiSliderSeekBar csssbSetupMySleepQ14n;
  private CustomSemiSliderSeekBar csssbSetupMySleepQ9;
  private boolean disableCheckBoxSelFlg;
  private EditText edittextSetupMySleep_Q15bA8;
  private List<ImageView> listIvSetupHowOftenYouSnore;
  private List<ImageView> listIvSetupMySleepAffectsYourSleep;
  private List<ImageView> listIvSetupMySleepDurationOfNap;
  private List<ImageView> listIvSetupMySleepExtentOfInterfere;
  private List<ImageView> listIvSetupMySleepMorningOrEveningPerson;
  private List<ImageView> listIvSetupMySleepRegularBedTime;
  private List<ImageView> listIvSetupMySleepTherapyMachine;
  private List<ImageView> listIvSetupMySleepTherapyMachineManufacturer;
  private List<ImageView> listIvSetupMySleepchildrenUnderAge18;
  private LinearLayout llMysleepBack;
  private LinearLayout llMysleepNext;
  private LinearLayout llSetupMySleep_Q12A;
  LinearLayout llSetupMySleep_Q14A_Q14N;
  LinearLayout llSetupMySleep_Q14B;
  LinearLayout llSetupMySleep_Q14C_Q14E;
  LinearLayout llSetupMySleep_Q14F_Q14J;
  LinearLayout llSetupMySleep_Q14K_Q14N;
  LinearLayout llSetupMySleep_Q15A_Q15B;
  LinearLayout llSetupMySleep_Q15B;
  private LinearLayout llSetupMySleep_Q7A_Q7C;
  public IMySleepUpdateProgress updateMySleepProg;
  
  private void captureOtherSleepTherapyMachineManufacturer(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.edittextSetupMySleep_Q15bA8.setVisibility(0);
      this.edittextSetupMySleep_Q15bA8.setText(RefreshModelController.getInstance().getUser().getProfile().getOtherMachineManufacturer());
    }
    for (;;)
    {
      return;
      this.edittextSetupMySleep_Q15bA8.setVisibility(8);
      this.edittextSetupMySleep_Q15bA8.setText("");
    }
  }
  
  private void hideKeyboard()
  {
    try
    {
      InputMethodManager localInputMethodManager = (InputMethodManager)getActivity().getSystemService("input_method");
      if (getActivity().getCurrentFocus().getWindowToken() != null) {
        localInputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 2);
      }
      return;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        Log.e("KeyBoardUtil", localException.toString(), localException);
      }
    }
  }
  
  private void loadGUI(View paramView)
  {
    this.llMysleepNext = ((LinearLayout)paramView.findViewById(2131100305));
    this.llMysleepBack = ((LinearLayout)paramView.findViewById(2131100304));
    this.listIvSetupMySleepRegularBedTime = new ArrayList();
    this.listIvSetupMySleepRegularBedTime.add((ImageView)paramView.findViewById(2131100154));
    this.listIvSetupMySleepRegularBedTime.add((ImageView)paramView.findViewById(2131100156));
    this.listIvSetupMySleepRegularBedTime.add((ImageView)paramView.findViewById(2131100158));
    this.listIvSetupMySleepRegularBedTime.add((ImageView)paramView.findViewById(2131100160));
    this.listIvSetupMySleepRegularBedTime.add((ImageView)paramView.findViewById(2131100162));
    this.llSetupMySleep_Q7A_Q7C = ((LinearLayout)paramView.findViewById(2131100163));
    this.csbSetupMySleep_Q7A = ((CustomSeekBar)paramView.findViewById(2131100164));
    Object localObject = new ArrayList();
    ((List)localObject).add(getString(2131165490));
    ((List)localObject).add(getString(2131165491));
    ((List)localObject).add(getString(2131165492));
    ((List)localObject).add(getString(2131165493));
    this.csbSetupMySleep_Q7A.setAdapter((List)localObject);
    this.csbSetupMySleep_Q7B = ((CustomSeekBar)paramView.findViewById(2131100165));
    localObject = new ArrayList();
    ((List)localObject).add(getString(2131165495));
    ((List)localObject).add(getString(2131165496));
    ((List)localObject).add(getString(2131165497));
    ((List)localObject).add(getString(2131165498));
    this.csbSetupMySleep_Q7B.setAdapter((List)localObject);
    this.listIvSetupMySleepMorningOrEveningPerson = new ArrayList();
    this.listIvSetupMySleepMorningOrEveningPerson.add((ImageView)paramView.findViewById(2131100167));
    this.listIvSetupMySleepMorningOrEveningPerson.add((ImageView)paramView.findViewById(2131100169));
    this.listIvSetupMySleepMorningOrEveningPerson.add((ImageView)paramView.findViewById(2131100171));
    this.listIvSetupMySleepMorningOrEveningPerson.add((ImageView)paramView.findViewById(2131100173));
    this.csbSetupMySleep_Q8 = ((CustomSeekBar)paramView.findViewById(2131100174));
    localObject = new ArrayList();
    ((List)localObject).add(getString(2131165505));
    ((List)localObject).add(getString(2131165506));
    ((List)localObject).add(getString(2131165507));
    ((List)localObject).add(getString(2131165508));
    this.csbSetupMySleep_Q8.setAdapter((List)localObject);
    this.csssbSetupMySleepQ9 = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100175));
    this.csssbSetupMySleepQ9.setProgressLineColor(-7829368);
    this.csssbSetupMySleepQ9.setBackgroundLineColor(-7829368);
    this.listIvSetupMySleepchildrenUnderAge18 = new ArrayList();
    this.listIvSetupMySleepchildrenUnderAge18.add((ImageView)paramView.findViewById(2131100178));
    this.listIvSetupMySleepchildrenUnderAge18.add((ImageView)paramView.findViewById(2131100180));
    this.listIvSetupMySleepchildrenUnderAge18.add((ImageView)paramView.findViewById(2131100182));
    this.listIvSetupMySleepchildrenUnderAge18.add((ImageView)paramView.findViewById(2131100184));
    this.listIvSetupMySleepchildrenUnderAge18.add((ImageView)paramView.findViewById(2131100186));
    this.csbSetupMySleep_Q11 = ((CustomSeekBar)paramView.findViewById(2131100187));
    localObject = new ArrayList();
    ((List)localObject).add(getString(2131165517));
    ((List)localObject).add(getString(2131165518));
    ((List)localObject).add(getString(2131165519));
    ((List)localObject).add(getString(2131165520));
    this.csbSetupMySleep_Q11.setAdapter((List)localObject);
    this.csbSetupMySleep_Q12 = ((CustomSeekBar)paramView.findViewById(2131100188));
    localObject = new ArrayList();
    ((List)localObject).add(getString(2131165522));
    ((List)localObject).add(getString(2131165523));
    ((List)localObject).add(getString(2131165524));
    ((List)localObject).add(getString(2131165525));
    this.csbSetupMySleep_Q12.setAdapter((List)localObject);
    this.llSetupMySleep_Q12A = ((LinearLayout)paramView.findViewById(2131100189));
    this.llSetupMySleep_Q12A.setVisibility(8);
    this.listIvSetupMySleepDurationOfNap = new ArrayList();
    this.listIvSetupMySleepDurationOfNap.add((ImageView)paramView.findViewById(2131100191));
    this.listIvSetupMySleepDurationOfNap.add((ImageView)paramView.findViewById(2131100193));
    this.listIvSetupMySleepDurationOfNap.add((ImageView)paramView.findViewById(2131100195));
    this.listIvSetupMySleepDurationOfNap.add((ImageView)paramView.findViewById(2131100197));
    this.listIvSetupMySleepDurationOfNap.add((ImageView)paramView.findViewById(2131100199));
    this.csssbSetupMySleepQ13 = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100200));
    this.csssbSetupMySleepQ13.setProgressLineColor(-7829368);
    this.csssbSetupMySleepQ13.setBackgroundLineColor(-7829368);
    this.cbAboutMysleepQ14A1 = ((CheckBox)paramView.findViewById(2131100203));
    this.cbAboutMysleepQ14A2 = ((CheckBox)paramView.findViewById(2131100205));
    this.cbAboutMysleepQ14A3 = ((CheckBox)paramView.findViewById(2131100207));
    this.cbAboutMysleepQ14A4 = ((CheckBox)paramView.findViewById(2131100209));
    this.cbAboutMysleepQ14A5 = ((CheckBox)paramView.findViewById(2131100211));
    this.cbAboutMysleepQ14A6 = ((CheckBox)paramView.findViewById(2131100213));
    this.cbAboutMysleepQ14A7 = ((CheckBox)paramView.findViewById(2131100215));
    this.llSetupMySleep_Q14A_Q14N = ((LinearLayout)paramView.findViewById(2131100216));
    this.llSetupMySleep_Q14A_Q14N.setVisibility(8);
    this.llSetupMySleep_Q14B = ((LinearLayout)paramView.findViewById(2131100230));
    this.llSetupMySleep_Q14B.setVisibility(8);
    this.llSetupMySleep_Q14C_Q14E = ((LinearLayout)paramView.findViewById(2131100232));
    this.llSetupMySleep_Q14C_Q14E.setVisibility(8);
    this.llSetupMySleep_Q14F_Q14J = ((LinearLayout)paramView.findViewById(2131100244));
    this.llSetupMySleep_Q14F_Q14J.setVisibility(8);
    this.llSetupMySleep_Q14K_Q14N = ((LinearLayout)paramView.findViewById(2131100256));
    this.llSetupMySleep_Q14K_Q14N.setVisibility(8);
    this.csssbSetupMySleepQ14b = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100231));
    this.csssbSetupMySleepQ14b.setProgressLineColor(-7829368);
    this.csssbSetupMySleepQ14b.setBackgroundLineColor(-7829368);
    this.csssbSetupMySleepQ14c = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100233));
    this.csssbSetupMySleepQ14c.setProgressLineColor(-7829368);
    this.csssbSetupMySleepQ14c.setBackgroundLineColor(-7829368);
    this.csbSetupMySleep_Q14d = ((CustomSeekBar)paramView.findViewById(2131100234));
    localObject = new ArrayList();
    ((List)localObject).add(getString(2131165552));
    ((List)localObject).add(getString(2131165553));
    ((List)localObject).add(getString(2131165554));
    ((List)localObject).add(getString(2131165555));
    this.csbSetupMySleep_Q14d.setAdapter((List)localObject);
    this.listIvSetupMySleepExtentOfInterfere = new ArrayList();
    this.listIvSetupMySleepExtentOfInterfere.add((ImageView)paramView.findViewById(2131100237));
    this.listIvSetupMySleepExtentOfInterfere.add((ImageView)paramView.findViewById(2131100239));
    this.listIvSetupMySleepExtentOfInterfere.add((ImageView)paramView.findViewById(2131100241));
    this.listIvSetupMySleepExtentOfInterfere.add((ImageView)paramView.findViewById(2131100243));
    this.listIvSetupHowOftenYouSnore = new ArrayList();
    this.listIvSetupHowOftenYouSnore.add((ImageView)paramView.findViewById(2131100247));
    this.listIvSetupHowOftenYouSnore.add((ImageView)paramView.findViewById(2131100249));
    this.listIvSetupHowOftenYouSnore.add((ImageView)paramView.findViewById(2131100251));
    this.csbSetupMySleep_Q14g = ((CustomSeekBar)paramView.findViewById(2131100252));
    localObject = new ArrayList();
    ((List)localObject).add(getString(2131165566));
    ((List)localObject).add(getString(2131165567));
    ((List)localObject).add(getString(2131165568));
    this.csbSetupMySleep_Q14g.setAdapter((List)localObject);
    this.csssbSetupMySleepQ14h = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100253));
    this.csssbSetupMySleepQ14h.setProgressLineColor(-7829368);
    this.csssbSetupMySleepQ14h.setBackgroundLineColor(-7829368);
    this.csssbSetupMySleepQ14i = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100254));
    this.csssbSetupMySleepQ14i.setProgressLineColor(-7829368);
    this.csssbSetupMySleepQ14i.setBackgroundLineColor(-7829368);
    this.csssbSetupMySleepQ14j = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100255));
    this.csssbSetupMySleepQ14j.setProgressLineColor(-7829368);
    this.csssbSetupMySleepQ14j.setBackgroundLineColor(-7829368);
    this.csssbSetupMySleepQ14k = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100257));
    this.csssbSetupMySleepQ14k.setProgressLineColor(-7829368);
    this.csssbSetupMySleepQ14k.setBackgroundLineColor(-7829368);
    this.csssbSetupMySleepQ14l = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100258));
    this.csssbSetupMySleepQ14l.setProgressLineColor(-7829368);
    this.csssbSetupMySleepQ14l.setBackgroundLineColor(-7829368);
    this.csssbSetupMySleepQ14m = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100259));
    this.csssbSetupMySleepQ14m.setProgressLineColor(-7829368);
    this.csssbSetupMySleepQ14m.setBackgroundLineColor(-7829368);
    this.csssbSetupMySleepQ14n = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100260));
    this.csssbSetupMySleepQ14n.setProgressLineColor(-7829368);
    this.csssbSetupMySleepQ14n.setBackgroundLineColor(-7829368);
    this.cbAboutMysleepQ15A1 = ((CheckBox)paramView.findViewById(2131100263));
    this.cbAboutMysleepQ15A2 = ((CheckBox)paramView.findViewById(2131100265));
    this.cbAboutMysleepQ15A3 = ((CheckBox)paramView.findViewById(2131100267));
    this.cbAboutMysleepQ15A4 = ((CheckBox)paramView.findViewById(2131100269));
    this.cbAboutMysleepQ15A5 = ((CheckBox)paramView.findViewById(2131100271));
    this.cbAboutMysleepQ15A6 = ((CheckBox)paramView.findViewById(2131100273));
    this.llSetupMySleep_Q15A_Q15B = ((LinearLayout)paramView.findViewById(2131100274));
    this.llSetupMySleep_Q15A_Q15B.setVisibility(8);
    this.llSetupMySleep_Q15B = ((LinearLayout)paramView.findViewById(2131100284));
    this.llSetupMySleep_Q15B.setVisibility(8);
    this.edittextSetupMySleep_Q15bA8 = ((EditText)paramView.findViewById(2131100303));
    this.edittextSetupMySleep_Q15bA8.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        paramAnonymousView.setFocusable(true);
        paramAnonymousView.setFocusableInTouchMode(true);
        return false;
      }
    });
    this.listIvSetupMySleepAffectsYourSleep = new ArrayList();
    this.listIvSetupMySleepAffectsYourSleep.add((ImageView)paramView.findViewById(2131100219));
    this.listIvSetupMySleepAffectsYourSleep.add((ImageView)paramView.findViewById(2131100221));
    this.listIvSetupMySleepAffectsYourSleep.add((ImageView)paramView.findViewById(2131100223));
    this.listIvSetupMySleepAffectsYourSleep.add((ImageView)paramView.findViewById(2131100225));
    this.listIvSetupMySleepAffectsYourSleep.add((ImageView)paramView.findViewById(2131100227));
    this.listIvSetupMySleepAffectsYourSleep.add((ImageView)paramView.findViewById(2131100229));
    this.listIvSetupMySleepTherapyMachine = new ArrayList();
    this.listIvSetupMySleepTherapyMachine.add((ImageView)paramView.findViewById(2131100277));
    this.listIvSetupMySleepTherapyMachine.add((ImageView)paramView.findViewById(2131100279));
    this.listIvSetupMySleepTherapyMachine.add((ImageView)paramView.findViewById(2131100281));
    this.listIvSetupMySleepTherapyMachine.add((ImageView)paramView.findViewById(2131100283));
    this.listIvSetupMySleepTherapyMachineManufacturer = new ArrayList();
    this.listIvSetupMySleepTherapyMachineManufacturer.add((ImageView)paramView.findViewById(2131100286));
    this.listIvSetupMySleepTherapyMachineManufacturer.add((ImageView)paramView.findViewById(2131100288));
    this.listIvSetupMySleepTherapyMachineManufacturer.add((ImageView)paramView.findViewById(2131100290));
    this.listIvSetupMySleepTherapyMachineManufacturer.add((ImageView)paramView.findViewById(2131100292));
    this.listIvSetupMySleepTherapyMachineManufacturer.add((ImageView)paramView.findViewById(2131100294));
    this.listIvSetupMySleepTherapyMachineManufacturer.add((ImageView)paramView.findViewById(2131100296));
    this.listIvSetupMySleepTherapyMachineManufacturer.add((ImageView)paramView.findViewById(2131100298));
    this.listIvSetupMySleepTherapyMachineManufacturer.add((ImageView)paramView.findViewById(2131100300));
    this.listIvSetupMySleepTherapyMachineManufacturer.add((ImageView)paramView.findViewById(2131100302));
    localObject = RefreshModelController.getInstance().getUser();
    setCustomSeekBarSemiSliderAdapter(((RST_User)localObject).getProfile().isSufferFromAllergies(), this.csssbSetupMySleepQ13);
    setCustomSeekBarSemiSliderAdapter(((RST_User)localObject).getProfile().getStopsBreathing(), this.csssbSetupMySleepQ14b);
    BitSet localBitSet = RefreshTools.bitmaskFromInt(((RST_User)localObject).getProfile().getSleepProblems());
    this.cbAboutMysleepQ14A1.setChecked(localBitSet.get(0));
    this.cbAboutMysleepQ14A2.setChecked(localBitSet.get(1));
    this.cbAboutMysleepQ14A3.setChecked(localBitSet.get(2));
    this.cbAboutMysleepQ14A4.setChecked(localBitSet.get(3));
    this.cbAboutMysleepQ14A5.setChecked(localBitSet.get(4));
    this.cbAboutMysleepQ14A6.setChecked(localBitSet.get(5));
    this.cbAboutMysleepQ14A7.setChecked(localBitSet.get(6));
    showAffectsYourSleepLayout();
    setSleepProblemsCheckBoxEnability(-1);
    localBitSet = RefreshTools.bitmaskFromInt(((RST_User)localObject).getProfile().getSleepDisorder());
    this.cbAboutMysleepQ15A1.setChecked(localBitSet.get(0));
    this.cbAboutMysleepQ15A2.setChecked(localBitSet.get(1));
    this.cbAboutMysleepQ15A3.setChecked(localBitSet.get(2));
    this.cbAboutMysleepQ15A4.setChecked(localBitSet.get(3));
    this.cbAboutMysleepQ15A5.setChecked(localBitSet.get(4));
    this.cbAboutMysleepQ15A6.setChecked(localBitSet.get(5));
    showSleepTherapyMachineLayout();
    setSleepDisorderCheckBoxEnability(-1);
    if (((RST_User)localObject).getProfile().getBedTime() != -1) {
      selectRegularTimeBed(((RST_User)localObject).getProfile().getBedTime());
    }
    showRegularBedTimeLayout(((RST_User)localObject).getProfile().getBedTime());
    this.csbSetupMySleep_Q7A.setSelection(((RST_User)localObject).getProfile().getWakeUpDifficulties());
    this.csbSetupMySleep_Q7B.setSelection(((RST_User)localObject).getProfile().getMorningAlertness());
    if (((RST_User)localObject).getProfile().getTypeofPerson() != -1) {
      selectSleepMorningOrEveningPerson(((RST_User)localObject).getProfile().getTypeofPerson());
    }
    if (((RST_User)localObject).getProfile().getChildrenCount() != -1) {
      selectChildrenUnderAge18(((RST_User)localObject).getProfile().getChildrenCount());
    }
    this.csbSetupMySleep_Q8.setSelection(((RST_User)localObject).getProfile().getTravelImpact());
    setCustomSeekBarSemiSliderAdapter(((RST_User)localObject).getProfile().isSleepWithPartner(), this.csssbSetupMySleepQ9);
    this.csbSetupMySleep_Q11.setSelection(((RST_User)localObject).getProfile().getPetSleepWithYouOnBed());
    this.csbSetupMySleep_Q12.setSelection(((RST_User)localObject).getProfile().getNaps());
    showNapDurationLayout();
    if (((RST_User)localObject).getProfile().getNapTime() != -1) {
      selectNapDuration(((RST_User)localObject).getProfile().getNapTime());
    }
    if (((RST_User)localObject).getProfile().getAffectsYourSleep() != -1) {
      selectAffectsYourSleep(((RST_User)localObject).getProfile().getAffectsYourSleep());
    }
    showAffectsYourSleepLayout(((RST_User)localObject).getProfile().getAffectsYourSleep());
    setCustomSeekBarSemiSliderAdapter(((RST_User)localObject).getProfile().isStayingAwakeDuringDay(), this.csssbSetupMySleepQ14c);
    this.csbSetupMySleep_Q14d.setSelection(((RST_User)localObject).getProfile().getConcernsOnSleepProblem());
    if (((RST_User)localObject).getProfile().getExtentInterfereSleepProblem() != -1) {
      selectExtentOfInterfereInSleepProblem(((RST_User)localObject).getProfile().getExtentInterfereSleepProblem());
    }
    if (((RST_User)localObject).getProfile().getSnoreFrequency() != -1) {
      selectHowOftenYouSnore(((RST_User)localObject).getProfile().getSnoreFrequency());
    }
    this.csbSetupMySleep_Q14g.setSelection(((RST_User)localObject).getProfile().getSleepingPositionSnore());
    setCustomSeekBarSemiSliderAdapter(((RST_User)localObject).getProfile().isDryMouth(), this.csssbSetupMySleepQ14h);
    setCustomSeekBarSemiSliderAdapter(((RST_User)localObject).getProfile().isNasalCongestion(), this.csssbSetupMySleepQ14i);
    setCustomSeekBarSemiSliderAdapter(((RST_User)localObject).getProfile().isBreathingObstruction(), this.csssbSetupMySleepQ14j);
    setCustomSeekBarSemiSliderAdapter(((RST_User)localObject).getProfile().isCrawlingSensation(), this.csssbSetupMySleepQ14k);
    setCustomSeekBarSemiSliderAdapter(((RST_User)localObject).getProfile().isMuscleTension(), this.csssbSetupMySleepQ14l);
    setCustomSeekBarSemiSliderAdapter(((RST_User)localObject).getProfile().isLegsStillAtNight(), this.csssbSetupMySleepQ14m);
    setCustomSeekBarSemiSliderAdapter(((RST_User)localObject).getProfile().getSleepMover(), this.csssbSetupMySleepQ14n);
    if (((RST_User)localObject).getProfile().getSleepDisorderMachine() != -1) {
      selectSleepTherapyMachine(((RST_User)localObject).getProfile().getSleepDisorderMachine());
    }
    showSleepTherapyMachineLayout();
    if (((RST_User)localObject).getProfile().getMachineManufacturer() != -1) {
      selectSleepTherapyMachineManufacturer(((RST_User)localObject).getProfile().getMachineManufacturer());
    }
    showSleepTherapyMachineManufacturerLayout(0);
    this.csbSetupMySleep_Q7A.setOnSeekBarChangeListener(this);
    this.csbSetupMySleep_Q7B.setOnSeekBarChangeListener(this);
    paramView.findViewById(2131100153).setOnClickListener(this);
    paramView.findViewById(2131100155).setOnClickListener(this);
    paramView.findViewById(2131100157).setOnClickListener(this);
    paramView.findViewById(2131100159).setOnClickListener(this);
    paramView.findViewById(2131100161).setOnClickListener(this);
    paramView.findViewById(2131100166).setOnClickListener(this);
    paramView.findViewById(2131100168).setOnClickListener(this);
    paramView.findViewById(2131100170).setOnClickListener(this);
    paramView.findViewById(2131100172).setOnClickListener(this);
    paramView.findViewById(2131100177).setOnClickListener(this);
    paramView.findViewById(2131100179).setOnClickListener(this);
    paramView.findViewById(2131100181).setOnClickListener(this);
    paramView.findViewById(2131100183).setOnClickListener(this);
    paramView.findViewById(2131100185).setOnClickListener(this);
    paramView.findViewById(2131100190).setOnClickListener(this);
    paramView.findViewById(2131100192).setOnClickListener(this);
    paramView.findViewById(2131100194).setOnClickListener(this);
    paramView.findViewById(2131100196).setOnClickListener(this);
    paramView.findViewById(2131100198).setOnClickListener(this);
    paramView.findViewById(2131100218).setOnClickListener(this);
    paramView.findViewById(2131100220).setOnClickListener(this);
    paramView.findViewById(2131100222).setOnClickListener(this);
    paramView.findViewById(2131100224).setOnClickListener(this);
    paramView.findViewById(2131100226).setOnClickListener(this);
    paramView.findViewById(2131100228).setOnClickListener(this);
    paramView.findViewById(2131100236).setOnClickListener(this);
    paramView.findViewById(2131100238).setOnClickListener(this);
    paramView.findViewById(2131100240).setOnClickListener(this);
    paramView.findViewById(2131100242).setOnClickListener(this);
    paramView.findViewById(2131100246).setOnClickListener(this);
    paramView.findViewById(2131100248).setOnClickListener(this);
    paramView.findViewById(2131100250).setOnClickListener(this);
    paramView.findViewById(2131100276).setOnClickListener(this);
    paramView.findViewById(2131100278).setOnClickListener(this);
    paramView.findViewById(2131100280).setOnClickListener(this);
    paramView.findViewById(2131100282).setOnClickListener(this);
    paramView.findViewById(2131100285).setOnClickListener(this);
    paramView.findViewById(2131100287).setOnClickListener(this);
    paramView.findViewById(2131100289).setOnClickListener(this);
    paramView.findViewById(2131100291).setOnClickListener(this);
    paramView.findViewById(2131100293).setOnClickListener(this);
    paramView.findViewById(2131100295).setOnClickListener(this);
    paramView.findViewById(2131100297).setOnClickListener(this);
    paramView.findViewById(2131100299).setOnClickListener(this);
    paramView.findViewById(2131100301).setOnClickListener(this);
    this.csssbSetupMySleepQ13.setOnSeekBarChangeListener(this);
    this.csssbSetupMySleepQ14b.setOnSeekBarChangeListener(this);
    this.csssbSetupMySleepQ14c.setOnSeekBarChangeListener(this);
    this.csssbSetupMySleepQ14h.setOnSeekBarChangeListener(this);
    this.csssbSetupMySleepQ14i.setOnSeekBarChangeListener(this);
    this.csssbSetupMySleepQ14j.setOnSeekBarChangeListener(this);
    this.csssbSetupMySleepQ14k.setOnSeekBarChangeListener(this);
    this.csssbSetupMySleepQ14l.setOnSeekBarChangeListener(this);
    this.csssbSetupMySleepQ14m.setOnSeekBarChangeListener(this);
    this.csssbSetupMySleepQ14n.setOnSeekBarChangeListener(this);
    this.csssbSetupMySleepQ9.setOnSeekBarChangeListener(this);
    this.csbSetupMySleep_Q8.setOnSeekBarChangeListener(this);
    this.csbSetupMySleep_Q11.setOnSeekBarChangeListener(this);
    this.csbSetupMySleep_Q12.setOnSeekBarChangeListener(this);
    this.csbSetupMySleep_Q14d.setOnSeekBarChangeListener(this);
    this.csbSetupMySleep_Q14g.setOnSeekBarChangeListener(this);
    this.cbAboutMysleepQ14A1.setOnCheckedChangeListener(this);
    this.cbAboutMysleepQ14A2.setOnCheckedChangeListener(this);
    this.cbAboutMysleepQ14A3.setOnCheckedChangeListener(this);
    this.cbAboutMysleepQ14A4.setOnCheckedChangeListener(this);
    this.cbAboutMysleepQ14A5.setOnCheckedChangeListener(this);
    this.cbAboutMysleepQ14A6.setOnCheckedChangeListener(this);
    this.cbAboutMysleepQ14A7.setOnCheckedChangeListener(this);
    this.cbAboutMysleepQ15A1.setOnCheckedChangeListener(this);
    this.cbAboutMysleepQ15A2.setOnCheckedChangeListener(this);
    this.cbAboutMysleepQ15A3.setOnCheckedChangeListener(this);
    this.cbAboutMysleepQ15A4.setOnCheckedChangeListener(this);
    this.cbAboutMysleepQ15A5.setOnCheckedChangeListener(this);
    this.cbAboutMysleepQ15A6.setOnCheckedChangeListener(this);
    this.llMysleepNext.setOnClickListener(this);
    this.llMysleepBack.setOnClickListener(this);
  }
  
  private void removeEditTextFocus()
  {
    this.edittextSetupMySleep_Q15bA8.setFocusable(false);
    this.edittextSetupMySleep_Q15bA8.setFocusableInTouchMode(false);
  }
  
  private void saveData()
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    localRefreshModelController.save();
    localRefreshModelController.updateUserProfile(this);
  }
  
  private void saveDataInDatabase()
  {
    RefreshModelController.getInstance().getUser().getProfile().setOtherMachineManufacturer(this.edittextSetupMySleep_Q15bA8.getText().toString().trim());
    RefreshModelController.getInstance().save();
  }
  
  private void selectAffectsYourSleep(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupMySleepAffectsYourSleep.size())
      {
        if (this.listIvSetupMySleepAffectsYourSleep.size() > paramInt) {
          ((ImageView)this.listIvSetupMySleepAffectsYourSleep.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setAffectsYourSleep(paramInt);
        RefreshModelController.getInstance().save();
        this.updateMySleepProg.updateMySleepProgress();
        return;
      }
      ((ImageView)this.listIvSetupMySleepAffectsYourSleep.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectChildrenUnderAge18(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupMySleepchildrenUnderAge18.size())
      {
        if (this.listIvSetupMySleepchildrenUnderAge18.size() > paramInt) {
          ((ImageView)this.listIvSetupMySleepchildrenUnderAge18.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setChildrenCount(paramInt);
        RefreshModelController.getInstance().save();
        this.updateMySleepProg.updateMySleepProgress();
        return;
      }
      ((ImageView)this.listIvSetupMySleepchildrenUnderAge18.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectExtentOfInterfereInSleepProblem(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupMySleepExtentOfInterfere.size())
      {
        if (this.listIvSetupMySleepExtentOfInterfere.size() > paramInt) {
          ((ImageView)this.listIvSetupMySleepExtentOfInterfere.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setExtentInterfereSleepProblem(paramInt);
        RefreshModelController.getInstance().save();
        this.updateMySleepProg.updateMySleepProgress();
        return;
      }
      ((ImageView)this.listIvSetupMySleepExtentOfInterfere.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectHowOftenYouSnore(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupHowOftenYouSnore.size())
      {
        if (this.listIvSetupHowOftenYouSnore.size() > paramInt) {
          ((ImageView)this.listIvSetupHowOftenYouSnore.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setSnoreFrequency(paramInt);
        RefreshModelController.getInstance().save();
        this.updateMySleepProg.updateMySleepProgress();
        return;
      }
      ((ImageView)this.listIvSetupHowOftenYouSnore.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectNapDuration(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupMySleepDurationOfNap.size())
      {
        if (this.listIvSetupMySleepDurationOfNap.size() > paramInt) {
          ((ImageView)this.listIvSetupMySleepDurationOfNap.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setNapTime(paramInt);
        RefreshModelController.getInstance().save();
        this.updateMySleepProg.updateMySleepProgress();
        return;
      }
      ((ImageView)this.listIvSetupMySleepDurationOfNap.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectRegularTimeBed(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupMySleepRegularBedTime.size())
      {
        if (this.listIvSetupMySleepRegularBedTime.size() > paramInt) {
          ((ImageView)this.listIvSetupMySleepRegularBedTime.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setBedTime(paramInt);
        RefreshModelController.getInstance().save();
        this.updateMySleepProg.updateMySleepProgress();
        return;
      }
      ((ImageView)this.listIvSetupMySleepRegularBedTime.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectSleepMorningOrEveningPerson(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupMySleepMorningOrEveningPerson.size())
      {
        if (this.listIvSetupMySleepMorningOrEveningPerson.size() > paramInt) {
          ((ImageView)this.listIvSetupMySleepMorningOrEveningPerson.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setTypeofPerson(paramInt);
        RefreshModelController.getInstance().save();
        this.updateMySleepProg.updateMySleepProgress();
        return;
      }
      ((ImageView)this.listIvSetupMySleepMorningOrEveningPerson.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectSleepTherapyMachine(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupMySleepTherapyMachine.size())
      {
        if (this.listIvSetupMySleepTherapyMachine.size() > paramInt) {
          ((ImageView)this.listIvSetupMySleepTherapyMachine.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setSleepDisorderMachine(paramInt);
        RefreshModelController.getInstance().save();
        this.updateMySleepProg.updateMySleepProgress();
        return;
      }
      ((ImageView)this.listIvSetupMySleepTherapyMachine.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectSleepTherapyMachineManufacturer(int paramInt)
  {
    int i = 0;
    if (i >= this.listIvSetupMySleepTherapyMachineManufacturer.size()) {
      if (this.listIvSetupMySleepTherapyMachineManufacturer.size() > paramInt)
      {
        ((ImageView)this.listIvSetupMySleepTherapyMachineManufacturer.get(paramInt)).setImageResource(2130837867);
        if (paramInt != 7) {
          break label112;
        }
        captureOtherSleepTherapyMachineManufacturer(true);
      }
    }
    for (;;)
    {
      RefreshModelController.getInstance().getUser().getProfile().setMachineManufacturer(paramInt);
      RefreshModelController.getInstance().save();
      this.updateMySleepProg.updateMySleepProgress();
      return;
      ((ImageView)this.listIvSetupMySleepTherapyMachineManufacturer.get(i)).setImageResource(2130837863);
      i++;
      break;
      label112:
      captureOtherSleepTherapyMachineManufacturer(false);
    }
  }
  
  private void setCustomSeekBarSemiSliderAdapter(int paramInt, View paramView)
  {
    ArrayList localArrayList = new ArrayList();
    if (paramInt == -1)
    {
      localArrayList.add(getResources().getString(2131165299));
      localArrayList.add("");
      localArrayList.add(getResources().getString(2131165298));
      paramInt = 1;
      switch (paramView.getId())
      {
      }
    }
    for (;;)
    {
      return;
      localArrayList.add(getResources().getString(2131165299));
      localArrayList.add(getResources().getString(2131165298));
      break;
      this.csssbSetupMySleepQ13.setAdapter(localArrayList);
      this.csssbSetupMySleepQ13.setSelection(paramInt);
      continue;
      this.csssbSetupMySleepQ14b.setAdapter(localArrayList);
      this.csssbSetupMySleepQ14b.setSelection(paramInt);
      continue;
      this.csssbSetupMySleepQ14c.setAdapter(localArrayList);
      this.csssbSetupMySleepQ14c.setSelection(paramInt);
      continue;
      this.csssbSetupMySleepQ14h.setAdapter(localArrayList);
      this.csssbSetupMySleepQ14h.setSelection(paramInt);
      continue;
      this.csssbSetupMySleepQ14i.setAdapter(localArrayList);
      this.csssbSetupMySleepQ14i.setSelection(paramInt);
      continue;
      this.csssbSetupMySleepQ14j.setAdapter(localArrayList);
      this.csssbSetupMySleepQ14j.setSelection(paramInt);
      continue;
      this.csssbSetupMySleepQ14k.setAdapter(localArrayList);
      this.csssbSetupMySleepQ14k.setSelection(paramInt);
      continue;
      this.csssbSetupMySleepQ14l.setAdapter(localArrayList);
      this.csssbSetupMySleepQ14l.setSelection(paramInt);
      continue;
      this.csssbSetupMySleepQ14m.setAdapter(localArrayList);
      this.csssbSetupMySleepQ14m.setSelection(paramInt);
      continue;
      this.csssbSetupMySleepQ14n.setAdapter(localArrayList);
      this.csssbSetupMySleepQ14n.setSelection(paramInt);
      continue;
      this.csssbSetupMySleepQ9.setAdapter(localArrayList);
      this.csssbSetupMySleepQ9.setSelection(paramInt);
    }
  }
  
  private void setSleepDisorderCheckBoxEnability(int paramInt)
  {
    switch (paramInt)
    {
    }
    for (;;)
    {
      return;
      if (this.cbAboutMysleepQ15A6.isChecked())
      {
        this.disableCheckBoxSelFlg = true;
        this.cbAboutMysleepQ15A1.setChecked(false);
        this.cbAboutMysleepQ15A2.setChecked(false);
        this.cbAboutMysleepQ15A3.setChecked(false);
        this.cbAboutMysleepQ15A4.setChecked(false);
        this.cbAboutMysleepQ15A5.setChecked(false);
        this.disableCheckBoxSelFlg = false;
      }
      else if ((this.cbAboutMysleepQ15A1.isChecked()) || (this.cbAboutMysleepQ15A2.isChecked()) || (this.cbAboutMysleepQ15A3.isChecked()) || (this.cbAboutMysleepQ15A4.isChecked()) || (this.cbAboutMysleepQ15A5.isChecked()))
      {
        this.cbAboutMysleepQ15A6.setChecked(false);
        continue;
        if ((!this.disableCheckBoxSelFlg) && ((this.cbAboutMysleepQ15A1.isChecked()) || (this.cbAboutMysleepQ15A2.isChecked()) || (this.cbAboutMysleepQ15A3.isChecked()) || (this.cbAboutMysleepQ15A4.isChecked()) || (this.cbAboutMysleepQ15A5.isChecked())))
        {
          this.cbAboutMysleepQ15A6.setChecked(false);
          continue;
          if (this.cbAboutMysleepQ15A6.isChecked())
          {
            this.disableCheckBoxSelFlg = true;
            this.cbAboutMysleepQ15A1.setChecked(false);
            this.cbAboutMysleepQ15A2.setChecked(false);
            this.cbAboutMysleepQ15A3.setChecked(false);
            this.cbAboutMysleepQ15A4.setChecked(false);
            this.cbAboutMysleepQ15A5.setChecked(false);
            this.disableCheckBoxSelFlg = false;
          }
        }
      }
    }
  }
  
  private void setSleepProblemsCheckBoxEnability(int paramInt)
  {
    switch (paramInt)
    {
    }
    for (;;)
    {
      return;
      if (this.cbAboutMysleepQ14A7.isChecked())
      {
        this.disableCheckBoxSelFlg = true;
        this.cbAboutMysleepQ14A1.setChecked(false);
        this.cbAboutMysleepQ14A2.setChecked(false);
        this.cbAboutMysleepQ14A3.setChecked(false);
        this.cbAboutMysleepQ14A4.setChecked(false);
        this.cbAboutMysleepQ14A5.setChecked(false);
        this.cbAboutMysleepQ14A6.setChecked(false);
        this.disableCheckBoxSelFlg = false;
      }
      else if ((this.cbAboutMysleepQ14A1.isChecked()) || (this.cbAboutMysleepQ14A2.isChecked()) || (this.cbAboutMysleepQ14A3.isChecked()) || (this.cbAboutMysleepQ14A4.isChecked()) || (this.cbAboutMysleepQ14A5.isChecked()) || (this.cbAboutMysleepQ14A6.isChecked()))
      {
        this.cbAboutMysleepQ14A7.setChecked(false);
        continue;
        if ((!this.disableCheckBoxSelFlg) && ((this.cbAboutMysleepQ14A1.isChecked()) || (this.cbAboutMysleepQ14A2.isChecked()) || (this.cbAboutMysleepQ14A3.isChecked()) || (this.cbAboutMysleepQ14A4.isChecked()) || (this.cbAboutMysleepQ14A5.isChecked()) || (this.cbAboutMysleepQ14A6.isChecked())))
        {
          this.cbAboutMysleepQ14A7.setChecked(false);
          continue;
          if (this.cbAboutMysleepQ14A7.isChecked())
          {
            this.disableCheckBoxSelFlg = true;
            this.cbAboutMysleepQ14A1.setChecked(false);
            this.cbAboutMysleepQ14A2.setChecked(false);
            this.cbAboutMysleepQ14A3.setChecked(false);
            this.cbAboutMysleepQ14A4.setChecked(false);
            this.cbAboutMysleepQ14A5.setChecked(false);
            this.cbAboutMysleepQ14A6.setChecked(false);
            this.disableCheckBoxSelFlg = false;
          }
        }
      }
    }
  }
  
  private void showAffectsYourSleepLayout()
  {
    if ((this.cbAboutMysleepQ14A1.isChecked()) || (this.cbAboutMysleepQ14A2.isChecked()) || (this.cbAboutMysleepQ14A3.isChecked()) || (this.cbAboutMysleepQ14A4.isChecked()) || (this.cbAboutMysleepQ14A5.isChecked()) || (this.cbAboutMysleepQ14A6.isChecked())) {
      this.llSetupMySleep_Q14A_Q14N.setVisibility(0);
    }
    for (;;)
    {
      return;
      this.llSetupMySleep_Q14A_Q14N.setVisibility(8);
    }
  }
  
  private void showAffectsYourSleepLayout(int paramInt)
  {
    this.llSetupMySleep_Q14B.setVisibility(8);
    this.llSetupMySleep_Q14C_Q14E.setVisibility(8);
    this.llSetupMySleep_Q14F_Q14J.setVisibility(8);
    this.llSetupMySleep_Q14K_Q14N.setVisibility(8);
    if ((paramInt == 0) || (paramInt == 1) || (paramInt == 3)) {
      this.llSetupMySleep_Q14C_Q14E.setVisibility(0);
    }
    for (;;)
    {
      return;
      if (paramInt == 2) {
        this.llSetupMySleep_Q14B.setVisibility(0);
      } else if (paramInt == 4) {
        this.llSetupMySleep_Q14F_Q14J.setVisibility(0);
      } else if (paramInt == 5) {
        this.llSetupMySleep_Q14K_Q14N.setVisibility(0);
      }
    }
  }
  
  private void showNapDurationLayout()
  {
    if (this.csbSetupMySleep_Q12.getPositionSelected() > 0) {
      this.llSetupMySleep_Q12A.setVisibility(0);
    }
    for (;;)
    {
      return;
      this.llSetupMySleep_Q12A.setVisibility(8);
    }
  }
  
  private void showRegularBedTimeLayout(int paramInt)
  {
    if ((paramInt == 3) || (paramInt == 4)) {
      this.llSetupMySleep_Q7A_Q7C.setVisibility(0);
    }
    for (;;)
    {
      return;
      this.llSetupMySleep_Q7A_Q7C.setVisibility(8);
    }
  }
  
  private void showSleepTherapyMachineLayout()
  {
    if (this.cbAboutMysleepQ15A4.isChecked()) {
      this.llSetupMySleep_Q15A_Q15B.setVisibility(0);
    }
    for (;;)
    {
      return;
      this.llSetupMySleep_Q15A_Q15B.setVisibility(8);
    }
  }
  
  private void showSleepTherapyMachineManufacturerLayout(int paramInt)
  {
    if ((paramInt == 0) || (paramInt == 1)) {
      this.llSetupMySleep_Q15B.setVisibility(0);
    }
    for (;;)
    {
      return;
      this.llSetupMySleep_Q15B.setVisibility(8);
    }
  }
  
  public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
  {
    removeEditTextFocus();
    RST_User localRST_User = RefreshModelController.getInstance().getUser();
    switch (paramCompoundButton.getId())
    {
    }
    for (;;)
    {
      RefreshModelController.getInstance().save();
      this.updateMySleepProg.updateMySleepProgress();
      return;
      setSleepProblemsCheckBoxEnability(paramCompoundButton.getId());
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbAboutMysleepQ14A1.isChecked());
      paramCompoundButton.set(1, this.cbAboutMysleepQ14A2.isChecked());
      paramCompoundButton.set(2, this.cbAboutMysleepQ14A3.isChecked());
      paramCompoundButton.set(3, this.cbAboutMysleepQ14A4.isChecked());
      paramCompoundButton.set(4, this.cbAboutMysleepQ14A5.isChecked());
      paramCompoundButton.set(5, this.cbAboutMysleepQ14A6.isChecked());
      paramCompoundButton.set(6, this.cbAboutMysleepQ14A7.isChecked());
      localRST_User.getProfile().setSleepProblems(RefreshTools.bitmaskToInt(paramCompoundButton));
      showAffectsYourSleepLayout();
      continue;
      setSleepDisorderCheckBoxEnability(paramCompoundButton.getId());
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbAboutMysleepQ15A1.isChecked());
      paramCompoundButton.set(1, this.cbAboutMysleepQ15A2.isChecked());
      paramCompoundButton.set(2, this.cbAboutMysleepQ15A3.isChecked());
      paramCompoundButton.set(3, this.cbAboutMysleepQ15A4.isChecked());
      paramCompoundButton.set(4, this.cbAboutMysleepQ15A5.isChecked());
      paramCompoundButton.set(5, this.cbAboutMysleepQ15A6.isChecked());
      localRST_User.getProfile().setSleepDisorder(RefreshTools.bitmaskToInt(paramCompoundButton));
      showSleepTherapyMachineLayout();
    }
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default: 
      removeEditTextFocus();
      switch (paramView.getId())
      {
      }
      break;
    }
    for (;;)
    {
      return;
      hideKeyboard();
      break;
      hideKeyboard();
      break;
      saveDataInDatabase();
      this.updateMySleepProg.screenTransitionFromMySleepFrag(2131099818, 2);
      continue;
      this.updateMySleepProg.screenTransitionFromMySleepFrag(2131099810, 1);
      continue;
      selectSleepMorningOrEveningPerson(0);
      continue;
      selectSleepMorningOrEveningPerson(1);
      continue;
      selectSleepMorningOrEveningPerson(2);
      continue;
      selectSleepMorningOrEveningPerson(3);
      continue;
      selectChildrenUnderAge18(0);
      continue;
      selectChildrenUnderAge18(1);
      continue;
      selectChildrenUnderAge18(2);
      continue;
      selectChildrenUnderAge18(3);
      continue;
      selectChildrenUnderAge18(4);
      continue;
      selectNapDuration(0);
      continue;
      selectNapDuration(1);
      continue;
      selectNapDuration(2);
      continue;
      selectNapDuration(3);
      continue;
      selectNapDuration(4);
      continue;
      selectAffectsYourSleep(0);
      showAffectsYourSleepLayout(0);
      continue;
      selectAffectsYourSleep(1);
      showAffectsYourSleepLayout(1);
      continue;
      selectAffectsYourSleep(2);
      showAffectsYourSleepLayout(2);
      continue;
      selectAffectsYourSleep(3);
      showAffectsYourSleepLayout(3);
      continue;
      selectAffectsYourSleep(4);
      showAffectsYourSleepLayout(4);
      continue;
      selectAffectsYourSleep(5);
      showAffectsYourSleepLayout(5);
      continue;
      selectExtentOfInterfereInSleepProblem(0);
      continue;
      selectExtentOfInterfereInSleepProblem(1);
      continue;
      selectExtentOfInterfereInSleepProblem(2);
      continue;
      selectExtentOfInterfereInSleepProblem(3);
      continue;
      selectHowOftenYouSnore(0);
      continue;
      selectHowOftenYouSnore(1);
      continue;
      selectHowOftenYouSnore(2);
      continue;
      selectSleepTherapyMachine(0);
      showSleepTherapyMachineManufacturerLayout(0);
      continue;
      selectSleepTherapyMachine(1);
      showSleepTherapyMachineManufacturerLayout(1);
      continue;
      selectSleepTherapyMachine(2);
      showSleepTherapyMachineManufacturerLayout(2);
      continue;
      selectSleepTherapyMachine(3);
      showSleepTherapyMachineManufacturerLayout(3);
      continue;
      selectSleepTherapyMachineManufacturer(0);
      continue;
      selectSleepTherapyMachineManufacturer(1);
      continue;
      selectSleepTherapyMachineManufacturer(2);
      continue;
      selectSleepTherapyMachineManufacturer(3);
      continue;
      selectSleepTherapyMachineManufacturer(4);
      continue;
      selectSleepTherapyMachineManufacturer(5);
      continue;
      selectSleepTherapyMachineManufacturer(6);
      continue;
      selectSleepTherapyMachineManufacturer(7);
      continue;
      selectSleepTherapyMachineManufacturer(8);
      continue;
      selectRegularTimeBed(0);
      showRegularBedTimeLayout(0);
      continue;
      selectRegularTimeBed(1);
      showRegularBedTimeLayout(1);
      continue;
      selectRegularTimeBed(2);
      showRegularBedTimeLayout(2);
      continue;
      selectRegularTimeBed(3);
      showRegularBedTimeLayout(3);
      continue;
      selectRegularTimeBed(4);
      showRegularBedTimeLayout(4);
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903150, paramViewGroup, false);
    loadGUI(paramLayoutInflater);
    this.updateMySleepProg.updateMySleepProgress();
    return paramLayoutInflater;
  }
  
  public void onPause()
  {
    super.onPause();
    saveDataInDatabase();
    Log.i("************  ", "************In Pasue");
  }
  
  public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean)
  {
    RST_UserProfile localRST_UserProfile = RefreshModelController.getInstance().getUser().getProfile();
    switch (paramSeekBar.getId())
    {
    }
    for (;;)
    {
      RefreshModelController.getInstance().save();
      this.updateMySleepProg.updateMySleepProgress();
      return;
      localRST_UserProfile.setWakeUpDifficulties(paramInt);
      continue;
      localRST_UserProfile.setMorningAlertness(paramInt);
      continue;
      localRST_UserProfile.setTravelImpact(paramInt);
      continue;
      localRST_UserProfile.setPetSleepWithYouOnBed(paramInt);
      continue;
      localRST_UserProfile.setNaps(paramInt);
      showNapDurationLayout();
      continue;
      localRST_UserProfile.setConcernsOnSleepProblem(paramInt);
      continue;
      localRST_UserProfile.setSleepingPositionSnore(paramInt);
      continue;
      int i;
      if (localRST_UserProfile.isSleepWithPartner() == -1)
      {
        if ((paramInt == 0) || (paramInt == 2))
        {
          i = paramInt;
          if (paramInt == 2) {
            i = 1;
          }
          setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMySleepQ9);
          localRST_UserProfile.setSleepWithPartner(i);
        }
      }
      else
      {
        localRST_UserProfile.setSleepWithPartner(paramInt);
        continue;
        if (localRST_UserProfile.isSufferFromAllergies() == -1)
        {
          if ((paramInt == 0) || (paramInt == 2))
          {
            i = paramInt;
            if (paramInt == 2) {
              i = 1;
            }
            setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMySleepQ13);
            localRST_UserProfile.setSufferFromAllergies(i);
          }
        }
        else
        {
          localRST_UserProfile.setSufferFromAllergies(paramInt);
          continue;
          if (localRST_UserProfile.getStopsBreathing() == -1)
          {
            if ((paramInt == 0) || (paramInt == 2))
            {
              i = paramInt;
              if (paramInt == 2) {
                i = 1;
              }
              setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMySleepQ14b);
              localRST_UserProfile.setStopsBreathing(i);
            }
          }
          else
          {
            localRST_UserProfile.setStopsBreathing(paramInt);
            continue;
            if (localRST_UserProfile.isStayingAwakeDuringDay() == -1)
            {
              if ((paramInt == 0) || (paramInt == 2))
              {
                i = paramInt;
                if (paramInt == 2) {
                  i = 1;
                }
                setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMySleepQ14c);
                localRST_UserProfile.setStayingAwakeDuringDay(i);
              }
            }
            else
            {
              localRST_UserProfile.setStayingAwakeDuringDay(paramInt);
              continue;
              if (localRST_UserProfile.isDryMouth() == -1)
              {
                if ((paramInt == 0) || (paramInt == 2))
                {
                  i = paramInt;
                  if (paramInt == 2) {
                    i = 1;
                  }
                  setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMySleepQ14h);
                  localRST_UserProfile.setDryMouth(i);
                }
              }
              else
              {
                localRST_UserProfile.setDryMouth(paramInt);
                continue;
                if (localRST_UserProfile.isNasalCongestion() == -1)
                {
                  if ((paramInt == 0) || (paramInt == 2))
                  {
                    i = paramInt;
                    if (paramInt == 2) {
                      i = 1;
                    }
                    setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMySleepQ14i);
                    localRST_UserProfile.setNasalCongestion(i);
                  }
                }
                else
                {
                  localRST_UserProfile.setNasalCongestion(paramInt);
                  continue;
                  if (localRST_UserProfile.isBreathingObstruction() == -1)
                  {
                    if ((paramInt == 0) || (paramInt == 2))
                    {
                      i = paramInt;
                      if (paramInt == 2) {
                        i = 1;
                      }
                      setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMySleepQ14j);
                      localRST_UserProfile.setBreathingObstruction(i);
                    }
                  }
                  else
                  {
                    localRST_UserProfile.setBreathingObstruction(paramInt);
                    continue;
                    if (localRST_UserProfile.isCrawlingSensation() == -1)
                    {
                      if ((paramInt == 0) || (paramInt == 2))
                      {
                        i = paramInt;
                        if (paramInt == 2) {
                          i = 1;
                        }
                        setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMySleepQ14k);
                        localRST_UserProfile.setCrawlingSensation(i);
                      }
                    }
                    else
                    {
                      localRST_UserProfile.setCrawlingSensation(paramInt);
                      continue;
                      if (localRST_UserProfile.isMuscleTension() == -1)
                      {
                        if ((paramInt == 0) || (paramInt == 2))
                        {
                          i = paramInt;
                          if (paramInt == 2) {
                            i = 1;
                          }
                          setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMySleepQ14l);
                          localRST_UserProfile.setMuscleTension(i);
                        }
                      }
                      else
                      {
                        localRST_UserProfile.setMuscleTension(paramInt);
                        continue;
                        if (localRST_UserProfile.isLegsStillAtNight() == -1)
                        {
                          if ((paramInt == 0) || (paramInt == 2))
                          {
                            i = paramInt;
                            if (paramInt == 2) {
                              i = 1;
                            }
                            setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMySleepQ14m);
                            localRST_UserProfile.setLegsStillAtNight(i);
                          }
                        }
                        else
                        {
                          localRST_UserProfile.setLegsStillAtNight(paramInt);
                          continue;
                          if (localRST_UserProfile.getSleepMover() == -1)
                          {
                            if ((paramInt == 0) || (paramInt == 2))
                            {
                              i = paramInt;
                              if (paramInt == 2) {
                                i = 1;
                              }
                              setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMySleepQ14n);
                              localRST_UserProfile.setSleepMover(i);
                            }
                          }
                          else {
                            localRST_UserProfile.setSleepMover(paramInt);
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  
  public void onResult(RST_Response<RST_UserProfile> paramRST_Response)
  {
    if ((getBaseActivity() == null) || (getBaseActivity().isFinishing()) || (!getBaseActivity().isAppValidated(paramRST_Response.getErrorCode()))) {
      return;
    }
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    if (paramRST_Response.isStatus()) {}
    for (boolean bool = false;; bool = true)
    {
      localRefreshModelController.setProfileUpdatePending(bool);
      break;
    }
  }
  
  public void onStartTrackingTouch(SeekBar paramSeekBar) {}
  
  public void onStopTrackingTouch(SeekBar paramSeekBar) {}
  
  public void setMySleepUpdateProgressRef(IMySleepUpdateProgress paramIMySleepUpdateProgress)
  {
    this.updateMySleepProg = paramIMySleepUpdateProgress;
  }
  
  public static abstract interface IMySleepUpdateProgress
  {
    public abstract void screenTransitionFromMySleepFrag(int paramInt1, int paramInt2);
    
    public abstract void updateMySleepProgress();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\MySleepFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */