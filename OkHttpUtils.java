package com.example.bookmanageapp.util;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtils {
    //单例模式（运行时内存只有一个okhttputil对象）
    private static OkHttpUtils instance=new OkHttpUtils();
    private OkHttpClient client;
    public OkHttpUtils(){
        //通过建造者模式创建
        client =new OkHttpClient.Builder().build();
    }
    //获取单例instance
    public static OkHttpUtils getInstance(){
        return instance;
    }
    //向服务端接口发起get请求:参数：请求地址,callback
    public void daGet(String url, Callback callback){
        //创建client对象，在前面
        //创建request对象，建造者模式
        Request request=new Request.Builder()
                .url(url)//设置请求地址
                .get()//设置请求方式
                .build();//创建request对象
        //通过request生成call对象
        Call call=client.newCall(request);
        //异步执行call
        call.enqueue(callback);
    }
    //向服务端接口发起post请求:
    public void doPost(String url, HashMap<String,String> params,Callback callback){
        //实例化FormBody
        FormBody.Builder builder=new FormBody.Builder();
        if(params != null){
            for(String key:params.keySet()){
                builder.add(key,params.get(key));
            }
            FormBody formBody=builder.build();
            //实例化resquest
            Request request=new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            //生成call
            Call call=client.newCall(request);
            //异步执行call
            call.enqueue(callback);
        }
    }
    //向服务端发起postjson请求
    public void doPostJson(String url,String jsonStr,Callback callback){
        //实例化requestBody
        MediaType mediaType=MediaType.get("application/json");
        RequestBody requestBody=RequestBody.create(jsonStr,mediaType);
        //实例化request对象
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //生成call
        Call call=client.newCall(request);
        //异步执行call
        call.enqueue(callback);
    }
}
