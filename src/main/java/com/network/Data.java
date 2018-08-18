package com.network;

public class Data {
    double[] in;
    double[] out;

    public Data(double[] in, double[] out) {
        this.in = in;
        this.out = out;
    }

    static class DataBuilder{
        double[] in;
        public Data out(double...out){
            return new Data(in,out);
        }

        public DataBuilder(double...in) {
            this.in = in;
        }
    }
    public static DataBuilder build(double...in){
        return new DataBuilder(in);
    }
}
