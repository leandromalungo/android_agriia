package com.agriia.app.feature.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FolderSpecial
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import coil.compose.SubcomposeAsyncImage
import com.agriia.app.core.designsystem.PillButtonShape
import com.agriia.app.core.designsystem.components.AgriiaLogo
import com.agriia.app.feature.chat.domain.model.Conversation
import com.example.R

@Composable
fun DrawerContent(
    conversations: List<Conversation>,
    currentConversationId: String?,
    userName: String,
    userEmail: String = "",
    userPhotoUrl: String = "",
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSelectConversation: (String) -> Unit,
    onDeleteConversation: ((String) -> Unit)? = null,
    onNewChatClick: () -> Unit,
    onCreateProjectClick: () -> Unit,
    onArtifactsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(320.dp),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Cabeçalho da Drawer: Logo da App (PNG) + Nome Agriia à esquerda e Ícone de Perfil no canto direito
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo PNG da App + Palavra Agriia
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_agriia_logo),
                        contentDescription = "Logo da APP Agriia",
                        modifier = Modifier
                            .size(26.dp)
                            .testTag("drawer_app_logo")
                    )
                    Text(
                        text = "Agriia",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Ícone de perfil no canto com borda bem fina cinza e imagem do perfil do usuário
                val userInitial = userName.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "U"
                val avatarUrl = remember(userEmail, userPhotoUrl) {
                    if (userPhotoUrl.isNotBlank()) {
                        userPhotoUrl
                    } else if (userEmail.isNotBlank()) {
                        val clean = userEmail.trim().lowercase()
                        val md5 = try {
                            val md = java.security.MessageDigest.getInstance("MD5")
                            val bytes = md.digest(clean.toByteArray(Charsets.UTF_8))
                            bytes.joinToString("") { "%02x".format(it) }
                        } catch (e: Exception) {
                            ""
                        }
                        if (md5.isNotEmpty()) {
                            "https://www.gravatar.com/avatar/$md5?d=identicon&s=160"
                        } else ""
                    } else ""
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            width = 0.8.dp,
                            color = Color(0xFFD1D5DB), // Borda bem fina cinza para destacar a foto de perfil
                            shape = CircleShape
                        )
                        .clickable {
                            onCloseDrawer()
                            onOpenSettings()
                        }
                        .testTag("drawer_profile_avatar"),
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarUrl.isNotBlank()) {
                        SubcomposeAsyncImage(
                            model = avatarUrl,
                            contentDescription = "Foto de perfil de $userName",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = userInitial,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            },
                            error = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = userInitial,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        )
                    } else {
                        Text(
                            text = userInitial,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            // Itens de Navegação Principais
            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // "Criar nova conversa" sem background preto, com mesmo estilo de "Notificações"
                DrawerMenuItem(
                    icon = Icons.Default.Add,
                    label = "Criar nova conversa",
                    onClick = {
                        onNewChatClick()
                        onCloseDrawer()
                    },
                    testTag = "drawer_new_chat_button"
                )
                DrawerMenuItem(
                    icon = Icons.Outlined.FolderSpecial,
                    label = stringResource(R.string.nav_create_project),
                    onClick = {
                        onCreateProjectClick()
                        onCloseDrawer()
                    },
                    testTag = "drawer_item_create_project"
                )
                DrawerMenuItem(
                    icon = Icons.Outlined.Notifications,
                    label = stringResource(R.string.nav_notifications),
                    onClick = {
                        onNotificationsClick()
                        onCloseDrawer()
                    },
                    testTag = "drawer_item_notifications"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                thickness = 0.5.dp
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Barra de Busca de Conversas
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Buscar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Buscar conversas...",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("conversation_search_field"),
                            textStyle = TextStyle(
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            singleLine = true
                        )
                    }
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpar busca",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Título "Recentes" ou "Resultados da busca"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (searchQuery.isBlank()) stringResource(R.string.nav_recent) else "Resultados da busca",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                )
                if (searchQuery.isNotBlank()) {
                    Text(
                        text = "${conversations.size} encontrada(s)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // Lista de Conversas Recentes ou estado vazio
            if (conversations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank()) "Nenhuma conversa encontrada para \"$searchQuery\"."
                        else "Nenhuma conversa recente.\nEnvie uma mensagem para começar!",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    items(conversations, key = { it.id }) { conversation ->
                        val isSelected = conversation.id == currentConversationId
                        RecentConversationItem(
                            conversation = conversation,
                            isSelected = isSelected,
                            onClick = {
                                onSelectConversation(conversation.id)
                                onCloseDrawer()
                            },
                            onDelete = {
                                onDeleteConversation?.invoke(conversation.id)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

@Composable
private fun RecentConversationItem(
    conversation: Conversation,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(
                if (isSelected) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                else MaterialTheme.colorScheme.background
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("recent_chat_${conversation.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = conversation.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        androidx.compose.material3.IconButton(
            onClick = onDelete,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Clear,
                contentDescription = "Excluir conversa",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
