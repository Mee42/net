package com.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ImgEvoNet {

    public static List<Data> data = new ArrayList<>();
    private static List<Data> allData;

    private static final int NET_COUNT = 20;
    private static final int IMAGE_COUNT = 10_000;
    private static final int TEST_COUNT = 100_000;

    private static int imageIndex = 0;

    public static void main(String[] args) throws IOException {
        List<Net> nets = new ArrayList<>();
        for(int i =0;i<NET_COUNT;i++)nets.add(NetBuilder.build((int)Math.pow(28,2),16,16,10));
        allData = new ArrayList<>();
        List<Image> images = Image.read();
        for(Image image : images){
            allData.add(image.toData());
        }
        resetData();
        print(nets);
        Scanner scan = new Scanner(System.in);
        int iters = 100;//10,000 will last all night at 10,000 test iamges
        while(true) {

            if(iters > 0) {
                runIters(iters, nets, true);
                beep();
                print(nets);
            }else if(iters == -1){
                images.get(imgIndex()).print();
                nets.get(0).process(allData.get(imageIndex).in);
                System.out.println(nets.get(0).resultsToString());
            }else if(iters == -2){
                int correct = 0;
                int total = 0;
                System.out.println("running "+TEST_COUNT+" tests...");
                for(int i = 0;i<TEST_COUNT;i++){
                    int answer= images.get(imgIndex()).id;
                    int generated = nets.get(0).process(allData.get(imageIndex).in).bestAnswer();
                    if(answer == generated){
                        correct++;
                    }
                    total++;
                }
                System.out.println("correct:" + (1d * correct)/(total * 1d)*100d + "%");
            }
            String str = scan.nextLine();
            try{
                iters = Integer.parseInt(str);
            }catch(NumberFormatException e){
                iters = -5;//error, do nothing
            }

        }

    }


    public static void beep(){
        ProcessBuilder p = new ProcessBuilder();
        p.command("bash","/home/carson/beep/beep");
        try {
            p.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static int imgIndex(){
        if(imageIndex > allData.size()-10){
            imageIndex = 0;//just for safety
            return imageIndex;
        }
        imageIndex++;
        return imageIndex;
    }


    private static void runIters(int iter, List<Net> nets,boolean print){
        if(print)System.out.println("running " + iter + " iterations");
        int startTimeSec = (int) (System.currentTimeMillis() / 1000);
        for(int i = 0;i<iter;i++){
            runIter(nets);
            if(iter / 100 == 0 || i % (iter / 100) == 0){
                int nowTimeSec = (int)(System.currentTimeMillis() / 1000);
                int passedTime = nowTimeSec - startTimeSec;
                if(print)System.out.println(printLine((int)((i*1d) / (iter * 1d / 100d)),passedTime));
            }
            if(iter / 5 != 0 && i % (iter / 5)==0){
                System.out.print("==epoch score:");
                resetData();
                double score = nets.get(0).getScore(data);
                System.out.println(score);
            }
            resetData();
        }
        if(print)System.out.println("done!");
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

    private static void resetData(){
        List<Data> filteredData = new ArrayList<>();

        while(filteredData.size() < IMAGE_COUNT){//only use X datapoints, for now
            filteredData.add(allData.get(imgIndex()));
        }
        data = filteredData;
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
        System.out.println("------------------");
        double total = 0;
        for(int i = 0;i<nets.size();i++){
            double score = nets.get(i).getScore(data);
            total+=score;
            System.out.println(toString(i,2) +":" + Net.toString(score));
        }
        System.out.println("===avg:" + (total / nets.size()));
        System.out.println("------------------");
    }

    private static String toString(Object o, int length){
        String str = o.toString();
        while(str.length() < length){
            str = " " + str;
        }
        return str;
    }



}
