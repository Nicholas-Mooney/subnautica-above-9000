import com.sun.tools.javac.Main;

import java.awt.*;

public class Entity {
    String chara = ".";
    String type = "default";
    int x = 10;
    int y = 10;
    Color color = Color.white;
    int lightPower = 0;
    int speed = 5; //(low = fast)
    boolean flagForRemoval = false;
    int startingDivisible = 0;
    boolean hasStartingDivisible = false;
    int ID = 0;

    FishAI AI = new FishAI(x, y, false, 0, 0, false, 0, 0);

    public Entity (String chara, String type, int x, int y, Color color,int lightPower) {


        this.ID = MainLoop.rand.nextInt(10000000);
        this.chara = chara;
        this.type = type;
        this.x = x;
        this.y = y;
        this.color = color;
        this.lightPower = lightPower;
    }
    public Entity() {

    }

    public void getMovement() {

    }

}
