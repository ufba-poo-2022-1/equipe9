package TRIVIA_GAME;

public class Jogador extends Usuario {

    private int pontuacao;

    public Jogador() { }

    public Jogador(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getPontuacao() {
        return pontuacao;
    }
    /**
     * Retorna pontuacao do usuário.
     * @param pontuacao pontuacao do usuário
     * @return retorna a pontuacao
     */

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public void adicionarPontuacao(int pontuacao) {
        this.pontuacao += pontuacao;
    }
    public boolean equalsNamejogador(Object jogador) {
        Jogador outroJogador = (Jogador)jogador;
        return this.getNome().equals(outroJogador.getNome());
      }
}