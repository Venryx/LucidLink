package com.resmed.refresh.model.mappers;

import com.resmed.refresh.model.RST_AdviceItem;
import com.resmed.refresh.model.RST_Button;
import com.resmed.refresh.model.RST_SleepEvent;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.Advice;
import com.resmed.refresh.model.json.AdviceButton;
import com.resmed.refresh.utils.RefreshTools;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AdviceMapper
{
  public static RST_AdviceItem processAdviceItem(Advice paramAdvice)
  {
    paramAdvice.check();
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    Object localObject2 = localRefreshModelController.localAdviceForId(paramAdvice.getId());
    Object localObject1 = localObject2;
    if (localObject2 == null) {
      localObject1 = localRefreshModelController.createAdviceItem(paramAdvice.getId());
    }
    ((RST_AdviceItem)localObject1).setCategory(paramAdvice.getCategory());
    ((RST_AdviceItem)localObject1).setContent(paramAdvice.getContent());
    ((RST_AdviceItem)localObject1).setDetail(paramAdvice.getDetails());
    ((RST_AdviceItem)localObject1).setHeader(paramAdvice.getTitle());
    ((RST_AdviceItem)localObject1).setHtmlContent(paramAdvice.getHtmlContent());
    ((RST_AdviceItem)localObject1).getHypnogramInfo().setEpochNumber(paramAdvice.getHypnogramInfo().getEpochNumber());
    ((RST_AdviceItem)localObject1).setIcon(paramAdvice.getIcon());
    ((RST_AdviceItem)localObject1).setOrdr(paramAdvice.getOrderid());
    ((RST_AdviceItem)localObject1).setId(paramAdvice.getId());
    ((RST_AdviceItem)localObject1).setSessionId(paramAdvice.getRecordId());
    ((RST_AdviceItem)localObject1).setArticleUrl(paramAdvice.getArticleUrl());
    ((RST_AdviceItem)localObject1).setSubtitle(paramAdvice.getSubtitle());
    Iterator localIterator = paramAdvice.getButtons().iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        ((RST_AdviceItem)localObject1).setTimestamp(RefreshTools.getDateFromString(paramAdvice.getDateTime()));
        ((RST_AdviceItem)localObject1).setType(paramAdvice.getType());
        ((RST_AdviceItem)localObject1).setRST_User(localRefreshModelController.getUser());
        ((RST_AdviceItem)localObject1).update();
        return (RST_AdviceItem)localObject1;
      }
      AdviceButton localAdviceButton = (AdviceButton)localIterator.next();
      localObject2 = new RST_Button(Long.valueOf(localAdviceButton.getId()));
      ((RST_Button)localObject2).setColour(localAdviceButton.getColour());
      ((RST_Button)localObject2).setIcon(localAdviceButton.getIcon());
      ((RST_Button)localObject2).setOrdr(localAdviceButton.getOrder().intValue());
      ((RST_Button)localObject2).setTitle(localAdviceButton.getTitle());
      ((RST_AdviceItem)localObject1).addButton((RST_Button)localObject2);
    }
  }
  
  public static List<RST_AdviceItem> processListAdviceItem(List<Advice> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    paramList = paramList.iterator();
    for (;;)
    {
      if (!paramList.hasNext()) {
        return localArrayList;
      }
      localArrayList.add(processAdviceItem((Advice)paramList.next()));
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */