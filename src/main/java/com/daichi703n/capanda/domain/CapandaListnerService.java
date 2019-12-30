package com.daichi703n.capanda.domain;

import java.io.EOFException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CapandaListnerService {

    @javax.annotation.PostConstruct
    public static void listen()
            throws UnknownHostException, PcapNativeException, EOFException, TimeoutException, NotOpenException {
    log.info("Start Listning.");

		// InetAddress addr = InetAddress.getByName("en0");
		final InetAddress addr = InetAddress.getLocalHost();
    final PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);
    final int snapLen = 65536;
    final PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
    final int timeout = 10;
    final PcapHandle handle = nif.openLive(snapLen, mode, timeout);
    // final Packet packet = handle.getNextPacketEx();
    // final IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
    // final Inet4Address srcAddr = ipV4Packet.getHeader().getSrcAddr();

    final PacketListener listener = new PacketListener() {
      @Override
      public void gotPacket(final Packet packet) {
        // Override the default gotPacket() function and process packet
        System.out.println(handle.getTimestamp());
        System.out.println(packet);
      }
    };

    // System.out.println(srcAddr);
    try {
      final int maxPackets = -1;
      handle.loop(maxPackets, listener);
    } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        handle.close();
        //https://www.devdungeon.com/content/packet-capturing-java-pcap4j

    }

}
