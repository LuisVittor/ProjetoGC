package com.criso.ui;

import com.criso.model.Carteira;
import com.criso.model.Transacao;
import com.criso.model.Despesa;
import com.criso.model.Receita;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelTransacoesRecentes extends PainelArredondado {
    public final Carteira carteira;
    public JScrollPane scrollPaneTransacoes;
    public JPanel painelLista;
    public static final int NUM_TRANSACOES_EXIBIDAS = 20;

    public PainelTransacoesRecentes(Carteira carteira) {
        super(24);
        this.carteira = carteira;

        definirCorDeFundo(Paleta.Branco);
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(criarCabecalho(), BorderLayout.NORTH);

        this.painelLista = new JPanel();
        this.painelLista.setLayout(new BoxLayout(this.painelLista, BoxLayout.Y_AXIS));
        this.painelLista.setBackground(Paleta.Branco);

        this.scrollPaneTransacoes = new JScrollPane(this.painelLista);
        this.scrollPaneTransacoes.setBorder(null);
        this.scrollPaneTransacoes.getViewport().setBackground(Paleta.Branco);

        JScrollBar verticalScrollBar = this.scrollPaneTransacoes.getVerticalScrollBar();
        verticalScrollBar.setUI(new BarraRolagemUI());
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setUnitIncrement(16);

        add(this.scrollPaneTransacoes, BorderLayout.CENTER);

        atualizarListaDeTransacoes();
    }

    public JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titulo = new JLabel("Transações Recentes");
        titulo.setFont(FonteInter.getBold(20f));
        titulo.setForeground(Paleta.TextoTitulo);

        JLabel linkVerTodas = new JLabel("Ver todas");
        linkVerTodas.setFont(FonteInter.getBold(14f));
        linkVerTodas.setForeground(Paleta.Azul);
        linkVerTodas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkVerTodas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                linkVerTodas.setForeground(Paleta.TextoTitulo);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                linkVerTodas.setForeground(Paleta.Azul);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Frame framePai = (Frame) SwingUtilities.getWindowAncestor(PainelTransacoesRecentes.this);
                Runnable callbackAtualizacao = () -> {
                    atualizarListaDeTransacoes();
                    Window parentWindow = SwingUtilities.getWindowAncestor(PainelTransacoesRecentes.this);
                    if (parentWindow instanceof JanelaPrincipal) {
                        ((JanelaPrincipal) parentWindow).atualizarDashboardPrincipal();
                    }
                };
                JanelaTodasTransacoes janela = new JanelaTodasTransacoes(framePai, carteira, callbackAtualizacao);
                janela.setVisible(true);
            }
        });

        cabecalho.add(titulo, BorderLayout.WEST);
        cabecalho.add(linkVerTodas, BorderLayout.EAST);
        return cabecalho;
    }

    public void atualizarListaDeTransacoes() {
        painelLista.removeAll();

        List<Transacao> transacoes = carteira.listarTransacoesRecentes(NUM_TRANSACOES_EXIBIDAS);

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance();
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yy");

        if (transacoes.isEmpty()) {
            JLabel mensagem = new JLabel("Nenhuma transação recente para exibir.");
            mensagem.setFont(FonteInter.getRegular(14f));
            mensagem.setForeground(Paleta.TextoCorpo.darker());
            mensagem.setHorizontalAlignment(SwingConstants.CENTER);
            painelLista.add(mensagem);
            painelLista.add(Box.createVerticalGlue());
        } else {
            for (int i = 0; i < transacoes.size(); i++) {
                Transacao transacao = transacoes.get(i);
                painelLista.add(criarItemDeTransacao(transacao, formatoMoeda, formatoData));
                if (i < transacoes.size() - 1) {
                    JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
                    separator.setForeground(new Color(241, 245, 249));
                    painelLista.add(separator);
                }
            }
        }

        painelLista.revalidate();
        painelLista.repaint();
        scrollPaneTransacoes.revalidate();
        scrollPaneTransacoes.repaint();
    }

    public JPanel criarItemDeTransacao(Transacao transacao, NumberFormat formatoMoeda, DateTimeFormatter formatoData) {
        JPanel painelItem = new JPanel(new BorderLayout(10, 0));
        painelItem.setOpaque(false);
        painelItem.setBorder(new EmptyBorder(10, 5, 10, 5));
        painelItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        painelItem.setPreferredSize(new Dimension(painelItem.getPreferredSize().width, 70));
        painelItem.setAlignmentX(Component.LEFT_ALIGNMENT);

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

        JLabel dataTransacao = new JLabel(transacao.getData().format(formatoData));
        dataTransacao.setFont(FonteInter.getRegular(13f));
        dataTransacao.setForeground(Paleta.Azul);
        dataTransacao.setAlignmentX(Component.LEFT_ALIGNMENT);

        painelInfo.add(nomeTransacao);
        painelInfo.add(Box.createVerticalStrut(2));
        painelInfo.add(dataTransacao);

        JLabel valorTransacao = new JLabel(prefixoValor + formatoMoeda.format(transacao.getValor()));
        valorTransacao.setFont(FonteInter.getBold(16f));
        valorTransacao.setForeground(corPrincipal);

        painelItem.add(painelIcone, BorderLayout.WEST);
        painelItem.add(painelInfo, BorderLayout.CENTER);
        painelItem.add(valorTransacao, BorderLayout.EAST);

        return painelItem;
    }

    public JScrollPane criarPainelListaTransacoes() {
        JScrollPane scrollPane = new JScrollPane(painelLista);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Paleta.Branco);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new BarraRolagemUI());
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setUnitIncrement(16);

        return scrollPane;
    }
}