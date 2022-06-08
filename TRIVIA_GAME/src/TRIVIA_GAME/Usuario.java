package trivia_bot;

public class Usuario {
	 
		protected String nome;
		boolean logged = false;
	    int id;

	    /**
	     * Construtor usuário.
	     *
	     * @param nome nome do usuário 
	     * @param login login de acesso do usuário 
	     * @param senha senha de acesso do usuário 
	     * @param tipo identifica o tipo de usuário 
	     */

	    public Usuario (String nome){
	        this.nome = nome;
	    }
	    
	    /**
	     * Retorna nome do usuário.
	     * @return retorna o nome
	     */
	    public String getNome() {
	        return nome;
	    }

	    public void setNome(String nome) {
	        this.nome = nome;
	    }

	     /**
	     * Retorna id do usuário.
	     * @return retorna o id
	     */
	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }
	    
	    public boolean notNullString(String string) {
	        return !string.trim().equals("");
	      }
	    
	    public void setIsLogged (boolean logged) {
	    	this.logged = logged;
	    }
	    
	    public boolean getIsLogged () {
	    	return this.logged;
	    }
	    
}
