package com.resmed.edflib;

public abstract interface EdfLibCallbackHandler
{
  public abstract void onDigitalSamplesRead(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
  
  public abstract void onFileClosed();
  
  public abstract void onFileCompressed(int paramInt);
  
  public abstract void onFileFixed();
  
  public abstract void onFileOpened();
  
  public abstract void onWroteDigitalSamples();
  
  public abstract void onWroteMetadata();
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\edflib\EdfLibCallbackHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */