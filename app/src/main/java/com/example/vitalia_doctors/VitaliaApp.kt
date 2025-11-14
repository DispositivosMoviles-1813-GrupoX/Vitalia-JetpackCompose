package com.example.vitalia_doctors

import android.app.Application
import com.example.vitalia_doctors.model.client.RetrofitClient

class VitaliaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitClient.initialize(this)
    }
}
