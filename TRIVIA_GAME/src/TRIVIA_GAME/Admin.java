package TRIVIA_GAME;


public class Admin extends Usuario {

	  private String nome;
	  private static String loginPadrao = "admin";
	  private static String senhaPadrao = "1234";

	  /**
	   * Construtor Admin.
	   * 
	   * @param nome nome do usuario admin
	   */
	  public Admin(String nome) {
	    super(nome);
	  }

	  public String getNome() {
	    return super.nome;
	  }

	  public void setNome(String nome) {
	    super.nome = nome;
	  }

	  public static boolean adminValido(String login, String senha) {
	    return login.equals(loginPadrao) && senha.equals(senhaPadrao);
	  }


	}