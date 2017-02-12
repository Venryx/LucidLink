package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.Last30DaysReportResponse;
import com.resmed.refresh.ui.activity.GuideActivty;
import com.resmed.refresh.ui.activity.HomeActivity;
import com.resmed.refresh.ui.activity.RelaxMeditationActivity;
import com.resmed.refresh.ui.activity.SettingsActivity;
import com.resmed.refresh.ui.activity.SetupProfileActivity;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;
import com.resmed.refresh.utils.Log;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuFragment
  extends BaseFragment
  implements View.OnClickListener, RST_CallbackItem<RST_Response<Last30DaysReportResponse>>
{
  private String convertInDateFormat(long paramLong)
  {
    return new SimpleDateFormat("yyyy-MM-dd").format(new Date(paramLong));
  }
  
  private void requestToSendLast30DaysReport()
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    long l = System.currentTimeMillis();
    String str2 = convertInDateFormat(l);
    String str1 = convertInDateFormat(l - Long.parseLong("2592000000"));
    Log.d("LOG_TAG", "Timediff  = " + str2 + "   30daysdate=> " + str1);
    if (!RefreshApplication.getInstance().isConnectedToInternet()) {
      getBaseActivity().showDialog(new CustomDialogBuilder(getBaseActivity()).title("Error").description(getResources().getString(2131165350)).setPositiveButton(2131165296, null));
    }
    for (;;)
    {
      return;
      localRefreshModelController.sendLast30DayReport(str1, str2, true, this);
      getBaseActivity().showDialog(new CustomDialogBuilder(getBaseActivity()).title("Success").description(getResources().getString(2131166136)).setPositiveButton(2131165296, null));
    }
  }
  
  private void setupGUI(View paramView)
  {
    paramView.findViewById(2131100036).setOnClickListener(this);
    paramView.findViewById(2131100037).setOnClickListener(this);
    paramView.findViewById(2131100038).setOnClickListener(this);
    paramView.findViewById(2131100040).setOnClickListener(this);
    paramView.findViewById(2131100039).setOnClickListener(this);
    paramView.findViewById(2131100041).setOnClickListener(this);
    paramView.findViewById(2131100042).setOnClickListener(this);
    paramView.findViewById(2131100043).setOnClickListener(this);
  }
  
  public void onClick(View paramView)
  {
    Object localObject = null;
    switch (paramView.getId())
    {
    default: 
      paramView = (View)localObject;
    }
    for (;;)
    {
      if (paramView != null) {
        startActivity(paramView);
      }
      return;
      RefreshModelController.getInstance().updateFlurryEvents("Menu_Splusguide");
      paramView = new Intent(getActivity(), GuideActivty.class);
      paramView.putExtra("com.resmed.refresh.ui.uibase.app.came_from_settings", true);
      continue;
      RefreshModelController.getInstance().updateFlurryEvents("Menu_FAQ");
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse(com.resmed.refresh.ui.utils.Consts.REFRESH_URL[3])));
      paramView = (View)localObject;
      continue;
      RefreshModelController.getInstance().updateFlurryEvents("Menu_RelaxDayTime");
      paramView = new Intent(getActivity(), RelaxMeditationActivity.class);
      continue;
      RefreshModelController.getInstance().updateFlurryEvents("Menu_Profile");
      paramView = new Intent(getActivity(), SetupProfileActivity.class);
      paramView.putExtra("com.resmed.refresh.ui.uibase.app.came_from_settings", true);
      continue;
      RefreshModelController.getInstance().updateFlurryEvents("Menu_Settings");
      paramView = new Intent(getActivity(), SettingsActivity.class);
      continue;
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse(com.resmed.refresh.ui.utils.Consts.REFRESH_URL[2])));
      paramView = (View)localObject;
      continue;
      getBaseActivity().showDialog(new CustomDialogBuilder(getBaseActivity()).title(2131166041).setNegativeButton(2131165297, null).setPositiveButton(2131165296, new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          RefreshModelController.getInstance().userLogout();
          paramAnonymousView = new Intent(MenuFragment.this.getActivity(), HomeActivity.class);
          paramAnonymousView.setFlags(268468224);
          MenuFragment.this.startActivity(paramAnonymousView);
          MenuFragment.this.getActivity().overridePendingTransition(2130968582, 2130968583);
        }
      }));
      paramView = (View)localObject;
      continue;
      requestToSendLast30DaysReport();
      paramView = (View)localObject;
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903148, paramViewGroup, false);
    setupGUI(paramLayoutInflater);
    return paramLayoutInflater;
  }
  
  public void onResult(RST_Response<Last30DaysReportResponse> paramRST_Response) {}
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\MenuFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */