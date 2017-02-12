package com.resmed.refresh.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.resmed.refresh.ui.font.GroteskLight;
import com.resmed.refresh.utils.audio.SoundResources.RelaxSound;

public class MeditationAdapter
  extends BaseAdapter
{
  private int index = 0;
  
  public MeditationAdapter(int paramInt)
  {
    this.index = paramInt;
  }
  
  public int getCount()
  {
    return SoundResources.RelaxSound.values().length;
  }
  
  public Object getItem(int paramInt)
  {
    return null;
  }
  
  public long getItemId(int paramInt)
  {
    return 0L;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = paramView;
    paramView = (View)localObject;
    if (localObject == null)
    {
      paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(2130903190, null);
      paramViewGroup = new ViewHolder();
      paramViewGroup.text = ((GroteskLight)paramView.findViewById(2131100614));
      paramViewGroup.shape = ((ImageView)paramView.findViewById(2131100613));
      paramViewGroup.wrapperShape = ((RelativeLayout)paramView.findViewById(2131100612));
      paramViewGroup.wrapperShape.setBackgroundResource(2131296329);
      paramView.setTag(paramViewGroup);
    }
    localObject = (ViewHolder)paramView.getTag();
    paramViewGroup = SoundResources.RelaxSound.getRelaxForPosition(paramInt);
    ((ViewHolder)localObject).text.setText(paramViewGroup.getName());
    ((ViewHolder)localObject).shape.setImageResource(paramViewGroup.getResDrawable());
    if (this.index == paramInt) {
      ((ViewHolder)localObject).wrapperShape.setBackgroundResource(2131296355);
    }
    for (;;)
    {
      return paramView;
      ((ViewHolder)localObject).wrapperShape.setBackgroundResource(2131296329);
    }
  }
  
  public void selectItem(int paramInt)
  {
    this.index = paramInt;
    notifyDataSetChanged();
  }
  
  static class ViewHolder
  {
    public ImageView shape;
    public GroteskLight text;
    public RelativeLayout wrapperShape;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\adapters\MeditationAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */