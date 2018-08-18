package com.carson;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class MathBot implements Comparable<MathBot>{
    public int a = 0;
    public int b = 0;


    public int calc(int i){
        return a*i+b;
    }

    public MathBot(int a, int b) {
        this.a = a;
        this.b = b;
    }
    public MathBot(){
        this.a = (int)(Math.random()*50);
        this.b = (int)(Math.random()*50);
    }

    /*** smaller is better*/
    public int calc(Pair pair){
        return Math.abs(pair.out - calc(pair.in));
    }

    @Override
    public String toString() {
        return "MATHBOT:" + "  A:" + a + " B:" + b;
    }

    @Override
    public boolean equals(Object o) {
        return o.toString().equals(this.toString());
    }


    public double getScore(List<Pair> pairs){
        double score = 0;
        for(Pair pair : pairs){
            score+=calc(pair);//calcs distance from correct
        }
        return (score / (pairs.size()*1d));
    }

    public static int compare(MathBot a, MathBot b, List<Pair> pairs){
        if(a.equals(b)){
            return 0;
        }

//        double d = (a.getScore(pairs) - b.getScore(pairs));//TODO switch this if ordering wrong
        double d = (b.getScore(pairs) - a.getScore(pairs));
        if(d < 0)return -1;
        if(d > 0)return 1;
        return 0;
    }

    @Override
    public int compareTo(MathBot mathBot) {
        if(mathBot.equals(this))return 0;
        int result = compare(this, mathBot, Main.pairs);
        if(result > 0)return -1;
        if(result < 0)return 1;
        return 0;
    }

    public MathBot similar(double chance) {
        int tempA = this.a;
        int tempB = this.b;
        Random r = new Random();
        if(r.nextBoolean() && Math.random() < chance ){
            tempA++;
        }
        if(r.nextBoolean()&& Math.random() < chance){
            tempA--;
        }
        if(r.nextBoolean()&& Math.random() < chance){
            tempB++;
        }
        if(r.nextBoolean()&& Math.random() < chance){
            tempB--;
        }
        return new MathBot(tempA,tempB);
    }
    public MathBot similar(){
        int tempA = this.a;
        int tempB = this.b;
        Random r = new Random();
        if(r.nextBoolean()&& r.nextBoolean()){
            tempA++;
        }
        if(r.nextBoolean()&& r.nextBoolean()){
            tempA--;
        }
        if(r.nextBoolean()){
            tempB++;
        }
        if(r.nextBoolean()){
            tempB--;
        }
        return new MathBot(tempA,tempB);
    }

    public static int base = -1;
    public int toRGB(List<Pair> pairs){
        if(base == -1){
            throw new IllegalStateException("base must be set to a value other then -1 before calling");
        }
        double score = getScore(pairs);
//        System.out.print("score:" + score);
        //the further the score from zero, the more red
        score = Math.abs(score);
        //more score = more red

        //base score around base. 0red = 0score, 255red = (base)score
        score = score / base;
        //score = 1 / 255th
        score = score * 255;
//        System.out.println("  red:" + score);
        if(score > 254)score = 254;
        if(score < 1)score = 1;//to avoid exceptions if one-off from range
        return new Color((int)score, 0, 0).getRGB();//should cleanly cast to int
    }
}










