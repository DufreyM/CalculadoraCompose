package com.example.calculadoracientifica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composecalculator.ScientificCalculator

class MainActivity : ComponentActivity() {
    private val calculator = ScientificCalculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorScreen()
        }
    }

    @Composable
    fun CalculatorScreen() {
        var display by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campo de texto con fondo diferente y validación de entrada
            BasicTextField(
                value = display,
                onValueChange = { newValue ->
                    if (isInputValid(newValue)) {
                        display = newValue
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.LightGray)  // Color de fondo del cuadro de texto
                    .padding(8.dp),
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )

            val buttons = listOf(
                "7", "8", "9", "/", "√",
                "4", "5", "6", "*", "^",
                "1", "2", "3", "-", "(",
                "0", ".", "=", "+", ")",
                "C"
            )

            buttons.chunked(5).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { symbol ->
                        Button(
                            onClick = {
                                when (symbol) {
                                    "=" -> {
                                        try {
                                            display = calculator.evaluate(display).toString()
                                        } catch (e: Exception) {
                                            display = "Error"
                                        }
                                    }
                                    "C" -> display = ""
                                    else -> {
                                        if (symbol == "." && display.contains(".")) {
                                            // Si ya hay un punto decimal, no permite agregar otro
                                            return@Button
                                        }
                                        display += symbol
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(symbol, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }

    // Función para validar la entrada
    private fun isInputValid(input: String): Boolean {
        // Permite solo números, operadores matemáticos y un punto decimal
        val regex = "^[0-9+\\-*/().√^]*$".toRegex()
        return regex.matches(input)
    }

    @Preview(showBackground = true)
    @Composable
    fun CalculatorScreenPreview() {
        MaterialTheme {
            Surface {
                CalculatorScreen()
            }
        }
    }
}
