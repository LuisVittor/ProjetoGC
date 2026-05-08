package com.criso.persistencia;

import com.criso.model.Carteira;

import java.io.*;

public class Serializador{
    public static final String NOMEDIRETORIO = ".criso";
    public static final String NOMEARQUIVO = "dados";

    public static String getHome() {
        return System.getProperty("user.home");
    }

    public static String getAPath(){
        String homePath = getHome();
        File diretorio = new File(homePath, NOMEDIRETORIO);
        if(!diretorio.exists()){
            diretorio.mkdir();
        }
        File dados = new File(diretorio, NOMEARQUIVO);
        return dados.getAbsolutePath();
    }

    public static void serializar(Carteira obj){
        String APath = getAPath();
        try(FileOutputStream arquivoSaida = new FileOutputStream(APath);
             ObjectOutputStream saida = new ObjectOutputStream(arquivoSaida)){
            saida.writeObject(obj);
        }catch(IOException e){
            System.err.println("Erro ao serializar: " + e.getMessage());
        }
    }

    public static Carteira deserializar(){
        String APath = getAPath();
        Carteira obj = null;
        try(FileInputStream arquivoEntrada = new FileInputStream(APath);
             ObjectInputStream entrada = new ObjectInputStream(arquivoEntrada)){
            obj = (Carteira) entrada.readObject();
        }catch (FileNotFoundException e){
            System.err.println(e.getMessage());
        }catch (IOException | ClassNotFoundException e){
            System.err.println("Erro ao deserializar: " + e.getMessage());
        }
        return obj;
    }
}