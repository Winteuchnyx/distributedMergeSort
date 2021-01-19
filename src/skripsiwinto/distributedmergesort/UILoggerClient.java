/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.distributedmergesort;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import skripsiwinto.TampilanClient;

/**
 *
 * @author Winto Junior Khosasi
 */
public class UILoggerClient extends SwingWorker<Void, String>{
    
    private String message;
    private TampilanClient form;
    private boolean tulis = false;
    private Path file;
    private int[] arr;
    
    public UILoggerClient(TampilanClient form, String message, Path file, boolean tulis){
        this.message = message;
        this.form = form;
        this.tulis = tulis;
        this.file = file;
    }
    
    public UILoggerClient(TampilanClient form, int[] arr, Path file, boolean tulis){
        this.form = form;
        this.tulis = tulis;
        this.file = file;
        this.arr = arr;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        if(arr==null){
            publish(message);
        }else{
            String messageTemp = "";
            for(int item : arr){
                messageTemp += (item +" ");
            }
            publish(messageTemp);
        }
        
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        String temp = String.format("%s%s%n", form.jTextArea1.getText() , chunks.get(0));
        form.jTextArea1.setText(temp);
        if (tulis) {
            try {
                if (Files.notExists(file.getParent())) {
                    Files.createDirectory(file.getParent());
                }
                try (PrintWriter pw = new PrintWriter(Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.APPEND), true)) {
                    pw.println(chunks.get(0));
                }
            } catch (IOException ex) {
                Logger.getLogger(UILogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
