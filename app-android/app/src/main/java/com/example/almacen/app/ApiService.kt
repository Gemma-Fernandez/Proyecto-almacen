package com.example.almacen.app

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Body
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("api/productos")
    fun getProductos(): Call<List<Producto>>
    @POST("api/productos")
    fun crearProducto(@Body producto: Producto): Call<Producto>
}

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.17:8080/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}