package trivia_bot;
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

    // Imprime o enunciado da pergunta
    public void PrintPergunta(){
		System.out.println(getPergunta());

        for(String alt : getAlternativas()){
            System.out.println(alt);
        }

    }

    //Retorna string com alternativas separadas por linha
    public String StringAlternativas(){
        String s = "";
        for (String alt : getAlternativas()){
        	s += alt + "\n";
        }
        return s;
    }

    // Getters

    public String getPergunta(){
        return pergunta;
    }

    public String getResposta(){
        return resposta;
    }

    // Retorna array com todas as alternativas
    public ArrayList<String> getAlternativas(){ 
        return alternativas;
    }

    // Retorna alternativa X indicada no parametro
    public String getAlternativa(String alt){

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
