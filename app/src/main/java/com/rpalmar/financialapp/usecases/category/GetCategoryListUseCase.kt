package com.rpalmar.financialapp.usecases.category

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.providers.database.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    val TAG = "GetCategoriesUseCase"

    suspend operator fun invoke(): List<CategoryDomain>? {
        try{
            //GET CATEGORIES
            var categories = categoryRepository.getAll()

            //MAP TO DOMAIN
            val categoryDomainList = categories.map { it.toDomain() };

            //RETURN DATA
            Log.i(TAG, "💳 Categories Obtain: ${categoryDomainList.map { it.toString() }}")
            return categoryDomainList;
        }catch (ex: Exception){
            Log.e(TAG, ex.message.toString());
            return null;
        }
    }

    fun getPaginated(pageSize: Int = 20): Flow<PagingData<CategoryDomain>> {
        return flow {
            val categoryFlow = categoryRepository.getPaginated(pageSize)
                .map { pagingData ->
                    pagingData.map { category ->
                        //MAP TO DOMAIN
                        category.toDomain()
                    }
                }

            //RETURN FLOW WITH DATA
            emitAll(categoryFlow)
        }
    }
}
