/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.iohandling;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Winto Junior Khosasi
 */
public class FileToArray {
    private static int[] arr;
    private static int[][] multiArr;
    
    public static int[] getArr(){
        return arr;
    }
    
    public static int[][] getMultiArr(){
        return multiArr;
    }
    
    public static int convertToArr(Path file){
        int size = 0;
        try{
            Scanner scan = new Scanner(file);
            while(scan.hasNext()){
                scan.nextInt();
                size++;
            }
            scan.close();
            scan = new Scanner(file);
            arr = new int[size];
            int i = 0;
            while(scan.hasNext()){
                arr[i++] = scan.nextInt();
            }        
            scan.close();
        } catch (IOException ex) {
            Logger.getLogger(FileToArray.class.getName()).log(Level.SEVERE, null, ex);
        }
        return size;
    }
    
    public static int convertToMultiArr(Path file,int hostSize){
        int size = 0;
        try {
            Scanner scan = new Scanner(file);
            while(scan.hasNext()){
                scan.nextInt();
                size++;
            }
            scan.close();
            scan = new Scanner(file);
            multiArr = new int[hostSize][];
            int j = 0;
            int startIndex = 0;
            int endIndex = 0;
            int group = size / hostSize;
            int remainder = size;
            int hostId = -1;
            int hostIndex = 0;
            while(scan.hasNext()){
                if(j < endIndex){
                    multiArr[hostId][hostIndex++] = scan.nextInt();
                    j++;
                }else{
                    startIndex = endIndex;
                    remainder = ((remainder / (2 * group)) > 0)? remainder - group : 0;
                    endIndex = size - remainder;
                    hostId++;
                    hostIndex = 0;
                    multiArr[hostId] = new int[endIndex - startIndex];                    
                }
            }
            scan.close();
        } catch (IOException ex) {
            Logger.getLogger(FileToArray.class.getName()).log(Level.SEVERE, null, ex);
        }
        return size;
    }
}
