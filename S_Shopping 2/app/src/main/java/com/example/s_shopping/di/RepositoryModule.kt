package com.example.s_shopping.di

import com.example.s_shopping.data.repository.AuthRepository
import com.example.s_shopping.data.repository.ProductRepository
import com.example.s_shopping.data.source.local.ProductDao
import com.example.s_shopping.data.source.remote.ProductService
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductsRepository(
        productService: ProductService, productDao: ProductDao
    ) = ProductRepository(productService, productDao)

    @Provides
    @Singleton
    fun provideAutRepository(
        firebaseAuth: FirebaseAuth
    ) = AuthRepository(firebaseAuth)
}
