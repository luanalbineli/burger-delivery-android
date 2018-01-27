package com.burgerdelivery.repository;

import com.burgerdelivery.model.BurgerModel;
import com.burgerdelivery.model.request.OrderRequestModel;
import com.burgerdelivery.model.response.FinishOrderResponseModel;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {
    @GET("burger")
    Call<List<BurgerModel>> getBurgerList();

    @POST("order")
    Single<FinishOrderResponseModel> finishOrder(@Body OrderRequestModel orderRequestModel);
}
