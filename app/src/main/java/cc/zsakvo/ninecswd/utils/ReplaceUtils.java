package cc.zsakvo.ninecswd.utils;

/**
 * Created by akvo on 2018/3/17.
 */

public class ReplaceUtils {
    public static int getChapterID(String bookID,String url){
        return Integer.parseInt (url.replace ("/book/"+bookID+"/","")
                .replace (".htm",""));
    }
}
