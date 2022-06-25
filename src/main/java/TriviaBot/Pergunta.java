package TriviaBot;

import java.util.ArrayList;

public class Pergunta {
    private final String pergunta;
    private final String resposta; //indice maior que 1
    private final ArrayList<String> alternativas;

    // Construtor

    public Pergunta(String pergunta, ArrayList<String> alternativas, String resposta) {
        this.pergunta = pergunta;
        this.alternativas = alternativas;
        this.resposta = resposta;
    }

    // Metodos

    //Retorna string com alternativas separadas por linha
    public String StringAlternativas() {
        StringBuilder s = new StringBuilder();
        for (String alt : getAlternativas()) {
            s.append(alt).append("\n");
        }
        return s.toString();
    }

    // Getters

    public String getPergunta() {
        return pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    // Retorna array com todas as alternativas
    public ArrayList<String> getAlternativas() {
        return alternativas;
    }


}
