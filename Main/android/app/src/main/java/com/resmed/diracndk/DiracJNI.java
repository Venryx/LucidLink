package com.resmed.diracndk;

import android.util.Log;
import java.io.FileDescriptor;

public class DiracJNI
{
  static
  {
    Log.i(IndexActivity.TAG, "import library");
    System.loadLibrary("dirac-jni");
  }
  
  public native void changeDuration(float paramFloat);
  
  public native void changePitch(float paramFloat);
  
  public native int getProcessingType();
  
  public native int getSampleRate();
  
  public native void loopBack();
  
  public native long processAudioThread(short[] paramArrayOfShort, int paramInt);
  
  public native void setProcessingType(int paramInt);
  
  public native void setupDirac(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2);
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\diracndk\DiracJNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */