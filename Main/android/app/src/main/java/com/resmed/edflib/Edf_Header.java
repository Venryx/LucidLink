package com.resmed.edflib;

public class Edf_Header
{
  private static int EDFLIB_MAXSIGNALS = 512;
  String admincode;
  long annotations_in_file;
  String birthdate;
  long datarecord_duration;
  long datarecords_in_file;
  int edfsignals;
  String equipment;
  long file_duration;
  int filetype;
  String gender;
  int handle;
  String patient;
  String patient_additional;
  String patient_name;
  String patientcode;
  String recording;
  String recording_additional;
  Edf_Param[] signalparam = new Edf_Param[EDFLIB_MAXSIGNALS];
  int startdate_day;
  int startdate_month;
  int startdate_year;
  int starttime_hour;
  int starttime_minute;
  int starttime_second;
  long starttime_subsecond;
  String technician;
  
  public class Edf_Param
  {
    int dig_max;
    int dig_min;
    String label;
    double phys_max;
    double phys_min;
    String physdimension;
    String prefilter;
    int smp_in_datarecord;
    long smp_in_file;
    String transducer;
    
    public Edf_Param() {}
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\edflib\Edf_Header.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */