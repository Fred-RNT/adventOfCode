package com.adis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D2 {

    public static void main(String[] args) {
        try {
            List<Data> data = parseInputFile();
            var result = 0;
            for (Data d : data) {
                if (d.isSafe()) {
                    result++;
                }
            }
            System.out.println(result);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static List<Data> parseInputFile() throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(D2.class.getResource("/d2.input")
                                                                  .toURI()));

        List<Data> result = new ArrayList<>();

        for (String line : lines) {
            var data = new Data(Arrays.stream(line.trim()
                                                  .split(" "))
                                      .map(Integer::parseInt)
                                      .toList());
            result.add(data);
        }

        return result;
    }

    public static class Data {
        private final List<Integer> content;

        public Data(List<Integer> content) {
            this.content = content;
        }

        private static boolean invalidDelta(int delta) {
            if (delta > 3 || delta == 0 || delta < -3) {
                return true;
            }
            return false;
        }

        public boolean isSafe() {
            if(isSafe(content)) {
                return true;
            }
            for (int i = 0; i < content.size(); i++) {
                var copy = new ArrayList<>(content);
                copy.remove(i);
                if (isSafe(copy)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isSafe(List<Integer> input) {
            var prev = input.get(1);
            var delta = input.get(1) - input.get(0);
            if (invalidDelta(delta)) return false;
            for (int i = 2; i < input.size(); i++) {
                var current = input.get(i);
                var newDelta = current - prev;
                if (invalidDelta(newDelta)) return false;
                if (delta * newDelta < 0) return false;
                prev = current;
            }
            return true;
        }
    }
}
