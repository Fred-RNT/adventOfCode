package com.adis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class D12 {

    char[][] matrix;
    List<Aire> aires;

    public D12() throws IOException, URISyntaxException {
        matrix = parseInputFile();
        aires = rempliAire();
    }

    public static void main(String[] args) {
        try {
            var puzzle = new D12();
            puzzle.resolve();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void resolve() {
        int result = 0;
        for (Aire aire : aires) {
            System.out.println("Aire : " + aire.valeur + " aire : " + aire.aire() + " nombre de cote : " + aire.nombreCote());
            result += aire.aire() * aire.nombreCote();
        }
        System.out.println("resultat : " +result);
    }

    char[][] parseInputFile() throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(getClass().getResource("/d12.input").toURI()));
        int numRows = lines.size();
        int numCols = lines.getFirst().length();
        char[][] matrix = new char[numRows][numCols];

        for (int row = 0; row < numRows; row++) {
            matrix[row] = lines.get(row).toCharArray();
        }
        return matrix;
    }

    private List<Aire> rempliAire() {
        List<Aire> result = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            var ligne = matrix[i];
            for (int j = 0; j < ligne.length; j++) {
                final var curChar = matrix[i][j];
                boolean added = false;
                Element toAdd = new Element(i, j, curChar);
                for (Aire aire : result) {
                    if (aire.add(toAdd)) {
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    var nouvelleAire = new Aire(curChar);
                    result.add(nouvelleAire);
                    nouvelleAire.add(toAdd);
                }
            }
        }
        return consolideGlobal(result);
    }

    private List<Aire> consolideGlobal(List<Aire> result) {
        Map<Character, LinkedList<Aire>> tmp = new java.util.HashMap<>();
        for (Aire aire : result) {
            if (tmp.containsKey(aire.valeur)) {
                tmp.get(aire.valeur).add(aire);
            } else {
                LinkedList<Aire> list = new LinkedList<>();
                list.add(aire);
                tmp.put(aire.valeur, list);
            }
        }

        List<Aire> res = new ArrayList<>();
        for (LinkedList<Aire> value : tmp.values()) {
            res.addAll(consolide(value));
        }
        return res;
    }

    private List<Aire> consolide(LinkedList<Aire> value) {
        for (int i = 0; i < value.size(); i++) {
            Aire aire = value.get(i);
            if (aire == null) {
                continue;
            }
            boolean merged = false;
            for (int j = 0; j < value.size(); j++) {
                Aire aire2 = value.get(j);
                if (aire2 == null || aire2 == aire) {
                    continue;
                }
                if (aire.elements.stream().anyMatch(e -> aire2.canAdd(e))) {
                    aire.elements.addAll(aire2.elements);
                    value.set(j, null);
                    merged = true;
                }
            }
            if (merged) {
                i--;
            }
        }
        return value.stream().filter(aire -> aire != null).toList();
    }

    static record Element(int x, int y, char value) {
        public Element gauche() {
            return new Element(x, y-1, value);
        }

        public Element droite() {
            return new Element(x, y+1, value);
        }

        public Element haut() {
            return new Element(x-1, y, value);
        }

        public Element bas() {
            return new Element(x+1, y, value);
        }
    }

    class Aire {
        final char valeur;
        Set<Element> elements = new HashSet<>();

        Aire(char valeur) {
            this.valeur = valeur;
        }

        boolean add(Element e) {
            if (e.value != valeur) {
                return false;
            }
            if (canAdd(e)) {
                elements.add(e);
                return true;
            }
            return false;
        }

        boolean canAdd(Element e) {
            return elements.isEmpty() || elements.contains(e.gauche()) || elements.contains(e.droite()) || elements.contains(e.haut()) || elements.contains(e.bas());
        }

        int aire() {
            return elements.size();
        }

        int nombreCote() {
            if (elements.size() < 3) {
                return 4;
            }

            int direction = 0; //0 = haut, 1 = droite, 2 = bas, 3 = gauche

            Function<Element, Element>[] mouvements = new Function[4];
            mouvements[0] = Element::haut;
            mouvements[1] = Element::droite;
            mouvements[2] = Element::bas;
            mouvements[3] = Element::gauche;

            int nbRotation = 0;
            final var bordsDroits = elements.stream()
                                            .filter(e -> !elements.contains(e.droite()))
                                            .collect(Collectors.toCollection(ArrayDeque::new));

            while (!bordsDroits.isEmpty()) {
                //on prend le 1er bord droit
                Element debut = bordsDroits.removeFirst();
                //on va parcourir notre surface avec l'algo de la main droite

                Element current = debut;
                do {
                    int maDroite = Math.floorMod(direction + 1, 4);
                    //si je n'ai pas de mur à ma droite, je tourne à droite et j'avance
                    if (elements.contains(mouvements[maDroite].apply(current))) {
                        nbRotation++;
                        direction = maDroite;
                        current = mouvements[direction].apply(current);
                    } else {
                        // j'ai un mur à ma droite je cherche à avancer
                        //je ne peux pas avancer je tourne à gauche
                        if (!elements.contains(mouvements[direction].apply(current))) {
                            nbRotation++;
                            direction = Math.floorMod(direction -1 , 4);
                        } else {
                            //j'avance
                            current = mouvements[direction].apply(current);
                        }
                    }
                    //si j'ai un bord droit je le retire de la liste
                    if(bordsDroits.contains(current) && direction == 0){
                        bordsDroits.remove(current);
                    }
                } while (!current.equals(debut) || direction != 0);
            }
            return nbRotation;
        }

        int perimetre() {
            int result = 0;
            for (Element element : elements) {
                if (!elements.contains(element.gauche())) {
                    result++;
                }
                if (!elements.contains(element.droite())) {
                    result++;
                }
                if (!elements.contains(element.haut())) {
                    result++;
                }
                if (!elements.contains(element.bas())) {
                    result++;
                }
            }
            return result;
        }
    }

}
