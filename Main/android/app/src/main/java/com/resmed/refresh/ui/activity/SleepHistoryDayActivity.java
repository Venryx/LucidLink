package com.resmed.refresh.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.resmed.refresh.ui.fragments.ShareDialogFragment;
import com.resmed.refresh.ui.fragments.SleepHistoryDayFragment;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.InternalFileProvider;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.refresh.utils.TwitterManager;
import com.resmed.refresh.utils.TwitterManager.StatusResponse;
import com.resmed.refresh.utils.TwitterManager.TwitterCallback;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import twitter4j.TwitterException;

public class SleepHistoryDayActivity
  extends BaseActivity
{
  private static final String CONSUMER_KEY = "AdyFKtbprKMJBaq1kGGemxo54";
  private static final String FB_TAG = "facebook";
  private static final String PERMISSION = "publish_actions";
  private static final String SECRET_KEY = "7k1bqJAwDD0oe8xRKWNozxI1EqNgZ4xeCFNimQZ5XGP6ta9mB0";
  private static int heigthScreen;
  private static int screehShotHideHeigth = 0;
  private Bitmap bmToShare;
  private Session.StatusCallback callbackFacebook = new Session.StatusCallback()
  {
    public void call(Session paramAnonymousSession, SessionState paramAnonymousSessionState, Exception paramAnonymousException)
    {
      SleepHistoryDayActivity.this.onSessionStateChange(paramAnonymousSession, paramAnonymousSessionState, paramAnonymousException);
    }
  };
  private File fileImageToShare;
  private boolean pendingPublishFB;
  private boolean receivedCallback = false;
  private SleepHistoryDayFragment sleepHistoryFragment;
  private String textToShare;
  private int tries;
  private View twitterFrame;
  private UiLifecycleHelper uiHelper;
  
  private void confirmPublish(ShareDialogFragment.Type paramType, ShareDialogFragment.ShareDialogReceiver paramShareDialogReceiver)
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    Fragment localFragment = getFragmentManager().findFragmentByTag("sharedialog");
    if (localFragment != null)
    {
      ((DialogFragment)localFragment).dismiss();
      getSupportFragmentManager().executePendingTransactions();
    }
    localFragmentTransaction.addToBackStack(null);
    if (this.bmToShare == null) {
      this.bmToShare = createPhoto();
    }
    paramType = new ShareDialogFragment(paramType, paramShareDialogReceiver);
    paramShareDialogReceiver = new Bundle();
    paramShareDialogReceiver.putString("SHARE_TEXT_TAG", getTextToShare());
    paramShareDialogReceiver.putParcelable("SHARE_IMG_TAG", this.bmToShare);
    paramType.setArguments(paramShareDialogReceiver);
    paramType.show(localFragmentTransaction, "sharedialog");
    showRightButton(2130837785);
    Log.d("com.resmed.refresh.dialog", "BaseActivity dialogFragment.show");
  }
  
  private void confirmPublishFB()
  {
    if (this.bmToShare == null) {
      this.bmToShare = createPhoto();
    }
    confirmPublish(ShareDialogFragment.Type.FACEBOOK, new ShareDialogFragment.ShareDialogReceiver()
    {
      public void share(String paramAnonymousString)
      {
        SleepHistoryDayActivity.this.textToShare = paramAnonymousString;
        SleepHistoryDayActivity.this.performPublish();
      }
    });
  }
  
  private void confirmPublishTwitter(final TwitterManager paramTwitterManager, final File paramFile)
  {
    confirmPublish(ShareDialogFragment.Type.TWITTER, new ShareDialogFragment.ShareDialogReceiver()
    {
      public void share(String paramAnonymousString)
      {
        SleepHistoryDayActivity.this.sendTwit(paramTwitterManager, paramAnonymousString, paramFile);
      }
    });
  }
  
  private Bitmap createPhoto()
  {
    Bitmap localBitmap2 = null;
    Bitmap localBitmap1 = localBitmap2;
    try
    {
      ScrollView localScrollView = (ScrollView)this.sleepHistoryFragment.getView().findViewWithTag(this.sleepHistoryFragment.getCurrentViewTag()).findViewById(2131100620);
      localBitmap1 = localBitmap2;
      localScrollView.setVerticalScrollBarEnabled(false);
      localBitmap1 = localBitmap2;
      localScrollView.scrollTo(0, 0);
      localBitmap1 = localBitmap2;
      View localView = findViewById(2131099763);
      localBitmap1 = localBitmap2;
      int j = localView.getMeasuredWidth();
      localBitmap1 = localBitmap2;
      int i = localView.getMeasuredHeight();
      localBitmap1 = localBitmap2;
      Bitmap localBitmap3 = Bitmap.createBitmap(localView.getWidth(), localView.getHeight() + screehShotHideHeigth, Bitmap.Config.RGB_565);
      localBitmap1 = localBitmap2;
      localView.measure(View.MeasureSpec.makeMeasureSpec(localBitmap3.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(localBitmap3.getHeight(), 1073741824));
      localBitmap1 = localBitmap2;
      localView.layout(0, 0, localView.getMeasuredWidth(), localView.getMeasuredHeight() + screehShotHideHeigth);
      localBitmap1 = localBitmap2;
      Canvas localCanvas = new android/graphics/Canvas;
      localBitmap1 = localBitmap2;
      localCanvas.<init>(localBitmap3);
      localBitmap1 = localBitmap2;
      localView.draw(localCanvas);
      localBitmap1 = localBitmap2;
      int k = Math.round(getResources().getDimension(2131034198));
      localBitmap1 = localBitmap2;
      localBitmap2 = Bitmap.createBitmap(localBitmap3, 0, k, localBitmap3.getWidth(), localBitmap3.getHeight() - k);
      localBitmap1 = localBitmap2;
      localView.measure(View.MeasureSpec.makeMeasureSpec(j, 1073741824), View.MeasureSpec.makeMeasureSpec(i, 1073741824));
      localBitmap1 = localBitmap2;
      localView.layout(0, 0, j, i);
      localBitmap1 = localBitmap2;
      localScrollView.setVerticalScrollBarEnabled(true);
      localBitmap1 = localBitmap2;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        localException.printStackTrace();
      }
    }
    return localBitmap1;
  }
  
  private String getTextToShare()
  {
    return "Have a look at my Sleep Score from last night. I scored " + this.sleepHistoryFragment.getCurrentScore() + "!\n" + "S+ the serious science of sleep.\n" + "www.mysplus.com";
  }
  
  private boolean hasPublishPermission()
  {
    List localList = Session.getActiveSession().getPermissions();
    Iterator localIterator = localList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return isSubsetOf(Arrays.asList(new String[] { "publish_actions" }), localList);
      }
      String str = (String)localIterator.next();
      Log.d("facebook", "permission:" + str);
    }
  }
  
  private boolean isSubsetOf(Collection<String> paramCollection1, Collection<String> paramCollection2)
  {
    paramCollection1 = paramCollection1.iterator();
    if (!paramCollection1.hasNext()) {}
    for (boolean bool = true;; bool = false)
    {
      return bool;
      if (paramCollection2.contains((String)paramCollection1.next())) {
        break;
      }
    }
  }
  
  private void onClickLogin()
  {
    Session localSession = Session.getActiveSession();
    if ((!localSession.isOpened()) && (!localSession.isClosed()))
    {
      localSession.openForRead(new Session.OpenRequest(this).setPermissions(Arrays.asList(new String[] { "public_profile" })).setCallback(this.callbackFacebook));
      Log.d("facebook", "session.openForRead");
    }
    for (;;)
    {
      return;
      Session.openActiveSession(this, true, this.callbackFacebook);
      Log.d("facebook", "Session.openActiveSession(this, true, callback);");
    }
  }
  
  private void onSessionStateChange(Session paramSession, SessionState paramSessionState, Exception paramException)
  {
    Log.d("facebook", "onSessionStateChange state:" + paramSessionState);
    if (((paramException instanceof FacebookOperationCanceledException)) || ((paramException instanceof FacebookAuthorizationException)))
    {
      Toast.makeText(getApplicationContext(), paramException.getMessage(), 1).show();
      Toast.makeText(getApplicationContext(), 2131166010, 1).show();
    }
    for (;;)
    {
      return;
      Log.d("facebook", "onSessionStateChange else pendingPublish=" + this.pendingPublishFB);
      if ((this.pendingPublishFB) && (hasPublishPermission())) {
        confirmPublishFB();
      }
    }
  }
  
  private void performPublish()
  {
    Log.d("facebook", "performPublish");
    Session localSession = Session.getActiveSession();
    if (localSession != null)
    {
      if (!hasPublishPermission()) {
        break label38;
      }
      Log.d("facebook", "hasPublishPermission We can do the action right away");
      postPhoto();
    }
    for (;;)
    {
      return;
      label38:
      if (localSession.isOpened())
      {
        Log.d("facebook", "We need to get new permissions, then complete the action when we get called back");
        Session.NewPermissionsRequest localNewPermissionsRequest = new Session.NewPermissionsRequest(this, new String[] { "publish_actions" });
        localNewPermissionsRequest.setCallback(new Session.StatusCallback()
        {
          public void call(Session paramAnonymousSession, SessionState paramAnonymousSessionState, Exception paramAnonymousException)
          {
            Log.d("facebook", "Callback get permission");
            if (SleepHistoryDayActivity.this.hasPublishPermission()) {
              SleepHistoryDayActivity.this.postPhoto();
            }
          }
        });
        localSession.requestNewPublishPermissions(localNewPermissionsRequest);
      }
    }
  }
  
  private void postPhoto()
  {
    Log.d("facebook", "postPhoto");
    if (this.bmToShare == null) {
      this.bmToShare = createPhoto();
    }
    if (this.bmToShare == null)
    {
      Toast.makeText(getApplicationContext(), 2131165303, 1).show();
      showPublishResult(null, new FacebookRequestError(0, "", getString(2131166012)));
    }
    for (;;)
    {
      return;
      Request.newMyUploadPhotoRequest(Session.getActiveSession(), this.bmToShare, this.textToShare, "test", new Request.Callback()
      {
        public void onCompleted(Response paramAnonymousResponse)
        {
          SleepHistoryDayActivity.this.showPublishResult(paramAnonymousResponse.getGraphObject(), paramAnonymousResponse.getError());
        }
      }).executeAsync();
      Toast.makeText(getApplicationContext(), 2131166009, 0).show();
    }
  }
  
  private void sendTwit(final TwitterManager paramTwitterManager, final String paramString, final File paramFile)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        paramTwitterManager.publishPicture(paramString, paramFile);
      }
    }).start();
    runOnUiThread(new Runnable()
    {
      public void run()
      {
        Toast.makeText(SleepHistoryDayActivity.this.getApplicationContext(), 2131166011, 0).show();
      }
    });
  }
  
  private void sendWithTwitter4j(final String paramString, final File paramFile)
    throws TwitterException
  {
    hideRightButton();
    paramString = new TwitterManager(this, (WebView)this.twitterFrame.findViewById(2131099835), "AdyFKtbprKMJBaq1kGGemxo54", "7k1bqJAwDD0oe8xRKWNozxI1EqNgZ4xeCFNimQZ5XGP6ta9mB0");
    this.receivedCallback = false;
    TwitterManager.TwitterCallback local3 = new TwitterManager.TwitterCallback()
    {
      public void onFinishLogin(final TwitterManager.StatusResponse paramAnonymousStatusResponse)
      {
        SleepHistoryDayActivity.this.runOnUiThread(new Runnable()
        {
          public void run()
          {
            SleepHistoryDayActivity.this.showRightButton(2130837785);
          }
        });
        if (paramAnonymousStatusResponse == TwitterManager.StatusResponse.OK) {
          if (!SleepHistoryDayActivity.this.receivedCallback) {}
        }
        for (;;)
        {
          return;
          SleepHistoryDayActivity.this.receivedCallback = true;
          SleepHistoryDayActivity.this.confirmPublishTwitter(paramString, paramFile);
          continue;
          SleepHistoryDayActivity.this.runOnUiThread(new Runnable()
          {
            public void run()
            {
              SleepHistoryDayActivity.this.twitterFrame.setVisibility(8);
              if (paramAnonymousStatusResponse == TwitterManager.StatusResponse.ERROR_LOGIN) {
                Toast.makeText(SleepHistoryDayActivity.this.getApplicationContext(), 2131166007, 1).show();
              }
              for (;;)
              {
                return;
                Toast.makeText(SleepHistoryDayActivity.this.getApplicationContext(), 2131166008, 1).show();
              }
            }
          });
        }
      }
      
      public void onFinishingLogin()
      {
        SleepHistoryDayActivity.this.runOnUiThread(new Runnable()
        {
          public void run()
          {
            SleepHistoryDayActivity.this.twitterFrame.setVisibility(8);
            SleepHistoryDayActivity.this.showRightButton(2130837785);
          }
        });
      }
    };
    if (paramString.isConnected()) {
      confirmPublishTwitter(paramString, paramFile);
    }
    for (;;)
    {
      return;
      this.twitterFrame.setVisibility(0);
      paramString.connect(local3);
    }
  }
  
  public static void setEndPositionForScreenShot(int paramInt)
  {
    Log.d("facebook", "setEndPositionForScreenShot(" + (paramInt - heigthScreen) + ")");
    screehShotHideHeigth = paramInt - heigthScreen;
  }
  
  private void shareFacebook()
  {
    this.pendingPublishFB = true;
    this.tries = 0;
    Log.d("facebook", "onClickPostPhoto");
    if ((Session.getActiveSession() != null) && (Session.getActiveSession().isOpened()))
    {
      Log.d("facebook", "shareFacebook()");
      confirmPublishFB();
    }
    for (;;)
    {
      return;
      Log.d("facebook", "onClickLogin");
      onClickLogin();
    }
  }
  
  private void shareTwitter()
  {
    for (;;)
    {
      try
      {
        this.bmToShare = createPhoto();
        str = getTextToShare();
        localIntent = new android/content/Intent;
        localIntent.<init>("android.intent.action.SEND");
        localIntent.setType("image/*");
        localIntent.putExtra("android.intent.extra.TEXT", str);
        localObject2 = Calendar.getInstance();
        localObject1 = new java/text/SimpleDateFormat;
        ((SimpleDateFormat)localObject1).<init>("yyyyMMddHHmmss");
        localObject1 = ((SimpleDateFormat)localObject1).format(((Calendar)localObject2).getTime());
        localObject3 = new java/io/File;
        File localFile = RefreshTools.getFilesPath();
        localObject2 = new java.lang.StringBuilder;
        ((StringBuilder)localObject2).<init>("sleep_score_");
        ((File)localObject3).<init>(localFile, (String)localObject1 + ".jpg");
        this.fileImageToShare = ((File)localObject3);
        localObject1 = new java/io/FileOutputStream;
        ((FileOutputStream)localObject1).<init>(this.fileImageToShare);
        this.bmToShare.compress(Bitmap.CompressFormat.JPEG, 100, (OutputStream)localObject1);
        ((FileOutputStream)localObject1).flush();
        ((FileOutputStream)localObject1).close();
        if ((!this.fileImageToShare.exists()) || (!this.fileImageToShare.canRead()))
        {
          Log.d("com.resmed.refresh.filelog", "Failed to attach file as does not exist");
          return;
        }
      }
      catch (Exception localException)
      {
        String str;
        Intent localIntent;
        Object localObject1;
        int j;
        localException.printStackTrace();
        continue;
        localResolveInfo = (ResolveInfo)((List)localObject3).get(i);
        Object localObject2 = localResolveInfo.activityInfo.packageName;
        if (!((String)localObject2).contains("com.twitter.android")) {
          continue;
        }
        Object localObject3 = new android/content/ComponentName;
        ((ComponentName)localObject3).<init>((String)localObject2, localResolveInfo.activityInfo.name);
        localIntent.setComponent((ComponentName)localObject3);
        i = 1;
        continue;
        i++;
        continue;
        continue;
      }
      localObject1 = new java.lang.StringBuilder;
      ((StringBuilder)localObject1).<init>();
      localObject1 = Uri.parse(InternalFileProvider.CONTENT_URI + this.fileImageToShare.getName());
      if (Consts.USE_EXTERNAL_STORAGE)
      {
        localObject1 = new java.lang.StringBuilder;
        ((StringBuilder)localObject1).<init>("file://");
        localObject1 = Uri.parse(this.fileImageToShare);
      }
      localObject2 = new java.lang.StringBuilder;
      ((StringBuilder)localObject2).<init>("ParcelFileDescriptor uri:");
      Log.d("com.resmed.refresh", ((Uri)localObject1).toString());
      localIntent.putExtra("android.intent.extra.STREAM", (Parcelable)localObject1);
      j = 0;
      try
      {
        localObject3 = getPackageManager().queryIntentActivities(localIntent, 0);
        i = 0;
        int k = ((List)localObject3).size();
        if (i < k) {
          continue;
        }
        i = j;
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        ResolveInfo localResolveInfo;
        localActivityNotFoundException.printStackTrace();
        int i = j;
        continue;
        sendWithTwitter4j(str, this.fileImageToShare);
      }
      if (i == 0) {
        continue;
      }
      startActivity(localIntent);
    }
  }
  
  private void showPublishResult(GraphObject paramGraphObject, FacebookRequestError paramFacebookRequestError)
  {
    if ((paramFacebookRequestError != null) && (this.tries < 5) && (hasPublishPermission()) && (this != null) && (this.isAvailable))
    {
      this.tries += 1;
      this.pendingPublishFB = true;
      performPublish();
      return;
    }
    if (paramFacebookRequestError == null)
    {
      paramGraphObject = getString(2131166011);
      this.tries = 0;
    }
    for (;;)
    {
      Log.d("facebook", "showPublishResult:" + paramGraphObject);
      Toast.makeText(getApplicationContext(), paramGraphObject, 1).show();
      break;
      paramGraphObject = paramFacebookRequestError.getErrorMessage();
    }
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    this.uiHelper.onActivityResult(paramInt1, paramInt2, paramIntent);
    Log.d("facebook", "onActivityResult(" + paramInt1 + "," + paramInt2 + "," + paramIntent.getDataString() + ");");
  }
  
  public void onBackPressed()
  {
    if (this.twitterFrame.getVisibility() == 0)
    {
      this.twitterFrame.setVisibility(8);
      showRightButton(2130837785);
    }
    for (;;)
    {
      return;
      super.onBackPressed();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903092);
    setTypeRefreshBar(BaseActivity.TypeBar.NO_BED);
    setTitle(2131165995);
    showRightButton(2130837785);
    this.sleepHistoryFragment = new SleepHistoryDayFragment();
    Object localObject = getSupportFragmentManager().beginTransaction();
    ((FragmentTransaction)localObject).replace(2131099833, this.sleepHistoryFragment);
    ((FragmentTransaction)localObject).commit();
    localObject = getWindowManager().getDefaultDisplay();
    Point localPoint = new Point();
    ((Display)localObject).getSize(localPoint);
    heigthScreen = localPoint.y;
    this.twitterFrame = findViewById(2131099834);
    this.pendingPublishFB = false;
    this.uiHelper = new UiLifecycleHelper(this, this.callbackFacebook);
    this.uiHelper.onCreate(paramBundle);
    if (Session.getActiveSession().isOpened()) {
      Session.getActiveSession().closeAndClearTokenInformation();
    }
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    this.uiHelper.onDestroy();
  }
  
  public void onPause()
  {
    super.onPause();
    this.uiHelper.onPause();
  }
  
  protected void onResume()
  {
    super.onResume();
    this.uiHelper.onResume();
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.uiHelper.onSaveInstanceState(paramBundle);
  }
  
  protected void rightBtnPressed()
  {
    if (RefreshApplication.getInstance().isConnectedToInternet())
    {
      this.textToShare = getTextToShare();
      new AlertDialog.Builder(this).setTitle(2131166003).setAdapter(new ShareAdapter(null), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          SleepHistoryDayActivity.this.bmToShare = null;
          if (paramAnonymousInt == 0) {
            SleepHistoryDayActivity.this.shareFacebook();
          }
          for (;;)
          {
            return;
            SleepHistoryDayActivity.this.shareTwitter();
          }
        }
      }).create().show();
    }
    for (;;)
    {
      return;
      showErrorDialog(getString(2131165350));
    }
  }
  
  private class ShareAdapter
    extends BaseAdapter
  {
    private ShareAdapter() {}
    
    public int getCount()
    {
      return 2;
    }
    
    public Object getItem(int paramInt)
    {
      return new Integer(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    @SuppressLint({"InflateParams"})
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      paramView = paramViewGroup;
      if (paramViewGroup == null) {
        paramView = SleepHistoryDayActivity.this.getLayoutInflater().inflate(2130903199, null);
      }
      paramViewGroup = (TextView)paramView.findViewById(2131100667);
      ImageView localImageView = (ImageView)paramView.findViewById(2131100666);
      if (paramInt == 0)
      {
        paramViewGroup.setText(2131166006);
        localImageView.setImageResource(2130837748);
      }
      for (;;)
      {
        return paramView;
        paramViewGroup.setText(2131166005);
        localImageView.setImageResource(2130837948);
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\SleepHistoryDayActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */