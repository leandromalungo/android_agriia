# Plano de Implementação: agriia (Fase 0)

O **agriia** é um cliente de chat com IA nativo para Android construído com **Kotlin**, **Jetpack Compose** e **Material 3**. Possui arquitetura limpa em camadas (Clean Architecture + MVVM), identidade visual orgânica inspirada no design minimalista moderno (tons suaves de leite e verde-lima, logo serifado refinado e modo escuro), suporte a streaming de respostas e persistência local robusta.

---

## Decisões Confirmadas com o Usuário

- **Provedor de IA**: Google Gemini (`gemini-3.5-flash` via API direta com suporte a streaming SSE e chave injetada via `BuildConfig.GEMINI_API_KEY`).
- **Injeção de Dependências**: Padrão nativo **AppContainer** + **ViewModelProvider.Factory** (alto desempenho de compilação, sem sobrecarga de plugins ou instabilidade de build).
- **Saudação do Usuário**: Personalizável e reativa, salva no DataStore Preferences (com fallback inteligente conforme o horário: "Bom dia", "Boa tarde", "Boa noite").

---

## 1. Resumo do Aplicativo e Conceito Central

- **O que faz**: Oferece uma interface conversacional fluida, moderna e focada em produtividade e clareza cognitiva. Permite criar novos chats, navegar pelo histórico recente no menu lateral retrátil (Navigation Drawer), alternar entre modos de modelo/pensamento, anexar mídias, ditar mensagens e alternar para chat temporário.
- **Público-alvo**: Usuários que buscam uma experiência de chat com inteligência artificial sofisticada, sem distrações visuais, com tipografia legível e resposta rápida.
- **Diferencial de Valor**: Experiência premium em tela cheia (Edge-to-Edge), animações fluidas, identidade visual orgânica com paleta acolhedora e suporte offline para consulta ao histórico local.

---

## 2. Design System & Identidade Visual

### 2.1 Paleta de Cores (Tokens Semânticos M3)

#### Tema Claro (Padrão - Orgânico & Convidativo)
- **Background / Fundo Base**: `#FAFAF2` (Branco leitoso acolhedor, sem ofuscamento)
- **Background Gradient Accent**: `#EEF5D0` (Verde-lima sutil em degradê linear suave de 15% na base ou splash)
- **Surface / Cartões & Caixa de Entrada**: `#F3F4EB` (Superfície ligeiramente contrastada)
- **Surface Variant**: `#E8EAE0` (Bordas suaves e botões utilitários)
- **Primary / Destaques & Ações**: `#1F3A2B` (Verde-floresta profundo / grafite orgânico)
- **OnPrimary**: `#FFFFFF`
- **Secondary / Botão de Modo & Chips**: `#2D533D`
- **Accent / Botão de Voz e Microfone**: `#3B6E50` (com opção de container `#D8ECD7`)
- **Text Primary / Títulos & Saudação**: `#17231B` (Grafite escuro quase preto com matiz verde)
- **Text Secondary / Placeholders**: `#6E776F` (Cinza musgo suave)
- **Border / Divisores sutis**: `#E2E5DC`

#### Tema Escuro (Modern Dark Vibe)
- **Background / Fundo Base**: `#141715` (Carvão profundo com matiz verde escuro)
- **Surface / Cartões & Input**: `#1D221E` (Superfície elevada)
- **Surface Variant**: `#252C27`
- **Primary**: `#A9D8B4` (Verde sálvia claro)
- **OnPrimary**: `#0B2014`
- **Text Primary**: `#EBF1EC`
- **Text Secondary**: `#919D94`
- **Border**: `#2B332E`

### 2.2 Tipografia & Hierarquia
- **Display / Marca**: Tipografia serifada elegante para o logotipo "agriia" (`FontFamily.Serif`, peso `Bold` ou `Medium`, kerning ligeiramente espaçado).
- **Headings / Saudação Central**: Serif suave em tamanho grande (26sp, `FontWeight.Normal`), trazendo sofisticação editorial.
- **Body & Mensagens**: Sem serifa moderna (Roboto / Sans-serif do sistema), legível em blocos longos com espaçamento confortável entre linhas (15sp - 16sp, altura de linha 22sp).
- **Labels & Micro-cópias**: Sans-serif com tracking sutil (11sp - 12sp, `FontWeight.Medium`).

### 2.3 Formas & Elevação
- **Caixa de Entrada Principal**: Pílula/retângulo arredondado com cantos de `28.dp`, padding interno confortável (`16.dp`), borda translúcida sutil e elevação suave (`2.dp` a `4.dp`).
- **Botões de Ação (+, Microfone, Voz)**: Formato circular de `44.dp` a `48.dp` respeitando a área mínima de toque do Material 3.
- **Menu Lateral**: Bordas arredondadas à direita (`20.dp`), fundo contínuo sem divisores pesados.

---

## 3. Arquitetura do Sistema & Diagrama de Dados

```
┌────────────────────────────────────────────────────────────────────────┐
│                          UI LAYER (Compose M3)                         │
│  ┌──────────────────┐  ┌──────────────────┐  ┌───────────────────────┐ │
│  │   SplashScreen   │  │    ChatScreen    │  │  AppNavigationDrawer  │ │
│  └────────┬─────────┘  └────────┬─────────┘  └───────────┬───────────┘ │
│           │                     │                        │             │
│           └─────────────────────┼────────────────────────┘             │
│                                 ▼                                      │
│                  ┌──────────────────────────────┐                      │
│                  │        ChatViewModel         │                      │
│                  │  (UiState / UserEvents Flow) │                      │
│                  └──────────────┬───────────────┘                      │
└─────────────────────────────────┼──────────────────────────────────────┘
                                  ▼
┌────────────────────────────────────────────────────────────────────────┐
│                             DOMAIN LAYER                               │
│  ┌──────────────────────┐  ┌───────────────────┐  ┌──────────────────┐ │
│  │ ChatRepository (Itf) │  │ SendMessageUseCase│  │ StreamChatUseCase│ │
│  └──────────┬───────────┘  └─────────┬─────────┘  └────────┬─────────┘ │
└─────────────┼────────────────────────┼─────────────────────┼───────────┘
              ▼                        ▼                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│                              DATA LAYER                                │
│       ┌───────────────────────────────────────────────────────┐        │
│       │                  ChatRepositoryImpl                   │        │
│       └───────────┬───────────────────────────────┬───────────┘        │
│                   │                               │                    │
│                   ▼                               ▼                    │
│   ┌───────────────────────────────┐  ┌───────────────────────────────┐ │
│   │       Local Persistence       │  │        Network Engine         │ │
│   │  - AgriiaDatabase (Room)      │  │  - GeminiApiService (Retrofit)│ │
│   │  - ConversationDao, MessageDao│  │  - OkHttpClient (Stream SSE)  │ │
│   │  - UserPreferences (DataStore)│  │  - Moshi / Kotlinx.Serializ.  │ │
│   └───────────────────────────────┘  └───────────────────────────────┘ │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Estrutura de Pastas Completa (Package by Feature)

A estrutura organiza o pacote raiz `com.agriia.app` separando o núcleo reutilizável (`core`) dos módulos de usuário (`feature`), permitindo fácil futura modularização se desejado:

```
app/src/main/java/com/agriia/app/
├── core/
│   ├── common/
│   │   ├── Result.kt                   // Wrapper genérico de sucesso/erro/carregando
│   │   ├── DateUtils.kt                // Utilitários de saudação temporal e datas
│   │   └── Extensions.kt               // Extensões de String, Modifier e Flow
│   ├── database/
│   │   ├── AgriiaDatabase.kt           // Definição RoomDatabase (versão 1)
│   │   ├── dao/
│   │   │   ├── ConversationDao.kt      // Queries de chats e listagem recente
│   │   │   └── MessageDao.kt           // Inserção e leitura de mensagens
│   │   └── entity/
│   │       ├── ConversationEntity.kt   // ID, título, timestamp, modelo usado
│   │       └── MessageEntity.kt        // ID, chatId, papel (user/assistant), texto, data
│   ├── datastore/
│   │   ├── PreferencesKeys.kt          // Chaves de nome de usuário, tema e modelo
│   │   └── UserPreferencesRepository.kt// Interface e Flow de preferências
│   ├── designsystem/
│   │   ├── Color.kt                    // Definições hexadecimais da paleta agriia
│   │   ├── Theme.kt                    // AgriiaTheme com Light & Dark ColorScheme
│   │   ├── Type.kt                     // Tipografia serifada e sans-serif
│   │   ├── Shape.kt                    // Formas arredondadas e pílulas
│   │   └── components/
│   │       ├── AgriiaLogo.kt           // Ícone orgânico vetorial + tipografia serif
│   │       ├── AgriiaTopAppBar.kt      // Barra superior com hambúrguer e fantasma
│   │       ├── AgriiaChatInput.kt      // Campo arredondado, anexos, microfone e voz
│   │       └── AgriiaDrawerSheet.kt    // Conteúdo estilizado do NavigationDrawer
│   ├── di/
│   │   ├── AppContainer.kt             // Instâncias singleton de DB, Network e Repos
│   │   └── ViewModelFactory.kt         // Provedor tipado de ViewModels
│   └── network/
│       ├── GeminiApiService.kt         // Endpoints REST de geração e SSE streaming
│       ├── GeminiDto.kt                // Modelos de requisição e resposta JSON
│       └── NetworkClient.kt            // Configuração OkHttp (timeouts de 60s) e Retrofit
├── feature/
│   ├── splash/
│   │   └── SplashScreen.kt             // Animação de fade-in e transição suave
│   ├── chat/
│   │   ├── ui/
│   │   │   ├── ChatScreen.kt           // Tela principal: saudação central e novo chat
│   │   │   ├── ChatViewModel.kt        // Gerenciamento de estado, envio e histórico
│   │   │   ├── ChatUiState.kt          // Estado imutável (conversas, mensagens, loading)
│   │   │   └── components/
│   │   │       ├── GreetingHeader.kt   // Saudação com ícone e texto "Bom dia, [Nome]"
│   │   │       ├── MessageBubble.kt    // Balões de mensagens do usuário e da IA
│   │   │       └── UpgradeBanner.kt    // Banner sutil de upgrade/pro
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │   ├── Conversation.kt     // Modelo de domínio desacoplado
│   │   │   │   └── ChatMessage.kt      // Mensagem de domínio
│   │   │   └── repository/
│   │   │       └── ChatRepository.kt   // Interface abstrata do repositório
│   │   └── data/
│   │       └── ChatRepositoryImpl.kt   // Implementação unificando Room e Mock/Gemini
│   ├── drawer/
│   │   └── DrawerContent.kt            // Itens de menu, recentes e rodapé com avatar
│   ├── projects/
│   │   └── ProjectsPlaceholder.kt      // Tela/diálogo informativo de "Criar projeto"
│   └── artifacts/
│       └── ArtifactsPlaceholder.kt     // Tela/diálogo informativo de "Artefatos"
├── navigation/
│   ├── AgriiaDestinations.kt           // Rotas de navegação (Splash, Chat, etc.)
│   └── AgriiaNavHost.kt                // NavHost conectando as telas
├── AgriiaApplication.kt                // Inicializador da aplicação e do AppContainer
└── MainActivity.kt                     // Ponto de entrada, enableEdgeToEdge e Theme
```

### Justificativa das Decisões de Estrutura
- **Separação `core` vs `feature`**: Isola componentes reutilizáveis de design system, banco de dados e utilitários. Qualquer nova feature (ex.: "Artefatos" ou "Projetos") consome o `core` sem acoplamento cruzado.
- **Camadas `ui`, `domain`, `data` dentro de `feature/chat`**: Garante que regras de negócio não conheçam classes de UI ou dependam de detalhes do banco de dados (Clean Architecture).
- **AppContainer Nativo**: Evita falhas de sincronização de plugins Hilt no ambiente conteinerizado e fornece injeção de dependências limpa, testável e manutenível.

---

## 5. Dependências e Propósito

Todas as bibliotecas já estão mapeadas no `libs.versions.toml` do projeto e serão aproveitadas com precisão:

| Dependência | Versão / Biblioteca | Propósito |
|---|---|---|
| **Jetpack Compose BOM** | `compose-bom:2024.09.00` | Alinhamento de versões estáveis de UI Compose |
| **Material 3** | `androidx.compose.material3` | Sistema de design moderno, ColorScheme, Scaffold, NavigationDrawer |
| **Icons Extended** | `material-icons-extended` | Ícones para menu, fantasma (chat temporário), microfone, áudio, anexos |
| **Navigation Compose** | `androidx.navigation:navigation-compose:2.8.9` | Roteamento tipado e transições entre telas |
| **Lifecycle ViewModel Compose** | `lifecycle-viewmodel-compose:2.8.7` | Gestão de ciclo de vida e retenção de estado de UI |
| **Room Runtime & KTX** | `androidx.room:2.7.0` | Persistência local do histórico de conversas e mensagens |
| **Room KSP Compiler** | `room-compiler:2.7.0` | Geração de código em tempo de compilação para o Room |
| **DataStore Preferences** | `androidx.datastore:1.1.7` | Armazenamento reativo de preferências (nome, tema escuro, modelo) |
| **Retrofit & OkHttp** | `retrofit:2.12.0`, `okhttp:4.10.0` | Cliente HTTP para chamadas REST e streaming SSE do Gemini |
| **Moshi / Converter** | `converter-moshi:2.12.0` | Serialização e desserialização rápida de payloads JSON |
| **Coroutines Android** | `kotlinx-coroutines-android:1.10.2` | Fluxo assíncrono, StateFlow, Channel e background dispatchers |

---

## 6. Etapas de Implementação Numeradas

### Etapa 1: Fundação do Projeto, Configurações e Design System agriia
- **Entregável**: Pacote base atualizado para `com.agriia.app`, nomes e descrições alinhados em `metadata.json` e `res/values/strings.xml`, `AgriiaApplication` com container de injeção e pacote `core/designsystem` completo (paleta leitoso/lima, tipografia, ícone orgânico do agriia em vetor).
- **Critério de Conclusão**: Compilação bem-sucedida com `compile_applet` sem avisos ou conflitos de namespace.

### Etapa 2: Splash Screen com Transição Suave
- **Entregável**: Composable `SplashScreen` com fundo leitoso em degradê sutil, logotipo e tipografia orgânica, créditos discretos inferiores e animação fade-in com transição automática para a tela principal.
- **Critério de Conclusão**: Visualização fluida da splash com rota inicial configurada no `NavHost`.

### Etapa 3: Tela Inicial (Novo Chat) e Navigation Drawer com Dados Mock
- **Entregável**:
  - `ChatScreen` com saudação dinâmica ("Bom dia / Boa tarde / Boa noite, [Nome]"), ícone central, TopAppBar (menu e chat temporário) e caixa inferior completa (anexos `+`, seletor de modelo "Modelo X · Pensamento", microfone e modo de voz).
  - `AppNavigationDrawer` funcional contendo: "Conversas", "Criar projeto", "Artefatos", lista de "Recentes" com itens mockados reais e rodapé com avatar e botão de "Novo bate-papo".
  - Banner sutil de upgrade integrado.
- **Critério de Conclusão**: Drawer abre e fecha suavemente via botão de hambúrguer e gestos; campo de digitação aceita texto com expansão dinâmica; alternância de chat temporário altera o cabeçalho.

### Etapa 4: Camada de Domínio, Persistência Local (Room) e Estado
- **Entregável**: Entidades e DAOs do Room para conversas e mensagens, repositório local e `ChatViewModel` com `StateFlow` gerenciando criação de chats, gravação de mensagens e listagem de recentes em tempo real.
- **Critério de Conclusão**: Ao enviar uma mensagem na interface, a conversa é persistida no banco de dados local e aparece imediatamente na lista de "Recentes" do drawer.

### Etapa 5: Resposta de IA com Efeito Streaming Simulado e Integração Gemini
- **Entregável**:
  - Mecanismo de streaming de texto reativo no ViewModel com renderização token-a-token fluida.
  - Integração do cliente Retrofit Gemini via `BuildConfig.GEMINI_API_KEY` para quando a chave for ativada no ambiente.
- **Critério de Conclusão**: Mensagens do assistente são digitadas em tempo real com indicador visual de geração e cancelamento de resposta.

---

## 7. Riscos e Decisões Técnicas

1. **Streaming em Tempo Real (SSE vs Chunks)**:
   - *Decisão*: Utilizar `@Streaming` do Retrofit com leitura contínua de linhas `data:` do stream do Gemini (`streamGenerateContent?alt=sse`).
   - *Mitigação*: Implementar `Flow<String>` com buffer desacoplado no repositório, garantindo que reconexões ou erros na rede não quebrem o estado da UI.
2. **Insets de Tela e Área Segura (Edge-to-Edge)**:
   - *Decisão*: Uso mandatório de `enableEdgeToEdge()` com tratamento explícito de `WindowInsets.safeDrawing`, `statusBarsPadding()` e `navigationBarsPadding()` na barra inferior customizada e no drawer, garantindo compatibilidade com entalhes de câmera e navegação por gestos.
3. **Trabalho Offline & Resiliência**:
   - *Decisão*: Salvar sempre a mensagem do usuário no Room antes de disparar a chamada de rede. Caso ocorra erro de conexão, o status da mensagem é atualizado para `Failed` com opção de reenvio com um clique.
4. **Permissões de Microfone e Áudio**:
   - *Decisão*: Declarar `RECORD_AUDIO` no manifesto para a funcionalidade de ditado/voz e solicitar em tempo de execução via `rememberLauncherForActivityResult` apenas no momento do toque no botão de microfone.

---

## Próximo Passo

Aguardando sua aprovação para iniciar a **Etapa 1** (Setup de arquitetura, configurações base e Design System).
