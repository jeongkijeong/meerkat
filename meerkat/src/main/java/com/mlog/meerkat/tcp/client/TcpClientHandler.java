package com.mlog.meerkat.tcp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.meerkat.tcp.handler.MeerKat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpClientHandler extends ChannelInboundHandlerAdapter{
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private MeerKat meerKet = null;

	public TcpClientHandler(MeerKat meerKet) {
		this.meerKet = meerKet;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelActive");

		if (meerKet != null) {
			meerKet.channelActived(ctx);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelInactive");
		
		if (meerKet != null) {
			meerKet.channelRemoved(ctx);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Client exception", cause);
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (meerKet != null) {
			meerKet.recvMessage((String) msg);
		}
	}
}
