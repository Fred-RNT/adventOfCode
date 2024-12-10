package com.adis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class D6 {
    public static char[][] parseInputFile() throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(D3.class.getResource("/d6.input")
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

    public static char[][] copyMatrix(char[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;
        char[][] newMatrix = new char[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, numCols);
        }

        return newMatrix;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        int direction = 0; // 0 : top, 1 : right, 2 : bottom, 3 : left
        int x = 0;
        int y = 0;
        char[][] originalMatrix = parseInputFile();

        outer:
        for(int i = 0 ; i < originalMatrix.length; i++){
            for(int j = 0 ; j < originalMatrix[0].length; j++){
            if(originalMatrix[i][j] == '^'){
                x = j;
                y = i;
                break outer;
            }
            }
        }

        int nbLoop = 0;
        for(int i = 0 ; i < originalMatrix.length; i++){
            for(int j = 0 ; j < originalMatrix[0].length; j++){
                if(originalMatrix[i][j] != '.'){
                    continue;
                }
//                System.out.println("check pour i = " + i + " et j = " + j);
                var matrix = copyMatrix(originalMatrix);
                matrix[x][y] = 'X';
                matrix[i][j] = '#';
                var sentinelle = new Sentinelle(x, y, direction);
                while (sentinelle.deplace(matrix, i==8 && j == 3)) {
                }
                if (sentinelle.loop) {
                    nbLoop++;
                    System.out.println("loop en " + i + " " + j);
                }
            }
        }
        System.out.println(nbLoop);
    }

    static record Position(int x, int y, int direction) {
    }
    public static class Sentinelle {
        public int x;
        public int y;
        public int direction;

        Set<Position> virages = new HashSet<>();

        public boolean loop = false;

        public Sentinelle(int x, int y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        boolean deplace(char[][] matrix, boolean debug) {
            int newX;
            int newY;
            int changeDirection = 0;
            do {
                newX = x;
                newY = y;
                if (direction == 0) {
                    newY--;
                } else if (direction == 1) {
                    newX++;
                } else if (direction == 2) {
                    newY++;
                } else {
                    newX--;
                }
                if(newX<0 || newX>=matrix[0].length || newY<0 || newY>=matrix.length){
                    return false;
                }
                if (matrix[newY][newX] == '#') {
                    direction = (direction + 1) % 4;
                    changeDirection++;
                }
                if(changeDirection == 4){
                    loop = true;
                    return false;
                }
            }while (matrix[newY][newX] == '#');
            if(newX<0 || newX>=matrix[0].length || newY<0 || newY>=matrix.length){
                return false;
            }
            if(changeDirection!=0) {
                if (translateAndCheckLoop(newX, newY, debug)) {
                    loop = true;
                    return false;
                }
            }else{
                x=newX;
                y = newY;
            }
            matrix[y][x] = 'X';
            return true;
        }

        private boolean translateAndCheckLoop(int newX, int newY, boolean debug) {
            Position position = new Position(x, y, direction);
            if((!virages.add(position))){
                return true;
            }
            x = newX;
            y = newY;
            return false;
        }
    }
}
