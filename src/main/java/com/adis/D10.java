package com.adis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class D10 {
    int[][] matrix;

    public D10() throws IOException, URISyntaxException {
        this.matrix = parseInputFile();
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        var puzzle = new D10();
        puzzle.resolve();
    }

    public int[][] parseInputFile() throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(getClass().getResource("/d10.input").toURI()));
        int numRows = lines.size();
        int numCols = lines.getFirst().length();
        int[][] matrix = new int[numRows][numCols];

        for (int row = 0; row < numRows; row++) {
            final var charArray = lines.get(row).toCharArray();
            matrix[row] = new int[charArray.length];
            for (int col = 0; col < charArray.length; col++) {
                matrix[row][col] = Character.getNumericValue(charArray[col]);
            }
        }
        return matrix;
    }

    private void resolve() {
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                chercheChemin(0, i, j, counter, new HashSet<>());
            }
        }
        System.out.println("on a trouvé " + counter.get() + " chemins");
    }

    private boolean chercheChemin(int valeurAChercher, int i, int j, AtomicInteger counter, Set<IntPair> path) {
        try {
            if (matrix[i][j] != valeurAChercher) {
                return false;
            }
            if (matrix[i][j] == 9) {
                path.add(new IntPair(i, j));
                counter.incrementAndGet();
//                    System.out.println("nouveau sommet atteind : " + path);
                return true;
            }
            var result = chercheChemin(valeurAChercher + 1, i + 1, j, counter, path);
            result |= chercheChemin(valeurAChercher + 1, i - 1, j, counter, path);
            result |= chercheChemin(valeurAChercher + 1, i, j + 1, counter, path);
            result |= chercheChemin(valeurAChercher + 1, i, j - 1, counter, path);
            return result;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
