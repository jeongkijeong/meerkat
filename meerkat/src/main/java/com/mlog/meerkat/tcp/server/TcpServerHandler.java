/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.mlog.meerkat.tcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.meerkat.tcp.client.handler.MeerKat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Handles a server-side channel.
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<Object> {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private MeerKat meerKet = null;

	public TcpServerHandler(MeerKat meerKet) {
		this.meerKet = meerKet;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);

		if (meerKet != null) {
			meerKet.channelActived(ctx);
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);

		if (meerKet != null) {
			meerKet.channelRemoved(ctx);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("exceptionCaught", cause);
		ctx.close();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
		if (meerKet != null) {
			meerKet.recvMessage(ctx, obj);
		}
	}

}
