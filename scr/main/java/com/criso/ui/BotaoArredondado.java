package com.criso.ui;

import javax.swing.*;
import java.awt.*;

public class BotaoArredondado extends JButton {
    public int raioDaBorda = 15;

    public BotaoArredondado(String texto) {
        super(texto);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    public void setRaioDaBorda(int raio) {
        this.raioDaBorda = raio;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isArmed()) {
            g2.setColor(getBackground().darker());
        } else {
            g2.setColor(getBackground());
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), raioDaBorda, raioDaBorda);

        super.paintComponent(g);
        g2.dispose();
    }
}