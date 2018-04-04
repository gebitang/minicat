package com.mcxiaoke.minicat.service;

import android.util.Log;

import com.android.internal.http.multipart.MultipartEntity;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.PartBase;
import com.android.internal.http.multipart.StringPart;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gebitang on 26/03/2018.
 *
 */

class HttpClientMe {

    private final String TAG = "9x9in";
    private final String URL = "https://api.weibo.com/2/statuses/share.json";

    boolean postToWeibo(String txt, String token, String domain) throws IOException{
        //https://wiki.apache.org/HttpComponents/QuickStart
        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpPost httpost = new HttpPost(URL);

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("access_token", token));
        nvps.add(new BasicNameValuePair("status", txt +  domain));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        HttpResponse response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();

        Log.i(TAG, String.format("postInfo txt:%s  %s", txt, response.getStatusLine()));
        if (entity != null) {
            entity.consumeContent();
        }
        return response.getStatusLine().getStatusCode() == 200;
    }

    boolean postToWeibo(String txt, String file, String token, String domain) throws IOException{
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(URL);
        Log.i(TAG, String.format("postInfo file:%s txt:%s ",  file, txt));

        Part[] parts = new Part[3];
        parts[0] = new StringPart("access_token", token);
        parts[1] = new StringPart("status", txt + domain, "UTF-8");
        String type = URLConnection.guessContentTypeFromName(file);
        byte[] content = readFileImage(file);
        parts[2] = new ByteArrayPart(content, "pic", type);

//        HttpParams params = httpclient.getParams();
//        params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Charset.forName("UTF-8"));
        MultipartEntity multiEntity = new MultipartEntity(parts);
        httpost.setEntity(multiEntity);

        HttpResponse response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();

        Log.i(TAG, String.format("postInfo result:%s imgType:%s",  response.getStatusLine(), type));
        if (entity != null) {
            entity.consumeContent();
        }
        return response.getStatusLine().getStatusCode() == 200;
    }

    private byte[] readFileImage(String filename) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(
                new FileInputStream(filename));
        int len = bufferedInputStream.available();
        byte[] bytes = new byte[len];
        int r = bufferedInputStream.read(bytes);
        if (len != r) {
            throw new IOException("wrong file format");
        }
        bufferedInputStream.close();
        return bytes;
    }

    private class ByteArrayPart extends PartBase {
        private byte[] mData;
        private String mName;

        private ByteArrayPart(byte[] data, String name, String type)
                throws IOException {
            super(name, type, "UTF-8", "binary");
            mName = name;
            mData = data;
        }

        protected void sendData(OutputStream out) throws IOException {
            out.write(mData);
        }

        protected long lengthOfData() throws IOException {
            return mData.length;
        }

        protected void sendDispositionHeader(OutputStream out)
                throws IOException {
            super.sendDispositionHeader(out);
            out.write(("; filename=\"" + mName + "\"").getBytes());
        }
    }


}
