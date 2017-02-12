package com.resmed.refresh.expansion;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Messenger;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.android.vending.expansion.downloader.DownloadProgressInfo;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.google.android.vending.expansion.downloader.DownloaderServiceMarshaller;
import com.google.android.vending.expansion.downloader.Helpers;
import com.google.android.vending.expansion.downloader.IDownloaderClient;
import com.google.android.vending.expansion.downloader.IDownloaderService;
import com.google.android.vending.expansion.downloader.IStub;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.activity.SplashActivity;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.utils.Log;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.zip.CRC32;

public class SampleDownloaderActivity
  extends BaseActivity
  implements IDownloaderClient
{
  private static final float SMOOTHING_FACTOR = 0.005F;
  private static XAPKFile[] xAPKS;
  private TextView mAverageSpeed;
  private boolean mCancelValidation;
  private View mCellMessage;
  private View mDashboard;
  private IStub mDownloaderClientStub;
  private ProgressBar mPB;
  private Button mPauseButton;
  private TextView mProgressFraction;
  private TextView mProgressPercent;
  private IDownloaderService mRemoteService;
  private int mState;
  private boolean mStatePaused;
  private TextView mStatusText;
  private TextView mTimeRemaining;
  private Button mWiFiSettingsButton;
  private RelativeLayout relativeValidation;
  
  private boolean doesFileExist(Context paramContext, String paramString, long paramLong, boolean paramBoolean)
  {
    if ((new File(Helpers.generateSaveFileName(paramContext, paramString)).exists()) && (paramLong > 0L)) {}
    for (paramBoolean = true;; paramBoolean = false) {
      return paramBoolean;
    }
  }
  
  private void initializeDownloadUI()
  {
    this.mDownloaderClientStub = DownloaderClientMarshaller.CreateStub(this, SampleDownloaderService.class);
    setContentView(2130903070);
    changeRefreshBarVisibility(false, false);
    this.mPB = ((ProgressBar)findViewById(2131099781));
    this.mStatusText = ((TextView)findViewById(2131099777));
    this.mProgressFraction = ((TextView)findViewById(2131099779));
    this.mProgressPercent = ((TextView)findViewById(2131099780));
    this.mAverageSpeed = ((TextView)findViewById(2131099782));
    this.mTimeRemaining = ((TextView)findViewById(2131099783));
    this.mDashboard = findViewById(2131099778);
    this.mCellMessage = findViewById(2131099786);
    this.mPauseButton = ((Button)findViewById(2131099784));
    this.mWiFiSettingsButton = ((Button)findViewById(2131099791));
    this.relativeValidation = ((RelativeLayout)findViewById(2131099776));
    this.mPauseButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (SampleDownloaderActivity.this.mStatePaused)
        {
          SampleDownloaderActivity.this.mRemoteService.requestContinueDownload();
          paramAnonymousView = SampleDownloaderActivity.this;
          if (!SampleDownloaderActivity.this.mStatePaused) {
            break label60;
          }
        }
        label60:
        for (boolean bool = false;; bool = true)
        {
          paramAnonymousView.setButtonPausedState(bool);
          return;
          SampleDownloaderActivity.this.mRemoteService.requestPauseDownload();
          break;
        }
      }
    });
    this.mWiFiSettingsButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SampleDownloaderActivity.this.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
      }
    });
    ((Button)findViewById(2131099790)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SampleDownloaderActivity.this.mRemoteService.setDownloadFlags(1);
        SampleDownloaderActivity.this.mRemoteService.requestContinueDownload();
        SampleDownloaderActivity.this.mCellMessage.setVisibility(8);
      }
    });
  }
  
  private void setButtonPausedState(boolean paramBoolean)
  {
    this.mStatePaused = paramBoolean;
    if (paramBoolean) {}
    for (int i = 2131166131;; i = 2131166130)
    {
      this.mPauseButton.setText(i);
      return;
    }
  }
  
  private void setState(int paramInt)
  {
    if (this.mState != paramInt)
    {
      this.mState = paramInt;
      this.mStatusText.setText(Helpers.getDownloaderStringResourceIDFromState(paramInt));
    }
  }
  
  private void showFailed(String paramString)
  {
    this.mStatusText.setText(this.mStatusText.getText() + "\n" + getString(2131166129));
    this.mStatusText.setText(paramString);
    this.mDashboard.setVisibility(0);
    this.mPB.setVisibility(8);
    this.mProgressFraction.setVisibility(8);
    this.mProgressPercent.setVisibility(8);
    this.mPauseButton.setText(2131165296);
    this.mPauseButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SampleDownloaderActivity.this.startApp();
      }
    });
  }
  
  private void startApp()
  {
    startActivity(new Intent(this, SplashActivity.class));
    finish();
    overridePendingTransition(2130968586, 2130968586);
  }
  
  boolean expansionFilesDelivered()
  {
    boolean bool2 = false;
    XAPKFile[] arrayOfXAPKFile = xAPKS;
    int j = arrayOfXAPKFile.length;
    for (int i = 0;; i++)
    {
      boolean bool1;
      if (i >= j) {
        bool1 = true;
      }
      XAPKFile localXAPKFile;
      do
      {
        return bool1;
        localXAPKFile = arrayOfXAPKFile[i];
        bool1 = bool2;
      } while (!doesFileExist(this, Helpers.getExpansionAPKFileName(this, localXAPKFile.mIsMain, localXAPKFile.mFileVersion), localXAPKFile.mFileSize, false));
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!isTaskRoot()) {
      finish();
    }
    for (;;)
    {
      return;
      xAPKS = new XAPKFile[1];
      xAPKS[0] = new XAPKFile(true, 51, 6000L);
      if (!expansionFilesDelivered()) {}
      for (;;)
      {
        Object localObject;
        try
        {
          localObject = getIntent();
          paramBundle = new android/content/Intent;
          paramBundle.<init>(this, getClass());
          paramBundle.setFlags(335544320);
          paramBundle.setAction(((Intent)localObject).getAction());
          if (((Intent)localObject).getCategories() != null)
          {
            localObject = ((Intent)localObject).getCategories().iterator();
            if (((Iterator)localObject).hasNext()) {
              break label170;
            }
          }
          if (DownloaderClientMarshaller.startDownloadServiceIfRequired(this, PendingIntent.getActivity(this, 0, paramBundle, 134217728), SampleDownloaderService.class) != 0) {
            initializeDownloadUI();
          }
        }
        catch (PackageManager.NameNotFoundException paramBundle)
        {
          Log.e("com.resmed.refresh.expansion", "Cannot find own package! MAYDAY!");
          paramBundle.printStackTrace();
        }
        initializeDownloadUI();
        if (RefreshModelController.getInstance().getExpansionValidated()) {
          break label187;
        }
        validateXAPKZipFiles();
        break;
        label170:
        paramBundle.addCategory((String)((Iterator)localObject).next());
      }
      label187:
      startApp();
    }
  }
  
  protected void onDestroy()
  {
    this.mCancelValidation = true;
    super.onDestroy();
  }
  
  public void onDownloadProgress(DownloadProgressInfo paramDownloadProgressInfo)
  {
    this.mAverageSpeed.setText(getString(2131165210, new Object[] { Helpers.getSpeedString(paramDownloadProgressInfo.mCurrentSpeed) }));
    this.mTimeRemaining.setText(getString(2131165211, new Object[] { Helpers.getTimeRemaining(paramDownloadProgressInfo.mTimeRemaining) }));
    paramDownloadProgressInfo.mOverallTotal = paramDownloadProgressInfo.mOverallTotal;
    this.mPB.setMax((int)(paramDownloadProgressInfo.mOverallTotal >> 8));
    this.mPB.setProgress((int)(paramDownloadProgressInfo.mOverallProgress >> 8));
    this.mProgressPercent.setText(Long.toString(paramDownloadProgressInfo.mOverallProgress * 100L / paramDownloadProgressInfo.mOverallTotal) + "%");
    this.mProgressFraction.setText(Helpers.getDownloadProgressString(paramDownloadProgressInfo.mOverallProgress, paramDownloadProgressInfo.mOverallTotal));
  }
  
  public void onDownloadStateChanged(int paramInt)
  {
    setState(paramInt);
    int k = 1;
    int j = 0;
    int i = 0;
    boolean bool2;
    boolean bool1;
    switch (paramInt)
    {
    case 6: 
    case 10: 
    case 11: 
    case 13: 
    case 17: 
    default: 
      bool2 = true;
      bool1 = true;
      paramInt = 1;
      if (paramInt != 0)
      {
        paramInt = 0;
        label118:
        if (this.mDashboard.getVisibility() != paramInt) {
          this.mDashboard.setVisibility(paramInt);
        }
        if (j == 0) {
          break label333;
        }
      }
      break;
    }
    label333:
    for (paramInt = 0;; paramInt = 8)
    {
      if (this.mCellMessage.getVisibility() != paramInt) {
        this.mCellMessage.setVisibility(paramInt);
      }
      this.mPB.setIndeterminate(bool1);
      setButtonPausedState(bool2);
      if (i != 0) {
        showFailed(this.mStatusText.getText() + "\n" + getString(2131166129));
      }
      for (;;)
      {
        return;
        bool2 = false;
        bool1 = true;
        paramInt = k;
        break;
        paramInt = 1;
        bool2 = false;
        bool1 = true;
        break;
        bool2 = false;
        paramInt = 1;
        bool1 = false;
        break;
        bool2 = true;
        paramInt = 0;
        bool1 = false;
        i = 1;
        break;
        paramInt = 0;
        bool2 = true;
        bool1 = false;
        j = 1;
        break;
        bool2 = true;
        bool1 = false;
        paramInt = k;
        break;
        bool2 = true;
        bool1 = false;
        paramInt = k;
        break;
        if (!RefreshModelController.getInstance().getExpansionValidated()) {
          validateXAPKZipFiles();
        } else {
          startApp();
        }
      }
      paramInt = 8;
      break label118;
    }
  }
  
  protected void onResume()
  {
    if (this.mDownloaderClientStub != null) {
      this.mDownloaderClientStub.connect(this);
    }
    super.onResume();
  }
  
  public void onServiceConnected(Messenger paramMessenger)
  {
    this.mRemoteService = DownloaderServiceMarshaller.CreateProxy(paramMessenger);
    this.mRemoteService.onClientUpdated(this.mDownloaderClientStub.getMessenger());
  }
  
  protected void onStop()
  {
    if (this.mDownloaderClientStub != null) {
      this.mDownloaderClientStub.disconnect(this);
    }
    super.onStop();
  }
  
  void validateXAPKZipFiles()
  {
    new AsyncTask()new Object
    {
      protected Boolean doInBackground(Object... paramAnonymousVarArgs)
      {
        paramAnonymousVarArgs = SampleDownloaderActivity.xAPKS;
        int k = paramAnonymousVarArgs.length;
        int i = 0;
        for (;;)
        {
          if (i >= k) {}
          label23:
          Object localObject1;
          for (paramAnonymousVarArgs = Boolean.valueOf(true);; paramAnonymousVarArgs = Boolean.valueOf(false))
          {
            return paramAnonymousVarArgs;
            localObject2 = paramAnonymousVarArgs[i];
            localObject1 = Helpers.getExpansionAPKFileName(SampleDownloaderActivity.this, ((SampleDownloaderActivity.XAPKFile)localObject2).mIsMain, ((SampleDownloaderActivity.XAPKFile)localObject2).mFileVersion);
            if (SampleDownloaderActivity.this.doesFileExist(SampleDownloaderActivity.this, (String)localObject1, ((SampleDownloaderActivity.XAPKFile)localObject2).mFileSize, false)) {
              break;
            }
          }
          String str = Helpers.generateSaveFileName(SampleDownloaderActivity.this, (String)localObject1);
          Log.d("Expansion", "Check the content of the zip downloaded");
          Object localObject2 = new byte[262144];
          try
          {
            localObject1 = new com/android/vending/expansion/zipfile/ZipResourceFile;
            ((ZipResourceFile)localObject1).<init>(str);
            ZipResourceFile.ZipEntryRO[] arrayOfZipEntryRO = ((ZipResourceFile)localObject1).getAllEntries();
            long l1 = 0L;
            int m = arrayOfZipEntryRO.length;
            int j = 0;
            label135:
            float f1;
            long l2;
            if (j >= m)
            {
              f1 = 0.0F;
              l2 = l1;
              m = arrayOfZipEntryRO.length;
              j = 0;
            }
            for (;;)
            {
              if (j >= m)
              {
                i++;
                break;
                l1 += arrayOfZipEntryRO[j].mCompressedLength;
                j++;
                break label135;
              }
              localObject1 = arrayOfZipEntryRO[j];
              float f2 = f1;
              long l3 = l2;
              if (-1L != ((ZipResourceFile.ZipEntryRO)localObject1).mCRC32)
              {
                long l4 = ((ZipResourceFile.ZipEntryRO)localObject1).getOffset();
                l3 = ((ZipResourceFile.ZipEntryRO)localObject1).mCompressedLength;
                CRC32 localCRC32 = new java/util/zip/CRC32;
                localCRC32.<init>();
                RandomAccessFile localRandomAccessFile = new java/io/RandomAccessFile;
                localRandomAccessFile.<init>(str, "r");
                localRandomAccessFile.seek(l4);
                l4 = SystemClock.uptimeMillis();
                label263:
                if (l3 <= 0L)
                {
                  f2 = f1;
                  l3 = l2;
                  if (localCRC32.getValue() == ((ZipResourceFile.ZipEntryRO)localObject1).mCRC32) {
                    break label543;
                  }
                  paramAnonymousVarArgs = new java/lang/StringBuilder;
                  paramAnonymousVarArgs.<init>("CRC does not match for entry: ");
                  Log.e("LVLDL", ((ZipResourceFile.ZipEntryRO)localObject1).mFileName);
                  paramAnonymousVarArgs = new java/lang/StringBuilder;
                  paramAnonymousVarArgs.<init>("In file: ");
                  Log.e("LVLDL", ((ZipResourceFile.ZipEntryRO)localObject1).getZipFileName());
                  paramAnonymousVarArgs = Boolean.valueOf(false);
                  break label23;
                }
                long l5;
                label370:
                int n;
                long l6;
                if (l3 > localObject2.length)
                {
                  l5 = localObject2.length;
                  n = (int)l5;
                  localRandomAccessFile.readFully((byte[])localObject2, 0, n);
                  localCRC32.update((byte[])localObject2, 0, n);
                  l3 -= n;
                  l6 = SystemClock.uptimeMillis();
                  l4 = l6 - l4;
                  f2 = f1;
                  l5 = l2;
                  if (l4 > 0L)
                  {
                    f2 = n / (float)l4;
                    if (0.0F == f1) {
                      break label538;
                    }
                  }
                }
                label538:
                for (f1 = 0.005F * f2 + 0.995F * f1;; f1 = f2)
                {
                  l5 = l2 - n;
                  l2 = ((float)l5 / f1);
                  DownloadProgressInfo localDownloadProgressInfo = new com/google/android/vending/expansion/downloader/DownloadProgressInfo;
                  localDownloadProgressInfo.<init>(l1, l1 - l5, l2, f1);
                  publishProgress(new DownloadProgressInfo[] { localDownloadProgressInfo });
                  f2 = f1;
                  l4 = l6;
                  f1 = f2;
                  l2 = l5;
                  if (!SampleDownloaderActivity.this.mCancelValidation) {
                    break label263;
                  }
                  paramAnonymousVarArgs = Boolean.valueOf(true);
                  break;
                  l5 = l3;
                  break label370;
                }
              }
              label543:
              j++;
              f1 = f2;
              l2 = l3;
            }
          }
          catch (IOException paramAnonymousVarArgs)
          {
            paramAnonymousVarArgs.printStackTrace();
            paramAnonymousVarArgs = Boolean.valueOf(false);
          }
        }
      }
      
      protected void onPostExecute(Boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean.booleanValue())
        {
          SampleDownloaderActivity.this.mDashboard.setVisibility(0);
          SampleDownloaderActivity.this.mCellMessage.setVisibility(8);
          SampleDownloaderActivity.this.mStatusText.setText(2131166127);
          SampleDownloaderActivity.this.mPauseButton.setVisibility(8);
          SampleDownloaderActivity.this.mPauseButton.setText(17039370);
          RefreshModelController.getInstance().setExpansionValidated(true);
          SampleDownloaderActivity.this.startApp();
        }
        for (;;)
        {
          super.onPostExecute(paramAnonymousBoolean);
          return;
          SampleDownloaderActivity.this.relativeValidation.setVisibility(0);
          SampleDownloaderActivity.this.showFailed(SampleDownloaderActivity.this.getString(2131166128));
          SampleDownloaderActivity.this.mDashboard.setVisibility(0);
        }
      }
      
      protected void onPreExecute()
      {
        SampleDownloaderActivity.this.relativeValidation.setVisibility(4);
        SampleDownloaderActivity.this.mDashboard.setVisibility(0);
        SampleDownloaderActivity.this.mCellMessage.setVisibility(8);
        SampleDownloaderActivity.this.mStatusText.setText(2131166126);
        SampleDownloaderActivity.this.mPauseButton.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymous2View)
          {
            SampleDownloaderActivity.this.mCancelValidation = true;
          }
        });
        SampleDownloaderActivity.this.mPauseButton.setText(2131166133);
        super.onPreExecute();
      }
      
      protected void onProgressUpdate(DownloadProgressInfo... paramAnonymousVarArgs)
      {
        SampleDownloaderActivity.this.onDownloadProgress(paramAnonymousVarArgs[0]);
        super.onProgressUpdate(paramAnonymousVarArgs);
      }
    }.execute(new Object[] { new Object() });
  }
  
  private static class XAPKFile
  {
    public final long mFileSize;
    public final int mFileVersion;
    public final boolean mIsMain;
    
    XAPKFile(boolean paramBoolean, int paramInt, long paramLong)
    {
      this.mIsMain = paramBoolean;
      this.mFileVersion = paramInt;
      this.mFileSize = paramLong;
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\expansion\SampleDownloaderActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */