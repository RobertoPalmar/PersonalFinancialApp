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
import com.rpalmar.financialapp.models.Constants
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.usecases.category.GetCategoryListUseCase
import com.rpalmar.financialapp.usecases.transaction.DeleteTransactionUseCase
import com.rpalmar.financialapp.usecases.transaction.GetTransactionSourceListUseCase
import com.rpalmar.financialapp.usecases.transaction.UpdateTransactionUseCase
import com.rpalmar.financialapp.utils.Utils.formatDouble
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getAccountByIDUseCase: GetAccountByIDUseCase,
    private val getTransactionSourceListUseCase: GetTransactionSourceListUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase
) : ViewModel() {

    //UI STATE
    private val _transactionUIState = MutableStateFlow(TransactionUiState())
    val transactionUIState: StateFlow<TransactionUiState> = _transactionUIState.asStateFlow()

    //UI EVENT LISTENER
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun loadInitialData(sourceID: Long?) {
        viewModelScope.launch {
            //DEFAULT ACCOUNT
            var originSource: SimpleTransactionSourceAux? = null;
            if (sourceID != null) {
                var transactionSource = getAccountByIDUseCase(sourceID)
                originSource = transactionSource?.toAuxDomain()
            }

            //ACCOUNT SOURCES
            val allSources = getTransactionSourceListUseCase() ?: emptyList()

            //CATEGORIES
            val allCategories = getCategoryListUseCase();
            val validCategories = allCategories!!.filter { !it.isBaseCategory }


            //CURRENT DATE DEFAULT
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val currentDate = dateFormat.format(Date())

            _transactionUIState.update {
                it.copy(
                    transactionSources = allSources,
                    categories = validCategories,
                    transactionDate = currentDate,
                    originSource = originSource,
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
                _transactionUIState.update {
                    it.copy(
                        amount = event.value,
                        errors = _transactionUIState.value.errors - "amount"
                    )
                }
                if (_transactionUIState.value.isCrossCurrencyTransaction) {
                    recalculateDestinationAmount()
                }
                calculateAdjustmentAmount()
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
                calculateAdjustmentAmount()
            }

            is TransactionFormEvent.OnExchangeRateChange -> {
                _transactionUIState.update {
                    it.copy(exchangeRate = event.value)
                }
                recalculateDestinationAmount()
            }

            is TransactionFormEvent.OnDestinationAmountChange -> {
                _transactionUIState.update {
                    it.copy(destinationAmount = event.value)
                }
                recalculateExchangeRate()
            }

            is TransactionFormEvent.Reset -> {
                cleanForm()
            }

            is TransactionFormEvent.Submit -> {
                if (_transactionUIState.value.isEditing) updateTransaction(event.transactionType)
                else saveTransaction(event.transactionType)
            }

            is TransactionFormEvent.OnCategoryChange -> {
                _transactionUIState.update {
                    it.copy(
                        category = event.value,
                        errors = it.errors - "category"
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
        }
    }

    /**
     * Recalculate the exchange rate based on the destination amount
     */
    private fun recalculateExchangeRate() {
        val amount = _transactionUIState.value.amount
        val destinationAmount = _transactionUIState.value.destinationAmount
        if (amount > 0) {
            val exchangeRate = destinationAmount / amount
            val finalExchangeRate = formatDouble(exchangeRate, 5)
            _transactionUIState.update {
                it.copy(exchangeRate = finalExchangeRate)
            }
        }
    }

    /**
     * Recalculate the destination amount based on the exchange rate
     */
    private fun recalculateDestinationAmount() {
        val amount = _transactionUIState.value.amount
        val exchangeRate = _transactionUIState.value.exchangeRate
        val destinationAmount = amount * exchangeRate
        val finalDestinationAmount = formatDouble(destinationAmount)
        _transactionUIState.update {
            it.copy(destinationAmount = finalDestinationAmount)
        }
    }

    private fun calculateAdjustmentAmount() {
        val realAmountValue = _transactionUIState.value.amount
        val currentBalance = _transactionUIState.value.originSource?.balance ?: 0.0
        val adjustment = realAmountValue - currentBalance
        val finalAdjustment = formatDouble(adjustment)
        _transactionUIState.update {
            it.copy(adjustmentAmount = finalAdjustment)
        }
    }

    /**
     * Validate if the transaction is a cross currency transaction
     */
    private fun checkCrossCurrencyAndUpdate() {
        val sourceCurrency = _transactionUIState.value.originSource?.currency
        val destinationCurrency = _transactionUIState.value.destinationSource?.currency

        val isCrossCurrency = sourceCurrency != null && destinationCurrency != null && sourceCurrency.id != destinationCurrency.id

        var exchangeRate = 1.0
        if (isCrossCurrency) {
            // El exchangeRate representa cuántas unidades de moneda destino equivalen a 1 unidad de moneda origen
            // Si sourceCurrency.exchangeRate = 1.0 (USD base) y destinationCurrency.exchangeRate = 0.85 (EUR)
            // Entonces: 1 USD = 1.0/0.85 = 1.176 EUR
            exchangeRate = sourceCurrency.exchangeRate / destinationCurrency.exchangeRate
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
     * Set the transaction type in the UI state
     */
    fun setTransactionType(transactionType: TransactionType) {
        _transactionUIState.update {
            it.copy(transactionType = transactionType)
        }
    }

    /**
     * Clean form data to new entries
     */
    fun cleanForm(transactionType: TransactionType? = null) {
        val typeToSet = transactionType ?: _transactionUIState.value.transactionType

        _transactionUIState.value = _transactionUIState.value.copy(
            id = null,
            originSource = null,
            destinationSource = null,
            exchangeRate = 0.0,
            amount = 0.0,
            destinationAmount = 0.0,
            description = "",
            transactionType = typeToSet,
            errors = emptyMap()
        )
    }

    /**
     * Set the transaction type in the UI state
     */
    fun prepareCreateFromAccount(
        transactionType: TransactionType,
        accountId: Long?
    ) {
        viewModelScope.launch {
            cleanForm()

            if (accountId != null) {
                val account = getAccountByIDUseCase(accountId)
                _transactionUIState.value = _transactionUIState.value.copy(
                    transactionType = transactionType,
                    originSource = account!!.toAuxDomain()
                )
            } else {
                _transactionUIState.value = _transactionUIState.value.copy(
                    transactionType = transactionType
                )
            }

        }
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

                //SET AMOUNT
                var amount: Double = 0.0;
                if (transactionType == TransactionType.ADJUSTMENT)
                    amount = _transactionUIState.value.adjustmentAmount
                else
                    amount = _transactionUIState.value.amount

                //SET CATEGORY
                var category: CategoryDomain;
                when (transactionType) {
                    TransactionType.INCOME,
                    TransactionType.EXPENSE ->
                        category = _transactionUIState.value.category!!

                    TransactionType.ADJUSTMENT ->
                        category = Constants.ADJUSTMENT_CATEGORY.toDomain()

                    TransactionType.TRANSFER ->
                        category = Constants.TRANSFER_CATEGORY.toDomain()
                }

                //MAP TRANSACTION SOURCE TO DOMAIN ENTITY
                originTransaction = TransactionDomain(
                    transactionCode = UUID.randomUUID(),
                    source = _transactionUIState.value.originSource!!,
                    amount = amount,
                    amountInBaseCurrency = 0.0,
                    transactionType = transactionType,
                    transactionDate = SimpleDateFormat("yyyy-MM-dd").parse(_transactionUIState.value.transactionDate),
                    currency = _transactionUIState.value.originSource!!.currency,
                    exchangeRate = _transactionUIState.value.originSource!!.currency.exchangeRate,
                    description = _transactionUIState.value.description,
                    category = category
                )

                //MAP TRANSACTION DESTINATION TO DOMAIN ENTITY
                if (transactionType == TransactionType.TRANSFER) {
                    destinationTransaction = TransactionDomain(
                        transactionCode = UUID.randomUUID(),
                        source = _transactionUIState.value.destinationSource!!,
                        transactionType = transactionType,
                        transactionDate = SimpleDateFormat("yyyy-MM-dd").parse(_transactionUIState.value.transactionDate),
                        currency = _transactionUIState.value.destinationSource!!.currency,
                        exchangeRate = _transactionUIState.value.destinationSource!!.currency.exchangeRate,
                        description = _transactionUIState.value.description,
                        category = category
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
     * Delete transaction and set null his UI state
     */
    fun handleDeleteTransaction(transactionID: Long) {
        viewModelScope.launch {
            deleteTransactionUseCase(transactionID)
        }
    }

    /**
     * Set the current transaction fields to update
     */
    fun handleUpdateTransactionForm(transaction: TransactionDomain) {
        viewModelScope.launch {
            _transactionUIState.value = _transactionUIState.value.copy(
                id = transaction.id,
                originSource = transaction.source,
                destinationSource = transaction.linkedTransaction?.source,
                exchangeRate = transaction.exchangeRate,
                amount = abs(transaction.amount),
                description = transaction.description,
                transactionType = transaction.transactionType,
                category = transaction.category,
                errors = emptyMap(),
                isEditing = true
            )
        }
    }

    /**
     * Update the transaction in the database
     */
    private fun updateTransaction(transactionType: TransactionType) {
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

                //MAP TRANSACTION SOURCE TO DOMAIN ENTITY
                originTransaction = TransactionDomain(
                    transactionCode = UUID.randomUUID(),
                    source = _transactionUIState.value.originSource!!,
                    amount = _transactionUIState.value.amount.toDouble(),
                    amountInBaseCurrency = 0.0,
                    transactionType = transactionType,
                    transactionDate = Date(),
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
                        transactionDate = Date(),
                        currency = _transactionUIState.value.destinationSource!!.currency,
                        exchangeRate = _transactionUIState.value.destinationSource!!.currency.exchangeRate,
                        description = _transactionUIState.value.description,
                        category = _transactionUIState.value.category!!
                    )
                }

                //CREATE TRANSACTION
                updateTransactionUseCase(
                    _transactionUIState.value.id!!,
                    originTransaction,
                    _transactionUIState.value.originSource!!,
                    destinationTransaction,
                    _transactionUIState.value.destinationSource,
                    if (_transactionUIState.value.isCrossCurrencyTransaction) _transactionUIState.value.exchangeRate.toDouble() else 1.0
                )

                //NOTIFY SUCCESS UPDATE
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

}
