package com.criso.ui;

import com.criso.model.Carteira;
import com.criso.model.Meta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PainelMetasFinanceiras extends PainelArredondado {

    public final transient Carteira carteira;
    public JScrollPane scrollPaneMetas;
    public JPanel painelListaMetas;

    public PainelMetasFinanceiras(Carteira carteira) {
        super(24);
        this.carteira = carteira;

        definirCorDeFundo(Paleta.Branco);
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(criarCabecalho(), BorderLayout.NORTH);

        this.scrollPaneMetas = criarPainelListaMetas();
        add(scrollPaneMetas, BorderLayout.CENTER);

        atualizarListaDeMetas();
    }

    public void atualizarListaDeMetas() {
        painelListaMetas.removeAll();

        List<Meta> metas = carteira.getMetas();

        for (Meta meta : metas) {
            painelListaMetas.add(criarItemDeMeta(meta));
        }

        painelListaMetas.revalidate();
        painelListaMetas.repaint();
    }

    public JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titulo = new JLabel("Metas");
        titulo.setFont(FonteInter.getBold(20f));
        titulo.setForeground(Paleta.TextoTitulo);

        BotaoArredondado botaoCriarMeta = new BotaoArredondado("Criar Meta");
        botaoCriarMeta.setFont(FonteInter.getBold(14f));
        botaoCriarMeta.setBackground(Paleta.VerdeEscuro);
        botaoCriarMeta.setForeground(Paleta.Amarelo);
        botaoCriarMeta.setIcon(new IconeOperacao(IconeOperacao.Tipo.ADICAO, Paleta.Amarelo, 12));
        botaoCriarMeta.setRaioDaBorda(12);
        botaoCriarMeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoCriarMeta.setBorder(new EmptyBorder(10, 20, 10, 20));
        botaoCriarMeta.setIconTextGap(8);

        botaoCriarMeta.addActionListener(e -> {
            Frame framePai = (Frame) SwingUtilities.getWindowAncestor(this);
            JanelaCriarMeta janela = new JanelaCriarMeta(framePai, this.carteira, this::atualizarListaDeMetas);
            janela.setVisible(true);
        });

        cabecalho.add(titulo, BorderLayout.WEST);
        cabecalho.add(botaoCriarMeta, BorderLayout.EAST);
        return cabecalho;
    }

    public JPanel criarItemDeMeta(Meta meta) {
        JPanel painelItem = new JPanel();
        painelItem.setOpaque(false);
        painelItem.setLayout(new GridBagLayout());
        painelItem.setBorder(new EmptyBorder(10, 5, 10, 5));
        painelItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        painelItem.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 4, 0);

        int percentual = 0;
        if (meta.getSaldoAlvo() != null && meta.getSaldoAlvo().compareTo(BigDecimal.ZERO) > 0) {
            percentual = meta.getSaldoAtual()
                            .divide(meta.getSaldoAlvo(), 2, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal(100))
                            .intValue();
            if (percentual > 100) {
                percentual = 100;
            }
        }

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance();
        String saldoAtualFormatado = formatoMoeda.format(meta.getSaldoAtual());
        String saldoAlvoFormatado = formatoMoeda.format(meta.getSaldoAlvo());

        JLabel nomeMeta = new JLabel(meta.getNome());
        nomeMeta.setFont(FonteInter.getRegular(15f));
        nomeMeta.setForeground(Paleta.TextoCorpo);
        nomeMeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        nomeMeta.addMouseListener(new MouseAdapter() {
            public Color originalColor = nomeMeta.getForeground();

            @Override
            public void mouseClicked(MouseEvent e) {
                Frame framePai = (Frame) SwingUtilities.getWindowAncestor(PainelMetasFinanceiras.this);
                JanelaEditarMeta janela = new JanelaEditarMeta(framePai, meta, carteira, PainelMetasFinanceiras.this::atualizarListaDeMetas);
                janela.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                nomeMeta.setForeground(Paleta.Azul);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nomeMeta.setForeground(originalColor);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        painelItem.add(nomeMeta, gbc);

        JLabel progressoLabel = new JLabel(String.format("%s/%s - %d%%", saldoAtualFormatado, saldoAlvoFormatado, percentual));
        progressoLabel.setFont(FonteInter.getBold(14f));
        progressoLabel.setForeground(Paleta.Azul);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        painelItem.add(progressoLabel, gbc);

        final int finalPercentual = percentual;
        JPanel barraProgresso = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int borderRadius = height / 2;

                g2.setColor(new Color(229, 231, 235));
                g2.fillRoundRect(0, 0, width, height, borderRadius, borderRadius);

                int fillWidth = (int) (width * (finalPercentual / 100.0));
                g2.setColor(Paleta.Amarelo);
                g2.fillRoundRect(0, 0, fillWidth, height, borderRadius, borderRadius);

                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 10);
            }
        };
        barraProgresso.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        painelItem.add(barraProgresso, gbc);

        return painelItem;
    }

    public JScrollPane criarPainelListaMetas() {
        painelListaMetas = new JPanel();
        painelListaMetas.setLayout(new BoxLayout(painelListaMetas, BoxLayout.Y_AXIS));
        painelListaMetas.setBackground(Paleta.Branco);

        JScrollPane scrollPane = new JScrollPane(painelListaMetas);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Paleta.Branco);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new BarraRolagemUI());
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setUnitIncrement(16);

        return scrollPane;
    }

    public void recarregarMetas() {
        atualizarListaDeMetas();
    }
}