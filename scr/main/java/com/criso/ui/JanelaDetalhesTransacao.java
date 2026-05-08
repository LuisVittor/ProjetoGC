package com.criso.ui;

import com.criso.model.Carteira;
import com.criso.model.Despesa;
import com.criso.model.Receita;
import com.criso.model.StatusDespesa;
import com.criso.model.Transacao;
import com.criso.persistencia.Serializador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class JanelaDetalhesTransacao extends JDialog {

    private final transient Transacao transacao;
    private final transient Carteira carteira;
    private final transient Runnable onTransacoesAtualizadasCallback;

    public JanelaDetalhesTransacao(Frame parent, Transacao transacao, Carteira carteira, Runnable onTransacoesAtualizadasCallback) {
        super(parent, "Detalhes da Transação", true);
        this.transacao = transacao;
        this.carteira = carteira;
        this.onTransacoesAtualizadasCallback = onTransacoesAtualizadasCallback;
        initComponents();
        setupLayout();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(450, 400);
        setMinimumSize(new Dimension(400, 350));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(Paleta.Azul, 1));
    }

    private void setupLayout() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(15, 15));
        painelPrincipal.setBackground(Paleta.Branco);
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Detalhes da Transação");
        titulo.setFont(FonteInter.getBold(20f));
        titulo.setForeground(Paleta.TextoTitulo);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel painelInfo = new JPanel(new GridBagLayout());
        painelInfo.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 10);

        addInfoRow(painelInfo, gbc, "Nome:", transacao.getNome());
        addInfoRow(painelInfo, gbc, "Valor:", NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(transacao.getValor()));
        addInfoRow(painelInfo, gbc, "Data:", transacao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        addInfoRow(painelInfo, gbc, "Categoria:", transacao.getCategoria() != null ? transacao.getCategoria().getNome() : "N/A");
        addInfoRow(painelInfo, gbc, "Descrição:", transacao.getDescricao() != null && !transacao.getDescricao().isEmpty() ? transacao.getDescricao() : "Sem descrição");

        if (transacao instanceof Receita receita) {
            addInfoRow(painelInfo, gbc, "Tipo:", "Receita");
            addInfoRow(painelInfo, gbc, "Caixa de Destino:", receita.getCaixaQueRecebe().getNome());
        } else if (transacao instanceof Despesa despesa) {
            if (despesa.getCaixaQueRecebe() != null) {
                addInfoRow(painelInfo, gbc, "Tipo:", "Transferência");
                addInfoRow(painelInfo, gbc, "Caixa de Origem:", despesa.getCaixaQuePaga().getNome());
                addInfoRow(painelInfo, gbc, "Caixa de Destino:", despesa.getCaixaQueRecebe().getNome());
            } else {
                addInfoRow(painelInfo, gbc, "Tipo:", "Despesa");
                addInfoRow(painelInfo, gbc, "Caixa de Origem:", despesa.getCaixaQuePaga().getNome());
                addInfoRow(painelInfo, gbc, "Status:", despesa.getStatus().name());
            }
        }

        JPanel wrapperInfo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapperInfo.setOpaque(false);
        wrapperInfo.add(painelInfo);
        painelPrincipal.add(wrapperInfo, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(new EmptyBorder(10, 0, 0, 0));

        BotaoArredondado btnExcluir = new BotaoArredondado("Excluir");
        btnExcluir.setBackground(Paleta.Vermelho);
        btnExcluir.setForeground(Paleta.Branco);
        btnExcluir.setFont(FonteInter.getBold(12f));
        btnExcluir.setRaioDaBorda(8);
        btnExcluir.setBorder(new EmptyBorder(6, 12, 6, 12));
        btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(e -> excluirTransacao());
        painelBotoes.add(btnExcluir);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.setFont(FonteInter.getBold(12f));
        btnFechar.setForeground(Paleta.Azul);
        btnFechar.setContentAreaFilled(false);
        btnFechar.setBorderPainted(false);
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addActionListener(e -> dispose());
        painelBotoes.add(btnFechar);

        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
        pack();
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbc, String labelText, String valueText) {
        JLabel label = new JLabel(labelText);
        label.setFont(FonteInter.getBold(14f));
        label.setForeground(Paleta.TextoCorpo);
        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(label, gbc);

        JLabel value = new JLabel(valueText);
        value.setFont(FonteInter.getRegular(14f));
        value.setForeground(Paleta.Azul);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(value, gbc);

        gbc.gridy++;
    }

    private void excluirTransacao() {
        int confirmacao = JOptionPane.showConfirmDialog(this,
                String.format("Tem certeza que deseja excluir a transação '%s'?", transacao.getNome()),
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                if (transacao instanceof Despesa despesa) {
                    if (despesa.getStatus() == StatusDespesa.Pago) {
                        despesa.getCaixaQuePaga().adicionarValor(despesa.getValor());
                    }
                    if (despesa.getCaixaQueRecebe() != null) {
                        despesa.getCaixaQueRecebe().removerValor(despesa.getValor());
                    }
                } else if (transacao instanceof Receita receita) {
                    receita.getCaixaQueRecebe().removerValor(receita.getValor());
                }

                carteira.removerTransacao(transacao);
                Serializador.serializar(carteira);
                JOptionPane.showMessageDialog(this,
                        "Transação excluída com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                if (onTransacoesAtualizadasCallback != null) {
                    onTransacoesAtualizadasCallback.run();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir transação: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}