package com.rpalmar.financialapp.usecases.category

import android.util.Log
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.providers.database.repositories.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCategoryByIDUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    val TAG = "GetCategoryByIDUseCase"

    suspend operator fun invoke(categoryID:Long): CategoryDomain?{
        try {
            //GET CATEGORY
            val categoryEntity = categoryRepository.getByID(categoryID);

            //VALIDATE CATEGORY
            if(categoryEntity == null){
                Log.e(TAG, "Error al obtener la categoria")
                return null;
            }

            //MAP TO DOMAIN
            val categoryDomain = categoryEntity.toDomain()

            //RETURN DATA
            Log.i(TAG, "💳 Category Obtain: $categoryDomain")
            return categoryDomain;
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString());
            return null;
        }
    }
}