/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.probingip;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import javax.swing.SwingWorker;
import skripsiwinto.TampilanClient;
import skripsiwinto.TampilanUtama;
import skripsiwinto.containerHostConfig;

/**
 *
 * @author Winto Junior Khosasi
 */
public class IpProbing extends SwingWorker<String, String>{
    
    private final TampilanUtama form;
    private String networkAddress = null;
    private final LinkedList<String> hostList;
    private final byte core;
    
    public IpProbing(TampilanUtama form,LinkedList<String> hostList,String networkAddress,byte core){
        this.form = form;
        this.hostList = hostList;
        this.networkAddress = networkAddress;
        this.core = core;
    }
    
    @Override
    protected String doInBackground() throws Exception {
        while (!isCancelled()) {
            while (networkAddress == null && !isCancelled()) {
                LinkedList<String> list = new LinkedList<>();
                try {
                    Enumeration networkAdapter = NetworkInterface.getNetworkInterfaces();

                    while (networkAdapter.hasMoreElements()) {
                        NetworkInterface ipAdapter = (NetworkInterface) networkAdapter.nextElement();
                        
                        if(ipAdapter.isLoopback()){
                            continue;
                        }
                        publish("Status : Scan adapter pada client");
                        for(InterfaceAddress interfaceAddress : ipAdapter.getInterfaceAddresses()){
                            InetAddress ips = (InetAddress) interfaceAddress.getBroadcast();
                            if(ips == null)
                                continue;                            
                            list.add(ips.getHostAddress());
                        }
                        //code dibawah yang di comment deprecated
                        /*
                        Enumeration address = ipAdapter.getInetAddresses();
                        publish("Status : Scan adapter pada client");
                        while (address.hasMoreElements()) {
                            InetAddress ips = (InetAddress) address.nextElement();
                            if (ips instanceof Inet6Address || ips.getHostAddress().equals("127.0.0.1")) {
                                continue;
                            } else {
                                String temp = ips.getHostAddress();
                                list.add(temp.substring(0, temp.lastIndexOf(".")) + ".255");
                            }
                        }
                        */                        
                    }
                    Thread.sleep(2000);
                    publish("Status : " + list.size() + " adapter terdeteksi");
                } catch (SocketException e) {
                    //beritahu kesalahan pada program aja
                    e.printStackTrace();
                    publish("tidak dapat memeriksa network interface pada perangkat anda");
                }

                networkAddress = list.getFirst();
                int max = 0;
                try {
                    DatagramSocket socketUdp = new DatagramSocket();
                    DatagramPacket packet;
                    byte[] buf = new byte[256];
                    buf = "skripsi winto?".getBytes(StandardCharsets.UTF_8);

                    while (list.size() > 0 && !isCancelled()) {
                        String networkAddressTemp = list.poll();
                        packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(networkAddressTemp), 9999);
                        publish("Status : Mencari LAN yang sesuai ...");
                        socketUdp.send(packet);

                        int maxCandidate = 0;
                        
                        while (!isCancelled()) {
                            socketUdp.setSoTimeout(2000);
                            try {
                                byte[] bufRecv = new byte[256];
                                DatagramPacket packetRecv = new DatagramPacket(bufRecv, bufRecv.length);
                                socketUdp.receive(packetRecv);
                                String respon = new String(packetRecv.getData(), StandardCharsets.UTF_8);
                                if (respon.trim().matches("ya server skripsinya winto")) {
                                    maxCandidate++;
                                }

                            } catch (SocketTimeoutException e) {
                                //TODO: handle exception
                                break;
                            }
                        }
                        if (max < maxCandidate) {
                            max = maxCandidate;
                            networkAddress = networkAddressTemp;
                        }
                    }
                    socketUdp.close();
                    publish("Status : network address terpilih  " + networkAddress);

                } catch (SocketException e) {
                    //TODO: handle exception
                } catch (IOException e) {

                }
            }

            if (networkAddress != null) {
                if(!hostList.isEmpty())
                    hostList.clear();
                try {
                    DatagramSocket socketUdp = new DatagramSocket();
                    DatagramPacket packet;
                    byte[] buf = new byte[256];
                    buf = "skripsi winto?".getBytes(StandardCharsets.UTF_8);
                    packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(networkAddress), 9999);
                    socketUdp.send(packet);
                    socketUdp.setSoTimeout(300);
                    Thread.sleep(1000);
                    publish("Status : Mendeteksi server ...");
                    while (!isCancelled()) {
                        try {
                            byte[] bufRecv = new byte[256];
                            DatagramPacket packetRecv = new DatagramPacket(bufRecv, bufRecv.length);
                            socketUdp.receive(packetRecv);
                            String respon = new String(packetRecv.getData(), StandardCharsets.UTF_8);
                            if (respon.trim().matches("ya server skripsinya winto")) {
                                //kirim kembali ke server untuk setting hostname

                                int port = packetRecv.getPort();
                                InetAddress ack = packetRecv.getAddress();
                                hostList.add(ack.getHostAddress());
                                bufRecv = ("hostname : " + ack.getHostAddress()).getBytes(StandardCharsets.UTF_8);
                                packetRecv = new DatagramPacket(bufRecv, bufRecv.length, ack, port);
                                socketUdp.send(packetRecv);
                            }
                            Thread.sleep(1000);
                            publish("Status : Terdapat "+hostList.size()+" Server Terdeteksi.");
                        } catch (SocketTimeoutException e) {
                            //TODO: handle exception
                            break;
                        }
                    }
                    socketUdp.close();
                } catch (SocketException e) {
                    //TODO: handle exception
                } catch (IOException e) {

                }
                if (hostList.size() == 0) {
                    networkAddress = null;
                } else {
                    break;
                }
            }
        }
        return networkAddress;
    }
    
    private void broadcaster(DatagramSocket udpSocket,LinkedList<String> listHost, String messageSend,String messageRecv,String errMessageRecv,boolean last) throws SocketException, UnknownHostException, IOException{
        LinkedList<String> hostList = (LinkedList<String>)listHost.clone();
        byte backOff = 3;
        while(!hostList.isEmpty() && backOff > 0){            
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
                    String temp = new String(buf,StandardCharsets.UTF_8);
                    if (temp.trim().matches(messageRecv)) {
                        hostList.remove(packet.getAddress().getHostAddress());
                    }else if(temp.trim().matches(errMessageRecv)){
                        InetAddress inet = packet.getAddress();
                        JOptionPane.showMessageDialog(form, "Host : " + inet.getHostAddress()+
                                " mengalami masalah mohon direstart","Server Bermasalah",JOptionPane.ERROR_MESSAGE);
                        buf = ("hostname : " + inet.getHostAddress()).getBytes(StandardCharsets.UTF_8);
                        if(last)
                            buf = ("core : " + core).getBytes(StandardCharsets.UTF_8);
                        packet = new DatagramPacket(buf, buf.length, inet, 9999);
                        udpSocket.send(packet);
                    }
                } catch (SocketTimeoutException ex) {
                    
                }
                max--;
            }
            backOff--;
        }
        if(!hostList.isEmpty()){
            hostList.forEach((item) -> {
                listHost.remove(item);
            });
        }
    }

    @Override
    protected void process(List<String> chunks) {
        form.statusLabel.setText(chunks.get(0));
    }    

    @Override
    protected void done() {
        try {
            if(!isCancelled()){
                form.setNetworkAddress(get());
                try {
                    form.statusLabel.setText("Status : Memulai aplikasi client...");
                    DatagramSocket udpSocket = new DatagramSocket();
                    broadcaster(udpSocket, hostList, "siap?", "ya siap","tidak siap",false);
                    broadcaster(udpSocket, hostList, "core : " + core, "ya core diterima","core tidak diterima",true);
                    LinkedList<containerHostConfig> completeList = new LinkedList<>();
                    for(String item : hostList){
                        completeList.add(new containerHostConfig(item, core));
                    }
                    udpSocket.close();
                    form.setVisible(false);
                    if(form.panduan !=null)
                        form.panduan.dispose();
                    TampilanClient form2 = new TampilanClient(form,networkAddress,completeList);
                    form2.setVisible(true);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(IpProbing.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(IpProbing.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(IpProbing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(IpProbing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }      
}
