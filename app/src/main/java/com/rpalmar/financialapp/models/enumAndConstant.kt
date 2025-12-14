package com.rpalmar.financialapp.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.rpalmar.financialapp.models.database.CategoryEntity
import com.rpalmar.financialapp.models.database.StyleEntity
import com.rpalmar.financialapp.views.ui.components.toHex
import com.rpalmar.financialapp.views.ui.theme.AccentYellow
import com.rpalmar.financialapp.views.ui.theme.Blue
import com.rpalmar.financialapp.views.ui.theme.Cyan
import com.rpalmar.financialapp.views.ui.theme.Green
import com.rpalmar.financialapp.views.ui.theme.Red
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.ExchangeAltSolid
import compose.icons.lineawesomeicons.MinusSquareSolid
import compose.icons.lineawesomeicons.PlusSquareSolid
import compose.icons.lineawesomeicons.WrenchSolid

object Constants {
    //ROOM DB
    const val ROOM_DB_NAME = "FINANCIAL_DB"

    val ADJUSTMENT_CATEGORY = CategoryEntity(
        id = -1,
        type = CategoryType.TRANSACTION,
        name = "Adjustment",
        style = StyleEntity(
            stringColor = AccentYellow.toHex(),
            icon = LineAwesomeIcons.WrenchSolid.name
        ),
        isDelete = false,
        isBaseCategory = true
    )

    val TRANSFER_CATEGORY = CategoryEntity(
        id = -2,
        type = CategoryType.TRANSACTION,
        name = "Transfer",
        style = StyleEntity(
            stringColor = Cyan.toHex(),
            icon = LineAwesomeIcons.ExchangeAltSolid.name
        ),
        isDelete = false,
        isBaseCategory = true
    )
}

enum class EnvelopStatus {
    ACTIVE,
    BLOCKED,
    COMPLETED,
    CLOSED,
    INACTIVE
}

enum class GoalType {
    MONTHLY,
    ANNUAL,
    SPECIFIC
}

enum class TransactionSourceType{
    ACCOUNT,
//    ENVELOPE,
}

enum class ButtonType{
    PRIMARY,
    SECONDARY,
    OUTLINE
}

enum class TransactionType{
    INCOME,
    EXPENSE,
    TRANSFER,
    ADJUSTMENT;

    fun resolveIcon(): ImageVector {
        val icon = when (this) {
            TransactionType.INCOME -> LineAwesomeIcons.PlusSquareSolid
            TransactionType.EXPENSE -> LineAwesomeIcons.MinusSquareSolid
            TransactionType.TRANSFER -> LineAwesomeIcons.ExchangeAltSolid
            TransactionType.ADJUSTMENT -> LineAwesomeIcons.WrenchSolid
        }
        return icon;
    }

    fun resolveLabel(): String {
        val label = when (this) {
            TransactionType.INCOME -> "Ingreso"
            TransactionType.EXPENSE -> "Gasto"
            TransactionType.TRANSFER -> "Transferencia"
            TransactionType.ADJUSTMENT -> "Ajuste"
        }
        return label;
    }

    fun resolveColor(): Color{
        val color = when (this) {
            TransactionType.INCOME -> Green
            TransactionType.EXPENSE -> Red
            TransactionType.TRANSFER -> Blue
            TransactionType.ADJUSTMENT -> AccentYellow
        }
        return color;
    }
}

enum class ExchangeRateApi{
    BCV_API,
    FRANKFURTER_API
}

enum class RateMode {
    MANUAL,
    AUTO
}

enum class ExchangeRateType{
    MANUAL,
    API
}

enum class CategoryType{
    TRANSACTION
}

enum class NavScreen{
    HOME,
    STATS,
    SETTINGS
}

enum class SummaryAnimationDirection {
    FORWARD,
    BACKWARD,
    NONE
}

