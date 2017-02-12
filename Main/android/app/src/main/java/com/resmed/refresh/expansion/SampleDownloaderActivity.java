package com.resmed.refresh.expansion;

/*import com.resmed.refresh.ui.uibase.base.*;
import android.widget.*;
import java.io.*;
import android.view.*;
import com.resmed.refresh.ui.activity.*;
import android.content.*;
import android.app.*;
import com.resmed.refresh.utils.*;
import android.content.pm.*;
import com.resmed.refresh.model.*;
import java.util.*;
import com.google.android.vending.expansion.downloader.*;
import android.os.*;

public class SampleDownloaderActivity extends BaseActivity implements IDownloaderClient
{
	private static class XAPKFile
	{
		public final long mFileSize;
		public final int mFileVersion;
		public final boolean mIsMain;

		XAPKFile(final boolean mIsMain, final int mFileVersion, final long mFileSize) {
			this.mIsMain = mIsMain;
			this.mFileVersion = mFileVersion;
			this.mFileSize = mFileSize;
		}
	}


	private static final float SMOOTHING_FACTOR = 0.005f;
	private static SampleDownloaderActivity.XAPKFile[] xAPKS;
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

	private boolean doesFileExist(final Context context, final String s, final long n, final boolean b) {
		return new File(Helpers.generateSaveFileName(context, s)).exists() && n > 0L;
	}

	private void initializeDownloadUI() {
		this.mDownloaderClientStub = DownloaderClientMarshaller.CreateStub((IDownloaderClient)this, (Class)SampleDownloaderService.class);
		this.setContentView(2130903070);
		this.changeRefreshBarVisibility(false, false);
		this.mPB = (ProgressBar)this.findViewById(2131099781);
		this.mStatusText = (TextView)this.findViewById(2131099777);
		this.mProgressFraction = (TextView)this.findViewById(2131099779);
		this.mProgressPercent = (TextView)this.findViewById(2131099780);
		this.mAverageSpeed = (TextView)this.findViewById(2131099782);
		this.mTimeRemaining = (TextView)this.findViewById(2131099783);
		this.mDashboard = this.findViewById(2131099778);
		this.mCellMessage = this.findViewById(2131099786);
		this.mPauseButton = (Button)this.findViewById(2131099784);
		this.mWiFiSettingsButton = (Button)this.findViewById(2131099791);
		this.relativeValidation = (RelativeLayout)this.findViewById(2131099776);
		this.mPauseButton.setOnClickListener((View.OnClickListener)new SampleDownloaderActivity());
		this.mWiFiSettingsButton.setOnClickListener((View.OnClickListener)new SampleDownloaderActivity());
		((Button)this.findViewById(2131099790)).setOnClickListener((View.OnClickListener)new SampleDownloaderActivity());
	}

	private void setButtonPausedState(final boolean mStatePaused) {
		this.mStatePaused = mStatePaused;
		int text;
		if (mStatePaused) {
			text = 2131166131;
		}
		else {
			text = 2131166130;
		}
		this.mPauseButton.setText(text);
	}

	private void setState(final int mState) {
		if (this.mState != mState) {
			this.mState = mState;
			this.mStatusText.setText(Helpers.getDownloaderStringResourceIDFromState(mState));
		}
	}

	private void showFailed(final String text) {
		this.mStatusText.setText((CharSequence)((Object)this.mStatusText.getText() + "\n" + this.getString(2131166129)));
		this.mStatusText.setText((CharSequence)text);
		this.mDashboard.setVisibility(0);
		this.mPB.setVisibility(8);
		this.mProgressFraction.setVisibility(8);
		this.mProgressPercent.setVisibility(8);
		this.mPauseButton.setText(2131165296);
		this.mPauseButton.setOnClickListener((View.OnClickListener)new SampleDownloaderActivity());
	}

	private void startApp() {
		this.startActivity(new Intent((Context)this, (Class)SplashActivity.class));
		this.finish();
		this.overridePendingTransition(2130968586, 2130968586);
	}

	boolean expansionFilesDelivered() {
		for (final SampleDownloaderActivity.XAPKFile xapkFile : SampleDownloaderActivity.xAPKS) {
			final boolean doesFileExist = this.doesFileExist((Context)this, Helpers.getExpansionAPKFileName((Context)this, xapkFile.mIsMain, xapkFile.mFileVersion), xapkFile.mFileSize, false);
			final boolean b = false;
			if (!doesFileExist) {
				return b;
			}
		}
		return true;
	}

	public void onCreate(final Bundle bundle) {
		super.onCreate(bundle);
		if (!this.isTaskRoot()) {
			this.finish();
			return;
		}
		(SampleDownloaderActivity.xAPKS = new SampleDownloaderActivity.XAPKFile[1])[0] = new SampleDownloaderActivity.XAPKFile(true, 51, 6000L);
		if (!this.expansionFilesDelivered()) {
			while (true) {
				while (true) {
					Intent intent2 = null;
					Iterator iterator = null;
					Label_0176: {
						try {
							final Intent intent = this.getIntent();
							intent2 = new Intent((Context)this, (Class)this.getClass());
							intent2.setFlags(335544320);
							intent2.setAction(intent.getAction());
							if (intent.getCategories() != null) {
								iterator = intent.getCategories().iterator();
								if (iterator.hasNext()) {
									break Label_0176;
								}
							}
							if (DownloaderClientMarshaller.startDownloadServiceIfRequired((Context)this, PendingIntent.getActivity((Context)this, 0, intent2, 134217728), (Class)SampleDownloaderService.class) != 0) {
								this.initializeDownloadUI();
								return;
							}
						}
						catch (PackageManager.NameNotFoundException ex) {
							Log.e("com.resmed.refresh.expansion", "Cannot find own package! MAYDAY!");
							ex.printStackTrace();
						}
						break;
					}
					intent2.addCategory((String)iterator.next());
					continue;
				}
			}
		}
		this.initializeDownloadUI();
		if (!RefreshModelController.getInstance().getExpansionValidated()) {
			this.validateXAPKZipFiles();
			return;
		}
		this.startApp();
	}

	protected void onDestroy() {
		this.mCancelValidation = true;
		super.onDestroy();
	}

	public void onDownloadProgress(final DownloadProgressInfo downloadProgressInfo) {
		this.mAverageSpeed.setText((CharSequence)this.getString(2131165210, new Object[] { Helpers.getSpeedString(downloadProgressInfo.mCurrentSpeed) }));
		this.mTimeRemaining.setText((CharSequence)this.getString(2131165211, new Object[] { Helpers.getTimeRemaining(downloadProgressInfo.mTimeRemaining) }));
		downloadProgressInfo.mOverallTotal = downloadProgressInfo.mOverallTotal;
		this.mPB.setMax((int)(downloadProgressInfo.mOverallTotal >> 8));
		this.mPB.setProgress((int)(downloadProgressInfo.mOverallProgress >> 8));
		this.mProgressPercent.setText((CharSequence)(String.valueOf(Long.toString(100L * downloadProgressInfo.mOverallProgress / downloadProgressInfo.mOverallTotal)) + "%"));
		this.mProgressFraction.setText((CharSequence)Helpers.getDownloadProgressString(downloadProgressInfo.mOverallProgress, downloadProgressInfo.mOverallTotal));
	}

	public void onDownloadStateChanged(final int state) {
		this.setState(state);
		int n = 1;
		int n2 = 0;
		int n3 = 0;
		boolean buttonPausedState = false;
		boolean indeterminate = false;
		switch (state) {
			default: {
				buttonPausedState = true;
				indeterminate = true;
				n = 1;
				break;
			}
			case 1: {
				indeterminate = true;
				n3 = 0;
				buttonPausedState = false;
				n2 = 0;
				break;
			}
			case 2:
			case 3: {
				n = 1;
				indeterminate = true;
				n3 = 0;
				buttonPausedState = false;
				n2 = 0;
				break;
			}
			case 4: {
				n = 1;
				n3 = 0;
				indeterminate = false;
				buttonPausedState = false;
				n2 = 0;
				break;
			}
			case 15:
			case 16:
			case 18:
			case 19: {
				buttonPausedState = true;
				n3 = 1;
				indeterminate = false;
				n2 = 0;
				n = 0;
				break;
			}
			case 8:
			case 9: {
				buttonPausedState = true;
				n2 = 1;
				n3 = 0;
				indeterminate = false;
				n = 0;
				break;
			}
			case 7: {
				buttonPausedState = true;
				n3 = 0;
				indeterminate = false;
				n2 = 0;
				break;
			}
			case 12:
			case 14: {
				buttonPausedState = true;
				n3 = 0;
				indeterminate = false;
				n2 = 0;
				break;
			}
			case 5: {
				if (!RefreshModelController.getInstance().getExpansionValidated()) {
					this.validateXAPKZipFiles();
					return;
				}
				this.startApp();
				return;
			}
		}
		int visibility;
		if (n != 0) {
			visibility = 0;
		}
		else {
			visibility = 8;
		}
		if (this.mDashboard.getVisibility() != visibility) {
			this.mDashboard.setVisibility(visibility);
		}
		int visibility2;
		if (n2 != 0) {
			visibility2 = 0;
		}
		else {
			visibility2 = 8;
		}
		if (this.mCellMessage.getVisibility() != visibility2) {
			this.mCellMessage.setVisibility(visibility2);
		}
		this.mPB.setIndeterminate(indeterminate);
		this.setButtonPausedState(buttonPausedState);
		if (n3 != 0) {
			this.showFailed((Object)this.mStatusText.getText() + "\n" + this.getString(2131166129));
		}
	}

	protected void onResume() {
		if (this.mDownloaderClientStub != null) {
			this.mDownloaderClientStub.connect((Context)this);
		}
		super.onResume();
	}

	public void onServiceConnected(final Messenger messenger) {
		(this.mRemoteService = DownloaderServiceMarshaller.CreateProxy(messenger)).onClientUpdated(this.mDownloaderClientStub.getMessenger());
	}

	protected void onStop() {
		if (this.mDownloaderClientStub != null) {
			this.mDownloaderClientStub.disconnect((Context)this);
		}
		super.onStop();
	}

	/*void validateXAPKZipFiles() {
		((AsyncTask)new SampleDownloaderActivity.SampleDownloaderActivity$1(this)).execute(new Object[] { new Object() });
	}*#/
}
*/