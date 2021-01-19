/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.probingip;

/**
 *
 * @author Winto Junior Khosasi
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import javax.swing.SwingWorker;
public class ClientAnswerer extends SwingWorker<Void, Void>{
    private final DatagramSocket socketUdp;
    
    public ClientAnswerer(DatagramSocket socketUdp){
        this.socketUdp = socketUdp;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            DatagramPacket packet;
            String hostname;
            DatagramSocket socketUdpSend = new DatagramSocket();
            while(true){
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf,buf.length);
                socketUdp.receive(packet);
                String listen = new String(packet.getData(),StandardCharsets.UTF_8);
                if(listen.trim().matches("client tersedia[?]")){
                    InetAddress addr = packet.getAddress();
                    int port = packet.getPort();
                    buf = "ya saya tersedia".getBytes();
                    packet = new DatagramPacket(buf,buf.length,addr,port);
                    socketUdpSend.send(packet);
                }
            }            
        } catch (SocketException e) {
            //TODO: handle exception
            //e.printStackTrace();
        }catch(IOException e){
            //e.printStackTrace();
        }
        return null;
    }
}
