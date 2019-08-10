package Networking;

/**
 * Created by Spondon on 28/11/2016.
 */
import FileWriterReader.CustomFileReader;
import FileWriterReader.CustomFileWriter;

import java.io.*;
import java.net.Socket;


class clientThread extends Thread {
    private String clientName = null;
    private ObjectInputStream in = null ;
    private ObjectOutputStream out = null ;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;
    private static int counter = 0 ;
    public clientThread(Socket clientSocket, clientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }
    public void run(){
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream()) ;
            out = new ObjectOutputStream(clientSocket.getOutputStream()) ;

            while (true) {
                Object o = in.readObject() ;
                String[] words = (String[]) o;
                clientName = words[0] ;
                try {
                    CustomFileReader customFileReader = new CustomFileReader(new File("src/Networking/Online.txt")) ;
                    if(!customFileReader.isPresent(clientName)){
                        CustomFileWriter customFileWriter = new CustomFileWriter(new File("src/Networking/Online.txt")) ;
                        customFileWriter.write(clientName + "\n");
                        customFileWriter.close();
                    }
                    customFileReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (words[1].equals("Terminate")) {
                    break;
                }
                if(words[1].equals("////"))
                {
                    System.out.println(clientName);
                    continue;
                }
                if(words[1].endsWith("inchatBox")){
                    String[] privateMsg = words[1].split("-" , 2) ;

                    synchronized (this) {
                        for (int i = 0; i < maxClientsCount; i++) {
                            if (threads[i] != null && threads[i].clientName != null && !threads[i].clientName.equals(this.clientName) && threads[i].clientName.equals(privateMsg[0].substring(1))) {
                                String sendPrivateMessage = "In chat Box" ;
                                String newwords[] = new String[2] ;
                                newwords[0] = clientName + " :: " ;
                                newwords[1] = sendPrivateMessage ;
                                threads[i].out.writeObject(newwords);
                            }
                        }
                    }
                }
                if(words[1].endsWith("%#unreceived")){
                    String[] privateMsg = words[1].split("-" , 2) ;
                    String target = new String();
                    try {
                        CustomFileReader customFileReader = new CustomFileReader(new File("src/Networking/Unreceived/"+clientName + privateMsg[0].substring(1) + ".txt")) ;
                        target = customFileReader.copy() ;
                        customFileReader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    synchronized (this){
                        for (int i = 0; i < maxClientsCount; i++) {
                            if (threads[i] != null && threads[i].clientName != null && threads[i].clientName.equals(this.clientName)) {
                                String sendPrivateMessage = target + "Unreceived" ;
                                String newwords[] = new String[2] ;
                                newwords[0] = clientName + " :: " ;
                                newwords[1] = sendPrivateMessage ;
                                threads[i].out.writeObject(newwords);
                            }
                        }

                        FileWriter writer = new FileWriter(new File("src/Networking/Unreceived/"+clientName + privateMsg[0].substring(1) + ".txt")) ;
                        writer.write("");
                        writer.flush();
                        writer.close();
                    }
                }
                if(words[1].startsWith("@"))
                {
                    String[] privateMsg = words[1].split("-" , 2) ;
                    boolean flag = false;
                    synchronized (this) {
                        for (int i = 0; i < maxClientsCount; i++) {
                            if (threads[i] != null && threads[i].clientName != null && !threads[i].clientName.equals(this.clientName) && threads[i].clientName.equals(privateMsg[0].substring(1))) {
                                String sendPrivateMessage = privateMsg[1] ;
                                String newwords[] = new String[2] ;
                                newwords[0] = clientName + " :: " ;
                                newwords[1] = sendPrivateMessage ;
                                threads[i].out.writeObject(newwords);
                                flag = true ;
                            }
                        }

                        if(flag==false){
                            try {
                                CustomFileWriter customFileWriter = new CustomFileWriter(new File("src/Networking/Unreceived/"+privateMsg[0].substring(1) + clientName + ".txt"));
                                String sendPrivateMessage = privateMsg[1] ;
                                String newwords[] = new String[2] ;
                                newwords[0] = clientName + " :: " ;
                                newwords[1] = sendPrivateMessage ;
                                if(!words[1].equals("inchatBox")){
                                    customFileWriter.write(newwords[0] + newwords[1] + "\n");
                                }
                                customFileWriter.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try{
                                CustomFileWriter customFileWriter1 = new CustomFileWriter(new File("src/Networking/Unreceived/"+privateMsg[0].substring(1) + "Notification"+ ".txt"));
                                customFileWriter1.write(privateMsg[0].substring(1) + "-" + "You have message from " + clientName + " - unread" + "\n");
                                customFileWriter1.close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }

                else {
                    synchronized (this) {
                        for (int i = 0; i < maxClientsCount; i++) {
                            if (threads[i] != null && threads[i].clientName != null && !threads[i].clientName.equals(this.clientName) && threads[i].clientName.endsWith("Group")) {
                                threads[i].out.writeObject(o);
                            }

                            else{
                                continue;
                            }
                        } }
                }
            }
            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == this) {
                        threads[i] = null;
                    } }
            }
        }
        catch (IOException e) {
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
