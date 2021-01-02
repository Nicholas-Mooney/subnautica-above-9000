import com.sun.tools.javac.Main;

public class FishAI {
    int x;
    int y;
    int xt;
    int yt; //target
    int xa;
    int ya; //avoid

    boolean target;
    boolean avoid;

    public FishAI(int x, int y, boolean target, int xt, int yt, boolean avoid, int xa, int ya) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.xt = xt;
        this.yt = yt;
        this.avoid = avoid;
        this.xa = xa;
        this.ya = ya;
    }

    public int getMoveY() {
        int move = 0;
        if (!target && !avoid) {
            return MainLoop.rand.nextInt(3) - 1;
        } else if (avoid){
            if (ya > y) {
                if (MainLoop.rand.nextInt(4) == 0) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (ya < y){
                if (MainLoop.rand.nextInt(4) == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }else {
                return MainLoop.rand.nextInt(3) - 1;
            }
        } else {
            if (yt > y) {
                if (MainLoop.rand.nextInt(4) == 0) {
                    return 0;
                } else {
                    return 1;
                }
            } else if (yt < y) {
                if (MainLoop.rand.nextInt(4) == 0) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                return MainLoop.rand.nextInt(3) - 1;
            }
        }

    }
    public int getMoveX() {
        int move = 0;
        if (!target && !avoid) {
            return MainLoop.rand.nextInt(3) - 1;
        } else if (avoid){
            if (xa > x) {
                return -1;
            } else if (xa < x){
                return 1;
            } else {
                return MainLoop.rand.nextInt(3) - 1;
            }
        } else {
            if (xt > x) {
                return 1;
            } else if (xt < x) {
                return -1;
            } else {
                return MainLoop.rand.nextInt(3) - 1;
            }
        }

    }





}
