package john.net;

import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class DownLoader extends Thread{

    public interface OKListener{

        public void  complete();
        public void alreadyHave();

    }

    String url ="";
    String save_file = "";
    public String result = "";

    private final int SAVE_TO_SANDBOX = 0;
    private final int RETURN_STRING = 1;

    private int HOW = SAVE_TO_SANDBOX;

    public DownLoader(String url, String save_file){
        this.url = url;
        this.save_file = save_file;
        HOW = SAVE_TO_SANDBOX;
    }
    public DownLoader(String url){
        this.url = url;
        HOW = RETURN_STRING;
    }

    public OKListener noter;

    @Override
    public void run() {
        super.run();
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            byte[] small = new byte[1024];
            ArrayList<Byte> big = new ArrayList<Byte>();
            int how_much_this_time = in.read(small);
            while(how_much_this_time != -1){
                for(int i=0;i<how_much_this_time; i++){
                    big.add(small[i]);
                }
                how_much_this_time = in.read(small);

            }
            Object[] data = big.toArray();
            byte[] real_byte = new byte[data.length];
            for(int i=0; i<data.length;i++){
                real_byte[i] = (byte)data[i];
            }

            if(HOW==SAVE_TO_SANDBOX){
                FileOutputStream fos = new FileOutputStream(this.save_file);

                fos.write(real_byte);
                fos.flush();
            }else if(HOW==RETURN_STRING){
                result = new String(real_byte, "UTF-8");
            }



            in.close();
            Log.i("成功", "已經同步");
            noter.complete();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("出錯", e.toString());
        }

    }
}
