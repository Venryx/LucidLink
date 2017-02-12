package com.resmed.refresh.model.mindclear;

import com.google.gson.GsonBuilder;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.utils.Log;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class MindClearManager
{
  private static MindClearManager mindClearMapper;
  private static RefreshModelController refreshModelController;
  private Comparator<MindClearBase> comparator = new Comparator()
  {
    public int compare(MindClearBase paramAnonymousMindClearBase1, MindClearBase paramAnonymousMindClearBase2)
    {
      return new Date(paramAnonymousMindClearBase2.getTimestamp()).compareTo(new Date(paramAnonymousMindClearBase1.getTimestamp()));
    }
  };
  private int currentIndex;
  private MindClearItems mindClearItems;
  private String tempText;
  
  private MindClearManager()
  {
    refreshModelController = RefreshModelController.getInstance();
    fetchDataFromFilePreference();
    storeDataIntoFilePreference();
  }
  
  private void createStructure(String paramString)
  {
    this.mindClearItems = new MindClearItems();
    try
    {
      localJSONObject = new org/json/JSONObject;
      localJSONObject.<init>(paramString);
      paramString = localJSONObject.getJSONArray("Data");
      i = 0;
      if (i >= paramString.length()) {
        return;
      }
    }
    catch (JSONException paramString)
    {
      for (;;)
      {
        JSONObject localJSONObject;
        int i;
        Object localObject2;
        Object localObject1;
        paramString.printStackTrace();
      }
    }
    localJSONObject = paramString.getJSONObject(i);
    if ("TEXT".equals(localJSONObject.get("Type")))
    {
      localObject2 = this.mindClearItems;
      localObject1 = new com/resmed/refresh/model/mindclear/MindClearText;
      ((MindClearText)localObject1).<init>(localJSONObject.getString("Text"), Long.parseLong(localJSONObject.getString("Timestamp")));
      ((MindClearItems)localObject2).addElement((MindClearBase)localObject1);
    }
    for (;;)
    {
      i++;
      break;
      localObject1 = this.mindClearItems;
      localObject2 = new com/resmed/refresh/model/mindclear/MindClearVoice;
      ((MindClearVoice)localObject2).<init>(localJSONObject.getString("FileName"), Long.parseLong(localJSONObject.getString("Timestamp")), Integer.valueOf(Integer.parseInt(localJSONObject.getString("Duration"))));
      ((MindClearItems)localObject1).addElement((MindClearBase)localObject2);
    }
  }
  
  private void fetchDataFromFilePreference()
  {
    String str = refreshModelController.getClearMindData();
    if (str != null) {
      createStructure(str);
    }
    for (;;)
    {
      this.currentIndex = -1;
      return;
      this.mindClearItems = new MindClearItems();
    }
  }
  
  public static MindClearManager getInstance()
  {
    try
    {
      if (mindClearMapper == null)
      {
        localMindClearManager = new com/resmed/refresh/model/mindclear/MindClearManager;
        localMindClearManager.<init>();
        mindClearMapper = localMindClearManager;
      }
      MindClearManager localMindClearManager = mindClearMapper;
      return localMindClearManager;
    }
    finally {}
  }
  
  public void addMindClearText()
  {
    this.tempText = this.tempText.trim();
    if (this.tempText.length() > 0) {
      this.tempText = (this.tempText.substring(0, 1).toUpperCase() + this.tempText.substring(1));
    }
    Log.d("com.resmed.refresh.mindClear", "Adding text note with text : " + this.tempText);
    this.mindClearItems.addElement(new MindClearText(this.tempText, System.currentTimeMillis()));
    Collections.sort(this.mindClearItems.getMindClearNotes(), this.comparator);
  }
  
  public void addMindClearVoice(String paramString, Integer paramInteger)
  {
    this.mindClearItems.addElement(new MindClearVoice(paramString, System.currentTimeMillis(), paramInteger));
    Collections.sort(this.mindClearItems.getMindClearNotes(), this.comparator);
  }
  
  public void deleteElement(int paramInt)
  {
    this.mindClearItems.getMindClearNotes().remove(paramInt);
    Collections.sort(this.mindClearItems.getMindClearNotes(), this.comparator);
  }
  
  public int getCurrentPosition()
  {
    return this.currentIndex;
  }
  
  public String getFileName()
  {
    return this.mindClearItems.getFileName();
  }
  
  public List<MindClearBase> getMindClearNotes()
  {
    Collections.sort(this.mindClearItems.getMindClearNotes(), this.comparator);
    return this.mindClearItems.getMindClearNotes();
  }
  
  public int getNumberOfUnreadNotes()
  {
    Object localObject = this.mindClearItems.getMindClearNotes();
    int i = 0;
    localObject = ((List)localObject).iterator();
    for (;;)
    {
      if (!((Iterator)localObject).hasNext()) {
        return i;
      }
      if (!((MindClearBase)((Iterator)localObject).next()).isRead()) {
        i++;
      }
    }
  }
  
  public String getTempFileName()
  {
    return this.mindClearItems.getTempFileName();
  }
  
  public void logout()
  {
    mindClearMapper = null;
  }
  
  public void setCurrentPosition(int paramInt)
  {
    this.currentIndex = paramInt;
  }
  
  public void setTempText(String paramString)
  {
    this.tempText = paramString;
  }
  
  public void storeDataIntoFilePreference()
  {
    Object localObject = new GsonBuilder();
    ((GsonBuilder)localObject).excludeFieldsWithoutExposeAnnotation();
    localObject = ((GsonBuilder)localObject).create().toJson(this.mindClearItems);
    refreshModelController.storeClearMindData((String)localObject);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\mindclear\MindClearManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */