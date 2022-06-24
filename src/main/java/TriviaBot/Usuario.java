package TriviaBot;

import java.util.ArrayList;
import java.util.Iterator;

public class Usuario {
	 
		protected String nome;
		boolean logged = false;
	    int id;

	    /**
	     * Construtor usuario.
	     *
	     * @param nome nome do usuario 
	     * @param login login de acesso do usuario 
	     * @param senha senha de acesso do usuario 
	     * @param tipo identifica o tipo de usuario 
	     */

	    public Usuario (String nome){
	        this.nome = nome;
	    }
	    
	    /**
	     * Retorna nome do usuario.
	     * @return retorna o nome
	     */
	    public String getNome() {
	        return nome;
	    }

	    public void setNome(String nome) {
	        this.nome = nome;
	    }

	     /**
	     * Retorna id do usuario.
	     * @return retorna o id
	     */
	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }
	    /**
	     * Validacao para que usuario nao seja nulo.
		 * @param string nome do usuario 
	     * @return true caso a string nao seja nula
	     */
	    public boolean notNullString(String string) {
	        return !string.trim().equals("");
	      }
	    
	    public void setIsLogged (boolean logged) {
	    	this.logged = logged;
	    }
	    /**
	     * Retorna status do login.
	     * @return true caso o usuario tenha um login valido.
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
