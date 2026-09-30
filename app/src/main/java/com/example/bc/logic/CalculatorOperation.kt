package com.example.bc.logic

enum class CalculatorOperation(val symbol: String, val displaySymbol: String) {
    ADD("+", "+"),
    SUBTRACT("-", "−"),
    MULTIPLY("*", "×"),
    DIVIDE("/", "÷");

    companion object {
        fun fromSymbol(symbol: String): CalculatorOperation? {
            return entries.find { it.symbol == symbol || it.displaySymbol == symbol }
        }
    }
}
