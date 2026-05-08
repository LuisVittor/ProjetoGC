package com.criso.app;
import com.criso.model.Carteira;
import com.criso.ui.JanelaPrincipal;
import com.criso.persistencia.Serializador;

public class App {
    public static void main(String[] args) {
        // Configurações iniciais da aplicação
        Carteira carteira = Serializador.deserializar();
        if (carteira == null){
            carteira = new Carteira();
        }

        Carteira finalCarteira = carteira;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> 
            Serializador.serializar(finalCarteira)
        ));
        javax.swing.SwingUtilities.invokeLater(() -> {
            JanelaPrincipal janela = new JanelaPrincipal(finalCarteira);
            janela.setVisible(true);
        });
    }
}
