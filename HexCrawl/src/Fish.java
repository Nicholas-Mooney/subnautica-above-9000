import java.awt.*;
import java.util.zip.CheckedInputStream;

public class Fish extends Object {
    protected int heighlimit = 16;

    public Fish(int x, int y, int z, Color color, String symbol, String name, int heightlimit) {
        this.x = x;
        this.z = z;
        this.y = y;
        this.name = name;
        this.color = color;
        this.canCollide = true;
        this.airHere = false;
        this.canSee = true;
        this.symbol = symbol;
        this.heighlimit = heightlimit;
    }

    public void moveRandom() {

        int move = Room.rand.nextInt(30);
        switch (move) {
            case (0) -> x++;
            case (1) -> y++;
            case (2) -> z++;
            case (3) -> x--;
            case (4) -> y--;
            case (5) -> z--;
        }
        try {
            if (Game.room.objects[x][y][z].canCollide || x <= 0 || y <= 0 || x >= Game.room.xmax || y >= Game.room.ymax) {
                switch (move) {
                    case (0) -> x--;
                    case (1) -> y--;
                    case (2) -> z--;
                    case (3) -> x++;
                    case (4) -> y++;
                    case (5) -> z++;
                }
            }
        } catch (Exception e) {
            switch (move) {
                case (0) -> x--;
                case (1) -> y--;
                case (2) -> z--;
                case (3) -> x++;
                case (4) -> y++;
                case (5) -> z++;
            }
        }
        while (z > heighlimit) {
            z--;
        }

    }
}

