package com.resmed.edflib;

import java.io.File;

public interface FileEdfInterface {
    int closeFile();

    int compressFile(String str);

    boolean deleteFile();

    int fixEdfFile();

    File getFile();

    String getFilePath();

    String getRM60LibVersion();

    int openFileForMode(String str);

    int readDigitalSamples();

    int writeDigitalSamples(int[] iArr, int[] iArr2);
}
