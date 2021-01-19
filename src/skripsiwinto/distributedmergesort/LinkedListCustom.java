/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.distributedmergesort;

/**
 *
 * @author Winto Junior Khosasi
 */
public class LinkedListCustom {
    private volatile int count = 0;
    private volatile int maxBuffer;
    
    LinkedListCustom(int maxBuffer){
        this.maxBuffer = maxBuffer;
    }

    private class LinkedList {

        private int[] arr = null;
        protected LinkedList obj = null;

        public LinkedList(int[] arr) {
            this.arr = arr;
            count++;
        }

        protected int[] get() {
            return arr;
        }
    }

    LinkedList obj = null;

    public synchronized int size() {
        return count;
    }
    
    public synchronized void add(int[] arr){
        if (obj == null) {
            obj = new LinkedList(arr);
        } else {
            LinkedList p = obj;
            while (p.obj != null) {
                p = p.obj;
            }
            p.obj = new LinkedList(arr);
        }
    }

    public synchronized void put(int[] arr) throws InterruptedException{
        while(count >= maxBuffer){
            wait();
        }
        if (obj == null) {
            obj = new LinkedList(arr);
        } else {
            LinkedList p = obj;
            while (p.obj != null) {
                p = p.obj;
            }
            p.obj = new LinkedList(arr);
        }
    }

    public synchronized int[] poll() {
        int[] arr = null;
        if (obj != null) {
            arr = obj.get();
            LinkedList p = obj.obj;
            obj.arr = null;
            obj = null;
            obj = p;
            count--;
        }
        notifyAll();
        return arr;
    }
    
    public synchronized int arrSize(){
        int length = obj == null ? 0 : obj.get().length;
        return length;
    }
}
