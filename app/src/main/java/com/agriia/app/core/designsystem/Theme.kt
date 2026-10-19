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
    primary = PureBlack,
    onPrimary = PureWhite,
    primaryContainer = Gray100,
    onPrimaryContainer = PureBlack,
    secondary = Gray800,
    onSecondary = PureWhite,
    secondaryContainer = Gray100,
    onSecondaryContainer = PureBlack,
    tertiary = Gray700,
    onTertiary = PureWhite,
    background = PureWhite,
    onBackground = PureBlack,
    surface = PureWhite,
    onSurface = PureBlack,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray600,
    outline = Gray300
)

/**
 * Tema 100% monocromático: Branco, Cinza e Preto.
 * Sem sombras, sem tons verdes ou de cor secundária colorida.
 */
@Composable
fun AgriiaTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
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
