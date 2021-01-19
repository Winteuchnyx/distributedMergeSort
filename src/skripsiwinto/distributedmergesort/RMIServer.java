/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.distributedmergesort;

import skripsiwinto.mergesort.DistributedMergeSort;
import skripsiwinto.mergesort.Sorted;

/**
 *
 * @author Winto Junior Khosasi
 */
public class RMIServer implements sorting{

    private final byte core;
    
    public RMIServer(byte core){
        this.core = core;
    }
    
    @Override
    public int[] sort(int[] arr, Sorted sorted) {
        return DistributedMergeSort.sorting(arr,core,sorted);
    }
    
    @Override
    public long getMemory(){
        return Runtime.getRuntime().maxMemory();
    }
}
