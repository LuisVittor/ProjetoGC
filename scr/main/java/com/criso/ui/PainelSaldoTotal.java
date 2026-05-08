package com.criso.ui;

import com.criso.model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PainelSaldoTotal extends JPanel {

    public JLabel saldoValorLabel;
    public JLabel receitasValorLabel;
    public JLabel despesasValorLabel;

    private transient List<Transacao> todasTransacoes;

    public PainelSaldoTotal(BigDecimal saldoTotal, List<Transacao> todasTransacoes) {
        this.todasTransacoes = todasTransacoes;

        setLayout(new GridBagLayout());
        setOpaque(false);

        PainelArredondado cardPanel = new PainelArredondado(24);
        cardPanel.definirCorDeFundo(Paleta.VerdeEscuro);
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBorder(new EmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel saldoTituloLabel = new JLabel("Saldo Total Consolidado");
        saldoTituloLabel.setFont(FonteInter.getRegular(14f));
        saldoTituloLabel.setForeground(Paleta.Branco.darker());
        gbc.gridx = 0;
        gbc.gridy = 0;
        cardPanel.add(saldoTituloLabel, gbc);

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
        saldoValorLabel = new JLabel(formatoMoeda.format(saldoTotal));
        saldoValorLabel.setFont(FonteInter.getBold(36f));
        saldoValorLabel.setForeground(Paleta.Branco);
        gbc.gridy = 1;
        gbc.insets = new Insets(4, 0, 16, 0);
        cardPanel.add(saldoValorLabel, gbc);

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(255, 255, 255, 50));
        separator.setBackground(new Color(255, 255, 255, 50));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 16, 0);
        cardPanel.add(separator, gbc);

        JPanel detalhesPanel = new JPanel();
        detalhesPanel.setLayout(new BoxLayout(detalhesPanel, BoxLayout.X_AXIS));
        detalhesPanel.setOpaque(false);

        BigDecimal totalReceitas = calcularTotalReceitas(todasTransacoes);
        BigDecimal totalDespesas = calcularTotalDespesas(todasTransacoes);

        JPanel receitasPanel = createDetalhePanel("Receitas", totalReceitas, IconeOperacao.Tipo.ADICAO, Paleta.VerdeClaro);
        JPanel despesasPanel = createDetalhePanel("Despesas", totalDespesas, IconeOperacao.Tipo.SUBTRACAO, Paleta.Vermelho);

        receitasValorLabel = (JLabel) ((JPanel) ((BorderLayout) receitasPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER)).getComponent(1);
        despesasValorLabel = (JLabel) ((JPanel) ((BorderLayout) despesasPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER)).getComponent(1);

        detalhesPanel.add(receitasPanel);
        detalhesPanel.add(Box.createHorizontalStrut(32));
        detalhesPanel.add(despesasPanel);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        cardPanel.add(detalhesPanel, gbc);

        add(cardPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    public void atualizarValores(BigDecimal novoSaldoTotal, List<Transacao> novasTransacoes) {
        this.todasTransacoes = novasTransacoes;
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
        saldoValorLabel.setText(formatoMoeda.format(novoSaldoTotal));
        receitasValorLabel.setText(formatoMoeda.format(calcularTotalReceitas(novasTransacoes)));
        despesasValorLabel.setText(formatoMoeda.format(calcularTotalDespesas(novasTransacoes)));
    }

    private BigDecimal calcularTotalReceitas(List<Transacao> transacoes) {
        return transacoes.stream()
                .filter(t -> t instanceof Receita && ((Receita) t).getCaixaQuePaga() == null)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTotalDespesas(List<Transacao> transacoes) {
        return transacoes.stream()
                .filter(t -> t instanceof Despesa && ((Despesa) t).getCaixaQueRecebe() == null)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public JPanel createDetalhePanel(String titulo, BigDecimal valor, IconeOperacao.Tipo tipoIcone, Color corIcone) {
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

        JPanel painel = new JPanel(new BorderLayout(8, 0));
        painel.setOpaque(false);

        JLabel icone = new JLabel(new IconeOperacao(tipoIcone, corIcone, 16));
        icone.setBorder(new EmptyBorder(0, 0, 0, 4));
        painel.add(icone, BorderLayout.WEST);

        JPanel painelTexto = new JPanel();
        painelTexto.setLayout(new BoxLayout(painelTexto, BoxLayout.Y_AXIS));
        painelTexto.setOpaque(false);

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(FonteInter.getBold(12f));
        labelTitulo.setForeground(Paleta.Branco.darker());

        JLabel labelValor = new JLabel(formatoMoeda.format(valor));
        labelValor.setFont(FonteInter.getBold(18f));
        labelValor.setForeground(Paleta.Branco);

        painelTexto.add(labelTitulo);
        painelTexto.add(labelValor);
        painel.add(painelTexto, BorderLayout.CENTER);

        return painel;
    }
}
