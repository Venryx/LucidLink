package com.resmed.refresh.model.mappers;

import com.resmed.refresh.model.RST_LocationItem;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.Location;

public class LocationMapper
{
  public static Location getLocation(RST_LocationItem paramRST_LocationItem)
  {
    Location localLocation = new Location();
    localLocation.setDirection(String.valueOf(paramRST_LocationItem.getDirection()));
    localLocation.setLatitude(String.valueOf(paramRST_LocationItem.getLatitude()));
    localLocation.setLongitude(String.valueOf(paramRST_LocationItem.getLongitude()));
    localLocation.setTimeZone(String.valueOf(RefreshModelController.getInstance().userTimezoneOffset()));
    return localLocation;
  }
  
  public static RST_LocationItem getRST_LocationItem(Location paramLocation)
  {
    RST_LocationItem localRST_LocationItem = new RST_LocationItem();
    localRST_LocationItem.setDirection(Float.valueOf(paramLocation.getDirection()).floatValue());
    localRST_LocationItem.setLatitude(Float.valueOf(paramLocation.getLatitude()).floatValue());
    localRST_LocationItem.setLongitude(Float.valueOf(paramLocation.getLongitude()).floatValue());
    localRST_LocationItem.setTimezone(Integer.valueOf(paramLocation.getTimeZone()).intValue());
    return localRST_LocationItem;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */