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
        boolean running = true;

        while (running) {

            display();
            input();
            processInput();
        }
    }

    public static void input() {
        input = sc.nextLine();
    }
    public static void processInput() {
        if (input.equals("a")) {
            playerX--;
        }
        if (input.equals("s")) {
            playerY--;
        }
        if (input.equals("d")) {
            playerX++;
        }
        if (input.equals("w")) {
            playerY++;
        }
    }

    public static void display() {
        for (int y = playerY + viewportY; y >= playerY - viewportY; y--) {
            for (int x = playerX - viewportX; x < playerX + viewportX; x++) {

                if (y == playerY && x == playerX) {
                    System.out.print("P ");
                } else {
                    System.out.print(x + " s");
                }

            }
            System.out.println(" ");

        }

    }




}
