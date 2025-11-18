package com.rpalmar.financialapp.usecases.category

import android.util.Log
import com.rpalmar.financialapp.models.database.CategoryEntity
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.providers.database.repositories.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    val TAG = "UpdateCategoryUseCase"

    suspend operator fun invoke(categoryToUpdate: CategoryDomain): Boolean? {
        try {
            //MAP TO ENTITY
            val categoryEntity = categoryToUpdate.toEntity();

            //UPDATE ENTITY
            categoryRepository.update(categoryEntity);

            Log.i(TAG, "💳 Entity updated");
            return true;
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString());
            return null;
        }
    }
}