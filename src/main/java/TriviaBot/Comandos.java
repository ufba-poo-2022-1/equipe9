package TriviaBot;

import java.util.ArrayList;
import java.util.List;

public class Comandos {

	List<String> listaDeComandos = new ArrayList<String>();
	
	
	//Ao iniciar o contrustor dessa classe ela carrega todos os comandos v�lidos na lista
	public Comandos() {
		listaDeComandos.add("!ping");
		listaDeComandos.add("!roll");
		listaDeComandos.add("!whoami");
		listaDeComandos.add("!start");
		listaDeComandos.add("!stop");
		listaDeComandos.add("!regras");
		listaDeComandos.add("!repete");
		listaDeComandos.add("!cadastrar");
		listaDeComandos.add("!login");
		listaDeComandos.add("!logout");
		listaDeComandos.add("!rank");
		listaDeComandos.add("!login-admin");
		listaDeComandos.add("!logout-admin");
		listaDeComandos.add("!reset-ranking");
		
	}
	
	public List<String> getListaDeComandos() {
		return listaDeComandos;
	}
	
	public boolean comandoValido (String comando) {
		if (listaDeComandos.contains(comando)) {
			return true;
		}
		else {
		return false;
		}
	}
	
	public int codigoDoComando(String comando) {

		if(comandoValido(comando)) {
			int indice = listaDeComandos.indexOf(comando);
			return indice;
		}
		else {
			return -1;
		}
	}
	
}
