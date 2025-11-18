package com.rpalmar.financialapp.usecases.category

import android.util.Log
import com.rpalmar.financialapp.providers.database.repositories.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
){

    suspend operator fun invoke(categoryID: Long): Boolean? {
        try {
            val categoryToDelete = categoryRepository.getByID(categoryID)

            if(categoryToDelete == null){
                Log.i("DeleteCategoryUseCase", "Entity not found");
                return false;
            }

            categoryRepository.softDelete(categoryToDelete.id);

            Log.i("DeleteCategoryUseCase", "💳 Entity deleted")
            return true;
        } catch (ex: Exception) {
            Log.e("DeleteCategoryUseCase", ex.message.toString());
            return null;
        }
    }

}