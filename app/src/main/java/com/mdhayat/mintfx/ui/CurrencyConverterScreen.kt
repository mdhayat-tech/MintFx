package com.mdhayat.mintfx.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay

private val Ink = Color(0xFF0B1724)
private val InkSoft = Color(0xFF152637)
private val Mint = Color(0xFF63E6BE)
private val MintPale = Color(0xFFD8FFF2)
private val Canvas = Color(0xFFF4F8F7)
private val Muted = Color(0xFF6D7D83)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(viewModel: CurrencyViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(120)
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Header(onRefresh = viewModel::refreshRates, isLoading = state.isLoading)

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 8 })
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text(
                        text = "Convert with confidence",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Ink,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Live rates, saved for the moments you are offline.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Muted
                    )

                    if (state.isUsingCachedRates) {
                        CacheBadge()
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "I want to convert",
                                style = MaterialTheme.typography.labelLarge,
                                color = Muted
                            )
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.amountInput,
                                onValueChange = viewModel::onAmountChange,
                                label = { Text("Amount") },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Ink
                                ),
                                colors = converterFieldColors()
                            )
                            CurrencyDropdown(
                                label = "From",
                                options = state.availableCurrencies,
                                selected = state.sourceCurrency,
                                onSelected = viewModel::onSourceCurrencyChange
                            )
                            SwapButton(onClick = viewModel::swapCurrencies)
                            CurrencyDropdown(
                                label = "To",
                                options = state.availableCurrencies,
                                selected = state.targetCurrency,
                                onSelected = viewModel::onTargetCurrencyChange
                            )
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Ink),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(modifier = Modifier.padding(22.dp)) {
                            Text(
                                text = "YOU RECEIVE",
                                style = MaterialTheme.typography.labelMedium,
                                color = Mint,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${state.convertedAmount} ${state.targetCurrency}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Based on the latest available rate",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFB7C6CA)
                            )
                        }
                    }

                    state.errorMessage?.let { message ->
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(
                        onClick = viewModel::refreshRates,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Mint,
                            contentColor = Ink
                        )
                    ) {
                        Text("Refresh rates", fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "Rates are cached automatically on every successful refresh.",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Muted
                    )
                }
            }
        }
    }
}

@Composable
private fun Header(onRefresh: () -> Unit, isLoading: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Ink)
            .padding(horizontal = 20.dp, vertical = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(46.dp).clip(RoundedCornerShape(14.dp)).background(Mint),
            contentAlignment = Alignment.Center
        ) {
            Text("M", color = Ink, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
        }
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text("MintFx", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Currency converter", color = Color(0xFFB7C6CA), style = MaterialTheme.typography.labelMedium)
        }
        TextButton(onClick = onRefresh, enabled = !isLoading) {
            Text(if (isLoading) "Loading" else "Refresh", color = Mint, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CacheBadge() {
    Row(
        modifier = Modifier.clip(RoundedCornerShape(50)).background(MintPale).padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF15966E)))
        Text(
            text = "  Offline mode - using saved rates",
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF126B52),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SwapButton(onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        TextButton(onClick = onClick) {
            Text("SWAP CURRENCIES", color = Color(0xFF15966E), fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            value = currencyLabel(selected),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            colors = converterFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(currencyLabel(option)) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun converterFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF15966E),
    unfocusedBorderColor = Color(0xFFD1DDDA),
    focusedLabelColor = Color(0xFF15966E),
    cursorColor = Color(0xFF15966E)
)
