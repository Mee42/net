package com.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Image{
    short[][] pixels;
    int id;
    public Image(String str){
        String[] split = str.split(",");
        id = Integer.parseInt(split[0]);
        pixels = new short[28][28];
        int total = 1;
        for(int x = 0;x<28;x++){
            for(int y = 0;y<28;y++){
                pixels[x][y] = Short.parseShort(split[total]);
                total++;
            }
        }
    }

    public Data toData(){
        double[] in = new double[784];
        int index = 0;//we can use a separate index to ensure we have no overlap because the actual order the pixels are imputed does not matter as long as they are consistent throughout everything
        for(int x = 0;x<28;x++){
            for(int y = 0;y<28;y++){
                in[index] = (pixels[x][y] *1d) / 255d;//   / 255 to get a number between 0 and 1
                index++;
            }
        }
        double[] out = new double[10];
        for(int i = 0;i<10;i++){
            if(i == id){
                out[i] = 1;
            }else{
                out[i] = 0;
            }
        }
        return new Data(in, out);
    }


    public void print() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        String str = "ID:" + id + "\n";
        for(int x = 0;x<28;x++){
            for(int y = 0;y<28;y++){
                str+=format(pixels[x][y]);
            }
            str+="\n";
        }
        return str;
    }

    private String format(int i){
        String str = i + "";
        while(str.length() < 3){
             str+=" ";
        }
        return str;
    }


    public static List<Image> read() throws IOException {
        System.out.print("reading....");
        List<Image> images = new ArrayList<>();
        BufferedReader r = new BufferedReader(new FileReader(new File("data/mnist_train.csv")));
        String line = r.readLine();
        while(line != null){
            images.add(new Image(line));
            line = r.readLine();
        }
        System.out.println("read!");
        return images;
    }


}