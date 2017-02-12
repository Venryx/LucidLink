package com.resmed.refresh.expansion;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.android.vending.expansion.downloader.Helpers;
import java.io.FileDescriptor;

public class ExpansionUtil
{
  public static FileDescriptor getFDFromExpansion(Context paramContext, String paramString)
  {
    Object localObject = paramContext.getPackageName();
    try
    {
      paramContext = Helpers.getExpansionAPKFileName(paramContext, true, paramContext.getPackageManager().getPackageInfo((String)localObject, 0).versionCode);
      localObject = new com/android/vending/expansion/zipfile/ZipResourceFile;
      ((ZipResourceFile)localObject).<init>(paramContext);
      paramContext = ((ZipResourceFile)localObject).getAssetFileDescriptor(paramString).getFileDescriptor();
      return paramContext;
    }
    catch (Exception paramContext)
    {
      for (;;)
      {
        paramContext = null;
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\expansion\ExpansionUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */