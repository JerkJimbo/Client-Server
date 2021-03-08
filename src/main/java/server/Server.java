package main.java.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.*;

public class Server {
    public static final int port = 8000;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            BufferedOutputStream otc = new BufferedOutputStream(socket.getOutputStream());
            Thread t = new ClientHandler(socket, otc);
            t.start();
        }


    }






    static class ClientHandler extends Thread {
        final Socket s;
        final BufferedOutputStream otc;

        public ClientHandler(Socket s, BufferedOutputStream otc){
            this.s = s;
            this.otc = otc;
        }

        public void run()
        {
            try{
            byte[] ab = new byte[8192];
            File sound = new File("./src/main/java/resources/tosend.wav");
            File zip = new File("soundZ");
            compressFile(sound, zip);
            FileInputStream in = new FileInputStream(zip);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            OutputStream out = s.getOutputStream();
            try{
                int count;
                while((count = in.read(ab)) > 0)
                {
                    out.write(ab, 0, count);
                }
                out.flush();
                out.close();
                System.out.println("Send");
            }catch (Exception e){}

            }catch (Exception e){}
        }

        public static void compressFile(File raw, File compressed) throws IOException {
            InputStream in = new FileInputStream(raw);
            OutputStream out = new DeflaterOutputStream(new FileOutputStream(compressed));
            shoveInToOut(in, out);
            in.close();
            out.close();
        }
        private static void shoveInToOut(InputStream in, OutputStream out) throws IOException {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        }
    }
}