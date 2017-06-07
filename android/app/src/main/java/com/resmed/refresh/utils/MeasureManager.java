package com.resmed.refresh.utils;

public class MeasureManager
{
  public static float convertCelsiusToFahrenheit(float paramFloat)
  {
    return 9.0F * paramFloat / 5.0F + 32.0F;
  }
  
  public static float convertFahrenheitToCelsius(float paramFloat)
  {
    return (paramFloat - 32.0F) * 5.0F / 9.0F;
  }
  
  public static float getInchFromMeters(float paramFloat)
  {
    return (float)(paramFloat / 2.54D);
  }
  
  public static float getKilogramFromPound(float paramFloat)
  {
    return (float)(paramFloat * 0.453592D);
  }
  
  public static float getMetersFromInch(float paramFloat)
  {
    return (float)(paramFloat * 2.54D);
  }
  
  public static float getPoundFromKilogram(float paramFloat)
  {
    return (float)(paramFloat / 0.453592D);
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */