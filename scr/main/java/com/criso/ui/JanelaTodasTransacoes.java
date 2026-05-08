package com.criso.ui;

import com.criso.model.*;
import com.criso.persistencia.Serializador;
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
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.stream.Collectors;

public class JanelaTodasTransacoes extends JDialog {

    private final transient Carteira carteira;
    private final transient Runnable onTransacoesAtualizadasCallback;

    private JComboBox<String> comboFiltroTipo;
    private JComboBox<Caixa> comboFiltroCaixa;
    private JComboBox<Categoria> comboFiltroCategoria;
    private JComboBox<StatusDespesa> comboFiltroStatusDespesa;
    private JTextField campoDataInicio;
    private JTextField campoDataFim;

    private JPanel painelListaTransacoes;
    private JPanel painelGraficosConteudo;
    private JLabel lblNenhumaTransacaoOuErro;

    private JComboBox<String> comboTipoGrafico;

    private JPanel painelConteudoPrincipalRolavel;
    private JScrollPane scrollPanePrincipalDaJanela;

    public JanelaTodasTransacoes(Frame parent, Carteira carteira, Runnable onTransacoesAtualizadasCallback) {
        super(parent, "Todas as Transações", true);
        this.carteira = carteira;
        this.onTransacoesAtualizadasCallback = onTransacoesAtualizadasCallback;
        initComponents();
        setupLayout();
        carregarTransacoes();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(1000, 800);
        setMinimumSize(new Dimension(900, 720));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(Paleta.Azul, 1));

        comboFiltroTipo = new JComboBox<>(new String[]{"Todos", "Receita", "Despesa", "Transferência"});
        estilizarComboBox(comboFiltroTipo, FonteInter.getRegular(12f));

        comboFiltroCaixa = new JComboBox<>();
        popularComboCaixas();
        estilizarComboBox(comboFiltroCaixa, FonteInter.getRegular(12f));

        comboFiltroCategoria = new JComboBox<>();
        popularComboCategorias();
        estilizarComboBox(comboFiltroCategoria, FonteInter.getRegular(12f));

        comboFiltroStatusDespesa = new JComboBox<>(StatusDespesa.values());
        comboFiltroStatusDespesa.insertItemAt(null, 0);
        comboFiltroStatusDespesa.setSelectedItem(null);
        estilizarComboBox(comboFiltroStatusDespesa, FonteInter.getRegular(12f));

        campoDataInicio = new JTextField(LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
        estilizarCampo(campoDataInicio, FonteInter.getRegular(12f));
        campoDataFim = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 10);
        estilizarCampo(campoDataFim, FonteInter.getRegular(12f));

        painelListaTransacoes = new JPanel();
        painelListaTransacoes.setLayout(new BoxLayout(painelListaTransacoes, BoxLayout.Y_AXIS));
        painelListaTransacoes.setBackground(Paleta.Branco);
        painelListaTransacoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        painelListaTransacoes.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelListaTransacoes.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        painelGraficosConteudo = new JPanel(new CardLayout());
        painelGraficosConteudo.setOpaque(false);
        painelGraficosConteudo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Gráficos por Categoria",
                TitledBorder.LEADING, TitledBorder.TOP,
                FonteInter.getBold(16f), Paleta.TextoTitulo
        ));
        painelGraficosConteudo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelGraficosConteudo.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));


        lblNenhumaTransacaoOuErro = new JLabel("");
        lblNenhumaTransacaoOuErro.setFont(FonteInter.getRegular(14f));
        lblNenhumaTransacaoOuErro.setForeground(Paleta.Vermelho);
        lblNenhumaTransacaoOuErro.setHorizontalAlignment(SwingConstants.CENTER);

        comboTipoGrafico = new JComboBox<>(new String[]{"Despesas por Categoria", "Receitas por Categoria"});
        estilizarComboBox(comboTipoGrafico, FonteInter.getRegular(12f));
        comboTipoGrafico.addActionListener(e -> actualizarGraficos(getTransacoesAtualmenteFiltradas()));
    }

    private void setupLayout() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(20, 20));
        painelPrincipal.setBackground(Paleta.Branco);
        painelPrincipal.setBorder(new EmptyBorder(25, 25, 25, 25));

        painelPrincipal.add(criarPainelCabecalho(), BorderLayout.NORTH);

        painelConteudoPrincipalRolavel = new JPanel();
        painelConteudoPrincipalRolavel.setLayout(new BoxLayout(painelConteudoPrincipalRolavel, BoxLayout.Y_AXIS));
        painelConteudoPrincipalRolavel.setOpaque(false);
        painelConteudoPrincipalRolavel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelConteudoPrincipalRolavel.setBorder(new EmptyBorder(0, 0, 0, 15));

        JPanel painelFiltrosComBotao = new JPanel(new BorderLayout());
        painelFiltrosComBotao.setOpaque(false);
        painelFiltrosComBotao.add(criarPainelFiltrosInner(), BorderLayout.CENTER);

        JPanel painelAplicarFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        painelAplicarFiltros.setOpaque(false);
        BotaoArredondado btnAplicarFiltro = new BotaoArredondado("Aplicar Filtros");
        btnAplicarFiltro.setBackground(Paleta.VerdeEscuro);
        btnAplicarFiltro.setForeground(Paleta.Amarelo);
        btnAplicarFiltro.setFont(FonteInter.getBold(12f));
        btnAplicarFiltro.setRaioDaBorda(8);
        btnAplicarFiltro.setBorder(new EmptyBorder(6, 12, 6, 12));
        btnAplicarFiltro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAplicarFiltro.addActionListener(e -> carregarTransacoes());
        painelAplicarFiltros.add(btnAplicarFiltro);
        painelFiltrosComBotao.add(painelAplicarFiltros, BorderLayout.EAST);

        painelFiltrosComBotao.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelFiltrosComBotao.setMaximumSize(new Dimension(Integer.MAX_VALUE, painelFiltrosComBotao.getPreferredSize().height));

        painelConteudoPrincipalRolavel.add(painelFiltrosComBotao);
        painelConteudoPrincipalRolavel.add(Box.createVerticalStrut(15));

        JPanel painelControleGrafico = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelControleGrafico.setOpaque(false);
        painelControleGrafico.add(new JLabel("Mostrar Gráfico:"));
        painelControleGrafico.add(comboTipoGrafico);

        painelControleGrafico.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelControleGrafico.setMaximumSize(new Dimension(Integer.MAX_VALUE, painelControleGrafico.getPreferredSize().height));

        painelConteudoPrincipalRolavel.add(painelControleGrafico);
        painelConteudoPrincipalRolavel.add(Box.createVerticalStrut(5));

        painelGraficosConteudo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelConteudoPrincipalRolavel.add(painelGraficosConteudo);
        painelConteudoPrincipalRolavel.add(Box.createVerticalStrut(20));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Paleta.VerdeEscuro);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel separatorWrapper = new JPanel(new BorderLayout());
        separatorWrapper.setOpaque(false);
        separatorWrapper.setBorder(new EmptyBorder(0, 10, 0, 10));
        separatorWrapper.add(separator, BorderLayout.CENTER);
        separatorWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        separatorWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, separatorWrapper.getPreferredSize().height));

        painelConteudoPrincipalRolavel.add(separatorWrapper);
        painelConteudoPrincipalRolavel.add(Box.createVerticalStrut(20));

        painelConteudoPrincipalRolavel.add(painelListaTransacoes);

        scrollPanePrincipalDaJanela = new JScrollPane(painelConteudoPrincipalRolavel);
        scrollPanePrincipalDaJanela.setBorder(null);
        scrollPanePrincipalDaJanela.getViewport().setBackground(Paleta.Branco);
        scrollPanePrincipalDaJanela.getVerticalScrollBar().setUI(new BarraRolagemUI());
        scrollPanePrincipalDaJanela.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPanePrincipalDaJanela.getVerticalScrollBar().setUnitIncrement(16);
        scrollPanePrincipalDaJanela.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        painelPrincipal.add(scrollPanePrincipalDaJanela, BorderLayout.CENTER);

        JPanel painelBotoesInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        painelBotoesInferior.setOpaque(false);
        painelBotoesInferior.setBorder(new EmptyBorder(15, 0, 0, 0));

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
        painel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel titulo = new JLabel("Todas as Transações");
        titulo.setFont(FonteInter.getBold(24f));
        titulo.setForeground(Paleta.TextoTitulo);
        painel.add(titulo, BorderLayout.WEST);

        return painel;
    }

    private JPanel criarPainelFiltrosInner() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painel.setOpaque(false);

        painel.add(new JLabel("Tipo:"));
        painel.add(comboFiltroTipo);
        painel.add(new JLabel("Caixa:"));
        painel.add(comboFiltroCaixa);
        painel.add(new JLabel("Categoria:"));
        painel.add(comboFiltroCategoria);
        painel.add(new JLabel("Status Despesa:"));
        painel.add(comboFiltroStatusDespesa);
        painel.add(new JLabel("De:"));
        painel.add(campoDataInicio);
        painel.add(new JLabel("Até:"));
        painel.add(campoDataFim);

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
                if (value instanceof Caixa) {
                    setText(((Caixa) value).getNome());
                } else if (value instanceof Categoria) {
                    setText(((Categoria) value).getNome());
                } else if (value instanceof StatusDespesa) {
                    setText(((StatusDespesa) value).name());
                } else if (value == null) {
                    setText("Todos");
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

    private void popularComboCaixas() {
        comboFiltroCaixa.removeAllItems();
        DefaultComboBoxModel<Caixa> model = new DefaultComboBoxModel<>();
        model.addElement(null);
        for (Caixa caixa : carteira.getCaixas()) {
            model.addElement(caixa);
        }
        comboFiltroCaixa.setModel(model);
        comboFiltroCaixa.setSelectedItem(null);
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

    private List<Transacao> getTransacoesAtualmenteFiltradas() {
        LocalDate dataInicio = null;
        LocalDate dataFim = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            if (!campoDataInicio.getText().trim().isEmpty()) {
                dataInicio = LocalDate.parse(campoDataInicio.getText().trim(), formatter);
            }
            if (!campoDataFim.getText().trim().isEmpty()) {
                dataFim = LocalDate.parse(campoDataFim.getText().trim(), formatter);
            }

            if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
                throw new PeriodoDaConsultaDeTransacoesInvalidoException("A data de início não pode ser posterior à data de fim.");
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use DD/MM/AAAA.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        } catch (PeriodoDaConsultaDeTransacoesInvalidoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro de Período", JOptionPane.WARNING_MESSAGE);
            return new ArrayList<>();
        }

        final LocalDate finalDataInicio = dataInicio;
        final LocalDate finalDataFim = dataFim;

        List<Transacao> transacoesFiltradas = carteira.getTransacoes().stream()
                .filter(t -> filtroPorTipo(t) && filtroPorCaixa(t) && filtroPorCategoria(t) && filtroPorStatusDespesa(t))
                .filter(t -> {
                    LocalDate dataTransacao = t.getData();
                    boolean dataDentroDoPeriodo = true;
                    if (finalDataInicio != null && dataTransacao.isBefore(finalDataInicio)) {
                        dataDentroDoPeriodo = false;
                    }
                    if (finalDataFim != null && dataTransacao.isAfter(finalDataFim)) {
                        dataDentroDoPeriodo = false;
                    }
                    return dataDentroDoPeriodo;
                })
                .sorted(Comparator.comparing(Transacao::getData).reversed())
                .collect(Collectors.toList());

        return transacoesFiltradas;
    }

    private void carregarTransacoes() {
        painelListaTransacoes.removeAll();
        lblNenhumaTransacaoOuErro.setText("");

        List<Transacao> transacoesFiltradas = getTransacoesAtualmenteFiltradas();

        try {
            if (transacoesFiltradas.isEmpty()) {
                throw new NenhumaTransacaoNoPeriodoConsultadoException();
            }

            for (Transacao transacao : transacoesFiltradas) {
                painelListaTransacoes.add(criarItemDeTransacao(transacao));
            }
            lblNenhumaTransacaoOuErro.setText("");
        } catch (NenhumaTransacaoNoPeriodoConsultadoException e) {
            lblNenhumaTransacaoOuErro.setText(e.getMessage());
            JPanel errorPanel = new JPanel(new BorderLayout());
            errorPanel.setOpaque(false);
            errorPanel.add(lblNenhumaTransacaoOuErro, BorderLayout.CENTER);
            painelListaTransacoes.add(errorPanel);
        } finally {
            painelListaTransacoes.revalidate();
            painelListaTransacoes.repaint();

            painelGraficosConteudo.revalidate();
            painelGraficosConteudo.repaint();

            actualizarGraficos(transacoesFiltradas);

            if (scrollPanePrincipalDaJanela != null) {
                painelConteudoPrincipalRolavel.revalidate();
                painelConteudoPrincipalRolavel.repaint();
                scrollPanePrincipalDaJanela.revalidate();
                scrollPanePrincipalDaJanela.repaint();
            }
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

    private boolean filtroPorCaixa(Transacao transacao) {
        Caixa caixaSelecionado = (Caixa) comboFiltroCaixa.getSelectedItem();
        if (caixaSelecionado == null) {
            return true;
        }

        if (transacao instanceof Receita) {
            return ((Receita) transacao).getCaixaQueRecebe().getId().equals(caixaSelecionado.getId());
        } else if (transacao instanceof Despesa) {
            return ((Despesa) transacao).getCaixaQuePaga().getId().equals(caixaSelecionado.getId()) ||
                    (((Despesa) transacao).getCaixaQueRecebe() != null && ((Despesa) transacao).getCaixaQueRecebe().getId().equals(caixaSelecionado.getId()));
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

    private boolean filtroPorStatusDespesa(Transacao transacao) {
        StatusDespesa statusSelecionado = (StatusDespesa) comboFiltroStatusDespesa.getSelectedItem();
        if (statusSelecionado == null) {
            return true;
        }
        if (transacao instanceof Despesa) {
            return ((Despesa) transacao).getStatus() == statusSelecionado;
        }
        return true;
    }

    private JPanel criarItemDeTransacao(Transacao transacao) {
        JPanel painelItem = new JPanel(new BorderLayout(10, 0));
        painelItem.setOpaque(false);
        painelItem.setBorder(new EmptyBorder(10, 5, 10, 5));
        painelItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        painelItem.setPreferredSize(new Dimension(painelItem.getPreferredSize().width, 70));
        painelItem.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel painelIcone = new JPanel(new GridBagLayout());
        painelIcone.setPreferredSize(new Dimension(48, 48));
        painelIcone.setOpaque(false);
        painelIcone.setBackground(Paleta.Branco);

        Color corPrincipal;
        Color corFundoIcone;
        String caminhoIcone;
        String prefixoValor;

        if (transacao instanceof Despesa despesa && despesa.getCaixaQueRecebe() != null) {
            corPrincipal = Paleta.Azul;
            corFundoIcone = new Color(Paleta.Azul.getRed(), Paleta.Azul.getGreen(), Paleta.Azul.getBlue(), 25);
            caminhoIcone = "/icones/icone_de_transferencia.png";
            prefixoValor = "";
        } else if (transacao instanceof Despesa) {
            corPrincipal = Paleta.Vermelho;
            corFundoIcone = new Color(Paleta.Vermelho.getRed(), Paleta.Vermelho.getGreen(), Paleta.Vermelho.getBlue(), 25);
            caminhoIcone = "/icones/icone_de_despesas.png";
            prefixoValor = "- ";
        } else if (transacao instanceof Receita) {
            corPrincipal = Paleta.VerdeClaro;
            corFundoIcone = new Color(Paleta.VerdeClaro.getRed(), Paleta.VerdeClaro.getGreen(), Paleta.VerdeClaro.getBlue(), 25);
            caminhoIcone = "/icones/icone_de_receitas.png";
            prefixoValor = "+ ";
        } else {
            corPrincipal = Color.GRAY;
            corFundoIcone = new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), 25);
            caminhoIcone = "/icones/default_transacao.png";
            prefixoValor = "";
        }

        JPanel circuloFundo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(corFundoIcone);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        circuloFundo.setOpaque(false);
        circuloFundo.setPreferredSize(new Dimension(48, 48));
        circuloFundo.setLayout(new GridBagLayout());

        JLabel iconeLabel;
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(caminhoIcone));
            Image image = icon.getImage();
            Image newimg = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            iconeLabel = new JLabel(new ImageIcon(newimg));
        } catch (Exception e) {
            iconeLabel = new JLabel("?");
            iconeLabel.setForeground(corPrincipal);
            iconeLabel.setFont(FonteInter.getBold(20f));
            iconeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iconeLabel.setVerticalAlignment(SwingConstants.CENTER);
            System.err.println("Erro ao carregar ícone: " + caminhoIcone + " - " + e.getMessage());
        }

        circuloFundo.add(iconeLabel);
        painelIcone.add(circuloFundo);

        JPanel painelInfo = new JPanel();
        painelInfo.setLayout(new BoxLayout(painelInfo, BoxLayout.Y_AXIS));
        painelInfo.setOpaque(false);
        painelInfo.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel nomeTransacao = new JLabel(transacao.nome);
        nomeTransacao.setFont(FonteInter.getBold(15f));
        nomeTransacao.setForeground(Paleta.TextoTitulo);
        nomeTransacao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel detalhesAdicionais = new JLabel();
        detalhesAdicionais.setFont(FonteInter.getRegular(13f));
        detalhesAdicionais.setForeground(Paleta.Azul);
        detalhesAdicionais.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (transacao instanceof Despesa despesa) {
            if (despesa.getCaixaQueRecebe() != null) {
                detalhesAdicionais.setText("De: " + despesa.getCaixaQuePaga().getNome() + " Para: " + despesa.getCaixaQueRecebe().getNome());
            } else {
                detalhesAdicionais.setText("Caixa: " + despesa.getCaixaQuePaga().getNome() + " | Status: " + despesa.getStatus().name());
            }
        } else if (transacao instanceof Receita receita) {
            detalhesAdicionais.setText("Caixa: " + receita.getCaixaQueRecebe().getNome());
        }

        painelInfo.add(nomeTransacao);
        painelInfo.add(Box.createVerticalStrut(2));
        painelInfo.add(detalhesAdicionais);

        JLabel valorTransacao = new JLabel(prefixoValor + NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(transacao.getValor()));
        valorTransacao.setFont(FonteInter.getBold(16f));
        valorTransacao.setForeground(corPrincipal);

        JPanel painelValorAcao = new JPanel(new BorderLayout());
        painelValorAcao.setOpaque(false);
        painelValorAcao.add(valorTransacao, BorderLayout.NORTH);

        JLabel dataTransacaoLabel = new JLabel(transacao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
        dataTransacaoLabel.setFont(FonteInter.getRegular(13f));
        dataTransacaoLabel.setForeground(Paleta.Azul);

        if (transacao instanceof Despesa despesa && despesa.getStatus() == StatusDespesa.Pendente) {
            BotaoArredondado btnPagar = new BotaoArredondado("Pagar");
            btnPagar.setBackground(Paleta.VerdeClaro);
            btnPagar.setForeground(Paleta.Branco);
            btnPagar.setFont(FonteInter.getBold(12f));
            btnPagar.setRaioDaBorda(8);
            btnPagar.setBorder(new EmptyBorder(4, 8, 4, 8));
            btnPagar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnPagar.addActionListener(e -> marcarDespesaComoPaga(despesa));

            JPanel painelPagar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            painelPagar.setOpaque(false);
            painelPagar.add(btnPagar);

            painelValorAcao.add(painelPagar, BorderLayout.SOUTH);
        } else {
            painelValorAcao.add(dataTransacaoLabel, BorderLayout.SOUTH);
        }

        painelItem.add(painelIcone, BorderLayout.WEST);
        painelItem.add(painelInfo, BorderLayout.CENTER);
        painelItem.add(painelValorAcao, BorderLayout.EAST);

        painelItem.addMouseListener(new MouseAdapter() {
            private Color originalNomeColor = nomeTransacao.getForeground();
            private Color originalDetalhesColor = detalhesAdicionais.getForeground();
            private Color originalValorColor = valorTransacao.getForeground();
            private Color originalDateColor = dataTransacaoLabel.getForeground();

            @Override
            public void mouseEntered(MouseEvent e) {
                nomeTransacao.setForeground(Paleta.Azul);
                detalhesAdicionais.setForeground(Paleta.Azul.darker());
                valorTransacao.setForeground(Paleta.Azul);
                dataTransacaoLabel.setForeground(Paleta.Azul);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nomeTransacao.setForeground(originalNomeColor);
                detalhesAdicionais.setForeground(originalDetalhesColor);
                valorTransacao.setForeground(originalValorColor);
                dataTransacaoLabel.setForeground(originalDateColor);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JanelaDetalhesTransacao dialog = new JanelaDetalhesTransacao(
                        (Frame) SwingUtilities.getWindowAncestor(JanelaTodasTransacoes.this),
                        transacao,
                        carteira,
                        onTransacoesAtualizadasCallback
                );
                dialog.setVisible(true);
                carregarTransacoes();
            }
        });

        return painelItem;
    }

    private void marcarDespesaComoPaga(Despesa despesa) {
        int confirmacao = JOptionPane.showConfirmDialog(this,
                String.format("Tem certeza que deseja marcar a despesa '%s' como Paga?", despesa.getNome()),
                "Confirmar Pagamento", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            despesa.setStatus(StatusDespesa.Pago);
            carteira.atualizarTransacao(despesa);
            JOptionPane.showMessageDialog(this,
                    String.format("Despesa '%s' marcada como Paga com sucesso!", despesa.getNome()),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarTransacoes();
            if (onTransacoesAtualizadasCallback != null) {
                onTransacoesAtualizadasCallback.run();
            }
        }
    }

    private void actualizarGraficos(List<Transacao> transacoesFiltradas) {
        painelGraficosConteudo.removeAll();

        String tipoGrafico = (String) comboTipoGrafico.getSelectedItem();

        if (tipoGrafico == null) {
            tipoGrafico = "Despesas por Categoria";
        }

        JPanel graficoAtual = null;

        if ("Despesas por Categoria".equals(tipoGrafico)) {
            Map<String, BigDecimal> tempDespesasPorNomeCategoria = transacoesFiltradas.stream()
                    .filter(t -> t instanceof Despesa && ((Despesa) t).getCaixaQueRecebe() == null)
                    .collect(Collectors.groupingBy(t -> t.getCategoria() != null ? t.getCategoria().getNome() : "Sem Categoria",
                                                   Collectors.reducing(BigDecimal.ZERO, Transacao::getValor, BigDecimal::add)));

            Map<Categoria, BigDecimal> despesasPorCategoria = new HashMap<>();
            tempDespesasPorNomeCategoria.forEach((nomeCategoria, valorTotal) -> {
                despesasPorCategoria.put(new Categoria(nomeCategoria, ""), valorTotal);
            });

            if (!despesasPorCategoria.isEmpty()) {
                graficoAtual = new PainelGraficoBarras(despesasPorCategoria, "Despesas", Paleta.Vermelho);
            } else {
                JPanel noDataPanel = new JPanel(new GridBagLayout());
                noDataPanel.setOpaque(false);
                JLabel msgLabel = new JLabel("Nenhuma despesa para exibir no gráfico.", SwingConstants.CENTER);
                msgLabel.setFont(FonteInter.getRegular(14f));
                msgLabel.setForeground(Paleta.TextoCorpo);
                noDataPanel.add(msgLabel);
                graficoAtual = noDataPanel;
            }
        } else if ("Receitas por Categoria".equals(tipoGrafico)) {
            Map<String, BigDecimal> tempReceitasPorNomeCategoria = transacoesFiltradas.stream()
                    .filter(t -> t instanceof Receita)
                    .collect(Collectors.groupingBy(t -> t.getCategoria() != null ? t.getCategoria().getNome() : "Sem Categoria",
                                                   Collectors.reducing(BigDecimal.ZERO, Transacao::getValor, BigDecimal::add)));

            Map<Categoria, BigDecimal> receitasPorCategoria = new HashMap<>();
            tempReceitasPorNomeCategoria.forEach((nomeCategoria, valorTotal) -> {
                receitasPorCategoria.put(new Categoria(nomeCategoria, ""), valorTotal);
            });

            if (!receitasPorCategoria.isEmpty()) {
                graficoAtual = new PainelGraficoBarras(receitasPorCategoria, "Receitas", Paleta.VerdeClaro);
            } else {
                JPanel noDataPanel = new JPanel(new GridBagLayout());
                noDataPanel.setOpaque(false);
                JLabel msgLabel = new JLabel("Nenhuma receita para exibir no gráfico.", SwingConstants.CENTER);
                msgLabel.setFont(FonteInter.getRegular(14f));
                msgLabel.setForeground(Paleta.TextoCorpo);
                noDataPanel.add(msgLabel);
                graficoAtual = noDataPanel;
            }
        }

        if (graficoAtual != null) {
            painelGraficosConteudo.add(graficoAtual, tipoGrafico);
            ((CardLayout) painelGraficosConteudo.getLayout()).show(painelGraficosConteudo, tipoGrafico);
        }

        painelGraficosConteudo.revalidate();
        painelGraficosConteudo.repaint();
    }

    class PainelGraficoBarras extends JPanel {
        private final Map<Categoria, BigDecimal> dados;
        private final String tituloGrafico;
        private final Color corPrincipalBarra;
        private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

        private static final int BAR_HEIGHT = 20;
        private static final int TEXT_HEIGHT = 15;
        private static final int VERTICAL_PADDING_BETWEEN_TEXT_AND_BAR = 3;
        private static final int BAR_BLOCK_SPACING = 15;

        public PainelGraficoBarras(Map<Categoria, BigDecimal> dados, String tituloGrafico, Color corPrincipalBarra) {
            this.dados = dados;
            this.tituloGrafico = tituloGrafico;
            this.corPrincipalBarra = corPrincipalBarra;
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            setAlignmentX(LEFT_ALIGNMENT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelWidth;
            if (getParent() != null) {
                panelWidth = getParent().getWidth() - getParent().getInsets().left - getParent().getInsets().right;
            } else {
                panelWidth = getWidth();
            }
            panelWidth = Math.max(panelWidth, getMinimumSize().width);

            int barHeight = BAR_HEIGHT;
            int textHeight = TEXT_HEIGHT;
            int verticalPaddingBetweenTextAndBar = VERTICAL_PADDING_BETWEEN_TEXT_AND_BAR;
            int barBlockSpacing = BAR_BLOCK_SPACING;

            int leftMargin = 10;
            int rightMargin = 10;

            BigDecimal totalGeral = dados.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalGeral.compareTo(BigDecimal.ZERO) == 0) {
                g2.setColor(Paleta.TextoCorpo);
                g2.setFont(FonteInter.getRegular(14f));
                String msg = "Nenhum dado para o gráfico de " + tituloGrafico.toLowerCase() + ".";
                FontMetrics fm = g2.getFontMetrics();
                int x = (panelWidth - fm.stringWidth(msg)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(msg, x, y);
                g2.dispose();
                return;
            }

            int currentY = textHeight + 5;

            List<Map.Entry<Categoria, BigDecimal>> sortedData = dados.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            int maxBarDrawableWidth = panelWidth - leftMargin - rightMargin;

            for (Map.Entry<Categoria, BigDecimal> entry : sortedData) {
                Categoria categoria = entry.getKey();
                BigDecimal valor = entry.getValue();

                if (valor.compareTo(BigDecimal.ZERO) <= 0) continue;

                g2.setColor(Paleta.TextoCorpo);
                g2.setFont(FonteInter.getBold(14f));
                g2.drawString(categoria.getNome(), leftMargin, currentY);

                String valorStr = formatoMoeda.format(valor);
                g2.setFont(FonteInter.getBold(14f));
                int valorWidth = g2.getFontMetrics().stringWidth(valorStr);
                g2.drawString(valorStr, panelWidth - rightMargin - valorWidth, currentY);

                int actualBarWidth = (int) (maxBarDrawableWidth * (valor.doubleValue() / totalGeral.doubleValue()));
                actualBarWidth = Math.max(actualBarWidth, 10);

                int barY = currentY + verticalPaddingBetweenTextAndBar;

                g2.setColor(corPrincipalBarra);
                g2.fillRoundRect(leftMargin, barY, actualBarWidth, barHeight, 5, 5);

                currentY = barY + barHeight + barBlockSpacing + textHeight;
            }
            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            int numBars = dados.size();
            int totalHeight = numBars * (TEXT_HEIGHT + VERTICAL_PADDING_BETWEEN_TEXT_AND_BAR + BAR_HEIGHT + BAR_BLOCK_SPACING) + 20;

            int calculatedWidth = 800;
            if (getParent() != null) {
                calculatedWidth = getParent().getWidth() - getParent().getInsets().left - getParent().getInsets().right;
                calculatedWidth = Math.max(calculatedWidth, 300);
            }

            return new Dimension(calculatedWidth, Math.max(200, totalHeight));
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(300, 200);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
        }
    }
}
