package TRIVIA_GAME;

import java.util.ArrayList;
import java.util.Iterator;

public class Usuario {
	 
		protected String nome;
		boolean logged = false;
	    int id;

	    /**
	     * Construtor usu�rio.
	     *
	     * @param nome nome do usu�rio 
	     * @param login login de acesso do usu�rio 
	     * @param senha senha de acesso do usu�rio 
	     * @param tipo identifica o tipo de usu�rio 
	     */

	    public Usuario (String nome){
	        this.nome = nome;
	    }
	    
	    /**
	     * Retorna nome do usu�rio.
	     * @return retorna o nome
	     */
	    public String getNome() {
	        return nome;
	    }

	    public void setNome(String nome) {
	        this.nome = nome;
	    }

	     /**
	     * Retorna id do usu�rio.
	     * @return retorna o id
	     */
	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }
	    /**
	     * Valida��o para que usu�rio n�o seja nulo.
		 * @param string nome do usu�rio 
	     * @return true caso a string n�o seja nula
	     */
	    public boolean notNullString(String string) {
	        return !string.trim().equals("");
	      }
	    
	    public void setIsLogged (boolean logged) {
	    	this.logged = logged;
	    }
	    /**
	     * Retorna status do login.
	     * @return true caso o usu�rio tenha um login v�lido.
	     */
	    public boolean getIsLogged () {
	    	return this.logged;
	    }

			public static Usuario existe(String nome, ArrayList<? extends Usuario> usuarios) {
				Iterator<? extends Usuario> iterator = usuarios.iterator();
				while (iterator.hasNext()) {
					Usuario usuario = iterator.next();
					if (usuario.nome.equals(nome)) {
						return usuario;
					}
				}
				return null;
			}

}
