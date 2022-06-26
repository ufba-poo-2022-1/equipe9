package triviabot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class ListaPerguntas {

    private ArrayList<Pergunta> perguntas;

    public ListaPerguntas() {
        perguntas = new ArrayList<>();
        perguntas = leitorDePerguntas();
    }

    private ArrayList<Pergunta> leitorDePerguntas() {

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

        } catch (NumberFormatException | NullPointerException | IOException e) {
            Discordbot.getLogger().log(Level.SEVERE, Arrays.toString(e.getStackTrace()), e);
        }

        return perguntas;

    }

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

}