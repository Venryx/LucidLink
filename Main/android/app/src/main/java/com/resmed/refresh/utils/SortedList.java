package com.resmed.refresh.utils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;

public class SortedList<T extends Comparable<T>>
  extends AbstractList<T>
{
  private int capacity;
  private ArrayList<T> internalList;
  private T lastReplacedElement;
  
  public SortedList()
  {
    this.internalList = new ArrayList();
  }
  
  public SortedList(int paramInt)
  {
    this.internalList = new ArrayList(paramInt);
    this.capacity = paramInt;
  }
  
  private int belongsInList(T paramT)
  {
    for (int i = 0;; i++)
    {
      int j;
      if (i >= this.internalList.size()) {
        j = -1;
      }
      do
      {
        return j;
        j = i;
      } while (paramT.compareTo((Comparable)this.internalList.get(i)) > 0);
    }
  }
  
  public T get(int paramInt)
  {
    return (Comparable)this.internalList.get(paramInt);
  }
  
  public T getLastReplacedElement()
  {
    return this.lastReplacedElement;
  }
  
  public boolean insert(T paramT)
  {
    boolean bool = true;
    if (this.internalList.size() < this.capacity) {
      this.internalList.add(paramT);
    }
    for (;;)
    {
      return bool;
      int i = belongsInList(paramT);
      if (i >= 0)
      {
        this.lastReplacedElement = ((Comparable)this.internalList.set(i, paramT));
        Collections.sort(this.internalList);
      }
      else
      {
        bool = false;
      }
    }
  }
  
  public int size()
  {
    return this.internalList.size();
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */