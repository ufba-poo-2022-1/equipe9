package trivia_bot;

public class Admin extends Usuario {

    private String login;
    String senha;

    public Admin(String nome){
    	super(nome);
    }
    

    public void dadosAdmin(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }
  /**
     * Retorna senha do usuário.
     * @param senha senha do usuário
     * @return retorna a senha
     */

    public String getSenha() {
        return senha;
    }
   /**
     * Retorna tipo do usuário.
     * @param tipo tipo do usuário
     * @return retorna o tipo
     */

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean equalsAdmin(Object admin) {
        Admin outroAdmin = (Admin)admin;
        return login.equals(outroAdmin.login);
    }
	
}
