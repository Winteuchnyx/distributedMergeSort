/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.distributedmergesort;

import java.awt.Component;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.MarshalException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.ServerError;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import skripsiwinto.mergesort.DistributedMergeSort;
import skripsiwinto.mergesort.Sorted;

/**
 *
 * @author Winto Junior Khosasi
 */
public class RMIClient {
    private final int chunkSize;
    private int[][] arr;
    private int[] resultArr;
    private final String[] hostList;
    private final byte core;
    private final Sorted sorted;
    private  boolean success;
        
    public RMIClient(int[][] arr, int chunkSize, String[] hostList,byte core,Sorted sorted){
        this.arr = arr;
        this.chunkSize = chunkSize;
        this.hostList = hostList;
        this.core = core;
        this.sorted = sorted;
    }
    
    public double sort(){
        success = true;
        byte hostSize = (byte)hostList.length;
        long startTime = System.nanoTime();
        int length = 0;
        for(int i = 0; i < arr.length;i++)
            length += arr[i].length;
        
        if (length > (chunkSize * hostSize)) {
            Runnable[] runnable = new Runnable[hostSize];
            LinkedListCustom queue = new LinkedListCustom(hostSize * 4);
            for (int i = 0; i < hostSize; i++) {
                String hostName = hostList[i];
                int[] arrCapture = arr[i];
                runnable[i] = new Runnable() {
                    @Override
                    public void run() {
                        int baseArrayLength = arrCapture.length;
                        try {
                            if(chunkSize > 0){
                                int remainderIn = baseArrayLength;
                                int startIn = 0;
                                int endIn;
                                int[] arrChunked = new int[chunkSize];
                                while (remainderIn > 0) {
                                    remainderIn = ((remainderIn - chunkSize) > 0) ? remainderIn - chunkSize : 0;
                                    endIn = baseArrayLength - remainderIn;
                                    if((endIn - startIn) != arrChunked.length){
                                        arrChunked = new int[endIn - startIn];
                                    }
                                    for (int i = startIn, j = 0; i < endIn; i++) {
                                        arrChunked[j++] = arrCapture[i];
                                    }
                                    Registry registry = LocateRegistry.getRegistry(hostName);
                                    sorting stub = (sorting) registry.lookup("MERGESORT");
                                    while(true){
                                        try{
                                            queue.put(stub.sort(arrChunked, sorted));                                            
                                            arrChunked = null;
                                            break;
                                        } catch (RemoteException ex) {
                                            JFrame js = new JFrame();
                                            int pilihan = JOptionPane.showConfirmDialog(js, ex.getMessage()+"\napakah ingin batal?", "Chunking error", JOptionPane.YES_NO_OPTION);
                                            js.dispose();
                                            if(pilihan == JOptionPane.YES_OPTION){
                                                queue.add(arrCapture);
                                                success = false;
                                                break;
                                            }
                                            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    } 
                                    synchronized (RMIClient.this) {
                                        RMIClient.this.notify();
                                    }
                                    startIn = endIn;
                                }
                            }else{
                                Registry registry = LocateRegistry.getRegistry(hostName);
                                sorting stub = (sorting) registry.lookup("MERGESORT");
                                try {
                                    queue.put(stub.sort(arrCapture, sorted));
                                    synchronized (RMIClient.this) {
                                        RMIClient.this.notify();
                                    }
                                } catch (RemoteException e) {
                                    long maxHostMemory = stub.getMemory();

                                    int chunkSpecial = (int) (maxHostMemory / 4);
                                    chunkSpecial *= 0.8;
                                    int remainderSpecial = baseArrayLength;
                                    int startSpecial = 0;
                                    int endSpecial = 0;

                                    while (remainderSpecial > 0 && chunkSpecial > 0) {
                                        while (true) {
                                            remainderSpecial = ((remainderSpecial - chunkSpecial) > 0) ? (remainderSpecial - chunkSpecial) : 0;
                                            endSpecial = baseArrayLength - remainderSpecial;
                                            int[] arrChunked = new int[endSpecial - startSpecial];
                                            for (int i = startSpecial, j = 0; i < endSpecial; i++) {
                                                arrChunked[j++] = arrCapture[i];
                                            }
                                            try {
                                                queue.put(stub.sort(arrChunked, sorted));
                                                arrChunked = null;
                                                break;
                                            } catch(ConnectException connectException){
                                                JFrame js = new JFrame();
                                                int pilihan = JOptionPane.showConfirmDialog(js, connectException.getMessage() + "\ningin batalkan?", "Automatic chunking failed", JOptionPane.YES_NO_OPTION);
                                                js.dispose();
                                                if (pilihan == JOptionPane.YES_OPTION) {
                                                    queue.add(arrCapture);
                                                    success = false;
                                                    chunkSpecial = -1;
                                                    queue.add(arrChunked);
                                                    break;
                                                }
                                            } catch(UnmarshalException unmarshallex){
                                                JFrame js = new JFrame();
                                                int pilihan = JOptionPane.showConfirmDialog(js, unmarshallex.getMessage() + "\ningin batalkan?", "Automatic chunking failed", JOptionPane.YES_NO_OPTION);
                                                js.dispose();
                                                if (pilihan == JOptionPane.YES_OPTION) {
                                                    queue.add(arrCapture);
                                                    success = false;
                                                    chunkSpecial = -1;
                                                    queue.add(arrChunked);
                                                    break;
                                                }
                                            } catch(MarshalException|ServerError marshallException){
                                                
                                            } catch (RemoteException remoteEx) {
                                                JFrame js = new JFrame();
                                                JOptionPane.showMessageDialog(js, remoteEx);
                                                js.dispose();
                                            }
                                            chunkSpecial = (int) (chunkSpecial - (maxHostMemory * 0.01));
                                            remainderSpecial = baseArrayLength;
                                        }
                                        synchronized (RMIClient.this) {
                                            RMIClient.this.notify();
                                        }
                                        startSpecial = endSpecial;
                                    }
                                }
                            }
                        } catch (NotBoundException e) {
                            System.err.println(e.toString());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ConnectException ex2){
                            JFrame js = new JFrame();
                            JOptionPane.showMessageDialog(js, ex2.getMessage(),"Timeout !!! di "+hostName,JOptionPane.ERROR_MESSAGE);
                            js.dispose();
                            queue.add(arrCapture);
                            success = false;
                            synchronized (RMIClient.this) {
                                RMIClient.this.notify();
                            }
                        } catch (ConnectIOException ex3){
                            JFrame js = new JFrame();
                            JOptionPane.showMessageDialog(js, ex3.getMessage(), "koneksi tidak tersedia kepada host = " + hostName, JOptionPane.ERROR_MESSAGE);
                            js.dispose();
                            queue.add(arrCapture);
                            success = false;
                            synchronized (RMIClient.this) {
                                RMIClient.this.notify();
                            }
                        } catch (RemoteException ex4){
                            JFrame js = new JFrame();
                            JOptionPane.showMessageDialog(js, ex4.getMessage(), "Error tidak terduga pada host : " + hostName, JOptionPane.ERROR_MESSAGE);
                            js.dispose();
                            queue.add(arrCapture);
                            success = false;
                            synchronized (RMIClient.this) {
                                RMIClient.this.notify();
                            }
                        }catch (OutOfMemoryError outofmemory){
                            JFrame js = new JFrame();
                            JOptionPane.showMessageDialog(js, "Memori tidak cukup pada host "+hostName+", mohon tambah RAM Client atau server atau tambah jumlah host server.", outofmemory.getMessage(), JOptionPane.ERROR_MESSAGE);
                            js.dispose();
                            queue.add(arrCapture);
                            success = false;
                            synchronized (RMIClient.this) {
                                RMIClient.this.notify();
                            }
                        }
                    }                    
                };
            }
            ExecutorService exec = Executors.newWorkStealingPool(core);
            for (Runnable item : runnable) {
                exec.execute(item);
            }
            arr = null;
            exec.shutdown();
            if(sorted == Sorted.ASC){
                while (true) {
                    synchronized (RMIClient.this) {
                        try {
                            RMIClient.this.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    while (queue.size() > 1) {                           
                        queue.add(merge(queue.poll(), queue.poll()));                        
                    }                    
                    if(queue.arrSize() >= length)
                            break;
                }
            }else{
                while (true) {
                    synchronized (RMIClient.this) {
                        try {
                            RMIClient.this.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    while (queue.size() > 1) {
                        queue.add(mergeDesc(queue.poll(), queue.poll()));                        
                    }
                    if(queue.arrSize() >= length)
                            break;
                }
            }
            resultArr = queue.poll();
            if(!success)
                resultArr = null;
        }else{
            int[] tempArr = new int[length];
            for(int i = 0, index = 0; i < arr.length; i++){
                for(int j = 0; j < arr[i].length;j++){
                    tempArr[index++] = arr[i][j];
                }
            }
            DistributedMergeSort.sorting(tempArr, core, sorted);
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "distributed merge sort not executed");
            resultArr = tempArr;
        }
        long endTime = System.nanoTime();
        double elapsedTime = (endTime - startTime) / 1_000_000;
        return elapsedTime;
    }
    
    static int[] merge(int[] left,int[] right){
        int total = left.length + right.length;
        int[] arr = new int[total];
        int i, li , ri;
        i = li = ri = 0;
        while(i < total){
            if((li < left.length)&&(ri < right.length)){
                if(left[li] < right[ri]){
                    arr[i] = left[li];
                    li++;
                    i++;
                }else{
                    arr[i] = right[ri];
                    ri++;
                    i++;
                }
            }else{
                if(li >= left.length){
                    while(ri < right.length){
                        arr[i] = right[ri];
                        i++;
                        ri++;
                    }
                }
                if(ri >= right.length){
                    while(li < left.length){
                        arr[i] = left[li];
                        li++;
                        i++;
                    }
                }
            }
        }
        return arr;
    }
    
    static int[] mergeDesc(int[] left,int[] right){
        int total = left.length + right.length;
        int[] arr = new int[total];
        int i, li , ri;
        i = li = ri = 0;
        while(i < total){
            if((li < left.length)&&(ri < right.length)){
                if(left[li] > right[ri]){
                    arr[i] = left[li];
                    li++;
                    i++;
                }else{
                    arr[i] = right[ri];
                    ri++;
                    i++;
                }
            }else{
                if(li >= left.length){
                    while(ri < right.length){
                        arr[i] = right[ri];
                        i++;
                        ri++;
                    }
                }
                if(ri >= right.length){
                    while(li < left.length){
                        arr[i] = left[li];
                        li++;
                        i++;
                    }
                }
            }
        }
        return arr;
    }
    
    public int[] getArr(){
        return resultArr;
    }
}
