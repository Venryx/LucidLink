package com.resmed.edflib;

public class RstEdfMetaData {
	public enum Enum_EDF_Meta {
		age,
		ahi,
		ahiA,
		ahiAlgver,
		ahiH,
		algorithmVer,
		autoStop,
		avhAlgver,
		bodyScore,
		crrAv,
		crrSd,
		csaPercent,
		csrAlgver,
		csrPercent,
		devRecId,
		firmwareVer,
		hardwareVer,
		hcpId,
		index1,
		index2,
		index3,
		indexAlgVer1,
		indexAlgVer2,
		indexAlgVer3,
		mindScore,
		mobileAppVer,
		noInterruptions,
		osaPercent,
		ovcAlgver,
		patientId,
		rawLocn,
		recLength,
		recordingDtg,
		recordingId,
		rm20LibVer,
		rstEdfLibVer,
		scheduleRec,
		sensorSerial,
		sleepAlgver,
		sleepEfficiency,
		sleepLength,
		sleepOnset,
		sleepScore,
		sleepVsNormal,
		startDateandTime,
		sumAwake,
		sumDeepSleep,
		sumLightSleep,
		sumRemSleep,
		timeToSleep,
		unitId,
		unitType,
		unitVer,
		unkPercent,
		userId,
		utcOffset;

		static {
			Enum_EDF_Meta[] var0 = new Enum_EDF_Meta[]{startDateandTime, hardwareVer, sensorSerial, firmwareVer, mobileAppVer, algorithmVer, rm20LibVer, rstEdfLibVer, recordingId, devRecId, unitId, unitType, unitVer, userId, rawLocn, hcpId, patientId, recordingDtg, utcOffset, scheduleRec, autoStop, recLength, sleepLength, timeToSleep, noInterruptions, sleepVsNormal, age, sleepOnset, sleepEfficiency, sumAwake, sumLightSleep, sumDeepSleep, sumRemSleep, sleepScore, mindScore, bodyScore, sleepAlgver, crrAv, crrSd, ahi, ahiAlgver, csrPercent, csrAlgver, ahiA, ahiH, avhAlgver, osaPercent, csaPercent, unkPercent, ovcAlgver, index1, indexAlgVer1, index2, indexAlgVer2, index3, indexAlgVer3};
		}
	}

	public class ValueTooLengthyException extends Exception
	{
		private static final long serialVersionUID = 4310055423368283026L;

		public ValueTooLengthyException(final RstEdfMetaData this$0, final String s) {
			super(s);
		}
	}



	private static final int META_FIELD_SIZE = 21;
	private String[] metaData;

	public RstEdfMetaData() {
		this.metaData = new String[56];
	}

	public void addMetaField(final RstEdfMetaData.Enum_EDF_Meta enum_EDF_Meta, final String s) {
		if (21 < s.length()) {
			new RstEdfMetaData.ValueTooLengthyException(this, "Bigger than :21").printStackTrace();
		}
		this.metaData[enum_EDF_Meta.ordinal()] = s;
	}

	public String[] toArray() {
		return this.metaData;
	}
}