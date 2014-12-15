package com.domeke.app.Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public abstract class Pool<T> {

	private int threadSize = 2;

	/** 是否关闭线程 */
	private boolean isClose = true;

	/** 所有子线程是否已结束 */
	private boolean isEnd = true;

	/** 子线程计数器 */
	private CountDownLatch countDown;

	private List<Element<T>> elements = Collections.synchronizedList(new ArrayList<Element<T>>());

	public Pool() {

	}

	/**
	 * 
	 * @param threadSize
	 *            线程数
	 */
	public Pool(int threadSize) {
		this.threadSize = threadSize;
	}

	/**
	 * 
	 * @param threadSize
	 *            线程数
	 */
	public Pool(int threadSize, boolean isClose) {
		this.threadSize = threadSize;
		this.isClose = isClose;
	}

	/**
	 * 多线程
	 */
	public void run() {
		isClose = false;
		isEnd = false;
		countDown = new CountDownLatch(threadSize);
		for (int i = 0; i < threadSize; i++) {
			handleThread(countDown);
		}
	}

	/**
	 * 处理线程
	 */
	private void handleThread(CountDownLatch countDown) {
		Runnable runnable = new ElementRunnable(countDown);
		Thread thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * 线程实例
	 */
	private class ElementRunnable implements Runnable {

		private CountDownLatch countDown;

		public ElementRunnable(CountDownLatch countDown) {
			this.countDown = countDown;
		}

		@Override
		public void run() {
			Element<T> element = getElement();
			while (element != null) {
				handleElement(element);
				synchronized (elements) {
					elements.remove(element);
				}
				element = getElement();
			}
			countDown.countDown();
		}
	}

	/** 当前线程处理程序 */
	protected abstract void handleElement(Element<T> element);

	/** 线程组允许完成之后调用 */
	protected abstract void afterRun();

	/**
	 * 获取对象
	 * 
	 * @return
	 */
	private Element<T> getElement() {
		synchronized (elements) {
			for (Element<T> element : elements) {
				if (!element.isUsed()) {
					element.Used();
					return element;
				}
			}
		}
		if (isClose) {
			return null;
		}
		synchronized (elements) {
			try {
				elements.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return getElement();
	}

	/**
	 * 添加对象
	 * 
	 * @param element
	 */
	public void addElement(Element<T> element) {
		synchronized (elements) {
			boolean isEmpty = elements.isEmpty();
			elements.add(element);
			if (!isClose && isEmpty) {
				elements.notifyAll();
			}
		}
	}

	/**
	 * 关闭线程
	 */
	public void close() {
		synchronized (elements) {
			if (!isClose && !isEnd) {
				isClose = true;
				elements.notifyAll();
			}
		}
		try {
			if (!isEnd) {
				countDown.await();
				isEnd = true;
				afterRun();
				elements.clear();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 线程是否已结束
	 */
	protected boolean isEnd() {
		return isEnd;
	}
}
