package com.evo;

import com.network.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static int ANIMAL_COUNT = 10;

    public static void main(String[] args) {
        List<Animal> animals = new ArrayList<>();
        for(int i = 0; i< ANIMAL_COUNT; i++){
            animals.add(new Animal(NetBuilder.build(2,5,5,2)));
        }
        




    }
}
