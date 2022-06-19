package TRIVIA_GAME;

public class Jogador extends Usuario{

	 private int pontuacao=0;

		/**
		* Construtor Jogador.
		* @param nome nome do jogador
		*/
	    public Jogador(String nome) {
	    	super(nome);
	    }

		/**
	     * Retorna pontuacao do usuário.
	     * @return retorna a pontuacao
	     */
	    public int getPontuacao() {
	        return this.pontuacao;
	    }
	
	    public void setPontuacao(int pontuacao) {
	        this.pontuacao = pontuacao;
	    }
		/**
	     * Incrementa pontuacao do usuário.
		 * @param pontuacao pontuação por acerto
	     */

	    public void adicionarPontuacao(int pontuacao) {
	        this.pontuacao += pontuacao;
	    }
		/**
		* compara nome do jogador, o nome do jogador deve ser único.
		* @param  jogador 
		* @return retorna true or false para a comparação 
		*/
	    public boolean equalsNamejogador(Object jogador) {
	        Jogador outroJogador = (Jogador)jogador;
	        return this.getNome().equals(outroJogador.getNome());
	      }
	   
}
