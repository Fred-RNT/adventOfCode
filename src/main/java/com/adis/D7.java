package com.adis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class D7 {
    public static void main(String[] args) {
        try {
            List<Ligne> lignes = parseInputFile("src/main/resources/d7.input");
            var reponse = lignes.stream()
                                .filter(Ligne::caMatch)
                                .mapToLong(l -> l.aAtteindre)
                                .sum();
            System.out.printf("Reponse: %d%n", reponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Ligne> parseInputFile(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        List<Ligne> lignes = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(":");
            long aAtteindre = Long.parseLong(parts[0].trim());
            String[] numbers = parts[1].trim()
                                       .split("\\s+");
            List<Long> valeurs = new ArrayList<>();
            for (String number : numbers) {
                valeurs.add(Long.parseLong(number));
            }
            lignes.add(new Ligne(aAtteindre, valeurs));
        }

        return lignes;
    }

    private static void genererCombinaisonsRec(BiFunction<Long, Long, Long>[] ops, List<BiFunction<Long, Long, Long>> current, int size, List<List<BiFunction<Long, Long, Long>>> result) {
        if (current.size() == size) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (BiFunction<Long, Long, Long> op : ops) {
            current.add(op);
            genererCombinaisonsRec(ops, current, size, result);
            current.remove(current.size() - 1);
        }
    }

    public static record Ligne(long aAtteindre, List<Long> valeurs) {
        boolean caMatch() {
            BiFunction<Long, Long, Long> add = new AddOperation();
            BiFunction<Long, Long, Long> mult = new MultOperation();
            BiFunction<Long, Long, Long> concat = new ConcatOperation();
            BiFunction<Long,Long,Long>[] ops = new BiFunction[]{add, mult, concat};
            ArrayList<List<BiFunction<Long, Long, Long>>> combinaisons = new ArrayList<>();
            genererCombinaisonsRec(ops, new ArrayList<>(), valeurs.size() - 1, combinaisons);

            for (var combinaison : combinaisons) {
                long result = valeurs.get(0);
                int position = 0;
                for (var current : combinaison) {

                    result = current.apply(result, valeurs.get(++position));
                }
                if (result == aAtteindre) {
                    System.out.println("on atteind " + aAtteindre + " avec " + combinaison);
                    return true;
                }
            }
            return false;
        }

        private static class AddOperation implements BiFunction<Long, Long, Long> {
            @Override
            public Long apply(Long x, Long y) {
                return Math.addExact(x, y);
            }
        }

        private static class MultOperation implements BiFunction<Long, Long, Long> {
            @Override
            public Long apply(Long x, Long y) {
                return Math.multiplyExact(x, y);
            }
        }

        private class ConcatOperation implements BiFunction<Long, Long, Long> {
            @Override
            public Long apply(Long aLong, Long aLong2) {
                return Long.parseLong(aLong.toString() + aLong2.toString());
            }
        }
    }
}
