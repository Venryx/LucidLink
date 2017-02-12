package com.resmed.cobs;

public class COBS
{
  private static COBS Instance = null;
  
  static
  {
    System.loadLibrary("cobs");
  }
  
  public static COBS getInstance()
  {
    if (Instance == null) {
      Instance = new COBS();
    }
    return Instance;
  }
  
  public native byte[] decode(byte[] paramArrayOfByte);
  
  public native byte[] encode(byte[] paramArrayOfByte);
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\cobs\COBS.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */