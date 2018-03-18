package cc.zsakvo.a99demo.utils;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by akvo on 2018/3/14.
 */

public class SplitUtil {
    public static String splitElement(Element element){
        Elements es = element.select("a");
        String str = "";
        for (Element e:es){
            String tmp = e.text()+" ";
            str = str + tmp;
        }
        element.select("a").remove();
        String tmp = element.text()+" ";
        str = tmp+str;
        return str;
    }

    public static String splitElement(Element element,String rStr1,String rStr2){
        Elements es = element.select("a");
        String str = " ";
        for (Element e:es){
            String tmp = e.text()+" ";
            str = str + tmp;
        }
        element.select("a").remove();
        String tmp = element.text();
        str = str.replace(rStr1,"");
        str = str.replace(rStr2.replace("作者:",""),"");
        str = tmp+str;
        return str;
    }

    public static int[][] splitChaIDsByNum(List<Integer> list, int num) {
        int size = list.size();
        int remainder = size % num;
        int threadNum;
        int[][] result;
        if (remainder == 0) {
            threadNum = size / num;
            result = new int[threadNum][num];
            for (int i = 0; i < threadNum; i++) {
                for (int j = 0; j < num; j++) {
                    result[i][j] = list.get(i * num + j);
                }
            }
        } else {
            threadNum = size / num + 1;
            result = new int[threadNum][num];
            for (int i = 0; i < threadNum - 1; i++) {
                for (int j = 0; j < num; j++) {
                    result[i][j] = list.get(i * num + j);
                }
            }
            for (int k = 0; k < remainder; k++) {
                result[threadNum - 1][k] = list.get((threadNum - 1) * num + k);
            }
        }
        return result;
    }
}
