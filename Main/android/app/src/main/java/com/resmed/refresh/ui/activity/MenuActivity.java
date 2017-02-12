package com.resmed.refresh.ui.activity;

import android.os.Bundle;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.uibase.base.BaseActivity;

public class MenuActivity
  extends BaseActivity
  implements RST_CallbackItem<RST_Response<RST_UserProfile>>
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903080);
    setTitle(2131166023);
    setIsModalActivity(true);
    setTypeRefreshBar(BaseActivity.TypeBar.NO_BED);
  }
  
  public void onResult(RST_Response<RST_UserProfile> paramRST_Response)
  {
    boolean bool = false;
    try
    {
      if (!isAppValidated(paramRST_Response.getErrorCode())) {}
      for (;;)
      {
        return;
        if ((paramRST_Response == null) || (!paramRST_Response.isStatus())) {
          break;
        }
        i = 1;
        paramRST_Response = RefreshModelController.getInstance();
        if (i == 0) {
          break label56;
        }
        paramRST_Response.setProfileUpdatePending(bool);
      }
    }
    catch (Exception paramRST_Response)
    {
      for (;;)
      {
        paramRST_Response.printStackTrace();
        continue;
        int i = 0;
        continue;
        label56:
        bool = true;
      }
    }
  }
  
  protected void onStop()
  {
    super.onStop();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\MenuActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */