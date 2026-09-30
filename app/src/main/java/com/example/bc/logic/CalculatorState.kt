package com.example.bc.logic

data class CalculatorState(
    val expression: String = "",
    val liveResult: String = "",
    val isEvaluated: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
) {
    /**
     * Display baris atas (tidak ada preview jawaban sebelum berlangganan)
     */
    val topDisplay: String
        get() = when {
            isError -> expression
            else -> ""
        }

    /**
     * Display baris utama: Menampilkan rumus ekspresi yang sedang diketik tanpa bocoran jawaban,
     * dan menampilkan hasil setelah tombol '=' ditekan dan dibayar.
     */
    val bottomDisplay: String
        get() = when {
            isError -> errorMessage
            isEvaluated && expression.isNotEmpty() -> expression
            expression.isNotEmpty() -> expression
            else -> "0"
        }
}
