package com.rpalmar.financialapp.models

object enumAndConstant {
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
    ENVELOP,
    EXTERNAL_INCOME
}