package com.burgerdelivery.dao;

import com.burgerdelivery.model.BurgerModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {
    @GET("burger")
    Call<List<BurgerModel>> getBurgerList();
}
