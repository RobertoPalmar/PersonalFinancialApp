package com.rpalmar.financialapp.providers.database.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.relations.AccountWithCurrencyRelation
import com.rpalmar.financialapp.models.database.relations.TransactionWithCurrencyRelation
import com.rpalmar.financialapp.models.domain.AccountDomain
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

    fun getAccountWithCurrencyByID(id:Long): AccountWithCurrencyRelation?{
        return accountDAO.getAccountWithCurrencyByID(id);
    }

    fun getAll():List<AccountEntity>{
        return accountDAO.getAll()
    }

    fun getPaginated(pageSize:Int = 20):Flow<PagingData<AccountWithCurrencyRelation>>{
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { accountDAO.getAccountListPaginated()}
        ).flow
    }

    fun getAccountListWithCurrency():Flow<List<AccountWithCurrencyRelation>>{
        return accountDAO.getAccountListWithCurrency()
    }

    suspend fun deleteAll(){
        accountDAO.deleteAll()
    }

    suspend fun delete(accountToDelete: AccountEntity){
        accountDAO.delete(accountToDelete)
    }

    suspend fun softDelete(ID:Long){
        accountDAO.softDelete(ID);
    }

    suspend fun updateBalance(accountID:Long, amount:Double){
        accountDAO.updateBalance(accountID, amount)
    }
}