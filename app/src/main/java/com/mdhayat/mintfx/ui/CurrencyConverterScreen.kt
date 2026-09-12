package com.mdhayat.mintfx.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CurrencyConverterScreen(viewModel: CurrencyViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MintFx") },
                actions = {
                    TextButton(onClick = viewModel::refreshRates) { Text("Refresh") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

            if (state.isUsingCachedRates) {
                Text(
                    text = "Using Cached Rates",
                    modifier = Modifier.background(Color(0xFFFFF3CD)).padding(10.dp),
                    color = Color(0xFF6B5200),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.amountInput,
                onValueChange = viewModel::onAmountChange,
                label = { Text("Amount") },
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CurrencyDropdown(
                    modifier = Modifier.weight(1f),
                    label = "From",
                    options = state.availableCurrencies,
                    selected = state.sourceCurrency,
                    onSelected = viewModel::onSourceCurrencyChange
                )
                CurrencyDropdown(
                    modifier = Modifier.weight(1f),
                    label = "To",
                    options = state.availableCurrencies,
                    selected = state.targetCurrency,
                    onSelected = viewModel::onTargetCurrencyChange
                )
            }

            Text("Converted Amount", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("${state.convertedAmount} ${state.targetCurrency}", style = MaterialTheme.typography.headlineSmall)

            state.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(onClick = viewModel::refreshRates, modifier = Modifier.width(180.dp)) {
                Text("Fetch Latest Rates")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyDropdown(
    modifier: Modifier,
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
