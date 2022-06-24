package TriviaBot;

import java.util.ArrayList;
import java.util.Iterator;

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
	     * Retorna pontua��o do usu�rio.
	     * @return retorna a pontua��o
	     */
	    public int getPontuacao() {
	        return this.pontuacao;
	    }
	
	    public void setPontuacao(int pontuacao) {
	        this.pontuacao = pontuacao;
	    }
		/**
	     * Incrementa pontuacao do usu�rio.
		 * @param pontuacao pontua��o por acerto
	     */

	    public void adicionarPontuacao(int pontuacao) {
	        this.pontuacao += pontuacao;
	    }
		/**
		* compara nome do jogador, o nome do jogador deve ser �nico.
		* @param  jogador 
		* @return retorna true or false para a compara��o 
		*/
	    public boolean equalsNamejogador(Object jogador) {
	        Jogador outroJogador = (Jogador)jogador;
	        return this.getNome().equals(outroJogador.getNome());
	      }

	public static void resetRanking(ArrayList<Jogador> jogadores) {
		Iterator<Jogador> iterator = jogadores.iterator();
		while (iterator.hasNext()) {
			Jogador jogador = iterator.next();
			jogador.setPontuacao(0);
		}
	}

}