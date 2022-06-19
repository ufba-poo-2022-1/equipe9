package TRIVIA_GAME;

public class Pergunta{
    String pergunta = null;
    
    String respostaUm = null;
    String respostaDois = null;
    String respostaTres = null;
    String respostaQuatro = null;
    
    String respostaCerta = null;
    /**
     * Cria uma nova pergunta.
     * @param pergunta descrição da pergunta
     * @param respostaUm descrição opção 1
     * @param respostaDois descrição opção 2
     * @param respostaTres descrição opção 3
     * @param respostaQuatro descrição opção 4
     * @param respostaCerta descrição resposta certa
     */
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
