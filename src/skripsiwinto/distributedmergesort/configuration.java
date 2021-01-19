/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.distributedmergesort;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Winto Junior Khosasi
 */
public interface configuration extends Remote{
    void aktivasiUlang() throws RemoteException;
}
