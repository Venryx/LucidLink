package com.resmed.refresh.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.webkit.WebView;
import com.resmed.refresh.ui.fragments.GuideFragment;
import com.resmed.refresh.ui.uibase.base.BaseActivity;

public class GuideActivty
  extends BaseActivity
{
  public static WebView displayYoutubeVideo;
  private boolean close = false;
  
  private void stopVideoPlayingOnGuidePage()
  {
    if (displayYoutubeVideo != null)
    {
      displayYoutubeVideo.loadUrl("file:///android_asset/nonexistent.html");
      displayYoutubeVideo = null;
    }
  }
  
  public void onBackPressed()
  {
    super.onBackPressed();
    stopVideoPlayingOnGuidePage();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    boolean bool1 = false;
    super.onCreate(paramBundle);
    setContentView(2130903072);
    boolean bool2 = getIntent().getBooleanExtra("com.resmed.refresh.ui.uibase.app.came_from_settings", false);
    boolean bool3 = getIntent().getBooleanExtra("com.resmed.refresh.ui.uibase.app.came_from_landing", false);
    if ((!bool2) && (!bool3)) {}
    for (;;)
    {
      this.close = bool1;
      if (!this.close) {
        setTypeRefreshBar(BaseActivity.TypeBar.NO_BED);
      }
      paramBundle = new GuideFragment();
      FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
      localFragmentTransaction.add(2131099793, paramBundle);
      localFragmentTransaction.commit();
      setTypeRefreshBar(BaseActivity.TypeBar.DEFAULT);
      setTitle(2131165386);
      return;
      bool1 = true;
    }
  }
  
  protected void rightBtnPressed()
  {
    if (this.close) {
      finish();
    }
    for (;;)
    {
      stopVideoPlayingOnGuidePage();
      return;
      Intent localIntent2 = new Intent(this, HomeActivity.class);
      localIntent2.setFlags(268533760);
      Intent localIntent1 = new Intent(this, SensorSetupActivity.class);
      localIntent1.putExtra("com.resmed.refresh.ui.uibase.app.activity_modal", true);
      localIntent1.putExtra("com.resmed.refresh.ui.uibase.app.came_from_home", true);
      startActivity(localIntent2);
      startActivity(localIntent1);
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\GuideActivty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */