package com.project.reshoe_fbu;

import android.app.Application;

import com.example.reshoe_fbu.R;
import com.project.reshoe_fbu.models.Message;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.Review;
import com.parse.Parse;
import com.parse.ParseObject;
import com.project.reshoe_fbu.models.Thread;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);// Register your parse models
        ParseObject.registerSubclass(Message.class);// Register your parse models
        ParseObject.registerSubclass(Thread.class);// Register your parse models
        ParseObject.registerSubclass(Review.class);// Register your parse models

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor);

        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url)).build()
        );

    }
}
