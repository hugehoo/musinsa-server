package com.musinsa.backend.common

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class Utils {

    companion object {

        private val wonPriceFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
        fun formatWonPrice(price: BigDecimal): String {
            return wonPriceFormat.format(price)
        }
    }
}