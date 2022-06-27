package triviabot;

import java.util.List;

public class Pergunta {
    private final String enunciado;
    private final String resposta; //indice maior que 1
    private final List<String> alternativas;

    // Construtor

    public Pergunta(String enunciado, List<String> alternativas, String resposta) {
        this.enunciado = enunciado;
        this.alternativas = alternativas;
        this.resposta = resposta;
    }

    // Metodos

    //Retorna string com alternativas separadas por linha
    public String stringAlternativas() {
        StringBuilder s = new StringBuilder();
        for (String alt : getAlternativas()) {
            s.append(alt).append("\n");
        }
        return s.toString();
    }

    // Getters

    public String getPergunta() {
        return enunciado;
    }

    public String getResposta() {
        return resposta;
    }

    // Retorna array com todas as alternativas
    public List<String> getAlternativas() {
        return alternativas;
    }


}
