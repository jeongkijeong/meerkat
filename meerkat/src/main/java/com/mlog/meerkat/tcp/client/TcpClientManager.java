package com.mlog.meerkat.tcp.client;

import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.meerkat.tcp.handler.MeerKat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TcpClientManager extends ChannelInitializer<SocketChannel>{
	private Logger logger = LoggerFactory.getLogger(getClass());

	private MeerKat meerKet = null;

	public TcpClientManager(MeerKat meerKet) {
		this.meerKet = meerKet;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		try {
			LinkedHashMap<String, ChannelHandler> pipeLine = meerKet.getPipeLine();
			
			if (pipeLine != null) {
				for (String key : pipeLine.keySet()) {
					ch.pipeline().addLast(key, pipeLine.get(key));
				}
			}

			ch.pipeline().addLast(new TcpClientHandler(meerKet));
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
