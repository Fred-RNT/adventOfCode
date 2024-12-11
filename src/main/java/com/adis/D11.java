package com.adis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class D11 {
    long[] pierres;
    long nbTotal = 0;

    public D11() throws IOException, URISyntaxException {
        pierres = parseInputFile();
    }

    public static void main(String[] args) {
        try {
            var puzzle = new D11();
            puzzle.calculEfficient();
//            puzzle.resolve();
            System.out.println(puzzle.nbTotal);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public long[] parseInputFile() throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(getClass().getResource("/d11.input").toURI()));
        return Arrays.stream(lines.getFirst().split(" "))
                     .mapToLong(Long::parseLong)
                     .toArray();
    }

    private void resolve() {
        for (int i = 0; i < 25; i++) {
            blink();
        }
        System.out.println(pierres.length);
    }

    private void blink() {
        long[] tmp = new long[2 * pierres.length];
        int index = 0;
        for (Long l : pierres) {
            String asStr = l.toString();
            if (l == 0) {
                tmp[index++] = 1;
            } else if (asStr.length() % 2 == 0) {
                String firstHalf = asStr.substring(0, asStr.length() / 2);
                String secondHalf = asStr.substring(asStr.length() / 2);
                tmp[index++] = Long.parseLong(firstHalf);
                tmp[index++] = Long.parseLong(secondHalf);
            } else {
                tmp[index++] = (2024 * l);
            }

        }
        pierres = new long[index];
        System.arraycopy(tmp, 0, pierres, 0, index);
    }

    private void calculEfficient() {
        HashMap<Long, Long> cache = new HashMap<>();
        for (long pierre : pierres) {
            long[] tmp = blink_1(pierre);
            for (long l : tmp) {
                long[] tmp2 = blink_1(l);
                for (long l1 : tmp2) {
                    if(cache.containsKey(l1)){
                        nbTotal += cache.get(l1);
                    }else{
                        long toAdd = blink_1(l1).length;
                        cache.put(l1, toAdd);
                        nbTotal += blink_1(l1).length;
                    }
                }
            }
            System.out.println("on a traité un item de base sur "+pierres.length+" éléments");
        }
    }

    private long[] blink_1(long pierre) {
        long[] tmp = new long[]{pierre};
        for (int i = 0; i < 25; i++) {
            tmp = blink_2(tmp);
        }
        return tmp;
    }

    private long[] blink_2(long[] input) {
        long[] tmp = new long[2 * input.length];
        int index = 0;
        for (Long l : input) {
            String asStr = l.toString();
            if (l == 0) {
                tmp[index++] = 1;
            } else if (asStr.length() % 2 == 0) {
                String firstHalf = asStr.substring(0, asStr.length() / 2);
                String secondHalf = asStr.substring(asStr.length() / 2);
                tmp[index++] = Long.parseLong(firstHalf);
                tmp[index++] = Long.parseLong(secondHalf);
            } else {
                tmp[index++] = (2024 * l);
            }

        }
        input = new long[index];
        System.arraycopy(tmp, 0, input, 0, index);
        return input;
    }
}
