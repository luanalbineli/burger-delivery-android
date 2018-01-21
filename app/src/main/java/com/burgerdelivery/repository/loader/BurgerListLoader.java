package com.burgerdelivery.repository.loader;

import android.content.Context;

import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.repository.APIInterface;
import com.burgerdelivery.repository.loader.AsyncTaskLoaderExecutor;

import java.util.List;

import retrofit2.Retrofit;
import timber.log.Timber;

public class BurgerListLoader extends AsyncTaskLoaderExecutor<List<BurgerModel>> {
    private final Retrofit mRetrofit;

    public BurgerListLoader(Context context, Retrofit retrofit) {
        super(context);

        this.mRetrofit = retrofit;
    }

    @Override
    public List<BurgerModel> loadInBackground() {
        Timber.d("Fetching the burger list from the API using Retrofit");
        try {
            return mRetrofit.create(APIInterface.class).getBurgerList().execute().body();
        } catch (Exception e) {
            Timber.e(e, "An error occurred while tried to fetch the burger list");
            return null;
        }
    }
}
