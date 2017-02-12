package com.resmed.refresh.model.mappers;

import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RST_ValueItem;
import com.resmed.refresh.model.graphs.RRHypnoData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class HypnoMapper
{
  public static List<RRHypnoData> getHypnoData(RST_SleepSessionInfo paramRST_SleepSessionInfo)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramRST_SleepSessionInfo.getStartTime();
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime((Date)localObject);
    localObject = paramRST_SleepSessionInfo.getSleepStates().iterator();
    for (;;)
    {
      if (!((Iterator)localObject).hasNext()) {
        return localArrayList;
      }
      RST_ValueItem localRST_ValueItem = (RST_ValueItem)((Iterator)localObject).next();
      paramRST_SleepSessionInfo = new RRHypnoData();
      paramRST_SleepSessionInfo.setState((int)localRST_ValueItem.getValue());
      paramRST_SleepSessionInfo.setHour(localCalendar.getTime());
      localArrayList.add(paramRST_SleepSessionInfo);
      localCalendar.add(13, 30);
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */