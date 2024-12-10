package com.adis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class D8 {
    public static char[][] parseInputFile() throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(D3.class.getResource("/d8.input")
                                                                  .toURI()));
        int numRows = lines.size();
        int numCols = lines.get(0)
                           .length();
        char[][] matrix = new char[numRows][numCols];

        for (int row = 0; row < numRows; row++) {
            matrix[row] = lines.get(row)
                               .toCharArray();
        }
        return matrix;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        var matrix = parseInputFile();
        char[][] antinodes = new char[matrix.length][matrix[0].length];
        int result = parseAntinode(matrix, antinodes);
        System.out.println(result);
    }

    private static int parseAntinode(char[][] matrix, char[][] antinodes) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (Character.isLetterOrDigit(matrix[i][j])) {
                    parseForAntenna(matrix, i, j, antinodes);
                }
            }
        }
        int result = 0;
        for (char[] antinode : antinodes) {
            for (char c : antinode) {
                if (c == 'A') {
                    result++;
                }
            }
        }
        return result;
    }

    private static void parseForAntenna(char[][] matrix, int originI, int originJ, char[][] antinodes) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (i == originI && j == originJ) {
                    continue;
                }
                if (matrix[i][j] == matrix[originI][originJ]) {
                    int deltaI = originI - i;
                    int deltaJ = originJ - j;
                    int factor = 0;
                    while ((originI + factor * deltaI >= 0 && originJ + factor * deltaJ >= 0 && originI + factor * deltaI < matrix.length && originJ + factor * deltaJ < matrix[0].length)) {
                        antinodes[originI + factor * deltaI][originJ + factor * deltaJ] = 'A';
                        factor++;
                    }
                    factor = 0;
                    while (i - factor *deltaI >= 0 && j - factor *deltaJ >= 0 && i - factor *deltaI < matrix.length && j - factor *deltaJ < matrix[0].length) {
                        antinodes[i - factor *deltaI][j - factor *deltaJ] = 'A';
                        factor++;
                    }
                }
            }
        }
    }

}
