/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.distributedmergesort;

import java.nio.file.Path;
import java.rmi.RemoteException;
import javax.swing.SwingWorker;
import skripsiwinto.TampilanServer;
import skripsiwinto.mergesort.DistributedMergeSort;
import skripsiwinto.mergesort.Sorted;

/**
 *
 * @author Winto Junior Khosasi
 */
public class RMIServerLogged implements sorting{

    public enum LOGOPTION{
        NO_LOG,NORMAL_LOG,DEBUG_LOG
    }
    
    private final TampilanServer form;
    private final byte core;
    private final Path file;
    
    private final LOGOPTION option;
    private final boolean autoSave;
    
    private SwingWorker gui;
    
    public RMIServerLogged(TampilanServer form, Path file, byte core, LOGOPTION option, boolean autoSave){
        this.form = form;
        this.core = core;
        this.option = option;
        this.autoSave = autoSave;
        this.file = file;
    }
    
    @Override
    public int[] sort(int[] arr, Sorted sorted) throws RemoteException {
        gui = new UILogger(form, "Array untuk di urutkan diterima", file, autoSave);
        gui.execute();
        if(option == LOGOPTION.DEBUG_LOG){
            gui = new UILogger(form, arr, file, autoSave);
            gui.execute();
        }
        long startTime = System.nanoTime();
        int[] tempArr = DistributedMergeSort.sorting(arr, core,sorted);
        long endTime = System.nanoTime();
        double elapsedTime = (endTime - startTime) / 1_000_000.0;
        gui = new UILogger(form, "Array selesai disortir.", file, autoSave);
        gui.execute();
        if(option == LOGOPTION.DEBUG_LOG){
            gui = new UILogger(form, tempArr, file, autoSave);
            gui.execute();
        }
        String temp = String.format("Elapsed time : %f ms%n",elapsedTime);
        gui = new UILogger(form, temp, file, autoSave);
        gui.execute();
        return tempArr;
    }
    
    @Override
    public long getMemory(){
        return Runtime.getRuntime().maxMemory();
    }
}
