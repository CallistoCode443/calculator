package com.github.CallistoCode443.calculator

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var expression by rememberSaveable() { mutableStateOf("") }
    var result by rememberSaveable() { mutableStateOf("") }

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
                text = expression.ifEmpty { "0" },
                fontSize = 96.sp,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                textAlign = TextAlign.End
            )
            Text(
                text = result.ifEmpty { "0" },
                fontSize = 64.sp,
                color = MaterialTheme.colorScheme.secondary,
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
                CalculatorKey("C", MaterialTheme.colorScheme.error, Modifier.weight(1f))
                CalculatorKey("%", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("CE", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("/", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            }
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CalculatorKey("7", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("8", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("9", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("X", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            }
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CalculatorKey("4", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("5", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("6", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("-", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            }
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CalculatorKey("1", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("2", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("3", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("+", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            }
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CalculatorKey("\u27F3", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("0", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey(",", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                CalculatorKey("=", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CalculatorKey(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = {},
        modifier = modifier.fillMaxHeight(),
        colors = ButtonDefaults.textButtonColors(contentColor = color),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(label, fontSize = 38.sp, fontWeight = FontWeight.Medium)
    }
}
