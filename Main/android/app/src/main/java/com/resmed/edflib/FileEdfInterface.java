package com.resmed.edflib;

import java.io.File;

public abstract interface FileEdfInterface
{
  public abstract int closeFile();
  
  public abstract int compressFile(String paramString);
  
  public abstract boolean deleteFile();
  
  public abstract int fixEdfFile();
  
  public abstract File getFile();
  
  public abstract String getFilePath();
  
  public abstract String getRM60LibVersion();
  
  public abstract int openFileForMode(String paramString);
  
  public abstract int readDigitalSamples();
  
  public abstract int writeDigitalSamples(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\edflib\FileEdfInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */