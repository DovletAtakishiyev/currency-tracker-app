package com.tshahakurov.currencytracker.app.logic.di

import android.content.Context
import androidx.room.Room
import com.tshahakurov.currencytracker.app.logic.network.CurrencyApi
import com.tshahakurov.currencytracker.data.db.AppDataBase
import com.tshahakurov.currencytracker.data.db.CURRENCY_DB_NAME
import com.tshahakurov.currencytracker.data.db.CurrencyDao
import com.tshahakurov.currencytracker.data.db.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext app: Context): AppDataBase {
        return Room.databaseBuilder(
            app, AppDataBase::class.java, CURRENCY_DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDB: AppDataBase): UserDao {
        return appDB.userDao()
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(appDB: AppDataBase): CurrencyDao {
        return appDB.currencyDao()
    }

    @Provides
    @Singleton
    fun provideCurrencyApi(): CurrencyApi {
        val baseUrl = "http://api.exchangeratesapi.io/v1/"
        val apiKey = "2a96fa5c54ac0952a31d65839d058f41"

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val modifiedUrl = originalRequest.url.newBuilder()
                    .addQueryParameter("access_key", apiKey)
                    .build()

                val modifiedRequest = originalRequest.newBuilder()
                    .url(modifiedUrl)
                    .build()

                chain.proceed(modifiedRequest)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CurrencyApi::class.java)
    }
}