package com.resmed.refresh.ui.utils;

import com.resmed.refresh.ui.fragments.SleepButtonFragment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SleepButtonObserver
{
  private static SleepButtonObserver sleepButtonObserver;
  private List<SleepButtonObserverInterface> concreteObserver = new ArrayList();
  
  public static SleepButtonObserver getInstance()
  {
    try
    {
      if (sleepButtonObserver == null)
      {
        localSleepButtonObserver = new com/resmed/refresh/ui/utils/SleepButtonObserver;
        localSleepButtonObserver.<init>();
        sleepButtonObserver = localSleepButtonObserver;
      }
      SleepButtonObserver localSleepButtonObserver = sleepButtonObserver;
      return localSleepButtonObserver;
    }
    finally {}
  }
  
  public void register(SleepButtonObserverInterface paramSleepButtonObserverInterface)
  {
    if (!this.concreteObserver.contains(paramSleepButtonObserverInterface)) {
      this.concreteObserver.add(paramSleepButtonObserverInterface);
    }
  }
  
  public void unRegister(SleepButtonObserverInterface paramSleepButtonObserverInterface)
  {
    if (this.concreteObserver.contains(paramSleepButtonObserverInterface)) {
      this.concreteObserver.remove(paramSleepButtonObserverInterface);
    }
  }
  
  public void updateRelaxIcon(boolean paramBoolean, Integer paramInteger)
  {
    Iterator localIterator = this.concreteObserver.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      SleepButtonObserverInterface localSleepButtonObserverInterface = (SleepButtonObserverInterface)localIterator.next();
      if ((localSleepButtonObserverInterface != null) && (((SleepButtonFragment)localSleepButtonObserverInterface).isAdded())) {
        localSleepButtonObserverInterface.updateRelaxIcon(paramBoolean, paramInteger);
      }
    }
  }
  
  public void updateSmartAlartmIcon(boolean paramBoolean, String paramString)
  {
    Iterator localIterator = this.concreteObserver.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      SleepButtonObserverInterface localSleepButtonObserverInterface = (SleepButtonObserverInterface)localIterator.next();
      if ((localSleepButtonObserverInterface != null) && (((SleepButtonFragment)localSleepButtonObserverInterface).isAdded())) {
        localSleepButtonObserverInterface.updateSmartAlartmIcon(paramBoolean, paramString);
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\utils\SleepButtonObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */