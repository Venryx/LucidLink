package com.resmed.refresh.model.mappers;

import com.resmed.refresh.model.RST_DisplayItem;
import com.resmed.refresh.model.RST_NightQuestions;
import com.resmed.refresh.model.RST_QuestionItem;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.DisplayAnswers;
import com.resmed.refresh.model.json.NightQuestions;
import com.resmed.refresh.model.json.PreSleepQuestion;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class NightQuestionsMapper
{
  @Deprecated
  public static List<PreSleepQuestion> getPreSleepQuestions(RST_NightQuestions paramRST_NightQuestions)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramRST_NightQuestions.getQuestions().iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return localArrayList;
      }
      paramRST_NightQuestions = (RST_QuestionItem)localIterator.next();
      PreSleepQuestion localPreSleepQuestion = new PreSleepQuestion();
      localPreSleepQuestion.setAnswerId(paramRST_NightQuestions.getAnswer() + 1);
      localPreSleepQuestion.setQuestionId(paramRST_NightQuestions.getQuestionId());
      Log.d("com.resmed.refresh.model", "Question JSON question ID= " + localPreSleepQuestion.getQuestionId() + " answer=" + localPreSleepQuestion.getAnswerId());
      AppFileLog.addTrace("SDTeam: 1.Question JSON question ID= " + localPreSleepQuestion.getQuestionId() + " answer=" + localPreSleepQuestion.getAnswerId());
      localArrayList.add(localPreSleepQuestion);
    }
  }
  
  public static List<PreSleepQuestion> getPreSleepQuestions(String paramString1, String paramString2)
  {
    ArrayList localArrayList = new ArrayList();
    for (;;)
    {
      try
      {
        localStringTokenizer1 = new java/util/StringTokenizer;
        localStringTokenizer1.<init>(paramString1, ",");
        localStringTokenizer2 = new java/util/StringTokenizer;
        localStringTokenizer2.<init>(paramString2, ",");
        if (localStringTokenizer1.hasMoreElements()) {
          continue;
        }
        paramString1 = localArrayList;
      }
      catch (Exception paramString1)
      {
        StringTokenizer localStringTokenizer1;
        StringTokenizer localStringTokenizer2;
        String str;
        paramString1 = null;
        continue;
      }
      return paramString1;
      str = localStringTokenizer1.nextToken();
      paramString2 = localStringTokenizer2.nextToken();
      paramString1 = localArrayList;
      if (paramString2.length() != 0)
      {
        paramString1 = localArrayList;
        if (str.length() != 0)
        {
          paramString1 = new com/resmed/refresh/model/json/PreSleepQuestion;
          paramString1.<init>();
          paramString1.setQuestionId(Integer.parseInt(str));
          paramString1.setAnswerId(Integer.parseInt(paramString2) + 1);
          paramString2 = new java/lang/StringBuilder;
          paramString2.<init>("Question JSON index= ");
          Log.d("com.resmed.refresh.model", paramString1.getQuestionId() + " answer=" + paramString1.getAnswerId());
          paramString2 = new java/lang/StringBuilder;
          paramString2.<init>("SDTeam:Question JSON index with incrementing Q answerId by 1 to map with backend= ");
          AppFileLog.addTrace(paramString1.getQuestionId() + " answer=" + paramString1.getAnswerId());
          localArrayList.add(paramString1);
        }
      }
    }
  }
  
  public static RST_NightQuestions processNightQuestions(NightQuestions paramNightQuestions)
  {
    return processQuestions(paramNightQuestions.getPreSleepQuestion(), paramNightQuestions.getContentVersion());
  }
  
  public static RST_NightQuestions processNightQuestions(List<PreSleepQuestion> paramList)
  {
    return processQuestions(paramList, 0);
  }
  
  private static RST_NightQuestions processQuestions(List<PreSleepQuestion> paramList, int paramInt)
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    RST_NightQuestions localRST_NightQuestions = localRefreshModelController.createNightQuestionsItem();
    localRST_NightQuestions.setVersion(paramInt);
    paramInt = 0;
    if (paramInt >= paramList.size()) {
      return localRST_NightQuestions;
    }
    PreSleepQuestion localPreSleepQuestion = (PreSleepQuestion)paramList.get(paramInt);
    RST_QuestionItem localRST_QuestionItem = localRefreshModelController.createQuestionItem(localPreSleepQuestion.getBackendId());
    localRST_QuestionItem.setOrdr(paramInt);
    localRST_QuestionItem.setText(localPreSleepQuestion.getDisplayText());
    Log.d("com.resmed.refresh.model", "processQuestions " + paramList.size() + " size, received = " + localPreSleepQuestion.getDisplayText() + " ID = " + localPreSleepQuestion.getBackendId());
    for (int i = 0;; i++)
    {
      if (i >= localPreSleepQuestion.getDisplayAnswers().size())
      {
        localRST_NightQuestions.update();
        localRefreshModelController.save();
        paramInt++;
        break;
      }
      DisplayAnswers localDisplayAnswers = (DisplayAnswers)localPreSleepQuestion.getDisplayAnswers().get(i);
      RST_DisplayItem localRST_DisplayItem = new RST_DisplayItem();
      localRST_DisplayItem.setDisplayId(localDisplayAnswers.getId());
      localRST_DisplayItem.setOrdr(i);
      localRST_DisplayItem.setValue(localDisplayAnswers.getText());
      localRST_QuestionItem.addDisplayItem(localRST_DisplayItem);
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\mappers\NightQuestionsMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */