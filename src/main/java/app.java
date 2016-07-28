import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by fox on 27/07/2016.
 */
public class app {


    private JPanel myJPanl;
    private JLabel URL;
    private JProgressBar bar;
    private JFrame frame;

    public app(String URL , String fileSaveName){
        onCreat(URL , fileSaveName);
        try {
            URL_Dowload(URL , fileSaveName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void URL_Dowload(String URL , String fileSaveName) throws IOException {
        BufferedInputStream input = null;
        FileOutputStream output = null;
        URL url;
        URLConnection urlConnection;
        try{
            url = new URL(URL);
            input = new BufferedInputStream(url.openStream());
            output = new FileOutputStream(fileSaveName);
            urlConnection = url.openConnection();
            urlConnection.connect();
            int file_size = urlConnection.getContentLength();
            int counert = 0;
            byte data[] = new byte[1024];
            int count;
            while((count = input.read(data,0,1024)) != -1) {
                output.write(data, 0, count);
                counert += count;
                bar.setString((counert*100/file_size)+"%");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(input != null){
                input.close();
            }
            if(output != null){
                output.close();
            }
            frame.dispose();
        }
    }

    private void onCreat(){
        frame = new JFrame("asdasd");
        frame.setContentPane(myJPanl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        URL.setText("Download File: //http:www.google.com");
        return;
    }

    private void onCreat(String url , String fileName) {
        frame = new JFrame(fileName);
        frame.setContentPane(myJPanl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        URL.setText("Download File: " + url);
        return;
    }
}
