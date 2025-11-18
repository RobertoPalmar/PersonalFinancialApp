package com.rpalmar.financialapp.usecases.category

import android.util.Log
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.providers.database.repositories.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
){
    val TAG = "CreateCategoryUseCase"

    suspend operator fun invoke(newCategory: CategoryDomain): Boolean {
        try {
            //MAP TO ENTITY
            val newCategoryEntity = newCategory.toEntity();

            //SAVE NEW ENTITY
            categoryRepository.insert(newCategoryEntity);

            Log.i(TAG, "💳 Entity created: $newCategory");
            return true;
        }catch (ex: Exception){
            Log.e(TAG, ex.message.toString());
            return false;
        }
    }
}