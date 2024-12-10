package com.adis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D3 {
    public static List<Operation> parseInputFile() throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(D3.class.getResource("/d3.input")
                                                                  .toURI()));

        List<Operation> operations = new ArrayList<>();
        Pattern pattern = Pattern.compile("do\\(\\)|don't\\(\\)|(mul)\\((\\d+),(\\d+)\\)");
        ;

        boolean process = true;
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                final var group = matcher.group();
                if (group.equals("do()")) {
                    process = true;
                } else if (group.equals("don't()")) {
                    process = false;
                } else if (process) {
                    operations.add(new Operation(matcher.group(1), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))));
                }
            }
        }
        return operations;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println(parseInputFile().stream()
                                           .map(Operation::resultat)
                                           .reduce(0, Integer::sum));
    }

    public static record Operation(String op, int x, int y) {
        int resultat() {
            if (op.equals("mul")) {
                return x * y;
            }
            return 0;
        }
    }
}
