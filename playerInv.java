import java.util.ArrayList;
import java.util.Random;

public class playerInv {
public int numItems = 0;
public ArrayList<String> items = new ArrayList<String>();

public void addItem(String item){
    numItems++;
    items.add(item);
}
public void deleteItem(String item){
    numItems--;
    items.remove(item);
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

public String retString(){
    String ret = "";
    for(int i = 0; i < numItems; i++){
        ret = ret + '\n' + i + ": " + items.get(i);
    }
    return ret;
}

}
