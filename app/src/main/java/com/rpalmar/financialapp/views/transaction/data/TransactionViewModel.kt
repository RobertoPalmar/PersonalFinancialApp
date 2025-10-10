package com.rpalmar.financialapp.views.transaction.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.usecases.account.GetAccountByIDUseCase
import com.rpalmar.financialapp.usecases.account.GetAccountsListUseCase
import com.rpalmar.financialapp.usecases.transaction.CreateTransactionUseCase
import com.rpalmar.financialapp.views.ui.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import androidx.lifecycle.SavedStateHandle
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.models.interfaces.IDomainTransaction
import com.rpalmar.financialapp.usecases.envelope.GetEnvelopeByIDUseCase
import com.rpalmar.financialapp.usecases.transaction.GetTransactionSourceListUseCase
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getAccountByIDUseCase: GetAccountByIDUseCase,
    private val getEnvelopeByIDUseCase: GetEnvelopeByIDUseCase,
    private val getTransactionSourceListUseCase: GetTransactionSourceListUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
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

                when(sourceType){
                    TransactionSourceType.ENVELOPE -> transactionSource = getEnvelopeByIDUseCase(sourceId)
                    TransactionSourceType.ACCOUNT -> transactionSource = getAccountByIDUseCase(sourceId)
                }
            }

            val auxSource = transactionSource!!.toAuxDomain()
            loadInitialData(auxSource)
        }
    }

    private fun loadInitialData(defaultSource: SimpleTransactionSourceAux?) {
        viewModelScope.launch {
            val allSources = getTransactionSourceListUseCase() ?: emptyList()
            _transactionUIState.update {
                it.copy(
                    originSource = defaultSource,
                    transactionSources = allSources,
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
                if (event.value.isEmpty() || event.value.matches(regex)) {
                    _transactionUIState.update {
                        it.copy(
                            amount = event.value,
                            errors = _transactionUIState.value.errors - "amount"
                        )
                    }
                    if(_transactionUIState.value.isCrossCurrencyTransaction){
                        recalculateDestinationAmount()
                    }

                    val realAmountValue = _transactionUIState.value.amount.toDoubleOrNull() ?: 0.0
                    val currentBalance = _transactionUIState.value.originSource?.balance ?: 0.0
                    val adjustment = realAmountValue - currentBalance
                    _transactionUIState.update {
                        it.copy(
                            adjustmentAmount = adjustment
                        )
                    }
                }
            }
            is TransactionFormEvent.OnDescriptionChange -> {
                _transactionUIState.value = _transactionUIState.value.copy(
                    description = event.value,
                    errors = _transactionUIState.value.errors - "description"
                )
            }
            is TransactionFormEvent.OnDestinationSourceChange -> {
                _transactionUIState.update {
                    it.copy(
                        destinationSource = event.value,
                        errors = _transactionUIState.value.errors - "destinationAccount"
                    )
                }
                checkCrossCurrencyAndUpdate()
            }
            is TransactionFormEvent.OnOriginSourceChange -> {
                _transactionUIState.update {
                    it.copy(
                        originSource = event.value,
                        errors = _transactionUIState.value.errors - "sourceAccount"
                    )
                }
                checkCrossCurrencyAndUpdate()
            }
            is TransactionFormEvent.OnExchangeRateChange -> {
                val regex = Regex("^\\d*\\.?\\d*$")
                if (event.value.isEmpty() || event.value.matches(regex)) {
                    _transactionUIState.update {
                        it.copy(exchangeRate = event.value)
                    }
                    recalculateDestinationAmount()
                }
            }
            is TransactionFormEvent.OnDestinationAmountChange -> {
                val regex = Regex("^\\d*\\.?\\d{0,2}$")
                if (event.value.isEmpty() || event.value.matches(regex)) {
                    _transactionUIState.update {
                        it.copy(destinationAmount = event.value)
                    }
                    recalculateExchangeRate()
                }
            }
            TransactionFormEvent.Reset -> {
                cleanForm()
            }
            is TransactionFormEvent.Submit -> {
                if(_transactionUIState.value.isEditing) updateTransaction(event.transactionType)
                else saveTransaction(event.transactionType)
            }
        }
    }

    /**
     * Validate if the transaction is a cross currency transaction
     */
    private fun checkCrossCurrencyAndUpdate() {
        val sourceCurrency = _transactionUIState.value.originSource?.currency
        val destinationCurrency = _transactionUIState.value.destinationSource?.currency

        val isCrossCurrency = sourceCurrency != null && destinationCurrency != null && sourceCurrency.id != destinationCurrency.id

        var exchangeRate = "1.0"
        if (isCrossCurrency) {
            exchangeRate = (destinationCurrency!!.exchangeRate / sourceCurrency!!.exchangeRate).toString()
        }

        _transactionUIState.update {
            it.copy(
                isCrossCurrencyTransaction = isCrossCurrency,
                exchangeRate = exchangeRate
            )
        }

        if (isCrossCurrency) {
            recalculateDestinationAmount()
        }
    }

    /**
     * Recalculate the destination amount based on the exchange rate
     */
    private fun recalculateDestinationAmount() {
        val amount = _transactionUIState.value.amount.toDoubleOrNull() ?: 0.0
        val exchangeRate = _transactionUIState.value.exchangeRate.toDoubleOrNull() ?: 1.0
        val destinationAmount = amount * exchangeRate
        _transactionUIState.update {
            it.copy(destinationAmount = String.format("%.2f", destinationAmount))
        }
    }

    /**
     * Recalculate the exchange rate based on the destination amount
     */
    private fun recalculateExchangeRate() {
        val amount = _transactionUIState.value.amount.toDoubleOrNull() ?: 0.0
        val destinationAmount = _transactionUIState.value.destinationAmount.toDoubleOrNull() ?: 0.0
        if (amount > 0) {
            val exchangeRate = destinationAmount / amount
            _transactionUIState.update {
                it.copy(exchangeRate = exchangeRate.toString())
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
            amount = "",
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

        val amount = state.amount.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
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

                var originTransaction:TransactionDomain? = null;
                var destinationTransaction:TransactionDomain? = null;

                var amount:Double = 0.0;
                if(transactionType == TransactionType.ADJUSTMENT)
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
                    transactionDate = Date(),
                    currency = _transactionUIState.value.originSource!!.currency,
                    exchangeRate = _transactionUIState.value.originSource!!.currency.exchangeRate,
                    description = _transactionUIState.value.description
                )

                //MAP TRANSACTION DESTINATION TO DOMAIN ENTITY
                if(transactionType == TransactionType.TRANSFER){
                    destinationTransaction = TransactionDomain(
                        transactionCode = UUID.randomUUID(),
                        source = _transactionUIState.value.destinationSource!!,
                        transactionType = transactionType,
                        transactionDate = Date(),
                        currency = _transactionUIState.value.destinationSource!!.currency,
                        exchangeRate = _transactionUIState.value.destinationSource!!.currency.exchangeRate,
                        description = _transactionUIState.value.description
                    )
                }

                //CREATE TRANSACTION
                createTransactionUseCase(
                    originTransaction,
                    _transactionUIState.value.originSource!!,
                    destinationTransaction,
                    _transactionUIState.value.destinationSource,
                    if(_transactionUIState.value.isCrossCurrencyTransaction) _transactionUIState.value.exchangeRate.toDouble() else 1.0
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
    private fun updateTransaction(transactionType: TransactionType){
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
