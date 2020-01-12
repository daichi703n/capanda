package com.daichi703n.capanda.domain;

import java.io.EOFException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.seratch.jslack.api.webhook.WebhookResponse;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Service
@Component
public class CapandaListnerService {

    @Autowired
    private SlackService slackService;

    // @javax.annotation.PostConstruct
    @Scheduled(fixedDelay = 1000)
    public void listen()
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
            // log.info(handle.getTimestamp().toString());
            if (packet.contains(IpV4Packet.class)){
                
                if (packet.contains(TcpPacket.class)){
                    final TcpPacket tcpPacket = packet.get(TcpPacket.class);
                    // final TcpHeader tcpHeader = (TcpHeader) packet.getHeader();
                    // if (tcpHeader.getDstPort().toString() == "80"){
                    // final String result = packet
                    String result;
                    try {
                        final byte[] bytes = tcpPacket.getRawData();
                        result = tcpPacket
                                // .get(TcpPacket.class)
                                .getPayload()
                                // .getRawData()
                                // .getHeader()
                                // .getClass()
                                .toString();
                                // .toHexString();
                        // log.info("tmp: "+result);
                        // log.info("flatten: "+flattenPayload(result));

                        result = hexToAscii(flattenPayload(result));
                        log.info(result);
                        // log.info("---search---");
                        String searchedPayload = searchPayload(result);
                        if (searchedPayload != ""){
                            log.info(searchedPayload);
                            try {
                                // WebhookResponse res = slackService.hello();
                                WebhookResponse res = slackService.send(searchedPayload + "<!here>");
                                System.out.println(res);
                            } catch (IOException e) {
                                // Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } catch (final NullPointerException e){
                        log.info("No Payload");
                    }

                    // }
                }else{
                    log.info("Ipv4 but not TCP");
                }
                }else{
                    log.info("not Ipv4");
                }
            }
        };

        // System.out.println(srcAddr);
        try {
            final int maxPackets = -1;
            // handle.setFilter("tcp and dst port 80", BpfCompileMode.OPTIMIZE);
            handle.setFilter("tcp and dst port 80 and (tcp[tcpflags] & (tcp-push|tcp-fin) != 0)", BpfCompileMode.OPTIMIZE);
            // https://biot.com/capstats/bpf.html
                
                handle.loop(maxPackets, listener);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        handle.close();

    }

    private static String hexToAscii(String hexStr) {
        //https://www.baeldung.com/java-convert-hex-to-ascii
        StringBuilder output = new StringBuilder("");
        
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    private static String flattenPayload(String str){
        String[] regexList = {
            ".*Hex stream: ",
            "\\[data .*\\n",
            " ",
            "\\n"
        };
        for(String regex : regexList){
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }

        return str;
    }

    private static String searchPayload(String str){
        String[] removeRegexList = {
            "^GET",
        };
        for(String regex : removeRegexList){
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }

        String result = "";
        int matchCount = 0;
        String[] regexList = {
            "password",
            "[3|4|5]\\d{15}.*20\\d{2}.*[0|1]\\d.*\\d{3}",
            "card",
        };
        for(String regex : regexList){
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            if (m.find()) {matchCount++;}
        }
        if (matchCount != 0){
            result = result + str;
        }
        return result;
    }

}

//https://www.devdungeon.com/content/packet-capturing-java-pcap4j
