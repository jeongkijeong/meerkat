package com.mlog.meerkat.tcp.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class AbstractHandler implements Runnable {
	private BlockingQueue<Object> queue = null;
	
	int queueSize = 1000;

	public AbstractHandler() {
		this(1000);
	}

	public AbstractHandler(int size) {
		queueSize = size;

		queue = new ArrayBlockingQueue<Object>(queueSize);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Object object = queue.poll(3, TimeUnit.SECONDS);
				handler(object);
			} catch (Exception e) {
			}
		}
	}

	public void put(String string) {
		try {
			queue.put(string);
		} catch (Exception e) {
		}
	}

	public abstract void handler(Object object);
}
