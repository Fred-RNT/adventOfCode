package com.adis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class D1 {
    public static void main(String[] args) {
        try {
            Pair pair = parseInputFile("/d1.input");

            var result = 0l;
            result = similarite(0, pair.gauche(), pair.droite());


            Set<Integer> gauche = new HashSet<>(pair.gauche());
            Set<Integer> droite = new HashSet<>(pair.droite());

            Set<Integer> communs = gauche.stream()
                                         .filter(droite::contains)
                                         .collect(Collectors.toSet());

            System.out.println(communs);
            System.out.println(result);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static long similarite(int debut, List<Integer> gauche, List<Integer> droite) {

        long result = 0;

        for (int i = debut; i < gauche.size(); i++) {
            var nombre = gauche.get(i);
            var facteur = droite.stream()
                                .filter(n -> Objects.equals(n, nombre))
                                .count();
            result += nombre * facteur;
        }
        return result;
    }

    public static Pair parseInputFile(String filePath) throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(D1.class.getResource(filePath)
                                                                  .toURI()));
        List<Integer> firstNumbers = new ArrayList<>();
        List<Integer> secondNumbers = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.trim()
                                 .split("\\s+");
            if (parts.length == 2) {
                firstNumbers.add(Integer.parseInt(parts[0]));
                secondNumbers.add(Integer.parseInt(parts[1]));
            }
        }

        return new Pair(firstNumbers, secondNumbers);
    }

    public static record Pair(List<Integer> gauche, List<Integer> droite) {
    }
}
