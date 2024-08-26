package com.example.composecalculator

import java.util.Stack
import kotlin.math.pow
import kotlin.math.sqrt

class ScientificCalculator {

    fun evaluate(expression: String): Double {
        val tokens = expression.replace(" ", "").toCharArray()
        val values = Stack<Double>()
        val ops = Stack<Char>()

        var i = 0
        while (i < tokens.size) {
            if (tokens[i].isDigit() || tokens[i] == '.') {
                val sb = StringBuilder()
                while (i < tokens.size && (tokens[i].isDigit() || tokens[i] == '.')) {
                    sb.append(tokens[i++])
                }
                values.push(sb.toString().toDouble())
                i--
            } else if (tokens[i] == '(') {
                ops.push(tokens[i])
            } else if (tokens[i] == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values))
                }
                ops.pop()
            } else if (tokens[i] == '√') {
                ops.push(tokens[i])
            } else if (tokens[i] in listOf('+', '-', '*', '/', '^')) {
                while (ops.isNotEmpty() && hasPrecedence(tokens[i], ops.peek())) {
                    values.push(applyOp(ops.pop(), values))
                }
                ops.push(tokens[i])
            }
            i++
        }

        while (ops.isNotEmpty()) {
            values.push(applyOp(ops.pop(), values))
        }

        return values.pop()
    }

    private fun applyOp(op: Char, values: Stack<Double>): Double {
        return when (op) {
            '+' -> {
                val b = values.pop()
                val a = values.pop()
                a + b
            }
            '-' -> {
                val b = values.pop()
                val a = values.pop()
                a - b
            }
            '*' -> {
                val b = values.pop()
                val a = values.pop()
                a * b
            }
            '/' -> {
                val b = values.pop()
                if (b == 0.0) throw UnsupportedOperationException("Cannot divide by zero")
                val a = values.pop()
                a / b
            }
            '^' -> {
                val b = values.pop()
                val a = values.pop()
                a.pow(b)
            }
            '√' -> {
                val a = values.pop()
                sqrt(a)
            }
            else -> throw UnsupportedOperationException("Unsupported operation")
        }
    }

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')') return false
        if ((op1 == '*' || op1 == '/' || op1 == '^' || op1 == '√') && (op2 == '+' || op2 == '-')) return false
        return true
    }
}
