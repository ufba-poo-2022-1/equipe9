package TriviaBot;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class ListaPerguntas {

    private static ArrayList<Pergunta> perguntas = new ArrayList<>();

    public ListaPerguntas() {
        perguntas = new ArrayList<>(LeitorDePerguntas());
    }

    public ArrayList<Pergunta> LeitorDePerguntas() {
        BufferedReader ler = null;
        FileInputStream fis;
        try {
            fis = new FileInputStream("perguntas.txt");
            ler = new BufferedReader(new InputStreamReader(fis));
        } catch (FileNotFoundException e1) {
            /** TODO Auto-generated catch block*/
            e1.printStackTrace();
        }


        int numeroPerguntas = 0;
        try {
            assert ler != null;
            numeroPerguntas = Integer.parseInt(ler.readLine());
        } catch (NumberFormatException | NullPointerException | IOException e1) {
            /** TODO Auto-generated catch block*/
            e1.printStackTrace();
        }

        for (int i = 0; i < numeroPerguntas; i++) {
            String linha0 = null;
            String linha1 = null;
            String linha2 = null;
            String linha3 = null;
            String linha4 = null;
            String linha5 = null;
            try {
                linha0 = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);
                linha1 = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);
                linha2 = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);
                linha3 = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);
                linha4 = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);
                linha5 = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                /** TODO Auto-generated catch block */
                e.printStackTrace();
            }
            /** Adiciona as alternativas */
            ArrayList<String> alternativas = new ArrayList<>();
            alternativas.add(linha1);
            alternativas.add(linha2);
            alternativas.add(linha3);
            alternativas.add(linha4);

            perguntas.add(new Pergunta(linha0, alternativas, linha5));
        }
        Collections.shuffle(perguntas);
        return perguntas;
    }

    public ArrayList<Pergunta> getPerguntas() {
        return perguntas;
    }

}