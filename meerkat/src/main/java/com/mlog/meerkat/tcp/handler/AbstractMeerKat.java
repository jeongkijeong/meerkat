package com.mlog.meerkat.tcp.handler;

import java.util.concurrent.BlockingQueue;

public abstract class AbstractMeerKat implements MeerKat {
	private BlockingQueue<String> queue = null;

	@Override
	public void sendMessage(String msg) {
		if (queue != null) {
			if (queue.size() >= 999) {
				queue.remove();
			}

			queue.add(msg);
		}
	}

	public BlockingQueue<String> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<String> queue) {
		this.queue = queue;
	}
}
