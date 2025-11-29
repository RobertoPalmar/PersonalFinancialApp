package com.rpalmar.financialapp.views.account.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.StyleDomain
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CashRegisterSolid

data class AccountUIState(
    //FORM INPUT FIELDS
    var id: Long? = null,
    var accountName: String = "",
    var description: String = "",
    var currency: CurrencyDomain? = null,
    var balance: Double = 0.0,
    var color: Color = Color(0xFF81C784),
    var icon: ImageVector = LineAwesomeIcons.CashRegisterSolid,
    var errors:Map<String,String?> = emptyMap(),
    var isEditing: Boolean = false,

    //AUX DATA
    var currencyList: List<CurrencyDomain> = emptyList(),
    var mainCurrency: CurrencyDomain? = null,
    var accountList: List<AccountDomain> = emptyList(),
    var totalAccountBalance:Double = 0.0,
    var isLoading: Boolean = true,
    var isSaving: Boolean = false,

    var currentSelectedAccount: AccountDomain? = null
){
    fun toTemporalAccount(): AccountDomain {
        return AccountDomain(
            id = id ?: 0,
            name = if(accountName.isEmpty()) "Account Name" else accountName,
            description = if(description.isEmpty()) "Description" else description,
            currency = currency ?: CurrencyDomain(id = 0, name = "", symbol = "#", ISO = "", mainCurrency = false, exchangeRate = 0.0),
            balance = balance,
            balanceInMainCurrency = if(currency != null) balance / currency!!.exchangeRate else 0.0,
            style = StyleDomain(
                uiColor = color,
                uiIcon = icon
            )
        )
    }
}

sealed class AccountFormEvent{
    data class OnAccountNameChange(val value:String): AccountFormEvent()
    data class OnDescriptionChange(val value:String): AccountFormEvent()
    data class OnCurrencyChange(val value: CurrencyDomain): AccountFormEvent()
    data class OnBalanceChange(val value: Double): AccountFormEvent()
    data class OnColorChange(val value:Color): AccountFormEvent()
    data class OnIconChange(val value: ImageVector): AccountFormEvent()
    object Submit: AccountFormEvent()
    object Reset: AccountFormEvent()
}