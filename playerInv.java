import java.util.ArrayList;
import java.util.Random;

public class playerInv {
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
    for(int i = 0; i < items.size(); i++){
        ret = ret + '\n' + items.get(i) + ": "+ count.get(i);
    }
    return ret;
}

}
