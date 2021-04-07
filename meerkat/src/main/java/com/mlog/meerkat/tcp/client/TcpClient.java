package com.mlog.meerkat.tcp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.meerkat.common.Constant;
import com.mlog.meerkat.tcp.client.handler.MeerKat;

public class TcpClient implements Runnable {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private EventLoopGroup group;

	private Channel channel;
	private ChannelFuture channelFuture;

	private MeerKat meerKet = null;
	private String tcpConnHost = null; // connect server host.
	private String tcpConnPort = null; // connect server port.

	private int connectTimeout = 10;

	private boolean isConnect = false;
	
	public TcpClient(String host, String port, MeerKat meerKet) {
		super();

		this.meerKet = meerKet;
		
		this.tcpConnHost = host;
		this.tcpConnPort = port;
	}

	@Override
	public void run() {
		start();
	}

	public int start() {
		int retv = Constant.FAILURE;
		
		channel = null;
		channelFuture = null;
		
		group = null;

		String host = tcpConnHost;
		String port = tcpConnPort;

		logger.debug("try to connect {} / {}", host, port);

		try {
			Bootstrap bootStrap = getBootStrap();
			bootStrap.group(group = new NioEventLoopGroup());

			channelFuture = bootStrap.connect(host, Integer.valueOf(port)).sync();
			if (channelFuture != null && channelFuture.isSuccess() == true) {
				logger.debug("success connected host{} / port{}", host, port);
				
				isConnect = true;
			} else {
				logger.debug("failure connected host{} / port{}", host, port);
			}

			// wait until the connection is closed.
			if (isConnect == true) {
				channel = channelFuture.channel();
				channel.closeFuture().sync();
			}
			
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			closeService();
		}

		return retv;
	}
	
	public int close() {
		int retv = closeService();
		return retv;
	}

	public int send(String data) {
		int retv = Constant.FAILURE;

		if (channel != null) {
			channel.writeAndFlush(data);
		}

		return retv;
	}

	public int sendByteBuffer(ByteBuffer data) {
		int retv = Constant.FAILURE;

		if (channel != null) {
			data.flip();

			channel.writeAndFlush(Unpooled.wrappedBuffer(data));
			retv = Constant.SUCCESS;
		}

		return retv;
	}

	public int closeService() {
		int retv = Constant.FAILURE;

		isConnect = false;

		if (channel != null && channel.isActive() == true) {
			channel.closeFuture();
		}

		if (group != null) {
			group.shutdownGracefully();
		}

		retv = Constant.SUCCESS;
		
		return retv;
	}

	public boolean isConnect() {
		return isConnect;
	}

	public void setConnect(boolean connect) {
		this.isConnect = connect;
	}

	public MeerKat getMeerKet() {
		return meerKet;
	}

	public void setMeerKet(MeerKat meerKet) {
		this.meerKet = meerKet;
	}
	
	private Bootstrap getBootStrap() {
		Bootstrap bootStrap = new Bootstrap();
		bootStrap.channel(NioSocketChannel.class);

		bootStrap.handler(new TcpClientManager(meerKet));

		bootStrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout * 1000);
		bootStrap.option(ChannelOption.TCP_NODELAY, true);

		return bootStrap;
	}
}
