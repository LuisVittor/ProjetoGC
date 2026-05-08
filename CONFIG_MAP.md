# CONFIG MAP

## Versão
- Versão do mapa de configuração: `1.0.0`
- Política de versionamento: Semântico (MAJOR.MINOR.PATCH)
  - MAJOR: alterações incompatíveis na definição de configuração
  - MINOR: novos itens de configuração ou melhorias compatíveis
  - PATCH: correções de texto/descrição e ajustes não incompatíveis

## Itens de Configuração (ICs)

### 1. Configuração geral do aplicativo
1. `APP_NAME`
   - Nome do aplicativo.
2. `APP_ENV`
   - Ambiente de execução do aplicativo.
3. `APP_LOCALE`
   - Localidade usada para formatação de data, número e moeda.
4. `APP_CURRENCY`
   - Moeda padrão exibida no sistema.
5. `APP_DATE_FORMAT`
   - Formato de data padrão.
6. `APP_TIME_ZONE`
   - Fuso horário padrão.
7. `APP_LOG_LEVEL`
   - Nível de log padrão para mensagens da aplicação.

### 2. Persistência de dados
8. `PERSISTENCE_DIRECTORY`
   - Diretório onde os dados serializados são armazenados.
9. `PERSISTENCE_FILENAME`
   - Nome base do arquivo de dados persistidos.
10. `PERSISTENCE_FILE_EXTENSION`
    - Extensão opcional do arquivo de persistência.
11. `PERSISTENCE_BACKUP_ENABLED`
    - Indica se backup automático está habilitado.
12. `PERSISTENCE_BACKUP_DIRECTORY`
    - Diretório de backup relativo ao diretório de persistência.
13. `PERSISTENCE_BACKUP_INTERVAL_DAYS`
    - Intervalo de backup em dias.
14. `Serializador.NOMEDIRETORIO`
    - Constante interna: `.criso`.
15. `Serializador.NOMEARQUIVO`
    - Constante interna: `dados`.

### 3. Comportamento da interface
16. `UI_THEME`
    - Tema da interface (ex: light, dark).
17. `UI_FONT_SIZE`
    - Tamanho base da fonte da interface.
18. `UI_MAX_RECENT_TRANSACTIONS`
    - Número máximo de transações recentes exibidas.
19. `UI_SHOW_TOOLTIPS`
    - Exibir ou ocultar dicas na interface.

### 4. Regras de validação e negócio
20. `ALLOW_NEGATIVE_BALANCE`
    - Permite ou bloqueia saldo negativo nos caixas.
21. `DEFAULT_CATEGORY_NAME`
    - Categoria padrão para transações sem categoria.
22. `DEFAULT_TRANSFER_DESCRIPTION`
    - Descrição padrão para transferências internas.
23. `MAX_CATEGORY_NAME_LENGTH`
    - Comprimento máximo permitido para nomes de categoria.
24. `MAX_TRANSACTION_DESCRIPTION_LENGTH`
    - Comprimento máximo permitido para descrições de transação.

### 5. Exportação de dados
25. `EXPORT_CSV_SEPARATOR`
    - Separador de campo para exportação CSV.
26. `EXPORT_DIRECTORY`
    - Diretório padrão para arquivos exportados.
27. `EXPORT_DEFAULT_FORMAT`
    - Formato padrão de exportação.

### 6. Debug e teste
28. `DEBUG_MODE`
    - Indica se o modo de depuração está ativo.

## Arquivos de Código do Projeto

### Pacote com.criso.app
- `App.java` - Classe principal de inicialização do aplicativo

### Pacote com.criso.model
- `Carteira.java` - Representa a carteira do usuário com múltiplos caixas
- `Caixa.java` - Representa uma conta ou carteira individual
- `Categoria.java` - Representa categorias de transações
- `Transacao.java` - Classe base para transações
- `Receita.java` - Transação de receita
- `Despesa.java` - Transação de despesa
- `Meta.java` - Metas financeiras
- `TipoCaixa.java` - Enum com tipos de caixa
- `StatusDespesa.java` - Enum com status de despesa
- `StatusMeta.java` - Enum com status de meta

### Pacote com.criso.exception
- `CaixaDeOrigemIgualAoCaixaDeDestinoException.java`
- `DuplicidadeNomeCaixaException.java`
- `DuplicidadeNomeCategoriaException.java`
- `NenhumaTransacaoNoPeriodoConsultadoException.java`
- `PeriodoDaConsultaDeTransacoesInvalidoException.java`
- `SaldoDoCaixaDeOrigemInsuficienteException.java`
- `TransacaoSemDataDefinidaException.java`
- `TransacaoSemValorDefinidoException.java`

### Pacote com.criso.persistencia
- `Serializador.java` - Responsável por serialização/deserialização de dados

### Pacote com.criso.ui
**Janelas principais:**
- `JanelaPrincipal.java` - Interface principal do aplicativo
- `JanelaDetalhesCaixa.java` - Detalhes de um caixa específico
- `JanelaTodasTransacoes.java` - Visualização de todas as transações
- `JanelaDetalhesTransacao.java` - Detalhes de uma transação

**Janelas de criação:**
- `JanelaCriarCaixa.java`
- `JanelaCriarDespesa.java`
- `JanelaCriarReceita.java`
- `JanelaCriarMeta.java`
- `JanelaCriarTransferencia.java`
- `JanelaEditarMeta.java`

**Painéis reutilizáveis:**
- `PainelHeader.java` - Cabeçalho da interface
- `PainelSaldoTotal.java` - Exibição do saldo total
- `PainelMeusCaixas.java` - Lista de caixas
- `PainelTransacoesRecentes.java` - Transações recentes
- `PainelMetasFinanceiras.java` - Metas financeiras
- `PainelArredondado.java` - Painel com bordas arredondadas (componente reutilizável)

**Componentes customizados:**
- `BotaoArredondado.java` - Botão com bordas arredondadas
- `BarraRolagemUI.java` - Customização de barra de rolagem
- `FonteInter.java` - Carregamento de fontes customizadas
- `IconeOperacao.java` - Ícones para operações
- `Paleta.java` - Definição de cores do aplicativo

### Recuros (scr/main/resources)
**Fontes:**
- `Inter-VariableFont_opsz,wght.ttf`
- `Inter-Italic-VariableFont_opsz,wght.ttf`

**Ícones:**
- `icone_de_despesas.png`
- `icone_de_receitas.png`
- `icone_de_transferencia.png`

**Logo:**
- `Logo_Criso_Cabeca_Lobo_Dourado.png`

### Testes
- `scr/test/java/com/criso/model/Testjunit.java` - Testes unitários com JUnit

### Documentação
- `README.md` - Este arquivo
- `CONFIG.ENV` - Arquivo de configuração com variáveis ambientais
- `CONFIG_MAP.md` - Mapa de configuração (este arquivo)
- `docs/diagramas.puml` - Diagramas UML do projeto

## Bibliotecas, frameworks e runtime
- `Java SE / JDK` (biblioteca padrão do projeto)
- `javax.swing` / `java.awt` (frameworks de UI Swing/AWT)
- `java.io` (serialização e IO de arquivos)
- `java.time` (datas e horas)
- `java.util` (coleções, Locale, UUID e streams)
- `java.math.BigDecimal` (valores financeiros)
- `java.text` (formatação de números e datas)
- `java.awt.event` (eventos de interface)
- `junit` (testes unitários)

> Observação: não há arquivo de build Maven/Gradle detectado no repositório, então não há dependências externas declaradas além do runtime padrão Java.

## Política de nomenclatura de versões

### Versionamento semântico: MAJOR.MINOR.PATCH

#### MAJOR (primeira posição)
Incrementa quando há mudanças **incompatíveis** e quebra compatibilidade com versões anteriores.

**Exemplos de mudança MAJOR:**
- Renomear uma chave de configuração existente (ex: `APP_NAME` → `APPLICATION_NAME`)
- Remover um IC que outras partes do sistema dependem
- Mudar o tipo ou comportamento fundamental de um IC (ex: `ALLOW_NEGATIVE_BALANCE` de boolean para string)
- Alterar a formato de persistência de dados de forma que dados antigos não são compatíveis

#### MINOR (segunda posição)
Incrementa quando há **novas funcionalidades ou ICs** adicionados de forma compatível.

**Exemplos de mudança MINOR:**
- Adicionar novo IC como `EXPORT_JSON_ENABLED`
- Incluir nova classe de modelo como `Investimento.java`
- Expandir funcionalidade sem quebrar código existente

#### PATCH (terceira posição)
Incrementa para correções e ajustes que **não afetam compatibilidade**.

**Exemplos de mudança PATCH:**
- Corrigir valor padrão de um IC (ex: `UI_FONT_SIZE=14` → `UI_FONT_SIZE=13`)
- Atualizar descrição ou documentação de um IC
- Corrigir bugs no código sem alterar interface pública

### Exemplos de versões do projeto:
- `1.0.0` - versão inicial estável (atual)
- `1.0.1` - correção de bug na persistência de dados
- `1.1.0` - adição de novo IC `ENCRYPTION_ENABLED`
- `1.1.1` - ajuste de descrição na documentação
- `2.0.0` - mudança incompatível: renomeação de diretório `.criso` → `.gc` (quebra compatibilidade)

### Relação entre versionamento e Git
- Tags Git devem seguir o padrão: `V{MAJOR}.{MINOR}.{PATCH}` (ex: `V1.0.0`, `V1.1.0`, `V2.0.0`)
- O arquivo `README.md` deve sempre conter a versão atual
- O arquivo `CONFIG_MAP.md` deve ser atualizado quando ICs são adicionados ou removidos
- O arquivo `CONFIG.ENV` deve refletir valores padrão correspondentes à versão

## Versão atual
- Versão do projeto: `1.0.0`
- Versão do mapa de configuração: `1.0.0`
- Tag Git associada: `V1.0.0`
