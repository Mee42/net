package com.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        Sigmoid.initulize();
        Net net = NetBuilder.buildNetFive();
        List<Data> allData = new ArrayList<>();
        List<Image> images = Image.read();
        for(Image image : images){
            allData.add(image.toData());
        }
        List<Data> filteredData = new ArrayList<>();
        int index = 0;
        while(filteredData.size() < 100){//only use 100 datapoints, for now
            filteredData.add(allData.get(index));
        }
        System.out.print("running inital scoring...");
        double startCost = net.totalCost(filteredData);
        System.out.println("score:" + startCost);
        long time = System.currentTimeMillis();
        print(net);
        index++;//doesn't matter if we skip a few
        Image img = images.get(index);//use the same images for all of them
        index++;
        for (int i = 0; i < 10_000; i++) {//2k a night
            System.out.print("improving, iteration:" + i + "...");
            net.impove(filteredData);
            System.out.println("done!");
        Data data = img.toData();
            img.print();
            System.out.println("is");
            net.process(data.in);
            System.out.println(net.resultsToString());
            print(net);
        }
        System.out.print("running end scoring...");
        double endCost = net.totalCost(filteredData);
        int sec = (int)((System.currentTimeMillis() - time) / 1000);

        System.out.println("score:" + endCost);
        System.out.println();
        System.out.println("seconds taken:" + sec);
        print(net);
        Image testImg = null;
        index++;
        while(testImg == null){
            if(images.get(index).id == 1){
                testImg = images.get(index);
            }
        }
        Data data = testImg.toData();
        testImg.print();
        net.process(data.in);
        System.out.println(net.resultsToString());
    }

    private static void print(Net net){
        System.out.println(net);
    }
}

/*
 *
 * optimization done:
 * initial time (per iteration)                                  : 75 seconds
 *
 * after implementing a wire cache                               : 26 seconds
 *
 * after cache v2                                                : 24 seconds
 * however cache v2 didn't work. going back to v1
 *
 * going back to v1 wire cache                                   : 24 seconds
 * huh
 *
 * removing double shortening                                    : 26 seconds
 * (never should have been a thing
 *  anyway)
 *
 * added sigmoid counted                                         : 24 seconds
 * sigmoid called 33038200 times
 * this can't be effective. anyway to
 * decrease?
 *
 *
 * added if(in == 0)continue;                                    : 22 seconds
 * this should skip a lot of the
 * 0 valued pixels
 *
 * added tracer for seconds spent on                             : 24 seconds
 * sigmoids,4821MS, almost 5 seconds
 *
 * reusing sigmoid operations,                                   : 21 seconds
 * still have counter and timer
 * 145521 sigmoid calls
 * 2442 MS
 * keep in mind the 2442 ms will only go
 * down as we have more iterations
 * *tested twice*
 * running ten iterations confirms this, with values being:     : 22 seconds per.it
 * (raw -- divided by ten -- one iter. value -- note)
 * calls: 1438695   --    143,869 -- 145,521   --  just a bit more
 * ms:    30,828     --     3,082 -- 2,442     --  a bit more, interestingly enough. pre-generating the table could save a lot of time though
 *
 * added initulizer for sigmoids up to four decimal places     : 22 seconds
 * now:   145521
 * before:145521
 * legit the same
 * running a shit ton more decimal places, but starting at 0.9 :
 *
 *
 *
 * switching to no cache
 *
 *
 *
 */