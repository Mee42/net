package com.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    //wires[starting_layer][starting_index][ending_index]
    //node[layer][node_index]
    //nodeCount[layer] = how many nodes are on that layer

    public static void main(String[] args) {
        Net net = NetBuilder.buildNetOne();
        net.wires = Net.generateRandomWires(net.wires);
        List<Data> data = getData1();
        double cost = net.totalCost(data);
        System.out.println("    ======================cost:" + cost);
        print(net);
        for(int i =0;i<100;i++) {
            net.impove(data);
            print(net);
            cost = net.totalCost(data);
            System.out.println("i:"  + i +  "  ======================cost:" + cost);
        }

        Scanner scan = new Scanner(System.in);
        while(true){
            double one = scan.nextDouble();
            double two = scan.nextDouble();
            System.out.print("===  " + one +"," + two + "  ->  0:" +net.process(one,two).nodes[1][0] + " 1:" + net.nodes[1][1] + "  most likely answer=" + ((net.nodes[1][0] > net.nodes[1][1])?"0":"1")+ "\n");
            System.out.println(one +"," + two + "  -> " + ((net.nodes[1][0] > net.nodes[1][1])?"0":"1"));
        }
    }

    //i:9999  ======================cost:0.125
    //i:99999  ======================cost:0.125
    //i:99999  ======================cost:0.125

    private static List<Data> getData1() {
        List<Data> data = new ArrayList<>();
        data.add(Data.build(1,0).out(1,0));
        data.add(Data.build(0,0).out(0,1));
        data.add(Data.build(1,1).out(1,0));
        data.add(Data.build(0,1).out(0,1));
        return data;
    }

    private static void print(Net net){
        System.out.println(net);
    }

    private static void netTwoConfOne(Net net){
        net.wires[0][0][1] = 0;//config one, no diagonal wires
        net.wires[0][1][0] = 0;
        net.wires[1][0][1] = 2;
        net.wires[1][1][0] = 0;
        net.wires[0][1][1] = 2;
        net.wires[1][1][1] = 2;
    }

    private static void netFourConfOne(Net net){
        net.wires[0][0][0] = 2;
        net.wires[0][1][0] = 1;
    }




}


/*
 cost:2.0
NET:
layers:3
nodes:
layer 0: 0.0  1.0
layer 1: 0.29  0.09
layer 2: 0.2839  0.1709
wires
0:0  ->  1:0     value:0.1
0:0  ->  1:1     value:0.24
0:1  ->  1:0     value:0.29
0:1  ->  1:1     value:0.09
1:0  ->  2:0     value:0.83
1:0  ->  2:1     value:0.31
1:1  ->  2:0     value:0.48
1:1  ->  2:1     value:0.9
*/