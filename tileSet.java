public class tileSet {
    String tileType = "";
    tileSet(String tileType){
        this.tileType = tileType;
    }
    //TODO add
    //canMove |
    public boolean canMove(){
        if(tileType.equals("water") || tileType.equals("air")){
            return true;
        }else{
            return false;
        }
    }
}
