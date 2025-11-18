package com.rpalmar.financialapp.providers.dagger

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rpalmar.financialapp.models.Constants.ROOM_DB_NAME
import com.rpalmar.financialapp.models.ExchangeRateApi
import com.rpalmar.financialapp.providers.api.ApiProviderConfig
import com.rpalmar.financialapp.providers.api.AuthInterceptor
import com.rpalmar.financialapp.providers.api.RetrofitFactory
import com.rpalmar.financialapp.providers.api.apis.BCVApi
import com.rpalmar.financialapp.providers.api.apis.FrankfurterApi
import com.rpalmar.financialapp.providers.database.DAOs.AccountDAO
import com.rpalmar.financialapp.providers.database.DAOs.CategoryDAO
import com.rpalmar.financialapp.providers.database.DAOs.CurrencyDAO
import com.rpalmar.financialapp.providers.database.DAOs.ExchangeRateDAO
import com.rpalmar.financialapp.providers.database.DAOs.TransactionDAO
import com.rpalmar.financialapp.providers.database.FinancialDatabase
import com.rpalmar.financialapp.providers.database.MIGRATION_9_10
import com.rpalmar.financialapp.providers.database.repositories.SharedPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaggerModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    //    //HTTP CLIENT
//    @Provides
//    @Singleton
//    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
//        val builder = OkHttpClient.Builder()
//        val okHttpClient = builder
//            .build();
//        return okHttpClient;
//    }

    // BCV API
    @Provides
    @Singleton
    @Named("BCV_API")
    fun provideBcvRetrofit(): Retrofit {
        val config = ApiProviderConfig.apiConfigMap[ExchangeRateApi.BCV_API]!!
        return RetrofitFactory.create(config)
    }

    @Provides
    @Singleton
    fun provideBcvApi(@Named("BCV_API") retrofit: Retrofit): BCVApi =
        retrofit.create(BCVApi::class.java)

    // FRANKFURTER API
    @Provides
    @Singleton
    @Named("FRANKFURTER_API")
    fun provideFrankfurterRetrofit(): Retrofit {
        val config = ApiProviderConfig.apiConfigMap[ExchangeRateApi.FRANKFURTER_API]!!
        return RetrofitFactory.create(config)
    }

    @Provides
    @Singleton
    fun provideFrankfurterApi(@Named("FRANKFURTER_API") retrofit: Retrofit): FrankfurterApi =
        retrofit.create(FrankfurterApi::class.java)

    //ROOM
    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): FinancialDatabase {
        return Room
            .databaseBuilder(context, FinancialDatabase::class.java, ROOM_DB_NAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
//            .addMigrations(MIGRATION_9_10)
            .build();
    }

    @Singleton
    @Provides
    fun accountDAO(db: FinancialDatabase): AccountDAO {
        return db.accountDAO()
    }

    @Singleton
    @Provides
    fun currencyDAO(db: FinancialDatabase): CurrencyDAO {
        return db.currencyDAO()
    }

    @Singleton
    @Provides
    fun transactionDAO(db: FinancialDatabase): TransactionDAO {
        return db.transactionDAO()
    }

    @Singleton
    @Provides
    fun exchangeRateDAO(db: FinancialDatabase): ExchangeRateDAO {
        return db.exchangeRateDAO()
    }

    @Singleton
    @Provides
    fun categoryDAO(db: FinancialDatabase): CategoryDAO {
        return db.categoryDAO()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("financial_app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesRepository(sharedPreferences: SharedPreferences): SharedPreferencesRepository {
        return SharedPreferencesRepository(sharedPreferences)
    }
}