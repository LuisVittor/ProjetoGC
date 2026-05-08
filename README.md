# Identificação do Projeto

## Ramo

Financeiro

## Líderes do Projeto e Autores da Documentação

* Kesley Soares da Silva
* Matheus Fagundes Soares
* Luis Vittor Ferreira Nunes

## Escopo do Projeto

### Necessidades

* Há uma dificuldade em controlar dados pessoais descentralizados de diferentes contas bancárias, despesas e fontes de renda;
* Não há uma boa visualização de orçamentos, metas e fluxos financeiros pessoais.

### Escopo do produto

O sistema fornecerá uma interface, dotada de ferramentas que permitam aos usuários registar saldo, ganhos e gastos de dinheiro. Dessa forma, cada usuário poderá saber da sua saúde financeira e a origem dos seus gastos e suas receitas, fornecendo controle e visualização do fluxo financeiro dele.
Esse sistema também permitirá movimentar o dinheiro entre carteiras/bancos, mantendo um registro.

## Fontes de requisitos

* Actual Budget;
* Mobills Controle Financeiro;
* Ynab.;

## Ferramentas/Técnicas de elicitação de requisitos

* Análise de similares;
* Brainstorms;

## Requisitos de Software

### Histórias de Usuário

#### `[H.U. 1]` Monitoramento da carteira do usuário
> **COMO** usuário,
> **QUERO** visualizar meus dados financeiros reunidos e sintetizados, bem como ter acesso o caixas e transações
> **PARA** monitorar e registrar minha saúde financeira.

#### `[H.U. 2]` Cadastro de Transações
> **COMO** usuário,
> **QUERO** registrar detalhadamente meus gastos e ganhos (incluindo valor, data, descrição e categoria),
> **PARA** ter um histórico completo e preciso do meu fluxo de caixa e poder consultá-lo a qualquer momento.

#### `[H.U. 3]` Categorização de Transações
> **COMO** usuário,
> **QUERO** criar e gerenciar categorias e subcategorias personalizadas (ex: "Alimentação", "Lazer", "Essenciais"),
> **PARA** organizar minhas transações de forma significativa e entender para onde meu dinheiro está indo.

#### `[H.U. 4]` Exportação de Dados
> **COMO** usuário,
> **QUERO** exportar meus dados de transações para um período selecionado, em formatos comuns como CSV,
> **PARA** poder fazer backups dos meus registros financeiros ou analisá-los externamente em outras ferramentas.

### Critérios de Aceitação

#### `[C.A. 1]` Monitoramento da Carteira do Usuário (Ref. `H.U. 1`)

**Cenário 1.1: Visualização de Dados Consolidados**
> **DADO** que o usuário acessou o sistema e possui transações e caixas registradas
> **QUANDO** o usuário acessa a tela principal
> **ENTÃO** o sistema deve exibir um resumo do saldo total de todas os caixas.
> **E** o sistema deve apresentar um resumo das receitas e despesas do período atual (ex: mês corrente).

**Cenário 1.2: Acesso a Caixas Individuais**
> **DADO** que o usuário está na tela de visualização de caixas
> **QUANDO** o usuário seleciona umo caixa específica
> **ENTÃO** o sistema deve exibir o saldo atual do caixa selecionada.
> **E** o sistema deve listar as últimas transações associadas a esso caixa.

**Cenário 1.3: Acesso a Transações**
> **DADO** que o usuário está visualizando umo caixa ou um relatório
> **QUANDO** o usuário seleciona a opção de ver todas as transações
> **ENTÃO** o sistema deve exibir uma lista paginada de todas as transações, permitindo filtros básicos (ex: por data, tipo).

#### `[C.A. 2]` Cadastro de Transações (Ref. `H.U. 2`)

**Cenário 2.1: Registro de Despesa Detalhada**
> **DADO** que o usuário selecionou a opção de adicionar uma nova transação
> **QUANDO** o usuário preenche o valor, data, descrição, seleciona uma categoria, escolhe o caixa de origem e marca como "Despesa"
> **E** clica em "Salvar"
> **ENTÃO** o sistema deve registrar a nova transação de despesa com todos os detalhes fornecidos.
> **E** o saldo do caixa de origem deve ser atualizado (diminuído).
> **E** uma mensagem de sucesso deve ser exibida.

**Cenário 2.2: Registro de Receita Detalhada**
> **DADO** que o usuário selecionou a opção de adicionar uma nova transação
> **QUANDO** o usuário preenche o valor, data, descrição, seleciona uma categoria, escolhe o caixa de destino e marca como "Receita"
> **E** clica em "Salvar"
> **ENTÃO** o sistema deve registrar a nova transação de receita com todos os detalhes fornecidos.
> **E** o saldo do caixa de destino deve ser atualizado (aumentado).
> **E** uma mensagem de sucesso deve ser exibida.

**Cenário 2.3: Consulta ao Histórico de Transações**
> **DADO** que o usuário possui transações registradas
> **QUANDO** o usuário acessa a seção de histórico de transações
> **ENTÃO** o sistema deve exibir uma lista de transações, mostrando pelo menos data, descrição, valor e tipo (receita/despesa).
> **E** o sistema deve permitir filtrar as transações por período, caixa e categoria.

#### `[C.A. 3]` Categorização de Transações (Ref. `H.U. 3`)

**Cenário 3.1: Criação de Nova Categoria Principal**
> **DADO** que o usuário está na área de gerenciamento de categorias
> **QUANDO** o usuário seleciona a opção de criar nova categoria, informa um nome para a categoria (ex: "Moradia") e salva
> **ENTÃO** o sistema deve registrar a nova categoria.
> **E** a nova categoria deve estar disponível para seleção ao registrar ou editar transações.

**Cenário 3.2: Criação de Nova Subcategoria**
> **DADO** que o usuário está na área de gerenciamento de categorias e selecionou uma categoria principal
> **QUANDO** o usuário seleciona a opção de criar nova subcategoria, informa um nome para a subcategoria (ex: "Aluguel" dentro de "Moradia") e salva
> **ENTÃO** o sistema deve registrar a nova subcategoria vinculada à categoria principal.
> **E** a nova subcategoria deve estar disponível para seleção ao registrar ou editar transações.

**Cenário 3.3: Associação de Categoria a uma Transação**
> **DADO** que o usuário está registrando ou editando uma transação
> **QUANDO** o usuário seleciona uma categoria e/ou subcategoria da lista de categorias disponíveis
> **ENTÃO** a transação deve ser associada à categoria/subcategoria selecionada.

**Cenário 3.4: Visualização de Gastos por Categoria**
> **DADO** que o usuário possui transações categorizadas
> **QUANDO** o usuário acessa um relatório de despesas por categoria
> **ENTÃO** o sistema deve exibir um resumo (lista ou gráfico) dos gastos agrupados por categoria e/ou subcategoria em um período selecionado.

#### `[C.A. 4]` Exportação de Dados (Ref. `H.U. 4`)

**Cenário 4.1: Exportação de Transações em CSV**
> **DADO** que o usuário está na seção de transações ou relatórios
> **QUANDO** o usuário seleciona um período específico (ex: último mês, ano corrente, intervalo personalizado)
> **E** seleciona a opção de exportar dados no formato CSV
> **E** clica em "Exportar"
> **ENTÃO** o sistema deve gerar um arquivo CSV contendo as transações do período selecionado, incluindo data, descrição, valor, tipo, categoria e caixa.
> **E** o sistema deve permitir o download do arquivo gerado.

**Cenário 4.2: Seleção de Período para Exportação**
> **DADO** que o usuário deseja exportar dados
> **QUANDO** o usuário acessa a funcionalidade de exportação
> **ENTÃO** o sistema deve oferecer opções para selecionar um período pré-definido (ex: "Mês Atual", "Mês Anterior", "Ano Atual") ou um intervalo de datas personalizado.

### Requisitos Funcionais

“O sistema deve”

1.  `[R.F. 1]` Permitir o cadastro de usuários.
2.  `[R.F. 2]` Permitir a autenticação de usuários.
3.  `[R.F. 3]` Permitir o cadastro de múltiplos caixas (ex: caixa corrente, poupança, carteira física, cartão de crédito, cheque, bitcoin, fundos de investimentos).
4.  `[R.F. 4]` Exibir o saldo individual de cada caixa.
5.  `[R.F. 5]` Exibir o saldo consolidado de todos os caixas do usuário.
6.  `[R.F. 6]` Permitir o registro de transações de receita, informando valor, data, descrição, caixa de destino e categoria.
7.  `[R.F. 7]` Permitir o registro de transações de despesa, informando valor, data, descrição, caixa de origem e categoria.
8.  `[R.F. 8]` Permitir a criação, edição e exclusão de categorias personalizadas para receitas e despesas.
9.  `[R.F. 9]` Permitir a criação, edição e exclusão de subcategorias personalizadas, vinculadas a uma categoria principal.
10. `[R.F. 10]` Permitir associar transações a categorias e subcategorias.
11. `[R.F. 11]` Listar o histórico de transações realizadas.
12. `[R.F. 12]` Permitir filtrar o histórico de transações por data, tipo (receita/despesa), caixa e categoria.
13. `[R.F. 13]` Permitir a edição dos detalhes de uma transação registrada (exceto, talvez, o tipo de transação após conciliação).
14. `[R.F. 14]` Permitir a exclusão de transações registradas.
15. `[R.F. 15]` Permitir a transferência de valores entre caixas/carteiras do próprio usuário, registrando como uma transação de saída na origem e uma de entrada no destino.
16. `[R.F. 16]` Apresentar relatórios visuais (gráficos de pizza, barras) de receitas e despesas por categoria em um período selecionado.
17. `[R.F. 17]` Apresentar a evolução do saldo ao longo do tempo (gráfico de linha).
18. `[R.F. 18]` Permitir a exportação do histórico de transações para formatos comuns (CSV).
19. `[R.F. 19]` Permitir a edição dos dados de um caixa/carteira (ex: nome).
20. `[R.F. 20]` Permitir a exclusão de um caixa/carteira (somente se não houver transações associadas ou com aviso de arquivamento).
21. `[R.F. 21]` O sistema deve permitir definir um saldo inicial ao criar uma novo caixa/carteira.

## Casos de Uso

### Atores:

* **Usuário:** Pessoa que utiliza o sistema para gerenciar suas finanças pessoais.

---

### 1: Registrar Nova Transação

* **Ator:** Usuário
* **Requisitos Atendidos:** `R.F. 6`, `R.F. 7`, `R.F. 10`, `H.U. 2`
* **Fluxo Principal:**
    1.  Usuário seleciona a opção para adicionar uma nova transação (ex: botão "+").
    2.  Sistema apresenta um formulário para nova transação.
    3.  Usuário seleciona o tipo de transação: "Receita" ou "Despesa".
    4.  Usuário insere o valor da transação.
    5.  Usuário seleciona a Data da transação (padrão: data atual).
    6.  Usuário pode inserir uma descrição para a transação.
    7.  Usuário seleciona o caixa onde a transação ocorreu (origem para despesa, destino para receita).
    8.  Usuário seleciona uma Categoria (e opcionalmente uma Subcategoria) para a transação.
    9.  Usuário clica em "Registrar" ou "Salvar".
    10. Sistema valida os dados (campos obrigatórios, formato do valor).
    11. Sistema guarda a transação associada ao caixa e categoria selecionadas.
    12. Sistema atualiza o saldo do caixa correspondente (aumenta para receita, diminui para despesa).
    13. Sistema exibe uma mensagem de confirmação e atualiza a lista de transações ou o dashboard.
* **Fluxos Alternativos:**
    * **10.a. Dados Inválidos:**
        1.  Sistema exibe uma mensagem de erro indicando os campos inválidos ou ausentes.
        2.  Usuário corrige os dados e retorna ao passo 9 do Fluxo Principal.
    * **8.a. Nova Categoria durante o Registro:**
        1.  Usuário não encontra a categoria desejada.
        2.  Sistema permite acesso rápido à funcionalidade de "Criar Nova Categoria" (ver Caso de Uso 4).
        3.  Após criar a categoria, usuário retorna ao formulário de transação e seleciona a nova categoria.

---

### 2: Criar Novo Caixa

* **Ator:** Usuário
* **Requisitos Atendidos:** `R.F. 3`, `R.F. 21`
* **Fluxo Principal:**
    1.  Usuário seleciona a opção para adicionar um novo caixa (ex: na seção "Meus Caixas").
    2.  Sistema apresenta um formulário para novo caixa.
    3.  Usuário insere o Nome do caixa (ex: "Banco X", "Carteira Pessoal").
    4.  Usuário insere o Saldo Inicial do caixa.
    5.  (Opcional) Usuário seleciona o tipo de caixa (ex: Conta-Corrente, Poupança, Cartão de Crédito, Dinheiro, Cheque, Bitcoin).
    6.  Usuário clica em "Salvar" ou "Criar".
    7.  Sistema valida os dados (nome é obrigatório, saldo inicial é um número válido).
    8.  Sistema cria a novo caixa/carteira com o nome e saldo inicial fornecidos.
    9.  Sistema atualiza a lista de caixas/carteiras visível para o usuário.
    10. Sistema exibe uma mensagem de confirmação.
* **Fluxos Alternativos:**
    * **7.a. Dados Inválidos:**
        1.  Sistema exibe uma mensagem de erro indicando os campos inválidos.
        2.  Usuário corrige os dados e retorna ao passo 6 do Fluxo Principal.

---

### 3: Visualizar Resumo Financeiro e Detalhes do caixa

* **Ator:** Usuário
* **Requisitos Atendidos:** `R.F. 4`, `R.F. 5`, `R.F. 11`, `R.F. 16`, `R.F. 17`, `H.U. 1`
* **Fluxo Principal (Dashboard/Tela Inicial):**
    1.  Usuário acessa o sistema.
    2.  Sistema exibe a tela principal (Carteira).
    3.  Sistema apresenta o saldo consolidado de todos os caixas.
    4.  Sistema exibe um resumo de receitas e despesas do período atual.
    5.  Sistema lista as últimas transações recentes de todos os caixas.
    6.  Sistema lista os caixas do usuário com seus respectivos saldos individuais.
* **Fluxo Principal (Detalhes do Caixa):**
    1.  Usuário, a partir de uma lista de caixas, seleciona um caixa específico.
    2.  Sistema exibe uma tela com os detalhes do caixa selecionado.
    3.  Sistema apresenta o saldo atual do caixa.
    4.  Sistema lista o histórico de transações específico deste caixa, permitindo filtros.

---

### 4: Gerenciar Categorias e Subcategorias

* **Ator:** Usuário
* **Requisitos Atendidos:** `R.F. 8`, `R.F. 9`, `H.U. 3`

#### Sub-Caso de Uso 4.1: Criar Categoria/Subcategoria

* **Fluxo Principal:**
    1.  Usuário acessa a seção de gerenciamento de categorias.
    2.  Para criar categoria principal: Usuário seleciona "Nova Categoria".
    3.  Para criar subcategoria: Usuário seleciona uma categoria principal existente e depois "Nova Subcategoria".
    4.  Sistema solicita o nome da nova categoria/subcategoria.
    5.  Usuário insere o nome e confirma.
    6.  Sistema valida o nome (ex: não pode ser vazio, não pode duplicar dentro do mesmo nível).
    7.  Sistema salva a nova categoria/subcategoria.
    8.  Sistema atualiza a lista de categorias/subcategorias.
    9.  Sistema exibe mensagem de sucesso.

#### Sub-Caso de Uso 4.2: Editar Categoria/Subcategoria

* **Fluxo Principal:**
    1.  Usuário acessa a seção de gerenciamento de categorias.
    2.  Usuário seleciona a categoria/subcategoria que deseja editar.
    3.  Usuário seleciona a opção "Editar".
    4.  Sistema exibe o nome atual e permite a alteração.
    5.  Usuário altera o nome e confirma.
    6.  Sistema valida e salva a alteração.
    7.  Sistema atualiza a lista e exibe mensagem de sucesso.

#### Sub-Caso de Uso 4.3: Excluir Categoria/Subcategoria

* **Fluxo Principal:**
    1.  Usuário acessa a seção de gerenciamento de categorias.
    2.  Usuário seleciona a categoria/subcategoria que deseja excluir.
    3.  Usuário seleciona a opção "Excluir".
    4.  Sistema solicita confirmação da exclusão.
    5.  **Regra de Negócio:** Se a categoria/subcategoria estiver em uso por transações, o sistema pode impedir a exclusão ou oferecer a opção de re-categorizar as transações afetadas.
    6.  Usuário confirma.
    7.  Sistema remove a categoria/subcategoria.
    8.  Sistema atualiza a lista e exibe mensagem de sucesso.
* **Fluxos Alternativos (para todos os sub-casos):**
    * **Validação Falha:** Sistema informa o erro (ex: nome duplicado) e permite correção.

---

### 5: Exportar Dados de Transações

* **Ator:** Usuário
* **Requisitos Atendidos:** `R.F. 18`, `H.U. 4`
* **Fluxo Principal:**
    1.  Usuário acessa a funcionalidade de exportação de dados (ex: em "Relatórios" ou "Configurações").
    2.  Sistema apresenta opções para a exportação.
    3.  Usuário seleciona o período desejado para a exportação (ex: "Mês atual", "Ano passado", intervalo de datas personalizado).
    4.  Usuário seleciona o formato do arquivo (ex: CSV).
    5.  (Opcional) Usuário pode selecionar caixas específicas para exportar.
    6.  Usuário clica no botão "Exportar" ou "Gerar Relatório".
    7.  Sistema processa as transações do período e caixas selecionadas.
    8.  Sistema gera o arquivo no formato especificado.
    9.  Sistema disponibiliza o arquivo para download pelo usuário.
    10. Sistema exibe uma mensagem de sucesso ou notificação de que o arquivo está pronto.
* **Fluxos Alternativos:**
    * **3.a. Período Inválido:** Se o usuário inserir um intervalo de datas inválido (ex: data final anterior à inicial), o sistema informa o erro e solicita correção.
    * **7.a. Nenhuma Transação no Período:** Se não houver transações para os critérios selecionados, o sistema informa o usuário e não gera um arquivo vazio, ou gera um arquivo com cabeçalhos apenas.

---

### 6: Realizar Transferência Entre Caixas

* **Ator:** Usuário
* **Requisitos Atendidos:** `R.F. 15` (Escopo do produto)
* **Fluxo Principal:**
    1.  Usuário seleciona a opção de "Transferência entre caixas".
    2.  Sistema apresenta um formulário de transferência.
    3.  Usuário seleciona o "Caixa de Origem" (de onde o dinheiro sairá).
    4.  Usuário seleciona o "Caixa de Destino" (para onde o dinheiro irá).
    5.  **Regra de Negócio:** Caixa de origem e destino não podem ser o mesmo.
    6.  Usuário insere o "Valor" da transferência.
    7.  Usuário insere a "Data" da transferência (padrão: data atual).
    8.  (Opcional) Usuário pode adicionar uma "Descrição" para a transferência (ex: "Cobrir saldo X").
    9.  Usuário clica em "Transferir" ou "Salvar".
    10. Sistema valida os dados (caixas diferentes, valor positivo, saldo suficiente no caixa de origem se aplicável).
    11. Sistema registra duas transações:
        * Uma transação de "Despesa de Transferência" no Caixa de Origem.
        * Uma transação de "Receita de Transferência" no Caixa de Destino.
    12. (Opcional) Ambas transações podem ser vinculadas ou marcadas como transferência para fácil identificação e para não serem contabilizadas como novas receitas/despesas no orçamento geral.
    13. Sistema atualiza o saldo do Caixa de Origem (diminui).
    14. Sistema atualiza o saldo do Caixa de Destino (aumenta).
    15. Sistema exibe uma mensagem de confirmação.
* **Fluxos Alternativos:**
    * **9.a. Dados Inválidos/Saldo Insuficiente:**
        1.  Sistema exibe uma mensagem de erro apropriada (ex: "Caixa de origem e destino devem ser diferentes", "Saldo insuficiente no caixa de origem").
        2.  Usuário corrige os dados e retorna ao passo 8 do Fluxo Principal.

---

### 7: Editar Transação Existente

* **Ator:** Usuário
* **Requisitos Atendidos:** `R.F. 13`
* **Fluxo Principal:**
    1.  Usuário localiza a transação que deseja editar (ex: na lista de histórico de transações).
    2.  Usuário seleciona a opção "Editar" para a transação escolhida.
    3.  Sistema apresenta o formulário de transação preenchido com os dados da transação selecionada.
    4.  Usuário modifica os campos desejados (valor, data, descrição, caixa, categoria).
    5.  **Regra de Negócio:** O tipo de transação (Receita/Despesa) pode ser bloqueado para edição ou sua alteração pode ter implicações mais complexas (ex: exigir recálculo de saldos históricos de forma diferente).
    6.  Usuário clica em "Salvar Alterações".
    7.  Sistema valida os novos dados.
    8.  Sistema atualiza a transação no banco de dados.
    9.  Sistema recalcula os saldos dos caixas afetadas (a original e a nova, se o caixa foi alterada; ou apenas o saldo do caixa se o valor foi alterado).
    10. Sistema exibe uma mensagem de confirmação e atualiza a visualização da lista de transações.
* **Fluxos Alternativos:**
    * **6.a. Dados Inválidos:**
        1.  Sistema exibe uma mensagem de erro.
        2.  Usuário corrige os dados e retorna ao passo 5 do Fluxo Principal.

---

### 8: Excluir Transação Existente

* **Ator:** Usuário
* **Requisitos Atendidos:** `R.F. 14`
* **Fluxo Principal:**
    1.  Usuário localiza a transação que deseja excluir na lista de histórico de transações.
    2.  Usuário seleciona a opção "Excluir" para a transação escolhida.
    3.  Sistema solicita confirmação da exclusão: "Tem certeza que deseja excluir esta transação?".
    4.  Usuário confirma a exclusão.
    5.  Sistema remove a transação do banco de dados.
    6.  Sistema recalcula o saldo do caixa à qual a transação estava associada (aumenta se era despesa, diminui se era receita).
    7.  Sistema exibe uma mensagem de confirmação e atualiza a visualização da lista de transações.
* **Fluxos Alternativos:**
    * **4.a. Usuário Cancela Exclusão:**
        1.  Usuário seleciona "Cancelar".
        2.  O caso de uso é encerrado sem alterações.
