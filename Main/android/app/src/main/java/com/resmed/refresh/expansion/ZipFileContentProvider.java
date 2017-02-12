package com.resmed.refresh.expansion;

import com.android.vending.expansion.zipfile.APEZProvider;

public class ZipFileContentProvider
  extends APEZProvider
{
  public String getAuthority()
  {
    return "com.resmed.refresh.expansion.provider.ZipFileContentProvider";
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */