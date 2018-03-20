//package cc.zsakvo.a99demo.phrase;
//
//import android.annotation.SuppressLint;
//import android.os.Handler;
//import android.os.Message;
//
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import cc.zsakvo.a99demo.SearchActivity;
//import cc.zsakvo.a99demo.utils.DecodeUtils;
//
///**
// * Created by akvo on 2018/2/16.
// */
//
//public class PhraseBookDetail {
//
//    String url;
//    private String bookID;
//    private String bookName;
//    private String bookAuthor;
//    private String bookCoverURL;
//    private List<String> chapters;
//    private List<String> titles;
//    static Book book;
//
//    public PhraseBookDetail(String url){
//        this.url = url;
//    }
//    public static Book url(String url){
//        new PhraseBookDetail(url).deal();
//        return book;
//    }
//
//    private void deal(){
//        bookID = url.replace("http://www.99lib.net/book/","").replace("/index.htm","");
//        Message msg = new Message();
//        @SuppressLint("HandlerLeak")
//        android.os.Handler handler = new android.os.Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case 1:
//                        book = new Book(bookID,bookName,bookAuthor,bookCoverURL,chapters,titles);
//                    default:
//                        break;
//                }
//            }
//        };
//        new getResult(handler,msg).start();
//    }
//
//    class getResult extends Thread{
//        Handler handler;
//        Message msg;
//        public getResult(Handler handler, Message msg){
//            this.handler = handler;
//            this.msg = msg;
//        }
//
//        private String splitElement(Element element){
//            Elements es = element.select("a");
//            String str = "";
//            for (Element e:es){
//                String tmp = e.text()+" ";
//                str = str + tmp;
//            }
//            element.select("a").remove();
//            String tmp = element.text()+" ";
//            str = tmp+str;
//            return str;
//        }
//
//        @Override
//        public void run() {
//            super.run();
//            try {
//                Document doc = Jsoup.connect(url).timeout(20000).get();
//                bookName = doc.selectFirst("h2").text();
//                bookAuthor = splitElement(doc.selectFirst("h4"));
//                bookCoverURL = "http://www.99lib.net"+doc.selectFirst("img").attr("src");
//                Elements elements_drags = doc.getElementById("right").select("dd");
//                titles = new ArrayList<>();
//                chapters = new ArrayList<>();
//                for (Element e:elements_drags){
//                    titles.add(e.selectFirst("a").text());
//                    chapters.add(DecodeUtils.url("http://www.99lib.net"+e.selectFirst("a").attr("href")));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            msg.what = 1;
//            handler.sendMessage(msg);
//        }
//    }
//
//}
