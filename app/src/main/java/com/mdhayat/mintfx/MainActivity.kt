package com.mdhayat.mintfx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdhayat.mintfx.ui.CurrencyConverterScreen
import com.mdhayat.mintfx.ui.CurrencyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val viewModel: CurrencyViewModel = viewModel(
                        factory = CurrencyViewModel.factory(applicationContext)
                    )
                    CurrencyConverterScreen(viewModel)
                }
            }
        }
    }
}
