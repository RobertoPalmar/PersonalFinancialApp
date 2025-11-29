package com.rpalmar.financialapp.views.transaction.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.usecases.account.GetAccountByIDUseCase
import com.rpalmar.financialapp.usecases.transaction.CreateTransactionUseCase
import com.rpalmar.financialapp.views.ui.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.lifecycle.SavedStateHandle
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.models.interfaces.IDomainTransaction
import com.rpalmar.financialapp.usecases.category.GetCategoryListUseCase
import com.rpalmar.financialapp.usecases.transaction.GetTransactionSourceListUseCase
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getAccountByIDUseCase: GetAccountByIDUseCase,
    private val getTransactionSourceListUseCase: GetTransactionSourceListUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //UI STATE
    private val _transactionUIState = MutableStateFlow(TransactionUiState())
    val transactionUIState: StateFlow<TransactionUiState> = _transactionUIState.asStateFlow()

    //UI EVENT LISTENER
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val sourceTypeStr = savedStateHandle.get<String>("sourceType")
            val sourceIdStr = savedStateHandle.get<String>("sourceId")

            var transactionSource: IDomainTransaction? = null

            if (sourceTypeStr != null && sourceIdStr != null) {
                val sourceType = TransactionSourceType.valueOf(sourceTypeStr)
                val sourceId = sourceIdStr.toLong()

                when (sourceType) {
//                    TransactionSourceType.ENVELOPE -> transactionSource = getEnvelopeByIDUseCase(sourceId)
                    TransactionSourceType.ACCOUNT -> transactionSource = getAccountByIDUseCase(sourceId)
                }
            }

            val auxSource = transactionSource?.toAuxDomain()
            loadInitialData(auxSource)

            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd")
            val currentDate = sdf.format(java.util.Date())
            _transactionUIState.update {
                it.copy(
                    transactionDate = currentDate
                )
            }
        }
    }

    private fun loadInitialData(defaultSource: SimpleTransactionSourceAux?) {
        viewModelScope.launch {
            val allSources = getTransactionSourceListUseCase() ?: emptyList()
            val categories = getCategoryListUseCase()
            _transactionUIState.update {
                it.copy(
                    originSource = defaultSource,
                    transactionSources = allSources,
                    categories = categories!!,
                    isLoading = false
                )
            }
        }
    }

    /**
     * Handle events from the account form
     */
    fun onTransactionFormEvent(event: TransactionFormEvent) {
        when (event) {
            is TransactionFormEvent.OnAmountChange -> {
                val regex = Regex("^\\d*\\.?\\d{0,2}$")
                _transactionUIState.update { currentState ->
                    val newAmount = event.value
                    var newDestinationAmount = currentState.destinationAmount
                    var newAdjustmentAmount = currentState.adjustmentAmount

                    if (currentState.isCrossCurrencyTransaction) {
                        val amount = newAmount
                        val exchangeRate = currentState.exchangeRate
                        val destinationAmount = amount * exchangeRate
                        newDestinationAmount = destinationAmount
                    }

                    val realAmountValue = newAmount
                    val currentBalance = currentState.originSource!!.balance
                    newAdjustmentAmount = realAmountValue - currentBalance


                    currentState.copy(
                        amount = newAmount,
                        destinationAmount = newDestinationAmount,
                        adjustmentAmount = newAdjustmentAmount,
                        errors = currentState.errors - "amount"
                    )
                }
            }

            is TransactionFormEvent.OnDescriptionChange -> {
                _transactionUIState.value = _transactionUIState.value.copy(
                    description = event.value,
                    errors = _transactionUIState.value.errors - "description"
                )
            }

            is TransactionFormEvent.OnDestinationSourceChange -> {
                _transactionUIState.update { currentState ->
                    val newDestinationSource = event.value
                    val sourceCurrency = currentState.originSource?.currency
                    val destinationCurrency = newDestinationSource?.currency

                    val isCrossCurrency = sourceCurrency != null && destinationCurrency != null && sourceCurrency.id != destinationCurrency.id

                    var exchangeRate = 1.0
                    var newDestinationAmount = currentState.destinationAmount

                    if (isCrossCurrency) {
                        exchangeRate = (destinationCurrency.exchangeRate / sourceCurrency.exchangeRate)
                        val amount = currentState.amount
                        val newExchangeRateValue = exchangeRate
                        val destinationAmount = amount * newExchangeRateValue
                        newDestinationAmount = destinationAmount
                    } else {
                        newDestinationAmount = currentState.amount
                    }

                    currentState.copy(
                        destinationSource = newDestinationSource,
                        errors = currentState.errors - "destinationAccount",
                        isCrossCurrencyTransaction = isCrossCurrency,
                        exchangeRate = exchangeRate,
                        destinationAmount = newDestinationAmount
                    )
                }
            }

            is TransactionFormEvent.OnOriginSourceChange -> {
                _transactionUIState.update { currentState ->
                    val newOriginSource = event.value
                    val sourceCurrency = newOriginSource?.currency
                    val destinationCurrency = currentState.destinationSource?.currency

                    val isCrossCurrency = sourceCurrency != null && destinationCurrency != null && sourceCurrency.id != destinationCurrency.id

                    var exchangeRate = 1.0
                    var newDestinationAmount = currentState.destinationAmount

                    if (isCrossCurrency) {
                        exchangeRate = destinationCurrency.exchangeRate / sourceCurrency.exchangeRate
                        val amount = currentState.amount
                        val newExchangeRateValue = exchangeRate
                        val destinationAmount = amount * newExchangeRateValue
                        newDestinationAmount = destinationAmount
                    } else {
                        newDestinationAmount = currentState.amount
                    }

                    currentState.copy(
                        originSource = newOriginSource,
                        errors = currentState.errors - "sourceAccount",
                        isCrossCurrencyTransaction = isCrossCurrency,
                        exchangeRate = exchangeRate,
                        destinationAmount = newDestinationAmount
                    )
                }
            }

            is TransactionFormEvent.OnExchangeRateChange -> {
                val regex = Regex("^\\d*\\.?\\d*$")
                _transactionUIState.update { currentState ->
                    val newExchangeRate = event.value

                    val amount = currentState.amount
                    val exchangeRate = newExchangeRate
                    val destinationAmount = amount * exchangeRate
                    val newDestinationAmount = destinationAmount

                    currentState.copy(
                        exchangeRate = newExchangeRate,
                        destinationAmount = newDestinationAmount
                    )
                }
            }

            is TransactionFormEvent.OnDestinationAmountChange -> {
                _transactionUIState.update { currentState ->
                    val newDestinationAmount = event.value
                    var newExchangeRate = currentState.exchangeRate

                    val amount = currentState.amount
                    val destinationAmount = newDestinationAmount
                    if (amount > 0) {
                        val exchangeRate = destinationAmount / amount
                        newExchangeRate = exchangeRate
                    }

                    currentState.copy(
                        destinationAmount = newDestinationAmount,
                        exchangeRate = newExchangeRate
                    )
                }
            }

            is TransactionFormEvent.OnDateChange -> {
                _transactionUIState.update {
                    it.copy(
                        transactionDate = event.value,
                        errors = it.errors - "transactionDate"
                    )
                }
            }

            is TransactionFormEvent.OnCategoryChange -> {
                _transactionUIState.update {
                    it.copy(
                        category = event.value,
                        errors = it.errors - "category"
                    )
                }
            }

            TransactionFormEvent.Reset -> {
                cleanForm()
            }

            is TransactionFormEvent.Submit -> {
                if (_transactionUIState.value.isEditing) updateTransaction(event.transactionType)
                else saveTransaction(event.transactionType)
            }
        }
    }

    /**
     * Clean form data to new entries
     */
    fun cleanForm() {
        _transactionUIState.value = _transactionUIState.value.copy(
            id = null,
            originSource = null,
            destinationSource = null,
            amount = 0.0,
            description = "",
            errors = emptyMap()
        )
    }

    fun validateFormFields(transactionType: TransactionType): Boolean {
        val errors = mutableMapOf<String, String>()
        val state = _transactionUIState.value

        if (state.originSource == null) {
            errors["sourceAccount"] = "Debe seleccionar una cuenta de origen."
        }

        val amount = state.amount
        if (amount <= 0.0) {
            errors["amount"] = "El monto debe ser mayor a cero."
        }

        if (transactionType == TransactionType.TRANSFER) {
            if (state.destinationSource == null) {
                errors["destinationAccount"] = "Debe seleccionar una cuenta de destino."
            }

            if (state.originSource != null && state.destinationSource != null && state.originSource.id == state.destinationSource.id) {
                errors["destinationAccount"] = "La cuenta de destino no puede ser la misma que la de origen."
            }
        }

        if (state.description.isBlank()) {
            errors["description"] = "La descripción es obligatoria."
        }

        _transactionUIState.update { it.copy(errors = errors) }
        return errors.isEmpty()
    }

    /**
     * Save the account to the database
     */
    fun saveTransaction(transactionType: TransactionType) {
        viewModelScope.launch {
            try {
                //START LOADING
                _transactionUIState.value = _transactionUIState.value.copy(
                    isSaving = true
                )

                //VALIDATE FIEDLS
                var validForm = validateFormFields(transactionType)
                if (!validForm) {
                    Log.e("TransactionViewModel", "Formulario Invalido")
                    return@launch
                }

                var originTransaction: TransactionDomain? = null;
                var destinationTransaction: TransactionDomain? = null;

                var amount: Double = 0.0;
                if (transactionType == TransactionType.ADJUSTMENT)
                    amount = _transactionUIState.value.adjustmentAmount!!.toDouble()
                else
                    amount = _transactionUIState.value.amount.toDouble()

                //MAP TRANSACTION SOURCE TO DOMAIN ENTITY
                originTransaction = TransactionDomain(
                    transactionCode = UUID.randomUUID(),
                    source = _transactionUIState.value.originSource!!,
                    amount = amount,
                    amountInBaseCurrency = 0.0,
                    transactionType = transactionType,
                    transactionDate = java.text.SimpleDateFormat("yyyy-MM-dd").parse(_transactionUIState.value.transactionDate),
                    currency = _transactionUIState.value.originSource!!.currency,
                    exchangeRate = _transactionUIState.value.originSource!!.currency.exchangeRate,
                    description = _transactionUIState.value.description,
                    category = _transactionUIState.value.category!!
                )

                //MAP TRANSACTION DESTINATION TO DOMAIN ENTITY
                if (transactionType == TransactionType.TRANSFER) {
                    destinationTransaction = TransactionDomain(
                        transactionCode = UUID.randomUUID(),
                        source = _transactionUIState.value.destinationSource!!,
                        transactionType = transactionType,
                        transactionDate = java.text.SimpleDateFormat("yyyy-MM-dd").parse(_transactionUIState.value.transactionDate),
                        currency = _transactionUIState.value.destinationSource!!.currency,
                        exchangeRate = _transactionUIState.value.destinationSource!!.currency.exchangeRate,
                        description = _transactionUIState.value.description,
                        category = _transactionUIState.value.category!!
                    )
                }

                //CREATE TRANSACTION
                createTransactionUseCase(
                    originTransaction,
                    _transactionUIState.value.originSource!!,
                    destinationTransaction,
                    _transactionUIState.value.destinationSource,
                    if (_transactionUIState.value.isCrossCurrencyTransaction) _transactionUIState.value.exchangeRate.toDouble() else 1.0
                )

                //NOTIFY SUCCESS CREATION
                _uiEvent.send(UIEvent.Success)
            } catch (e: Exception) {
                Log.e("TransactionViewModel", e.message.toString())

                //NOTIFY ERROR CREATION
                _uiEvent.send(UIEvent.ShowError("Error al Crear Transaccion"))

            } finally {
                //FINISH LOADING
                _transactionUIState.value = _transactionUIState.value.copy(isSaving = false)
            }
        }
    }

    /**
     * Update the transaction in the database
     */
    private fun updateTransaction(transactionType: TransactionType) {
        TODO()
//        viewModelScope.launch {
//            try {
//                //START LOADING
//                _transactionUIState.value = _transactionUIState.value.copy(
//                    isSaving = true
//                )
//
//                //VALIDATE FIEDLS
//                var validForm = validateFormFields(transactionType)
//                if (!validForm) {
//                    Log.e("TransactionViewModel", "Formulario Invalido")
//                    return@launch
//                }
//
//                var originTransaction:TransactionDomain? = null;
//                var destinationTransaction:TransactionDomain? = null;
//
//                //MAP TRANSACTION SOURCE TO DOMAIN ENTITY
//                originTransaction = TransactionDomain(
//                    transactionCode = UUID.randomUUID(),
//                    source = _transactionUIState.value.sourceAccount!!.toAuxDomain(),
//                    amount = _transactionUIState.value.amount.toDouble(),
//                    amountInBaseCurrency = 0.0,
//                    transactionType = transactionType,
//                    transactionDate = Date(),
//                    currency = _transactionUIState.value.sourceAccount!!.currency,
//                    exchangeRate = _transactionUIState.value.sourceAccount!!.currency.exchangeRate,
//                    description = _transactionUIState.value.description
//                )
//
//                //MAP TRANSACTION DESTINATION TO DOMAIN ENTITY
//                if(transactionType == TransactionType.TRANSFER){
//                    destinationTransaction = TransactionDomain(
//                        transactionCode = UUID.randomUUID(),
//                        source = _transactionUIState.value.destinationAccount!!.toAuxDomain(),
//                        transactionType = transactionType,
//                        transactionDate = Date(),
//                        currency = _transactionUIState.value.destinationAccount!!.currency,
//                        exchangeRate = _transactionUIState.value.destinationAccount!!.currency.exchangeRate,
//                        description = _transactionUIState.value.description
//                    )
//                }
//
//                //CREATE TRANSACTION
//                updateTransactionUseCase(
//                    originTransaction,
//                    _transactionUIState.value.sourceAccount!!,
//                    destinationTransaction,
//                    _transactionUIState.value.destinationAccount,
//                    if(_transactionUIState.value.isCrossCurrencyTransaction) _transactionUIState.value.exchangeRate.toDouble() else 1.0
//                )
//
//                //NOTIFY SUCCESS CREATION
//                _uiEvent.send(UIEvent.Success)
//            } catch (e: Exception) {
//                Log.e("TransactionViewModel", e.message.toString())
//
//                //NOTIFY ERROR CREATION
//                _uiEvent.send(UIEvent.ShowError("Error al Crear Transaccion"))
//
//            } finally {
//                //FINISH LOADING
//                _transactionUIState.value = _transactionUIState.value.copy(isSaving = false)
//            }
//        }
    }

}
