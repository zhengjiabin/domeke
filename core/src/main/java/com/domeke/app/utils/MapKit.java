package com.domeke.app.utils;

import java.util.Map;

public class MapKit {

	public static <T, M> boolean isBlank(Map<T, M> map) {
		return map == null || map.isEmpty() ? true : false;
	}

	public static <T, M> boolean isNotBlank(Map<T, M> map) {
		return !isBlank(map);
	}

	public static <T, M> boolean isNull(Map<T, M> map) {
		return map == null ? true : false;
	}
}
