package com.criso.ui;

import com.criso.model.Meta;
import com.criso.model.Carteira;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class JanelaCriarMeta extends JDialog {

    public final transient Carteira carteira;
    public final transient Runnable onMetaCriadaCallback;
    public JTextField campoNomeMeta;
    public JFormattedTextField campoSaldoAlvo;

    public JanelaCriarMeta(Frame parent, Carteira carteira, Runnable onMetaCriadaCallback) {
        super(parent, "Criar Nova Meta", true);
        this.carteira = carteira;
        this.onMetaCriadaCallback = onMetaCriadaCallback;
        initComponents();
        setupLayout();
        pack();
        setLocationRelativeTo(parent);
    }

  
    public void initComponents() {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance();
        decimalFormat.setParseBigDecimal(true);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);

        campoNomeMeta = new JTextField(20);
        campoSaldoAlvo = new JFormattedTextField(decimalFormat);

        campoSaldoAlvo.setValue(BigDecimal.ZERO);
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
       
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoes.setOpaque(false);

        BotaoArredondado btnCriar = new BotaoArredondado("Criar");
        btnCriar.setFont(FonteInter.getBold(14f));
        btnCriar.setBackground(Paleta.VerdeClaro);
        btnCriar.setForeground(Paleta.Branco);
        btnCriar.setRaioDaBorda(12);
        btnCriar.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnCriar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCriar.addActionListener(e -> criarMeta());

        BotaoArredondado btnCancelar = new BotaoArredondado("Cancelar");
        btnCancelar.setFont(FonteInter.getBold(14f));
        btnCancelar.setBackground(Paleta.Vermelho);
        btnCancelar.setForeground(Paleta.Branco);
        btnCancelar.setRaioDaBorda(12);
        btnCancelar.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());

        painelBotoes.add(btnCriar);
        painelBotoes.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 5, 0, 5);
        painelPrincipal.add(painelBotoes, gbc);

        add(painelPrincipal);
    }

    public void criarMeta() {
        String nome = campoNomeMeta.getText().trim();
        BigDecimal saldoAlvo = BigDecimal.ZERO;
        BigDecimal saldoInicial = BigDecimal.ZERO;

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome da meta não pode ser vazio.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            saldoAlvo = (BigDecimal) campoSaldoAlvo.getValue();
            if (saldoAlvo == null) {
                JOptionPane.showMessageDialog(this, "Saldo Alvo inválido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (saldoAlvo.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "O saldo alvo deve ser um valor positivo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Formato de Saldo Alvo inválido. Use apenas números e vírgula para decimais.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }


        Meta novaMeta = new Meta(nome, saldoAlvo, saldoInicial);
        carteira.adicionarMeta(novaMeta);

        if (onMetaCriadaCallback != null) {
            onMetaCriadaCallback.run();
        }

        dispose();
    }
}
