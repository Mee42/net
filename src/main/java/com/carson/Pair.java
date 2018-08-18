package com.carson;

public class Pair {
    final public int in;
    final public int out;

    public Pair(int in, int out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public String toString() {
        return "PAIR:   F(" + in + ") = " + out;
    }
}
