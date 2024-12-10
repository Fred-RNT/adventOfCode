package com.adis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class D4 {

    public static char[][] parseInputFile() throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(D3.class.getResource("/test.input").toURI()));
        int numRows = lines.size();
        int numCols = lines.get(0).length();
        char[][] matrix = new char[numRows][numCols];

        for (int row = 0; row < numRows; row++) {
            matrix[row] = lines.get(row).toCharArray();
        }

        return matrix;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        var matrice = parseInputFile();
        int result = 0;
        final var nbCol = matrice[0].length;
        final var nbRow = matrice.length;
        for (int row = 0; row < nbRow; row++) {
            for (int col = 0; col < nbCol; col++) {
                result+=findMot(matrice, nbRow, nbCol, row, col);
            }
        }
        System.out.println(result);
    }

    private static int findMot(char[][] matrice, int nbCol, int nbLig, int i, int j) {
        if(matrice[i][j] != 'A') {
            return 0;
        } else {
            try{
                char c1 = matrice[i-1][j-1];
                char c2 = matrice[i-1][j+1];
                char c3 = matrice[i+1][j+1];
                char c4 = matrice[i+1][j-1];
                boolean d1 = (c1 == 'M' && c3 == 'S') || (c3 == 'M' && c1 == 'S');
                boolean d2 = (c2 == 'M' && c4 == 'S') || (c4 == 'M' && c2 == 'S');
                return (d1 && d2)?1:0;
            }catch(Exception ignore){}
        }
        return 0;
    }

}
