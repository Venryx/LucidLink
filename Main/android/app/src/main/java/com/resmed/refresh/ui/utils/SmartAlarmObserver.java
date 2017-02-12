package com.resmed.refresh.ui.utils;

import com.resmed.refresh.ui.fragments.SmartAlarmFragment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SmartAlarmObserver
{
  private static SmartAlarmObserver instance;
  private List<SmartAlarmObserverInterface> concreteObserver = new ArrayList();
  
  public static SmartAlarmObserver getInstance()
  {
    if (instance == null) {
      instance = new SmartAlarmObserver();
    }
    return instance;
  }
  
  public void register(SmartAlarmObserverInterface paramSmartAlarmObserverInterface)
  {
    if (!this.concreteObserver.contains(paramSmartAlarmObserverInterface)) {
      this.concreteObserver.add(paramSmartAlarmObserverInterface);
    }
  }
  
  public void unRegister(SmartAlarmObserverInterface paramSmartAlarmObserverInterface)
  {
    if (this.concreteObserver.contains(paramSmartAlarmObserverInterface)) {
      this.concreteObserver.remove(paramSmartAlarmObserverInterface);
    }
  }
  
  public void updateSmartAlarmActiveButton(boolean paramBoolean)
  {
    Iterator localIterator = this.concreteObserver.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      SmartAlarmObserverInterface localSmartAlarmObserverInterface = (SmartAlarmObserverInterface)localIterator.next();
      if ((localSmartAlarmObserverInterface != null) && (((SmartAlarmFragment)localSmartAlarmObserverInterface).isAdded())) {
        localSmartAlarmObserverInterface.updateSmartAlarmActiveButton(paramBoolean);
      }
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */