package market

import java.lang.Exception

enum class Currency(val abr: String, val value: Double) {
    USD("usd", 1.0),
    EUR("eur", 1.13),
    RUB("rub", 0.015);

    companion object {
        fun fromString(name: String): Currency {
            return when (name) {
                "usd" -> Currency.USD
                "eur" -> Currency.EUR
                "rub" -> Currency.RUB
                else -> throw Exception()
            }
        }
    }
}