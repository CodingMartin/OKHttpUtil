package com.martin.httputil.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * 文件上传服务(上传文章 {@link #ACTION_UPLOAD_ARTICLE} /上传帖子 {@link #ACTION_UPLOAD_ASSIST})
 */
public class UploadService extends IntentService {

    public static final String ACTION_UPLOAD_ARTICLE = "action.hans.mq.article";
    public static final String ACTION_UPLOAD_ASSIST = "action.hans.mq.assist";

    public UploadService() {
        super("UploadService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
//            final String action = intent.getAction();
//            if (ACTION_UPLOAD_ARTICLE.equals(action)) {
//                handleUploadArticle(intent);
//            } else if (ACTION_UPLOAD_ASSIST.equals(action)) {
//                handleUploadAssist(intent);
//            }












        }
    }

    //处理帮帮贴上传
    private void handleUploadAssist(Intent intent) {

    }

    //处理文章上传
    private void handleUploadArticle(Intent intent) {

    }

}
