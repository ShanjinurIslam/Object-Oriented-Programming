//
//  main.cpp
//  Offline-1
//
//  Created by Shanjinur Islam on 26/09/2016.
//  Copyright © 2016 Shanjinur Islam. All rights reserved.
//

#include <iostream>
using namespace std ;
class String{
    char *s ;
    int len ;
public:
    String(){
        len = 0 ;
        s = new char[len] ;
    }
    String(char *p){
        len = strlen(p) ;
        s = new char[len] ;
        strcpy(s, p) ;
    }
    
    void setString(char p[]){
        s = new char[strlen(p)] ;
        strcpy(s,p) ;
    }
    
    String(const String &ob){
        len = strlen(ob.s) ;
        s = new char[len] ;
        strcpy(s,ob.s) ;
    }
    
    char* getString(){
        return s ;
    }
    
    char &operator[](int i){
        return s[i] ;
    }
    
    String operator ++(int notused){
        String ob(s) ;
        int i = 0 ;
        while(i<len){
            s[i] = s[i]+1 ;
            i++ ;
        }
        return ob ;
    }
    
    int getLen(){
        return len ;
    }
    
};

void bubbleSort(String a[],int n){
    int x,d,j= 0  ;
    char b[100] , c[100] ;
    String temp ;
    for (x = 0 ; x < ( n - 1 ); x++)
    {
        for (d = 0 ; d < n - x - 1; d++)
        {
            strcpy(b,a[d].getString());
            strcpy(c,a[d+1].getString()) ;
            String temp ;
            if(b[j]>c[j]){
                temp.setString(a[d].getString()) ;
                a[d].setString(a[d+1].getString()) ;
                a[d+1].setString(temp.getString()) ;
            }
        }
    }
    return ;
}

int binarySearch(String a[], int n , String b){
    int s= 0 ;
    int e = n - 1;
    int i = 0 ;
    int m = (s+e)/2 ;
    while(s<=e){
        char c[100] ;
        char d[100] ;
        strcpy(c,a[m].getString()) ;
        strcpy(d,a[s].getString()) ;
        
        if(c[i]>d[i]){
            e = m ;
        }
        
        if(c[i]<d[i]){
            s= m+1 ;
        }
        
        if(c[i]==d[i]){
            return 1 ;
        }
        m = (s+e)/2 ;
    }
    
    return 0 ;
}

String replaceAll(String ob ,char t, char s){
    int i = 0 ;
    while(i<ob.getLen()){
        if(ob[i]==t){
            ob[i] = s ;
        }
        i++ ;
    }
    
    return ob ;
}

int main(){
    char p[1000];
    String s("BUET");
    String strArray[20];
    for(int i=0;i<10;i++){
        cin>>p;
        strArray[i].setString(p);
    }
    bubbleSort(strArray,10);
    for(int i=0;i<10;i++){
        cout<<strArray[i].getString()<<endl;
    }
    cout<<binarySearch(strArray,10,s)<<endl;
    s = replaceAll(s,'T','L'); // s should now contain "BUEL"
    cout<< (s++).getString()<<endl;
    //s should contain "CVFM" , but in the console the output should be "BUEL"
    cout<<s.getString()<<endl; // the output should be "CVFM" }
}