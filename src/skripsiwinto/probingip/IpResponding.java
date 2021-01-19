/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.probingip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import skripsiwinto.TampilanServer;
import skripsiwinto.TampilanUtama;

/**
 *
 * @author Winto Junior Khosasi
 */
public class IpResponding extends SwingWorker<String, String>{
    private final TampilanUtama form;
    private final DatagramSocket socketUdp;
    private byte core = 0;
    private String hostname = null;//tujuan dari ini setting hostname server rmi;
    private String clientIp;

    public IpResponding(TampilanUtama form, DatagramSocket socketUdp) {
        this.form = form;
        this.socketUdp = socketUdp;
    }

    @Override
    protected void done() {
        try {
            if(!isCancelled()){
                this.hostname = get();
                try{
                    form.setVisible(false);
                    if(form.panduan != null)
                        form.panduan.dispose();
                    TampilanServer form2 = new TampilanServer(form, hostname, clientIp, core);
                    form2.setVisible(true);
                }catch(Exception e){
                    
                }
                
            }       
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientAnswerer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(ClientAnswerer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void process(List<String> chunks) {
        form.statusLabel.setText(chunks.get(0));
    }

    @Override
    protected String doInBackground() throws Exception {
        try {
            //DatagramSocket socketUdp = new DatagramSocket(9999);
            DatagramPacket packet;
            Boolean ready = false;
            
            while (!isCancelled()) {
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                publish("Status : Menunggu dan Mendengarkan Client");
                socketUdp.receive(packet);
                String listen = new String(packet.getData(), StandardCharsets.UTF_8);
                if (listen.trim().matches("skripsi winto[?]")) {
                    InetAddress addr = packet.getAddress();
                    int port = packet.getPort();
                    buf = "ya server skripsinya winto".getBytes();
                    packet = new DatagramPacket(buf, buf.length, addr, port);
                    publish("Status : Merespon Client...");
                    socketUdp.send(packet);
                } else if (listen.trim().matches("hostname.*([0-9]{1,3}[.]){3}[0-9]{1,3}")) {
                    publish("Status : Client terdeteksi, memulai RMIregistry...");
                    hostname = listen.trim().split("\\s")[2];
                    ready = true;
                    try {
                        LocateRegistry.createRegistry(1099);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(form, "Tidak dapat memulai RMI registry!!! coba cek penggunaan port 1099 atau matikan RMI registry pada proses berjalan","Tidak dapat memulai RMI Server lain Terdeteksi",JOptionPane.INFORMATION_MESSAGE);
                        ready = false;
                    }
                } else if (listen.trim().matches("siap[?]")) {
                    InetAddress addr = packet.getAddress();
                    int port = packet.getPort();
                    if(ready){
                        buf = "ya siap".getBytes(StandardCharsets.UTF_8);                        
                    }else{
                        buf = "tidak siap".getBytes(StandardCharsets.UTF_8);
                        System.exit(1);
                    }
                    packet = new DatagramPacket(buf, buf.length, addr, port);
                    socketUdp.send(packet);
                } else if (listen.trim().matches("core.*[0-9]")) {
                    publish("Status : Memulai aplikasi Server ...");
                    core = Byte.parseByte(listen.trim().split("\\s")[2]);
                    InetAddress addr = packet.getAddress();
                    int port = packet.getPort();
                    clientIp = addr.getHostAddress();
                    buf = "ya core diterima".getBytes();
                    packet = new DatagramPacket(buf, buf.length, addr, port);
                    socketUdp.send(packet);
                    break;
                }
            }
            socketUdp.close();

        } catch (SocketException e) {
            //TODO: handle exception
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return hostname;
    }
}
