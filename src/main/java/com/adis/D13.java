package com.adis;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class D13 {

    public static List<Systeme> parseInputFile(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        List<Systeme> systemes = new ArrayList<>();

        BigInteger[][] matrix = new BigInteger[2][2];
        BigInteger[] objectif = new BigInteger[2];

        for (String line : lines) {
            if (line.startsWith("Button A")) {
                matrix = new BigInteger[2][2];
                objectif = new BigInteger[2];
                String[] parts = line.split(": ")[1].split(", ");
                matrix[0][0] = BigInteger.valueOf(Integer.parseInt(parts[0].substring(2)));
                matrix[1][0] = BigInteger.valueOf(Integer.parseInt(parts[1].substring(2)));
            } else if (line.startsWith("Button B")) {
                String[] parts = line.split(": ")[1].split(", ");
                matrix[0][1] = BigInteger.valueOf(Integer.parseInt(parts[0].substring(2)));
                matrix[1][1] = BigInteger.valueOf(Integer.parseInt(parts[1].substring(2)));
            } else if (line.startsWith("Prize")) {
                String[] parts = line.split(": ")[1].split(", ");
                objectif[0] = BigInteger.valueOf(10000000000000L+ Integer.parseInt(parts[0].substring(2)));
                objectif[1] = BigInteger.valueOf(10000000000000L+ Integer.parseInt(parts[1].substring(2)));
                systemes.add(new Systeme(matrix.clone(), objectif.clone()));
            }
        }

        return systemes;
    }

    public static void main(String[] args) {
        try {
            List<Systeme> systemes = parseInputFile("src/main/resources/d13.input");
            BigInteger result = BigInteger.ZERO;
            for (Systeme systeme : systemes) {
                BigInteger[] res = systeme.solve();
                if (res == null) {
                    System.out.println("No solution found");
                    continue;
                }
                System.out.println("x = " + res[0] + " y = " + res[1]);
                result = result.add(res[0].multiply(BigInteger.valueOf(3)).add(res[1]));
            }
            System.out.println("Result: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static final class Systeme {
        private final BigInteger[][] matrix;
        private final BigInteger[] objectif;
        private BigInteger[] res;

        Systeme(BigInteger[][] matrix, BigInteger[] objectif) {
            this.matrix = matrix;
            this.objectif = objectif;
            res = solve();
        }

        BigInteger[] solve() {
            BigInteger m1 = matrix[0][1];
            BigInteger m2 = matrix[1][1];
            BigInteger[] result = new BigInteger[2];
            var num1 = objectif[0] .multiply(m2).subtract(objectif[1].multiply(m1));
            var deno1 = matrix[0][0].multiply(m2).subtract(matrix[1][0].multiply(m1));
            final var test1 = num1.divideAndRemainder(deno1);
            if(!Objects.equals(test1[1], BigInteger.ZERO)) {
                return null;
            }
            result[0] = test1[0];
            var num2 =objectif[1].subtract(matrix[1][0].multiply(result[0]));
            var deno2 = matrix[1][1];
            var test2 = num2.divideAndRemainder(deno2);
            if(!Objects.equals(test2[1], BigInteger.ZERO)) {
                return null;
            }
            result[1] = test2[0];
            if(result[0].signum() == -1 || result[1].signum() == -1) {
                return null;
            }
            return result;
        }
    }

}
