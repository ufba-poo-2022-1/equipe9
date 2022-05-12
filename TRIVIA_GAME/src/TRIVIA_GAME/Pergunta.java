package TRIVIA_GAME;

public class Pergunta {
    String pergunta = null;

    String respostaUm = null;
    String respostaDois = null;
    String respostaTres = null;
    String respostaQuatro = null;

    String respostaCerta = null;

    /**
     * Construtor de Pergunta. Para criar uma pergunta é necessária uma descrição para a mesma
     * e 5 opções de respostas, além de indicar a opção correta.
     * @param pergunta recebe a descrição da pergunta.
     * @param respostaUm opção 1 de resposta.
     * @param respostaDois opção 2 de resposta.
     * @param respostaTres opção 3 de resposta.
     * @param respostaQuatro opção 4 de resposta.
     * @param respostaCerta opção de resposta correta.
     * 
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

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getRespostaUm() {
        return respostaUm;
    }

    public void setRespostaUm(String respostaUm) {
        this.respostaUm = respostaUm;
    }

    public String getRespostaDois() {
        return respostaDois;
    }

    public void setRespostaDois(String respostaDois) {
        this.respostaDois = respostaDois;
    }

    public String getRespostaTres() {
        return respostaTres;
    }

    public void setRespostaTres(String respostaTres) {
        this.respostaTres = respostaTres;
    }

    public String getRespostaQuatro() {
        return respostaQuatro;
    }

    public void setRespostaQuatro(String respostaQuatro) {
        this.respostaQuatro = respostaQuatro;
    }

    public String getRespostaCerta() {
        return respostaCerta;
    }

    public void setRespostaCerta(String respostaCerta) {
        this.respostaCerta = respostaCerta;
    }
}
