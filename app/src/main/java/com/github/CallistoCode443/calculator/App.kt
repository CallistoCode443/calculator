package com.github.CallistoCode443.calculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.CallistoCode443.calculator.ui.theme.CalculatorTheme

@Preview
@Composable
fun AppPreview() {
    CalculatorTheme {
        App()
    }
}


@Composable
fun App(modifier: Modifier = Modifier) {
    var topText by rememberSaveable { mutableStateOf("") }
    var current by rememberSaveable { mutableStateOf("") }
    var accumulated by rememberSaveable { mutableDoubleStateOf(0.0) }
    var pendingOperation by rememberSaveable { mutableStateOf("") }
    var startNew by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val bottomText = if (current != "") {
        current
    } else if (pendingOperation != "") {
        formatNumber(accumulated)
    } else {
        "0"
    }

    fun resetAll() {
        topText = ""
        current = ""
        accumulated = 0.0
        pendingOperation = ""
        startNew = false
    }

    fun onKeyPress(label: String) {
        if (current == "Ошибка") {
            resetAll()
        }

        when (label) {
            "C" -> {
                resetAll()
            }

            "CE" -> {
                if (pendingOperation == "") {
                    resetAll()
                } else {
                    current = ""
                    startNew = false
                    topText = formatNumber(accumulated) + " " + pendingOperation
                }
            }

            "+", "-", "x", "/" -> {
                if (current != "") {
                    val number = current.toDouble()

                    if (pendingOperation != "") {
                        if (pendingOperation == "/" && number == 0.0) {
                            resetAll()
                            current = "Ошибка"
                            return
                        }
                        accumulated = calculate(accumulated, pendingOperation, number)
                    } else {
                        accumulated = number
                    }
                    pendingOperation = label
                    current = ""
                    startNew = false
                    topText = formatNumber(accumulated) + " " + label
                } else if (pendingOperation != "") {
                    pendingOperation = label
                    topText = formatNumber(accumulated) + " " + label
                } else if (label == "-") {
                    current = "-"
                }
            }

            "=" -> {
                if (pendingOperation != "") {
                    val number = if (current != "") {
                        current.toDouble()
                    } else {
                        accumulated
                    }

                    if (pendingOperation == "/" && number == 0.0) {
                        resetAll()
                        current = "Ошибка"
                        return
                    }

                    val answer = calculate(accumulated, pendingOperation, number)
                    topText =
                        formatNumber(accumulated) + " " + pendingOperation + " " + formatNumber(
                            number
                        ) + " ="
                    accumulated = number
                    current = formatNumber(answer)
                    pendingOperation = ""
                    startNew = true
                }
            }

            "%" -> {
                if (pendingOperation != "") {
                    val second = if (current != "") {
                        current.toDouble()
                    } else {
                        accumulated
                    }
                    val percent = accumulated * second / 100
                    current = formatNumber(percent)
                    topText = formatNumber(accumulated) + " " + pendingOperation + " " + current
                    startNew = true
                } else {
                    current = "0"
                    topText = "0"
                    startNew = true
                }
            }

            "." -> {
                if (startNew) {
                    current = ""
                    startNew = false
                    topText = if (pendingOperation == "") {
                        ""
                    } else {
                        formatNumber(accumulated) + " " + pendingOperation
                    }
                }

                if (!current.contains(".")) {
                    when (current) {
                        "" -> {
                            current = "0."
                        }
                        "-" -> {
                            current = "-0."
                        }
                        else -> {
                            current += "."
                        }
                    }
                }
            }

            "\u2398" -> {
                if (current == "Ошибка") {
                    return
                }
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("result", bottomText))
                Toast.makeText(context, "Скопировано", Toast.LENGTH_SHORT).show()
            }

            else -> {
                if (startNew) {
                    current = ""
                    startNew = false
                    topText = if (pendingOperation == "") {
                        ""
                    } else {
                        formatNumber(accumulated) + " " + pendingOperation
                    }
                }

                when (current) {
                    "0" -> {
                        current = label
                    }
                    "-0" -> {
                        current = "-$label"
                    }
                    else -> {
                        current += label
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.inverseOnSurface),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = topText.ifEmpty { " " },
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 1,

                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                textAlign = TextAlign.End
            )
            Text(
                text = bottomText,
                fontSize = 40.sp,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                textAlign = TextAlign.End
            )
        }

        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CalculatorKey("C", MaterialTheme.colorScheme.error, Modifier.weight(1f)) {
                    onKeyPress(
                        "C"
                    )
                }
                CalculatorKey("CE", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "CE"
                    )
                }
                CalculatorKey("%", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "%"
                    )
                }
                CalculatorKey("/", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "/"
                    )
                }
            }
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CalculatorKey("7", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "7"
                    )
                }
                CalculatorKey("8", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "8"
                    )
                }
                CalculatorKey("9", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "9"
                    )
                }
                CalculatorKey("x", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "x"
                    )
                }
            }
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CalculatorKey("4", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "4"
                    )
                }
                CalculatorKey("5", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "5"
                    )
                }
                CalculatorKey("6", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "6"
                    )
                }
                CalculatorKey("-", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "-"
                    )
                }
            }
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CalculatorKey("1", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "1"
                    )
                }
                CalculatorKey("2", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "2"
                    )
                }
                CalculatorKey("3", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "3"
                    )
                }
                CalculatorKey("+", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "+"
                    )
                }
            }
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CalculatorKey("\u2398", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "\u2398"
                    )
                }
                CalculatorKey("0", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "0"
                    )
                }
                CalculatorKey(".", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "."
                    )
                }
                CalculatorKey("=", MaterialTheme.colorScheme.primary, Modifier.weight(1f)) {
                    onKeyPress(
                        "="
                    )
                }
            }
        }
    }
}

@Composable
private fun CalculatorKey(
    label: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit = {}
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxHeight(),
        colors = ButtonDefaults.textButtonColors(contentColor = color),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(label, fontSize = 30.sp, fontWeight = FontWeight.Medium)
    }
}

private fun calculate(a: Double, op: String, b: Double): Double {
    return when (op) {
        "+" -> {
            a + b
        }

        "-" -> {
            a - b
        }

        "x" -> {
            a * b
        }

        else -> {
            a / b
        }
    }
}

private fun formatNumber(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toLong().toString()
    } else {
        value.toString()
    }
}