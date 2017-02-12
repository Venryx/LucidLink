package com.resmed.edflib;

public class RstEdfSigParams
{
  int digMax;
  int digMin;
  String label;
  String physDim;
  double physMax;
  double physMin;
  int sampFreq;
  
  public abstract class PhysicDimension
  {
    public static final String BPM = "bpm";
    public static final String MA = "mA";
    public static final String UV = "uV";
    
    public PhysicDimension() {}
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\edflib\RstEdfSigParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */