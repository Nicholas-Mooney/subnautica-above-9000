import java.awt.*;
import java.util.*;

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

    public static String input = "starting_input";

    public static void main(String[] args) {
        //game variables
        boolean running = true;
        mapGrid map = new mapGrid(1000);
        playerInv Inv = new playerInv();
        Inv.addItem("kyanite");


        //game loop
        while (running) {
            wait(50);
            display(map);
            processInput(map);
            input = "";
            gui.inventoryFieldUpdater("hello vietnam" + Inv.retString());
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

        for (int y = py - viewportY; y <=  py + viewportY; y++) {
            for (int x = px - viewportX; x < px + viewportX; x++) {
                if (y == playerY && x == playerX) {
                    textPane.append("P ");
                } else {
                    if(x >= mapGrid.maxX || y >= mapGrid.maxY || y < 0 || x < 0){
                        textPane.append("| ", Color.white);
                        //System.out.print("| ");
                    }else {
                        //System.out.print(displayForTile(mapGrid.map[x][y]));
                        textPane.append(charForTile(mapGrid.map[x][y]) + " ", colorForTile(mapGrid.map[x][y]));
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
        }
        if (!GUI.UP_HELD && GUI.DOWN_HELD && !GUI.LEFT_HELD && !GUI.RIGHT_HELD) {
            MainLoop.input = "s";
        }
        if (!GUI.UP_HELD && !GUI.DOWN_HELD && !GUI.LEFT_HELD && GUI.RIGHT_HELD) {
            MainLoop.input = "d";
        }
        if (!GUI.UP_HELD && !GUI.DOWN_HELD && GUI.LEFT_HELD && !GUI.RIGHT_HELD) {
            MainLoop.input = "a";
        }
        if (GUI.UP_HELD && !GUI.DOWN_HELD && !GUI.LEFT_HELD && GUI.RIGHT_HELD) {
            MainLoop.input = "e";
        }
        if (GUI.UP_HELD && !GUI.DOWN_HELD && GUI.LEFT_HELD && !GUI.RIGHT_HELD) {
            MainLoop.input = "q";
        }
        if (!GUI.UP_HELD && GUI.DOWN_HELD && !GUI.LEFT_HELD && GUI.RIGHT_HELD) {
            MainLoop.input = "c";
        }
        if (!GUI.UP_HELD && GUI.DOWN_HELD && GUI.LEFT_HELD && !GUI.RIGHT_HELD) {
            MainLoop.input = "z";
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
        if (input.equals("e")) {
            playerX++;
            playerY--;
        }
        if (input.equals("q")) {
            playerY--;
            playerX--;
        }
        if (input.equals("c")) {
            playerX++;
            playerY++;
        }
        if (input.equals("z")) {
            playerY++;
            playerX--;
        }
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
            if (input.equals("e") && (map.map[playerX][playerY].tileType.equals("air"))) {
                playerY++;
                playerX--;
            }
            //going up into solid
            if (input.equals("e") && !(map.map[playerX][playerY].canMove())) {
                playerY++;
                playerX--;
            }
            //going up into air
            if (input.equals("q") && (map.map[playerX][playerY].tileType.equals("air"))) {
                playerY++;
                playerX++;
            }
            //going up into solid
            if (input.equals("q") && !(map.map[playerX][playerY].canMove())) {
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
            } else if ((input.equals("e") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX--;
                playerY++;
            }
            else if ((input.equals("c") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX--;
                playerY--;
            }

            //going left into solid w/o air
            else if ((input.equals("a") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX++;
            } else if ((input.equals("z") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX++;
                playerY--;
            }
            else if ((input.equals("q") && !(map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerX++;
                playerY++;
            }

            //going left or right w/ air above
            else if (((input.equals("a") || input.equals("d")) && (map.map[playerX][playerY - 1].tileType.equals("air")) && !(map.map[playerX][playerY].canMove()))) {
                playerY--;
            }
        }
    };

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
            default:
                return '!';
        }
    }
    public static Color colorForTile(tileSet tile){
        switch(tile.tileType){
            case "air":
                return Color.white;
            case "water":
                return Color.BLUE;
            case "earth":
                return new Color(87, 71, 46);
            case "ore":
                return Color.GRAY;
            case "kelp":
                return Color.GREEN;
            case "brain":
                return Color.pink;
            default:
                return Color.RED;
        }
    }
}
