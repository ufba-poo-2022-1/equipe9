package TRIVIA_GAME;
import java.util.ArrayList;

public class Pergunta {
    private String pergunta;
    private ArrayList<String> alternativas = new ArrayList<String>();
    private int resposta; //indice maior que 1

    // Construtor

    public Pergunta(String pergunta,  ArrayList<String> alternativas , int resposta){
        this.pergunta = pergunta;
        this.alternativas = alternativas;
        this.resposta = resposta;
    }

    // Métodos

    public void PrintPergunta(){
		System.out.println(getPergunta());

        for(String alt : getAlternativas()){
            System.out.println(alt);
        }

    }


    // Getters

    public String getPergunta(){
        return pergunta;
    }

    public int getResposta(){
        return resposta;
    }

    public ArrayList<String> getAlternativas(){ // Para todas as alternativas
        return alternativas;
    }

    public String getAlternativa(int indice){ // Para uma alternativa específica
        return alternativas.get(indice+1); //indices de 1 a N
    }

    
    // Setters
    
    public void setPergunta(String pergunta){
        this.pergunta = pergunta;
    }

    public void setResposta(int resposta){
        this.resposta = resposta;

    }

    public void setAlternativa(int indice, String alternativa){ // Para uma alternativa específica
        alternativas.set(indice+1, alternativa);
    }

    public void setAlternativas(ArrayList<String> alternativas){ // Para todas as alternativas
        this.alternativas = alternativas;
    }


}
