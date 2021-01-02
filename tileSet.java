public class tileSet {
    String tileType = "";
    tileSet(String tileType){
        this.tileType = tileType;
    }
    //TODO add
    //canMove |
    public boolean canMove(){
        if(tileType.equals("water")
                || tileType.equals("air")
                || tileType.equals("brain")
                || tileType.equals("kelp")
                || tileType.equals("mushroom")
                || tileType.equals("mushroom2")
                || tileType.equals("mushroom3")
                || tileType.equals("fruit")){
            return true;
        }else{
            return false;
        }
    }
    public boolean waterBased(){
        if(tileType.equals("water")
                || tileType.equals("brain")
                || tileType.equals("kelp")
                || tileType.equals("mushroom")
                || tileType.equals("mushroom2")
                || tileType.equals("mushroom3")
                || tileType.equals("fruit")){
            return true;
        }else{
            return false;
        }
    }
    public boolean airBased(){
        if(tileType.equals("air")
                || tileType.equals("cloud")){
            return true;
        }else{
            return false;
        }
    }
}
