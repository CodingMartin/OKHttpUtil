package com.martin.httputil.builder;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.reflect.TypeToken;
import com.martin.httputil.OKHttpUtil;
import com.martin.httputil.handler.HttpConfigController;
import com.martin.httputil.pojo.FileResponseProvider;
import com.martin.httputil.pojo.FileUploadProvider;
import com.martin.httputil.pojo.OssRequestDataProvider;
import com.martin.httputil.pojo.Result;
import com.martin.httputil.util.HSON;
import com.martin.httputil.util.HttpConstants;
import com.martin.httputil.util.ParamsType;
import com.martin.httputil.util.UploadException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Response;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/23
 */
public class OSSUploadBuilder {

    public static final String APP_ID = "appId";
    public static final String FILE_TYPE = "fileType";
    public static final String BUSINESS_ID = "businessId";
    public static final String FILE_NAME = "fileName";

    List<File> files;
    int businessId = HttpConstants.BusinessIdTieZi;
    int type = HttpConstants.FileTypeImage;
    private ClientConfiguration mClientConfiguration;
    private int maxRetry = 2; //最大重试次数 默认两次
    private Context appContext;
    private static ExecutorService mExecutors = Executors.newFixedThreadPool(6);
    private SparseBooleanArray requestFinishFlags; //这个用来标记所有的请求是否全部结束
    private SparseIntArray requestRetryFlags; //这里用来标记请求是否超过了重试次数


    public OSSUploadBuilder(Context appContext) {
        if (appContext != null) this.appContext = appContext.getApplicationContext();
        initOSSCfgs();
    }

    private void initOSSCfgs() {
        if (mClientConfiguration == null) {
            mClientConfiguration = new ClientConfiguration();
            mClientConfiguration.setConnectionTimeout(30 * 1000); // 连接超时，默认15秒
            mClientConfiguration.setSocketTimeout(30 * 1000); // socket超时，默认15秒
            mClientConfiguration.setMaxConcurrentRequest(Runtime.getRuntime().availableProcessors() * 2); // 最大并发请求数，默认5个
        }
    }


    /**
     * 单个文件
     *
     * @param file {@link File}
     * @return
     */
    public OSSUploadBuilder put(File file) {
        if (!checkCollection(file))
            files.add(file);
        return this;
    }

    public OSSUploadBuilder putAll(List<File> files) {
        if (files == null) return this;
        for (File file : files) {
            put(file);
        }
        return this;
    }

    /**
     * 业务区分 默认 {@link HttpConstants#BusinessIdTieZi}
     *
     * @param id {@link HttpConstants#BusinessIdUser} {@link HttpConstants#BusinessIdTieZi} {@link HttpConstants#BusinessIdBanner}
     * @return {@link OSSUploadBuilder}
     */
    public OSSUploadBuilder businessId(int id) {
        this.businessId = id;
        return this;
    }

    /**
     * 上传类型 暂时只支持图片上传(默认)
     *
     * @param type {@link HttpConstants#FileTypeImage}
     * @return {@link OSSUploadBuilder}
     */
    public OSSUploadBuilder type(int type) {
        this.type = type;
        return this;
    }

    /**
     * 失败后最大重试次数，默认2次
     *
     * @param count 重试次数
     * @return
     */
    public OSSUploadBuilder maxRetry(int count) {
        this.maxRetry = count;
        mClientConfiguration.setMaxErrorRetry(maxRetry);
        return this;
    }

    private boolean checkCollection(File file) {
        if (file == null || !file.exists()) {
            try {
                if (HttpConstants.DEBUG)
                    Log.d(HttpConstants.TAG, "图片不存在:" + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        if (files == null) {
            files = new ArrayList();
            return false;
        }
        return files.contains(file);
    }

    /**
     * 同步执行上传操作(必须子线程中调用)
     *
     * @return 返回图片在服务器总的路径, 可能为空
     */
    public List<String> execute() throws UploadException {
        if (files == null || files.size() == 0) return null;
        HttpConfigController mHandler = OKHttpUtil.getController();
        if (mHandler != null && mHandler.networkCheck()) { //前置检查
            return null;
        }
        final List<String> fileServerPaths = new ArrayList<>(files.size());
        requestRetryFlags = new SparseIntArray(files.size());

        for (int i = 0; i < files.size(); i++) {
            final File file = files.get(i);
            requestRetryFlags.put(i, 0);
            uploadFile(i, fileServerPaths, file);
        }
        if (HttpConstants.DEBUG) Log.d(HttpConstants.TAG, "上传成功");
        return fileServerPaths;

    }

    private void uploadFile(int index, List<String> fileServerPaths, File file) throws UploadException {
        try {
            Response response = OKHttpUtil.get().api(HttpConstants.UploadImage).addParam(APP_ID, HttpConstants.AppId)
                    .addParam(FILE_TYPE, type).addParam(BUSINESS_ID, businessId)
                    .addParam(FILE_NAME, getFileName(file)).paramType(ParamsType.RAW)
                    .sign().execute();
            if (response.isSuccessful()) {
                String string = response.body().string();
                Result<FileUploadProvider> result = HSON.parse(string, new TypeToken<Result<FileUploadProvider>>() {
                });

                HttpConfigController mHandler = OKHttpUtil.getController();
                if (mHandler != null && !mHandler.unitHandle(result)) { //后置检查
                    return;
                }

                if (result.isSuccess()) {
                    FileUploadProvider data = result.data;
                    //拼装请求前置参数
                    OSS ossClient = getOSSClient(data);
                    PutObjectResult ossResult = requestOSSServer(file, data, ossClient);
                    parseOSSResult(index, fileServerPaths, file, ossResult);
                } else {
                    checkRetryCount(index, fileServerPaths, file, "重试次数超过" + maxRetry + "次了");
                }
            } else {
                checkRetryCount(index, fileServerPaths, file, "重试次数超过" + maxRetry + "次了");
            }
        } catch (IOException e) {
            e.printStackTrace();
            checkRetryCount(index, fileServerPaths, file, "获取凭证失败");
        } catch (ClientException e) {
            e.printStackTrace();
            checkRetryCount(index, fileServerPaths, file, "OSS服务器初始化失败");
        } catch (ServiceException e) {
            e.printStackTrace();
            checkRetryCount(index, fileServerPaths, file, "OSS服务器操作失败");
        } catch (Exception e) {
            e.printStackTrace();
            checkRetryCount(index, fileServerPaths, file, "上传失败");
        }
    }

    private void parseOSSResult(int index, List<String> fileServerPaths, File file, PutObjectResult ossResult) throws UploadException {
        //解析结果数据
        String body = ossResult.getServerCallbackReturnBody();
        Result<FileResponseProvider> uploadResult = HSON.parse(body, new TypeToken<Result<FileResponseProvider>>() {
        });
        if (uploadResult.isSuccess()) {
            fileServerPaths.add(uploadResult.data.fileName);
        } else {
            checkRetryCount(index, fileServerPaths, file, "重试次数超过" + maxRetry + "次了");
        }
    }

    private PutObjectResult requestOSSServer(File file, FileUploadProvider data, OSS ossClient) throws ClientException, ServiceException {
        PutObjectRequest put = new PutObjectRequest(data.bucket, data.objectKey, file.getAbsolutePath());
        put.setCallbackParam(data.callBack);
        put.setCallbackVars(data.params);

        //同步上传图片
        return ossClient.putObject(put);
    }

    private OSS getOSSClient(FileUploadProvider data) {
        OSSCredentialProvider credentialProvider = new OssRequestDataProvider(data.accessKeyId, data.accessKeySecret,
                data.securityToken, data.expiration);
        initOSSCfgs();
        return new OSSClient(appContext, data.endpoint, credentialProvider, mClientConfiguration);
    }

    private void checkRetryCount(int index, List<String> fileServerPaths, File file, String msg) throws UploadException {
        int retryCount = requestRetryFlags.get(index);
        if (retryCount < 2) {
            requestRetryFlags.put(index, ++retryCount);
            uploadFile(index, fileServerPaths, file);
        } else {
            throw new UploadException(msg);
        }
    }

    private String getFileName(File file) {
        String path = file.getAbsolutePath();
        return path.split("/")[path.split("/").length - 1];
    }
}
