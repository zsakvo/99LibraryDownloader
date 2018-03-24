package cc.zsakvo.ninecswd.utils;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;


/**
 * Created by akvo on 2018/2/8.
 */

public class DecodeUtils {


    public static class Decode99{

        private String url;
        private String title;
        private int type;
        String client = "";
        Element content;

        String getContent(String url,int type){
            this.type = type;
            try {
                this.url = url;
                Document doc = Jsoup.connect(url).timeout(13000).get();
                this.title = doc.selectFirst("h2").text();
                doc.select("strike,acronym,bdo,big,site,code,dfn,kbd,q,s,samp,tt,u,var,cite")
                        .remove();
                this.client = doc.select("meta").get(4).attr("content");
                this.content = doc.getElementById("content");
                return deal();
            } catch (IOException e) {
                return null;
            }
        }

        private String random(){
            return String.valueOf(Math.floor(Math.random() * 25 + 97) + Math.floor(Math.random() * (1000000000)));
        }

        public String deal()  {
            int star = 0;
            Elements childNotes = content.children();
            int childNodesLength = childNotes.size();
            for (int i = 0; i < childNodesLength; i++) {
                if (childNotes.get(i).tagName().equals("h2")) {
                    star = i + 1;
                }
                if (childNotes.get(i).tagName().equals("div") && !childNotes.get(i).className().equals("chapter")) {
                    break;
                }
            }
            String text = new String ();
            switch (type){
                case 0:
                    text = this.title+"\n\n"+load(childNotes,star)+"\n\n\n";
                    break;
                case 1:
                    text = "<h2 id=\"title\" class=\"titlel2std\">"+this.title+"</h2>\n"+load(childNotes,star);
                    break;
                case 2:
                    text = "<h2 id=\"title\" class=\"titlel2std\" style=\"text-align:center;\">"+this.title+"</h2>\n"+load(childNotes,star);
                default:
                    text = this.title+"\n\n"+load(childNotes,star)+"\n\n\n";
                break;
            }
            return text;
//            return "<h2 id=\"title\" class=\"titlel2std\">"+this.title+"</h2>\n"+load(childNotes,star);
//            return this.title+"\n"+load(childNotes,star);
        }

        private String load(Elements childNotes,int star){
            String t = base64(client).replaceAll("[^0-9%]","");
            String[] e = t.split("%");
            int k = 0;
            int size =0;
            for (int i=0;i<e.length;i++){
                int tmp = 0;
                if (Integer.parseInt(e[i])<5){
                    k++;
                    tmp = Integer.parseInt(e[i]);
                }else {
                    tmp = Integer.parseInt(e[i])-k;
                }
                if (size<tmp){
                    size = tmp;
                }
            }
            StringBuilder content = new StringBuilder();
            Element[] childNode = new Element[size+1];
            int j = 0;
            for (int i = 0; i < e.length;i++) {
                int z= Integer.parseInt(e[i]);
                if (z < 5) {
                    childNode[z] = childNotes.get(i+star);
                    j++;
                } else {
                    childNode[z-j] = childNotes.get(i+star);
                }
            }
            for (int i =0;i<childNode.length;i++){
                if (childNode[i]!=null) {
                    switch (type){
                        case 0:
                            content.append("        "+childNode[i].text()+"\n");
                            break;
                        case 1:
                            content.append("<p class=\"a\">"+childNode[i].text()+"</p>\n");
                            break;
                        case 2:
                            content.append("<p class=\"a\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+childNode[i].text()+"</p>\n");
                            break;
                    }
                }
            }
            return content.toString();
        }

        private String base64(String a){
            String map = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
            String b = "";
            String binary  = b;
            String[] sc = {"00000","0000","000","00","0",""};
            List<String> binarys = new ArrayList<>();
            for (int i = 0; i < a.length(); i++) {
                if (a.substring(i, i+1).equals("=")) {
                    break;
                }
                String c = Integer.toBinaryString(map.indexOf(a.charAt(i)));
                binary += sc[c.length()-1]+c;
            };
            for (int i=0;i<binary.length();i+=8){
                if (i+8>=binary.length()){
                    binarys.add(binary.substring(i));
                }else {
                    binarys.add(binary.substring(i, i + 8));
                }
            }
            for (int i = 0; i < binarys.size(); i++) {
                b += "" + (char)(Integer.parseInt(binarys.get(i),2));
            };
            return b;
        }
    };

    public static String url(String url,int type){
        return new Decode99().getContent(url,type);
    }

}
