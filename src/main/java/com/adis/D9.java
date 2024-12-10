package com.adis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class D9 {
    public static char[] parseInputFile() throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(D9.class.getResource("/d9.input")
                                                                  .toURI()));
        char[] matrix = lines.get(0)
                             .toCharArray();
        return matrix;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        var matrix = parseInputFile();
        var exploded = explode(matrix);
        compact(exploded);
//        System.out.println(Arrays.toString(exploded));
        System.out.println(checksum(exploded));
    }

    private static long checksum(Integer[] exploded) {
        long result = 0;
        for (int i = 0; i < exploded.length; i++) {
            if (Objects.isNull(exploded[i])) {
                break;
            }
            result += i * exploded[i];
        }
        return result;
    }

    private static void compact(Integer[] exploded) {
        int firstPos = getFirstPos(exploded, 0);
        for (int lastPos = exploded.length - 1; lastPos > firstPos; lastPos--) {
            if (exploded[lastPos] == null) {
                continue;
            }
            firstPos = getFirstPos(exploded, firstPos);
            if (firstPos >= lastPos) {
                break;
            }
            exploded[firstPos] = exploded[lastPos];
            exploded[lastPos] = null;
        }
    }

    private static int getFirstPos(Integer[] exploded, int firstPos) {
        while (exploded[firstPos] != null) {
            firstPos++;
        }
        return firstPos;
    }

    private static Integer[] explode(char[] matrix) {
        int newSize = 0;
        for (char c : matrix) {
            final var numericValue = Character.getNumericValue(c);
            newSize += numericValue;
        }
        Integer[] exploded = new Integer[newSize];
        int position = 0;
        int id = 0;
        for (int i = 0; i < matrix.length; i++) {
            char c = matrix[i];
            int count = Character.getNumericValue(c);
            Integer toWrite = i % 2 == 0 ? id++ : null;
            for (int j = 0; j < count; j++) {
                exploded[position++] = toWrite;
            }
        }

        return exploded;
    }
}
