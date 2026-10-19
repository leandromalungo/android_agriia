# agriia - Android

**agriia** é um cliente de chat moderno e inteligente para Android construído com **Kotlin**, **Jetpack Compose** e **Material 3**, seguindo rigorosamente os padrões de **Clean Architecture** e diretrizes oficiais do Google (Guide to App Architecture).

---

## Árvore de Pastas do Projeto

```
app/src/main/java/com/agriia/app/
├── core/
│   ├── common/
│   │   ├── Result.kt                   # Sealed interface para estados de sucesso, erro e loading
│   │   ├── DateUtils.kt                # Utilitários de saudação dinâmica ("Bom dia", etc.)
│   │   └── Extensions.kt               # Extensões úteis do Kotlin e Compose
│   ├── database/                       # Camada de persistência local SQLite com Room
│   │   ├── AgriiaDatabase.kt           # Instância singleton do RoomDatabase
│   │   ├── dao/
│   │   │   ├── ConversationDao.kt      # Operações de CRUD de conversas
│   │   │   └── MessageDao.kt           # Operações de mensagens e histórico
│   │   └── entity/
│   │       ├── ConversationEntity.kt   # Tabela de conversas
│   │       └── MessageEntity.kt        # Tabela de mensagens com chave estrangeira
│   ├── datastore/                      # Armazenamento reativo de preferências com DataStore
│   │   └── UserPreferencesRepository.kt# Chaves de nome do usuário, modelo e tema
│   ├── designsystem/                   # Sistema de design exclusivo do agriia
│   │   ├── Color.kt                    # Paleta orgânica leitoso (#FAFAF2) e verde-lima (#EEF5D0)
│   │   ├── Theme.kt                    # AgriiaTheme com suporte a Light e Dark mode
│   │   ├── Type.kt                     # Tipografia serifada elegante e sans-serif moderna
│   │   ├── Shape.kt                    # Cantos arredondados, pílulas e balões de chat
│   │   └── components/
│   │       ├── AgriiaLogo.kt           # Ícone radial orgânico vetorial e logo serifado
│   │       ├── AgriiaTopAppBar.kt      # Barra superior com menu lateral e chat temporário
│   │       └── AgriiaChatInput.kt      # Campo arredondado, seletor de modelos, microfone e voz
│   └── di/                             # Injeção de dependências limpa e estável
│       ├── AppContainer.kt             # Container de instâncias singleton da aplicação
│       └── ViewModelFactory.kt         # Provedor tipado de ViewModels
├── feature/
│   ├── splash/                         # Tela de abertura
│   │   └── SplashScreen.kt             # Transição suave (fade), logo serifado e assinatura
│   ├── chat/                           # Feature principal de conversação
│   │   ├── domain/                     # Camada de Domínio (isolada, sem dependências de UI/DB)
│   │   │   ├── model/
│   │   │   │   ├── Conversation.kt     # Entidade de domínio de conversa
│   │   │   │   └── ChatMessage.kt      # Entidade de domínio de mensagem
│   │   │   └── repository/
│   │   │       └── ChatRepository.kt   # Contrato abstrato do repositório
│   │   ├── data/                       # Camada de Dados (Room + Mock / Gemini Streaming)
│   │   │   └── ChatRepositoryImpl.kt   # Implementação com histórico e streaming de texto
│   │   └── ui/                         # Camada de Apresentação (Jetpack Compose)
│   │       ├── ChatScreen.kt           # Tela principal do chat
│   │       ├── ChatViewModel.kt        # Gestão de estado reativo (StateFlow)
│   │       ├── ChatUiState.kt          # Modelo imutável de estado de tela
│   │       └── components/
│   │           ├── GreetingHeader.kt   # Saudação personalizada centralizada
│   │           ├── MessageBubble.kt    # Balões de mensagem com cursor de digitação
│   │           ├── ModelSelectorDialog.kt # Seletor de modelos e raciocínio
│   │           └── UpgradeBanner.kt    # Banner de recursos Pro
│   ├── drawer/                         # Menu lateral retrátil (Navigation Drawer)
│   │   └── DrawerContent.kt            # Navegação, recentes com reticências e rodapé
│   ├── projects/                       # Funcionalidade de Projetos
│   │   └── ProjectsDialog.kt           # Criação e gestão de contextos
│   └── artifacts/                      # Funcionalidade de Artefatos
│       └── ArtifactsDialog.kt          # Visualização de códigos e documentos gerados
├── navigation/
│   ├── AgriiaDestinations.kt           # Constantes de rotas tipadas
│   └── AgriiaNavHost.kt                # Navegador com transições de fade
├── AgriiaApplication.kt                # Classe Application que inicializa o AppContainer
└── MainActivity.kt                     # Ponto de entrada Android com edge-to-edge
```

---

## Explicação das Camadas e Decisões

1. **Camada de Apresentação (`ui`)**:
   - Desenvolvida inteiramente em **Jetpack Compose + Material 3**.
   - Fluxo de dados unidirecional: a UI emite eventos para o `ChatViewModel`, que atualiza um `StateFlow<ChatUiState>`.
   - Adere aos insets de tela do Android (`enableEdgeToEdge`, `statusBarsPadding`, `navigationBarsPadding`, `imePadding`) para que nenhum elemento seja encoberto pelo entalhe da câmera ou barra de navegação do sistema.

2. **Camada de Domínio (`domain`)**:
   - Totalmente independente de bibliotecas de terceiros ou frameworks de banco de dados.
   - Define os modelos `Conversation` e `ChatMessage` e a interface `ChatRepository`.

3. **Camada de Dados (`data` & `database`)**:
   - Implementa `ChatRepository` unificando o banco local **Room** (`AgriiaDatabase`) e simulação de streaming reativo via `Flow<String>`.
   - Popula automaticamente um conjunto de conversas recentes com títulos realistas (como na referência) caso o banco esteja vazio na primeira inicialização.

4. **Injeção de Dependências (`di`)**:
   - Adota o padrão nativo `AppContainer` recomendado pelo Google para projetos de arquitetura limpa, garantindo compilações instantâneas e sem conflitos de plugins no ambiente conteinerizado.
