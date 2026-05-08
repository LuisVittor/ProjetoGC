package com.criso.ui;

import javax.swing.*;
import java.awt.*;

public class IconeOperacao implements Icon {
    public enum Tipo { ADICAO, SUBTRACAO }

    public final Tipo tipo;
    public final Color cor;
    public final int tamanho;

    public IconeOperacao(Tipo tipo, Color cor, int tamanho) {
        this.tipo = tipo;
        this.cor = cor;
        this.tamanho = tamanho;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(cor);
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));

        int comprimentoLinha = tamanho / 2;

        int centroX = x + tamanho / 2;
        int centroY = y + tamanho / 2;

        g2.drawLine(centroX - comprimentoLinha, centroY, centroX + comprimentoLinha, centroY);

        if (tipo == Tipo.ADICAO) {
            g2.drawLine(centroX, centroY - comprimentoLinha, centroX, centroY + comprimentoLinha);
        }

        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return tamanho;
    }

    @Override
    public int getIconHeight() {
        return tamanho;
    }
}