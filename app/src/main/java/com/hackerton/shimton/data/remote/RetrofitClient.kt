package com.hackerton.shimton.data.remote

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.hackerton.shimton.BuildConfig
import com.hackerton.shimton.data.remote.api.MainService
import com.hackerton.shimton.data.remote.api.RoomService
import com.hackerton.shimton.data.remote.dto.ErrorResponse
import com.hackerton.shimton.util.Network.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    val roomService: RoomService by lazy {
        getRetrofit().create(RoomService::class.java)
    }
    val orderService: MainService by lazy {
        getRetrofit().create(MainService::class.java)
    }

    private fun getRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildOkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .setLenient()
                        .create()
                )
            )
            .build()
    }

    //okHttp 로그
    private fun buildOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    fun getErrorResponse(errorBody: ResponseBody): ErrorResponse? {
        return getRetrofit().responseBodyConverter<ErrorResponse>(
            ErrorResponse::class.java,
            ErrorResponse::class.java.annotations
        ).convert(errorBody)
    }
}