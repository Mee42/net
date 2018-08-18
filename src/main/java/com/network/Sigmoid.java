package com.network;

public class Sigmoid {
//    private static Map<Double,Double> sigmoids = new HashMap<>();
//    static{
        //0.9971999999999065
        //0.0000000000000001
//        for(double d = 0.9;d<1;d+=0.0000000000000001){
//            sigmoid(d);
//        }
//    }
    public static double sigmoid(Double x){
//        if(sigmoids.containsKey(x)){
//            return sigmoids.get(x);
//        }
//        double out =  1 / (1 + Math.exp(-x));//legit
        double out = x / (1 + Math.abs(x));//faster(TODO has not been tested)
//        sigmoids.put(in, out);//need to remove in order to stop memory overload of 8000000GiB
//        System.out.println("sigmoid of " + in + " is " + out);
        return out;
    }

    public static void initulize() {
//        sigmoids.size();//doesn't actually do anything anymore
//        Net.sigmoidCalls = 0;
    }
}

/*
 *
 * 33038200 calls
 *
 *
 */