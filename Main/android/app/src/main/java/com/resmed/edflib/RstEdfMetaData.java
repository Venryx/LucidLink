package com.resmed.edflib;

public class RstEdfMetaData {
    private static final int META_FIELD_SIZE = 21;
    private String[] metaData = new String[56];

    public enum Enum_EDF_Meta {
        startDateandTime,
        hardwareVer,
        sensorSerial,
        firmwareVer,
        mobileAppVer,
        algorithmVer,
        rm20LibVer,
        rstEdfLibVer,
        recordingId,
        devRecId,
        unitId,
        unitType,
        unitVer,
        userId,
        rawLocn,
        hcpId,
        patientId,
        recordingDtg,
        utcOffset,
        scheduleRec,
        autoStop,
        recLength,
        sleepLength,
        timeToSleep,
        noInterruptions,
        sleepVsNormal,
        age,
        sleepOnset,
        sleepEfficiency,
        sumAwake,
        sumLightSleep,
        sumDeepSleep,
        sumRemSleep,
        sleepScore,
        mindScore,
        bodyScore,
        sleepAlgver,
        crrAv,
        crrSd,
        ahi,
        ahiAlgver,
        csrPercent,
        csrAlgver,
        ahiA,
        ahiH,
        avhAlgver,
        osaPercent,
        csaPercent,
        unkPercent,
        ovcAlgver,
        index1,
        indexAlgVer1,
        index2,
        indexAlgVer2,
        index3,
        indexAlgVer3
    }

    public class ValueTooLengthyException extends Exception {
        private static final long serialVersionUID = 4310055423368283026L;

        public ValueTooLengthyException(String msg) {
            super(msg);
        }
    }

    public String[] toArray() {
        return this.metaData;
    }

    public void addMetaField(Enum_EDF_Meta field, String value) {
        if (21 < value.length()) {
            new ValueTooLengthyException("Bigger than :21").printStackTrace();
        }
        this.metaData[field.ordinal()] = value;
    }
}
