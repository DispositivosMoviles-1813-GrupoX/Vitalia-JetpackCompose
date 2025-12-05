package com.example.vitalia_doctors.model.client

import android.content.Context
import android.content.SharedPreferences
import com.example.vitalia_doctors.doctor.data.remote.DoctorApiService
import com.example.vitalia_doctors.model.response.WebService
import com.example.vitalia_doctors.payments.data.remote.ReceiptsApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"
    private lateinit var sharedPreferences: SharedPreferences

    var token: String? = null
        private set

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("pref1", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", null)
    }

    fun setToken(newToken: String?) {
        token = newToken
        sharedPreferences.edit().apply {
            if (newToken == null) {
                remove("token")
            } else {
                putString("token", newToken)
            }
            apply()
        }
    }

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        token?.let {
            requestBuilder.header("Authorization", "Bearer $it")
        }
        chain.proceed(requestBuilder.build())
    }

    // Interceptor para registrar el cuerpo de las respuestas
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor) // Añadimos el interceptor de registro
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val webService: WebService by lazy {
        retrofit.create(WebService::class.java)
    }

    val doctorApiService: DoctorApiService by lazy {
        retrofit.create(DoctorApiService::class.java)
    }

    val receiptsApiService: ReceiptsApiService by lazy {
        retrofit.create(ReceiptsApiService::class.java)
    }
}
