package trivia_bot;
public class Pergunta{
    String pergunta = null;
    
    String respostaUm = null;
    String respostaDois = null;
    String respostaTres = null;
    String respostaQuatro = null;
    
    String respostaCerta = null;
    
    public Pergunta(String pergunta, String respostaUm, String respostaDois,
            String respostaTres, String respostaQuatro, String respostaCerta) {
            
        this.pergunta = pergunta;
        
        this.respostaUm = respostaUm;
        this.respostaDois = respostaDois;
        this.respostaTres = respostaTres;
        this.respostaQuatro = respostaQuatro;
        
        this.respostaCerta = respostaCerta;
    }
    
}
