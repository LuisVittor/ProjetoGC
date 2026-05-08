package com.criso.ui;

import com.criso.model.*;
import com.criso.persistencia.Serializador;
import com.criso.exception.DuplicidadeNomeCaixaException;
import com.criso.exception.NenhumaTransacaoNoPeriodoConsultadoException;
import com.criso.exception.PeriodoDaConsultaDeTransacoesInvalidoException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class JanelaDetalhesCaixa extends JDialog {

    private final transient Caixa caixaOriginal;
    private final transient Carteira carteira;
    private final transient Runnable onCaixaAtualizadoCallback;

    private JTextField campoNomeCaixa;
    private JFormattedTextField campoSaldoAtual;

    private JComboBox<String> comboFiltroTipo;
    private JComboBox<Categoria> comboFiltroCategoria;
    private JTextField campoDataInicio;
    private JTextField campoDataFim;
    private JPanel painelListaTransacoes;
    private JScrollPane scrollPaneTransacoes;

    public JanelaDetalhesCaixa(Frame parent, Caixa caixa, Carteira carteira, Runnable onCaixaAtualizadoCallback) {
        super(parent, "Detalhes do Caixa: " + caixa.getNome(), true);
        this.caixaOriginal = caixa;
        this.carteira = carteira;
        this.onCaixaAtualizadoCallback = onCaixaAtualizadoCallback;
        initComponents();
        setupLayout();
        carregarDadosCaixa();
        carregarTransacoes();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(700, 600);
        setMinimumSize(new Dimension(650, 550));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(Paleta.Azul, 1));

        campoNomeCaixa = new JTextField(20);

        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.of("pt", "BR"));
        decimalFormat.setParseBigDecimal(true);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        campoSaldoAtual = new JFormattedTextField(decimalFormat);

        comboFiltroTipo = new JComboBox<>(new String[]{"Todos", "Receita", "Despesa", "Transferência"});
        estilizarComboBox(comboFiltroTipo, FonteInter.getRegular(12f));

        comboFiltroCategoria = new JComboBox<>();
        popularComboCategorias();
        estilizarComboBox(comboFiltroCategoria, FonteInter.getRegular(12f));

        campoDataInicio = new JTextField(LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
        estilizarCampo(campoDataInicio, FonteInter.getRegular(12f));

        campoDataFim = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
        estilizarCampo(campoDataFim, FonteInter.getRegular(12f));

        painelListaTransacoes = new JPanel();
        painelListaTransacoes.setLayout(new BoxLayout(painelListaTransacoes, BoxLayout.Y_AXIS));
        painelListaTransacoes.setBackground(Paleta.Branco);
        scrollPaneTransacoes = new JScrollPane(painelListaTransacoes);
        scrollPaneTransacoes.setBorder(null);
        scrollPaneTransacoes.getViewport().setBackground(Paleta.Branco);
        scrollPaneTransacoes.getVerticalScrollBar().setUI(new BarraRolagemUI());
        scrollPaneTransacoes.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPaneTransacoes.getVerticalScrollBar().setUnitIncrement(16);
    }

    private void setupLayout() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(20, 20));
        painelPrincipal.setBackground(Paleta.FundoAplicacao);
        painelPrincipal.setBorder(new EmptyBorder(25, 25, 25, 25));

        painelPrincipal.add(criarPainelCabecalho(), BorderLayout.NORTH);

        JPanel painelConteudoCentro = new JPanel();
        painelConteudoCentro.setLayout(new GridBagLayout());
        painelConteudoCentro.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        JLabel lblNomeCaixa = new JLabel("Nome do Caixa:");
        lblNomeCaixa.setFont(FonteInter.getBold(15f));
        lblNomeCaixa.setForeground(Paleta.TextoCorpo);
        painelConteudoCentro.add(lblNomeCaixa, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        estilizarCampo(campoNomeCaixa, FonteInter.getRegular(15f));
        painelConteudoCentro.add(campoNomeCaixa, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel lblSaldoAtual = new JLabel("Saldo Atual:");
        lblSaldoAtual.setFont(FonteInter.getBold(15f));
        lblSaldoAtual.setForeground(Paleta.TextoCorpo);
        painelConteudoCentro.add(lblSaldoAtual, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        estilizarCampo(campoSaldoAtual, FonteInter.getRegular(15f));
        painelConteudoCentro.add(campoSaldoAtual, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 15, 0);
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Paleta.Azul.brighter());
        painelConteudoCentro.add(separator, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        painelConteudoCentro.add(criarPainelTransacoes(), gbc);

        painelPrincipal.add(painelConteudoCentro, BorderLayout.CENTER);

        JPanel painelBotoesInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        painelBotoesInferior.setOpaque(false);
        painelBotoesInferior.setBorder(new EmptyBorder(0, 0, 0, 0));

        BotaoArredondado btnSalvar = new BotaoArredondado("Salvar");
        btnSalvar.setBackground(Paleta.VerdeEscuro);
        btnSalvar.setForeground(Paleta.Branco);
        btnSalvar.setFont(FonteInter.getBold(14f));
        btnSalvar.setRaioDaBorda(12);
        btnSalvar.setBorder(new EmptyBorder(6, 18, 6, 18));
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> salvarAlteracoesCaixa());
        painelBotoesInferior.add(btnSalvar);

        BotaoArredondado btnExcluir = new BotaoArredondado("Excluir");
        btnExcluir.setBackground(Paleta.Vermelho);
        btnExcluir.setForeground(Paleta.Branco);
        btnExcluir.setFont(FonteInter.getBold(14f));
        btnExcluir.setRaioDaBorda(12);
        btnExcluir.setBorder(new EmptyBorder(6, 18, 6, 18));
        btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(e -> excluirCaixa());
        painelBotoesInferior.add(btnExcluir);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.setFont(FonteInter.getBold(14f));
        btnFechar.setForeground(Paleta.Azul);
        btnFechar.setContentAreaFilled(false);
        btnFechar.setBorderPainted(false);
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addActionListener(e -> dispose());
        painelBotoesInferior.add(btnFechar);

        painelPrincipal.add(painelBotoesInferior, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
        pack();
    }

    private JPanel criarPainelCabecalho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);
        painel.setBorder(new EmptyBorder(0, 0, 0, 0));

        JLabel titulo = new JLabel("Detalhes do Caixa");
        titulo.setFont(FonteInter.getBold(24f));
        titulo.setForeground(Paleta.TextoTitulo);
        painel.add(titulo, BorderLayout.WEST);

        return painel;
    }

    private JPanel criarPainelTransacoes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setOpaque(false);
        painel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 0, 0, 0),
                BorderFactory.createTitledBorder(
                        BorderFactory.createEmptyBorder(),
                        "Transações do Caixa",
                        TitledBorder.LEADING, TitledBorder.TOP,
                        FonteInter.getBold(16f), Paleta.TextoTitulo
                )
        ));

        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelFiltros.setOpaque(false);
        painelFiltros.setBorder(new EmptyBorder(0, 0, 0, 0));

        JButton btnAplicarFiltro = new BotaoArredondado("Aplicar");
        btnAplicarFiltro.setBackground(Paleta.Azul);
        btnAplicarFiltro.setForeground(Paleta.Branco);
        btnAplicarFiltro.setFont(FonteInter.getBold(12f));
        btnAplicarFiltro.setBorder(new EmptyBorder(6, 12, 6, 12));
        btnAplicarFiltro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAplicarFiltro.addActionListener(e -> carregarTransacoes());

        painelFiltros.add(new JLabel("Tipo:"));
        painelFiltros.add(comboFiltroTipo);
        painelFiltros.add(new JLabel("Categoria:"));
        painelFiltros.add(comboFiltroCategoria);
        painelFiltros.add(new JLabel("De:"));
        painelFiltros.add(campoDataInicio);
        painelFiltros.add(new JLabel("Até:"));
        painelFiltros.add(campoDataFim);
        painelFiltros.add(btnAplicarFiltro);

        painel.add(painelFiltros, BorderLayout.NORTH);
        painel.add(scrollPaneTransacoes, BorderLayout.CENTER);

        return painel;
    }

    private void estilizarCampo(JTextField campo, Font fonte) {
        campo.setFont(fonte);
        campo.setForeground(Paleta.TextoCorpo);
        campo.setBackground(Paleta.Branco);
        campo.setCaretColor(Paleta.Azul);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Paleta.Azul.brighter()),
                new EmptyBorder(4, 0, 4, 0)
        ));
    }

    private <T> void estilizarComboBox(JComboBox<T> comboBox, Font fonte) {
        comboBox.setFont(fonte);
        comboBox.setForeground(Paleta.TextoCorpo);
        comboBox.setBackground(Paleta.Branco);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Paleta.Azul.brighter()),
                new EmptyBorder(0, 0, 0, 0)
        ));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Categoria) {
                    setText(((Categoria) value).getNome());
                } else if (value instanceof String) {
                    setText((String) value);
                }
                if (isSelected) {
                    setBackground(Paleta.Azul);
                    setForeground(Paleta.Branco);
                } else {
                    setBackground(Paleta.Branco);
                    setForeground(Paleta.TextoCorpo);
                }
                return this;
            }
        });
    }

    private void carregarDadosCaixa() {
        campoNomeCaixa.setText(caixaOriginal.getNome());
        campoSaldoAtual.setValue(caixaOriginal.getSaldoAtual());
    }

    private void salvarAlteracoesCaixa() {
        String novoNome = campoNomeCaixa.getText().trim();
        BigDecimal novoSaldo;

        try {
            novoSaldo = (BigDecimal) campoSaldoAtual.getValue();
            if (novoSaldo == null) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um saldo válido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (ClassCastException | NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Formato de saldo inválido. Use apenas números e vírgula para decimais.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (novoNome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome do caixa não pode ser vazio.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            for (Caixa c : carteira.getCaixas()) {
                if (!c.getId().equals(caixaOriginal.getId()) && c.getNome().equalsIgnoreCase(novoNome)) {
                    throw new DuplicidadeNomeCaixaException();
                }
            }

            caixaOriginal.setNome(novoNome);
            caixaOriginal.setSaldoAtual(novoSaldo);

            Serializador.serializar(carteira);

            JOptionPane.showMessageDialog(this, "Caixa atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            if (onCaixaAtualizadoCallback != null) {
                onCaixaAtualizadoCallback.run();
            }
            dispose();
        } catch (DuplicidadeNomeCaixaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao Salvar Caixa", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluirCaixa() {
        if (!carteira.getTransacoes().stream().filter(t -> {
            if (t instanceof Receita) {
                return ((Receita) t).getCaixaQueRecebe().getId().equals(caixaOriginal.getId());
            } else if (t instanceof Despesa) {
                return ((Despesa) t).getCaixaQuePaga().getId().equals(caixaOriginal.getId()) ||
                       (((Despesa) t).getCaixaQueRecebe() != null && ((Despesa) t).getCaixaQueRecebe().getId().equals(caixaOriginal.getId()));
            }
            return false;
        }).collect(Collectors.toList()).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este caixa possui transações associadas e não pode ser excluído.", "Erro ao Excluir", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                String.format("Tem certeza que deseja excluir o caixa '%s'? Esta ação é irreversível.", caixaOriginal.getNome()),
                "Confirmar Exclusão de Caixa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            carteira.removerCaixa(caixaOriginal);
            Serializador.serializar(carteira);

            if (onCaixaAtualizadoCallback != null) {
                onCaixaAtualizadoCallback.run();
            }

            JOptionPane.showMessageDialog(this,
                    String.format("Caixa '%s' excluído com sucesso!", caixaOriginal.getNome()),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    private void popularComboCategorias() {
        comboFiltroCategoria.removeAllItems();
        comboFiltroCategoria.addItem(new Categoria("Todas", ""));

        for (Categoria cat : JanelaCriarReceita.CategoriaReceita) {
            comboFiltroCategoria.addItem(cat);
        }
        for (Categoria cat : JanelaCriarDespesa.CategoriasDespesa) {
            comboFiltroCategoria.addItem(cat);
        }
    }

    private void carregarTransacoes() {
        painelListaTransacoes.removeAll();

        List<Transacao> transacoesFiltradas = new ArrayList<>();
        try {
            final LocalDate[] dataInicioHolder = new LocalDate[1];
            final LocalDate[] dataFimHolder = new LocalDate[1];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            if (!campoDataInicio.getText().trim().isEmpty()) {
                dataInicioHolder[0] = LocalDate.parse(campoDataInicio.getText().trim(), formatter);
            }
            if (!campoDataFim.getText().trim().isEmpty()) {
                dataFimHolder[0] = LocalDate.parse(campoDataFim.getText().trim(), formatter);
            }
            final LocalDate dataInicio = dataInicioHolder[0];
            final LocalDate dataFim = dataFimHolder[0];

            if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
                throw new PeriodoDaConsultaDeTransacoesInvalidoException("A data de início não pode ser posterior à data de fim.");
            }

            transacoesFiltradas = carteira.getTransacoes().stream()
                    .filter(t -> {
                        if (t instanceof Receita) {
                            return ((Receita) t).getCaixaQueRecebe().getId().equals(caixaOriginal.getId());
                        } else if (t instanceof Despesa) {
                            return ((Despesa) t).getCaixaQuePaga().getId().equals(caixaOriginal.getId()) ||
                                   (((Despesa) t).getCaixaQueRecebe() != null && ((Despesa) t).getCaixaQueRecebe().getId().equals(caixaOriginal.getId()));
                        }
                        return false;
                    })
                    .filter(this::filtroPorTipo)
                    .filter(this::filtroPorCategoria)
                    .filter(t -> {
                        LocalDate dataTransacao = t.getData();
                        boolean dataDentroDoPeriodo = true;

                        if (dataInicio != null && dataTransacao.isBefore(dataInicio)) {
                            dataDentroDoPeriodo = false;
                        }
                        if (dataFim != null && dataTransacao.isAfter(dataFim)) {
                            dataDentroDoPeriodo = false;
                        }
                        return dataDentroDoPeriodo;
                    })
                    .sorted((t1, t2) -> t2.getData().compareTo(t1.getData()))
                    .collect(Collectors.toList());

            if (transacoesFiltradas.isEmpty()) {
                throw new NenhumaTransacaoNoPeriodoConsultadoException();
            }

            for (Transacao transacao : transacoesFiltradas) {
                painelListaTransacoes.add(criarItemTransacao(transacao));
            }

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use DD/MM/AAAA.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (PeriodoDaConsultaDeTransacoesInvalidoException | NenhumaTransacaoNoPeriodoConsultadoException e) {
            JLabel lblErro = new JLabel(e.getMessage());
            lblErro.setFont(FonteInter.getRegular(14f));
            lblErro.setForeground(Paleta.Vermelho);
            lblErro.setAlignmentX(Component.CENTER_ALIGNMENT);
            painelListaTransacoes.add(lblErro);
        } finally {
            painelListaTransacoes.revalidate();
            painelListaTransacoes.repaint();
        }
    }

    private boolean filtroPorTipo(Transacao transacao) {
        String tipoSelecionado = (String) comboFiltroTipo.getSelectedItem();
        if ("Todos".equals(tipoSelecionado)) {
            return true;
        } else if ("Receita".equals(tipoSelecionado)) {
            return transacao instanceof Receita;
        } else if ("Despesa".equals(tipoSelecionado)) {
            return transacao instanceof Despesa && ((Despesa) transacao).getCaixaQueRecebe() == null;
        } else if ("Transferência".equals(tipoSelecionado)) {
            return transacao instanceof Despesa && ((Despesa) transacao).getCaixaQueRecebe() != null;
        }
        return false;
    }

    private boolean filtroPorCategoria(Transacao transacao) {
        Categoria categoriaSelecionada = (Categoria) comboFiltroCategoria.getSelectedItem();
        if (categoriaSelecionada == null || "Todas".equals(categoriaSelecionada.getNome())) {
            return true;
        }
        return transacao.getCategoria() != null &&
               transacao.getCategoria().getNome().equalsIgnoreCase(categoriaSelecionada.getNome());
    }

    private JPanel criarItemTransacao(Transacao transacao) {
        JPanel painelItem = new JPanel(new BorderLayout(10, 0));
        painelItem.setOpaque(false);
        painelItem.setBorder(new EmptyBorder(8, 5, 8, 5));
        painelItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        painelItem.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nomeTransacao = new JLabel(transacao.getNome());
        nomeTransacao.setFont(FonteInter.getRegular(14f));
        nomeTransacao.setForeground(Paleta.TextoCorpo);

        JLabel valorTransacao = new JLabel(NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(transacao.getValor()));
        valorTransacao.setFont(FonteInter.getBold(14f));
        Color originalValorColor;

        if (transacao instanceof Receita) {
            valorTransacao.setForeground(Paleta.VerdeClaro.darker());
            originalValorColor = Paleta.VerdeClaro.darker();
        } else if (transacao instanceof Despesa) {
            if (((Despesa) transacao).getCaixaQueRecebe() != null) {
                valorTransacao.setForeground(Paleta.Azul.darker());
                nomeTransacao.setText(nomeTransacao.getText() + " (Transf. para " + ((Despesa) transacao).getCaixaQueRecebe().getNome() + ")");
                originalValorColor = Paleta.Azul.darker();
            } else {
                valorTransacao.setForeground(Paleta.Vermelho.darker());
                originalValorColor = Paleta.Vermelho.darker();
            }
        } else {
            originalValorColor = Paleta.TextoDoValor;
        }

        JLabel dataTransacao = new JLabel(transacao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dataTransacao.setFont(FonteInter.getRegular(12f));
        dataTransacao.setForeground(Paleta.TextoCorpo.darker());

        JPanel painelInfo = new JPanel(new BorderLayout());
        painelInfo.setOpaque(false);
        painelInfo.add(nomeTransacao, BorderLayout.WEST);
        painelInfo.add(dataTransacao, BorderLayout.EAST);

        painelItem.add(painelInfo, BorderLayout.CENTER);
        painelItem.add(valorTransacao, BorderLayout.EAST);

        painelItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        painelItem.addMouseListener(new MouseAdapter() {
            private Color originalNomeColor = nomeTransacao.getForeground();
            private Color originalDataColor = dataTransacao.getForeground();

            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(JanelaDetalhesCaixa.this,
                        "Detalhes da Transação:\n" +
                        "Nome: " + transacao.getNome() + "\n" +
                        "Valor: " + NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(transacao.getValor()) + "\n" +
                        "Data: " + transacao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                        "Tipo: " + (transacao instanceof Receita ? "Receita" : (transacao instanceof Despesa && ((Despesa)transacao).getCaixaQueRecebe() != null ? "Transferência" : "Despesa")) + "\n" +
                        "Categoria: " + (transacao.getCategoria() != null ? transacao.getCategoria().getNome() : "N/A"),
                        "Detalhes da Transação",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                nomeTransacao.setForeground(Paleta.Azul);
                valorTransacao.setForeground(Paleta.Azul);
                dataTransacao.setForeground(Paleta.Azul);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nomeTransacao.setForeground(originalNomeColor);
                valorTransacao.setForeground(originalValorColor);
                dataTransacao.setForeground(originalDataColor);
            }
        });

        return painelItem;
    }
}