package trivia_bot;

public class Jogador extends Usuario{

	 private int pontuacao=0;


	    public Jogador(String nome) {
	    	super(nome);
	    }


	    public int getPontuacao() {
	        return this.pontuacao;
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
