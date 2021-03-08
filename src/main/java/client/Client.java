package main.java.client;

import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;
import java.util.zip.InflaterInputStream;

public class Client
{
    public static final int port = 8000;
    public static final String host = "localhost";

    public static void main(String[] args)
    {
        try {
            Socket socket = null;
            InputStream is = null;
            socket = new Socket(host, port);
            is = socket.getInputStream();
            File f = new File("geted");
            int count;
            byte[] ab = new byte[8192];
            if (is != null) {
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                fos = new FileOutputStream(f);
                bos = new BufferedOutputStream(fos);
                while ((count = is.read(ab)) > 0) {
                    bos.write(ab, 0, count);
                    bos.flush();
                }
                bos.close();
            }
            socket.close();
            File ds = new File("sound.wav");
            decompressFile(f,ds);
            playSound(ds.getPath());

        }catch (Exception e){ System.out.println("3");}
    }

    public static synchronized void playSound(final String sound) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File f = new File(sound);
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f.toURI().toURL());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    while (clip.isOpen())
                    {
                        try {
                            Thread.sleep(2000);
                        }catch (InterruptedException ie){}
                        if(!clip.isActive()) break;
                    }
                } catch (Exception e) {}
            }
        }).start();
    }


    public static void decompressFile(File compressed, File raw) throws IOException
    {
        InputStream in = new InflaterInputStream(new FileInputStream(compressed));
        OutputStream out = new FileOutputStream(raw);
        shoveInToOut(in, out);
        in.close();
        out.close();
    }
    private static void shoveInToOut(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[8192];
        int len;
        while ((len = in.read(buffer)) > 0)
        {
            out.write(buffer, 0, len);
        }
    }
}
