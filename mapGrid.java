import java.util.Random;
import java.util.Scanner;

public class mapGrid {
    public static int maxX = 200;
    public static int maxY = 500;
    public static tileSet[][] map = new tileSet[maxX][maxY];
    public static double[] heightmap = new double[maxX];
    public static Random rand = new Random();

    mapGrid(int seed){
        String tileType;

        //generates heightmap
        for (int x = 0; x < maxX; x++) {

            double slope = 0.25; //slope of the terrain before modifications (slope downwards)
            double sin_weight = 10; //heigher values will amke the terrain look like a sin graph
            double noise_weight = 1; ///heigher values makes the terrain more noisier and random

            //adds random noise
            heightmap[x] =  noise_weight * SimplexNoise.noise(rand.nextInt(10),0);

            //slope trend of terrain
            heightmap[x] = heightmap[x] - 10 + slope * x;

            //adds sinosoidal noise
            heightmap[x] = heightmap[x] + sin_weight * Math.sin(0.25 * x);

        }

        //labels tiles air/water based on integer
        for(int y = 0; y < maxY; y++){
            for(int x = 0; x < maxX; x++){
                if(y<10){
                    tileType = "air";
                }else{
                    tileType = "water";
                }
                tileSet a = new tileSet(tileType);
                map[x][y] = a;
            }
        }

        //labels tiles water/earth based on height map
        for(int y = 0; y < maxY; y++){
            for(int x = 0; x < maxX; x++){
                if(y > 20 + heightmap[x]){
                    map[x][y].tileType = "earth";
                }
            }
        }

        //labels tiles ore
        int OreFrequency = 50;//(heigher = rarer)
        for(int y = 0; y < maxY; y++){
            for(int x = 0; x < maxX; x++){
                if(map[x][y].tileType.equals("earth")) {
                    if (rand.nextInt(OreFrequency) == 0) {
                        map[x][y].tileType = "ore";
                    }
                }
            }
        }

        //lay down kelp
        int PlantFrequency = 5;//(heigher = rarer)
        for(int y = 0; y < maxY; y++){
            for(int x = 0; x < maxX; x++){
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

        int BrainFrequency = 10;//(heigher = rarer)
        for(int y = 0; y < maxY; y++){
            for(int x = 0; x < maxX; x++){
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

    }
    public void mapRefresh(){
        for(int y = 0; y < maxY; y++){
            for(int x = 0; x < maxX; x++){
                int kelpFruitFreq = 100;

                //kelp fruit
                try {
                    if (map[x][y - 1].tileType.equals("water") && map[x][y].tileType.equals("kelp")) {
                        if (rand.nextInt(kelpFruitFreq) == 0) {
                            map[x][y].tileType = "fruit";
                        }
                    }
                } catch (Exception e) {
                    //System.out.println("ERROR");
                }
            }
        }
    }



}
