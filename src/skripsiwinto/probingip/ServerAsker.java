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
import javax.swing.SwingWorker;
import skripsiwinto.TampilanServer;

/**
 *
 * @author Winto Junior Khosasi
 */
public class ServerAsker extends SwingWorker<Void, Void>{
    DatagramSocket socketUdp;
    LinkedList<String> host;
    TampilanServer form;
    public ServerAsker(TampilanServer form,LinkedList<String> host){
        this.host = host;
        this.form = form;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        LinkedList<String> listHost = (LinkedList<String>) host.clone();
        socketUdp = new DatagramSocket();
        byte backOff = 3;
        while(!isCancelled()&&(backOff > 0) && !listHost.isEmpty()){
            byte[] buf = new byte[256];
            buf = "client tersedia?".getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet;
            for(String item : listHost){
                packet = new DatagramPacket(buf,buf.length,InetAddress.getByName(item),9998);
                socketUdp.send(packet);
            }
            
            int max = listHost.size();
            byte[] bufRecv = new byte[256];
            packet = new DatagramPacket(bufRecv,bufRecv.length);
            socketUdp.setSoTimeout(100000);
            while(max > 0){
                try{
                    socketUdp.receive(packet);
                    String temp = new String(packet.getData(), StandardCharsets.UTF_8);
                    if(temp.trim().matches("ya saya tersedia")){
                        listHost.remove(packet.getAddress().getHostAddress());
                        backOff = 3;
                    }
                }catch(SocketTimeoutException ex){
                    
                }
                max --;
            }
            backOff--;
        }
        if(!isCancelled()){
            if(!listHost.isEmpty()){
                for(String item : listHost){
                    host.remove(item);
                }
            }
            form.refreshScan();
        }
        socketUdp.close();
        return null;
    }

    @Override
    protected void done() {
        form.buttonScanClient.setText("SCAN");
    }
    
    
}
