package com.example.bc.logic

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CalculatorEngine {

    private val MATH_CONTEXT = MathContext(12, RoundingMode.HALF_UP)

    fun onAction(currentState: CalculatorState, action: CalculatorAction): CalculatorState {
        return when (action) {
            is CalculatorAction.Number -> handleNumber(currentState, action.number)
            is CalculatorAction.Decimal -> handleDecimal(currentState)
            is CalculatorAction.Clear -> CalculatorState()
            is CalculatorAction.Delete -> handleDelete(currentState)
            is CalculatorAction.Operation -> handleOperation(currentState, action.operation)
            is CalculatorAction.Calculate -> handleCalculate(currentState)
            is CalculatorAction.ToggleSign -> handleToggleSign(currentState)
            is CalculatorAction.Percentage -> handlePercentage(currentState)
        }
    }

    private fun handleNumber(state: CalculatorState, number: Int): CalculatorState {
        if (state.isError || state.isEvaluated) {
            val newExpr = number.toString()
            return CalculatorState(
                expression = newExpr,
                liveResult = newExpr,
                isEvaluated = false
            )
        }

        val expr = state.expression
        val lastToken = getLastToken(expr)

        val newExpr = if (lastToken == "0") {
            // Replace standalone 0 with the new number
            expr.dropLast(1) + number
        } else {
            expr + number
        }

        val live = evaluateExpressionSafely(newExpr)
        return state.copy(
            expression = newExpr,
            liveResult = live ?: "",
            isEvaluated = false,
            isError = false
        )
    }

    private fun handleDecimal(state: CalculatorState): CalculatorState {
        if (state.isError || state.isEvaluated) {
            return CalculatorState(
                expression = "0.",
                liveResult = "0",
                isEvaluated = false
            )
        }

        val expr = state.expression
        if (expr.isEmpty()) {
            return state.copy(expression = "0.", liveResult = "0")
        }

        val lastToken = getLastToken(expr)
        if (isOperator(lastToken)) {
            val newExpr = "$expr 0."
            return state.copy(expression = newExpr)
        }

        if (lastToken.contains(".")) {
            // Already has decimal point in current number
            return state
        }

        val newExpr = "$expr."
        return state.copy(
            expression = newExpr,
            liveResult = evaluateExpressionSafely(newExpr) ?: state.liveResult
        )
    }

    private fun handleOperation(state: CalculatorState, op: CalculatorOperation): CalculatorState {
        var expr = if (state.isEvaluated && !state.isError && state.liveResult.isNotEmpty()) {
            state.liveResult
        } else if (state.isError) {
            ""
        } else {
            state.expression
        }

        if (expr.isEmpty()) {
            if (op == CalculatorOperation.SUBTRACT) {
                return CalculatorState(expression = "-")
            }
            return state
        }

        expr = expr.trimEnd()

        // Check if expression ends with an operator
        val lastChar = expr.takeLast(1)
        if (lastChar == "+" || lastChar == "−" || lastChar == "-" || lastChar == "×" || lastChar == "*" || lastChar == "÷" || lastChar == "/") {
            // Replace previous operator
            val trimmed = expr.dropLast(1).trimEnd()
            val newExpr = "$trimmed ${op.displaySymbol} "
            return state.copy(
                expression = newExpr,
                isEvaluated = false,
                isError = false
            )
        }

        if (expr.endsWith(".")) {
            expr = expr.dropLast(1)
        }

        val newExpr = "$expr ${op.displaySymbol} "
        val live = evaluateExpressionSafely(newExpr)
        return state.copy(
            expression = newExpr,
            liveResult = live ?: state.liveResult,
            isEvaluated = false,
            isError = false
        )
    }

    private fun handleDelete(state: CalculatorState): CalculatorState {
        if (state.isError || state.isEvaluated) {
            return CalculatorState()
        }

        var expr = state.expression.trimEnd()
        if (expr.isEmpty()) return state

        // If it ends with an operator and space, remove operator
        expr = if (expr.endsWith(" +") || expr.endsWith(" −") || expr.endsWith(" -") || expr.endsWith(" ×") || expr.endsWith(" ÷")) {
            expr.dropLast(2).trimEnd()
        } else {
            expr.dropLast(1).trimEnd()
        }

        val live = if (expr.isNotEmpty()) evaluateExpressionSafely(expr) else ""
        return state.copy(
            expression = expr,
            liveResult = live ?: "",
            isEvaluated = false,
            isError = false
        )
    }

    private fun handleCalculate(state: CalculatorState): CalculatorState {
        if (state.expression.isBlank()) return state
        val result = evaluateExpression(state.expression)
        return if (result.isSuccess) {
            val formatted = result.getOrNull() ?: "0"
            state.copy(
                expression = formatted,
                liveResult = formatted,
                isEvaluated = true,
                isError = false
            )
        } else {
            val errorMsg = result.exceptionOrNull()?.message ?: "Error"
            state.copy(
                isError = true,
                errorMessage = errorMsg,
                isEvaluated = false
            )
        }
    }


    private fun handleToggleSign(state: CalculatorState): CalculatorState {
        val expr = if (state.isEvaluated && state.liveResult.isNotEmpty()) {
            state.liveResult
        } else {
            state.expression
        }

        if (expr.isEmpty()) return state

        // Find last number token
        val lastSpaceIndex = expr.lastIndexOf(' ')
        if (lastSpaceIndex == -1) {
            // Entire expression is a single number
            val toggled = if (expr.startsWith("-")) expr.drop(1) else "-$expr"
            val live = evaluateExpressionSafely(toggled) ?: toggled
            return state.copy(
                expression = toggled,
                liveResult = live,
                isEvaluated = false
            )
        }

        val prefix = expr.substring(0, lastSpaceIndex + 1)
        val lastToken = expr.substring(lastSpaceIndex + 1)

        if (isOperator(lastToken) || lastToken.isEmpty()) {
            return state
        }

        val toggledToken = if (lastToken.startsWith("-")) {
            lastToken.drop(1)
        } else {
            "-$lastToken"
        }

        val newExpr = prefix + toggledToken
        val live = evaluateExpressionSafely(newExpr) ?: state.liveResult
        return state.copy(
            expression = newExpr,
            liveResult = live,
            isEvaluated = false
        )
    }

    private fun handlePercentage(state: CalculatorState): CalculatorState {
        val expr = if (state.isEvaluated && state.liveResult.isNotEmpty()) {
            state.liveResult
        } else {
            state.expression
        }

        if (expr.isEmpty()) return state

        val lastSpaceIndex = expr.lastIndexOf(' ')
        val lastToken = if (lastSpaceIndex == -1) expr else expr.substring(lastSpaceIndex + 1)

        if (isOperator(lastToken) || lastToken.isEmpty()) return state

        val num = lastToken.toDoubleOrNull() ?: return state
        val percentValue = BigDecimal.valueOf(num).divide(BigDecimal(100), MATH_CONTEXT)
        val formattedPercent = formatBigDecimal(percentValue)

        val newExpr = if (lastSpaceIndex == -1) {
            formattedPercent
        } else {
            expr.substring(0, lastSpaceIndex + 1) + formattedPercent
        }

        val live = evaluateExpressionSafely(newExpr) ?: formattedPercent
        return state.copy(
            expression = newExpr,
            liveResult = live,
            isEvaluated = false
        )
    }

    private fun getLastToken(expression: String): String {
        val trimmed = expression.trimEnd()
        val lastSpace = trimmed.lastIndexOf(' ')
        return if (lastSpace == -1) trimmed else trimmed.substring(lastSpace + 1)
    }

    private fun isOperator(token: String): Boolean {
        return token == "+" || token == "−" || token == "-" || token == "×" || token == "*" || token == "÷" || token == "/"
    }

    fun evaluateExpressionSafely(rawExpr: String): String? {
        val result = evaluateExpression(rawExpr)
        return if (result.isSuccess) result.getOrNull() else null
    }

    fun evaluateExpression(rawExpr: String): Result<String> {
        return runCatching {
            var expr = rawExpr.trim()
            if (expr.isEmpty()) return@runCatching "0"

            // Standardize symbols
            expr = expr.replace("×", "*")
                .replace("÷", "/")
                .replace("−", "-")

            // If expression ends with trailing operator, drop it for evaluation
            val tokens = tokenize(expr)
            if (tokens.isEmpty()) return@runCatching "0"

            // Filter out trailing operator tokens for partial preview
            val validTokens = if (tokens.isNotEmpty() && isOpToken(tokens.last())) {
                tokens.dropLast(1)
            } else {
                tokens
            }

            if (validTokens.isEmpty()) return@runCatching "0"

            val postfix = infixToPostfix(validTokens)
            val result = evaluatePostfix(postfix)
            formatBigDecimal(result)
        }
    }

    private fun isOpToken(token: String): Boolean =
        token == "+" || token == "-" || token == "*" || token == "/"

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        val sb = StringBuilder()
        var i = 0

        while (i < expr.length) {
            val c = expr[i]

            if (c.isWhitespace()) {
                i++
                continue
            }

            if (c == '+' || c == '*' || c == '/') {
                if (sb.isNotEmpty()) {
                    tokens.add(sb.toString())
                    sb.clear()
                }
                tokens.add(c.toString())
                i++
            } else if (c == '-') {
                // Check if minus is unary (negative number) or binary (subtract operator)
                val isUnary = tokens.isEmpty() && sb.isEmpty() ||
                        (tokens.isNotEmpty() && isOpToken(tokens.last()) && sb.isEmpty())
                if (isUnary) {
                    sb.append(c)
                } else {
                    if (sb.isNotEmpty()) {
                        tokens.add(sb.toString())
                        sb.clear()
                    }
                    tokens.add(c.toString())
                }
                i++
            } else {
                sb.append(c)
                i++
            }
        }

        if (sb.isNotEmpty()) {
            tokens.add(sb.toString())
        }

        return tokens
    }

    private fun precedence(op: String): Int {
        return when (op) {
            "*", "/" -> 2
            "+", "-" -> 1
            else -> 0
        }
    }

    private fun infixToPostfix(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val operatorStack = ArrayDeque<String>()

        for (token in tokens) {
            if (isOpToken(token)) {
                while (operatorStack.isNotEmpty() &&
                    isOpToken(operatorStack.first()) &&
                    precedence(operatorStack.first()) >= precedence(token)
                ) {
                    output.add(operatorStack.removeFirst())
                }
                operatorStack.addFirst(token)
            } else {
                output.add(token)
            }
        }

        while (operatorStack.isNotEmpty()) {
            output.add(operatorStack.removeFirst())
        }

        return output
    }

    private fun evaluatePostfix(postfix: List<String>): BigDecimal {
        val stack = ArrayDeque<BigDecimal>()

        for (token in postfix) {
            if (isOpToken(token)) {
                if (stack.size < 2) {
                    throw IllegalArgumentException("Format tidak valid")
                }
                val b = stack.removeFirst()
                val a = stack.removeFirst()

                val res = when (token) {
                    "+" -> a.add(b, MATH_CONTEXT)
                    "-" -> a.subtract(b, MATH_CONTEXT)
                    "*" -> a.multiply(b, MATH_CONTEXT)
                    "/" -> {
                        if (b.compareTo(BigDecimal.ZERO) == 0) {
                            throw ArithmeticException("Tidak bisa dibagi 0")
                        }
                        a.divide(b, MATH_CONTEXT)
                    }
                    else -> BigDecimal.ZERO
                }
                stack.addFirst(res)
            } else {
                val num = token.toDoubleOrNull() ?: throw IllegalArgumentException("Format angka salah")
                stack.addFirst(BigDecimal.valueOf(num))
            }
        }

        if (stack.size != 1) {
            throw IllegalArgumentException("Format tidak valid")
        }

        return stack.first()
    }

    private fun formatBigDecimal(value: BigDecimal): String {
        // Strip trailing zeros after decimal point
        val stripped = value.stripTrailingZeros()
        val plainString = stripped.toPlainString()

        // Formatting for thousands separation if needed or clean display
        return if (plainString.contains('.')) {
            val parts = plainString.split('.')
            val intPart = parts[0]
            val decPart = parts[1]
            val formattedInt = formatIntegerString(intPart)
            "$formattedInt.$decPart"
        } else {
            formatIntegerString(plainString)
        }
    }

    private fun formatIntegerString(raw: String): String {
        return try {
            val isNegative = raw.startsWith('-')
            val absStr = if (isNegative) raw.drop(1) else raw
            if (absStr.length > 15) return raw // Don't overflow Long formatting
            val longVal = absStr.toLong()
            val symbols = DecimalFormatSymbols(Locale.US).apply { groupingSeparator = ',' }
            val df = DecimalFormat("#,###", symbols)
            val formatted = df.format(longVal)
            if (isNegative) "-$formatted" else formatted
        } catch (e: Exception) {
            raw
        }
    }
}
