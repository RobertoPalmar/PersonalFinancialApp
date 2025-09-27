package com.rpalmar.financialapp.views.ui

sealed class UIEvent {
    object Success: UIEvent()
    data class ShowError(val message:String): UIEvent()
}