package com.criso.ui;

import com.criso.model.Caixa;
import com.criso.model.Carteira;
import com.criso.model.Despesa;
import com.criso.model.Categoria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class JanelaCriarTransferencia extends JDialog {

    public final Runnable callbackSucesso;
    public final Carteira carteira;

    public JTextField campoValor;
    public JTextField campoData;
    public JComboBox<Caixa> comboCaixaOrigem;
    public JComboBox<Caixa> comboCaixaDestino;

    public JanelaCriarTransferencia(Frame pai, Carteira carteira, Runnable callback) {
        super(pai, "Realizar Transferência", true);
        this.carteira = carteira;
        this.callbackSucesso = callback;

        setSize(550, 350);
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

        JLabel titulo = new JLabel("Realizar Transferência");
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

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.LINE_START;
        Font fonteLabel = FonteInter.getRegular(14f);
        Font fonteCampo = FonteInter.getRegular(14f);

        JLabel labelValor = new JLabel("Valor (R$):");
        labelValor.setFont(fonteLabel);
        gbc.gridy = 0;
        painel.add(labelValor, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        campoValor = new JTextField("0,00");
        estilizarCampo(campoValor, fonteCampo);
        gbc.gridy = 0;
        painel.add(campoValor, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel labelData = new JLabel("Data (DD/MM/AAAA):");
        labelData.setFont(fonteLabel);
        gbc.gridy = 1;
        painel.add(labelData, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        campoData = new JTextField(LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        estilizarCampo(campoData, fonteCampo);
        gbc.gridy = 1;
        painel.add(campoData, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel labelCaixaOrigem = new JLabel("Caixa de Origem:");
        labelCaixaOrigem.setFont(fonteLabel);
        gbc.gridy = 2;
        painel.add(labelCaixaOrigem, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        comboCaixaOrigem = new JComboBox<>(carteira.getCaixas().toArray(new Caixa[0]));
        estilizarComboBox(comboCaixaOrigem, fonteCampo);
        gbc.gridy = 2;
        painel.add(comboCaixaOrigem, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel labelCaixaDestino = new JLabel("Caixa de Destino:");
        labelCaixaDestino.setFont(fonteCampo);
        gbc.gridy = 3;
        painel.add(labelCaixaDestino, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        comboCaixaDestino = new JComboBox<>(carteira.getCaixas().toArray(new Caixa[0]));
        estilizarComboBox(comboCaixaDestino, fonteCampo);
        gbc.gridy = 3;
        painel.add(comboCaixaDestino, gbc);

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

    public <T> void estilizarComboBox(JComboBox<T> comboBox, Font fonte) {
        comboBox.setFont(fonte);
        comboBox.setForeground(Paleta.TextoCorpo);
        comboBox.setBackground(Paleta.Branco);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(5, 5, 5, 5)
        ));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Caixa) {
                    setText(((Caixa) value).getNome());
                }
                return this;
            }
        });
    }

    public JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painel.setOpaque(false);

        BotaoArredondado botaoSalvar = new BotaoArredondado("Realizar Transferência");
        botaoSalvar.setBackground(Paleta.VerdeEscuro);
        botaoSalvar.setForeground(Paleta.Amarelo);
        botaoSalvar.setFont(FonteInter.getBold(14f));
        botaoSalvar.setRaioDaBorda(12);
        botaoSalvar.setBorder(new EmptyBorder(10, 22, 10, 22));
        botaoSalvar.addActionListener(e -> salvarTransferencia());

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

    public void salvarTransferencia() {
        try {
            String valorStr = campoValor.getText().replace(".", "").replace(",", ".");
            BigDecimal valor = new BigDecimal(valorStr);

            LocalDate data = LocalDate.parse(campoData.getText(), java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Caixa caixaOrigem = (Caixa) comboCaixaOrigem.getSelectedItem();
            Caixa caixaDestino = (Caixa) comboCaixaDestino.getSelectedItem();

            if (valor.compareTo(BigDecimal.ZERO) <= 0 || caixaOrigem == null || caixaDestino == null) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos e garanta que o valor seja positivo.", "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (caixaOrigem.equals(caixaDestino)) {
                JOptionPane.showMessageDialog(this, "A caixa de origem e a caixa de destino não podem ser as mesmas.", "Erro de Transferência", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (carteira.getCaixas().size() < 2) {
                JOptionPane.showMessageDialog(this, "Você precisa de pelo menos duas caixas para realizar uma transferência.", "Erro de Transferência", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nomeTransacao = "Transferência de " + caixaOrigem.getNome() + " para " + caixaDestino.getNome();

            Categoria categoriaTransferencia = null;
            if (!carteira.getCategorias().isEmpty()) {
                categoriaTransferencia = carteira.getCategorias().get(0);
            }

            Despesa despesaTransferencia = new Despesa(nomeTransacao, valor, data, com.criso.model.StatusDespesa.Pago, caixaOrigem, caixaDestino);
            despesaTransferencia.setCategoria(categoriaTransferencia);
            carteira.adicionarTransacao(despesaTransferencia);

            JOptionPane.showMessageDialog(this, "Transferência de R$" + valor + " realizada com sucesso de '" + caixaOrigem.getNome() + "' para '" + caixaDestino.getNome() + "'!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            if (callbackSucesso != null) {
                callbackSucesso.run();
            }

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O valor deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "A data deve estar no formato DD/MM/AAAA.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao realizar transferência: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}