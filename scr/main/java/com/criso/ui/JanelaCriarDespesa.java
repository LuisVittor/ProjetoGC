package com.criso.ui;

import com.criso.model.Caixa;
import com.criso.model.Carteira;
import com.criso.model.Categoria;
import com.criso.model.Despesa;
import com.criso.model.StatusDespesa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class JanelaCriarDespesa extends JDialog {

    public final Runnable callbackSucesso;
    public final Carteira carteira;

    public JTextField campoNome;
    public JTextField campoValor;
    public JTextField campoData;
    public JComboBox<Caixa> comboCaixaPaga;
    public JComboBox<StatusDespesa> comboStatus;
    public JComboBox<Categoria> comboCategoria;

    public static final List<Categoria> CategoriasDespesa = Arrays.asList(
        new Categoria("Outros (Despesas)", String.format("#%06X", (Paleta.TextoTitulo.darker().getRGB() & 0xFFFFFF))),
        new Categoria("Moradia", String.format("#%06X", (Paleta.Vermelho.getRGB() & 0xFFFFFF))),
        new Categoria("Alimentação", String.format("#%06X", (Paleta.Amarelo.getRGB() & 0xFFFFFF))),
        new Categoria("Transporte", String.format("#%06X", (Paleta.Azul.getRGB() & 0xFFFFFF))),
        new Categoria("Saúde", String.format("#%06X", (Paleta.VerdeClaro.darker().getRGB() & 0xFFFFFF))),
        new Categoria("Educação", String.format("#%06X", (Paleta.VerdeEscuro.darker().getRGB() & 0xFFFFFF))),
        new Categoria("Lazer", String.format("#%06X", (Paleta.CinzaAzulado.darker().getRGB() & 0xFFFFFF))),
        new Categoria("Vestuário", String.format("#%06X", (Paleta.TextoCorpo.darker().getRGB() & 0xFFFFFF))),
        new Categoria("Serviços Essenciais", String.format("#%06X", (Paleta.TextoDoValor.darker().getRGB() & 0xFFFFFF)))
        );

    public JanelaCriarDespesa(Frame pai, Carteira carteira, Runnable callback) {
        super(pai, "Registrar Nova Despesa", true);
        this.carteira = carteira;
        this.callbackSucesso = callback;

        setSize(550, 450);
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

        JLabel titulo = new JLabel("Registrar Nova Despesa");
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

        JLabel labelNome = new JLabel("Nome da Despesa:");
        labelNome.setFont(fonteLabel);
        gbc.gridy = 0;
        painel.add(labelNome, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        campoNome = new JTextField("");
        estilizarCampo(campoNome, fonteCampo);
        gbc.gridy = 0;
        painel.add(campoNome, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel labelValor = new JLabel("Valor (R$):");
        labelValor.setFont(fonteLabel);
        gbc.gridy = 1;
        painel.add(labelValor, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        campoValor = new JTextField("0,00");
        estilizarCampo(campoValor, fonteCampo);
        gbc.gridy = 1;
        painel.add(campoValor, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel labelData = new JLabel("Data (DD/MM/AAAA):");
        labelData.setFont(fonteLabel);
        gbc.gridy = 2;
        painel.add(labelData, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        campoData = new JTextField(LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        estilizarCampo(campoData, fonteCampo);
        gbc.gridy = 2;
        painel.add(campoData, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel labelCaixaPaga = new JLabel("Caixa que Paga:");
        labelCaixaPaga.setFont(fonteLabel);
        gbc.gridy = 3;
        painel.add(labelCaixaPaga, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        comboCaixaPaga = new JComboBox<>(carteira.getCaixas().toArray(new Caixa[0]));
        estilizarComboBox(comboCaixaPaga, fonteCampo);
        gbc.gridy = 3;
        painel.add(comboCaixaPaga, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel labelStatus = new JLabel("Status:");
        labelStatus.setFont(fonteLabel);
        gbc.gridy = 4;
        painel.add(labelStatus, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        comboStatus = new JComboBox<>(StatusDespesa.values());
        estilizarComboBox(comboStatus, fonteCampo);
        gbc.gridy = 4;
        painel.add(comboStatus, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel labelCategoria = new JLabel("Categoria:");
        labelCategoria.setFont(fonteLabel);
        gbc.gridy = 5;
        painel.add(labelCategoria, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        comboCategoria = new JComboBox<Categoria>(JanelaCriarDespesa.CategoriasDespesa.toArray(new Categoria[0]));
        estilizarComboBox(comboCategoria, fonteCampo);
        gbc.gridy = 5;
        painel.add(comboCategoria, gbc);

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
                } else if (value instanceof Categoria) {
                    setText(((Categoria) value).getNome());
                }
                return this;
            }
        });
    }

    public JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painel.setOpaque(false);

        BotaoArredondado botaoSalvar = new BotaoArredondado("Salvar Despesa");
        botaoSalvar.setBackground(Paleta.VerdeEscuro);
        botaoSalvar.setForeground(Paleta.Amarelo);
        botaoSalvar.setFont(FonteInter.getBold(14f));
        botaoSalvar.setRaioDaBorda(12);
        botaoSalvar.setBorder(new EmptyBorder(10, 22, 10, 22));
        botaoSalvar.addActionListener(e -> salvarDespesa());

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

    public void salvarDespesa() {
        try {
            String nome = campoNome.getText().trim();
            String valorStr = campoValor.getText().replace(".", "").replace(",", ".");
            BigDecimal valor = new BigDecimal(valorStr);

            LocalDate data = LocalDate.parse(campoData.getText(), java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Caixa caixaPaga = (Caixa) comboCaixaPaga.getSelectedItem();
            StatusDespesa status = (StatusDespesa) comboStatus.getSelectedItem();
            Categoria categoria = (Categoria) comboCategoria.getSelectedItem();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um nome para a despesa.", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (valor.compareTo(BigDecimal.ZERO) <= 0 || caixaPaga == null || status == null || categoria == null) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos obrigatórios e garanta que o valor seja positivo.", "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Despesa novaDespesa = new Despesa(nome, valor, data, status, caixaPaga);
            novaDespesa.setCategoria(categoria);

            carteira.adicionarTransacao(novaDespesa);

            JOptionPane.showMessageDialog(this, "Despesa '" + nome + "' registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            if (callbackSucesso != null) {
                callbackSucesso.run();
            }

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O valor deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "A data deve estar no formato DD/MM/AAAA.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar despesa: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}