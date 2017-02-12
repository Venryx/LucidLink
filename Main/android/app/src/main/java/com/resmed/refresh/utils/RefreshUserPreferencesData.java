package com.resmed.refresh.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class RefreshUserPreferencesData
{
  private static final String GENERAL_PREFERENCES_TAG = "GENERAL_PREFERENCES";
  private SharedPreferences preferences;
  
  public RefreshUserPreferencesData(Context paramContext)
  {
    this.preferences = paramContext.getSharedPreferences("GENERAL_PREFERENCES", 4);
  }
  
  public void clearAll()
  {
    this.preferences.edit().clear().commit();
  }
  
  public boolean getBooleanConfigValue(String paramString)
  {
    return this.preferences.getBoolean(paramString, false);
  }
  
  public boolean getBooleanUserValue(String paramString1, String paramString2)
  {
    return getBooleanConfigValue(paramString1 + paramString2);
  }
  
  public int getIntegerConfigValue(String paramString)
  {
    return this.preferences.getInt(paramString, -1);
  }
  
  public int getIntegerConfigValue(String paramString, int paramInt)
  {
    return this.preferences.getInt(paramString, paramInt);
  }
  
  public int getIntegerUserValue(String paramString1, String paramString2)
  {
    return getIntegerConfigValue(paramString1 + paramString2);
  }
  
  public long getLongConfigValue(String paramString)
  {
    return this.preferences.getLong(paramString, Long.MIN_VALUE);
  }
  
  public long getLongConfigValue(String paramString, long paramLong)
  {
    return this.preferences.getLong(paramString, paramLong);
  }
  
  public long getLongUserValue(String paramString1, String paramString2)
  {
    return getLongConfigValue(paramString1 + paramString2);
  }
  
  public String getStringConfigValue(String paramString)
  {
    return this.preferences.getString(paramString, null);
  }
  
  public String getStringUserValue(String paramString1, String paramString2)
  {
    return getStringConfigValue(paramString1 + paramString2);
  }
  
  public void removeBooleanConfigValue(String paramString)
  {
    SharedPreferences.Editor localEditor = this.preferences.edit();
    localEditor.remove(paramString);
    localEditor.commit();
  }
  
  public void removeBooleanUserValue(String paramString1, String paramString2)
  {
    removeBooleanConfigValue(paramString1 + paramString2);
  }
  
  public void removeIntegerConfigValue(String paramString)
  {
    SharedPreferences.Editor localEditor = this.preferences.edit();
    localEditor.remove(paramString);
    localEditor.commit();
  }
  
  public void removeIntegerUserValue(String paramString1, String paramString2)
  {
    removeIntegerConfigValue(paramString1 + paramString2);
  }
  
  public void removeLongConfigValue(String paramString)
  {
    SharedPreferences.Editor localEditor = this.preferences.edit();
    localEditor.remove(paramString);
    localEditor.commit();
  }
  
  public void removeLongUserValue(String paramString1, String paramString2)
  {
    removeLongConfigValue(paramString1 + paramString2);
  }
  
  public void removeStringConfigValue(String paramString)
  {
    SharedPreferences.Editor localEditor = this.preferences.edit();
    localEditor.remove(paramString);
    localEditor.commit();
  }
  
  public void removeStringUserValue(String paramString1, String paramString2)
  {
    removeStringConfigValue(paramString1 + paramString2);
  }
  
  public void setBooleanConfigValue(String paramString, boolean paramBoolean)
  {
    SharedPreferences.Editor localEditor = this.preferences.edit();
    localEditor.putBoolean(paramString, paramBoolean);
    localEditor.commit();
  }
  
  public void setBooleanUserValue(String paramString1, String paramString2, boolean paramBoolean)
  {
    setBooleanConfigValue(paramString1 + paramString2, paramBoolean);
  }
  
  public void setIntegerConfigValue(String paramString, Integer paramInteger)
  {
    SharedPreferences.Editor localEditor = this.preferences.edit();
    localEditor.putInt(paramString, paramInteger.intValue());
    localEditor.commit();
  }
  
  public void setIntegerUserValue(String paramString1, String paramString2, int paramInt)
  {
    setIntegerConfigValue(paramString1 + paramString2, Integer.valueOf(paramInt));
  }
  
  public void setLongConfigValue(String paramString, long paramLong)
  {
    SharedPreferences.Editor localEditor = this.preferences.edit();
    localEditor.putLong(paramString, paramLong);
    localEditor.commit();
  }
  
  public void setLongUserValue(String paramString1, String paramString2, long paramLong)
  {
    setLongConfigValue(paramString1 + paramString2, paramLong);
  }
  
  public void setStringConfigValue(String paramString1, String paramString2)
  {
    SharedPreferences.Editor localEditor = this.preferences.edit();
    localEditor.putString(paramString1, paramString2);
    localEditor.commit();
  }
  
  public void setStringUserValue(String paramString1, String paramString2, String paramString3)
  {
    setStringConfigValue(paramString1 + paramString2, paramString3);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\RefreshUserPreferencesData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */