package com.adis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class D5 {

    public static Content parseInputFile() throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(D5.class.getResource("/d5.input").toURI()));
        boolean firstSection = true;
        Set<Rule> rules = new HashSet<>();
        List<MAJ> majs = new ArrayList<>();
        for (String line : lines) {
            if(firstSection){
                if(line.isBlank()){
                    firstSection = false;
                    continue;
                }
                String[] split = line.split("\\|");
                rules.add(new Rule(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
            }else{
                final var array = Arrays.stream(line.split(","))
                                        .map(Integer::parseInt)
                                        .toArray(Integer[]::new);
                majs.add(new MAJ(array));
            }
        }
        return new Content(rules, majs);
    }

    public record Content(Set<Rule> rules, List<MAJ> maj){}

    public record Rule(int prev, int next){}

    public record MAJ(Integer[] pages){
        int middle(){
            return pages[pages.length/2];
        }

        MAJ repair(Set<Rule> againsRule){
            if(this.isValid(againsRule)){
                return new MAJ(new Integer[]{});
            }

            var compare = new Comparator<Integer>(){

                @Override
                public int compare(Integer o1, Integer o2) {
                    return againsRule.contains(new Rule(o1, o2)) ? -1 : 1;
                }
            };
            Arrays.sort(pages, compare);
            return new MAJ(pages);
        }

        boolean isValid(Set<Rule> againsRule){
            if(pages.length == 0){
                return false;
            }
            for (int i = 0 ; i < pages.length-2 ; i++) {
                for (int j = i+1 ; j < pages().length-1 ; j++) {
                    Rule rule = new Rule(pages[i], pages[j]);
                    if(!againsRule.contains(rule)){
                        return false;
                    }
                }
            }
            for (int i = 0 ; i < pages.length-1 ; i++) {
                Rule rule = new Rule(pages[pages().length-1], pages[i]);
                if(againsRule.contains(rule)){
                    return false;
                }
            }
            return true;
        }
    }

    public static void main(String[] args) {
        try {
            var content = parseInputFile();
            int result = 0;
            for (MAJ maj : content.maj) {
                var toCheck = maj.repair(content.rules);
                if(toCheck.isValid(content.rules)){
                    result+=toCheck.middle();
                }
            }
            System.out.println(result);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
