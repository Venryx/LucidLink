package com.resmed.refresh.ui.customview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.resmed.refresh.model.RST_QuestionItem;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.preSleepLog;
import java.util.List;

public class PreSleepQuestionView
  extends LinearLayout
{
  private RST_QuestionItem questionItem;
  private CustomSeekBar seekBar;
  
  public PreSleepQuestionView(Context paramContext, RST_QuestionItem paramRST_QuestionItem)
  {
    super(paramContext);
    this.questionItem = paramRST_QuestionItem;
    inflateLayout(paramContext);
  }
  
  private void inflateLayout(Context paramContext)
  {
    paramContext = ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(2130903205, this);
    ((TextView)paramContext.findViewById(2131100688)).setText(this.questionItem.getText());
    this.seekBar = ((CustomSeekBar)paramContext.findViewById(2131100689));
    this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        if (PreSleepQuestionView.this.questionItem.getAnswer() != paramAnonymousInt)
        {
          PreSleepQuestionView.this.questionItem.setAnswer(paramAnonymousInt);
          PreSleepQuestionView.this.questionItem.update();
          RefreshModelController.getInstance().save();
          preSleepLog.addTrace("QuestionItem Question= " + PreSleepQuestionView.this.questionItem.getText() + "QuestionItem Question index= " + PreSleepQuestionView.this.questionItem.getId() + " answer=" + PreSleepQuestionView.this.questionItem.getAnswer());
          Log.d("com.resmed.refresh.model", "QuestionItem index= " + PreSleepQuestionView.this.questionItem.getId() + " answer=" + PreSleepQuestionView.this.questionItem.getAnswer());
          AppFileLog.addTrace("SDTeam: interacting with QuestionItem with index= " + PreSleepQuestionView.this.questionItem.getId() + "Q text = " + PreSleepQuestionView.this.questionItem.getText() + " ,current answer=" + PreSleepQuestionView.this.questionItem.getAnswer());
        }
      }
      
      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {}
      
      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {}
    });
  }
  
  public Integer getAnswer()
  {
    return Integer.valueOf(this.questionItem.getAnswer());
  }
  
  public void setRST_QuestionItem(RST_QuestionItem paramRST_QuestionItem)
  {
    this.questionItem = paramRST_QuestionItem;
  }
  
  public void setSeekBarLabel(List<String> paramList)
  {
    this.seekBar.setAdapter(paramList);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\PreSleepQuestionView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */