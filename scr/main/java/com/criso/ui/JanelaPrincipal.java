package com.criso.ui;

import com.criso.model.Carteira;
import com.criso.model.Transacao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class JanelaPrincipal extends JFrame {

    public static final long serialVersionUID = 1L;
    public final Carteira carteira;

    JPanel fundoAplicacao;
    PainelHeader headerComponent;
    PainelSaldoTotal painelSaldoTotalComponente;
    PainelMeusCaixas painelMeusCaixas;
    PainelMetasFinanceiras painelMetasFinanceiras;
    PainelTransacoesRecentes painelTransacoes;

    public JanelaPrincipal(Carteira carteira) {
        this.carteira = carteira;
        Image icon = new ImageIcon(getClass().getResource("/logo/Logo_Criso_Cabeca_Lobo_Dourado.png")).getImage();
        this.setIconImage(icon);

        setTitle("Criso - Gestão Financeira");
        setSize(1280, 720);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Paleta.FundoAplicacao);
        getContentPane().setLayout(new BorderLayout());

        Runnable callbackAtualizacao = () -> {
            SwingUtilities.invokeLater(() -> {
                atualizarDashboardPrincipal();
                painelMeusCaixas.recarregarCaixas();
                painelTransacoes.atualizarListaDeTransacoes();
                JanelaPrincipal.this.revalidate();
                JanelaPrincipal.this.repaint();
            });
        };

        this.headerComponent = new PainelHeader("Crisador", this.carteira, this, callbackAtualizacao);
        getContentPane().add(this.headerComponent, BorderLayout.NORTH);

        this.fundoAplicacao = createFundoAplicacao();
        getContentPane().add(fundoAplicacao, BorderLayout.CENTER);
    }

    public JPanel createFundoAplicacao() {
        JPanel painel = new JPanel();
        painel.setBackground(Paleta.FundoAplicacao);
        painel.setLayout(new GridBagLayout());
        painel.setBorder(new EmptyBorder(30, 30, 30, 30));

        BigDecimal saldo = this.carteira.getSaldoTotal();
        List<Transacao> todasTransacoes = this.carteira.getTransacoes(); 

        painelSaldoTotalComponente = new PainelSaldoTotal(saldo, todasTransacoes); 
        painelSaldoTotalComponente.setPreferredSize(new Dimension(0, 250));
        GridBagConstraints gbcSaldo = new GridBagConstraints();
        gbcSaldo.insets = new Insets(0, 0, 20, 20);
        gbcSaldo.gridx = 0;
        gbcSaldo.gridy = 0;
        gbcSaldo.weightx = 0.4;
        gbcSaldo.weighty = 0;
        gbcSaldo.fill = GridBagConstraints.BOTH;
        gbcSaldo.anchor = GridBagConstraints.NORTHWEST;
        painel.add(painelSaldoTotalComponente, gbcSaldo);

        this.painelMeusCaixas = new PainelMeusCaixas(carteira, this);
        this.painelMeusCaixas.setPreferredSize(new Dimension(0, 250));
        GridBagConstraints gbcCaixas = new GridBagConstraints();
        gbcCaixas.insets = new Insets(0, 0, 20, 0);
        gbcCaixas.gridx = 1;
        gbcCaixas.gridy = 0;
        gbcCaixas.weightx = 0.6;
        gbcCaixas.weighty = 0.0;
        gbcCaixas.fill = GridBagConstraints.BOTH;
        gbcCaixas.anchor = GridBagConstraints.NORTHWEST;
        painel.add(painelMeusCaixas, gbcCaixas);

        this.painelTransacoes = new PainelTransacoesRecentes(carteira);
        painelTransacoes.setPreferredSize(new Dimension(0, 250));

        GridBagConstraints gbcTransacoes = new GridBagConstraints();
        gbcTransacoes.gridx = 0;
        gbcTransacoes.gridy = 1;
        gbcTransacoes.weightx = 0.6;
        gbcTransacoes.weighty = 1.0;
        gbcTransacoes.fill = GridBagConstraints.BOTH;
        gbcTransacoes.anchor = GridBagConstraints.SOUTHEAST;
        gbcTransacoes.insets = new Insets(0, 0, 0, 20);
        painel.add(painelTransacoes, gbcTransacoes);

        this.painelMetasFinanceiras = new PainelMetasFinanceiras(carteira);
        this.painelMetasFinanceiras.setPreferredSize(new Dimension(0, 250));
        GridBagConstraints gbcMetas = new GridBagConstraints();
        gbcMetas.insets = new Insets(0, 0, 0, 0);
        gbcMetas.gridx = 1;
        gbcMetas.gridy = 1;
        gbcMetas.weightx = 0.4;
        gbcMetas.weighty = 1.0;
        gbcMetas.fill = GridBagConstraints.BOTH;
        gbcMetas.anchor = GridBagConstraints.SOUTHWEST;
        painel.add(painelMetasFinanceiras, gbcMetas);

        return painel;
    }

    public void atualizarDashboardPrincipal() {
        BigDecimal novoSaldoTotal = carteira.getSaldoTotal();
        List<Transacao> novasTransacoes = carteira.getTransacoes();

        if (painelSaldoTotalComponente != null) {
            painelSaldoTotalComponente.atualizarValores(novoSaldoTotal, novasTransacoes);
        }
    }
}
