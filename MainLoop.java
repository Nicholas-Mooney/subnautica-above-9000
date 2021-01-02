import com.sun.tools.javac.Main;

import java.awt.*;
import java.util.*;
import java.lang.Math;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class MainLoop {
    public static Random rand = new Random();
    public static Scanner sc = new Scanner(System.in);
    public static GUI gui = new GUI(); //THIS

    public static int playerX = 20;
    public static int playerY = 5;
    public static int map_sizeX = 1000;
    public static int map_sizeY = 1000;
    public static int viewportX = 17;
    public static int viewportY = 12;

    public static int count = 0; ///THIS
    public static int dayCount = 1200;
    public static boolean displayAll = false;
    public static String input = "starting_input";
    public static playerInv Inv = new playerInv();
    public static mapGrid map = new mapGrid(1000);
    public static String direction = "N";

    public static ArrayList<Entity> entities = new ArrayList<Entity>();

    public static void main(String[] args) {
        //game variables
        boolean running = true;
        Inv.addItem("kyanite");

        //game loop
        while (running) {
            wait(50);
            display(map);
            processInput(map);
            input = "";
            gui.inventoryFieldUpdater("hello vietnam" + Inv.retString());
            map.mapRefresh(); //updates fruit and kelp growth on mapgrid
            entityTicker(); //updates entity tick count which controls entity behaviour
            entityUpdater(); //executes all entity movement
            entitySpawner(); //sapwns bubbless
            //daycount
            dayCount++;
            if (dayCount > 3000) {
                dayCount = dayCount - 3000;
            }
        }
    }

    public static void wait(int x) {
        try {
            Thread.sleep(x);
        } catch (Exception e) {

        }
    }

    public static void display(mapGrid map) {
        if (count == 0) {
            displayRoomX(gui.textPaneHView2, gui.textPaneHView, map);
            count = 1;
        }
        if (count == 1) {
            displayRoomX(gui.textPaneHView, gui.textPaneHView2, map);
            count = 0;
        }
    }

    public static void displayRoomX(JTextAreaA textPane, JTextAreaA textPane2, mapGrid map) {
        textPane.setText("");
        int py = playerY;
        int px = playerX;

        //add light sources location and power
        ArrayList<Integer> xArr = new ArrayList<Integer>();
        ArrayList<Integer> yArr = new ArrayList<Integer>();
        ArrayList<Integer> pArr = new ArrayList<Integer>();

        //add all entities
        //TODO change to entities loaded
        for (int h = 0; h < entities.size(); h++) {
            xArr.add(entities.get(h).x);
            yArr.add(entities.get(h).y);
            pArr.add(entities.get(h).lightPower);
        }
        //TODO add tile checker for light sources

        //all x/y on screen
        for (int y = py - viewportY; y <= py + viewportY; y++) {
            for (int x = px - viewportX; x < px + viewportX; x++) {

                //add player
                if (y == playerY && x == playerX) {
                    textPane.append("P ");

                    //add entities
                } else if (checkEntity(x, y)) {
                    textPane.append(getEntityChar(x, y) + " ", getEntityColor(x, y));
                } else {

                    //add border
                    if (x >= mapGrid.maxX || y >= mapGrid.maxY || y < 0 || x < 0) {
                        textPane.append("| ", Color.white);

                        //add  tiles
                    } else {
                        //new add player to light calc
                        xArr.add(playerX);
                        yArr.add(playerY);
                        pArr.add(8);

                        //add tile
                        textPane.append(charForTile(mapGrid.map[x][y]) + " ", colorForTile(mapGrid.map[x][y], x, y, xArr, yArr, pArr));
                    }
                }
            }
            textPane.append("\n", Color.white);
        }
        textPane.setVisible(true);
        textPane2.setVisible(false);
    }

    public static boolean checkEntity(int x, int y) {
        for (Entity entity : entities) {
            if (entity.x == x && entity.y == y) {
                return true;
            }
        }
        return false;
    }

    public static String getEntityChar(int x, int y) {
        for (Entity entity : entities) {
            if (entity.x == x && entity.y == y) {
                return entity.chara;
            }
        }
        return "!";
    }

    public static Color getEntityColor(int x, int y) {
        for (Entity entity : entities) {
            if (entity.x == x && entity.y == y) {
                return entity.color;
            }
        }
        return Color.BLUE;
    }

    //entity manager
    public static int entityTickCount = 0;

    public static void entityTicker() {
        entityTickCount++;
    }

    public static void entityUpdater() {     //controls entity behaviour
        for (Entity entity : entities) {
            if (entity.type.equals("bubble")) {
                //desynchronizes entity motion by comparing entityTickCount to a value assigned to entity at its birth depending on the tick, instead of updating on the same tick for all entities!
                if (!entity.hasStartingDivisible) {
                    entity.startingDivisible = entityTickCount % 4;
                    entity.hasStartingDivisible = true;
                }
                if (entityTickCount % 4 == entity.startingDivisible) {
                    entity.y--;
                }
                //dont remove entities while list is iterating
                if (mapGrid.map[entity.x][entity.y].tileType.equals("air") || mapGrid.map[entity.x][entity.y].tileType.equals("earth")) {
                    entity.flagForRemoval = true;
                }
                if (playerX == entity.x && playerY == entity.y) {
                    entity.flagForRemoval = true;
                    gui.errorFieldUpdater("BUBBLE COLLECTED", Color.white);
                }
            } else if (entity.type.equals("fish")) {

            } else {
                System.out.println("Untyped entity present");
            }
        }
        //remove flagged entities safely
        for (int x = 0; x < entities.size(); x++) {
            if (entities.get(x).flagForRemoval) {
                entities.remove(x);
                x++;
            }
        }
    }

    public static void entitySpawner() {
        for (int x = 0; x < mapGrid.maxX; x++) {
            for (int y = 0; y < mapGrid.maxY; y++) {
                if (mapGrid.map[x][y].tileType.equals("brain")) {
                    if (rand.nextInt(200) < 1) {
                        entities.add(new Entity("O", "bubble", x, y, Color.white, 5));
                    }
                }
            }
        }
    }

    //input functions
    public static void processInput(mapGrid map) {
        //these functions detect the HELD values from gui, turned off by key release signals
        if (GUI.UP_HELD && !GUI.DOWN_HELD && !GUI.LEFT_HELD && !GUI.RIGHT_HELD) {
            MainLoop.input = "w";
            MainLoop.direction = "N";
        }
        if (!GUI.UP_HELD && GUI.DOWN_HELD && !GUI.LEFT_HELD && !GUI.RIGHT_HELD) {
            MainLoop.input = "s";
            MainLoop.direction = "S";
        }
        if (!GUI.UP_HELD && !GUI.DOWN_HELD && !GUI.LEFT_HELD && GUI.RIGHT_HELD) {
            MainLoop.input = "d";
            MainLoop.direction = "E";
        }
        if (!GUI.UP_HELD && !GUI.DOWN_HELD && GUI.LEFT_HELD && !GUI.RIGHT_HELD) {
            MainLoop.input = "a";
            MainLoop.direction = "W";
        }
        if (GUI.UP_HELD && !GUI.DOWN_HELD && !GUI.LEFT_HELD && GUI.RIGHT_HELD) {
            MainLoop.input = "wd";
            MainLoop.direction = "NE";
        }
        if (GUI.UP_HELD && !GUI.DOWN_HELD && GUI.LEFT_HELD && !GUI.RIGHT_HELD) {
            MainLoop.input = "wa";
            MainLoop.direction = "NW";
        }
        if (!GUI.UP_HELD && GUI.DOWN_HELD && !GUI.LEFT_HELD && GUI.RIGHT_HELD) {
            MainLoop.input = "sd";
            MainLoop.direction = "SE";
        }
        if (!GUI.UP_HELD && GUI.DOWN_HELD && GUI.LEFT_HELD && !GUI.RIGHT_HELD) {
            MainLoop.input = "sa";
            MainLoop.direction = "SW";
        }

        //apply gravity
        while (map.map[playerX][playerY].tileType.equals("air") && (map.map[playerX][playerY + 1].tileType.equals("air") || map.map[playerX][playerY + 1].canMove())) {
            playerY++;
        }
        if (input.equals("a")) {
            playerX--;
        }
        if (input.equals("s")) {
            playerY++;
        }
        if (input.equals("d")) {
            playerX++;
        }
        if (input.equals("w")) {
            playerY--;
        }

        //DIAGONAL MOVERS
        if (input.equals("wd")) {
            playerX++;
            playerY--;
        }
        if (input.equals("wa")) {
            playerY--;
            playerX--;
        }
        if (input.equals("sd")) {
            playerX++;
            playerY++;
        }
        if (input.equals("sa")) {
            playerY++;
            playerX--;
        }
        processAction(map);
        unmover(map);
    }
    public static void unmover(mapGrid map) {
        boolean outOfBoundsFlag = false;

        //Hit border
        if (playerX >= mapGrid.maxX || playerY >= mapGrid.maxY || playerY < 0 || playerX < 0) {
            outOfBoundsFlag = true;
            if (input.equals("a")) {
                playerX++;
            }
            if (input.equals("s")) {
                playerY--;
            }
            if (input.equals("d")) {
                playerX--;
            }
            if (input.equals("w")) {
                playerY++;
            }
        }

        //at border simpler logic no out of bounds
        else if (playerY == 0) {
            outOfBoundsFlag = true;
            if (!map.map[playerX][playerY].canMove()) {
                if (input.equals("a")) {
                    playerX++;
                }
                if (input.equals("s")) {
                    playerY--;
                }
                if (input.equals("d")) {
                    playerX--;
                }
                if (input.equals("w")) {
                    playerY++;
                }
            }
        }

        //normal logic
        if (!outOfBoundsFlag) {


            //going up into air
            if (input.equals("w") && (map.map[playerX][playerY].tileType.equals("air"))) {
                playerY++;
            }
            //going up into solid
            if (input.equals("w") && !(map.map[playerX][playerY].canMove())) {
                playerY++;
            }
            //going up into air
            if (input.equals("wd") && (map.map[playerX][playerY].tileType.equals("air"))) {
                playerY++;
                playerX--;
            }
            //going up into solid
            if (input.equals("wd") && !(map.map[playerX][playerY].canMove())) {
                playerY++;
                playerX--;
            }
            //going up into air
            if (input.equals("wa") && (map.map[playerX][playerY].tileType.equals("air"))) {
                playerY++;
                playerX++;
            }
            //going up into solid
            if (input.equals("wa") && !(map.map[playerX][playerY].canMove())) {
                playerY++;
                playerX++;
            }
            //going down into solid
            if (input.equals("s") && !(map.map[playerX][playerY].canMove())) {
                playerY--;
            }
            //going right into solid w/o air
            if ((input.equals("d") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX--;
            } else if ((input.equals("wd") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX--;
                playerY++;
            } else if ((input.equals("sd") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX--;
                playerY--;
            }

            //going left into solid w/o air
            else if ((input.equals("a") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX++;
            } else if ((input.equals("sa") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX++;
                playerY--;
            } else if ((input.equals("wa") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX++;
                playerY++;
            }

            //going left or right w/ air above
            else if (((input.equals("a") || input.equals("d")) && (map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerY--;
            }
        }
    }
    public static void processAction(mapGrid map) {
        if (MainLoop.input.equals("g")) {
            System.out.print("g)");
            //check for something to grab
            //then change map or entity list / amend inv
            if (map.map[playerX][playerY].tileType.equals("fruit")) {
                MainLoop.map.map[playerX][playerY].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            } else if (map.map[playerX + 1][playerY].tileType.equals("fruit")) {
                MainLoop.map.map[playerX + 1][playerY].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            } else if (map.map[playerX + 1][playerY + 1].tileType.equals("fruit")) {
                MainLoop.map.map[playerX + 1][playerY + 1].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            } else if (map.map[playerX + 1][playerY - 1].tileType.equals("fruit")) {
                MainLoop.map.map[playerX + 1][playerY - 1].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            } else if (map.map[playerX][playerY - 1].tileType.equals("fruit")) {
                MainLoop.map.map[playerX][playerY - 1].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            } else if (map.map[playerX][playerY + 1].tileType.equals("fruit")) {
                MainLoop.map.map[playerX][playerY + 1].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            } else if (map.map[playerX - 1][playerY].tileType.equals("fruit")) {
                MainLoop.map.map[playerX - 1][playerY].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            } else if (map.map[playerX - 1][playerY + 1].tileType.equals("fruit")) {
                MainLoop.map.map[playerX - 1][playerY + 1].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            } else if (map.map[playerX - 1][playerY - 1].tileType.equals("fruit")) {
                MainLoop.map.map[playerX - 1][playerY - 1].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            }
        }
    }

    //display functions
    public static void displayOld() {
        for (int y = playerY - viewportY; y < playerY + viewportY; y++) {
            for (int x = playerX - viewportX; x < playerX + viewportX; x++) {
                if (y == playerY && x == playerX) {
                    //System.out.print("P ");
                } else {
                    if (x >= mapGrid.maxX || y >= mapGrid.maxY || y < 0 || x < 0) {
                        //System.out.print("| ");
                    } else {
                        //System.out.print(displayForTile(mapGrid.map[x][y]));
                        //System.out.print(" ");
                    }
                }
            }
            //System.out.println(" ");
        }
    }
    public static char charForTile(tileSet tile) {
        switch (tile.tileType) {
            case "air":
                return '\'';
            case "water":
                return '~';
            case "earth":
                return '=';
            case "ore":
                return 'o';
            case "kelp":
                return '$';
            case "brain":
                return '@';
            case "fruit":
                return '*';
            case "mushroom":
                return '%';
            case "mushroom2":
                return '%';
            case "mushroom3":
                return '%';
            default:
                return '!';
        }
    }
    public static Color colorForTile(tileSet tile, int x, int y, ArrayList<Integer> xArr, ArrayList<Integer> yArr, ArrayList<Integer> pArr) {
        //depth lighting
        double daylight = MainLoop.dayCount / 3000.0;
        daylight = (Math.sin(daylight * 2 * Math.PI) + 1) / 2.0;
        double elevation = y;
        if (elevation >= 100) {
            elevation = 100;
        }
        double m1 = daylight * (100 - elevation) / 100.0;



        //entity lighting
        double[] pr = new double[xArr.size()];
        for (int i = 0; i < pr.length; i++) {
            int dx = Math.abs(x - xArr.get(i));
            int dy = Math.abs(y - yArr.get(i));
            double radius = pow(((pow(dx, 2)) + (pow(dy, 2))), 0.5);
            pr[i] = pow(pArr.get(i) / radius, (2))/(radius*radius);
        }

        //take highest
        double xmax = 0;
        if (pr.length != 0) {
            //System.out.print("pr" + pr.length);
            xmax = pr[0];
            for (int h = 0; h < pr.length - 1; h++) {
                xmax = Math.max(xmax, pr[h + 1]);
            }
        }

        double  m = Math.max(m1, xmax);
        if (m > 1) {
            m = 1;
        }
        if (displayAll) {
            m = 1;
        }

        switch (tile.tileType) {
            case "air":
                return new Color((int) (m * 220), (int) (m * 200), (int) (m * 160));
            case "water":
                return new Color((int) (m * 0), (int) (m * 60), (int) (m * 135));
            case "earth":
                return new Color((int) (m * 160), (int) (m * 131), (int) (m * 46));
            case "ore":
                return new Color((int) (m * 142), (int) (m * 122), (int) (m * 102));
            case "kelp":
                return new Color((int) (m * 50), (int) (m * 220), (int) (m * 100));
            case "brain":
                return new Color((int) (m * 250), (int) (m * 100), (int) (m * 120));
            case "fruit":
                return new Color((int) (m * 250), (int) (m * 250), (int) (m * 0));
            case "mushroom":
                return new Color((int) (m * 204), (int) (m * 0), (int) (m * 0));
            case "mushroom2":
                return new Color((int) (m * 254), (int) (m * 250), (int) (m * 0));
            case "mushroom3":
                return new Color((int) (m * 250), (int) (m * 51), (int) (m * 152));
            default:
                return new Color((int) (m * 255), (int) (m * 0), (int) (m * 0));
        }
    }
}
