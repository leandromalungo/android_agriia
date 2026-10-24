package com.agriia.app.feature.chat.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyStateSuggestions(
    onSelectSuggestion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Analisar foto da lavoura ou folha
        SuggestionItem(
            icon = Icons.Outlined.Image,
            label = "Analisar foto da lavoura ou folha",
            testTag = "suggestion_analyze_crop",
            onClick = {
                onSelectSuggestion("Analise a saúde da lavoura nesta foto e aponte possíveis pragas ou deficiências: ")
            }
        )

        // 2. Calcular adubação e dosagem de safra
        SuggestionItem(
            icon = Icons.Outlined.Edit,
            label = "Calcular adubação e dosagem de safra",
            testTag = "suggestion_calc_fertilizer",
            onClick = {
                onSelectSuggestion("Calcule a recomendação de adubação e dosagem para o cultivo de ")
            }
        )

        // 3. Consultar clima, chuva e cotações agrícolas
        SuggestionItem(
            icon = Icons.Outlined.Language,
            label = "Consultar clima, chuva e cotações agrícolas",
            testTag = "suggestion_check_weather",
            onClick = {
                onSelectSuggestion("Consulte a previsão climática e cotações agrícolas recentes para ")
            }
        )
    }
}

@Composable
private fun SuggestionItem(
    icon: ImageVector,
    label: String,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 5.dp, horizontal = 4.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
            )
        )
    }
}
