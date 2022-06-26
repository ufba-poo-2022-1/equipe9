package triviabot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaPerguntas {

    private static ArrayList<Pergunta> perguntas = new ArrayList<>();

    public ListaPerguntas() {
        perguntas.clear();
        perguntas = new ArrayList<>(LeitorDePerguntas());
    }

    public List<Pergunta> LeitorDePerguntas() {

        try (BufferedReader ler = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("perguntas.txt"))))) {
            int numeroPerguntas = Integer.parseInt(ler.readLine());

            for (int i = 0; i < numeroPerguntas; i++) {
                String linha0 = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);
                String linha1 = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);
                String linha2 = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);
                String linha3 = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);
                String linha4 = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);
                String linha5 = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);


                /* Adiciona as alternativas */
                ArrayList<String> alternativas = new ArrayList<>();
                alternativas.add(linha1);
                alternativas.add(linha2);
                alternativas.add(linha3);
                alternativas.add(linha4);

                perguntas.add(new Pergunta(linha0, alternativas, linha5));
            }

            Collections.shuffle(perguntas);

        } catch (NumberFormatException | NullPointerException | IOException e1) {
            e1.printStackTrace();
        }

        return perguntas;

    }

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

}