package com.example.s_shopping.di

import android.content.Context
import com.example.s_shopping.common.Constants.BASE_URL
import com.example.s_shopping.data.source.remote.ProductService
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
// SingletonComponent : Application çalıştığı anda tüm uygulamada kullanılabilir hale gelmesini sağlar
object NetworkModule {

    //Chuker iletiyorum
    @Singleton //tekil
    @Provides  //sağla
    fun provideChuckerInterceptor(@ApplicationContext context: Context) =
        ChuckerInterceptor.Builder(context).build()
    //Fonksiyonu singleton olarak ayağa kaldır

    @Singleton
    @Provides
    fun provideOkHttp(chucker: ChuckerInterceptor) = OkHttpClient.Builder().apply {
        addInterceptor(
            Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("store", "magictouch")
                return@Interceptor chain.proceed(builder.build())
            }
        )
        addInterceptor(chucker)
    }.build()

    //okHttp iletiyorum
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder().apply {
        addConverterFactory(GsonConverterFactory.create())
        baseUrl(BASE_URL)
        client(okHttpClient)
    }.build()

    //ProductService ayağa kaldırılma işlemi
    //Retrofit iletiyorum
    @Singleton
    @Provides
    fun provideProductService(retrofit: Retrofit) = retrofit.create(ProductService::class.java)
}