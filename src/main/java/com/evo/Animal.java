package com.evo;

import com.network.*;

public class Animal {
    private final int id;
    private static int nextId = 0;
    private Net net;

    public Animal(Net net) {
        this.net = net;
        id = nextId++;
    }
}
