/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.iohandling;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Winto Junior Khosasi
 */
public class FileGenerator {
    public static void randomGenerator(String inputFile, int jmlUkuran, int jangkauan,int group){
        Random rnd = new Random();
        Path file = Paths.get(inputFile+".txt");
        if(Files.notExists(file.getParent())){
            try {
                Files.createDirectory(file.getParent());
            } catch (IOException ex) {
                Logger.getLogger(FileGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int counter = 1;
        while(Files.exists(file)){
            file = Paths.get(inputFile+" ("+(counter++)+").txt");            
        }
        int end = jmlUkuran / group;
        try(PrintWriter pw = new PrintWriter(Files.newOutputStream(file),true)){
            for(int i = 0; i < end; i++){
                for(int j = 0; j < group; j++){
                    pw.print(rnd.nextInt(jangkauan+1)+" ");
                }
                pw.println();
            }
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(FileGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
