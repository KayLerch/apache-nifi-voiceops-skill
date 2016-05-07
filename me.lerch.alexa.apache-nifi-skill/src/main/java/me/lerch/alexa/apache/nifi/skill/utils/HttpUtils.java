package me.lerch.alexa.apache.nifi.skill.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * Created by Kay on 29.04.2016.
 */
public class HttpUtils {
    public static HttpResponse postJsonHttp(String strUri, String body) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(strUri);
        httpPost.setHeader("Content-Type", "application/json");
        HttpEntity reqEntity = new ByteArrayEntity(body.getBytes("UTF-8"));
        httpPost.setEntity(reqEntity);
        return httpclient.execute(httpPost);
    }

    public static HttpResponse putJsonHttp(String strUri, String body) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(strUri);
        httpPut.setHeader("Content-Type", "application/json");
        HttpEntity reqEntity = new ByteArrayEntity(body.getBytes("UTF-8"));
        httpPut.setEntity(reqEntity);
        return httpclient.execute(httpPut);
    }

    public static HttpResponse getJsonHttp(String strUri) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(strUri);
        httpGet.setHeader("Content-Type", "application/json");
        return httpclient.execute(httpGet);
    }

    public static HttpResponse deleteJsonHttp(String strUri) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(strUri);
        httpDelete.setHeader("Content-Type", "application/json");
        return httpclient.execute(httpDelete);
    }
}
