package com.domeke.app.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class TimeKit {

	private static LocalDate now = LocalDate.now();

	/**
	 * 获取上周第一天日期
	 * @return
	 */
	public static LocalDate getLastWeekStart() {
		return firstDayOfWeek(now.minusWeeks(1));
	}

	/**
	 * 获取下周最后一天日期
	 * @return
	 */
	public static LocalDate getLastWeekEnd() {
		return lastDayOfWeek(now.minusWeeks(1));
	}

	/**
	 * 获取本周第一天日期
	 * @return
	 */
	public static LocalDate getCurrernWeekStart() {
		return firstDayOfWeek(now);
	}

	/**
	 * 获取本周最后一天日期
	 * @return
	 */
	public static LocalDate getCurrernWeekEnd() {
		return lastDayOfWeek(now);
	}

	public static LocalDate lastDayOfWeek(LocalDate date) {
		return date.with(DayOfWeek.SUNDAY);
	}

	public static LocalDate firstDayOfWeek(LocalDate date) {
		return date.with(DayOfWeek.MONDAY);
	}

	public static void main(String[] args) {
		LocalDate lastWeekStart = getCurrernWeekStart();
		LocalDate lastWeekEnd = getCurrernWeekEnd();
		System.out.println("now==" + now);
		System.out.println("lastWeekStart==" + lastWeekStart);
		System.out.println("lastWeekEnd==" + lastWeekEnd);

		if (now.isAfter(lastWeekStart) && now.isBefore(lastWeekEnd)) {
			System.out.println("true");
		}

	}

	public static boolean isBetween(LocalDate now, LocalDate start, LocalDate end) {
		if (now.isEqual(start)) {
			return true;
		}

		if (now.isEqual(end)) {
			return true;
		}

		if (now.isAfter(end) && now.isBefore(start)) {
			return true;
		}

		return false;

	}

}
