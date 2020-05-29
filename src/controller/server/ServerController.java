package controller.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerController {
    private ArrayList klientArrayList; // lista klientów
    private PrintWriter printWriter; // zapis tekstu

    //uruchomienie programu
    public static void main(String[] argsv) {
        ServerController server = new ServerController();
        server.startSerwer();
    }

    //uruchomienie serwera
    private void startSerwer() { // nasłuchiwanie i odbieranie komunikatu od klientów
        klientArrayList = new ArrayList();

        try {
            System.out.println("Serwer pracuje...");
            ServerSocket serverSocket = new ServerSocket(4500); //uruchomienie usługi nasłuchiwania na porcie 4500

            while (true) {  //pętla nieskończona - serwer działa cały czas
                Socket socket = serverSocket.accept(); //akceptowanie wszystkich połączeń przychodzacych na port
                System.out.println("Słucham: " + serverSocket);
                printWriter = new PrintWriter(socket.getOutputStream()); // wysyłanie komunikatów do klientów
                klientArrayList.add(printWriter); //dodanie klienta

                //uruchomienie wątku, dla każdego klienta który dołączy na serwer uruchamiany jest osobny wątek
                Thread t = new Thread(new SerwerKlient(socket));
                t.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //klasa wewnętrzna - wątek rozsyłający wiadomości do klientów
    class SerwerKlient implements Runnable {

        // zmienne globalne
        Socket socket; // przechwycanie danych które przychodza do konstruktora
        BufferedReader bufferedReader; // odczytywanie danych od klientów

        //konstruktor
        SerwerKlient(Socket socketKlient) { //odczyt wejścia
            try {
                System.out.println("Połączony.");
                socket = socketKlient;
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // odczyt tego co wchodzi na strumień na wejściu
                //jeżeli przyjdzie komunikat od klienta to trafi na wejście(InputStreamReader) a następnie BufferedReader odczyta to co wejdzie do strumienia wejściowego
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void run() {
            String str;
            PrintWriter pw = null;

            try {
                while ((str = bufferedReader.readLine()) != null) {  //dopóki string z bufferReadera nie jest pusty
                    System.out.println("Odebrano >> " + str);

                    //dopóki znajduje się następny klient na liście to rozsyłamy mu tekst wiadomości
                    Iterator it = klientArrayList.iterator(); //iterowanie po liście klientów
                    while (it.hasNext()) { //rozsyłanie do wszystkich klientów tekst z str'a jeśli iterator znajdzie na liście kolejnego klienta
                        pw = (PrintWriter) it.next(); // rzutowanie w sposób jawny ( castowanie na printWritera )
                        pw.println(str); // Wkładanie do printWritera tekstu z str
                        pw.flush(); //opróżnianie strumienia
                    }
                }

            } catch (Exception e) {

            }
        }
    }

    public ArrayList getKlientArrayList() {
        return klientArrayList;
    }

    public void setKlientArrayList(ArrayList klientArrayList) {
        this.klientArrayList = klientArrayList;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }
}
