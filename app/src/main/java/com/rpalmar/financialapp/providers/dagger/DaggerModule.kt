package com.rpalmar.financialapp.providers.dagger

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rpalmar.financialapp.models.Constants.ROOM_DB_NAME
import com.rpalmar.financialapp.providers.database.DAOs.AccountDAO
import com.rpalmar.financialapp.providers.database.DAOs.CurrencyDAO
import com.rpalmar.financialapp.providers.database.DAOs.EnvelopeDAO
import com.rpalmar.financialapp.providers.database.DAOs.TransactionDAO
import com.rpalmar.financialapp.providers.database.FinancialDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaggerModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    //ROOM
    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): FinancialDatabase {
        return  Room
            .databaseBuilder(context, FinancialDatabase::class.java,ROOM_DB_NAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build();
    }

    @Singleton
    @Provides
    fun accountDAO(db: FinancialDatabase): AccountDAO{
        return db.accountDAO()
    }

    @Singleton
    @Provides
    fun currencyDAO(db: FinancialDatabase): CurrencyDAO{
        return db.currencyDAO()
    }

    @Singleton
    @Provides
    fun envelopeDAO(db: FinancialDatabase): EnvelopeDAO{
        return db.envelopeDAO()
    }

    @Singleton
    @Provides
    fun transactionDAO(db: FinancialDatabase): TransactionDAO{
        return db.transactionDAO()
    }
}