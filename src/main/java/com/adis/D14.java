package com.adis;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D14 {

    public static class Vector {
        public int dx;
        public int dy;

        public Vector(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public String toString() {
            return "Vector{" +
                    "dx=" + dx +
                    ", dy=" + dy +
                    '}';
        }
    }

    public static class Structure {
        public Point origin;
        public Vector displacement;
        public Point destination;

        public Structure(Point origin, Vector displacement, int iteration, int width, int height) {
            this.origin = origin;
            this.displacement = displacement;
            this.destination = new Point(Math.floorMod(origin.x + iteration*displacement.dx, width), Math.floorMod(origin.y + iteration*displacement.dy, height));
        }

        @Override
        public String toString() {
            return "Structure{" +
                    "origin=" + origin +
                    ", displacement=" + displacement +
                    ", destination=" + destination +
                    '}';
        }
    }

    public static List<Structure> parseInputFile(String filePath, int iteration, int width, int height) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        List<Structure> structures = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(" ");
            String[] originParts = parts[0].substring(2).split(",");
            String[] vectorParts = parts[1].substring(2).split(",");

            Point origin = new Point(Integer.parseInt(originParts[0]), Integer.parseInt(originParts[1]));
            Vector displacement = new Vector(Integer.parseInt(vectorParts[0]), Integer.parseInt(vectorParts[1]));

            structures.add(new Structure(origin, displacement, iteration, width, height));
        }

        return structures;
    }

    public static void generateImage(List<Structure> structures, int width, int height, String outputFilePath) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Fond blanc
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Dessiner les points
        g2d.setColor(Color.RED);
        for (Structure structure : structures) {
            g2d.fillRect(structure.destination.x, structure.destination.y, 1, 1);
        }

        g2d.dispose();
        ImageIO.write(image, "png", new File(outputFilePath));
    }

    public static void main(String[] args) {
        try {
//            int width = 11;
//            int height = 7;
            int width = 101;
            int height = 103;

            int middleh = (height+1)/2-1;
            int middlew = (width+1)/2-1;

            long ne=0;
            long nw = 0;
            long se = 0;
            long sw = 0;

            for(int i = 0 ; i < 300 ; i++){
                int index1 = 30+103*i;
                int index2 = 89+101*i;
                List<Structure> structures = parseInputFile("src/main/resources/d14.input", index1, width, height);
                generateImage(structures, width, height, "src/main/resources/d14_" + index1 + ".png");
                structures = parseInputFile("src/main/resources/d14.input", index2, width, height);
                generateImage(structures, width, height, "src/main/resources/d14_" + index2 + ".png");
            }
//            List<Structure> structures = parseInputFile("src/main/resources/d14.input", 100, width, height);
//            for (Structure structure : structures) {
//                System.out.println(structure);
//                if (structure.destination.x < middlew) {
//                    if (structure.destination.y < middleh) {
//                        sw++;
//                    } else if(structure.destination.y >middleh){
//                        nw++;
//                    }
//                } else if (structure.destination.x > middlew){
//                    if (structure.destination.y < middleh) {
//                        se++;
//                    } else if (structure.destination.y > middleh) {
//                        ne++;
//                    }
//                }
//            }
//            System.out.println("ne: " + ne + " nw: " + nw + " se: " + se + " sw: " + sw);
//            System.out.println("result: " + (ne*sw*nw*se));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
