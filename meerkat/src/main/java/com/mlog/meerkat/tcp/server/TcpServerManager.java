package com.mlog.meerkat.tcp.server;

import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.meerkat.tcp.handler.MeerKat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TcpServerManager extends ChannelInitializer<SocketChannel>{
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private MeerKat meerKet = null;

	public TcpServerManager() {
		super();
	}

	public TcpServerManager(MeerKat meerKet) {
		super();
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
			
			ch.pipeline().addLast(new TcpServerHandler(meerKet));
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
