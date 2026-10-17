package com.agriia.app.feature.chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agriia.app.core.designsystem.LimeAccent
import com.agriia.app.core.designsystem.LimeSoft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentModalSheet(
    onDismiss: () -> Unit,
    onPickFile: () -> Unit,
    onPickImage: () -> Unit,
    onTakePhoto: () -> Unit,
    onPickMap: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Adicionar anexo",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
            )

            // 1. Enviar ficheiro
            AttachmentOptionItem(
                icon = Icons.Outlined.Description,
                title = "Enviar ficheiro",
                subtitle = "Documentos, PDFs, planilhas ou arquivos de texto",
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                iconTint = MaterialTheme.colorScheme.primary,
                testTag = "attachment_option_file",
                onClick = {
                    onDismiss()
                    onPickFile()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Enviar imagem
            AttachmentOptionItem(
                icon = Icons.Outlined.Image,
                title = "Enviar imagem",
                subtitle = "Fotos ou ilustrações da sua galeria",
                containerColor = LimeSoft,
                iconTint = MaterialTheme.colorScheme.secondary,
                testTag = "attachment_option_image",
                onClick = {
                    onDismiss()
                    onPickImage()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Tirar foto
            AttachmentOptionItem(
                icon = Icons.Outlined.PhotoCamera,
                title = "Tirar foto",
                subtitle = "Abra a câmera do dispositivo e capture na hora",
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                iconTint = MaterialTheme.colorScheme.onSurface,
                testTag = "attachment_option_camera",
                onClick = {
                    onDismiss()
                    onTakePhoto()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Pegar imagem do mapa
            AttachmentOptionItem(
                icon = Icons.Outlined.Map,
                title = "Pegar imagem do mapa",
                subtitle = "Anexe coordenadas ou imagem de área agrícola / localização",
                containerColor = LimeAccent.copy(alpha = 0.4f),
                iconTint = MaterialTheme.colorScheme.primary,
                testTag = "attachment_option_map",
                onClick = {
                    onDismiss()
                    onPickMap()
                }
            )
        }
    }
}

@Composable
private fun AttachmentOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    containerColor: Color,
    iconTint: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
