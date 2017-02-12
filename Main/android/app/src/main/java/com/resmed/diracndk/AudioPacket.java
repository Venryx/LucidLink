package com.resmed.diracndk;

public class AudioPacket
{
  public short[] data;
  public long samples;
  
  AudioPacket(int paramInt)
  {
    this.data = new short[paramInt];
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\diracndk\AudioPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */