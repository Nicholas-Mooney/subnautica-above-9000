public class mapGrid {
    public static int maxX = 200;
    public static int maxY = 50;
    public static tileSet[][] map = new tileSet[maxX][maxY];

    mapGrid(int seed){
        String tileType;
        for(int y = 0; y < 50; y++){
            for(int x = 0; x < 200; x++){
                if(y<10){
                    tileType = "air";
                }else{
                    tileType = "water";
                }
                tileSet a = new tileSet(tileType);
                map[x][y] = a;
            }
        }
    }
}
