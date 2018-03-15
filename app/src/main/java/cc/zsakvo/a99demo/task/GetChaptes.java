package cc.zsakvo.a99demo.task;

import android.os.Message;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;

import cc.zsakvo.a99demo.utils.DecodeUtils;

/**
 * Created by akvo on 2018/3/15.
 */

public class GetChaptes {

    private ConcurrentHashMap<Integer,String> ch = new ConcurrentHashMap<> ();
    private int completeNum;

    public static List<String> chapters(String preUrl,List<Integer> chapterIDs){
        return new GetChaptes ().deal (preUrl,chapterIDs);
    }

    private List<String> deal(String preUrl,List<Integer> chapterIDs){
        int chapterNum = chapterIDs.size ();
        int availableThreads = 5;
        int dealPerThread;
        if (chapterNum/ availableThreads ==0){
            dealPerThread = chapterNum/ availableThreads;
        }else {
            dealPerThread = chapterNum/ availableThreads +1;
        }

        int theChapter = 0;
        for (int h=0;h<availableThreads;h++) {
            if ((h+1)*availableThreads>dealPerThread){
                for (int i = 0; i < (h+1)*availableThreads-chapterNum; i++) {
                    new getChapter (chapterIDs.get (theChapter), preUrl);
                }
            }else {
                for (int i = 0; i < dealPerThread; i++) {
                    new getChapter (chapterIDs.get (theChapter), preUrl);
                }
            }
            theChapter += dealPerThread;
        }
        return null;
    }

    public List<String> cha(){
        return null;
    }

    class getChapter extends Thread{

        String url;
        int id;

        public getChapter(int id,String preUrl){
            this.url = preUrl+id+".htm";
            this.id = id;
        }

        @Override
        public void run() {
            super.run ();
            ch.put (id,DecodeUtils.url (url));
        }
    }

    class Handler extends android.os.Handler{

        private Message msg;
        private int chapterNum;

        public Handler(Message msg,int chapterNum){
            this.msg = msg;
            this.chapterNum = chapterNum;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage (msg);
            switch (msg.what){
                case 1:
                    completeNum++;
                    break;
                }
        }
    }
}

