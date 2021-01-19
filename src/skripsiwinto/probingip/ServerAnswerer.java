/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.probingip;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import javax.swing.SwingWorker;
import skripsiwinto.TampilanServer;

/**
 *
 * @author Winto Junior Khosasi
 */
public class ServerAnswerer extends SwingWorker<Void, Void>{
    
    private final DatagramSocket socketUdpRcv;
    private final TampilanServer form;
    
    public ServerAnswerer(DatagramSocket socketUdp,TampilanServer form, LinkedList host){
        this.socketUdpRcv = socketUdp;
        this.form = form;
    }

    @Override
    protected Void doInBackground() throws Exception {
        DatagramPacket packet;
        while(true){
            byte[] buf = new byte[256];            
            packet = new DatagramPacket(buf,buf.length);
            socketUdpRcv.receive(packet);
            String listen = new String(packet.getData(),StandardCharsets.UTF_8);
            byte[] bufSend = new byte[256];
            DatagramSocket socketUdp = new DatagramSocket();
            if(listen.trim().matches("skripsi winto[?]")){
                InetAddress addr = packet.getAddress();
                int port = packet.getPort();
                form.setClient(addr.getHostAddress());
                bufSend = "ya server skripsinya winto standby".getBytes(StandardCharsets.UTF_8);
                DatagramPacket packetRecv = new DatagramPacket(bufSend,bufSend.length,addr,port);
                socketUdp.send(packetRecv);
            }else if(listen.trim().matches("core[?]")){
                InetAddress addr = packet.getAddress();
                int port = packet.getPort();
                bufSend = ("core : "+form.getCore()).getBytes(StandardCharsets.UTF_8);
                DatagramPacket packetRecv = new DatagramPacket(bufSend, bufSend.length, addr, port);
                socketUdp.send(packetRecv);
            }else if(listen.trim(). matches("set core.*[0-9]")){
                form.setCore(Byte.parseByte(listen.trim().split("\\s")[3]));
                InetAddress addr = packet.getAddress();
                int port = packet.getPort();
                bufSend = ("ya core diset " + form.getCore()).getBytes(StandardCharsets.UTF_8);
                DatagramPacket packetRecv = new DatagramPacket(bufSend, bufSend.length, addr, port);
                socketUdp.send(packetRecv);
            }else if (listen.trim().matches("lumpuhkan UI")) {
                InetAddress addr = packet.getAddress();
                int port = packet.getPort();
                form.lumpuhkanUI(false);
                bufSend = "ya UI dilumpuhkan".getBytes(StandardCharsets.UTF_8);
                DatagramPacket packetRecv = new DatagramPacket(bufSend, bufSend.length, addr, port);
                socketUdp.send(packetRecv);
            }else if (listen.trim().matches("mulai")) {
                InetAddress addr = packet.getAddress();
                int port = packet.getPort();
                socketUdp.setSoTimeout(10000);
                bufSend = "ya siap mulai".getBytes(StandardCharsets.UTF_8);
                DatagramPacket packetRecv = new DatagramPacket(bufSend, bufSend.length, addr, port);
                byte backOff = 3;
                while(backOff > 0){
                    socketUdp.send(packetRecv);
                    try{
                        byte[] buffer = new byte[256];
                        packet = new DatagramPacket(buffer, buffer.length);
                        socketUdp.receive(packet);
                        listen = new String(packet.getData(), StandardCharsets.UTF_8);
                        if (listen.trim().matches("baik")) {
                            break;
                        }
                    }catch(SocketTimeoutException ex){
                        backOff--;
                    }                    
                }
                form.matikanSocket();                    
                socketUdp.close();
                break;
            }
        }
        
        System.out.println("sudah ditutup");
        return null;
    } 
}
