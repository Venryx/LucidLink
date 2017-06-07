package com.resmed.edflib;

public class RstEdfSigParams {
    int digMax;
    int digMin;
    String label;
    String physDim;
    double physMax;
    double physMin;
    int sampFreq;

    public abstract class PhysicDimension {
        public static final String BPM = "bpm";
        public static final String MA = "mA";
        public static final String UV = "uV";
    }
}
