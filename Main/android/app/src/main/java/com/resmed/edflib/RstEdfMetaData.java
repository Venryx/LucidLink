package com.resmed.edflib;

public class RstEdfMetaData
{
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