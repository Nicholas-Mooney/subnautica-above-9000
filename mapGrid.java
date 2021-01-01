import java.util.Random;
import java.util.Scanner;

public class mapGrid {
    public static int maxX = 200;
    public static int maxY = 500;
    public static tileSet[][] map = new tileSet[maxX][maxY];
    public static double[] heightmap = new double[maxX];
    public static Random rand = new Random();

    mapGrid(int seed){
        boolean regular = false;
        if (regular) {
            String tileType;

            //generates heightmap
            for (int x = 0; x < maxX; x++) {

                double slope = 0.25; //slope of the terrain before modifications (slope downwards)
                double sin_weight = 10; //heigher values will amke the terrain look like a sin graph
                double noise_weight = 1; ///heigher values makes the terrain more noisier and random

                //adds random noise
                heightmap[x] = noise_weight * SimplexNoise.noise(rand.nextInt(10), 0);

                //slope trend of terrain
                heightmap[x] = heightmap[x] - 10 + slope * x;

                //adds sinosoidal noise
                heightmap[x] = heightmap[x] + sin_weight * Math.sin(0.25 * x);

            }

            //labels tiles air/water based on integer
            for (int y = 0; y < maxY; y++) {
                for (int x = 0; x < maxX; x++) {
                    if (y < 10) {
                        tileType = "air";
                    } else {
                        tileType = "water";
                    }
                    tileSet a = new tileSet(tileType);
                    map[x][y] = a;
                }
            }

            //labels tiles water/earth based on height map
            for (int y = 0; y < maxY; y++) {
                for (int x = 0; x < maxX; x++) {
                    if (y > 20 + heightmap[x]) {
                        map[x][y].tileType = "earth";
                    }
                }
            }

            //labels tiles ore
            int OreFrequency = 50;//(heigher = rarer)
            for (int y = 0; y < maxY; y++) {
                for (int x = 0; x < maxX; x++) {
                    if (map[x][y].tileType.equals("earth")) {
                        if (rand.nextInt(OreFrequency) == 0) {
                            map[x][y].tileType = "ore";
                        }
                    }
                }
            }

            //lay down kelp


            int BrainFrequency = 10;//(heigher = rarer)
            for (int y = 0; y < maxY; y++) {
                for (int x = 0; x < maxX; x++) {
                    try {
                        if (map[x][y + 1].tileType.equals("earth") && map[x][y].tileType.equals("water")) {
                            if (rand.nextInt(BrainFrequency) == 0) {
                                map[x][y].tileType = "brain";

                            }
                        }
                    } catch (Exception e) {
                        //System.out.println("ERROR");
                    }
                }
            }


            boolean[][] cellmap = new boolean[maxX][maxY];
            for (int x = 0; x < maxX; x++) {
                for (int y = 0; y < maxY; y++) {
                    if (y > 40) {
                        cellmap[x][y] = false;
                    } else {
                        cellmap[x][y] = true;
                    }
                }
            }
            double seed_chance = 45;
            for (int x = 0; x < maxX; x++) {
                for (int y = 0; y < maxY; y++) {
                    if (rand.nextInt(100) < seed_chance) {
                        cellmap[x][y] = true; //false means water
                    }
                }
            }
            for (int z = 0; z < 1000; z++) {
                cellmap = doSimulationStep(cellmap);
            }
            for (int x = 0; x < maxX; x++) {
                for (int y = 0; y < maxY; y++) {
                    if (!cellmap[x][y]) {
                        map[x][y].tileType = "water";
                    }
                }
            }
            int PlantFrequency = 5;//(heigher = rarer)
            for (int y = 0; y < maxY; y++) {
                for (int x = 0; x < maxX; x++) {
                    try {
                        if (map[x][y + 1].tileType.equals("earth") && map[x][y].tileType.equals("water")) {
                            if (rand.nextInt(PlantFrequency) == 0) {
                                map[x][y].tileType = "kelp";

                                int kelpType = rand.nextInt(6) + 1;

                                for (int z = 1; z < kelpType; z++) {
                                    if (map[x][y - z].tileType.equals("water")) {
                                        map[x][y - z].tileType = "kelp";
                                    }
                                }
                            }
                        }
                    } catch (Exception ignored) {

                    }
                }
            }
        } else {
            maxX = 100;
            maxY = 100;
            String tileType;
            //labels tiles air/water based on integer
            for (int y = 0; y < maxY; y++) {
                for (int x = 0; x < maxX; x++) {
                    if (y < 10) {
                        tileType = "air";
                    } else {
                        tileType = "water";
                    }
                    tileSet a = new tileSet(tileType);
                    map[x][y] = a;
                }
            }
        }
    }


    public boolean[][] doSimulationStep(boolean[][] oldMap) {
        boolean[][] newMap = new boolean[maxX][maxY];
        int birthLimit = 4;
        int deathLimit = 3;
        //Loop over each row and column of the map
        for (int x = 0; x < oldMap.length; x++) {
            for (int y = 0; y < oldMap[0].length; y++) {
                int nbs = countAliveNeighbours(oldMap, x, y);
                //The new value is based on our simulation rules
                //First, if a cell is alive but has too few neighbours, kill it.
                if (oldMap[x][y]) {
                    if (nbs < deathLimit) {
                        newMap[x][y] = false;
                    } else {
                        newMap[x][y] = true;
                    }
                } //Otherwise, if the cell is dead now, check if it has the right number of neighbours to be 'born'
                else {
                    if (nbs > birthLimit) {
                        newMap[x][y] = true;
                    } else {
                        newMap[x][y] = false;
                    }
                }
            }
        }
        return newMap;
    }
    public int countAliveNeighbours(boolean[][] map, int x, int y){
        int count = 0;
        for(int i=-1; i<2; i++){
            for(int j=-1; j<2; j++){
                int neighbour_x = x+i;
                int neighbour_y = y+j;
                //If we're looking at the middle point
                if(i == 0 && j == 0){
                    //Do nothing, we don't want to add ourselves in!
                }
                //In case the index we're looking at it off the edge of the map
                else if(neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= map.length || neighbour_y >= map[0].length){
                    count = count + 1;
                }
                //Otherwise, a normal check of the neighbour
                else if(map[neighbour_x][neighbour_y]){
                    count = count + 1;
                }
            }
        }
        return count;
    }

    public void mapRefresh(){
        for(int y = 0; y < maxY; y++){
            for(int x = 0; x < maxX; x++){
                int kelpFruitFreq = 2000;
                int kelpGrowFreq = 2000;

                //kelp fruit
                try {
                    if (map[x][y - 1].tileType.equals("water") && map[x][y].tileType.equals("kelp")  && map[x][y + 1].tileType.equals("kelp")) {
                        if (rand.nextInt(kelpFruitFreq) == 0) {
                            map[x][y].tileType = "fruit";
                        }
                    }
                } catch (Exception e) {}
                //kelp grow
                try {
                    if (map[x][y - 1].tileType.equals("water") && map[x][y].tileType.equals("kelp")) {
                        if (rand.nextInt(kelpGrowFreq) == 0) {
                            map[x][y-1].tileType = "kelp";
                        }
                    }
                } catch (Exception e) {}
            }
        }
    }



}
