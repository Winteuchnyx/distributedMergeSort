/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto;

import java.net.DatagramSocket;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

/**
 *
 * @author Winto Junior Khosasi
 */
public class IpProber extends SwingWorker<String, String>{
    TampilanUtama obj;
    public IpProber(TampilanUtama obj,DatagramSocket socketUdp ){
        this.obj = obj;
    }
    
    public IpProber(){
        super();
    }
    
    public IpProber(TampilanUtama obj){
        this.obj = obj;
    }
    
    @Override
    protected String doInBackground() throws Exception {
        while(!Thread.interrupted()){
            publish("saya masih diproses");
            publish("tunggu ya");
        }
        return "saya sudah selesai dan berhenti";
    }

    @Override
    protected void process(List<String> chunks) {
        super.process(chunks); //To change body of generated methods, choose Tools | Templates.
        obj.statusLabel.setText(chunks.get(0));
    }

    @Override
    protected void done() {
        super.done(); //To change body of generated methods, choose Tools | Templates.
        try {
            if(!isCancelled())
            obj.statusLabel.setText(get());
        } catch (InterruptedException ex) {
            Logger.getLogger(IpProber.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(IpProber.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
