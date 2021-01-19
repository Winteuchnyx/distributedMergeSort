/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.mergesort;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author Winto Junior Khosasi
 */
public class ParallelMergeSort extends RecursiveAction{
    protected int low;
    protected int high;
    protected int[] array;

    private static final int SEQ_THRESHOLD = 1000000;

    ParallelMergeSort(int[] arr, int low, int high) {
        array = arr;
        this.low = low;
        this.high = high;
    }

    @Override
    protected void compute() {
        if ((low) <= SEQ_THRESHOLD) {
            SequentialMergeSort.sort(array,Sorted.ASC);
        } else {
            int mid = (high + low) / 2;
            if (low < high) {

                ParallelMergeSort left = new ParallelMergeSort(array, low, mid);
                ParallelMergeSort right = new ParallelMergeSort(array, mid + 1, high);
                invokeAll(left, right);
                merge(low, mid, high);
            }
        }
    }

    private void merge(int low, int mid, int high) {
        int n1 = mid - low + 1;
        int n2 = high - low + 1;
        int[] tempMergArr = new int[n2];
        for (int in = low, id = 0; in <= high; in++) {
            tempMergArr[id++] = array[in];
        }

        int i = 0;
        int j = n1;
        int k = low;

        while (i < n1 && j < n2) {
            if (tempMergArr[i] <= tempMergArr[j]) {
                array[k] = tempMergArr[i++];
            } else {
                array[k] = tempMergArr[j++];
            }
            k++;
        }
        while (i < n1) {
            array[k++] = tempMergArr[i++];
        }

        while (j < n2) {
            array[k++] = tempMergArr[j++];
        }
    }

    public static void sort(int[] arr,byte core, Sorted sorted) {
        ForkJoinPool fp = new ForkJoinPool(core);
        if(sorted == Sorted.DESC){
            ParallelMergeSortDesc obj = new ParallelMergeSortDesc(arr, 0, arr.length - 1);
            fp.invoke(obj);
        }else{
            ParallelMergeSort obj = new ParallelMergeSort(arr, 0, arr.length - 1);
            fp.invoke(obj);
        }        
    }
    
    private static class ParallelMergeSortDesc extends RecursiveAction {

        protected int low;
        protected int high;
        protected int[] array;

        private static final int SEQ_THRESHOLD = 1000;

        ParallelMergeSortDesc(int[] arr, int low, int high) {
            array = arr;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if ((low) <= SEQ_THRESHOLD) {
                SequentialMergeSort.sort(array, Sorted.DESC);
            } else {
                int mid = (high + low) / 2;
                if (low < high) {

                    ParallelMergeSortDesc left = new ParallelMergeSortDesc(array, low, mid);
                    ParallelMergeSortDesc right = new ParallelMergeSortDesc(array, mid + 1, high);
                    invokeAll(left, right);
                    mergeDesc(low, mid, high);
                }
            }
        }

        private void mergeDesc(int low, int mid, int high) {
            int n1 = mid - low + 1;
            int n2 = high - low + 1;
            int[] tempMergArr = new int[n2];
            for (int in = low, id = 0; in <= high; in++) {
                tempMergArr[id++] = array[in];
            }

            int i = 0;
            int j = n1;
            int k = low;

            while (i < n1 && j < n2) {
                if (tempMergArr[i] >= tempMergArr[j]) {
                    array[k] = tempMergArr[i++];
                } else {
                    array[k] = tempMergArr[j++];
                }
                k++;
            }
            while (i < n1) {
                array[k++] = tempMergArr[i++];
            }

            while (j < n2) {
                array[k++] = tempMergArr[j++];
            }
        }
    }
}
