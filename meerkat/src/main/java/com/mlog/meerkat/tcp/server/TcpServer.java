package com.mlog.meerkat.tcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.meerkat.common.Constant;
import com.mlog.meerkat.tcp.handler.MeerKat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public final class TcpServer implements Runnable {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private MeerKat meerKat = null;
	private String listenPort = null; // TCP server listen port.

	private EventLoopGroup masterGroup = null;
	private EventLoopGroup workerGroup = null;

	private ChannelFuture channelFuture = null;

	public TcpServer(String port, MeerKat meerKat) {
		super();

		this.listenPort = port;
		this.meerKat = meerKat;
	}

	@Override
	public void run() {
		start();
	}

	public int start() {
		int retv = Constant.FAILURE;

		masterGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap bootStrap = getServerBootStrap();
			channelFuture = bootStrap.bind(Integer.valueOf(listenPort)).sync();

			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			workerGroup.shutdownGracefully();
			masterGroup.shutdownGracefully();
			
			logger.info("server shutdown gracefully");
		}

		return retv;
	}
	
	public int close() {
		int retv = Constant.SUCCESS;

		try {
			if (workerGroup != null) {
				workerGroup.shutdownGracefully();
			}

			if (masterGroup != null) {
				masterGroup.shutdownGracefully();
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return retv;
	}

	private ServerBootstrap getServerBootStrap() {
		ServerBootstrap serverBootStrap = new ServerBootstrap();
		serverBootStrap.group(masterGroup, workerGroup).channel(NioServerSocketChannel.class);
		serverBootStrap.childHandler(new TcpServerManager(meerKat));

		return serverBootStrap;
	}
}
