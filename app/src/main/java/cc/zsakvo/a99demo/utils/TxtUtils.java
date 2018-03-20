package cc.zsakvo.a99demo.utils;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cc.zsakvo.a99demo.listener.Interface;

/**
 * Created by akvo on 2018/3/20.
 */

public class TxtUtils {

    private String cachePath;

    public static void generateTXT(String bookName,String BookChapters){
        new TxtUtils (bookName,BookChapters);
    }

    public TxtUtils(String bookName, String bookChapters){
        File dir = new File (Environment.getExternalStorageDirectory().getPath()+"/99lib/txt/");
        if (!dir.exists ()){
            dir.mkdirs ();
        }
        cachePath = Environment.getExternalStorageDirectory().getPath()+"/99lib/txt/"+bookName+".txt";
        writeStr (cachePath,bookChapters);
    }

    private void writeStr(String path,String bookChapters){
        File file = new File(path);
        if (file.exists()) file.delete();
        try {
            file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter (file));
            bufferedWriter.write(bookChapters);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
