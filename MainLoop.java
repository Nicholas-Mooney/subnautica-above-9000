import com.sun.tools.javac.Main;

import java.awt.*;
import java.util.*;
import java.lang.Math;

import static java.lang.Math.abs;

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
            map.mapRefresh();

            //daycount
            dayCount++;
            if( dayCount > 3000 ){
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
    public static void displayRoomX(JTextAreaA textPane, JTextAreaA textPane2, mapGrid map) {
        textPane.setText("");
        int py = playerY;
        int px = playerX;

        for (int y = py - viewportY; y <=  py + viewportY; y++) {
            for (int x = px - viewportX; x < px + viewportX; x++) {
                if (y == playerY && x == playerX) {
                    textPane.append("P ");
                } else if (checkEntity(x, y)) {
                    textPane.append(getEntityChar(x, y) + " ", getEntityColor(x, y));
                } else {
                    if(x >= mapGrid.maxX || y >= mapGrid.maxY || y < 0 || x < 0){
                        textPane.append("| ", Color.white);
                        //System.out.print("| ");
                    }else {
                        //System.out.print(displayForTile(mapGrid.map[x][y]));
                        int dx = abs(px - x);
                        int dy = abs(py - y);
                        int r;
                        if(dx <= 1 && dy <= 1){
                            r = 1;
                        }else if ((dx <= 2 && dy <= 2) && !(dx == 2 && dy == 2)){
                            r = 2;
                        }else if((dx == 2 && dy == 2) || (dx == 3 && dy <= 1) || (dy == 3 && dx <= 1)){
                            r =3;
                        }else if((dx == 2 && dy == 3) || (dx == 3 && dy == 2)  || (dx == 4 && dy <= 1) || (dy == 4 && dx <= 1)){
                            r =4;
                        }else{
                            r = 5;
                        }
                        textPane.append(charForTile(mapGrid.map[x][y]) + " ", colorForTile(mapGrid.map[x][y], y, r));
                    }
                }
            }
            textPane.append("\n", Color.white);
        }
        textPane.setVisible(true);
        textPane2.setVisible(false);
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
        while(map.map[playerX][playerY].tileType.equals("air") && (map.map[playerX][playerY+1].tileType.equals("air") || map.map[playerX][playerY+1].canMove())){
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
    public static void unmover(mapGrid map){
        boolean outOfBoundsFlag = false;

        //Hit border
        if(playerX >= mapGrid.maxX || playerY >= mapGrid.maxY || playerY < 0 || playerX < 0){
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
        else if(playerY == 0){
            outOfBoundsFlag = true;
            if(!map.map[playerX][playerY].canMove()) {
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
        if(!outOfBoundsFlag) {


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
            if ((input.equals("d") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))){
                playerX--;
            } else if ((input.equals("wd") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX--;
                playerY++;
            }
            else if ((input.equals("sd") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX--;
                playerY--;
            }

            //going left into solid w/o air
            else if ((input.equals("a") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX++;
            } else if ((input.equals("sa") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX++;
                playerY--;
            }
            else if ((input.equals("wa") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX++;
                playerY++;
            }

            //going left or right w/ air above
            else if (((input.equals("a") || input.equals("d")) && (map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerY--;
            }
        }
    };
    public static void processAction(mapGrid map){
        if(MainLoop.input.equals("g")){
            System.out.print("g)");
            //check for something to grab
            //then change map or entity list / amend inv
            if(         map.map[playerX][playerY].tileType.equals("fruit")){
                MainLoop.map.map[playerX][playerY].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            }else if(   map.map[playerX + 1][playerY].tileType.equals("fruit")){
                MainLoop.map.map[playerX + 1][playerY].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            }else if(   map.map[playerX + 1][playerY + 1].tileType.equals("fruit")){
                MainLoop.map.map[playerX + 1][playerY + 1].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            }else if(   map.map[playerX+ 1][playerY - 1].tileType.equals("fruit")){
                MainLoop.map.map[playerX + 1][playerY - 1].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            }else if(   map.map[playerX][playerY - 1].tileType.equals("fruit")){
                MainLoop.map.map[playerX][playerY - 1].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            }else if(map.map[playerX][playerY + 1].tileType.equals("fruit")){
                MainLoop.map.map[playerX][playerY + 1].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            }else if(map.map[playerX - 1][playerY].tileType.equals("fruit")){
                MainLoop.map.map[playerX - 1][playerY].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            }else if(map.map[playerX - 1][playerY + 1].tileType.equals("fruit")){
                MainLoop.map.map[playerX - 1][playerY + 1].tileType = "water";
                MainLoop.Inv.addItem("fruit");
            }else if(map.map[playerX - 1][playerY - 1].tileType.equals("fruit")){
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
                    if(x >= mapGrid.maxX || y >= mapGrid.maxY || y < 0 || x < 0){
                        //System.out.print("| ");
                    }else {
                        //System.out.print(displayForTile(mapGrid.map[x][y]));
                        //System.out.print(" ");
                    }
                }
            }
            //System.out.println(" ");
        }
    }
    public static char charForTile(tileSet tile){
        switch(tile.tileType){
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
            default:
                return '!';
        }
    }
    public static Color colorForTile(tileSet tile, int y, int r){
        double daylight = MainLoop.dayCount/3000.0;
        daylight = (Math.sin(daylight * 2 * Math.PI) + 1) / 2.0;

        double elevation = y;
        if(elevation >= 100){
            elevation = 0;
        }
        double m1 = daylight * (100 - elevation) / 100.0;
        double m2 = 0;
        if(r <= 4){
            if(r == 1){
                m2 = 0.90;
            }else if ( r == 2){
                m2 = 0.70;
            }else if ( r == 3){
                m2 = 0.50;
            }else if ( r == 4){
                m2 = 0.30;
            }
        }
        double m = Math.max(m1, m2);


        switch(tile.tileType){
            case "air":
                return new Color((int) (m *  220), (int) (m * 200),(int) (m *    160));
            case "water":
                return new Color((int) (m *  0),(int) (m *  60),(int) (m *  135));
            case "earth":
                return new Color((int) (m *  160), (int) (m *  131), (int) (m *  46));
            case "ore":
                return new Color((int) (m *  142), (int) (m *  122), (int) (m *  102));
            case "kelp":
                return new Color((int) (m *  50), (int) (m *  220), (int) (m *  100));
            case "brain":
                return new Color((int) (m *  250),(int) (m *   100), (int) (m *  120));
            case "fruit":
                return new Color((int) (m *  250), (int) (m *  250), (int) (m *  0));
            default:
                return new Color((int)( m *  255),(int) (m *   0), (int) (m *  0));
        }
    }
}
