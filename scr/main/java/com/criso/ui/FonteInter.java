package com.criso.ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public final class FonteInter {

    public static final String CAMINHO_INTER_REGULAR = "/fontes/Inter-VariableFont_opsz,wght.ttf";
    public static final String CAMINHO_INTER_ITALIC = "/fontes/Inter-Italic-VariableFont_opsz,wght.ttf";

    public static final Font fonteBaseRegular;
    public static final Font fonteBaseItalic;

    public FonteInter() {
    }

    static {
        try {
            fonteBaseRegular = carregarFonte(CAMINHO_INTER_REGULAR);
            fonteBaseItalic = carregarFonte(CAMINHO_INTER_ITALIC);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fonteBaseRegular);
            ge.registerFont(fonteBaseItalic);

        } catch (IOException | FontFormatException e) {
            System.err.println("Erro crítico: Falha ao carregar as fontes da aplicação.");
            e.printStackTrace();
            throw new RuntimeException("Não foi possível inicializar as fontes customizadas.", e);
        }
    }

    public static Font carregarFonte(String caminho) throws IOException, FontFormatException {
        try (InputStream stream = FonteInter.class.getResourceAsStream(caminho)) {
            if (stream == null) {
                throw new IOException("Arquivo de fonte não encontrado em: " + caminho);
            }
            return Font.createFont(Font.TRUETYPE_FONT, stream);
        }
    }

    public static Font getRegular(float tamanho) {
        return fonteBaseRegular.deriveFont(Font.PLAIN, tamanho);
    }

    public static Font getBold(float tamanho) {
        return fonteBaseRegular.deriveFont(Font.BOLD, tamanho);
    }

    public static Font getItalic(float tamanho) {
        return fonteBaseItalic.deriveFont(Font.PLAIN, tamanho);
    }

    public static Font getBoldItalic(float tamanho) {
        return fonteBaseItalic.deriveFont(Font.BOLD, tamanho);
    }
}