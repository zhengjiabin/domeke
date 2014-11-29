package com.domeke.app.Schedule;

public class Element<T> {
	private T key;
	/** 是否被使用 */
	private boolean isUsed = false;

	public Element(T key) {
		this.key = key;
	}

	public void Used() {
		this.isUsed = true;
	}

	public boolean isUsed() {
		return this.isUsed;
	}

	public T getElement() {
		return this.key;
	}
}
