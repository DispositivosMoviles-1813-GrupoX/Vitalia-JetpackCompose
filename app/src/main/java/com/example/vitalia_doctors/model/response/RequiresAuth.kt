package com.example.vitalia_doctors.model.response

import retrofit2.Invocation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiresAuth

fun Invocation.requiresAuth(): Boolean =
    method().getAnnotation(RequiresAuth::class.java) != null ||
    method().declaringClass.getAnnotation(RequiresAuth::class.java) != null
