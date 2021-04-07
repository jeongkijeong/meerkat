package com.mlog.meerkat.tcp.handler;

import java.util.LinkedHashMap;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

public interface MeerKat {
	public void recvMessage(ChannelHandlerContext ctx, Object obj);

	public void recvMessage(String msg);

	public void sendMessage(String msg);

	public void channelActived(ChannelHandlerContext ctx);

	public void channelRemoved(ChannelHandlerContext ctx);

	public LinkedHashMap<String, ChannelHandler> getPipeLine();
}
