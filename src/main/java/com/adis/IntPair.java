package com.adis;

public record IntPair(int i, int j) {
    @Override
    public String toString() {
        return "[" + i + ", " + j + "]";
    }
}
