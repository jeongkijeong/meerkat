package com.mlog.meerkat.tcp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.meerkat.common.Constant;
import com.mlog.meerkat.tcp.client.TcpClient;

public class MeerKatManager {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private TcpClient tcpClient = null;
	
	private String tcpConnPort = null;
	private String tcpConnHost = null;

	private MeerKat meerkat = null;
	
	public MeerKatManager() {
		super();
	}

	/**
	 * Start meerkat manager.
	 */
	public int start(String host, String port, MeerKat meerket) {
		int retv = Constant.FAILURE;
		
		if (tcpClient != null ) {
			return retv;
		}

		this.tcpConnHost = host;
		this.tcpConnPort = port;
		
		this.meerkat = meerket;

		retv = startMeerKatClient();

		return retv;
	}

	/**
	 * Close meerkat manager.
	 */
	public int close() {
		int retv = Constant.FAILURE;

		if (tcpClient != null) {
			tcpClient.close();
		}

		retv = Constant.SUCCESS;
		tcpClient = null;

		return retv;
	}

	/**
	 * TCP Client start.
	 * @return
	 */
	private int startMeerKatClient() {
		int retv = Constant.FAILURE;

		if (tcpConnHost == null || tcpConnPort == null) {
			return retv;
		}
		
		try {
			Thread thread = new Thread(tcpClient = new TcpClient(tcpConnHost, tcpConnPort, meerkat));
			thread.start();

			retv = Constant.SUCCESS;
		} catch (Exception e) {
			logger.error("", e);
		}

		return retv;
	}

}
