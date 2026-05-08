package com.criso.ui;

import javax.swing.*;
import java.awt.*;

public class PainelArredondado extends JPanel {
    public Color corDeFundo;
    public int raioDaBorda = 15;
    public transient Icon icone;

    public PainelArredondado(LayoutManager layout, int raio) {
        super(layout);
        setOpaque(false);
        this.raioDaBorda = raio;
    }

    public PainelArredondado(int raio) {
        super();
        setOpaque(false);
        this.raioDaBorda = raio;
    }

    public void definirIcone(Icon icone) {
        this.icone = icone;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcos = new Dimension(raioDaBorda, raioDaBorda);
        int largura = getWidth();
        int altura = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (corDeFundo != null) {
            graphics.setColor(corDeFundo);
        } else {
            graphics.setColor(getBackground());
        }
        graphics.fillRoundRect(0, 0, largura - 1, altura - 1, arcos.width, arcos.height);

        if (icone != null) {
            int iconeX = (largura - icone.getIconWidth()) / 2;
            int iconeY = (altura - icone.getIconHeight()) / 2;
            icone.paintIcon(this, g, iconeX, iconeY);
        }
    }

    public void definirCorDeFundo(Color cor) {
        this.corDeFundo = cor;
        repaint();
    }
}