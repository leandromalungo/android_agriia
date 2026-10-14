package com.agriia.app.feature.chat.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.agriia.app.core.designsystem.LimeSoft
import com.agriia.app.core.designsystem.components.AgriiaChatInput
import com.agriia.app.core.designsystem.components.AgriiaTopAppBar
import com.agriia.app.feature.artifacts.ArtifactsDialog
import com.agriia.app.feature.chat.ui.components.GreetingHeader
import com.agriia.app.feature.chat.ui.components.MessageBubble
import com.agriia.app.feature.chat.ui.components.ModelSelectorDialog
import com.agriia.app.feature.chat.ui.components.UpgradeBanner
import com.agriia.app.feature.drawer.DrawerContent
import com.agriia.app.feature.projects.ProjectsDialog
import com.example.R
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Fecha o drawer com o botão voltar se estiver aberto
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    // Scroll automático para a última mensagem recebida
    LaunchedEffect(uiState.messages.size, uiState.messages.lastOrNull()?.content) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    // Toast de notificação
    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.onDismissToast()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                conversations = uiState.conversations,
                currentConversationId = uiState.currentConversationId,
                userName = uiState.userName,
                onSelectConversation = { convId ->
                    viewModel.onSelectConversation(convId)
                },
                onNewChatClick = {
                    viewModel.onStartNewChat()
                },
                onCreateProjectClick = {
                    viewModel.onShowProjectsDialog(true)
                },
                onArtifactsClick = {
                    viewModel.onShowArtifactsDialog(true)
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        // Fundo com sutil degradê suave de branco leitoso a verde lima
        val bgBrush = Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.background,
                MaterialTheme.colorScheme.background,
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            )
        )

        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(bgBrush)
                .imePadding(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AgriiaTopAppBar(
                    onOpenDrawer = {
                        scope.launch { drawerState.open() }
                    },
                    isTemporaryChat = uiState.isTemporaryChat,
                    onToggleTemporaryChat = {
                        viewModel.onToggleTemporaryChat()
                    }
                )
            },
            bottomBar = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (uiState.showUpgradeBanner && uiState.messages.isEmpty()) {
                        UpgradeBanner(
                            onUpgradeClick = {
                                Toast.makeText(context, "Plano Pro: recursos avançados em breve!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    AgriiaChatInput(
                        inputText = uiState.inputText,
                        onInputTextChange = viewModel::onInputTextChange,
                        onSendMessage = viewModel::onSendMessage,
                        onAttachClick = {
                            Toast.makeText(context, "Selecione fotos, documentos ou dados", Toast.LENGTH_SHORT).show()
                        },
                        onMicClick = {
                            Toast.makeText(context, "Ditado por voz ativado", Toast.LENGTH_SHORT).show()
                        },
                        onVoiceModeClick = {
                            Toast.makeText(context, "Modo de voz bidirecional conectado", Toast.LENGTH_SHORT).show()
                        },
                        currentModelName = uiState.currentModelName,
                        onModelSelectorClick = {
                            viewModel.onShowModelSelectorDialog(true)
                        }
                    )
                }
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (uiState.messages.isEmpty()) {
                    // Estado Vazio: Saudação centralizada
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        GreetingHeader(userName = uiState.userName)
                        if (uiState.isTemporaryChat) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = stringResource(R.string.temp_chat_enabled_hint),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                ),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1.2f))
                    }
                } else {
                    // Lista de Mensagens da conversa atual
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp)
                    ) {
                        items(uiState.messages, key = { it.id }) { message ->
                            MessageBubble(message = message)
                        }
                    }
                }
            }
        }
    }

    // Diálogos modais
    if (uiState.showProjectsDialog) {
        ProjectsDialog(
            onDismiss = { viewModel.onShowProjectsDialog(false) },
            onProjectCreated = { title ->
                Toast.makeText(context, "Projeto \"$title\" criado com sucesso!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (uiState.showArtifactsDialog) {
        ArtifactsDialog(
            onDismiss = { viewModel.onShowArtifactsDialog(false) }
        )
    }

    if (uiState.showModelSelectorDialog) {
        ModelSelectorDialog(
            currentModelName = uiState.currentModelName,
            onModelSelected = { model ->
                viewModel.onSelectModel(model)
            },
            onDismiss = { viewModel.onShowModelSelectorDialog(false) }
        )
    }
}
