package com.resmed.edflib;

public interface EdfLibCallbackHandler {
    void onDigitalSamplesRead(int[] iArr, int[] iArr2);

    void onFileClosed();

    void onFileCompressed(int i);

    void onFileFixed();

    void onFileOpened();

    void onWroteDigitalSamples();

    void onWroteMetadata();
}
