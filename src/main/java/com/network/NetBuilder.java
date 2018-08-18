package com.network;

public class NetBuilder {
    public static Net buildNetOne(){
        int maxNodes = 2;
        int layers = 2;
        return new Net(layers,
                new double[2][2],
                new int[]{2,2},
                Net.generateRandomWires(new double[layers][maxNodes][maxNodes]));
    }
    public static Net buildNetTwo(){
        int maxNodes = 2;
        int layers = 3;
        return new Net(layers,
                new double[layers][maxNodes],
                new int[]{2,2,2},
                Net.generateEqualWires(new double[layers][maxNodes][maxNodes]));
    }

    public static Net buildNetThree(){
        int maxNodes = 3;
        int layers = 3;
        return new Net(layers,
                new double[layers][maxNodes],
                new int[]{2,3,2},
                Net.generateEqualWires(new double[layers][maxNodes][maxNodes]));
    }
    public static Net buildNetFour(){
        int maxNodes = 2;
        int layers = 2;
        return new Net(layers,
                new double[layers][maxNodes],
                new int[]{2,1},
                Net.generateEqualWires(new double[layers][maxNodes][maxNodes]));
    }

    public static Net buildNetTempOne(){
        int maxNodes = 10;
        int layers = 10;
        return new Net(layers,
                new double[layers][maxNodes],
                new int[]{2,5,7,10,10,10,7,5,3,2},
                Net.generateEqualWires(new double[layers][maxNodes][maxNodes]));
    }

    public static Net buildNetFive(){
        int maxNodes = 784;
        int layers = 3;
        return new Net(layers,
                new double[layers][maxNodes],
                new int[]{784,16,10},
                Net.generateRandomWires(new double[layers][maxNodes][maxNodes]));
    }

    public static Net buildNetSix() {
        int maxNodes = 16;
        int layers = 3;
        return new Net(layers,
                new double[layers][maxNodes],
                new int[]{1,16,16,1},
                Net.generateRandomWires(new double[layers][maxNodes][maxNodes]));
    }

    public static Net build(int...nodeCount){
        int maxNodes = -1;
        for(int i : nodeCount)
            if(i > maxNodes)
                maxNodes = i;
        int layers = nodeCount.length;
        return new Net(layers,
                new double[layers][maxNodes],
                nodeCount,
                Net.generateRandomWires(new double[layers][maxNodes][maxNodes]));
    }

}
