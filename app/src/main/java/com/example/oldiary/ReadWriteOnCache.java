package com.example.oldiary;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadWriteOnCache extends CreateActivity {
    public String cacheFileName;

    //コンストラクタ
    public ReadWriteOnCache(String cacheFileName) {
        this.cacheFileName = cacheFileName;
    }

    private final static String TAG = "Oldiary";
    public void writeOnCache(String txt/*, Context mContext*/) {
        FileOutputStream outputStream = null;
        //Context context = this;
        try {
            //new File.createTempFile("cache.txt", null, getApplicationContext().getCacheDir());
            File file = new File(getCacheDir(), this.cacheFileName);
            file.createNewFile();
            if (file.exists()) {
                outputStream = new FileOutputStream(file);
                outputStream.write(txt.getBytes());
            }
        } catch (IOException e) {
            //Log.e(TAG, "exception", e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                //Log.e(TAG, "exception", e);
            }
        }
    }

    public String readOnCache(/*Context mContext*/) {
        StringBuffer sb = new StringBuffer();
        FileInputStream inputStream = null;
        byte[] buffer = new byte[256];
        try {
            File file = new File(this.getCacheDir(), this.cacheFileName);
            if (file.exists()) {
                inputStream = new FileInputStream(file);
                inputStream.read(buffer);
                String data = new String(buffer, "UTF-8");
                sb.append(data);
            }
        }catch (IOException e) {
            //Log.e(TAG, "exception", e);
        }finally {
            try {
                if(inputStream != null) { inputStream.close(); }
            } catch (IOException e) {
                //Log.e(TAG, "exception", e);
            }
        }
        return sb.toString();
    }

    public void deleteCache(/*Context mContext*/) {
        File file = new File(this.getCacheDir(), this.cacheFileName);
        file.delete();
    }
}
