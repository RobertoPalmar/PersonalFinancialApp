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
    ENVELOPE,
    EXTERNAL_INCOME
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

