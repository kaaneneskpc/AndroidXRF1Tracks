package com.example.androidxrbasic

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember

object F1TrackRepository {
    val f1Tracks = listOf(
        F1Track("Australia GP", R.drawable.f1_australia),
        F1Track("Abu Dhabi GP", R.drawable.f1_abu_dhabi),
        F1Track("Italy GP", R.drawable.f1_italy),
        F1Track("Japan GP", R.drawable.f1_japan),
        F1Track("Turkey GP", R.drawable.f1_turkey),
        F1Track("Singapore GP", R.drawable.f1_singapore),
        F1Track("Suudi Arabia GP", R.drawable.f1_suudi_arabia)
    )
}

@Composable
fun rememberF1TrackIndex() = remember { mutableIntStateOf(0) }

data class F1Track(
    val name: String,
    @DrawableRes val imageRes: Int
)