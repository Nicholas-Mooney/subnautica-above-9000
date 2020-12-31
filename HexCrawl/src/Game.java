import java.awt.*;
import java.util.ArrayList;

public class Game {

    public static int radx = 12; //26
    public static int rady = 12; //17
    public static int radz = 12;
    public static GUI gui = new GUI();
    public static int count = 0;
    public static String input = " ";
    public static double time = 5;
    public static boolean setting;
    public static int light = 60;
    public static int moveTimer = 0;
    public static int moveLimit = 1;
    public static int bloodtimer = 0;
    public static int bloodlimit = 5;
    public static boolean checkCollision = true;
    public static Room room = new Room();
    public static Player player = new Player();
    public static ArrayList<Fish> fish = new ArrayList<Fish>();
    public static boolean takeDamage = false;

    public static void main(String[] arg) {
        for (int x = 0; x < 35; x++) {
            fish.add(new Fish(Room.rand.nextInt(room.xmax), Room.rand.nextInt(room.ymax), Room.rand.nextInt(36 - 18) + 18, new Color(Room.rand.nextInt(100) + 50, Room.rand.nextInt(100) + 50, 0), "ƍ", "Dartfish", 18));
        }
        while (true) {
            while (fish.size() < 50) {
                fish.add(new Fish(Room.rand.nextInt(room.xmax), Room.rand.nextInt(room.ymax), Room.rand.nextInt(36 - 18) + 18, new Color(Room.rand.nextInt(100) + 50, Room.rand.nextInt(100) + 50, 0),"ƍ", "Dartfish", 18));
                gui.errorFieldUpdater("Spawned new fish", Color.white);
            }
            if (setting) {
                time = time - 0.1;
                if (time < 5) {
                    setting = false;
                }
            }
            if (!setting) {
                time = time + 0.1;
                if (time > 10) {
                    setting = true;
                }
            }
            player.oxygen = player.oxygen - 0.05;
            if (player.oxygen <= 0) {
                player.oxygen = 0;
                takeDamage = true;
            }
            checkAir(room);
            //gui.errorFieldUpdater("Oxygen: " + player.oxygen, Color.white);
            wait(50);

            for (int u = 0; u < fish.size(); u++) {
                fish.get(u).moveRandom();
            }
            checkInput(room);

            displayManager(room);
            //System.out.println("RadX" + radx + " RadY" + rady);
        }


    }

    public static void checkInput(Room room) {
        if (input.equals("grab")) {
            for (int u = 0; u < fish.size(); u++) {
                if (fish.get(u).x <= player.x + 1 && fish.get(u).x >= player.x - 1) {
                    if (fish.get(u).z <= player.z && fish.get(u).z >= player.z - 1) {
                        if (fish.get(u).y <= player.y + 1 && fish.get(u).y >= player.y - 1) {
                            fish.remove(fish.get(u));
                            gui.errorFieldUpdater("You grabbed a fish.", Color.green);
                        }
                    }
                }
            }
        }
        try {
            moveTimer++;
            if (moveTimer >= moveLimit) {
                moveTimer =0;
                switch (input) {
                    case ("down") -> {
                        player.y--;
                    }
                    case ("up") -> {
                        player.y++;
                    }
                    case ("left") -> {
                        player.x--;
                    }
                    case ("right") -> {
                        player.x++;
                    }
                    case ("ascend") -> {
                        player.z++;
                    }
                    case ("descend") -> {
                        player.z--;
                    }
                }
                if (room.objects[player.x][player.y][player.z].canCollide && checkCollision) {
                    switch (input) {
                        case ("down") -> {
                            player.y++;
                        }
                        case ("up") -> {
                            player.y--;
                        }
                        case ("left") -> {
                            player.x++;
                        }
                        case ("right") -> {
                            player.x--;
                        }
                        case ("ascend") -> {
                            player.z--;
                        }
                        case ("descend") -> {
                            player.z++;
                        }
                    }
                }
            }

        } catch (Exception ignored){

        }
        input = " ";
    }
    public static void wait(int x) {
        try {
            Thread.sleep(x);
        } catch (Exception e) {

        }
    }
    public static void displayManager(Room room) {
        if (takeDamage) {
            if (count == 0) {
                //displayRoomS(gui.textPaneHView, gui.textPaneHView2, room);
                displayRoomXBlood(gui.textPaneHView2, gui.textPaneHView, room);
                count = 1;
            }
            if (count == 1) {
                //displayRoomS(gui.textPaneHView2, gui.textPaneHView, room);
                displayRoomXBlood(gui.textPaneHView, gui.textPaneHView2, room);
                count = 0;
            }
        } else {
            if (count == 0) {
                //displayRoomS(gui.textPaneHView, gui.textPaneHView2, room);
                displayRoomX(gui.textPaneHView2, gui.textPaneHView, room);
                count = 1;
            }
            if (count == 1) {
                //displayRoomS(gui.textPaneHView2, gui.textPaneHView, room);
                displayRoomX(gui.textPaneHView, gui.textPaneHView2, room);
                count = 0;
            }
        }
    }
    public static void displayRoomX(JTextAreaA textPane, JTextAreaA textPane2, Room room) {
        textPane.setText("");
        int py = player.y;
        int px = player.x;
        int pz = player.z;

        for (int y = py + rady; y >  py - rady; y--) {
            for (int x = px - radx; x < px + radx; x++) {
                //check for fish at this xy
                boolean fishHere = false;
                for (int f = 0; f < fish.size(); f++) {
                    if (fish.get(f).x == x && fish.get(f).y == y && fish.get(f).z == pz) {
                        fish.get(f).updateShade(light / 100.0, player.x, player.y, player.z);
                        fish.get(f).updateShade2(0.9, x, y, room.waterline + 5);
                        textPane.append(fish.get(f).symbol, fish.get(f).shade);
                        textPane.append("x",Color.black);
                        fishHere = true;
                    }
                } //Œ  ŏ  Ŏ  ō  ƍ Ʊ  ƾ  ǒ  ɶ  ϑ  ϙ  ϙ  д  ю  Ѫ  ѫ  Ӂ  Ծ  Ժ	  ठ
                //show fish if it is at xyz;
                //allow fish to be seen by descender

                try {
                    room.objects[x][y][pz].updateShade(light / 100.0, player.x, player.y, player.z);
                    room.objects[x][y][pz].updateShade2(time / 10.0 - 0.2, x, y, room.waterline + 5);
                } catch (Exception ignored) {

                }
                if (!fishHere) {
                    if (y == py && x == px) {
                        textPane.append("p", Color.green);
                        textPane.append("x", Color.black);
                    } else {
                        //room.objects[x][y][z].applyDepth(pz, px, py)
                        try {
                            int pz2 = pz;
                            while (!room.objects[x][y][pz2].canSee && !fishHere) {
                                pz2--;
                                for (int f = 0; f < fish.size(); f++) {
                                    if (fish.get(f).x == x && fish.get(f).y == y && fish.get(f).z == pz2) {
                                        fish.get(f).updateShade(light / 100.0, player.x, player.y, player.z);
                                        fish.get(f).updateShade2(time / 10 - 0.2, x, y, room.waterline);
                                        textPane.append(fish.get(f).symbol, fish.get(f).shade);

                                        fishHere = true;
                                    }
                                }
                            }
                            if (!fishHere) {
                                room.objects[x][y][pz2].updateShade(light / 100.0, player.x, player.y, player.z);
                                room.objects[x][y][pz2].updateShade2(0.9, x, y, room.waterline);
                                textPane.append(room.objects[x][y][pz2].symbol, room.objects[x][y][pz2].shade);
                            }
                        } catch (Exception e) {
                            textPane.append("x", Color.black);
                        }
                        textPane.append("x", Color.black);
                    }
                }
            }
            textPane.append("\n", Color.white);
        }
        textPane.append("X: " + player.x +" Y: " + player.y + " Z: " + (player.z) + "  Oxygen: " + (int) player.oxygen + "%" + " Time: " + (int) time + " Light: " + light, Color.green);
        textPane.setVisible(true);
        textPane2.setVisible(false);
    }
    public static void displayRoomXBlood(JTextAreaA textPane, JTextAreaA textPane2, Room room) {
        textPane.setText("");
        int py = player.y;
        int px = player.x;
        int pz = player.z;

        for (int y = py + rady; y >  py - rady; y--) {
            for (int x = px - radx; x < px + radx; x++) {
                //check for fish at this xy
                boolean fishHere = false;
                for (int f = 0; f < fish.size(); f++) {
                    if (fish.get(f).x == x && fish.get(f).y == y && fish.get(f).z == pz) {
                        fish.get(f).updateShadeBlood(light / 100.0, player.x, player.y, player.z);
                        fish.get(f).updateShadeBlood2(0.9, x, y, room.waterline);
                        textPane.append(fish.get(f).symbol, fish.get(f).shade);
                        textPane.append("x",Color.black);
                        fishHere = true;
                    }
                } //Œ  ŏ  Ŏ  ō  ƍ Ʊ  ƾ  ǒ  ɶ  ϑ  ϙ  ϙ  д  ю  Ѫ  ѫ  Ӂ  Ծ  Ժ	  ठ
                //show fish if it is at xyz;
                //allow fish to be seen by descender

                try {
                    room.objects[x][y][pz].updateShadeBlood(light / 100.0, player.x, player.y, player.z);
                    room.objects[x][y][pz].updateShadeBlood2(0.9, x, y, room.waterline);
                } catch (Exception ignored) {

                }
                if (!fishHere) {
                    if (y == py && x == px) {
                        textPane.append("p", Color.red);
                        textPane.append("x", Color.black);
                    } else {
                        //room.objects[x][y][z].applyDepth(pz, px, py)
                        try {
                            int pz2 = pz;
                            while (!room.objects[x][y][pz2].canSee && !fishHere) {
                                pz2--;
                                for (int f = 0; f < fish.size(); f++) {
                                    if (fish.get(f).x == x && fish.get(f).y == y && fish.get(f).z == pz2) {
                                        fish.get(f).updateShadeBlood(light / 100.0, player.x, player.y, player.z);
                                        fish.get(f).updateShadeBlood2(0.9, x, y, room.waterline);
                                        textPane.append(fish.get(f).symbol, fish.get(f).shade);

                                        fishHere = true;
                                    }
                                }
                            }
                            if (!fishHere) {
                                room.objects[x][y][pz2].updateShadeBlood(light / 100.0, player.x, player.y, player.z);
                                room.objects[x][y][pz2].updateShadeBlood2(0.9, x, y, room.waterline);
                                textPane.append(room.objects[x][y][pz2].symbol, room.objects[x][y][pz2].shade);
                            }
                        } catch (Exception e) {
                            textPane.append("x", Color.black);
                        }
                        textPane.append("x", Color.black);
                    }
                }
            }
            textPane.append("\n", Color.white);
        }
        textPane.append("X: " + player.x +" Y: " + player.y + " Z: " + (player.z) + "  Oxygen: " + (int) player.oxygen + "%" + " Time: " + (int) time + " Light: " + light, Color.green);
        textPane.setVisible(true);
        textPane2.setVisible(false);
        bloodtimer++;
        if (bloodtimer > bloodlimit) {
            bloodtimer = 0;
            takeDamage = false;
        }
    }
    public static void checkAir(Room room) {
        try {
            if (room.objects[player.x][player.y][player.z].airHere) {
                player.oxygen = player.oxygenmax;
            }
        } catch (Exception e ) {

        }
    }



}
