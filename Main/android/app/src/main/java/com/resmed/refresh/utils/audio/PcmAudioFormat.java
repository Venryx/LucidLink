package com.resmed.refresh.utils.audio;

public class PcmAudioFormat
{
  protected final boolean bigEndian;
  private final int bytesRequiredPerSample;
  private final int channels;
  private final int sampleRate;
  private final int sampleSizeInBits;
  private final boolean signed;
  
  protected PcmAudioFormat(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramInt1 < 1) {
      throw new IllegalArgumentException("sampleRate cannot be less than one. But it is:" + paramInt1);
    }
    this.sampleRate = paramInt1;
    if ((paramInt2 < 2) || (paramInt2 > 31)) {
      throw new IllegalArgumentException("sampleSizeInBits must be between (including) 2-31. But it is:" + paramInt2);
    }
    this.sampleSizeInBits = paramInt2;
    if ((paramInt3 < 1) || (paramInt3 > 2)) {
      throw new IllegalArgumentException("channels must be 1 or 2. But it is:" + paramInt3);
    }
    this.channels = paramInt3;
    this.bigEndian = paramBoolean1;
    this.signed = paramBoolean2;
    if (paramInt2 % 8 == 0) {}
    for (this.bytesRequiredPerSample = (paramInt2 / 8);; this.bytesRequiredPerSample = (paramInt2 / 8 + 1)) {
      return;
    }
  }
  
  public int calculateSampleIndex(double paramDouble)
  {
    if (paramDouble < 0.0D) {
      throw new IllegalArgumentException("Time information cannot be negative.");
    }
    int j = (int)(this.sampleRate * paramDouble * this.bytesRequiredPerSample);
    int i = j;
    if (j % this.bytesRequiredPerSample != 0) {
      i = j + (this.bytesRequiredPerSample - j % this.bytesRequiredPerSample);
    }
    return i;
  }
  
  public double calculateSampleTime(int paramInt)
  {
    if (paramInt < 0) {
      throw new IllegalArgumentException("sampleIndex information cannot be negative:" + paramInt);
    }
    return paramInt / this.sampleRate;
  }
  
  public int getBytePerSample()
  {
    return this.bytesRequiredPerSample;
  }
  
  public int getChannels()
  {
    return this.channels;
  }
  
  public int getSampleRate()
  {
    return this.sampleRate;
  }
  
  public int getSampleSizeInBits()
  {
    return this.sampleSizeInBits;
  }
  
  public boolean isBigEndian()
  {
    return this.bigEndian;
  }
  
  public boolean isSigned()
  {
    return this.signed;
  }
  
  PcmAudioFormat mono16BitSignedLittleEndian(int paramInt)
  {
    return new PcmAudioFormat(paramInt, 16, 1, false, true);
  }
  
  public String toString()
  {
    return "[ Sample Rate:" + this.sampleRate + " , SampleSizeInBits:" + this.sampleSizeInBits + ", channels:" + this.channels + ", signed:" + this.signed + ", bigEndian:" + this.bigEndian + " ]";
  }
  
  public static class Builder
  {
    private boolean _bigEndian = false;
    private int _channels = 1;
    private int _sampleRate;
    private int _sampleSizeInBits = 16;
    private boolean _signed = true;
    
    public Builder(int paramInt)
    {
      this._sampleRate = paramInt;
    }
    
    public Builder bigEndian()
    {
      this._bigEndian = true;
      return this;
    }
    
    public PcmAudioFormat build()
    {
      return new PcmAudioFormat(this._sampleRate, this._sampleSizeInBits, this._channels, this._bigEndian, this._signed);
    }
    
    public Builder channels(int paramInt)
    {
      this._channels = paramInt;
      return this;
    }
    
    public Builder sampleSizeInBits(int paramInt)
    {
      this._sampleSizeInBits = paramInt;
      return this;
    }
    
    public Builder unsigned()
    {
      this._signed = false;
      return this;
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */