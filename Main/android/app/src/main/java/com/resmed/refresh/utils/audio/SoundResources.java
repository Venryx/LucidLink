package com.resmed.refresh.utils.audio;

import com.resmed.refresh.ui.uibase.app.RefreshApplication;

import android.content.res.*;

public class SoundResources
{
	public enum RelaxSound {
		RELAX_AIR(2131165846, "Relax/Air.wav", 2131099667, 2130837799),
		RELAX_DRIFT(2131165847, "Relax/Drift.wav", 2131099668, 2130837797),
		RELAX_ETHEREAL(2131165848, "Relax/Ethereal.wav", 2131099669, 2130837800),
		RELAX_MOONLIGHT(2131165849, "Relax/Moonlight.wav", 2131099670, 2130837798),
		RELAX_OCEAN(2131165850, "Relax/Ocean.wav", 2131099671, 2130837799),
		RELAX_SHORELINE(2131165851, "Relax/Shoreline.wav", 2131099672, 2130837797),
		RELAX_SKY(2131165852, "Relax/Sky.wav", 2131099673, 2130837800),
		RELAX_TRANQUILITY(2131165853, "Relax/Tranquility.wav", 2131099674, 2130837798),
		RELAX_TWILIGHT(2131165854, "Relax/Twilight.wav", 2131099675, 2130837799);

		private String fileName;
		private int id;
		private int resDrawable;
		private int resName;

		static {
			RelaxSound[] var0 = new RelaxSound[]{RELAX_AIR, RELAX_DRIFT, RELAX_ETHEREAL, RELAX_MOONLIGHT, RELAX_OCEAN, RELAX_SHORELINE, RELAX_SKY, RELAX_TRANQUILITY, RELAX_TWILIGHT};
		}

		private RelaxSound(int var3, String var4, int var5, int var6) {
			this.resName = var3;
			this.resDrawable = var6;
			this.fileName = var4;
			this.id = var5;
		}

		public static RelaxSound getRelaxForId(int var0) {
			switch(var0) {
				case 2131099667:
					return RELAX_AIR;
				case 2131099668:
					return RELAX_DRIFT;
				case 2131099669:
					return RELAX_ETHEREAL;
				case 2131099670:
					return RELAX_MOONLIGHT;
				case 2131099671:
					return RELAX_OCEAN;
				case 2131099672:
					return RELAX_SHORELINE;
				case 2131099673:
					return RELAX_SKY;
				case 2131099674:
					return RELAX_TRANQUILITY;
				case 2131099675:
					return RELAX_TWILIGHT;
				default:
					return RELAX_AIR;
			}
		}

		public static RelaxSound getRelaxForPosition(int var0) {
			switch(var0) {
				case 0:
					return RELAX_AIR;
				case 1:
					return RELAX_DRIFT;
				case 2:
					return RELAX_ETHEREAL;
				case 3:
					return RELAX_MOONLIGHT;
				case 4:
					return RELAX_OCEAN;
				case 5:
					return RELAX_SHORELINE;
				case 6:
					return RELAX_SKY;
				case 7:
					return RELAX_TRANQUILITY;
				case 8:
					return RELAX_TWILIGHT;
				default:
					return RELAX_AIR;
			}
		}

		public AssetFileDescriptor getAssetFileDescriptor() {
			// $FF: Couldn't be decompiled
			return null;
		}

		public int getId() {
			return this.id;
		}

		public String getName() {
			return RefreshApplication.getInstance().getString(this.resName);
		}

		public int getResDrawable() {
			return this.resDrawable;
		}
	}


}
public enum SmartAlarmSound {
	SMART_DAYBREAK(2131166075, "SmartAlarm/DaybreakLoop30sec.wav", 2131099661),
	SMART_DEFAULT(2131166073, "SmartAlarm/DefaultAlarmSound.wav", 2131099660),
	SMART_DUO(2131166076, "SmartAlarm/DuoLoop30sec.wav", 2131099662),
	SMART_FOREST(2131166077, "SmartAlarm/ForestLoop30sec.wav", 2131099663),
	SMART_MEADOW(2131166078, "SmartAlarm/MeadowLoop30sec.wav", 2131099664),
	SMART_SUNRISE(2131166079, "SmartAlarm/SunriseLoop30sec.wav", 2131099665),
	SMART_TEMPO(2131166074, "SmartAlarm/TempoLoop30sec.wav", 2131099666);

	private String fileName;
	private int id;
	private int resName;

	static {
		SmartAlarmSound[] var0 = new SmartAlarmSound[]{SMART_DEFAULT, SMART_DAYBREAK, SMART_DUO, SMART_FOREST, SMART_MEADOW, SMART_SUNRISE, SMART_TEMPO};
	}

	private SmartAlarmSound(int var3, String var4, int var5) {
		this.resName = var3;
		this.fileName = var4;
		this.id = var5;
	}

	public static SmartAlarmSound getSmartAlarmForId(int var0) {
		switch(var0) {
			case 2131099660:
				return SMART_DEFAULT;
			case 2131099661:
				return SMART_DAYBREAK;
			case 2131099662:
				return SMART_DUO;
			case 2131099663:
				return SMART_FOREST;
			case 2131099664:
				return SMART_MEADOW;
			case 2131099665:
				return SMART_SUNRISE;
			case 2131099666:
				return SMART_TEMPO;
			default:
				return SMART_DEFAULT;
		}
	}

	public static SmartAlarmSound getSmartAlarmForPosition(int var0) {
		switch(var0) {
			case 0:
				return SMART_DEFAULT;
			case 1:
				return SMART_DAYBREAK;
			case 2:
				return SMART_DUO;
			case 3:
				return SMART_FOREST;
			case 4:
				return SMART_MEADOW;
			case 5:
				return SMART_SUNRISE;
			case 6:
				return SMART_TEMPO;
			default:
				return SMART_DEFAULT;
		}
	}

	public AssetFileDescriptor getAssetFileDescriptor() {
		// $FF: Couldn't be decompiled
		return null;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return RefreshApplication.getInstance().getString(this.resName);
	}
}
