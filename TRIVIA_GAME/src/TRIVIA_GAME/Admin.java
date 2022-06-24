package TRIVIA_GAME;


public class Admin extends Usuario {

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

	  public static boolean adminValido(String login, String senha) {
	    return login.equals(loginPadrao) && senha.equals(senhaPadrao);
	  }

	}