package com.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EvoNet {
    public static List<Data> data = new ArrayList<>();

    public static void main(String[] args) {
        List<Net> nets = generateSinTestOne();
        sort(nets);
        print(nets);

        int iter = 10000;
        runIters(iter, nets);

        print(nets);
        Net main = nets.get(0);
        System.out.println(main);
        Scanner s = new Scanner(System.in);
        while(true){
            List<Double> doubles = new ArrayList<>();
            System.out.println();
            while(doubles.size() != main.nodeCount[0]){
                System.out.print(">");
                doubles.add(s.nextDouble());
            }
            main.process(toArray(doubles));
            System.out.println(main.resultsToString());
        }
//        double[] score = runMuliSineTest();
//        for(int i =1;i<score.length;i++){
//            System.out.println(i + ":" + score[i]);
//        }
    }
    public static double[] runMuliSineTest(){
        double[] score = new double[30];
        for(int i = 1;i<19;i++){
            score[i] = runMuliSineTest(i);
        }
        return score;
    }
    private static double runMuliSineTest(int i){
        List<Net> nets = new ArrayList<>();
        for(int index =0;index<100;index++)nets.add(NetBuilder.build(1,i,1));
        runIters(1000,nets);
        return nets.get(0).getScore(data);
    }


    private static void runIters(int iter, List<Net> nets){
        System.out.println("running " + iter + " iterations");
        int startTimeSec = (int) (System.currentTimeMillis() / 1000);
        for(int i = 0;i<iter;i++){
            runIter(nets);
            if(i % (iter / 100) == 0){
                int nowTimeSec = (int)(System.currentTimeMillis() / 1000);
                int passedTime = nowTimeSec - startTimeSec;
                System.out.println(printLine(i / (iter / 100),passedTime));
            }
        }
        System.out.println("done!");
    }

    /*


     */

    /**
     *
     * @param pos is a number >= 0 && <= 100
     * @return
     */
    public static String printLine(int pos, int timeTaken){
        int other = 100-pos;
        String str = pos + "%  {";
        for(int i = 0;i<pos;i++){
            str+="#";
        }
        for(int i = 0;i<other;i++){
            str+="-";
        }
        str+="}";
        if(pos == 0)pos = 1;//no / by zero errors

        int timeLeft = (timeTaken / pos) * other;
        if(pos == 0) {
            str+="ETA calculating";
        }else if(timeLeft == 0){
            str+="ETA:<" + other + " seconds";
        }else {
            str += "ETA:" + timeLeft + " seconds";
        }
        return str;
    }


    public static double[] toArray(List<Double> doubles) {
        double[] ret = new double[doubles.size()];
        for (int i=0; i < ret.length; i++){
            ret[i] = doubles.get(i);
        }
        return ret;
    }


    private static List<Net> generateSinTestOne(){
        Net.effective = false;
        for(double i = - Math.PI;i<Math.PI;i+=0.001){
            data.add(new Data.DataBuilder(i).out((Math.sin(i)+1)*0.5));//times 0.5 lets me map numbers 0 to pi
        }
        List<Net> nets = new ArrayList<>();
        for(int i =-0;i<Math.pow(4,2);i++){
            nets.add(NetBuilder.build(1, 15,15,1));
        }
        return nets;
    }


    private static List<Net> generateTestOne(){
        data.add(new Data.DataBuilder(0,0).out(0,0));
        data.add(new Data.DataBuilder(0,1).out(0,1));
        data.add(new Data.DataBuilder(1,0).out(1,0));
        data.add(new Data.DataBuilder(1,1).out(1,1));
        List<Net> nets = new ArrayList<>();
        for(int i =0;i<Math.pow(4,2);i++){
            nets.add(NetBuilder.buildNetThree());
        }
        return nets;
    }



    private static List<Net> runIter(List<Net> nets) {
        sort(nets);
        int half = nets.size()/2;
        for(int lowerI = half;lowerI<nets.size();lowerI++){
            int higherI = (int)(Math.random()*half);
//            System.out.println("debug:-------------------higherI:" + higherI);
            nets.set(lowerI,nets.get(higherI).similar());
            nets.set(higherI,nets.get(higherI).similar());
        }
        return nets;
    }


    private static List<Net> sort(List<Net> nets) {
        for(int i = 0;i<nets.size();i++){
            int smallest = i;
            for(int index= i+1;index<nets.size();index++){
                if(nets.get(index).getScore(data) < nets.get(smallest).getScore(data)){
                    smallest = index;
                }
            }
            Net temp = nets.get(smallest);
            nets.set(smallest,nets.get(i));
            nets.set(i,temp);
        }

        return nets;
    }

    private static void print(List<Net> nets){
        sort(nets);
        System.out.println("------------------");
        for(int i = 0;i<nets.size();i++){
            System.out.println(toString(i,2) +":" + Net.toString(nets.get(i).getScore(data)));
        }
        System.out.println("===avg:" + avg(nets));
        System.out.println("------------------");
    }

    private static String toString(Object o, int length){
        String str = o.toString();
        while(str.length() < length){
            str = " " + str;
        }
        return str;
    }

    private static double avg(List<Net> nets){
        double avg = 0;
        for(Net net : nets){
            avg+=net.getScore(data);
        }
        return (avg *1d)/ nets.size();
    }

}


/*


0:  with 10_000 iters
    took aprox 600 seconds
    16 iters a second
    1000 iters a minute
    60_000 iters an hour
    700_000 iters in 12 hours

1: 700_000

2: 1.5 MIL

0:
0:0.2612085203091752
1:-2.575862104056891
2:0.36036705837192784
R:0.0

1:
0:0.9104269763870121
1:0.7819723526977265
2:0.8754560427354008
R:0.84147098481

1.5:
0:0.9111323403267946
1:0.9175038332306003
2:0.9310442679727318
R:0.9974949866

2:
0:0.8781822194876747
1:0.8874951730433929
2:0.9240239162550066
R:0.90929742683

3:
0:0.14694707851334865
1:-1.5160050805529477
2:0.19258363059116546
R:0.14112000806

3.1415:
0:0.07366257559674594
1:-1.3598976398487526
2:0.08870714611556618
R:0.00009265359

as many number of pi possible:
0:0.07362332360239798    0.07
1:-1.3597582226452116    1.35
2:0.08866994073594969    0.08
R:0

 */