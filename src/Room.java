import java.awt.*;
import java.util.Random;

public class Room {
    protected int xmax = 50;
    protected int ymax = 50;
    protected int zmax = 70;
    protected int waterline = 60;
    public static Random rand = new Random();
    Object[][][] objects = new Object[xmax][ymax][zmax];
    String alphabet = "abcdefghijklmnopqrstuvwxyz";

    public Room() {
        createWater();
        createGround();
        //layFloor();
       // makeMountain();

    }
    public void createWater() {
        for (int y = 0; y < ymax; y++) {
            for (int x = 0; x < xmax; x++) {
                for (int z = 0; z < zmax; z++) {
                    objects[x][y][z] = new Object(x, y, z, new Color(5, 5, rand.nextInt(40) + 20), false, "x", false, false, "Water");
                }
            }
        }
    }

    public void createGround() {
        int[][] depths = new int[xmax][ymax];
        for (int y = 0; y < ymax; y++) {
            for (int x = 0; x < xmax; x++) {
                depths[x][y] = rand.nextInt(6) + 9;
            }
        }

        for (int y = 0; y < ymax; y++) {
            for (int x = 0; x < xmax; x++) {
                for (int z = 0; z < zmax; z++) {
                    if (z == depths[x][y]) {
                        int p = rand.nextInt(alphabet.length());
                        String symbol2 = alphabet.substring(p, p + 1);
                        //
                        //100, 50, 0
                        objects[x][y][z] = new Object(x, y, z, new Color(rand.nextInt(10) + 50, 150, 50), false, "x", true, false, "Kelp",new Item.Kelp() );
                    }
                    if (z < depths[x][y]) {
                        objects[x][y][z] = new Object(x, y, z, new Color(150, 150, 0), true, "w", true, false, "Sandstone");
                    }
                    if (z == waterline) {
                        objects[x][y][z] = new Object(x, y, z, new Color(5, 5, rand.nextInt(40) + 60), false, "~", true, true, "Waves");
                    }
                    if (z > waterline) {
                        objects[x][y][z] = new Object(x, y, z, new Color(5, 5, 5), true, "x", false, true, "Air");
                    }
                }
            }
        }
    }

    public void makeMountain() {
        double a = 50;
        double bx = 14;
        double by = 14;
        double c = 10;
        int minx = 0;
        int miny = 0;
        int xsize = 40;
        int ysize = 40;
        int minz = 10;
        int[][] heights = new int[xsize][ysize];
        for (int x = 0; x < heights.length; x++) {
            for (int y = 0; y < heights[0].length; y++) {
                double numerator = -(x - bx) * (x - bx);
                double numerator2 = -(y - by) * (y - by);
                double denom = 2 * c * c;

                double exponent = numerator / denom;
                double exponent2 = numerator2 / denom;
                double exptotal = exponent + exponent2;
                double base = Math.pow(2.72, exptotal);
                double value = base * a;

                heights[x][y] = (int) value + minz;
            }
        }
        for (int x = minx; x < minx + heights.length; x++) {
            for (int y = miny; y < miny + heights[0].length; y++) {
                for (int z = 0; z < zmax; z++) {
                    int random = rand.nextInt(3);
                    if (z == heights[x - minx][y - miny] + random) {
                        objects[x][y][z] = new Object(x, y, z, new Color(rand.nextInt(10) + 50, 150, 50), false, "x", true, false, "Seagrass");
                    }
                    if (z < heights[x - minx][y - miny]+ random) {
                        objects[x][y][z] = new Object(x, y, z, new Color(150, 150, 0), true, "w", true, false, "Sandstone");
                    }

                }
            }
        }

        }

    }

