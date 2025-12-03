package com.example.vitalia_doctors.model.client

import android.content.Context
import android.content.SharedPreferences
import com.example.vitalia_doctors.appointment.data.remote.AppointmentApiService
import com.example.vitalia_doctors.model.response.WebService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("pref1", Context.MODE_PRIVATE)
    }

    private val authInterceptor = Interceptor { chain ->
        // No todas las peticiones necesitan el token (ej: LogIn, SignUp)
        val original = chain.request()

        // Leemos el token guardado en SharedPreferences
        val token = sharedPreferences.getString("token", null)

        val requestBuilder = original.newBuilder()

        // Si hay un token, lo agregamos como cabecera Bearer
        token?.let {
            requestBuilder.header("Authorization", "Bearer $it")

        }

        chain.proceed(requestBuilder.build())
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

    fun getUserId(): Long {
        return sharedPreferences.getLong("userId", 0L)
    }

    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }


}