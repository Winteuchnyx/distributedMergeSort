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
import java.net.SocketTimeoutException;

import java.util.LinkedList;

import java.io.IOException;
import java.net.UnknownHostException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import skripsiwinto.TampilanClient;
import skripsiwinto.TampilanUtama;
import skripsiwinto.containerHostConfig;
/**
 * mekanisnme ini penting untuk dipanggil berkali kali pada scan/refresh.
 */
public class ClientAsker extends SwingWorker<Void, String>{
    private final String networkAddress;
    private final LinkedList<containerHostConfig> hostListConf;
    private final TampilanClient form ;
    private DatagramSocket socketUdp;
    
    private final LinkedList<String> oldServer = new LinkedList<>();
    private final LinkedList<String> newServer = new LinkedList<>();
    
    public ClientAsker(TampilanClient form,String networkAddress,LinkedList<containerHostConfig> hostList){
        this.networkAddress = networkAddress;
        this.hostListConf = hostList;
        this.form = form;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try{
            socketUdp = new DatagramSocket();
            DatagramPacket packet;
            byte[] buf = new byte[256];
            buf = "skripsi winto?".getBytes(StandardCharsets.UTF_8);
            packet = new DatagramPacket(buf,buf.length,InetAddress.getByName(networkAddress),9999);
            socketUdp.send(packet);
            socketUdp.setSoTimeout(1000);
            while(!isCancelled()){
                try {
                    byte[] bufRecv = new byte[256];
                    DatagramPacket packetRecv = new DatagramPacket(bufRecv,bufRecv.length);
                    socketUdp.receive(packetRecv);
                    String respon = new String(packetRecv.getData(),StandardCharsets.UTF_8);
                    if(respon.trim().matches("ya server skripsinya winto")){
                        //kirim kembali ke server untuk setting hostname
                
                        int port = packetRecv.getPort();
                        InetAddress ack = packetRecv.getAddress();
                        newServer.add(ack.getHostAddress());
                        bufRecv = ("hostname : "+ack.getHostAddress()).getBytes(StandardCharsets.UTF_8);
                        packetRecv = new DatagramPacket(bufRecv, bufRecv.length,ack,port);
                        socketUdp.send(packetRecv);
                    }else if(respon.trim().matches("ya server skripsinya winto standby")){
                        InetAddress ack = packetRecv.getAddress();
                        oldServer.add(ack.getHostAddress());
                    }
                } catch (SocketTimeoutException e) {
                    //TODO: handle exception
                    break;
                }
            }
            
            socketUdp.close();
        } catch (SocketException e) {
            //TODO: handle exception
        }catch(IOException e){
            
        }
        return null;
    }
    
    private void broadcaster(DatagramSocket udpSocket, LinkedList<String> listHost, String messageSend, String messageRecv, String errMessageRecv, boolean last) throws SocketException, UnknownHostException, IOException {
        LinkedList<String> hostList = (LinkedList<String>) listHost.clone();
        byte backOff = 3;
        while (!hostList.isEmpty() && backOff > 0) {
            udpSocket.setSoTimeout(0);
            for (String serverHost : hostList) {
                byte[] buf = new byte[256];
                DatagramPacket packet;

                buf = messageSend.getBytes(StandardCharsets.UTF_8);
                packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(serverHost), 9999);
                udpSocket.send(packet);
            }
            int max = hostList.size();
            udpSocket.setSoTimeout(100000);
            while (max > 0) {
                try {
                    byte[] buf = new byte[256];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    udpSocket.receive(packet);
                    String temp = new String(buf);
                    if (temp.trim().matches(messageRecv)) {
                        hostList.remove(packet.getAddress().getHostAddress());
                    } else if (temp.trim().matches(errMessageRecv)) {
                        InetAddress inet = packet.getAddress();
                        JOptionPane.showMessageDialog(form, "Host : " + inet.getHostAddress()
                                + " mengalami masalah mohon direstart", "Server Bermasalah", JOptionPane.ERROR_MESSAGE);
                        buf = ("hostname : " + inet.getHostAddress()).getBytes(StandardCharsets.UTF_8);
                        if (last) {
                            buf = ("core : " + TampilanUtama.defaultCore).getBytes(StandardCharsets.UTF_8);
                        }
                        packet = new DatagramPacket(buf, buf.length, inet, 9999);
                        udpSocket.send(packet);
                    }
                } catch (SocketTimeoutException ex) {

                }
                max--;
            }
            backOff--;
        }
        if (!hostList.isEmpty()) {
            hostList.forEach((item) -> {
                listHost.remove(item);
            });
        }
    }
    
    
    
    /*
    buat list untuk broadcast berdasarkan balasan broadcast pesan "ya server skripsi winto standby" untuk menanyakan configurasi core masing masing server;,
    buat list untuk broadcast berdasarkan balasan broadcast pesan ya server skripsi winto untuk membalas hostname dan broadcast siap? serta set core.
    
    */

    @Override
    protected void done() {
        if (!isCancelled()) {            
            try {
                socketUdp = new DatagramSocket();
                byte backOff;
                hostListConf.clear();
                for (String item : oldServer) {
                    backOff = 3;
                    byte[] buf = "core?".getBytes(StandardCharsets.UTF_8);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(item), 9999);
                    socketUdp.send(packet);
                    socketUdp.setSoTimeout(300);
                    while (!isCancelled() && backOff > 0) {
                        try {
                            byte[] bufRecv = new byte[255];
                            DatagramPacket packetRecv = new DatagramPacket(bufRecv, bufRecv.length);
                            socketUdp.receive(packetRecv);
                            String respon = new String(packetRecv.getData(), StandardCharsets.UTF_8);
                            if (respon.trim().matches("core.*[0-9]")) {
                                byte core = Byte.parseByte(respon.trim().split("\\s")[2]);
                                hostListConf.add(new containerHostConfig(item, core));
                            }
                        } catch (SocketTimeoutException e) {
                            backOff--;
                        }
                    }
                }
                DatagramSocket udpSocket = new DatagramSocket();
                broadcaster(udpSocket, newServer, "siap?", "ya siap", "tidak siap", false);
                broadcaster(udpSocket, newServer, "core : " + TampilanUtama.defaultCore, "ya core diterima", "core tidak diterima", true);
                for (String item : newServer) {
                    hostListConf.add(new containerHostConfig(item, TampilanUtama.defaultCore));
                }
                udpSocket.close();
                form.refreshScan();
                form.buttonScanServer.setText("SCAN");
            } catch (UnknownHostException ex) {
                Logger.getLogger(IpProbing.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(IpProbing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
