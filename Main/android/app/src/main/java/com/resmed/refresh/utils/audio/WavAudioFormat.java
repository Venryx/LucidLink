package com.resmed.refresh.utils.audio;

public class WavAudioFormat
  extends PcmAudioFormat
{
  protected final boolean bigEndian = false;
  
  private WavAudioFormat(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    super(paramInt1, paramInt2, paramInt3, false, paramBoolean);
  }
  
  public static WavAudioFormat mono16Bit(int paramInt)
  {
    return new WavAudioFormat(paramInt, 16, 1, true);
  }
  
  public static WavAudioFormat wavFormat(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt2 == 8) {}
    for (WavAudioFormat localWavAudioFormat = new WavAudioFormat(paramInt1, paramInt2, paramInt3, false);; localWavAudioFormat = new WavAudioFormat(paramInt1, paramInt2, paramInt3, true)) {
      return localWavAudioFormat;
    }
  }
  
  public static class Builder
  {
    private int _channels = 1;
    private int _sampleRate;
    private int _sampleSizeInBits = 16;
    
    public WavAudioFormat build()
    {
      if (this._sampleSizeInBits == 8) {}
      for (WavAudioFormat localWavAudioFormat = new WavAudioFormat(this._sampleRate, this._sampleSizeInBits, this._channels, false, null);; localWavAudioFormat = new WavAudioFormat(this._sampleRate, this._sampleSizeInBits, this._channels, true, null)) {
        return localWavAudioFormat;
      }
    }
    
    public Builder channels(int paramInt)
    {
      this._channels = paramInt;
      return this;
    }
    
    public Builder sampleRate(int paramInt)
    {
      this._sampleRate = paramInt;
      return this;
    }
    
    public Builder sampleSizeInBits(int paramInt)
    {
      this._sampleSizeInBits = paramInt;
      return this;
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */