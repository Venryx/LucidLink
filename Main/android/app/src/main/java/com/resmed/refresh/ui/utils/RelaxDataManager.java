package com.resmed.refresh.ui.utils;

import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.utils.audio.SoundResources.RelaxSound;

public class RelaxDataManager
{
  private static RelaxDataManager relaxDataManager;
  private RefreshModelController refreshModelController;
  private boolean relaxSoundActive;
  private int relaxSoundSelected;
  
  public static RelaxDataManager getInstance()
  {
    try
    {
      if (relaxDataManager == null)
      {
        localRelaxDataManager = new com/resmed/refresh/ui/utils/RelaxDataManager;
        localRelaxDataManager.<init>();
        relaxDataManager = localRelaxDataManager;
        relaxDataManager.init();
      }
      RelaxDataManager localRelaxDataManager = relaxDataManager;
      return localRelaxDataManager;
    }
    finally {}
  }
  
  private void init()
  {
    this.refreshModelController = RefreshModelController.getInstance();
    this.relaxSoundActive = this.refreshModelController.getActiveRelax();
    this.relaxSoundSelected = this.refreshModelController.getRelaxSound().intValue();
    if (this.relaxSoundSelected == -1) {
      this.relaxSoundSelected = 2131099667;
    }
  }
  
  public boolean getActiveRelax()
  {
    return this.relaxSoundActive;
  }
  
  public SoundResources.RelaxSound getRelaxSound()
  {
    return SoundResources.RelaxSound.getRelaxForId(this.relaxSoundSelected);
  }
  
  public void setActiveRelax(boolean paramBoolean)
  {
    this.refreshModelController.storeActiveRelax(paramBoolean);
    this.relaxSoundActive = paramBoolean;
  }
  
  public void setRelaxSound(int paramInt)
  {
    this.relaxSoundSelected = paramInt;
    this.refreshModelController.storeRelaxSound(Integer.valueOf(this.relaxSoundSelected));
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\utils\RelaxDataManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */