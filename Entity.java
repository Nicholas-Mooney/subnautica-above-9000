import java.awt.*;

public class Entity {
    String chara = ".";
    String type = "default";
    int x = 10;
    int y = 10;
    Color color = Color.white;

    boolean flagForRemoval = false;
    int startingDivisible = 0;
    boolean hasStartingDivisible = false;

    public Entity (String chara, String type, int x, int y, Color color) {
        this.chara = chara;
        this.type = type;
        this.x = x;
        this.y = y;
        this.color = color;
    }
    public Entity() {

    }

    public void getMovement() {

    }

}
