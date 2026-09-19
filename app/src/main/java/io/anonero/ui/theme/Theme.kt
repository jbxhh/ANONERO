package io.anonero.ui.theme 

import io.anonero.ui.theme.Typography
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import io.anonero.ui.theme.Black
import io.anonero.ui.theme.DarkGray
import io.anonero.ui.theme.DarkOrange
import io.anonero.ui.theme.Orange
import io.anonero.ui.theme.White

private val DarkColorScheme = darkColorScheme(
    primary = Orange,
    onPrimary = Black,
    primaryContainer = DarkOrange,
    onPrimaryContainer = White,
    secondary = DarkGray,
    onSecondary = White,
    background = Black,
    onBackground = White,
    surface = Black,
    onSurface = White
)

@Composable
fun AnonNeroTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content,
        typography = Typography.copy(
            titleLarge = Typography.titleLarge.copy(fontFamily = FontFamily.SansSerif),
            titleSmall = Typography.titleSmall.copy(fontFamily = FontFamily.SansSerif),
            titleMedium = Typography.titleMedium.copy(fontFamily = FontFamily.SansSerif),
            headlineMedium = Typography.headlineMedium.copy(fontFamily = FontFamily.SansSerif),
            headlineLarge = Typography.headlineLarge.copy(fontFamily = FontFamily.SansSerif),
            headlineSmall = Typography.headlineSmall.copy(fontFamily = FontFamily.SansSerif),
            bodyLarge = Typography.bodyLarge.copy(fontFamily = FontFamily.SansSerif),
            bodySmall = Typography.bodySmall.copy(fontFamily = FontFamily.SansSerif),
            bodyMedium = Typography.bodyMedium.copy(fontFamily = FontFamily.SansSerif),
            displayLarge = Typography.displayLarge.copy(fontFamily = FontFamily.SansSerif),
            displaySmall = Typography.displaySmall.copy(fontFamily = FontFamily.SansSerif),
            displayMedium = Typography.displayMedium.copy(fontFamily = FontFamily.SansSerif),
            labelLarge = Typography.labelLarge.copy(fontFamily = FontFamily.SansSerif),
            labelSmall = Typography.labelSmall.copy(fontFamily = FontFamily.SansSerif),
            labelMedium = Typography.labelMedium.copy(fontFamily = FontFamily.SansSerif),
        )
    )
}

@Composable
fun AnonOutlineButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    child: @Composable () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(16.dp)
    ) {
        child()
    }
}
