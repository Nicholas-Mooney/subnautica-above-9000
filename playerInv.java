import java.util.ArrayList;
import java.util.Random;

public class playerInv {
    public int selectNum = 0;
public int numItems = 0;
public ArrayList<String> items = new ArrayList<String>();
public ArrayList<Integer> count = new ArrayList<Integer>();

public void addItem(String item){
    numItems++;
    if(!items.contains(item)) {
        items.add(item);
        count.add(1);
    }else{
        int curr = count.get(items.indexOf(item));
        count.set(items.indexOf(item), curr + 1);
    }
}
public void deleteItem(String item) {
    numItems--;
    if (count.get(items.indexOf(item)) == 1){
        count.remove(items.indexOf(item));
        items.remove(item);
    }else{
        int curr = count.get(items.indexOf(item));
        count.set(items.indexOf(item), curr - 1);
    }
}
public boolean hasItem(String item){
        return (items.contains(item));
    }

public void resetItems(){
    while(numItems > 0){
        items.remove(0);
        numItems--;
    }
}
public void die(){
    Random rand = new Random();
    for(int i = numItems/2; i > 0; i--){
        items.remove(rand.nextInt(numItems));
    }
}

//for eating
public int getHealthFrom(){
    if(numItems == 0){
        return 0;
    }else if(items.get(selectNum % numItems).equals("fish")) {
        return 3;
    }else{
        return 0;
    }
}
public int getFoodFrom(){
    if(numItems == 0){
        return 0;
    }else if(items.get(selectNum % numItems).equals("fish")) {
        return 3;
    }else{
        return 0;
    }
}
public void removeSelected(){
    deleteItem(items.get(selectNum % numItems));
}
public boolean isEatable(){
    if(numItems == 0){
        return false;
    }else if(items.get(selectNum % numItems).equals("fish")) {
        return true;
    }else{
        return false;
    }
}
public String retString(){
    String ret = "";
    for(int i = 0; i < items.size(); i++){
        if(i == (selectNum % numItems)){
            ret = ret + '\n'+ " > " + items.get(i) + ": " + count.get(i) + " < ";
        }else{
            ret = ret + '\n' + items.get(i) + ": " + count.get(i);
        }
    }
    return ret;
}
public void invSelector(String input){
    if(input == "upArrow"){
        selectNum--;
    }else if(input == "downArrow"){
        selectNum++;
    }
    if(numItems == 0){
        selectNum = 0;
    }
    if(selectNum >= numItems){
        selectNum = 0;
    }
    if(selectNum < 0){
        selectNum = numItems -1;
    }
}

}
