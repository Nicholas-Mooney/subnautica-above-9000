import java.util.*;

public class MainLoop {
    public static Random rand = new Random();
    public static Scanner sc = new Scanner(System.in);

    public static int playerX = 10;
    public static int playerY = 50;
    public static int map_sizeX = 1000;
    public static int map_sizeY = 1000;
    public static int viewportX = 10;
    public static int viewportY = 5;

    public static String input = "starting_input";

    public static void main(String[] args) {
        //game variables
        boolean running = true;
        mapGrid map = new mapGrid(1000);

        //game loop
        while (running) {
            display();
            input();
            processInput();
        }
    }

    //input functions
    public static void input() {
        input = sc.nextLine();
    }
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
    public static void display() {
        for (int y = playerY - viewportY; y < playerY + viewportY; y++) {
            for (int x = playerX - viewportX; x < playerX + viewportX; x++) {
                if (y == playerY && x == playerX) {
                    System.out.print("P ");
                } else {
                    if(x >= mapGrid.maxX || y >= mapGrid.maxY || y < 0 || x < 0){
                        System.out.print("| ");
                    }else {
                        System.out.print(displayForTile(mapGrid.map[x][y]));
                        System.out.print(" ");
                    }
                }
            }
            System.out.println(" ");
        }
    }
    public static char displayForTile(tileSet tile){
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
}
