package ru.oraora.books.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val ShimmerColorShades = listOf(
    Color.LightGray.copy(0.4f),
    Color.LightGray.copy(0.1f),
    Color.LightGray.copy(0.4f)
)

// Цвета для градиента обложки,
// чтобы на любой картинке была видна иконка закладка
val BookCardShades = arrayOf(
    0.0f to Color.Transparent,
    0.6f to Color.Transparent,
    0.7f to Color.Black.copy(0.02f),
    0.8f to Color.Black.copy(0.06f),
    1f to Color.Black.copy(0.1f)
)