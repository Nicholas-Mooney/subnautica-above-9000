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

    public static boolean grabbing = false; //NEW

    public static int count = 0; ///THIS
    public static int dayCount = 2000;
    public static int mineCounter = 0;
    public static int mineCounterMax = 5;

    public static boolean displayAll = false;
    public static String input = "starting_input";

    public static playerInv Inv = new playerInv();
    public static mapGrid map = new mapGrid(1000);
    public static String direction = "N";
    public static int playerLightVal = 128;

    public static boolean playerInteract = true;

    public static int health = 10;
    public static int health_max = 10;

    public static int oxygen = 100;
    public static int oxygen_max = 109;
    public static int oxygenCount = 0;
    public static int oxygenCountMax = 10;
    public static int oxygenRefillRate = 3;
    public static int bubbleOxygenRate = 30;

    public static int food = 10;
    public static int food_max = 10;
    public static int foodCount = 0;
    public static int foodCountMax = 400;

    public static int hitCountX = 0;
    public static int hitRefreshX = 10;
    public static int hitDamagehX = 1;




    public static ArrayList<Entity> entities = new ArrayList<Entity>();
    public static ArrayList<Entity> entitiesR = new ArrayList<Entity>();
    public static void main(String[] args) {
        //game variables
        boolean running = true;
        Inv.addItem("kyanite");

        //game loop
        while (running) {
            wait(50);
            entityRenderUpdater();
            display(map);
            processInput(map);
            input = "";

            //health
            checkForDamage();

            //Sidebars
            gui.inventoryFieldUpdater("hello vietnam" + Inv.retString());
            foodRefresh();
            oxygenRefresh();
            oxygenRefill();
            barRefresh();

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
        for (int h = 0; h < entitiesR.size(); h++) {
            if (entitiesR.get(h).lightPower > 0) {
                xArr.add(entitiesR.get(h).x);
                yArr.add(entitiesR.get(h).y);
                pArr.add(entitiesR.get(h).lightPower);
            }
        }
        //TODO add tile checker for light sources

        //all x/y on screen

        xArr.add(playerX);
        yArr.add(playerY);
        pArr.add(playerLightVal / 8);

        for (int y = py - viewportY; y <= py + viewportY; y++) {
            for (int x = px - viewportX; x < px + viewportX; x++) {
                try {
                    //add player
                    if (y == playerY && x == playerX) {
                        textPane.append("P ");

                        //add entities
                    } else if (checkEntity(x, y)) {
                        textPane.append(getEntityChar(x, y) + " ", getShade(getEntityColor(x, y), MForTile(mapGrid.map[x][y], x, y, xArr, yArr, pArr, direction)));

                    } else {
                        if (x >= mapGrid.maxX || y >= mapGrid.maxY || y < 0 || x < 0) {
                            textPane.append("| ", Color.white);
                        } else {
                            //add tile
                            textPane.append(charForTile(mapGrid.map[x][y]) + " ", getShade(getTileColor(mapGrid.map[x][y].tileType), MForTile(mapGrid.map[x][y], x, y, xArr, yArr, pArr, direction)));
                        }
                    }
                } catch (Exception ignored) {

                }
            }
            textPane.append("\n", Color.white);
        }
        textPane.setVisible(true);
        textPane2.setVisible(false);
    }
    public static boolean checkEntity(int x, int y) {
        for (Entity entity : entitiesR) {
            if (entity.x == x && entity.y == y) {
                return true;
            }
        }
        return false;
    }
    public static String getEntityChar(int x, int y) {
        for (Entity entity : entitiesR) {
            if (entity.x == x && entity.y == y) {
                return entity.chara;
            }
        }
        return "!";
    }
    public static Color getEntityColor(int x, int y) {
        for (Entity entity : entitiesR) {
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
        for (Entity entity : entitiesR) {

            if (entity.type.equals("bubble")) {
                //desynchronizes entity motion by comparing entityTickCount to a value assigned to entity at its birth depending on the tick, instead of updating on the same tick for all entities!
                if (!entity.hasStartingDivisible) {
                    entity.startingDivisible = entityTickCount % 4;
                    entity.hasStartingDivisible = true;
                }
                if (entityTickCount % entity.speed == entity.startingDivisible) {
                    entity.y--;
                }
                //dont remove entities while list is iterating
                if (mapGrid.map[entity.x][entity.y].tileType.equals("air") || mapGrid.map[entity.x][entity.y].tileType.equals("earth")) {
                    entity.flagForRemoval = true;
                }
                if (playerX == entity.x && playerY == entity.y) {
                    entity.flagForRemoval = true;
                    gui.errorFieldUpdater("BUBBLE COLLECTED", Color.white);
                    oxygen = oxygen + bubbleOxygenRate;
                    if(oxygen > oxygen_max){
                        oxygen = oxygen_max;
                    }
                }

            } else if (entity.type.equals("fish")) {
                int oldx = entity.x;
                int oldy = entity.y;
                entity.AI.x = entity.x;
                entity.AI.y = entity.y;

                //normal behavior
                entity.AI.avoid = false;
                entity.color = Color.green;
                entity.speed = 4;
                //ENTITY LIGHTING MODULATOR
                entity.lightPower = (int) (3 * Math.sin(0.25*entityTickCount) + 5.0);
                //predator avoider
                for (Entity entity2 : entitiesR) {
                    if (entity.ID != entity2.ID) {
                        if (entity2.type.equals("hunter")) {
                            if (5 > Math.sqrt((entity2.y - entity.y) * (entity2.y - entity.y) + (entity2.x - entity.x) * (entity2.x - entity.x))) {
                                entity.AI.avoid = true;
                                entity.AI.xa = entity2.x;
                                entity.AI.ya = entity2.y;
                                entity.speed = 1;
                            }
                        }
                    }
                }
                if (playerInteract) {
                    //player avoider
                    if (5 > Math.sqrt((playerY - entity.y) * (playerY - entity.y) + (playerX - entity.x) * (playerX - entity.x))) {
                        entity.AI.avoid = true;
                        entity.AI.xa = playerX;
                        entity.AI.ya = playerY;
                        entity.speed = 1;
                    }
                }

                if (entityTickCount % entity.speed == 0) {
                    entity.y = entity.y + entity.AI.getMoveY();
                    entity.x = entity.x + entity.AI.getMoveX();
                }

                //out of bounds
                if (entity.x >= mapGrid.maxX || entity.y >= mapGrid.maxY || entity.x < 0 || entity.y < 0) {
                    entity.x = oldx;
                    entity.y = oldy;
                }
                //air
                if (mapGrid.map[entity.x][entity.y].tileType.equals("air")) {

                    entity.y = oldy;
                }
                //solid
                if (!mapGrid.map[entity.x][entity.y].canMove()) {
                    entity.x = oldx;
                    entity.y = oldy;
                }

            } else if (entity.type.equals("hunter")) {
                int oldx = entity.x;
                int oldy = entity.y;
                entity.AI.x = entity.x;
                entity.AI.y = entity.y;

                //normal behavior
                entity.AI.target = false;
                entity.color = Color.red;
                entity.speed = 4;

                //scan for "fish" to chase within 10 radius, sets target coordinates and targeting boolean to fish within radius
                for (Entity entity2 : entitiesR) {
                    if (entity.ID != entity2.ID) {
                        if (entity2.type.equals("fish")) {
                            if (10 > Math.sqrt((entity2.y - entity.y) * (entity2.y - entity.y) + (entity2.x - entity.x) * (entity2.x - entity.x))) {
                                entity.AI.target = true;
                                entity.AI.xt = entity2.x;
                                entity.AI.yt = entity2.y;
                                entity.speed = 2;
                            }
                        }
                    }
                }

                if (playerInteract) {
                    //attack players within 10 ft
                    if (10 > Math.sqrt((playerY - entity.y) * (playerY - entity.y) + (playerX - entity.x) * (playerX - entity.x))) {
                        entity.AI.target = true;
                        entity.AI.xt = playerX;
                        entity.AI.yt = playerY;
                        entity.speed = 2;
                    }
                }
                if (entityTickCount % entity.speed == 0) {
                    entity.y = entity.y + entity.AI.getMoveY();
                    entity.x = entity.x + entity.AI.getMoveX();
                }

                //out of bounds
                if (entity.x >= mapGrid.maxX || entity.y >= mapGrid.maxY || entity.x < 0 || entity.y < 0) {
                    entity.x = oldx;
                    entity.y = oldy;
                }
                //air
                if (mapGrid.map[entity.x][entity.y].tileType.equals("air")) {

                    entity.y = oldy;
                }
                //solid
                if (!mapGrid.map[entity.x][entity.y].canMove()) {
                    entity.x = oldx;
                    entity.y = oldy;
                }

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

    public static void entityRenderUpdater() {
        entitiesR.clear();
        for (Entity entity : entities) {
            if (entity.x < playerX + viewportX + 5 && entity.x > playerX - viewportX - 5) {
                if (entity.y < playerY + viewportY + 5 && entity.y > playerY - viewportY - 5) {
                    entitiesR.add(entity);
                }
            }
        }

        gui.errorFieldUpdater("R: " + entitiesR.size() + " T: " + entities.size(), Color.white);
    }
    public static void entitySpawner() {
        for (int x = 0; x < mapGrid.maxX; x++) {
            for (int y = 0; y < mapGrid.maxY; y++) {
                if (mapGrid.map[x][y].tileType.equals("brain")) {
                    if (rand.nextInt(200) < 1) {
                        entities.add(new Entity("O", "bubble", x, y, new Color(255,255,255), 0));
                    }
                }
                if (mapGrid.map[x][y].tileType.equals("water")) {
                    if (entities.size() < 100) {
                        if (rand.nextInt(1000) == 0) {
                            entities.add(new Entity("o", "fish", x, y, new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)), 5));
                        } else {
                            if (rand.nextInt(2000) == 0) {
                                entities.add(new Entity("X", "hunter", x, y, new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)), 0));
                            }
                        }
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
        if (grabbing) {

            //check for something to grab
            //then change map or entity list / amend inv
            for (Entity entity : entitiesR) {
                if (entity.type.equals("fish")) {
                    if (entity.x <= playerX + 1 && entity.x >= playerX - 1) {
                        if (entity.y <= playerY + 1 && entity.y >= playerY - 1) {
                            entity.flagForRemoval = true;
                            MainLoop.Inv.addItem("Fish");
                        }
                    }
                }
            }


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
        if(MainLoop.input.equals("q")){
            //System.out.print("q");
            if(mineCounter < mineCounterMax){
                mineCounter++;
            }else{
                mineCounter = 0;
                //apply mine
                //mining down into not air or water
                if (direction.equals("S")
                        && ((
                        !(MainLoop.map.map[playerX][playerY+1].tileType.equals("water"))
                                && !(MainLoop.map.map[playerX][playerY+1].tileType.equals("air"))))) {
                    //if water adjacent or above
                    int blockX = playerX;
                    int blockY = playerY + 1;
                    if (MainLoop.map.map[blockX - 1][blockY].waterBased() || //left
                            MainLoop.map.map[blockX + 1][blockY].waterBased() || //right
                            MainLoop.map.map[blockX - 1][blockY - 1].waterBased() || //NW
                            MainLoop.map.map[blockX][blockY - 1].waterBased() || //N
                            MainLoop.map.map[blockX + 1][blockY - 1].waterBased() //NE
                    ) {
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "water";
                    } else {
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "air";
                    }
                }

                //mining downleft into not air or water
                if (direction.equals("SE")
                        && ((
                        !(MainLoop.map.map[playerX+1][playerY+1].tileType.equals("water"))
                                && !(MainLoop.map.map[playerX+1][playerY+1].tileType.equals("air"))))) {
                    //if water adjacent or above
                    int blockX = playerX+1;
                    int blockY = playerY+1;
                    if(MainLoop.map.map[blockX - 1][blockY].waterBased() || //left
                            MainLoop.map.map[blockX + 1][blockY].waterBased() || //right
                            MainLoop.map.map[blockX-1][blockY-1].waterBased() || //NW
                            MainLoop.map.map[blockX][blockY-1].waterBased() || //N
                            MainLoop.map.map[blockX+1][blockY-1].waterBased() //NE
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "water";
                    }else {
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "air";
                    }
                }
                if (direction.equals("SW")
                        && ((
                        !(MainLoop.map.map[playerX-1][playerY+1].tileType.equals("water"))
                                && !(MainLoop.map.map[playerX-1][playerY+1].tileType.equals("air"))))) {
                    //if water adjacent or above
                    int blockX = playerX-1;
                    int blockY = playerY+1;
                    if(MainLoop.map.map[blockX - 1][blockY].waterBased() || //left
                            MainLoop.map.map[blockX + 1][blockY].waterBased() || //right
                            MainLoop.map.map[blockX-1][blockY-1].waterBased() || //NW
                            MainLoop.map.map[blockX][blockY-1].waterBased() || //N
                            MainLoop.map.map[blockX+1][blockY-1].waterBased() //NE
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "water";
                    }else {
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "air";
                    }
                }
                if (direction.equals("W")
                        && ((
                        !(MainLoop.map.map[playerX-1][playerY].tileType.equals("water"))
                                && !(MainLoop.map.map[playerX-1][playerY].tileType.equals("air"))))) {
                    //if water adjacent or above
                    int blockX = playerX-1;
                    int blockY = playerY;
                    if(MainLoop.map.map[blockX - 1][blockY].waterBased() || //left
                            MainLoop.map.map[blockX + 1][blockY].waterBased() || //right
                            MainLoop.map.map[blockX-1][blockY-1].waterBased() || //NW
                            MainLoop.map.map[blockX][blockY-1].waterBased() || //N
                            MainLoop.map.map[blockX+1][blockY-1].waterBased() //NE
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "water";
                    }else {
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "air";
                    }
                }
                if (direction.equals("E")
                        && ((
                        !(MainLoop.map.map[playerX+1][playerY].tileType.equals("water"))
                                && !(MainLoop.map.map[playerX+1][playerY].tileType.equals("air"))))) {
                    //if water adjacent or above
                    int blockX = playerX+1;
                    int blockY = playerY;
                    if(MainLoop.map.map[blockX - 1][blockY].waterBased() || //left
                            MainLoop.map.map[blockX + 1][blockY].waterBased() || //right
                            MainLoop.map.map[blockX-1][blockY-1].waterBased() || //NW
                            MainLoop.map.map[blockX][blockY-1].waterBased() || //N
                            MainLoop.map.map[blockX+1][blockY-1].waterBased() //NE
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "water";
                    }else {
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "air";
                    }
                }
                if (direction.equals("N")
                        && ((
                        !(MainLoop.map.map[playerX][playerY-1].tileType.equals("water"))
                                && !(MainLoop.map.map[playerX][playerY-1].tileType.equals("air"))))) {
                    //if water adjacent or above
                    int blockX = playerX;
                    int blockY = playerY-1;
                    if(MainLoop.map.map[blockX - 1][blockY].waterBased() || //left
                            MainLoop.map.map[blockX + 1][blockY].waterBased() || //right
                            MainLoop.map.map[blockX-1][blockY-1].waterBased() || //NW
                            MainLoop.map.map[blockX][blockY-1].waterBased() || //N
                            MainLoop.map.map[blockX+1][blockY-1].waterBased() //NE
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "water";
                    }else if(MainLoop.map.map[blockX - 1][blockY].airBased() || //left
                            MainLoop.map.map[blockX + 1][blockY].airBased() || //right
                            MainLoop.map.map[blockX-1][blockY-1].airBased() || //NW
                            MainLoop.map.map[blockX][blockY-1].airBased() || //N
                            MainLoop.map.map[blockX+1][blockY-1].airBased() //NE
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "air";
                    }else if(MainLoop.map.map[blockX][blockY+1].airBased() || //S
                            MainLoop.map.map[blockX + 1][blockY+1].airBased() || //SE
                            MainLoop.map.map[blockX-1][blockY+1].airBased()  //SW
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "air";
                    }else{
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "water";
                    }
                }
                if (direction.equals("NE")
                        && ((
                        !(MainLoop.map.map[playerX+1][playerY-1].tileType.equals("water"))
                                && !(MainLoop.map.map[playerX+1][playerY-1].tileType.equals("air"))))) {
                    //if water adjacent or above
                    int blockX = playerX+1;
                    int blockY = playerY-1;
                    if(MainLoop.map.map[blockX - 1][blockY].waterBased() || //left
                            MainLoop.map.map[blockX + 1][blockY].waterBased() || //right
                            MainLoop.map.map[blockX-1][blockY-1].waterBased() || //NW
                            MainLoop.map.map[blockX][blockY-1].waterBased() || //N
                            MainLoop.map.map[blockX+1][blockY-1].waterBased() //NE
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "water";
                    }else if(MainLoop.map.map[blockX - 1][blockY].airBased() || //left
                            MainLoop.map.map[blockX + 1][blockY].airBased() || //right
                            MainLoop.map.map[blockX-1][blockY-1].airBased() || //NW
                            MainLoop.map.map[blockX][blockY-1].airBased() || //N
                            MainLoop.map.map[blockX+1][blockY-1].airBased() //NE
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "air";
                    }else if(MainLoop.map.map[blockX][blockY+1].airBased() || //S
                            MainLoop.map.map[blockX + 1][blockY+1].airBased() || //SE
                            MainLoop.map.map[blockX-1][blockY+1].airBased()  //SW
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "air";
                    }else{
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "water";
                    }
                }
                if (direction.equals("NW")
                        && ((
                        !(MainLoop.map.map[playerX-1][playerY-1].tileType.equals("water"))
                                && !(MainLoop.map.map[playerX-1][playerY-1].tileType.equals("air"))))) {
                    //if water adjacent or above
                    int blockX = playerX-1;
                    int blockY = playerY-1;
                    if(MainLoop.map.map[blockX - 1][blockY].waterBased() || //left
                            MainLoop.map.map[blockX + 1][blockY].waterBased() || //right
                            MainLoop.map.map[blockX-1][blockY-1].waterBased() || //NW
                            MainLoop.map.map[blockX][blockY-1].waterBased() || //N
                            MainLoop.map.map[blockX+1][blockY-1].waterBased() //NE
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "water";
                    }else if(MainLoop.map.map[blockX - 1][blockY].airBased() || //left
                            MainLoop.map.map[blockX + 1][blockY].airBased() || //right
                            MainLoop.map.map[blockX-1][blockY-1].airBased() || //NW
                            MainLoop.map.map[blockX][blockY-1].airBased() || //N
                            MainLoop.map.map[blockX+1][blockY-1].airBased() //NE
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "air";
                    }else if(MainLoop.map.map[blockX][blockY+1].airBased() || //S
                            MainLoop.map.map[blockX + 1][blockY+1].airBased() || //SE
                            MainLoop.map.map[blockX-1][blockY+1].airBased()  //SW
                    ){
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "air";
                    }else{
                        GetMined(blockX, blockY);
                        MainLoop.map.map[blockX][blockY].tileType = "water";
                    }
                }
            }
        }
        else{
            mineCounter = 0;
        }

        grabbing = false;
    }
    public static void GetMined(int x, int y){
        MainLoop.Inv.addItem(MainLoop.map.map[x][y].tileType);
    }

    //display functions
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
    public static double MForTile(tileSet tile, int x, int y, ArrayList<Integer> xArr, ArrayList<Integer> yArr, ArrayList<Integer> pArr, String direction) {
        //depth lighting

        double daylight = MainLoop.dayCount / 3000.0;
        daylight = (Math.sin(daylight * 2 * Math.PI) + 1) / 2.0;
        double elevation = y;
        if (elevation >= 100) {
            elevation = 100;
        }
        double m1 = daylight * (100 - elevation) / 100.0;

        if (checkInCone(direction, x, y)) {
            xArr.add(playerX);
            yArr.add(playerY);
            pArr.add(playerLightVal);
        }
        //entity lighting
        double[] pr = new double[xArr.size()];
        for (int i = 0; i < pr.length; i++) {
            int dx = Math.abs(x - xArr.get(i));
            int dy = Math.abs(y - yArr.get(i));
            double radius = pow(((pow(dx, 2)) + (pow(dy, 2))), 0.5);
            pr[i] = pow(pArr.get(i) / radius, (2))/(radius*radius) + 0.1;
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

        if (xArr.size() > 0) {
            if (checkInCone(direction, x, y)) {
                xArr.remove(xArr.size() - 1);
                yArr.remove(yArr.size() - 1);
                pArr.remove(pArr.size() - 1);
            }
        }
        if (m < 0.05) {
            m = 0.05;
        }

        return m;
    }
    public static boolean checkInCone(String direction, int x, int y) {
        //y < ((playerX - x) * 0.5) + playerY + 1 && y > ((playerX - x) * -0.5) + playerY - 1

        if (direction.equals("W")) {
            if (y < ((playerX - x) * 0.5) + playerY + 1 && y > ((playerX - x) * -0.5) + playerY - 1 && playerX > x) {
                return true;
            }
        }
        if (direction.equals("S")) {
            if (y > ((playerX - x) * 2) + playerY - 2 && y > ((playerX - x) * -2) + playerY - 2 && playerY < y) {
                return true;
            }
        }
        if (direction.equals("E")) {
            if (y > ((playerX - x) * 0.5) + playerY - 1 && y < ((playerX - x) * -0.5) + playerY + 1 && playerX < x) {
                return true;
            }
        }
        if (direction.equals("N")) {
            if (y < ((playerX - x) * 2) + playerY + 2 && y < ((playerX - x) * -2) + playerY + 2 && playerY > y) {
                return true;
            }
        }

        if (direction.equals("NW")) {
            if (y >= (playerX - x) * -3 + playerY - 3 && y <= (playerX - x) * -0.33 + playerY + 1 && x <= playerX && y <= playerY) {
                return true;
            }
        }
        if (direction.equals("NE")) {
            if (y >= (playerX - x) * 3 + playerY - 3 && y <= (playerX - x) * 0.33 + playerY + 1 && x >= playerX && y <= playerY) {
                return true;
            }
        }
//
        if (direction.equals("SE")) {
            if (y <= (playerX - x) * -3 + playerY + 3 && y >= (playerX - x) * -0.33 + playerY - 1 && x >= playerX && y >= playerY ) {
                return true;
            }
        }
        //y <= (playerX - x) * 2 + playerY + 2 && y <= (playerX - x) * -0.5 + playerY - 1 &&
        if (direction.equals("SW")) {
            if (y <= (playerX - x) * 3 + playerY + 3 && y >= (playerX - x) * 0.33+ playerY - 1 && x <= playerX && y >= playerY) {
                return true;
            }
        }


        return false;
    }
    public static Color getTileColor (String name) {
        switch (name) {
            case "air":
                return new Color((int) (220), (int) (200), (int) (160));
            case "water":
                return new Color(0, 79, 198);
            case "earth":
                return new Color(153, 114, 3);
            case "ore":
                return new Color(108, 83, 83);
            case "kelp":
                return new Color(0, 78, 24);
            case "brain":
                return new Color((int) (250), (int) (100), (int) (120));
            case "fruit":
                return new Color((int) (250), (int) (250), (int) (0));
            case "mushroom":
                return new Color((int) (204), (int) (0), (int) (0));
            case "mushroom2":
                return new Color((int) (254), (int) (250), (int) (0));
            case "mushroom3":
                return new Color((int) (250), (int) (51), (int) (152));
            default:
                return new Color((int) (255), (int) ( 0), (int) (0));
        }
    }
    public static Color getShade(Color color, double m) {
        double red = m * color.getRed();
        double green = m * color.getGreen();
        double blue = m * color.getBlue();
        return new Color((int) red, (int) green, (int) blue);
    }

    //char stat functions
    public static void oxygenRefresh(){
        oxygenCount++;
        if(oxygenCount == oxygenCountMax){
            oxygenCount = 0;
            oxygen--;
        }
    }
    public static void foodRefresh(){
        foodCount++;
        if(foodCount == foodCountMax){
            foodCount = 0;
            food--;
        }
    }
    public static void oxygenRefill(){
        if(playerX == 1 || playerX == 0 || playerY == 1 || playerY == 0 || playerX == 400 || playerX == 399 || playerY == 200 || playerY == 199 ){
            //dont check
        }else {
            if (map.map[playerX][playerY - 1].tileType == "air") {
                oxygen = oxygen + oxygenRefillRate;
                if(oxygen > oxygen_max){
                    oxygen = oxygen_max;
                }
            } else if (map.map[playerX][playerY].tileType == "air") {
                oxygen = oxygen + oxygenRefillRate;
                if(oxygen > oxygen_max){
                    oxygen = oxygen_max;
                }
            } else if (map.map[playerX][playerY + 1].tileType == "air") {
                oxygen = oxygen + oxygenRefillRate;
                if(oxygen > oxygen_max){
                    oxygen = oxygen_max;
                }
            }else if (map.map[playerX + 1][playerY - 1].tileType == "air") {
                oxygen = oxygen + oxygenRefillRate;
                if(oxygen > oxygen_max){
                    oxygen = oxygen_max;
                }
            }else if (map.map[playerX + 1][playerY].tileType == "air") {
                oxygen = oxygen + oxygenRefillRate;
                if(oxygen > oxygen_max){
                    oxygen = oxygen_max;
                }
            }else if (map.map[playerX + 1][playerY + 1].tileType == "air") {
                oxygen = oxygen + oxygenRefillRate;
                if(oxygen > oxygen_max){
                    oxygen = oxygen_max;
                }
            }else if (map.map[playerX - 1][playerY - 1].tileType == "air") {
                oxygen = oxygen + oxygenRefillRate;
                if(oxygen > oxygen_max){
                    oxygen = oxygen_max;
                }
            }else if (map.map[playerX - 1][playerY + 1].tileType == "air") {
                oxygen = oxygen + oxygenRefillRate;
                if(oxygen > oxygen_max){
                    oxygen = oxygen_max;
                }
            }else if (map.map[playerX - 1][playerY].tileType == "air") {
                oxygen = oxygen + oxygenRefillRate;
                if(oxygen > oxygen_max){
                    oxygen = oxygen_max;
                }
            }
        }
    }
    public static void barRefresh(){
        String healthStr = "";
        for(int x = 0; x < (health); x++){
            healthStr = healthStr + "H";
        }
        String foodStr = "";
        for(int x = 0; x < (food); x++){
            foodStr = foodStr + "F";
        }
        String oxyStr = "";
        for(int x = 0; x < (oxygen/10); x++){
            oxyStr = oxyStr + "O";
        }
        gui.healthPane.setText("");
        gui.healthPane.append("Health: " + healthStr + "\n",Color.red);
        gui.healthPane.append("Food:   " + foodStr + "\n",Color.cyan);
        gui.healthPane.append("Oxygen: " + oxyStr + "\n",Color.white);
    }
    public static void updateHitCounts(){
        if(hitCountX > 0 ){
            hitCountX--;
        }
    }
    public static void checkForDamage() {
        updateHitCounts();
        for (int g = 0; g < entitiesR.size(); g++) {
            int x = entitiesR.get(g).x; //get x
            int y = entitiesR.get(g).y; //get y

            if (playerX == 1 || playerX == 0 || playerY == 1 || playerY == 0 || playerX == 400 || playerX == 399 || playerY == 200 || playerY == 199) {
                //dont check near borders
            } else {
                if (playerX == x && playerY == y) {
                    if(hitCountX == 0){
                        health = health - hitDamagehX;
                        hitCountX = hitRefreshX;
                    }
                } else if (map.map[playerX][playerY].tileType == "air") {
                    oxygen = oxygen + oxygenRefillRate;
                    if (oxygen > oxygen_max) {
                        oxygen = oxygen_max;
                    }
                } else if (map.map[playerX][playerY + 1].tileType == "air") {
                    oxygen = oxygen + oxygenRefillRate;
                    if (oxygen > oxygen_max) {
                        oxygen = oxygen_max;
                    }
                } else if (map.map[playerX + 1][playerY - 1].tileType == "air") {
                    oxygen = oxygen + oxygenRefillRate;
                    if (oxygen > oxygen_max) {
                        oxygen = oxygen_max;
                    }
                } else if (map.map[playerX + 1][playerY].tileType == "air") {
                    oxygen = oxygen + oxygenRefillRate;
                    if (oxygen > oxygen_max) {
                        oxygen = oxygen_max;
                    }
                } else if (map.map[playerX + 1][playerY + 1].tileType == "air") {
                    oxygen = oxygen + oxygenRefillRate;
                    if (oxygen > oxygen_max) {
                        oxygen = oxygen_max;
                    }
                } else if (map.map[playerX - 1][playerY - 1].tileType == "air") {
                    oxygen = oxygen + oxygenRefillRate;
                    if (oxygen > oxygen_max) {
                        oxygen = oxygen_max;
                    }
                } else if (map.map[playerX - 1][playerY + 1].tileType == "air") {
                    oxygen = oxygen + oxygenRefillRate;
                    if (oxygen > oxygen_max) {
                        oxygen = oxygen_max;
                    }
                } else if (map.map[playerX - 1][playerY].tileType == "air") {
                    oxygen = oxygen + oxygenRefillRate;
                    if (oxygen > oxygen_max) {
                        oxygen = oxygen_max;
                    }
                }
            }
        }//for every entity
    }
}
