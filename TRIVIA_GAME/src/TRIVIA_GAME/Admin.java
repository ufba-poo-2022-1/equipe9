package TRIVIA_GAME;

import java.util.ArrayList;
import java.util.Iterator;

public class Admin extends Usuario {

  private String nome;
  private static String loginPadrao = "admin";
  private static String senhaPadrao = "1234";

  /**
   * Construtor Admin.
   * 
   * @param nome nome do usu�rio admin
   */
  public Admin(String nome) {
    super(nome);
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public static boolean adminValido(String login, String senha) {
    return login.equals(loginPadrao) && senha.equals(senhaPadrao);
  }

  public static boolean ehAdmin(String nome, ArrayList<Admin> admins) {
    Iterator<Admin> iterator = admins.iterator();
    while (iterator.hasNext()) {
      Admin admin = iterator.next();
      if (admin.nome.equals(nome)) {
        return true;
      }
    }
    return false;
  }

}
