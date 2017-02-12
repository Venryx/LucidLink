package com.resmed.refresh.model;

import android.content.*;
import java.util.*;

public class RST_SeriesItem {
	public enum RST_GraphParameter {
		kGraphParameterBodyScore,
		kGraphParameterDeepSleep,
		kGraphParameterLightSleep,
		kGraphParameterMindScore,
		kGraphParameterNone,
		kGraphParameterRemSleep,
		kGraphParameterSleepScore;

		static {
			RST_GraphParameter[] var0 = new RST_GraphParameter[]{kGraphParameterSleepScore, kGraphParameterMindScore, kGraphParameterBodyScore, kGraphParameterRemSleep, kGraphParameterDeepSleep, kGraphParameterLightSleep, kGraphParameterNone};
		}
	}


	private int maxValue;
	private int minValue;
	private ArrayList<Integer> xSeries;
	private ArrayList<Integer> ySeries;

	static int[] $SWITCH_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter;
	static /* synthetic */ int[] $SWITCH_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter() {
		final int[] $switch_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter = RST_SeriesItem.$SWITCH_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter;
		if ($switch_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter != null) {
			return $switch_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter;
		}
		final int[] $switch_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter2 = new int[RST_SeriesItem.RST_GraphParameter.values().length];
		while (true) {
			try {
				$switch_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter2[RST_SeriesItem.RST_GraphParameter.kGraphParameterBodyScore.ordinal()] = 3;
				try {
					$switch_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter2[RST_SeriesItem.RST_GraphParameter.kGraphParameterDeepSleep.ordinal()] = 5;
					try {
						$switch_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter2[RST_SeriesItem.RST_GraphParameter.kGraphParameterLightSleep.ordinal()] = 6;
						try {
							$switch_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter2[RST_SeriesItem.RST_GraphParameter.kGraphParameterMindScore.ordinal()] = 2;
							try {
								$switch_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter2[RST_SeriesItem.RST_GraphParameter.kGraphParameterNone.ordinal()] = 7;
								try {
									$switch_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter2[RST_SeriesItem.RST_GraphParameter.kGraphParameterRemSleep.ordinal()] = 4;
									try {
										$switch_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter2[RST_SeriesItem.RST_GraphParameter.kGraphParameterSleepScore.ordinal()] = 1;
										return RST_SeriesItem.$SWITCH_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter = $switch_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter2;
									}
									catch (NoSuchFieldError noSuchFieldError) {}
								}
								catch (NoSuchFieldError noSuchFieldError2) {}
							}
							catch (NoSuchFieldError noSuchFieldError3) {}
						}
						catch (NoSuchFieldError noSuchFieldError4) {}
					}
					catch (NoSuchFieldError noSuchFieldError5) {}
				}
				catch (NoSuchFieldError noSuchFieldError6) {}
			}
			catch (NoSuchFieldError noSuchFieldError7) {
				continue;
			}
			break;
		}
		return RST_SeriesItem.$SWITCH_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter;
	}

	public RST_SeriesItem() {
		this.minValue = 100;
		this.maxValue = -100;
	}

	public static String labelForType(final Context context, final RST_SeriesItem.RST_GraphParameter rst_GraphParameter) {
		switch ($SWITCH_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter()[rst_GraphParameter.ordinal()]) {
			default: {
				return "";
			}
			case 1: {
				return context.getString(2131165367);
			}
			case 2: {
				return context.getString(2131165368);
			}
			case 3: {
				return context.getString(2131165369);
			}
			case 4: {
				return context.getString(2131165370);
			}
			case 5: {
				return context.getString(2131165371);
			}
			case 6: {
				return context.getString(2131165372);
			}
		}
	}

	public int getMaxValue() {
		return this.maxValue;
	}

	public int getMinValue() {
		return this.minValue;
	}

	public ArrayList<Integer> getxSeries() {
		return this.xSeries;
	}

	public ArrayList<Integer> getySeries() {
		return this.ySeries;
	}

	public void populateSeries(final RST_SeriesItem.RST_GraphParameter rst_GraphParameter, final List<RST_SleepSessionInfo> list) {
		int n = 0;
		int n2 = 0;
		this.xSeries = new ArrayList<Integer>(list.size());
		this.ySeries = new ArrayList<Integer>(list.size());
		for (final RST_SleepSessionInfo rst_SleepSessionInfo : list) {
			Label_0142: {
				switch ($SWITCH_TABLE$com$resmed$refresh$model$RST_SeriesItem$RST_GraphParameter()[rst_GraphParameter.ordinal()]) {
					default: {
						n = 0;
						break Label_0142;
					}
					case 1: {
						n = rst_SleepSessionInfo.getSleepScore();
						break Label_0142;
					}
					case 2: {
						n = rst_SleepSessionInfo.getMindScore();
						break Label_0142;
					}
					case 3: {
						n = rst_SleepSessionInfo.getBodyScore();
					}
					case 4:
					case 5:
					case 6: {
						if (n < this.minValue) {
							this.minValue = n;
						}
						if (n > this.maxValue) {
							this.maxValue = n;
						}
						this.ySeries.add(n);
						final ArrayList<Integer> xSeries = this.xSeries;
						final int n3 = n2 + 1;
						xSeries.add(n2);
						n2 = n3;
						continue;
					}
				}
			}
		}
		if (list.size() == 1) {
			this.minValue = 0;
			this.maxValue = n + 10;
		}
	}

	public void setMaxValue(final int maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(final int minValue) {
		this.minValue = minValue;
	}

	public void setxSeries(final ArrayList<Integer> xSeries) {
		this.xSeries = xSeries;
	}

	public void setySeries(final ArrayList<Integer> ySeries) {
		this.ySeries = ySeries;
	}
}
