package com.mcxiaoke.minicat.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gebitang on 26/03/2018.
 *
 */

public class HttpClientMe {

    //private final String TAG = "gebitang";

    public boolean postToWeibo(String txt, String token, String domain) throws IOException{
        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpPost httpost = new HttpPost("https://api.weibo.com/2/statuses/share.json");

        List <NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("access_token", token));
        nvps.add(new BasicNameValuePair("status", txt +  domain));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        HttpResponse response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();

        //Log.i(TAG,"status line: " + response.getStatusLine());
        if (entity != null) {
//            ByteArrayOutputStream result = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int length;
//            InputStream inputStream = entity.getContent();
//            while ((length = inputStream.read(buffer)) != -1) {
//                result.write(buffer, 0, length);
//            }
//            Log.i(TAG,"content result: " + result.toString("UTF-8"));
            entity.consumeContent();
        }
        return response.getStatusLine().getStatusCode() == 200;
    }


    //https://wiki.apache.org/HttpComponents/QuickStart
    private static void example() throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();


        HttpGet httpget = new HttpGet("https://portal.sun.com/portal/dt");

        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();

        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }
        System.out.println("Initial set of cookies:");
        List<Cookie> cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }

        HttpPost httpost = new HttpPost("https://api.weibo.com/2/statuses/share.json");

        List <NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("IDToken1", "username"));
        nvps.add(new BasicNameValuePair("IDToken2", "password"));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        response = httpclient.execute(httpost);
        entity = response.getEntity();

        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }

        System.out.println("Post logon cookies:");
        cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
    }


}
