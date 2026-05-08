package com.criso.ui;

import com.criso.model.Caixa;
import com.criso.model.Carteira;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.math.BigDecimal;

public class JanelaCriarCaixa extends JDialog {

    public final transient Runnable callbackSucesso;
    public final Carteira carteira;

    public JTextField campoNome;
    public JTextField campoSaldoInicial;

    public JanelaCriarCaixa(Frame pai, Carteira carteira, Runnable callback) {
        super(pai, "Criar Novo Caixa", true);
        this.carteira = carteira;
        this.callbackSucesso = callback;

        setSize(480, 320);
        setLocationRelativeTo(pai);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JPanel painelPrincipal = new JPanel(new BorderLayout(15, 15));
        painelPrincipal.setBackground(Paleta.FundoAplicacao);
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        painelPrincipal.add(criarPainelTitulo(), BorderLayout.NORTH);
        painelPrincipal.add(criarPainelCampos(), BorderLayout.CENTER);
        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
    }

    public JPanel criarPainelTitulo() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painel.setOpaque(false);

        JLabel titulo = new JLabel("Criar Novo Caixa");
        titulo.setFont(FonteInter.getBold(22f));
        titulo.setForeground(Paleta.TextoTitulo);

        painel.add(titulo);
        return painel;
    }

    public JPanel criarPainelCampos() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.weightx = 0.3; gbc.anchor = GridBagConstraints.LINE_START;
        Font fonteLabel = FonteInter.getRegular(14f);

        JLabel labelNome = new JLabel("Nome do Caixa:");
        labelNome.setFont(fonteLabel);
        gbc.gridy = 0;
        painel.add(labelNome, gbc);

        JLabel labelSaldo = new JLabel("Saldo Inicial (R$):");
        labelSaldo.setFont(fonteLabel);
        gbc.gridy = 2;
        painel.add(labelSaldo, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        Font fonteCampo = FonteInter.getRegular(14f);

        campoNome = new JTextField();
        estilizarCampo(campoNome, fonteCampo);
        gbc.gridy = 0;
        painel.add(campoNome, gbc);

        campoSaldoInicial = new JTextField("0,00");
        estilizarCampo(campoSaldoInicial, fonteCampo);
        gbc.gridy = 2;
        painel.add(campoSaldoInicial, gbc);

        return painel;
    }

    public void estilizarCampo(JTextField campo, Font fonte) {
        campo.setFont(fonte);
        campo.setForeground(Paleta.TextoCorpo);
        campo.setBackground(Paleta.Branco);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(5, 5, 5, 5)
        ));
    }

    public JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painel.setOpaque(false);

        BotaoArredondado botaoSalvar = new BotaoArredondado("Salvar Caixa");
        botaoSalvar.setBackground(Paleta.VerdeEscuro);
        botaoSalvar.setForeground(Paleta.Amarelo);
        botaoSalvar.setFont(FonteInter.getBold(14f));
        botaoSalvar.setRaioDaBorda(12);
        botaoSalvar.setBorder(new EmptyBorder(10, 22, 10, 22));
        botaoSalvar.addActionListener(e -> salvarCaixa());

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setFont(FonteInter.getBold(14f));
        botaoCancelar.setForeground(Paleta.Azul);
        botaoCancelar.setContentAreaFilled(false);
        botaoCancelar.setBorderPainted(false);
        botaoCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoCancelar.addActionListener(e -> dispose());

        painel.add(botaoCancelar);
        painel.add(botaoSalvar);

        return painel;
    }

    public void salvarCaixa() {
        try {
            String nome = campoNome.getText();
            String saldoStr = campoSaldoInicial.getText().replace(".", "").replace(",", ".");
            BigDecimal saldo = new BigDecimal(saldoStr);

            Caixa novaCaixa = new Caixa(nome, saldo);
            carteira.adicionarCaixa(novaCaixa);

            JOptionPane.showMessageDialog(this, "Caixa '" + nome + "' criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            if (callbackSucesso != null) {
                callbackSucesso.run();
            }

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O saldo inicial deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Criar Caixa", JOptionPane.ERROR_MESSAGE);
        }
    }
}