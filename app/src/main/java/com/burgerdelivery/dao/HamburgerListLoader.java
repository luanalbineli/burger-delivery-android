package com.burgerdelivery.dao;

import android.content.Context;

import com.burgerdelivery.model.BurgerModel;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import timber.log.Timber;

public class HamburgerListLoader extends AsyncTaskLoaderExecutor<List<BurgerModel>> {
    private final Retrofit mRetrofit;

    public HamburgerListLoader(Context context, Retrofit retrofit) {
        super(context);

        this.mRetrofit = retrofit;
    }

    @Override
    public List<BurgerModel> loadInBackground() {
        Timber.d("Fetching the burger list from the API using Retrofit");
        try {
            return mRetrofit.create(APIInterface.class).getBurgerList().execute().body();
        } catch (IOException e) {
            Timber.e(e, "An error occurred while tried to fetch the burger list");
            return null;
        }
    }
}
