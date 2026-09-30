package com.example.bc.logic

data class CalculatorState(
    val expression: String = "",
    val liveResult: String = "",
    val isEvaluated: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
) {
    /**
     * Display baris atas: Menampilkan rumus / ekspresi perhitungan yang sedang diketik
     * (misal "888 × 9"). Saat ditekan '=', operasi hilang (menjadi kosong).
     */
    val topDisplay: String
        get() = when {
            isError -> expression
            isEvaluated -> "" // Rumus hilang setelah klik '='
            expression.contains(" ") || expression.contains("+") || expression.contains("−") || expression.contains("×") || expression.contains("÷") -> expression
            else -> ""
        }

    /**
     * Display baris bawah: Menampilkan hasil perhitungan (live preview atau hasil final atau angka aktif).
     */
    val bottomDisplay: String
        get() = when {
            isError -> errorMessage
            isEvaluated && expression.isNotEmpty() -> expression
            isEvaluated && liveResult.isNotEmpty() -> liveResult
            expression.contains(" ") && liveResult.isNotEmpty() -> liveResult
            expression.isNotEmpty() -> expression
            else -> "0"
        }
}


