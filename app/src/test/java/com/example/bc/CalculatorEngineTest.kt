package com.example.bc

import com.example.bc.logic.CalculatorAction
import com.example.bc.logic.CalculatorEngine
import com.example.bc.logic.CalculatorOperation
import com.example.bc.logic.CalculatorState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculatorEngineTest {

    @Test
    fun testBasicAddition() {
        var state = CalculatorState()
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(5))
        state = CalculatorEngine.onAction(state, CalculatorAction.Operation(CalculatorOperation.ADD))
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(3))
        
        // Sebelum calculate: menampilkan rumus yang sedang diketik tanpa bocoran jawaban
        assertEquals("5 + 3", state.bottomDisplay)

        state = CalculatorEngine.onAction(state, CalculatorAction.Calculate)

        // Setelah calculate: menampilkan hasil akhir
        assertEquals("8", state.bottomDisplay)
        assertTrue(state.isEvaluated)
        assertFalse(state.isError)
    }



    @Test
    fun testOperatorPrecedence() {
        // 2 + 3 * 4 should equal 14, not 20
        var state = CalculatorState()
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(2))
        state = CalculatorEngine.onAction(state, CalculatorAction.Operation(CalculatorOperation.ADD))
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(3))
        state = CalculatorEngine.onAction(state, CalculatorAction.Operation(CalculatorOperation.MULTIPLY))
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(4))
        state = CalculatorEngine.onAction(state, CalculatorAction.Calculate)

        assertEquals("14", state.liveResult)
    }

    @Test
    fun testDivisionByZero() {
        var state = CalculatorState()
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(9))
        state = CalculatorEngine.onAction(state, CalculatorAction.Operation(CalculatorOperation.DIVIDE))
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(0))
        state = CalculatorEngine.onAction(state, CalculatorAction.Calculate)

        assertTrue(state.isError)
        assertEquals("Tidak bisa dibagi 0", state.errorMessage)
    }

    @Test
    fun testDecimals() {
        var state = CalculatorState()
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(0))
        state = CalculatorEngine.onAction(state, CalculatorAction.Decimal)
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(1))
        state = CalculatorEngine.onAction(state, CalculatorAction.Operation(CalculatorOperation.ADD))
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(0))
        state = CalculatorEngine.onAction(state, CalculatorAction.Decimal)
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(2))
        state = CalculatorEngine.onAction(state, CalculatorAction.Calculate)

        assertEquals("0.3", state.liveResult)
    }

    @Test
    fun testToggleSign() {
        var state = CalculatorState()
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(25))
        state = CalculatorEngine.onAction(state, CalculatorAction.ToggleSign)

        assertEquals("-25", state.expression)

        state = CalculatorEngine.onAction(state, CalculatorAction.ToggleSign)
        assertEquals("25", state.expression)
    }

    @Test
    fun testPercentage() {
        var state = CalculatorState()
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(50))
        state = CalculatorEngine.onAction(state, CalculatorAction.Percentage)

        assertEquals("0.5", state.expression)
    }

    @Test
    fun testDeleteBackspace() {
        var state = CalculatorState()
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(1))
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(2))
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(3))
        state = CalculatorEngine.onAction(state, CalculatorAction.Delete)

        assertEquals("12", state.expression)
    }

    @Test
    fun testClearAll() {
        var state = CalculatorState()
        state = CalculatorEngine.onAction(state, CalculatorAction.Number(9))
        state = CalculatorEngine.onAction(state, CalculatorAction.Operation(CalculatorOperation.ADD))
        state = CalculatorEngine.onAction(state, CalculatorAction.Clear)

        assertEquals("", state.expression)
        assertEquals("", state.liveResult)
    }
}
