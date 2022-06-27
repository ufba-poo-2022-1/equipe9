package triviabot;

import java.util.List;

public class Jogador extends Usuario {

    private int pontuacao = 0;

    /**
     * Construtor Jogador.
     *
     * @param nome nome do jogador
     */
    public Jogador(String nome) {
        super(nome);
    }

    public static void resetRanking(List<Jogador> jogadores) {
        for (Jogador jogador : jogadores) {
            jogador.setPontuacao(0);
        }
    }

    /**
     * Retorna pontuacao do usuario.
     *
     * @return retorna a pontuacao
     */
    public int getPontuacao() {
        return this.pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

}