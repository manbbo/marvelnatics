package br.com.digitalhouse.marvelnaticos.marvelnatics.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = primaryColor,
    primaryVariant = primaryLightColor,
    secondary = primaryDarkColor,
    background = secondaryColor,
    surface = secondaryColor,
    onPrimary = secondaryColor,
    onSecondary = black,
    onBackground = black,
    onSurface = black,
)

private val LightColorPalette = lightColors(
    primary = primaryColor,
    primaryVariant = primaryLightColor,
    secondary = primaryDarkColor ,
    background = white,
    surface = white,
    onPrimary = white,
    onSecondary = black,
    onBackground = black,
    onSurface = black,
)

@Composable
fun MarvelnaticsTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
    )
}