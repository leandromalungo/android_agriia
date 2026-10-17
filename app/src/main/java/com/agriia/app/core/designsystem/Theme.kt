package com.agriia.app.core.designsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = PureWhite,
    primaryContainer = LimeSoft,
    onPrimaryContainer = ForestGreenPrimary,
    secondary = ForestGreenSecondary,
    onSecondary = PureWhite,
    secondaryContainer = SurfaceVariantLight,
    onSecondaryContainer = ForestGreenSecondary,
    tertiary = AccentGreen,
    onTertiary = PureWhite,
    background = MilkWhite,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderLight
)

/**
 * Tema exclusivo claro/branco para o agriia.
 * Sempre utiliza o esquema de cores MilkWhite e LimeSoft com ícones de sistema escuros.
 */
@Composable
fun AgriiaTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    // Configurado exclusivamente para tema branco
    val colorScheme = LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AgriiaTypography,
        shapes = AgriiaShapes,
        content = content
    )
}
