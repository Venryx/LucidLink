package com.resmed.edflib;

import java.io.*;
import android.content.*;

public class EdfFileManager implements FileEdfInterface
{
	private EdfLibJNI edfLibJNI;
	private String filePath;
	private String[] pMeta;

	public EdfFileManager(final File file, final String s, final String[] pMeta, final EdfLibCallbackHandler edfLibCallbackHandler, final Context context) {
		this.edfLibJNI = new EdfLibJNI(file, edfLibCallbackHandler, context);
		this.filePath = String.valueOf(file.getAbsolutePath()) + "/" + s;
		this.pMeta = pMeta;
	}

	public EdfFileManager(final String filePath, final String[] pMeta, final EdfLibCallbackHandler edfLibCallbackHandler, final Context context) {
		this.edfLibJNI = new EdfLibJNI((File)null, edfLibCallbackHandler, context);
		this.filePath = filePath;
		this.pMeta = pMeta;
	}

	public int closeFile() {
		synchronized (this) {
			return this.edfLibJNI.closeFileEdf(this.filePath, this.pMeta);
		}
	}

	public int compressFile(final String s) {
		synchronized (this) {
			return this.edfLibJNI.compressFile(this.filePath, s);
		}
	}

	public boolean deleteFile() {
		synchronized (this) {
			return new File(this.filePath).delete();
		}
	}

	public int fixEdfFile() {
		synchronized (this) {
			return this.edfLibJNI.fixEdfFile(this.filePath);
		}
	}

	public File getFile() {
		synchronized (this) {
			return new File(this.filePath);
		}
	}

	public String getFilePath() {
		return this.filePath;
	}

	public String getRM60LibVersion() {
		return this.edfLibJNI.getLibVersion();
	}

	public int openFileForMode(final String s) {
		synchronized (this) {
			return this.edfLibJNI.openFileForMode(this.filePath, this.pMeta, s);
		}
	}

	public int readDigitalSamples() {
		synchronized (this) {
			return this.edfLibJNI.readDigitalSamples(this.filePath);
		}
	}

	public int writeDigitalSamples(final int[] array, final int[] array2) {
		synchronized (this) {
			final RstEdfMetaData rstEdfMetaData = new RstEdfMetaData();
			rstEdfMetaData.addMetaField(RstEdfMetaData.Enum_EDF_Meta.autoStop, "no");
			return this.edfLibJNI.writeDigitalSamples(this.filePath, array, array2, rstEdfMetaData.toArray());
		}
	}
}
