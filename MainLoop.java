import java.awt.*;
import java.util.*;

public class MainLoop {
    public static Random rand = new Random();
    public static Scanner sc = new Scanner(System.in);
    public static GUI gui = new GUI(); //THIS
    public static int playerX = 10;
    public static int playerY = 50;
    public static int map_sizeX = 1000;
    public static int map_sizeY = 1000;
    public static int viewportX = 15;
    public static int viewportY = 10;

    public static int count = 0; ///THIS

    public static String input = "starting_input";

    public static void main(String[] args) {
        //game variables
        boolean running = true;
        mapGrid map = new mapGrid(1000);

        //game loop
        while (running) {
            wait(100);
            display(map);
            processInput();
            input = "";
      }
    }
    public static void wait(int x) {
        try {
            Thread.sleep(x);
        } catch (Exception e) {

        }
    }
    //THESE
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
    public static void processInput() {
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
        unmover();
    }
    public static void unmover(){
        if(playerX >= mapGrid.maxX || playerY >= mapGrid.maxY || playerY < 0 || playerX < 0){
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
    };

    //
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
            default:
                return Color.RED;
        }
    }
}
