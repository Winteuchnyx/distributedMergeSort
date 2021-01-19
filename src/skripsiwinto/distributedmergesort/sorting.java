/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.distributedmergesort;

import java.rmi.Remote;
import java.rmi.RemoteException;
import skripsiwinto.mergesort.Sorted;

/**
 *
 * @author Winto Junior Khosasi
 */
public interface sorting extends Remote{
    int[] sort(int[] arr, Sorted sorted)throws RemoteException;
    long getMemory() throws RemoteException;
}
