package com.loggin.and.testing;

import java.util.Arrays;

public class ArrayClass {

    public     int [] takeLasts (int [] inputArray){
        final int FOUR=4;
        int [] returnArray;
        if (inputArray.length==0) throw  new RuntimeException("Массив пустой");
        if (inputArray.length/FOUR<1) throw new RuntimeException("Массив должен содержать хотя бы одну чертверку");
        if (inputArray.length%FOUR==0) throw new RuntimeException("В массив разбит на четверки");
        int sizeArray=inputArray.length%FOUR;
        returnArray=new int[sizeArray];
        for (int i = inputArray.length-1, j=sizeArray-1;   i > inputArray.length-1-sizeArray ; i--, j--) {
            returnArray[j]=inputArray[i];
        }
        return returnArray;
    }

    public  boolean checkArray (int [] inputArray){
        final int NUMB_ONE=1;
        final int NUMB_FOUR=4;
        boolean boolOne=false;
        boolean boolFour=false;
    if (inputArray.length==0) throw  new RuntimeException("Массив пустой");
        for (int element : inputArray) {
            if (element==NUMB_FOUR){
                boolFour=true;
                continue;
            }else if (element==NUMB_ONE){
                boolOne=true;
                continue;
            }return false;
        }if (boolOne==true && boolFour==true) return true;
        return false;
    }

//    public static void main(String[] args) {
//        int [] array ={1,1,1,4,1,4,1,4,1,4};
//        System.out.println(Arrays.toString(takeLasts(array)));
//        System.out.println(checkArray(array));
//    }
}

