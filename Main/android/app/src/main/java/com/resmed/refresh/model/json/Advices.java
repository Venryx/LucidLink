package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Advices
{
  @SerializedName("Result")
  private List<Advice> advices;
  
  public List<Advice> getAdvices()
  {
    return this.advices;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\Advices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */