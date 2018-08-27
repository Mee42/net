package com.network;

import java.util.List;
import java.util.Random;

public class Net {
    public static boolean effective = true;
    int layers;//this is at least 2, one for input and one for output
    double[][] nodes;//this is the values of the nodes
    //node[layer][node_index]

    int[] nodeCount;//per layer
    //nodeCount[layer] = how many nodes are on that layer

    int maxNodeLength;

    double[][][] wires;//the strength of the wires
    //wires[starting_layer][starting_index][ending_index]






    public static double[][][] generateEqualWires(double[][][] arr){//takes a array and flood all values with 1. needs an array passed to ensure size is correct
        for(int i1 = 0;i1<arr.length;i1++){
            for(int i2 = 0;i2<arr[i1].length;i2++){
                for(int i3 = 0;i3<arr[i1][i2].length;i3++){
                    arr[i1][i2][i3] = 0;
                }
            }
        }
        return arr;
    }

    public static double[][][] generateRandomWires(double[][][] arr){
        for(int i1 = 0;i1<arr.length;i1++){
            for(int i2 = 0;i2<arr[i1].length;i2++){
                for(int i3 = 0;i3<arr[i1][i2].length;i3++){
                    arr[i1][i2][i3] = Math.random() - 0.5;
                }
            }
        }
        return arr;

    }






    public Net resetValues(){
        for(int i = 0;i<nodes.length;i++){
            for(int i2 = 0;i2<nodes[i].length;i2++){
                nodes[i][i2] = 0;
            }
        }
        this.cost = -1;
        return this;
    }

    public Net process(double...input){
        if(input.length != nodeCount[0]){
            throw new IndexOutOfBoundsException("inputs need to align with layer0 nodes");
        }
        resetValues();
        nodes = new double[nodes.length][nodes[0].length];
        for(int i = 0;i<input.length;i++){
            nodes[0][i] = input[i];
        }
        for(int startingLayer = 0;startingLayer<layers - 1;startingLayer++){
            for(int startingIndex = 0;startingIndex<nodeCount[startingLayer];startingIndex++) {
                for (int endingIndex = 0; endingIndex < nodeCount[startingLayer + 1]; endingIndex++) {
                    double in = nodes[startingLayer][startingIndex];
                    if(in == 0)continue;//if in == 0, the ending node will not be changed
                    double wireStrength = wires[startingLayer][startingIndex][endingIndex];
                    double value = in*wireStrength;
                    nodes[startingLayer+1][endingIndex]+= value;//maybe run sigmoid in real-time?
                }
            }
            for(int index = 0;index<nodeCount[startingLayer+1];index++){
                nodes[startingLayer+1][index] = Sigmoid.sigmoid(nodes[startingLayer+1][index] + 10);//apply sigmoid before moving on to next layer
                //ten is the bias
            }
        }
        this.cost = -1;
        return this;
    }



    public double totalCost(List<Data> data){
        double cost = 0;
        for(Data d : data){
            cost+=generateCost(d.in,d.out);
        }
        this.cost = cost;// / (1d * data.size());
        return cost / (1d * data.size());
    }

    public double generateCost(double[] input, double[] expected){
        process(input);
        double[] generated = new double[expected.length];
        for(int i = 0;i<generated.length;i++){
            generated[i] = nodes[layers-1][i];
        }
        //https://youtu.be/IHZwWFHWa-w?t=4m11s
        double cost = 0;
        for(int i = 0;i<expected.length;i++){
//            System.out.println("got:" + generated[i] + " should have gotten:" + expected[i]);
            double calc = generated[i] - expected[i];
//            System.out.println("calc:" + calc);
            calc = Math.pow(calc,2);
            cost+=calc;
        }
        return cost;
    }

    /** should only be called for printing*/
    private double equilize(double d){
        double amount = 10000000;//CHANGED TODO set changed as an anotation
        return (double)((int)(d*amount)) * 1d / amount * 1d;
    }


    @Override
    public String toString() {
        String str ="NET:\nlayers:" + layers + "\n";
        str+="nodes:\n";
        for(int layer = 0;layer<nodes.length;layer++){
            str+="layer " + layer + ": ";
            String layerStr = "";
            for(int index = 0;index<nodeCount[layer];index++){
                layerStr+=equilize(nodes[layer][index]) + "  ";
            }
            str+=alignCenter(layerStr,maxNodeLength*4) + "\n";
        }
//        str+="wires\n";
        for(int startingLayer = 0;startingLayer<layers - 1;startingLayer++) {//loop through all wires
            for (int startingIndex = 0; startingIndex < nodeCount[startingLayer]; startingIndex++) {
                for (int endingIndex = 0; endingIndex < nodeCount[startingLayer + 1]; endingIndex++) {
                    str+=startingLayer + ":" + startingIndex + "  ->  " + (startingLayer+1) + ":" + endingIndex + "     value:" + wires[startingLayer][startingIndex][endingIndex] + "\n";
                }
            }
        }
        return str;
    }

    private String alignCenter(String str, int length){//improvable doesn't work very well if one layer has 784 and the others less then 20
        while(str.length() < length){
            if(str.length()+1 == length){
                return " " + str;//do not put two spaces if it only needs one
            }
            str = " " + str + " ";
        }
        return str;
    }





    Net(int layers, double[][] nodes, int[] nodeCount, double[][][] wires) {
        this.layers = layers;
        this.nodes = nodes;
        this.nodeCount = nodeCount;
        this.wires = wires;
        maxNodeLength = -1;
        for(int i :nodeCount){
            if(i > maxNodeLength)maxNodeLength = i;
        }
    }


    public void impove(List<Data> data){
        cost = -1;
        double[][][] newWires = new double[wires.length][wires[0].length][wires[0][0].length];
        double score = totalCost(data);
        int connections = 1;
        for(int startLayer = 0;startLayer<layers-1;startLayer++){//could be written into a lambda (doesn't actually speed it up though)
            for(int startIndex = 0;startIndex<nodeCount[startLayer];startIndex++){
                 connections+=nodeCount[startLayer+1];
            }
        }
        int on = 0;
        for(int startLayer = 0;startLayer<layers-1;startLayer++){//could be written into a lambda (doesn't actually speed it up though)
            for(int startIndex = 0;startIndex<nodeCount[startLayer];startIndex++){
                for(int endIndex = 0;endIndex<nodeCount[startLayer+1];endIndex++) {
                    if(startIndex % 100 == 0&& endIndex % 10 == 0) System.out.print("changing wire " + startLayer +":" + startIndex + " -> " + startLayer+1 + ":" + endIndex + " from " + wires[startLayer][startIndex][endIndex]);
                    wires[startLayer][startIndex][endIndex]+=1;
                    double scoreWithPlus = totalCost(data);
                    double partial = scoreWithPlus - score;
                    partial *= -1;//reverse the partial
                    //the partial tells us how C will change if we change X by one
                    //we want to figure out how to change X so C goes down
                    //so if C is positive, we want to decrease X
                    //and if C is negative, we want to increase X
                    //so we reverse the partial and add it to the wire
                    //so if X++ -> C++
                    //P = C++ - C
                    //P = -P
                    //X+=P
//                    newWires[startLayer][startIndex][endIndex] = getRealWires()[startLayer][startIndex][endIndex] + partial;//trying something new on the line below

                    wires[startLayer][startIndex][endIndex]-=2;
                    newWires[startLayer][startIndex][endIndex]+= partial;//update wires instantly
                    if(startIndex % 100 == 0 && endIndex % 10 == 0) System.out.println(" to " + wires[startLayer][startIndex][endIndex] + "  inital score:" + score + "   score with change:" + scoreWithPlus + "   partial:" + partial);
                    if (on % (connections / 100) == 0){
                        System.out.print(".");
                    }
                    on++;
                }

            }
            System.out.print("_");
            //12704 connections at:
            //L0:784
            //L1:16
            //L2:10
        }
        wires = newWires;
    }

    //notimprovable -> gets called for every wire. I should be able to just use the current net and change the value back for more processing
    //doing the above has no increase in speed
    private double[][][] wireCache;//CACHE V1
    public void backupWires(){
        wireCache = wires;
    }
    public void recall(){
        wires = wireCache;
    }
    public double[][][] getRealWires(){
        return wireCache;
    }



    private int[] clone(int[] arr){
        int[] ne = new int[arr.length];
        for(int i = 0;i<arr.length;i++)ne[i] = arr[i];
        return ne;
    }
    private double[] clone(double[] arr){
        double[] ne = new double[arr.length];
        for(int i = 0;i<arr.length;i++)ne[i] = arr[i];
        return ne;
    }
    private double[][] clone(double[][] arr){
        double[][] ne = new double[arr.length][arr[0].length];
        for(int i = 0;i<arr.length;i++)ne[i] = clone(arr[i]);
        return ne;
    }
    private double[][][] clone(double[][][] arr){
        double[][][] ne = new double[arr.length][arr[0].length][arr[0][0].length];
        for(int i = 0;i<arr.length;i++)ne[i] = clone(arr[i]);
        return ne;
    }

    public int bestAnswer(){
        int best = 0;
        for(int i = 0;i<nodeCount[layers-1];i++){
            if(nodes[layers-1][i] > nodes[layers-1][best]){
                best = i;
            }
        }
        return best;
    }

    public String resultsToString(){
        double most = -999;
        int mostIndex = 0;
        String str = "";
        for(int i = 0;i<nodeCount[layers-1];i++){
            if(nodes[layers-1][i] > most){
                mostIndex = i;
                most = nodes[layers-1][i];
            }
            str+=i +":" + toString(nodes[layers-1][i]) + "\n";
         }
        return str + "best answer:" + mostIndex + "    :" + most + "\n";
    }

    public static String toString(double value){
        //README ONLY FOR SINE FUNCTION
        value = (value / 0.5)-1;
        //README ONLY FOR THE SINE FUNCTION
        String str = String.valueOf(value);
        if(!effective)return str;
        if(value > 0 && value < 0.0000001){
            str = "effectivly 0             (E" + str.split("E")[1] + ")";//take just the E value
        }
        return str;
    }

    private double cost = -1;
    public double getScore(List<Data> data) {
        if(cost == -1){
            cost = totalCost(data);
        }
        return cost;
    }


    public Net similar(){
        double[][][] newWires = clone(wires);

        Random r = new Random();
        for(int i = 0;i<newWires.length;i++) {
            for (int ii = 0; ii < newWires[0].length; ii++) {
                for(int iii = 0;iii<newWires[0][0].length;iii++){
                    double value = newWires[i][ii][iii];
                    if(r.nextInt() % 100 == 0){
                        value+=Math.random() - 0.5;
                        newWires[i][ii][iii] = value;
                        continue;
                    }
                }
            }
        }

        return new Net(layers, clone(nodes),clone(nodeCount),newWires);
    }
//    int layers;//this is at least 2, one for input and one for output
//    double[][] nodes;//this is the values of the nodes
//    //nodes[layer][node_index]
//
//    int[] nodeCount;//per layer
//    //nodeCount[layer] = how many nodes are on that layer
//
//    int maxNodeLength;
//
//    double[][][] wires;//the strength of the wires
//    //wires[starting_layer][starting_index][ending_index]
}
