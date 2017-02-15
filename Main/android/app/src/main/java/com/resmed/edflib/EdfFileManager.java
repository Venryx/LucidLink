package com.resmed.edflib;

import android.content.Context;
import com.resmed.edflib.RstEdfMetaData.Enum_EDF_Meta;
import java.io.File;

public class EdfFileManager implements FileEdfInterface {
    private EdfLibJNI edfLibJNI;
    private String filePath;
    private String[] pMeta;

    public EdfFileManager(File filesFolder, String fileName, String[] pMeta, EdfLibCallbackHandler callbacks, Context context) {
        this.edfLibJNI = new EdfLibJNI(filesFolder, callbacks, context);
        this.filePath = filesFolder.getAbsolutePath() + "/" + fileName;
        this.pMeta = pMeta;
    }

    public EdfFileManager(String fileFullPath, String[] pMeta, EdfLibCallbackHandler callbacks, Context context) {
        this.edfLibJNI = new EdfLibJNI(null, callbacks, context);
        this.filePath = fileFullPath;
        this.pMeta = pMeta;
    }

    public synchronized int writeDigitalSamples(int[] mi_channel_data, int[] mq_channel_data) {
        RstEdfMetaData metaDataObj;
        metaDataObj = new RstEdfMetaData();
        metaDataObj.addMetaField(Enum_EDF_Meta.autoStop, "no");
        return this.edfLibJNI.writeDigitalSamples(this.filePath, mi_channel_data, mq_channel_data, metaDataObj.toArray());
    }

    public synchronized int readDigitalSamples() {
        return this.edfLibJNI.readDigitalSamples(this.filePath);
    }

    public synchronized int compressFile(String outputFile) {
        return this.edfLibJNI.compressFile(this.filePath, outputFile);
    }

    public synchronized int openFileForMode(String mode) {
        return this.edfLibJNI.openFileForMode(this.filePath, this.pMeta, mode);
    }

    public synchronized int closeFile() {
        return this.edfLibJNI.closeFileEdf(this.filePath, this.pMeta);
    }

    public synchronized boolean deleteFile() {
        return new File(this.filePath).delete();
    }

    public synchronized File getFile() {
        return new File(this.filePath);
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getRM60LibVersion() {
        return this.edfLibJNI.getLibVersion();
    }

    public synchronized int fixEdfFile() {
        return this.edfLibJNI.fixEdfFile(this.filePath);
    }
}
