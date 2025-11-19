package com.rpalmar.financialapp.models

object Constants {
    //ROOM DB
    const val ROOM_DB_NAME = "FINANCIAL_DB"
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
    ADJUSTMENT
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
