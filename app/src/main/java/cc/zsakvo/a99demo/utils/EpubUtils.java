package cc.zsakvo.a99demo.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by akvo on 2018/2/1.
 */

public class EpubUtils {
    private static String BOOKTOC = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"zh-CN\">\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\" />\n" +
            "<meta name=\"generator\" content=\"EasyPub v1.50\" />\n" +
            "<title>\n" +
            "Table Of Contents\n" +
            "</title>\n" +
            "<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\"/>\n" +
            "</head>\n" +
            "<body>\n" +
            "<h2 class=\"titletoc\">目录</h2>\n" +
            "<div class=\"toc\">\n" +
            "<dl>\n" +
            "toreplace0\n" +
            "</dl>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>\n";
    public static String CHAPTER = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"zh-CN\">\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\" />\n" +
            "<meta name=\"generator\" content=\"EasyPub v1.50\" />\n" +
            "<title>\n" +
            "toreplace0 - 0\n" +
            "</title>\n" +
            "<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\"/>\n" +
            "</head>\n" +
            "<body>\n" +
            "toreplace1\n"+
            "</body>\n" +
            "</html>";
    private static String CONTAINER = "<?xml version=\"1.0\"?>\n" +
            "<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\n" +
            "  <rootfiles>\n" +
            "    <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>\n" +
            "  </rootfiles>\n" +
            "</container>";
    private static String CONTENT = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
            "<package version=\"2.0\" xmlns=\"http://www.idpf.org/2007/opf\" unique-identifier=\"bookid\">\n" +
            "<metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:opf=\"http://www.idpf.org/2007/opf\">\n" +
            "<dc:identifier id=\"bookid\">easypub-c260e840</dc:identifier>\n" +
            "<dc:title>toreplace0</dc:title>\n" +
            "<dc:creator opf:role=\"aut\">toreplace1</dc:creator>\n" +
            "<dc:date>2018</dc:date>\n" +
            "<dc:rights>Created with EasyPub v1.50</dc:rights>\n" +
            "<dc:language>zh-CN</dc:language>\n" +
            "<meta name=\"cover\" content=\"cover-image\"/>\n" +
            "</metadata>\n" +
            "toreplace2\n" +
            "toreplace3" +
            "<guide>\n" +
            "<reference href=\"cover.html\" type=\"cover\" title=\"Cover\"/>\n" +
            "<reference href=\"book-toc.html\" type=\"toc\" title=\"Table Of Contents\"/>\n" +
            "<reference href=\"chapter0.html\" type=\"text\" title=\"Beginning\"/>\n" +
            "</guide>\n" +
            "</package>\n";
    private static String MIMETYPE = "application/epub+zip";
    private static String STYLE = "@font-face {\n" +
            "      font-family: \"easypub\";\n" +
            "      src: url(res:///system/fonts/DroidSansFallback.ttf),\n" +
            "           url(res:///ebook/fonts/../../system/fonts/DroidSansFallback.ttf);\n" +
            "}\n" +
            "\n" +
            "@page { \n" +
            "      margin-top: 0px;\n" +
            "      margin-bottom: 0px;\n" +
            "}\n" +
            "\n" +
            "body { \n" +
            "      font-family: \"easypub\";\n" +
            "      padding: 0;\n" +
            "      margin-left: 0px;\n" +
            "      margin-right: 0px;\n" +
            "      orphans: 0;\n" +
            "      widows: 0;\n" +
            "}\n" +
            "\n" +
            "p { \n" +
            "      font-family: \"easypub\";\n" +
            "      font-size: 120%;\n" +
            "      line-height: 125%;\n" +
            "      margin-top: 5px;\n" +
            "      margin-bottom: 0;\n" +
            "      margin-left: 0;\n" +
            "      margin-right: 0;\n" +
            "      orphans: 0;\n" +
            "      widows: 0;\n" +
            "}\n" +
            "\n" +
            ".a { \n" +
            "      text-indent: 0em;\n" +
            "}\n" +
            "\n" +
            "div.centeredimage {\n" +
            "      text-align:center;\n" +
            "      display:block;\n" +
            "      margin-top: 0.5em;\n" +
            "      margin-bottom: 0.5em;\n" +
            "}\n" +
            "\n" +
            "img.attpic {\n" +
            "      border: 1px solid #000000;\n" +
            "      max-width: 100%;\n" +
            "      margin: 0;\n" +
            "}\n" +
            "\n" +
            ".booktitle {\n" +
            "      margin-top: 30%;\n" +
            "      margin-bottom: 0;\n" +
            "      border-style: none solid none none;\n" +
            "      border-width: 50px;\n" +
            "      border-color: #4E594D;\n" +
            "      font-size: 3em;\n" +
            "      line-height: 120%;\n" +
            "      text-align: right;\n" +
            "}\n" +
            "\n" +
            ".bookauthor {\n" +
            "      margin-top: 0;\n" +
            "      border-style: none solid none none;\n" +
            "      border-width: 50px;\n" +
            "      border-color: #4E594D;\n" +
            "      page-break-after: always;\n" +
            "      font-size: large;\n" +
            "      line-height: 120%;\n" +
            "      text-align: right;\n" +
            "}\n" +
            "\n" +
            ".titletoc, .titlel1top, .titlel1std,.titlel2top, .titlel2std,.titlel3top, .titlel3std,.titlel4std {\n" +
            "      margin-top: 0;\n" +
            "      border-style: none double none solid;\n" +
            "      border-width: 0px 5px 0px 20px;\n" +
            "      border-color: #586357;\n" +
            "      background-color: #C1CCC0;\n" +
            "      padding: 45px 5px 5px 5px;\n" +
            "      font-size: x-large;\n" +
            "      line-height: 115%;\n" +
            "      text-align: justify;\n" +
            "}\n" +
            "\n" +
            ".titlel1single,.titlel2single,.titlel3single {\n" +
            "      margin-top: 35%;\n" +
            "      border-style: none solid none none;\n" +
            "      border-width: 30px;\n" +
            "      border-color: #4E594D;\n" +
            "      padding: 30px 5px 5px 5px;\n" +
            "      font-size: x-large;\n" +
            "      line-height: 125%;\n" +
            "      text-align: right;\n" +
            "}\n" +
            "\n" +
            ".toc {\n" +
            "      margin-left:16%;\n" +
            "      padding:0px;\n" +
            "      line-height:130%;\n" +
            "      text-align: justify;\n" +
            "}\n" +
            "\n" +
            ".toc a { text-decoration: none; color: #000000; }\n" +
            "\n" +
            ".tocl1 {\n" +
            "      margin-top:0.5em;\n" +
            "      margin-left:-30px;\n" +
            "      border-style: none double double solid;\n" +
            "      border-width: 0px 5px 2px 20px;\n" +
            "      border-color: #6B766A;\n" +
            "      line-height: 135%;\n" +
            "      font-size: 132%;\n" +
            "}\n" +
            "\n" +
            ".tocl2 {\n" +
            "      margin-top: 0.5em;\n" +
            "      margin-left:-20px;\n" +
            "      border-style: none double none solid;\n" +
            "      border-width: 0px 2px 0px 10px;\n" +
            "      border-color: #939E92;\n" +
            "      line-height: 123%;\n" +
            "      font-size: 120%;\n" +
            "}\n" +
            "\n" +
            ".tocl3 {\n" +
            "      margin-top: 0.5em;\n" +
            "      margin-left:-20px;\n" +
            "      border-style: none double none solid;\n" +
            "      border-width: 0px 2px 0px 8px;\n" +
            "      border-color: #939E92;\n" +
            "      line-height: 112%;\n" +
            "      font-size: 109%;\n" +
            "}\n" +
            "\n" +
            ".tocl4 {\n" +
            "      margin-top: 0.5em;\n" +
            "      margin-left:-20px;\n" +
            "      border-style: none double none solid;\n" +
            "      border-width: 0px 2px 0px 6px;\n" +
            "      border-color: #939E92;\n" +
            "      line-height: 115%;\n" +
            "      font-size: 110%;\n" +
            "}\n" +
            "\n" +
            ".subtoc {\n" +
            "      margin-left:15%;\n" +
            "      padding:0px;\n" +
            "      text-align: justify;\n" +
            "}\n" +
            "\n" +
            ".subtoclist {\n" +
            "      margin-top: 0.5em;\n" +
            "      margin-left:-20px;\n" +
            "      border-style: none double none solid;\n" +
            "      border-width: 0px 2px 0px 10px;\n" +
            "      border-color: #939E92;\n" +
            "      line-height: 123%;\n" +
            "      font-size: 120%;\n" +
            "}\n" +
            "\n";
    private static String TOC = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
            "<!DOCTYPE ncx PUBLIC \"-//NISO//DTD ncx 2005-1//EN\" \"http://www.daisy.org/z3986/2005/ncx-2005-1.dtd\">\n" +
            "<ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">\n" +
            "<head>\n" +
            "<meta name=\"cover\" content=\"cover\"/>\n" +
            "<meta name=\"dtb:uid\" content=\"easypub-c260e840\" />\n" +
            "<meta name=\"dtb:depth\" content=\"1\"/>\n" +
            "<meta name=\"dtb:generator\" content=\"EasyPub v1.50\"/>\n" +
            "<meta name=\"dtb:totalPageCount\" content=\"0\"/>\n" +
            "<meta name=\"dtb:maxPageNumber\" content=\"0\"/>\n" +
            "</head>\n" +
            "\n" +
            "<docTitle>\n" +
            "<text>toreplace0</text>\n" +
            "</docTitle>\n" +
            "<docAuthor>\n" +
            "<text>toreplace1</text>\n" +
            "</docAuthor>\n" +
            "\n" +
            "toreplace2" +
            "</ncx>\n";

    private static String COVERHTML="<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"zh-CN\">\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\" />\n" +
            "<meta name=\"generator\" content=\"EasyPub v1.50\" />\n" +
            "<title>\n" +
            "Cover\n" +
            "</title>\n" +
            "<style type=\"text/css\">\n" +
            "html,body {height:100%; margin:0; padding:0;}\n" +
            ".wedge {float:left; height:50%; margin-bottom: -359px;}\n" +
            ".container {clear:both; height:0em; position: relative;}\n" +
            "table, tr, th {height: 719px; width:100%; text-align:center;}\n" +
            "</style>\n" +
            "<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\"/>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div class=\"wedge\"></div>\n" +
            "<div class=\"container\">\n" +
            "<table><tr><td>\n" +
            "<img src=\"cover.jpg\" alt=\"toreplace0\" />\n" +
            "</td></tr></table>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>";

    private static String ELE_DT_A = "<dt class=\"tocl2\"><a href=\"chapter.html\">Table</a></dt>";

    private static String ELE_MANIFEST = "<manifest>\n" +
            "<item id=\"ncxtoc\" href=\"toc.ncx\" media-type=\"application/x-dtbncx+xml\"/>\n" +
            "<item id=\"htmltoc\"  href=\"book-toc.html\" media-type=\"application/xhtml+xml\"/>\n" +
            "<item id=\"css\" href=\"style.css\" media-type=\"text/css\"/>\n" +
            "<item id=\"cover-image\" href=\"cover.jpg\" media-type=\"image/jpeg\"/>\n" +
            "<item id=\"cover\" href=\"cover.html\" media-type=\"application/xhtml+xml\"/>\n" +
            "</manifest>";

    private static String ELE_SPINE = "<spine toc=\"ncxtoc\">\n" +
            "<itemref idref=\"cover\" linear=\"no\"/>\n" +
            "<itemref idref=\"htmltoc\" linear=\"yes\"/>\n" +
            "</spine>";

    private static String ELE_NAV_MAP = "<navMap>\n" +
            "<navPoint id=\"cover\" playOrder=\"POD\">\n" +
            "<navLabel><text>THETEXT</text></navLabel>\n" +
            "<content src=\"THEHTML\"/>\n" +
            "</navPoint>\n";

    private static String ELE_CHAPTER_BODY = "<body>\n" +
            "<h2 id=\"title\" class=\"titlel2std\"></h2>\n" +
            "<p class=\"a\"></p>\n" +
            "</body>";

    private String sdPath = Environment.getExternalStorageDirectory().getPath()+"/99lib/";
    private String cachePath;
    private String bookID;
    private String bookName;
    private String bookAuthor;
    private String bookCoverURL;
    private List<String> chapters;
    private List<String> titles;
    private int chapterNums = 0;
    int threadNum = 4;

    public Boolean getOthersOK() {
        return isOthersOK;
    }

    public void setOthersOK(Boolean othersOK) {
        isOthersOK = othersOK;
    }

    public Boolean getChaptersOK() {
        return isChaptersOK;
    }

    public void setChaptersOK(Boolean chaptersOK) {
        isChaptersOK = chaptersOK;
    }

    private Boolean isOthersOK;
    private Boolean isChaptersOK;

    public EpubUtils(String bookID, String bookName, String bookAuthor, String bookCoverURL, List<String> chapters, List<String> titles){
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookCoverURL = bookCoverURL;
        this.chapters = chapters;
        this.titles = titles;
        chapterNums = chapters.size();
        cachePath = Environment.getExternalStorageDirectory().getPath()+"/99lib/cache/"+bookID;
        new File(cachePath).mkdirs();
    }

    private void writeStr(String path,String str){
        File file = new File(path);
        if (file.exists()) file.delete();
        try {
                file.createNewFile();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write(str);
                bufferedWriter.flush();
                bufferedWriter.close();
           } catch (IOException e) {
                e.printStackTrace();
        }
    }

    private void generateOthers(){
        System.out.println(cachePath);
        new File(cachePath+"/META-INF").mkdirs();
        new File(cachePath+"/OEBPS").mkdirs();
        writeStr(cachePath+"/mimetype",MIMETYPE);
        writeStr(cachePath+"/META-INF/container.xml",CONTAINER);
        writeStr(cachePath+"/OEBPS/style.css",STYLE);
        generateCover();
        generateTocNcx();
        generateContentOpf();
        generateBookToc();
        generateCoverHtml();
    }

    private void generateCover(){
        try {
            URL url_cover = new URL(bookCoverURL);
            URLConnection uri = url_cover.openConnection();
            InputStream is = uri.getInputStream();
            OutputStream os = new FileOutputStream(new File(cachePath+"/OEBPS/cover.jpg"));
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            generateCover();
        }
    }

    private void generateTocNcx(){
        String fileText = TOC;
        {
            StringBuilder d = new StringBuilder();
            String e = ELE_NAV_MAP;
            d.append(e.replace("POD","1").replace("THETEXT","封面").replace("THEHTML","cover.html"));
            d.append(e.replace("POD","2").replace("THETEXT","目录").replace("THEHTML","book-toc.html"));
            int playorder = 3;
            int chapter = 0;
            for (int i=0;i<chapterNums;i++){
                d.append(e.replace("POD",""+playorder).replace("THETEXT",titles.get(i)).replace("THEHTML","chapter"+chapter+".html"));
                playorder++;
                chapter++;
            }
            fileText = fileText.replace("toreplace0",bookName);
            fileText = fileText.replace("toreplace1",bookAuthor);
            fileText = fileText.replace("toreplace2",d);
            writeStr(cachePath+"/OEBPS/toc.ncx",fileText);
        }
    }

    private void generateContentOpf(){
        String fileText = CONTENT;
        Element element_manifest = Jsoup.parse(ELE_MANIFEST).selectFirst("manifest");
        Element element_spine = Jsoup.parse(ELE_SPINE).selectFirst("spine");
        Element d = element_manifest.select("item").get(4).clone();
        Element e = element_spine.select("itemref").get(1).clone();
        int chapter = 0;
        for (int i=0;i<chapterNums;i++){
            d.selectFirst("item").attr("id","chapter"+chapter);
            d.selectFirst("item").attr("href","chapter"+chapter+".html");
            e.selectFirst("itemref").attr("idref","chapter"+chapter);
            element_manifest.append(d.toString());
            element_spine.append(e.toString());
            chapter++;
        }
        fileText = fileText.replace("toreplace0",bookName);
        fileText = fileText.replace("toreplace1",bookAuthor);
        fileText = fileText.replace("toreplace2",element_manifest.toString());
        fileText = fileText.replace("toreplace3",element_spine.toString());
        writeStr(cachePath+"/OEBPS/content.opf",fileText);
    }

    private void generateBookToc(){
        String fileText = BOOKTOC;
        StringBuilder tmp = new StringBuilder();
//        Element element_dl = Jsoup.parse(ELE_DT_A).selectFirst("dl");
//        Element d = element_dl.select("dt").get(0).clone();
//        element_dl.selectFirst("dt").remove();
        int chapter = 0;
        for (int i=0;i<chapterNums;i++){
            tmp.append(ELE_DT_A.replace("chapter.html","chapter"+chapter).replace("Table",titles.get(i))+"\n");
//            d.selectFirst("a").attr("href","chapter"+chapter+".html");
//            d.selectFirst("a").text(titles.get(i));
//            element_dl.append(d.toString());
            chapter++;
        }
        fileText =  fileText.replace("toreplace0",tmp.toString());
        writeStr(cachePath+"/OEBPS/book-toc.html",fileText);
    }

    private void generateCoverHtml(){
        writeStr(cachePath+"/OEBPS/cover.html",COVERHTML.replace("toreplace0",bookName));
    }

    private void packageEpub(){
        ZipUtil.pack(new File(cachePath), new File(sdPath+bookName+".epub"), new NameMapper() {
            public String map(String name) {
                return name;
            }
        });
    }

    private void generateChapter(int i){
        String fileText = CHAPTER.replace("toreplace0","chapter"+i);
        fileText = fileText.replace("toreplace1",chapters.get(i));
        writeStr(cachePath+"/OEBPS/chapter"+i+".html",fileText);
    }

    class Generate extends Thread{
        Message msg;
        Handler handler;
        public Generate(Message msg, Handler handler){
            this.msg = msg;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            generateOthers();
            for (int i=0;i<chapterNums;i++){
                generateChapter(i);
            }
            msg.what = 1;
            handler.sendMessage(msg);
        }
    }

    public void generateEpub(){
        Message msgGenerate = new Message();

        @SuppressLint("HandlerLeak")
        Handler handGenerate = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        packageEpub();
                    default:
                        break;
                }
            }
        };
        new Generate(msgGenerate,handGenerate).start();
    }

}
