package com.resmed.refresh.model.graphs;

import com.resmed.refresh.model.RST_SleepSessionInfo.SleepSessionUserState;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;

public enum RRHypnoBar {
	// $FF: synthetic field
	private static int[] $SWITCH_TABLE$com$resmed$refresh$model$RST_SleepSessionInfo$SleepSessionUserState;
	// $FF: synthetic field
	private static int[] $SWITCH_TABLE$com$resmed$refresh$model$graphs$RRHypnoBar;
	KRRSleepStateBarLevelDeep(3),
	KRRSleepStateBarLevelLight(2),
	KRRSleepStateBarLevelOther(0),
	KRRSleepStateBarLevelREM(1),
	KRRSleepStateBarLevelWake(-1);

	private int value;

	// $FF: synthetic method
	static int[] $SWITCH_TABLE$com$resmed$refresh$model$RST_SleepSessionInfo$SleepSessionUserState() {
		int[] var0 = $SWITCH_TABLE$com$resmed$refresh$model$RST_SleepSessionInfo$SleepSessionUserState;
		if(var0 != null) {
			return var0;
		} else {
			int[] var1 = new int[SleepSessionUserState.values().length];

			try {
				var1[SleepSessionUserState.ksleepSessionUserStateAbsent.ordinal()] = 2;
			} catch (NoSuchFieldError var15) {
				;
			}

			try {
				var1[SleepSessionUserState.ksleepSessionUserStateBreak.ordinal()] = 4;
			} catch (NoSuchFieldError var14) {
				;
			}

			try {
				var1[SleepSessionUserState.ksleepSessionUserStateDeepSleep.ordinal()] = 6;
			} catch (NoSuchFieldError var13) {
				;
			}

			try {
				var1[SleepSessionUserState.ksleepSessionUserStateLightSleep.ordinal()] = 5;
			} catch (NoSuchFieldError var12) {
				;
			}

			try {
				var1[SleepSessionUserState.ksleepSessionUserStateRemSleep.ordinal()] = 7;
			} catch (NoSuchFieldError var11) {
				;
			}

			try {
				var1[SleepSessionUserState.ksleepSessionUserStateUnknown.ordinal()] = 3;
			} catch (NoSuchFieldError var10) {
				;
			}

			try {
				var1[SleepSessionUserState.ksleepSessionUserStateWake.ordinal()] = 1;
			} catch (NoSuchFieldError var9) {
				;
			}

			$SWITCH_TABLE$com$resmed$refresh$model$RST_SleepSessionInfo$SleepSessionUserState = var1;
			return var1;
		}
	}

	// $FF: synthetic method
	static int[] $SWITCH_TABLE$com$resmed$refresh$model$graphs$RRHypnoBar() {
		int[] var0 = $SWITCH_TABLE$com$resmed$refresh$model$graphs$RRHypnoBar;
		if(var0 != null) {
			return var0;
		} else {
			int[] var1 = new int[values().length];

			try {
				var1[KRRSleepStateBarLevelDeep.ordinal()] = 5;
			} catch (NoSuchFieldError var11) {
				;
			}

			try {
				var1[KRRSleepStateBarLevelLight.ordinal()] = 4;
			} catch (NoSuchFieldError var10) {
				;
			}

			try {
				var1[KRRSleepStateBarLevelOther.ordinal()] = 1;
			} catch (NoSuchFieldError var9) {
				;
			}

			try {
				var1[KRRSleepStateBarLevelREM.ordinal()] = 3;
			} catch (NoSuchFieldError var8) {
				;
			}

			try {
				var1[KRRSleepStateBarLevelWake.ordinal()] = 2;
			} catch (NoSuchFieldError var7) {
				;
			}

			$SWITCH_TABLE$com$resmed$refresh$model$graphs$RRHypnoBar = var1;
			return var1;
		}
	}

	static {
		RRHypnoBar[] var0 = new RRHypnoBar[]{KRRSleepStateBarLevelOther, KRRSleepStateBarLevelWake, KRRSleepStateBarLevelREM, KRRSleepStateBarLevelLight, KRRSleepStateBarLevelDeep};
	}

	private RRHypnoBar(int var3) {
		this.value = var3;
	}

	public static RRHypnoBar fromSleepSessionState(SleepSessionUserState var0) {
		RRHypnoBar var10000 = KRRSleepStateBarLevelOther;
		switch($SWITCH_TABLE$com$resmed$refresh$model$RST_SleepSessionInfo$SleepSessionUserState()[var0.ordinal()]) {
			case 1:
				return KRRSleepStateBarLevelWake;
			case 2:
			case 3:
			case 4:
			default:
				return KRRSleepStateBarLevelOther;
			case 5:
				return KRRSleepStateBarLevelLight;
			case 6:
				return KRRSleepStateBarLevelDeep;
			case 7:
				return KRRSleepStateBarLevelREM;
		}
	}

	public static RRHypnoBar fromValue(int var0) {
		RRHypnoBar var10000 = KRRSleepStateBarLevelOther;
		switch(var0) {
			case 1:
				return KRRSleepStateBarLevelREM;
			case 2:
				return KRRSleepStateBarLevelLight;
			case 3:
				return KRRSleepStateBarLevelDeep;
			default:
				return KRRSleepStateBarLevelWake;
		}
	}

	public static int getColor(RRHypnoBar var0, boolean var1) {
		switch($SWITCH_TABLE$com$resmed$refresh$model$graphs$RRHypnoBar()[var0.ordinal()]) {
			case 2:
				if(var1) {
					return RefreshApplication.getInstance().getResources().getColor(2131296348);
				}

				return RefreshApplication.getInstance().getResources().getColor(2131296349);
			case 3:
				return RefreshApplication.getInstance().getResources().getColor(2131296350);
			case 4:
				return RefreshApplication.getInstance().getResources().getColor(2131296351);
			case 5:
				return RefreshApplication.getInstance().getResources().getColor(2131296352);
			default:
				return RefreshApplication.getInstance().getResources().getColor(2131296315);
		}
	}

	public float getVal() {
		return this.value == -1?-0.5F:(float)this.value;
	}
}
