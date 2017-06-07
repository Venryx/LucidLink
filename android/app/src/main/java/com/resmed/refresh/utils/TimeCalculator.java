package com.resmed.refresh.utils;

import java.util.Calendar;

public class TimeCalculator {
	private static final Integer SECONDS_PER_DAY = Integer.valueOf(86400);
	private static final Integer SECONDS_PER_HOUR = Integer.valueOf(3600);
	private static final Integer SECONDS_PER_MINUTE = Integer.valueOf(60);

	public static Integer calculateRemainingDaySeconds() {
		int i = getTodaySecondsSpent().intValue();
		return Integer.valueOf(SECONDS_PER_DAY.intValue() - i);
	}

	public static Integer getTodaySecondsSpent() {
		Calendar localCalendar = Calendar.getInstance();
		int i = localCalendar.get(11);
		int k = localCalendar.get(12);
		int j = localCalendar.get(13);
		return Integer.valueOf(SECONDS_PER_HOUR.intValue() * i + SECONDS_PER_MINUTE.intValue() * k + j);
	}

	public static Calendar rescheduleNextAlarm(final long timeInMillis, final int n) {
		final Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(timeInMillis);
		instance.add(5, 1);
		switch (n) {
			case 2131099658: {
				final int value = instance.get(7);
				if (value == 1) {
					instance.add(5, 1);
					break;
				}
				if (value == 7) {
					instance.add(5, 2);
					break;
				}
				break;
			}
		}
		instance.set(14, 0);
		instance.set(13, 0);
		return instance;
	}

	public static Calendar setCurrentAlarm(final long timeInMillis, final int n) {
		final Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(timeInMillis);
		final Calendar instance2 = Calendar.getInstance();
		instance2.setTimeInMillis(System.currentTimeMillis());
		if (instance2.getTimeInMillis() > instance.getTimeInMillis()) {
			instance.set(instance2.get(1), instance2.get(2), instance2.get(5));
		}
		if (instance2.getTimeInMillis() > instance.getTimeInMillis()) {
			instance.add(5, 1);
		}
		switch (n) {
			case 2131099658: {
				final int value = instance.get(7);
				if (value == 1) {
					instance.add(5, 1);
					break;
				}
				if (value == 7) {
					instance.add(5, 2);
					break;
				}
				break;
			}
		}
		instance.set(14, 0);
		instance.set(13, 0);
		return instance;
	}
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */