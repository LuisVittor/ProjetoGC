package com.criso.ui;

import com.criso.model.Carteira;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PainelHeader extends JPanel {

    public final Carteira carteira;
    public final JanelaPrincipal janelaPrincipal;
    public final Runnable callbackAtualizacao;

    public PainelHeader(String nomeUsuario, Carteira carteira, JanelaPrincipal janelaPrincipal, Runnable callbackAtualizacao) {
        this.carteira = carteira;
        this.janelaPrincipal = janelaPrincipal;
        this.callbackAtualizacao = callbackAtualizacao;

        setBackground(Paleta.FundoAplicacao);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 30, 0, 30));

        JPanel painelInfoUsuario = new JPanel();
        painelInfoUsuario.setOpaque(false);
        painelInfoUsuario.setLayout(new BoxLayout(painelInfoUsuario, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Minha Carteira");
        titulo.setFont(FonteInter.getBold(28f));
        titulo.setForeground(Paleta.TextoTitulo);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Bem-vindo, " + nomeUsuario + "!");
        subtitulo.setFont(FonteInter.getRegular(16f));
        subtitulo.setForeground(Color.DARK_GRAY);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        painelInfoUsuario.add(titulo);
        painelInfoUsuario.add(Box.createVerticalStrut(8));
        painelInfoUsuario.add(subtitulo);

        JPanel painelBotoesTransacao = new JPanel();
        painelBotoesTransacao.setOpaque(false);
        painelBotoesTransacao.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        BotaoArredondado botaoDespesa = new BotaoArredondado("Nova Despesa");
        botaoDespesa.setFont(FonteInter.getBold(14f));
        botaoDespesa.setBackground(Paleta.VerdeEscuro);
        botaoDespesa.setForeground(Paleta.Branco);
        botaoDespesa.setIcon(new IconeOperacao(IconeOperacao.Tipo.SUBTRACAO, Paleta.Vermelho, 12));
        botaoDespesa.setRaioDaBorda(12);
        botaoDespesa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoDespesa.setBorder(new EmptyBorder(10, 20, 10, 20));
        botaoDespesa.setIconTextGap(8);
        botaoDespesa.addActionListener(e -> {
            JanelaCriarDespesa janelaDespesa = new JanelaCriarDespesa(this.janelaPrincipal, this.carteira, this.callbackAtualizacao);
            janelaDespesa.setVisible(true);
        });
        painelBotoesTransacao.add(botaoDespesa);

        BotaoArredondado botaoReceita = new BotaoArredondado("Nova Receita");
        botaoReceita.setFont(FonteInter.getBold(14f));
        botaoReceita.setBackground(Paleta.VerdeEscuro);
        botaoReceita.setForeground(Paleta.Branco);
        botaoReceita.setIcon(new IconeOperacao(IconeOperacao.Tipo.ADICAO, Paleta.VerdeClaro, 12));
        botaoReceita.setRaioDaBorda(12);
        botaoReceita.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoReceita.setBorder(new EmptyBorder(10, 20, 10, 20));
        botaoReceita.setIconTextGap(8);
        botaoReceita.addActionListener(e -> {
            JanelaCriarReceita janelaReceita = new JanelaCriarReceita(this.janelaPrincipal, this.carteira, this.callbackAtualizacao);
            janelaReceita.setVisible(true);
        });
        painelBotoesTransacao.add(botaoReceita);

        BotaoArredondado botaoTransferencia = new BotaoArredondado("Transferência");
        botaoTransferencia.setFont(FonteInter.getBold(14f));
        botaoTransferencia.setBackground(Paleta.VerdeEscuro);
        botaoTransferencia.setForeground(Paleta.Branco);
        botaoTransferencia.setIcon(new IconeOperacao(IconeOperacao.Tipo.ADICAO, Paleta.Amarelo, 12));
        botaoTransferencia.setRaioDaBorda(12);
        botaoTransferencia.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoTransferencia.setBorder(new EmptyBorder(10, 20, 10, 20));
        botaoTransferencia.setIconTextGap(8);
        botaoTransferencia.addActionListener(e -> {
            JanelaCriarTransferencia janelaTransferencia = new JanelaCriarTransferencia(this.janelaPrincipal, this.carteira, this.callbackAtualizacao);
            janelaTransferencia.setVisible(true);
        });
        painelBotoesTransacao.add(botaoTransferencia);

        add(painelInfoUsuario, BorderLayout.WEST);
        add(painelBotoesTransacao, BorderLayout.EAST);
    }
}