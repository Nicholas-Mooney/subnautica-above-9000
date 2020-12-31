import java.awt.*;

public class Object {
    protected int x;
    protected int z;
    protected int y;
    protected Color color;
    protected Color shade;
    protected boolean canCollide;
    protected boolean canSee;
    protected boolean airHere;
    protected String symbol;
    protected boolean hasItem;
    protected Item item;
    protected String name;

    public Object(int x, int y, int z, Color color, boolean canCollide, String symbol, boolean canSee, boolean airHere, String name) {
        this.x = x;
        this.z = z;
        this.y =    y;
        this.name = name;
        this.color = color;
        this.canCollide = canCollide;
        this.airHere = airHere;
        this.canSee = canSee;
        this.symbol = symbol;
        this.hasItem = false;
    }
    public Object(int x, int y, int z, Color color, boolean canCollide, String symbol, boolean canSee, boolean airHere,String name, Item item) {
        this.x = x;
        this.z = z;
        this.y = y;
        this.name = name;
        this.color = color;
        this.canCollide = canCollide;
        this.airHere = airHere;
        this.canSee = canSee;
        this.symbol = symbol;
        this.item = item;
        this.hasItem = true;
    }

    public Object() {

    }
    public void updateShadeBlood(double power, int x, int y, int z) {
        Color base = new Color(30, 0, 0);
        int diffx = x - this.x;
        if (diffx < 0) {
            diffx = diffx * -1;
        }
        diffx = diffx - 1;

        int diffy = y - this.y;
        if (diffy < 0) {
            diffy = diffy * -1;
        }
        diffy = diffy - 1;

        int diffz = (z - this.z) * 2;
        if (diffz < 0) {
            diffz = diffz * -1;
        }
        diffz = diffz - 1;

        double difflat = Math.sqrt((diffx * diffx) + (diffy * diffy) + (diffz * diffz));
        int difflati = (int) Math.round(difflat);
        int newred = (int) ((double) base.getRed() + Math.pow(power,difflati)*250 - 15);
        int newgreen = (int) ((double) base.getGreen() + Math.pow(power,difflati)*this.color.getGreen() - 15);
        int newblue = (int) ((double) base.getBlue() + Math.pow(power,difflati)*this.color.getBlue());
        if (newred < 0) {
            newred = 1;
        }
        if (newblue < 0) {
            newblue = 1;
        }
        if (newgreen < 0) {
            newgreen = 1;
        }
        shade = new Color(newred, newgreen, newblue);
    }
    public void updateShadeBlood2(double power, int x, int y, int z) {
        Color base = shade;
        int diffx = x - this.x;
        if (diffx < 0) {
            diffx = diffx * -1;
        }
        diffx = diffx - 1;
        int diffz = (int) ((z - this.z) * 1.3);
        if (diffz < 0) {
            diffz = diffz * -1;
        }
        diffz = diffz - 1;

        double difflat = Math.sqrt(diffx * diffx + diffz * diffz);
        int difflati = (int) Math.round(difflat);
        int newred = (int) ((double) base.getRed() + Math.pow(power,difflati)*this.color.getRed());
        int newgreen = (int) ((double) base.getGreen() + Math.pow(power,difflati)*this.color.getGreen());
        int newblue = (int) ((double) base.getBlue() + Math.pow(power,difflati)*this.color.getBlue());
        if (newred < 0) {
            newred = 1;
        }
        if (newblue < 0) {
            newblue = 1;
        }
        if (newgreen < 0) {
            newgreen = 1;
        }
        if (newred > 255) {
            newred = 255;
        }
        if (newblue > 255) {
            newblue= 255;
        }
        if (newgreen > 255) {
            newgreen = 255;
        }
        shade = new Color(newred, newgreen, newblue);
    }
    public void updateShade(double power, int x, int y, int z) {
        Color base = new Color(0, 0, 0);
        int diffx = x - this.x;
        if (diffx < 0) {
            diffx = diffx * -1;
        }
        diffx = diffx - 1;

        int diffy = y - this.y;
        if (diffy < 0) {
            diffy = diffy * -1;
        }
        diffy = diffy - 1;

        int diffz = (z - this.z) * 2;
        if (diffz < 0) {
            diffz = diffz * -1;
        }
        diffz = diffz - 1;

        double difflat = Math.sqrt((diffx * diffx) + (diffy * diffy) + (diffz * diffz));
        int difflati = (int) Math.round(difflat);
        int newred = (int) ((double) base.getRed() + Math.pow(power,difflati)*this.color.getRed() - 15);
        int newgreen = (int) ((double) base.getGreen() + Math.pow(power,difflati)*this.color.getGreen() - 15);
        int newblue = (int) ((double) base.getBlue() + Math.pow(power,difflati)*this.color.getBlue());
        if (newred < 0) {
            newred = 1;
        }
        if (newblue < 0) {
            newblue = 1;
        }
        if (newgreen < 0) {
            newgreen = 1;
        }
        shade = new Color(newred, newgreen, newblue);
    }
    public void updateShade2(double power, int x, int y, int z) {
        Color base = shade;
        int diffx = x - this.x;
        if (diffx < 0) {
            diffx = diffx * -1;
        }
        diffx = diffx - 1;
        int diffz = (int) ((z - this.z) * 1.3);
        if (diffz < 0) {
            diffz = diffz * -1;
        }
        diffz = diffz - 1;

        double difflat = Math.sqrt(diffx * diffx + diffz * diffz);
        int difflati = (int) Math.round(difflat);
        int newred = (int) ((double) base.getRed() + Math.pow(power,difflati)*this.color.getRed());
        int newgreen = (int) ((double) base.getGreen() + Math.pow(power,difflati)*this.color.getGreen());
        int newblue = (int) ((double) base.getBlue() + Math.pow(power,difflati)*this.color.getBlue());
        if (newred < 0) {
            newred = 1;
        }
        if (newblue < 0) {
            newblue = 1;
        }
        if (newgreen < 0) {
            newgreen = 1;
        }
        if (newred > 255) {
            newred = 255;
        }
        if (newblue > 255) {
            newblue= 255;
        }
        if (newgreen > 255) {
            newgreen = 255;
        }
        shade = new Color(newred, newgreen, newblue);
    }
}
