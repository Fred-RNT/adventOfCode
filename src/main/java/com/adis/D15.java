package com.adis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

public class D15 {
    public static Structure parseInputFile(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        List<String> labyrintheLines = new ArrayList<>();
        List<Direction> mouvements = new ArrayList<>();

        boolean isLabyrinthe = true;
        for (String line : lines) {
            if (line.isEmpty()) {
                isLabyrinthe = false;
                continue;
            }
            if (isLabyrinthe) {
                labyrintheLines.add(line);
            } else {
                for (char move : line.toCharArray()) {
                    switch (move) {
                        case '^':
                            mouvements.add(Direction.HAUT);
                            break;
                        case 'v':
                            mouvements.add(Direction.BAS);
                            break;
                        case '>':
                            mouvements.add(Direction.DROITE);
                            break;
                        case '<':
                            mouvements.add(Direction.GAUCHE);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        char[][] grid = new char[labyrintheLines.size()][];
        for (int i = 0; i < labyrintheLines.size(); i++) {
            var chared = labyrintheLines.get(i).toCharArray();
            var toAdd = new char[2 * chared.length];
            int j = 0;
            for (char c : chared) {
                if (c == '#') {
                    toAdd[2 * j] = '#';
                    toAdd[2 * j + 1] = '#';
                } else if (c == '.') {
                    toAdd[2 * j] = '.';
                    toAdd[2 * j + 1] = '.';
                } else if (c == 'O') {
                    toAdd[2 * j] = '[';
                    toAdd[2 * j + 1] = ']';
                } else {
                    toAdd[2 * j] = '@';
                    toAdd[2 * j + 1] = '.';
                }
                j++;
            }
            grid[i] = toAdd;
        }

        Labyrinthe labyrinthe = new Labyrinthe(grid);

        return new Structure(labyrinthe, mouvements);
    }

    public static void main(String[] args) {
        try {
            Structure structure = parseInputFile("src/main/resources/d15.input");
            int compteur = 1;
            for (Direction mouvement : structure.moves) {
                structure.labyrinthe.deplace2(mouvement);
//                System.out.printf("Mouvement #%d: %s%n", compteur++, mouvement);
//                System.out.println(structure.labyrinthe);
            }
            System.out.println(structure.labyrinthe);
            int sumGps = 0;
            for (int i = 0; i < structure.labyrinthe.hauteur; i++) {
                for (int j = 0; j < structure.labyrinthe.largeur; j++) {
                    if (structure.labyrinthe.grid[i][j] == '[') {
//                        var bottomGap = structure.labyrinthe.hauteur - 1 - i;
//                        var closestHeight = Math.min(i, bottomGap);
//                        int rightGap = structure.labyrinthe.largeur - 1 - (j + 1);
//                        var closestWidth = Math.min(j, rightGap);
//                        sumGps += 100 * closestHeight + closestWidth;
                        sumGps += 100 * i + j;
                    }
                }
            }
            System.out.println(sumGps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Direction {
        HAUT(0, -1), BAS(0, 1), GAUCHE(-1, 0), DROITE(1, 0);

        int addLargeur;
        int addHauteur;

        Direction(int addLargeur, int addHauteur) {
            this.addLargeur = addLargeur;
            this.addHauteur = addHauteur;
        }
    }

    public static class Labyrinthe {
        public char[][] grid;
        int hauteur;
        int largeur;

        int hauteurGardien;
        int largeurGardien;
        Comparator<Point> comparatorHaut = (p1, p2) -> Integer.compare(p1.x, p2.x);
        Comparator<Point> comparatorBas = comparatorHaut.reversed();
        Comparator<Point> comparatorGauche = (p1, p2) -> Integer.compare(p1.y, p2.y);
        Comparator<Point> comparatorDroit = comparatorGauche.reversed();

        public Labyrinthe(char[][] grid) {
            this.grid = grid;
            hauteur = grid.length;
            largeur = grid[0].length;
            for (int i = 0; i < hauteur; i++) {
                for (int j = 0; j < largeur; j++) {
                    if (grid[i][j] == '@') {
                        hauteurGardien = i;
                        largeurGardien = j;
                    }
                }
            }
        }

        public void deplace(Direction direction) {
            int newHauteurGardien = hauteurGardien + direction.addHauteur;
            int newLargeurGardien = largeurGardien + direction.addLargeur;
            if (grid[newHauteurGardien][newLargeurGardien] == '#') {
                return;
            }
            if (grid[newHauteurGardien][newLargeurGardien] == '.') {
                grid[newHauteurGardien][newLargeurGardien] = '@';
                grid[hauteurGardien][largeurGardien] = '.';
                hauteurGardien = newHauteurGardien;
                largeurGardien = newLargeurGardien;
            }
            if (grid[newHauteurGardien][newLargeurGardien] == '[' || grid[newHauteurGardien][newLargeurGardien] == ']') {
                int tmpHauteurGardien = newHauteurGardien;
                int tmpLargeurGardien = newLargeurGardien;
                while (true) {
                    tmpLargeurGardien += direction.addLargeur;
                    tmpHauteurGardien += direction.addHauteur;
                    if (grid[tmpHauteurGardien][tmpLargeurGardien] == '#') {
                        //on n'a trouvé qu'un mur
                        break;
                    }
                    if (grid[tmpHauteurGardien][tmpLargeurGardien] == '.') {
                        //on a trouvé une case vide
                        grid[tmpHauteurGardien][tmpLargeurGardien] = 'O';
                        grid[newHauteurGardien][newLargeurGardien] = '@';
                        grid[hauteurGardien][largeurGardien] = '.';
                        hauteurGardien = newHauteurGardien;
                        largeurGardien = newLargeurGardien;
                        break;
                    }
                }
            }
        }

        public void deplace2(Direction direction) {
            int newHauteurGardien = hauteurGardien + direction.addHauteur;
            int newLargeurGardien = largeurGardien + direction.addLargeur;
            final var cible = grid[newHauteurGardien][newLargeurGardien];
            if (cible == '#') {
                return;
            }
            if (cible == '.') {
                grid[newHauteurGardien][newLargeurGardien] = '@';
                grid[hauteurGardien][largeurGardien] = '.';
                hauteurGardien = newHauteurGardien;
                largeurGardien = newLargeurGardien;
            }
            if (cible == '[' || cible == ']') {
                LinkedHashSet<Point> aTester = new LinkedHashSet<>();
                LinkedHashSet<Point> deplacables = new LinkedHashSet<>();
                aTester.add(new Point(newHauteurGardien, newLargeurGardien));
                aTester.add(new Point(newHauteurGardien, newLargeurGardien + ((cible == '[') ? +1 : -1)));
                if (estDeplacable(aTester, deplacables, direction)) {
                    deplacer(deplacables, direction);
                    grid[newHauteurGardien][newLargeurGardien] = '@';
                    grid[hauteurGardien][largeurGardien] = '.';
                    hauteurGardien = newHauteurGardien;
                    largeurGardien = newLargeurGardien;
                }
            }
        }

        private void deplacer(LinkedHashSet<Point> deplacables, Direction direction) {
            Comparator<Point> comparator = switch (direction) {
                case HAUT -> comparatorHaut;
                case BAS -> comparatorBas;
                case GAUCHE -> comparatorGauche;
                case DROITE -> comparatorDroit;
            };
            deplacables.stream().sorted(comparator).forEach(point -> {
                grid[point.x + direction.addHauteur][point.y + direction.addLargeur] = grid[point.x][point.y];
                grid[point.x][point.y] = '.';
            });
        }

        private boolean estDeplacable(LinkedHashSet<Point> aTester, LinkedHashSet<Point> deplacables, Direction direction) {
            if (aTester.isEmpty()) {
                return true;
            }
            var point = aTester.removeFirst();
            var suivant = new Point(point.x + direction.addHauteur, point.y + direction.addLargeur);
            final var cible = grid[suivant.x][suivant.y];
            if (cible == '#') {
                return false;
            } else if (cible == '.') {
                deplacables.add(point);
                return estDeplacable(aTester, deplacables, direction);
            } else if (cible == '[' || cible == ']') {
                deplacables.add(point);
                if (!deplacables.contains(suivant)) {
                    aTester.add(suivant);
                }
                final var c2 = new Point(suivant.x, suivant.y + ((cible == '[') ? +1 : -1));
                if (!deplacables.contains(c2)) {
                    aTester.add(c2);
                }
                return estDeplacable(aTester, deplacables, direction);
            } else {
                throw new IllegalStateException("Cible inconnue");
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (char[] row : grid) {
                sb.append(new String(row)).append("\n");
            }
            return sb.toString();
        }
    }

    public static class Structure {
        public Labyrinthe labyrinthe;
        public List<Direction> moves;

        public Structure(Labyrinthe labyrinthe, List<Direction> moves) {
            this.labyrinthe = labyrinthe;
            this.moves = moves;
        }
    }
}
