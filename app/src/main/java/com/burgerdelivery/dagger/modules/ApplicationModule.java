package com.burgerdelivery.dagger.modules;

import com.burgerdelivery.BuildConfig;
import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.repository.loader.BurgerListLoader;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {
    private final BurgerDeliveryApplication mApplication;

    public ApplicationModule(BurgerDeliveryApplication application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    BurgerDeliveryApplication providePopularMovieApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit() {
        // Add a log interceptor
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .build();
    }

    @Provides
    @Singleton
    BurgerListLoader providesHamburgerListLoader(Retrofit retrofit) {
        return new BurgerListLoader(mApplication, retrofit);
    }

    private static final String BASE_URL = "http://192.168.0.103:8080/api/";
    //private static final String BASE_URL = "http://10.0.2.2:9090/api/";
}
