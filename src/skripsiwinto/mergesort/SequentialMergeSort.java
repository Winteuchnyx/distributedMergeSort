/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto.mergesort;

/**
 *
 * @author Winto Junior Khosasi
 */
public class SequentialMergeSort {
    private static void mergeSort(int left, int right, int[] arr) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(left, mid, arr);
            mergeSort(mid + 1, right, arr);

            merge(left, mid, right, arr);
        }
    }

    private static void merge(int left, int mid, int right, int[] arr) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] tempArrLeft = new int[n1];
        int[] tempArrRight = new int[n2];
        int i = left, li = 0, ri = 0;

        for (int j = 0; j < n1; j++) {
            tempArrLeft[j] = arr[left + j];
        }

        for (int j = 0; j < n2; j++) {
            tempArrRight[j] = arr[mid + j + 1];
        }
        while (li < n1 && ri < n2) {
            if (tempArrLeft[li] <= tempArrRight[ri]) {
                arr[i++] = tempArrLeft[li++];
            } else {
                arr[i++] = tempArrRight[ri++];
            }
        }

        while (li < n1) {
            arr[i++] = tempArrLeft[li++];
        }

        while (ri < n2) {
            arr[i++] = tempArrRight[ri++];
        }
    }

    public static void sort(int[] arr,Sorted sorted) {
        if(sorted == Sorted.DESC){
            mergeSortDesc(0, arr.length - 1, arr);
        }else{
            mergeSort(0, arr.length - 1, arr);            
        }
    }
    
    private static void mergeSortDesc(int left, int right, int[] arr) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortDesc(left, mid, arr);
            mergeSortDesc(mid + 1, right, arr);

            mergeDesc(left, mid, right, arr);
        }
    }

    private static void mergeDesc(int left, int mid, int right, int[] arr) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] tempArrLeft = new int[n1];
        int[] tempArrRight = new int[n2];
        int i = left, li = 0, ri = 0;

        for (int j = 0; j < n1; j++) {
            tempArrLeft[j] = arr[left + j];
        }

        for (int j = 0; j < n2; j++) {
            tempArrRight[j] = arr[mid + j + 1];
        }
        while (li < n1 && ri < n2) {
            if (tempArrLeft[li] >= tempArrRight[ri]) {
                arr[i++] = tempArrLeft[li++];
            } else {
                arr[i++] = tempArrRight[ri++];
            }
        }

        while (li < n1) {
            arr[i++] = tempArrLeft[li++];
        }

        while (ri < n2) {
            arr[i++] = tempArrRight[ri++];
        }
    }
}
