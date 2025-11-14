package com.example.vitalia_doctors.model.client

import android.content.Context
import android.content.SharedPreferences
import com.example.vitalia_doctors.appointment.data.remote.AppointmentApiService
import com.example.vitalia_doctors.model.response.WebService
import com.example.vitalia_doctors.model.response.requiresAuth
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8093/api/v1/"
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("pref1", Context.MODE_PRIVATE)
    }

    private val authInterceptor = Interceptor { chain ->
        val invocation = chain.request().tag(Invocation::class.java)!!
        val original = chain.request()

        if (invocation.requiresAuth()) {
            val token = sharedPreferences.getString("token", null)
            val requestBuilder = original.newBuilder()
            token?.let {
                requestBuilder.header("Authorization", "Bearer $it")
            }
            chain.proceed(requestBuilder.build())
        } else {
            chain.proceed(original)
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    val webService: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(WebService::class.java)
    }

    val appointmentApiService: AppointmentApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AppointmentApiService::class.java)
    }
}