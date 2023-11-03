package com.sagar.livesubscounter.daggermodules;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sagar.livesubscounter.ApplicationScope;
import com.sagar.livesubscounter.BuildConfig;
import com.sagar.livesubscounter.constants.KeyStore;
import com.sagar.livesubscounter.network.repo.SponsorRepo;
import com.sagar.livesubscounter.network.repo.YoutubeRepo;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SAGAR MAHOBIA on 14-Sep-18. at 14:01
 */

@Module
public class NetworkModule {
    private static final String GOOGLE_END_POINT = "https://www.googleapis.com/youtube/v3/";
    private static final String SPONSOR_END_POINT = "##Removed";


    @Provides
    @ApplicationScope
    @Named("google")
    Interceptor provideInterceptorForGoogle(KeyStore keyStore) {
        return chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl.Builder builder = originalHttpUrl.newBuilder();
            builder.addQueryParameter("key", keyStore.getKey());
            HttpUrl url = builder.build();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
    }

    @ApplicationScope
    @Provides
    @Named("google")
    HttpLoggingInterceptor providesHttpLoggingInterceptorForGoogle() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @ApplicationScope
    @Named("google")
    OkHttpClient provideOkHttpClientForGoogle(@Named("google") Interceptor interceptor, @Named("google") HttpLoggingInterceptor httpLoggingInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor);
        }

        return builder.build();
    }

    @Provides
    @ApplicationScope
    @Named("google")
    Retrofit provideRetrofitForGoogle(@Named("google") OkHttpClient client) {

        return new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(GOOGLE_END_POINT)
                .client(client)
                .build();
    }


    @Provides
    @ApplicationScope
    YoutubeRepo youtubeRepo(@Named("google") Retrofit retrofit) {

        return retrofit.create(YoutubeRepo.class);

    }



    /*---------------------------------*/

    @ApplicationScope
    @Provides
    @Named("sponsor")
    HttpLoggingInterceptor providesHttpLoggingInterceptorForSponsor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @ApplicationScope
    @Named("sponsor")
    OkHttpClient provideOkHttpClientForSponsor(@Named("sponsor") HttpLoggingInterceptor httpLoggingInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }

    @Provides
    @ApplicationScope
    @Named("sponsor")
    Retrofit provideRetrofitForSponsor(@Named("sponsor") OkHttpClient client) {

        return new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(SPONSOR_END_POINT)
                .client(client)
                .build();
    }

    @Provides
    @ApplicationScope
    SponsorRepo sponsorRepo(@Named("sponsor") Retrofit retrofit) {

        return retrofit.create(SponsorRepo.class);

    }

    @Provides
    @ApplicationScope
    Picasso providePicasso(Context context) {
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(context, Integer.MAX_VALUE))
                .build();
        if (BuildConfig.DEBUG) {
            picasso.setIndicatorsEnabled(true);
            picasso.setLoggingEnabled(true);
        }
        return picasso;
    }


}
