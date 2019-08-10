package Online ;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
    double getBalance() ;
    ShopItem[] getInventory() ;
}
class FruitShop implements Shop{
    private int buycounter ;
    private int highest ;
    private int current ;
    private static double balance ;
    private static ShopItem shopItem[] = new ShopItem[100] ;
    FruitShop(int x, double y){
        int i = 0 ;
        for(;i<100;i++){
            shopItem[i] = new ShopItem() ;
        }
        highest = x;
        balance = y ;
        buycounter = 0 ;
        current = 0 ;
    }

    @Override
    synchronized public void buy(int type, int amount) {
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
            }
            else {
                System.out.println("Not enough space in inventory.");
            }
        }
    }

    @Override
    synchronized public void sell(int type, int amount) {
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
            buycounter = j ;
            balance += amount*tem.getSellingPricePerUnit() ;
        }
        else{
            System.out.printf("Not Enough Amount");
        }
    }

    @Override
    synchronized public double getBalance() {
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
class SalesmanThread extends Thread {
    private int x;
    public FruitShop fruitShop;
    private Scanner sc ;

    SalesmanThread(FruitShop fruitShop, int x) throws FileNotFoundException {
        this.x = x;
        this.fruitShop = fruitShop;
        sc = new Scanner(new File("src/input"+ String.valueOf(x) + ".txt"));
    }

    @Override
    synchronized public void run() {
        int n = sc.nextInt();
        int choice, type, amount;
        for (int i = 0; i < n; i++) {
            choice = sc.nextInt();
            if (choice == 1) {
                type = sc.nextInt();
                amount = sc.nextInt();
                fruitShop.buy(type,amount);
            } else if (choice == 2) {
                type = sc.nextInt();
                amount = sc.nextInt();
                fruitShop.sell(type,amount);
            } else if (choice == 3) {
                System.out.println("SalesMan " + this.x + "- " + fruitShop.getBalance());
            }
        }
    }
}
public class Main{
    public static void main(String agrs[]) throws FileNotFoundException {
        FruitShop fruitShop = new FruitShop(200,7000) ;
        SalesmanThread salesmanThread[] = new SalesmanThread[3] ;
        int i = 0 ;
        while (i<3){
            salesmanThread[i] = new SalesmanThread(fruitShop,i+1) ;
            try {
                synchronized (salesmanThread[i]){
                    salesmanThread[i].start();
                    Thread.sleep(1000);
                }
            }catch (Exception e) {
                System.out.println(e);
            }
            i++ ;
        }
    }
}