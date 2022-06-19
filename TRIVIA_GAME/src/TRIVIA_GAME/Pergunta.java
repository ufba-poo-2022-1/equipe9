package TRIVIA_GAME;
import java.util.ArrayList;

public class Pergunta {
    private String pergunta;
    private ArrayList<String> alternativas = new ArrayList<String>();
    private String resposta; //indice maior que 1

    // Construtor

    public Pergunta(String pergunta,  ArrayList<String> alternativas , String resposta){
        this.pergunta = pergunta;
        this.alternativas = alternativas;
        this.resposta = resposta;
    }

    // Métodos

    public void PrintPergunta(){
		System.out.println(getPergunta());

        //char c = 'A'; //primeira alternativa

        for(String alt : getAlternativas()){
            //System.out.print(c++ + ". "); //Prefixo da alternativa
            System.out.println(alt);
        }

    }


    // Getters

    public String getPergunta(){
        return pergunta;
    }

    public String getResposta(){
        return resposta;
    }

    public ArrayList<String> getAlternativas(){ // Para todas as alternativas
        return alternativas;
    }

    public String getAlternativa(String alt){ // Para uma alternativa específica

        // Converte Char to Int. A = 1, B = 2....
        int indice = (int) alt.toUpperCase().charAt(0) - 64;
        return alternativas.get(indice); //indices de 1 a N
    }

    
    // Setters
    
    public void setPergunta(String pergunta){
        this.pergunta = pergunta;
    }

    public void setResposta(String resposta){
        this.resposta = resposta;

    }

    public void setAlternativa(int indice, String alternativa){ // Para uma alternativa específica
        alternativas.set(indice+1, alternativa);
    }

    public void setAlternativas(ArrayList<String> alternativas){ // Para todas as alternativas
        this.alternativas = alternativas;
    }


}
