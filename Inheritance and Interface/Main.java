package offline;
import java.text.SimpleDateFormat;
import java.util.Date;

class ShopItem{
    private String name="" ;
    private double sellingPricePerUnit=0;
    private double buyingPricePerUnit=0;
    int temp=0;

    public void setName(int identifier){
        if(identifier==1){
            name = "Green Apple" ;
        }
        if(identifier==2){
            name = "Red Apple" ;
        }
        if(identifier==3){
            name = "Orange" ;
        }
        if(identifier==4){
            name = "Canned Strawberries" ;
        }
        if(identifier==5){
            name = "Packed Strawberries" ;
        }
    }
    public void setSel(int identifier){
        if(identifier==1){
            sellingPricePerUnit = 5 ;
        }
        if(identifier==2){
            sellingPricePerUnit = 5 ;
        }
        if(identifier==3){
            sellingPricePerUnit = 6 ;
        }
        if(identifier==4){
            sellingPricePerUnit = 10 ;
        }
        if(identifier==5){
            sellingPricePerUnit = 8 ;
        }
    }
    public void setBuy(int identifier){
        if(identifier==1){
            buyingPricePerUnit = 3 ;
        }
        if(identifier==2){
            buyingPricePerUnit = 3 ;
        }
        if(identifier==3){
            buyingPricePerUnit = 3 ;
        }
        if(identifier==4){
            buyingPricePerUnit = 8 ;
        }
        if(identifier==5){
            buyingPricePerUnit = 5 ;
        }
    }

    public void setTemp(int identifier){
        temp = identifier ;
    }
    public String getName(){
        return name ;
    }

    public double getSellingPricePerUnit(){
        return sellingPricePerUnit ;
    }


    public double getBuyingPricePerUnit(){
        return buyingPricePerUnit ;
    }

    public int getTemp(){
        return temp ;
    }

    public String toString(){
        return name + " " + buyingPricePerUnit + " " + sellingPricePerUnit +"\n";
    }
}
interface Shop{
    void buy(int type,int amount);
    void sell(int type, int amount);
    LogEntry[] getLog();
    double getBalance() ;
    ShopItem[] getInventory() ;
}
class FruitShop implements Shop{
    private int buycounter ;
    private int highest ;
    private int current ;
    private int logcounter;
    private double balance ;
    private LogEntry logEntry[] = new LogEntry[10] ;
    private ShopItem shopItem[] = new ShopItem[100] ;
    FruitShop(int x, double y){
        int i = 0 ;
        for(;i<100;i++){
            shopItem[i] = new ShopItem() ;
        }

        for(i=0;i<10;i++){
            logEntry[i] = new LogEntry() ;
        }
        highest = x;
        balance = y ;
        buycounter = 0 ;
        current = 0 ;
        logcounter = 0 ;
    }

    @Override
    public void buy(int type, int amount) {
        ShopItem tem = new ShopItem();
        tem.setName(type);
        tem.setBuy(type);
        if(amount*tem.getBuyingPricePerUnit()>balance){
            System.out.println("Not enough balance");
        }

        else {
            if (amount <= highest - current) {
                int i = 0 ;
                while (i<amount) {
                    shopItem[i+buycounter].setName(type);
                    shopItem[i+buycounter].setSel(type);
                    shopItem[i+buycounter].setBuy(type);
                    shopItem[i+buycounter].setTemp(type);
                    i++;
                }
                buycounter += amount ;
                balance -= amount * tem.getBuyingPricePerUnit();
                current += amount;
                if(amount>0){
                    logEntry[logcounter].setItemName(tem.getName());
                    logEntry[logcounter].setAmountUnit(amount);
                    logEntry[logcounter].bs("Bought");
                    logcounter++;
                }
            }
            else {
                System.out.println("Not enough space in inventory.");
            }
        }
    }

    @Override
    public void sell(int type, int amount) {
        ShopItem tem = new ShopItem() ;
        ShopItem shopItem1[] = new ShopItem[buycounter] ;
        int j = 0 ;
        int i = 0 ;
        int ide = 0 ;
        int ide2 = 0 ;
        while (i<buycounter){
            shopItem1[i] = new ShopItem() ;
            i++ ;
        }
        tem.setName(type);
        tem.setTemp(type);
        tem.setSel(type);
        boolean isEnough = false ;
        i = 0 ;
        int count=0 ;
        while (i<buycounter){
            if (shopItem[i].getTemp()==tem.getTemp()){
                count++ ;
                if(count==amount){
                    isEnough = true ;
                    break ;
                }
            }
            i++ ;
        }

        if(isEnough==true){
            i= 0 ;
            while (i<buycounter){
                if(shopItem[i].getTemp()==type){
                    ide = i ;
                    ide2 = ide+amount-1 ;
                    break ;
                }
                i++ ;
            }
            if(ide==0){
                j=0 ;
                i =  ide2 ;
                while (i<buycounter-1){
                    shopItem1[j++] = shopItem[i] ;
                    i++ ;
                }
            }
            else{
                i = 0 ;
                while (i<ide){
                    shopItem1[j++] = shopItem[i] ;
                    i++ ;
                }
                i =  ide2 ;
                while (i<buycounter-1){
                    shopItem1[j++] = shopItem[i] ;
                    i++ ;
                }
            }
            System.out.println(ide+" "+ide2);
            buycounter = j ;
            current = j;
            balance += amount*tem.getSellingPricePerUnit() ;
            logEntry[logcounter].setItemName(tem.getName());
            logEntry[logcounter].setAmountUnit(amount);
            logEntry[logcounter].bs("sold");
            logcounter++;
        }
        else{
            System.out.printf("Not Enough Amount");
        }
    }

    @Override
    public LogEntry[] getLog() {
        LogEntry result[] = new LogEntry[logcounter] ;
        int i = 0 ;
        while (i<logcounter){
            result[i] = logEntry[i] ;
            i++ ;
        }
        return result ;
    }
    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public ShopItem[] getInventory() {
        ShopItem result[] = new ShopItem[buycounter];
        int i = 0 ;
        while (i<buycounter){
            result[i] = shopItem[i] ;
            i++ ;
        }
        return result ;
    }
}
class LogEntry{
    String timestamp = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy").format(new Date()) ;
    private String itemName ;
    private int amountUnit;
    private String borS ;

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setAmountUnit(int amountUnit) {
        this.amountUnit = amountUnit;
    }

    public void bs(String a){
        borS = a ;
    }


    public String toString(){
        return timestamp+" "+itemName+" "+amountUnit+" "+ borS +"\n" ;
    }

}
public class Main {

    public static void main(String[] args) {
        FruitShop fruitShop= new FruitShop(20, 60);
        System.out.println(fruitShop.getBalance());
        fruitShop.buy(1,4);
        fruitShop.buy(4,2);
        fruitShop.buy(2,4);
        fruitShop.sell(2,4);
        System.out.println(fruitShop.getBalance());
        for(ShopItem shopItem: fruitShop.getInventory()){
            System.out.println(shopItem.toString());
        }

        for(LogEntry logEntry: fruitShop.getLog()){
            System.out.println(logEntry.toString());
        }
    }
}
