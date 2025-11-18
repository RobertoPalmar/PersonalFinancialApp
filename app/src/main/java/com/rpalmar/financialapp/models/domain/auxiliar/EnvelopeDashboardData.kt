package com.rpalmar.financialapp.models.domain.auxiliar

import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.EnvelopeDomain

data class EnvelopeDashboardData(
    var envelopeList: List<EnvelopeDomain>,
    var mainCurrency: CurrencyDomain,
    var totalEnvelopeBalance:Double
)