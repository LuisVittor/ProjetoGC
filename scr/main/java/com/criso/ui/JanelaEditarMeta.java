package com.criso.ui;

import com.criso.model.Meta;
import com.criso.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class JanelaEditarMeta extends JDialog {

    public final transient Meta metaOriginal;
    public final transient Carteira carteira;
    public final transient Runnable onMetaAtualizadaCallback;
    public JTextField campoNomeMeta;
    public JFormattedTextField campoSaldoAlvo;
    public JLabel labelSaldoAtual;
    public JFormattedTextField campoValorOperacao;

    public JanelaEditarMeta(Frame parent, Meta meta, Carteira carteira, Runnable onMetaAtualizadaCallback) {
        super(parent, "Editar Meta: " + meta.getNome(), true);
        this.metaOriginal = meta;
        this.carteira = carteira;
        this.onMetaAtualizadaCallback = onMetaAtualizadaCallback;
        initComponents();
        setupLayout();
        pack();
        setLocationRelativeTo(parent);
        carregarDadosMeta();
    }
  
    public void initComponents() {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance();
        decimalFormat.setParseBigDecimal(true);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);

        campoNomeMeta = new JTextField(25);
        campoSaldoAlvo = new JFormattedTextField(decimalFormat);
        labelSaldoAtual = new JLabel();
        campoValorOperacao = new JFormattedTextField(decimalFormat);
        campoValorOperacao.setValue(BigDecimal.ZERO);
    }

    public void setupLayout() {
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(Paleta.Branco);
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblNomeMeta = new JLabel("Nome da Meta:");
        lblNomeMeta.setFont(FonteInter.getBold(14f));
        lblNomeMeta.setForeground(Paleta.TextoCorpo);
        painelPrincipal.add(lblNomeMeta, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        campoNomeMeta.setFont(FonteInter.getRegular(14f));
        campoNomeMeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.Azul, 1),
                new EmptyBorder(5, 5, 5, 5)
        ));
        painelPrincipal.add(campoNomeMeta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel lblSaldoAlvo = new JLabel("Saldo Alvo:");
        lblSaldoAlvo.setFont(FonteInter.getBold(14f));
        lblSaldoAlvo.setForeground(Paleta.TextoCorpo);
        painelPrincipal.add(lblSaldoAlvo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        campoSaldoAlvo.setFont(FonteInter.getRegular(14f));
        campoSaldoAlvo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.Azul, 1),
                new EmptyBorder(5, 5, 5, 5)
        ));
        painelPrincipal.add(campoSaldoAlvo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel lblSaldoAtual = new JLabel("Saldo Atual:");
        lblSaldoAtual.setFont(FonteInter.getBold(14f));
        lblSaldoAtual.setForeground(Paleta.TextoCorpo);
        painelPrincipal.add(lblSaldoAtual, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        labelSaldoAtual.setFont(FonteInter.getBold(14f));
        labelSaldoAtual.setForeground(Paleta.TextoDoValor);
        painelPrincipal.add(labelSaldoAtual, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        painelPrincipal.add(separator, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel lblValorOperacao = new JLabel("Valor para Operação:");
        lblValorOperacao.setFont(FonteInter.getBold(14f));
        lblValorOperacao.setForeground(Paleta.TextoCorpo);
        painelPrincipal.add(lblValorOperacao, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        campoValorOperacao.setFont(FonteInter.getRegular(14f));
        campoValorOperacao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.Azul, 1),
                new EmptyBorder(5, 5, 5, 5)
        ));
        painelPrincipal.add(campoValorOperacao, gbc);

        JPanel painelOperacaoBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        painelOperacaoBotoes.setOpaque(false);

        BotaoArredondado btnAdicionar = new BotaoArredondado("Adicionar");
        btnAdicionar.setFont(FonteInter.getBold(14f));
        btnAdicionar.setBackground(Paleta.VerdeClaro);
        btnAdicionar.setForeground(Paleta.Branco);
        btnAdicionar.setRaioDaBorda(12);
        btnAdicionar.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionar.addActionListener(e -> adicionarValor());

        BotaoArredondado btnRemover = new BotaoArredondado("Remover");
        btnRemover.setFont(FonteInter.getBold(14f));
        btnRemover.setBackground(Paleta.Vermelho);
        btnRemover.setForeground(Paleta.Branco);
        btnRemover.setRaioDaBorda(12);
        btnRemover.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnRemover.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemover.addActionListener(e -> removerValor());

        painelOperacaoBotoes.add(btnAdicionar);
        painelOperacaoBotoes.add(btnRemover);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 5, 10, 5);
        painelPrincipal.add(painelOperacaoBotoes, gbc);

        JPanel painelAcaoBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelAcaoBotoes.setOpaque(false);

        BotaoArredondado btnExcluir = new BotaoArredondado("Excluir Meta");
        btnExcluir.setFont(FonteInter.getBold(14f));
        btnExcluir.setBackground(Paleta.CinzaAzulado);
        btnExcluir.setForeground(Paleta.Branco);
        btnExcluir.setRaioDaBorda(12);
        btnExcluir.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(e -> excluirMeta());

        BotaoArredondado btnFechar = new BotaoArredondado("Fechar");
        btnFechar.setFont(FonteInter.getBold(14f));
        btnFechar.setBackground(Paleta.VerdeEscuro);
        btnFechar.setForeground(Paleta.Branco);
        btnFechar.setRaioDaBorda(12);
        btnFechar.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addActionListener(e -> dispose());

        painelAcaoBotoes.add(btnExcluir);
        painelAcaoBotoes.add(btnFechar);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 5, 0, 5);
        painelPrincipal.add(painelAcaoBotoes, gbc);

        add(painelPrincipal);
    }

    public void carregarDadosMeta() {
        campoNomeMeta.setText(metaOriginal.getNome());
        campoSaldoAlvo.setValue(metaOriginal.getSaldoAlvo());
        atualizarLabelSaldoAtual();
    }

    public void atualizarLabelSaldoAtual() {
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance();
        labelSaldoAtual.setText(formatoMoeda.format(metaOriginal.getSaldoAtual()));
    }

    public void adicionarValor() {
        BigDecimal valor;
        try {
            valor = (BigDecimal) campoValorOperacao.getValue();
            if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um valor positivo para adicionar.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Use apenas números e vírgula para decimais.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        metaOriginal.adicionarValor(valor);
        carteira.atualizarMeta(metaOriginal);

        atualizarLabelSaldoAtual();
        campoValorOperacao.setValue(BigDecimal.ZERO);

        if (onMetaAtualizadaCallback != null) {
            onMetaAtualizadaCallback.run();
        }

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance();
        JOptionPane.showMessageDialog(this,
                                      String.format("%s adicionados com sucesso à meta '%s'!", formatoMoeda.format(valor), metaOriginal.getNome()),
                                      "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public void removerValor() {
        BigDecimal valor;
        try {
            valor = (BigDecimal) campoValorOperacao.getValue();
            if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um valor positivo para remover.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Use apenas números e vírgula para decimais.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (metaOriginal.getSaldoAtual().compareTo(valor) < 0) {
            JOptionPane.showMessageDialog(this, "Saldo insuficiente para remover este valor.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        metaOriginal.removerValor(valor);
        carteira.atualizarMeta(metaOriginal);

        atualizarLabelSaldoAtual();
        campoValorOperacao.setValue(BigDecimal.ZERO);

        if (onMetaAtualizadaCallback != null) {
            onMetaAtualizadaCallback.run();
        }

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance();
        JOptionPane.showMessageDialog(
            this,
            String.format("%s removidos com sucesso da meta '%s'!", formatoMoeda.format(valor), metaOriginal.getNome()),
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public void excluirMeta() {
        int confirmacao = JOptionPane.showConfirmDialog(this,
                String.format("Tem certeza que deseja excluir a meta '%s'? Esta ação é irreversível.", metaOriginal.getNome()),
                "Confirmar Exclusão de Meta", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            carteira.removerMeta(metaOriginal);

            if (onMetaAtualizadaCallback != null) {
                onMetaAtualizadaCallback.run();
            }

            JOptionPane.showMessageDialog(this,
                String.format("Meta '%s' excluída com sucesso!", metaOriginal.getNome()),
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
