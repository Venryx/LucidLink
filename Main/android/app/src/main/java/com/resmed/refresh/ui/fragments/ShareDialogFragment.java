package com.resmed.refresh.ui.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ShareDialogFragment
  extends DialogFragment
{
  public static final String SHARE_IMG_TAG = "SHARE_IMG_TAG";
  public static final String SHARE_TEXT_TAG = "SHARE_TEXT_TAG";
  private ShareDialogReceiver receiver;
  private Type type;
  
  public ShareDialogFragment(Type paramType, ShareDialogReceiver paramShareDialogReceiver)
  {
    this.receiver = paramShareDialogReceiver;
    this.type = paramType;
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
    paramBundle = getActivity().getLayoutInflater();
    if (this.type == Type.TWITTER) {}
    for (paramBundle = paramBundle.inflate(2130903124, null);; paramBundle = paramBundle.inflate(2130903122, null))
    {
      localBuilder.setView(paramBundle);
      final Object localObject1 = getArguments();
      Object localObject2 = ((Bundle)localObject1).getString("SHARE_TEXT_TAG");
      Bitmap localBitmap = (Bitmap)((Bundle)localObject1).getParcelable("SHARE_IMG_TAG");
      localObject1 = (EditText)paramBundle.findViewById(2131099886);
      ImageView localImageView = (ImageView)paramBundle.findViewById(2131099887);
      ((EditText)localObject1).setText((CharSequence)localObject2);
      localImageView.setImageBitmap(localBitmap);
      localObject2 = (Button)paramBundle.findViewById(2131099889);
      paramBundle = (Button)paramBundle.findViewById(2131099888);
      ((Button)localObject2).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          ShareDialogFragment.this.receiver.share(localObject1.getText().toString());
          ShareDialogFragment.this.getDialog().cancel();
        }
      });
      paramBundle.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          ShareDialogFragment.this.getDialog().cancel();
        }
      });
      return localBuilder.create();
    }
  }
  
  public static abstract interface ShareDialogReceiver
  {
    public abstract void share(String paramString);
  }
  
  public static enum Type
  {
    FACEBOOK,  TWITTER;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\ShareDialogFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */