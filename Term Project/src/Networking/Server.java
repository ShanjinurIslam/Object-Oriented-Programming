package Networking;

import sample.Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Spondon on 28/11/2016.
 */
public class Server {
    private ServerSocket serverSocket ;
    private ObjectOutputStream out ;
    private ObjectInputStream in ;
    private Socket socket ;
    private String name = null ;
    private static final int maxClientsCount = 50;
    private static int i = 0 ;
    private static final clientThread[] threads = new clientThread[maxClientsCount];
    public Server() throws Exception {
        serverSocket = new ServerSocket(15555);
        synchronized (this) {
            while (true) {
                socket = serverSocket.accept(); //
                int i = 0;
                for (i = 0; i < maxClientsCount; i++) {
                        if (threads[i] == null) {
                            (threads[i] = new clientThread(socket, threads)).start(); //
                            break;
                        }
                }
                    if (i == maxClientsCount) {
                        System.out.println("Server too busy");
                    }
                }
            }
        }


        public static void main(String args[]) throws  Exception{
            FileWriter writer = new FileWriter(new File("src/Networking/Online.txt")) ;
            writer.write("");
            writer.flush();
            writer.close();
            new Server() ;
        }
    }

