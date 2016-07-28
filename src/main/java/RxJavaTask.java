import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static sun.misc.PostVMInitHook.run;

/**
 * Created by fox on 27/07/2016.
 */
public class RxJavaTask {

    static int currentDownloadFile = 0;
    static String fileNamesDownload[] = {"File_1.pdf" , "File_2.pdf" , "File_3.pdf" , "File_4.pdf"};

    public static void main(String []args) throws IOException {
        Observable<String> task = URLsFileTODownload("URLS.txt");
        task.subscribe(URL -> {
            try {
                downloadFileFromURL(URL, fileNamesDownload[currentDownloadFile++]);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private static void downloadFileFromURL(String URL , String savedFileName) throws IOException {
        Thread thread = new Thread(){
            public void run(){
                try {
                    new app(URL,savedFileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("Done! For : " + savedFileName);
                }
            }
        };
        thread.start();
    }

    private  static Observable<String> URLsFileTODownload(String fileName){

        return Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber) {
                BufferedInputStream URLsFile = null;
                try {
                    URLsFile = new BufferedInputStream(new FileInputStream(fileName));
                    String URL = "";
                    while(URLsFile.available() > 0){
                        char nextChar = (char)URLsFile.read();
                        URL += nextChar;
                        if(nextChar == '\n'){
                            subscriber.onNext(URL);
                            URL = "";
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        });
    }
}
