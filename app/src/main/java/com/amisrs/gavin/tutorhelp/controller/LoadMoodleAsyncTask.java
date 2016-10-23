package com.amisrs.gavin.tutorhelp.controller;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.PatternMatcher;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Gavin on 24/10/2016.
 */

public class LoadMoodleAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String MOODLE_URL = "https://moodle.telt.unsw.edu.au/";
    private static final String TAG = "LoadMoodleAsyncTask";

    OnLoadListener onLoadListener;

    public LoadMoodleAsyncTask(OnLoadListener onLoadListener) {
        super();
        this.onLoadListener = onLoadListener;
    }

    @Override
    protected String doInBackground(Void... params) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOODLE_URL)
                .build();

        BlankCall blankCall = retrofit.create(BlankCall.class);
        Call<ResponseBody> moodleCall = blankCall.getHTML();
        String HTMLResponse = "";
        try {
            retrofit2.Response<ResponseBody> response = moodleCall.execute();
            HTMLResponse = response.body().string();
            Log.d(TAG, HTMLResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pattern cssPattern = Pattern.compile(
                "(<link rel=\"stylesheet\" type=\"text\\/css\" href=\"https:\\/\\/moodle\\.telt\\.unsw\\.edu\\.au\\/theme\\/styles\\.php\\/unsw_new\\/(.+?)\\/all\" \\/>)"
        );
        Matcher cssMatcher = cssPattern.matcher(HTMLResponse);
        cssMatcher.find();

        String cssNumber = cssMatcher.group(2);
        CssCall cssCall = retrofit.create(CssCall.class);
        Call<ResponseBody> css = cssCall.getCSS(cssNumber);
        String cssString = "";
        try {
            retrofit2.Response<ResponseBody> cssResponse = css.execute();
            cssString = cssResponse.body().string();
            Log.d(TAG, cssString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String wat = "#page\\{background-image:url\\(\\/\\/(.+?)\\)\\}";

        Pattern imgUrlPattern = Pattern.compile(wat);
        Matcher imgUrlMatcher = imgUrlPattern.matcher(cssString);

        imgUrlMatcher.find();
        String imgUrl = "https://" + imgUrlMatcher.group(1);



        return imgUrl;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        Log.d(TAG, "Finished loading");
        onLoadListener.onLoad(string);
    }

    private interface BlankCall {
        @GET("login/index.php")
        Call<ResponseBody> getHTML();
    }

    private interface CssCall {
        @GET("theme/styles.php/unsw_new/{number}/all")
        Call<ResponseBody> getCSS(@Path(value = "number", encoded = true) String number);
    }
}
