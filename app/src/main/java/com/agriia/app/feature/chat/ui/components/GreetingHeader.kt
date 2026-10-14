package com.agriia.app.feature.chat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agriia.app.core.common.DateUtils
import com.agriia.app.core.designsystem.components.AgriiaIcon

@Composable
fun GreetingHeader(
    userName: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val greetingText = DateUtils.getGreeting(context, userName)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .testTag("greeting_header"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícone orgânico do agriia centralizado
        AgriiaIcon(
            size = 54.dp,
            tint = MaterialTheme.colorScheme.primary,
            animated = false
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Saudação contextual temporal com tipografia serif elegante
        Text(
            text = greetingText,
            style = MaterialTheme.typography.displayMedium.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal,
                fontSize = 26.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            textAlign = TextAlign.Center
        )
    }
}
