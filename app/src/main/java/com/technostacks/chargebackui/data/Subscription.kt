package com.technostacks.chargebackui.data

data class Subscription(
    var service: Service? = null,
    var amount: Double = 0.0,
    var category: Category? = null,
    var startDate: String = "",
    var frequency: Frequency? = null,
    var isActive: Boolean = true
)