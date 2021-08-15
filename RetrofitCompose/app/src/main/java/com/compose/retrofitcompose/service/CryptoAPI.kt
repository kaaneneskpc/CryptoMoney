package com.compose.retrofitcompose.service

import com.compose.retrofitcompose.model.CryptoModel
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {

    // atilsamancioglu/K21-JSONDataSet/master/crypto.json
    // BASE_URL: https://raw.githubusercontent.com/

    @GET("atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    fun getData(): Call<List<CryptoModel>>
}