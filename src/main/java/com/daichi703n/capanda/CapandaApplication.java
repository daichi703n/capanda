package com.daichi703n.capanda;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
// @EnableScheduling
// @EnableWebSecurity
public class CapandaApplication {

	public static void main(String[] args)
			throws PcapNativeException, UnknownHostException, EOFException, TimeoutException, NotOpenException {
		SpringApplication.run(CapandaApplication.class, args);

		// InetAddress addr = InetAddress.getByName("en0");
		InetAddress addr = InetAddress.getLocalHost();
		PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);
		int snapLen = 65536;
		PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
		int timeout = 1000;
		PcapHandle handle = nif.openLive(snapLen, mode, timeout);
		Packet packet = handle.getNextPacketEx();
		IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
		Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();
		System.out.println(srcAddr);
		handle.close();

	}

}
