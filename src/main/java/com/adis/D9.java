package com.adis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class D9 {
    private static int getFirstPos(Integer[] exploded, int firstPos) {
        while (exploded[firstPos] != null) {
            firstPos++;
        }
        return firstPos;
    }

    private static List<Content> explode(char[] matrix) {
        List<Content> exploded = new ArrayList<>();
        int position = 0;
        int id = 0;
        for (int i = 0; i < matrix.length; i++) {
            char c = matrix[i];
            int count = Character.getNumericValue(c);
            Integer toWrite = i % 2 == 0 ? id++ : null;
            exploded.add(new Content(toWrite, count));
        }

        return exploded;
    }

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
        System.out.println(exploded);
        System.out.println(checksum(exploded));
    }

    private static long checksum(List<Content> exploded) {
        long result = 0;
        int counter = 0;
        for (Content content : exploded) {
            if(content.value == null) {
                counter += content.nb;
            }else{
                for(int i = 0 ; i < content.nb; i++){
                    result += (counter++)*content.value;
                }
            }

        }
        return result;
    }

    private static void compact(List<Content> exploded) {
        for (int lastPos = exploded.size() - 1; lastPos > 0; lastPos--) {
            final var content = exploded.get(lastPos);
            if (content.value == null) {
                continue;
            }
            for (int i = 0; i < lastPos; i++) {
                final var fillPossible = exploded.get(i);
                if (fillPossible.value == null) {
                    if (fillPossible.nb >= content.nb) {
                        exploded.set(i, content);
                        exploded.set(lastPos, new Content(null, content.nb));
                        if (fillPossible.nb > content.nb) {
                            exploded.add(i + 1, new Content(null, fillPossible.nb - content.nb));
                            lastPos++;
                        }
                        break;
                    }
                }
            }
        }
    }

    record Content(Integer value, int nb) {
    }
}
