package com.rpalmar.financialapp.providers.database.repositories

import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.relations.AccountWithCurrencyRelation
import com.rpalmar.financialapp.providers.database.DAOs.AccountDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val accountDAO: AccountDAO
): BaseEntityRepository<AccountEntity, AccountDAO>(accountDAO) {

    fun getByID(id: Long): AccountEntity? {
        return accountDAO.getByID(id)
    }

    suspend fun getAccountWithCurrencyByID(id:Long): AccountWithCurrencyRelation?{
        return accountDAO.getAccountWithCurrencyByID(id);
    }

    suspend fun getAll():List<AccountEntity>{
        return accountDAO.getAll()
    }

    suspend fun getAccountListWithCurrency():Flow<List<AccountWithCurrencyRelation>>{
        return accountDAO.getAccountListWithCurrency()
    }

    suspend fun deleteAll(){
        accountDAO.deleteAll()
    }

    suspend fun delete(accountToDelete: AccountEntity){
        accountDAO.delete(accountToDelete)
    }
}