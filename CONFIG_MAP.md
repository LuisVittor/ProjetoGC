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

## Bibliotecas, frameworks e runtime
- `Java SE / JDK` (biblioteca padrão do projeto)
- `javax.swing` / `java.awt` (frameworks de UI Swing/AWT)
- `java.io` (serialização e IO de arquivos)
- `java.time` (datas e horas)
- `java.util` (coleções, Locale, UUID e streams)
- `java.math.BigDecimal` (valores financeiros)

> Observação: não há arquivo de build Maven/Gradle detectado no repositório, então não há dependências externas declaradas além do runtime padrão Java.

## Política de nomenclatura de versões
- A versão do projeto e dos itens de configuração deve seguir **versionamento semântico**: `MAJOR.MINOR.PATCH`.
- Exemplo de versões:
  - `1.0.0` - versão inicial estável do mapa de configuração.
  - `1.1.0` - adiciona novas chaves de configuração, mantendo compatibilidade.
  - `1.1.1` - ajustes de descrição ou correções de configuração.
  - `2.0.0` - mudanças incompatíveis na forma como as configurações são lidas ou no significado das chaves.
- Regra de atualização de versão:
  - alterar MAJOR quando modificar ou remover ICs existentes de forma incompatível.
  - alterar MINOR quando adicionar novos ICs ou comportamentos compatíveis.
  - alterar PATCH para correções de documentação, nomes ou valores padrão sem impacto de compatibilidade.

## Versão do arquivo de configuração
- Este documento declara a versão `1.0.0` como versão inicial do mapa de configuração.
