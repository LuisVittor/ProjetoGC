package com.criso.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;

import com.criso.model.Caixa;
import com.criso.model.Carteira;

public class PainelMeusCaixas extends PainelArredondado {

    public JScrollPane scrollPaneCaixas;
    public Carteira carteira;
    public JanelaPrincipal janelaPrincipal;

    public PainelMeusCaixas(Carteira carteira, JanelaPrincipal janelaPrincipal) {
        super(24);
        definirCorDeFundo(Paleta.Branco);
        setLayout(new BorderLayout(0, 10));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        this.carteira = carteira;
        this.janelaPrincipal = janelaPrincipal;

        JPanel painelConteudoCaixas = new JPanel();
        painelConteudoCaixas.setLayout(new BoxLayout(painelConteudoCaixas, BoxLayout.Y_AXIS));
        painelConteudoCaixas.setBackground(Paleta.Branco);

        this.scrollPaneCaixas = new JScrollPane(painelConteudoCaixas);
        this.scrollPaneCaixas.setBorder(null);
        this.scrollPaneCaixas.getViewport().setBackground(Paleta.Branco);

        JScrollBar verticalScrollBar = this.scrollPaneCaixas.getVerticalScrollBar();
        verticalScrollBar.setUI(new BarraRolagemUI());
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setUnitIncrement(16);

        add(criarCabecalho(), BorderLayout.NORTH);
        add(this.scrollPaneCaixas, BorderLayout.CENTER);

        recarregarCaixas();
    }

    public void recarregarCaixas() {
        JPanel painelLista = (JPanel) scrollPaneCaixas.getViewport().getView();
        painelLista.removeAll();

        List<Caixa> caixas = carteira.getCaixas();
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance();

        for (Caixa caixa : caixas) {
            painelLista.add(criarItemDeCaixa(caixa, formatoMoeda));
        }

        painelLista.revalidate();
        painelLista.repaint();
    }

    public JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titulo = new JLabel("Meus Caixas");
        titulo.setFont(FonteInter.getBold(20f));
        titulo.setForeground(Paleta.TextoTitulo);

        BotaoArredondado botaoCriarCaixa = new BotaoArredondado("Criar Caixa");
        botaoCriarCaixa.setFont(FonteInter.getBold(14f));

        botaoCriarCaixa.setBackground(Paleta.Amarelo);
        botaoCriarCaixa.setForeground(Paleta.VerdeEscuro);
        botaoCriarCaixa.setIcon(new IconeOperacao(IconeOperacao.Tipo.ADICAO, Paleta.VerdeEscuro, 12));

        botaoCriarCaixa.setRaioDaBorda(12);
        botaoCriarCaixa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoCriarCaixa.setBorder(new EmptyBorder(10, 20, 10, 20));
        botaoCriarCaixa.setIconTextGap(8);

        botaoCriarCaixa.addActionListener(e -> {
            Runnable callbackCompleto = () -> {
                this.recarregarCaixas();
                if (janelaPrincipal != null) {
                    janelaPrincipal.atualizarDashboardPrincipal();
                }
            };

            Frame framePai = (Frame) SwingUtilities.getWindowAncestor(this);
            JanelaCriarCaixa janela = new JanelaCriarCaixa(framePai, this.carteira, callbackCompleto);
            janela.setVisible(true);
        });

        cabecalho.add(titulo, BorderLayout.WEST);
        cabecalho.add(botaoCriarCaixa, BorderLayout.EAST);
        return cabecalho;
    }

    public JPanel criarItemDeCaixa(Caixa caixa, NumberFormat formatoMoeda) {
        JPanel painelItem = new JPanel(new BorderLayout(10, 0));
        painelItem.setOpaque(false);
        painelItem.setBorder(new EmptyBorder(10, 5, 10, 5));
        painelItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        painelItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel painelInfo = new JPanel();
        painelInfo.setLayout(new BoxLayout(painelInfo, BoxLayout.Y_AXIS));
        painelInfo.setOpaque(false);

        JLabel nomeCaixa = new JLabel(caixa.getNome());
        nomeCaixa.setFont(FonteInter.getBold(15f));
        nomeCaixa.setForeground(Paleta.TextoCorpo);

        painelInfo.add(nomeCaixa);

        JLabel saldoCaixa = new JLabel(formatoMoeda.format(caixa.getSaldoAtual()));
        saldoCaixa.setFont(FonteInter.getBold(15f));
        saldoCaixa.setForeground(Paleta.TextoDoValor);

        painelItem.add(painelInfo, BorderLayout.CENTER);
        painelItem.add(saldoCaixa, BorderLayout.EAST);

        painelItem.addMouseListener(new MouseAdapter() {
            public Color originalNomeColor = nomeCaixa.getForeground();
            public Color originalSaldoColor = saldoCaixa.getForeground();

            @Override
            public void mouseClicked(MouseEvent e) {
                Frame framePai = (Frame) SwingUtilities.getWindowAncestor(PainelMeusCaixas.this);
                Runnable callback = () -> {
                    recarregarCaixas();
                    if (janelaPrincipal != null) {
                        janelaPrincipal.atualizarDashboardPrincipal();
                    }
                };
                JanelaDetalhesCaixa janela = new JanelaDetalhesCaixa(framePai, caixa, carteira, callback);
                janela.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                nomeCaixa.setForeground(Paleta.Azul);
                saldoCaixa.setForeground(Paleta.Azul);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nomeCaixa.setForeground(originalNomeColor);
                saldoCaixa.setForeground(originalSaldoColor);
            }
        });

        return painelItem;
    }
}