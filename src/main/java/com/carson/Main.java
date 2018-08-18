package com.carson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static List<Pair> pairs;


    public static void main(String[] args) throws IOException {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");

        Main.pairs = new ArrayList<>();

        MathBot real = new MathBot(1,0);
        for(int i = -100;i<100;i++) {
            Main.pairs.add(new Pair(i, (int) (real.calc(i) + 1d * Math.random()*3d-1.5)));
            System.out.println(pairs.get(pairs.size()-1).in + ":" + pairs.get(pairs.size()-1).out);
        }
        List<MathBot> bots = new ArrayList<>();

        for(int i = 0;i<Math.pow(100,2);i++){
            bots.add(new MathBot());
        }

        System.out.println(bots.get(0).toString());
        new ImageMaker().makeImage(bots, fix(0));
//        double effective = 1;see below
        for(int i = 1;i<20;i++) {
            for(int temp = 0;temp<1;temp++) {
                Main.runOne(bots);
            }
//            if(i % 10 == 0)
//            MathBot.base = -1;
//            effective-=(1d / 150d);//we want bigger changes in the inital stages, and then tiny changes. not sure how well this works, we are going to have some problems with outliers always being generated
//            //currently not being used, keeping code
            //having no effective, (ie stable effective of 0.5), leads to slower finding of the inital ballpark value, but then fluctuates 1-2 numbers afterwards, which leads to everything after 40 iterations being the same
            Collections.sort(bots);
            System.out.println(bots.get(0).toString() + "  :  " + bots.get(0).getScore(pairs));
            new ImageMaker().makeImage(bots, fix(i));
            //starting the mathbots at a bigger range leads to a higher base,(48950), and therefor a darker screen after 10 iterations
            //setting the base every 10 leads to the effective of every change being visible, and also decreases the base to around 150-200 after a couple iterations
        }
        System.out.println("DONE");
//        print(bots);
    }


    public static void print(List<MathBot> bots){
        System.out.println("*************");
        Collections.sort(bots);
        int count = 0;
        String equal = bots.get(0).toString();
        for(MathBot bot : bots){
            if(equal.equals(bot.toString())){
                count++;
            }else if(count != -1){
                System.out.println("COUNT:" + count);
                count=-1;
            }
            System.out.println(bot);
        }
    }

    public static void runOne(List<MathBot> bots){
        Collections.sort(bots);
        int higherIndex = 0;
        for(int lowerIndex = bots.size() / 2;lowerIndex<bots.size();lowerIndex++){
            higherIndex = (int)(Math.random()*(bots.size() /2));//randomizes higherIndex, so lower half doesn't look like two gradients split in half
            bots.set(lowerIndex, bots.get(higherIndex).similar());
            bots.set(higherIndex, bots.get(higherIndex).similar());//remove this to get a perfect top half
            higherIndex++;//see two lines above this
        }
    }


    public static String fix(int i){
        String str = String.valueOf(i);
        while(str.length()<=3){
            str = "0" + str;
        }
        return str;
    }
}



