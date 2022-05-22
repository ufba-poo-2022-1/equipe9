package TRIVIA_GAME;

import TRIVIA_GAME.exeptions.OpcaoInvalidaExeption;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.ArrayList;
import java.util.Scanner;

public class jogo {

	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<Opcao> opcoes = new ArrayList<Opcao>();
	 /**
     * Exibe menu de opções para o usuário.
     * sendo 1 para cadastrar pergunta ou 2 para jogar
     */
		opcoes.add(new Opcao("Cadastrar Pergunta", jogo::cadastrarPergunta));
		opcoes.add(new Opcao("Jogar", jogo::jogar));
		System.out.println("Digite o número de uma das opções a seguir:");

		for (int i = 0; i < opcoes.size(); i++) {
			System.out.println(i + 1 + " - " + opcoes.get(i).getLabel());
		}
	/**
     * Faz leitura da opção digitada e verifica se é uma opão válida.
     * 
     */	
		Scanner teclado = new Scanner(System.in);
		int op = teclado.nextInt();
		if (op < 1 || op > opcoes.size())
			throw new OpcaoInvalidaExeption();

		System.out.println();
		opcoes.get(op - 1).getMetodo().run();
	}

	public static void cadastrarPergunta() {
		Scanner teclado = new Scanner(System.in);

		System.out.print("Questão: ");
		String p = teclado.nextLine();

		ArrayList<String> alternativas = new ArrayList<>();
		for (int i = 65; i < 65 + 4; i++) {
			//System.out.println("Digite a alternativa " + i + ":");
			System.out.print((char) i + ". ");
			alternativas.add(teclado.nextLine());
		} 

		System.out.println();
		System.out.print("Resposta: ");
		//System.out.println("Qual das alternativas é a correta? Digite um número de 1 a " + alternativas.size());
		String correta = teclado.nextLine();
		
		// ##############################################
		// [Pendente] Validar opção inválida (Exception) 

		// if (correta < 1 || correta > alternativas.size()){
		// 	teclado.close();
		// 	throw new OpcaoInvalidaExeption();
		// }

		Pergunta pergunta = new Pergunta(p, alternativas, correta);
		teclado.nextLine();
		teclado.close();
		printPerguntaCadastrada(pergunta);

	}
	/**
     * Exibe informações da pergunta cadastrada.
     * @param pergunta
     */

	public static void printPerguntaCadastrada(Pergunta pergunta) {
		System.out.println();
		System.out.println("----------------------------------");
		System.out.println("A pergunta cadastrada foi:");
		System.out.println();
		pergunta.PrintPergunta();
		System.out.println();
		System.out.println("A Resposta correta é a alternativa: " + pergunta.getResposta());

	}

	public static void jogar() {
		try {
			//*Cadastro de um novo usuário
			Scanner read = new Scanner(System.in);
			Admin admin = new Admin();
			Admin admin2 = new Admin();
			
			admin2.setLogin("Exemplo");
			
			System.out.println("Qual seu nome?");
			admin.setNome(read.nextLine());
			System.out.println("Login: ");
			admin.setLogin(read.nextLine());
			do {
				System.out.println("Usu�rio existente ou nulo! Escolha outro login:");
				admin.setLogin(read.nextLine());
			} while (admin.equalsAdmin(admin2) && !admin.notNullString(admin.getLogin()));
			System.out.println("Senha: ");
			admin.setSenha(read.nextLine());
			do {
				System.out.println("Senha nula! Digite uma senha v�lida:");
				admin.setSenha(read.nextLine());
			} while (!admin.notNullString(admin.getSenha()));

			System.out.println("Seja bem-vindo ao TRIVIA " + admin.getNome());
		

			//Leitura das perguntas
			
		String fileName = "perguntas.txt";
			
			File perguntasFile = new File(fileName);
			Scanner ler = new Scanner(perguntasFile);

			int numeroPerguntas = Integer.parseInt(ler.nextLine());
			ArrayList<Pergunta> bancoQuestoes = new ArrayList<>();
		
			for(int i = 0; i < numeroPerguntas; i++) {

				String perg = ler.nextLine();	
				ArrayList<String> alts = new ArrayList<String>();
				for (int c=0; c<4; c++)
					alts.add(ler.nextLine());
				String resp = ler.nextLine();	

				bancoQuestoes.add(new Pergunta(perg, alts, resp) );
			}

			ler.close();

			Collections.shuffle(bancoQuestoes); // Embaralha ordem de questões

			Scanner teclado = new Scanner(System.in);
			Jogador player1 = new Jogador();
			Jogador player2 = new Jogador();
			player1.setPontuacao(0);
			player2.setPontuacao(0);
			//int pontosPlayerUm = 0;
			//int pontosPlayerDois = 0;

			System.out.println("Insira o nome do jogador 1: ");
			player1.setNome(teclado.next());
			do {
				System.out.println("Nome nulo!");
				System.out.println("Insira o nome do jogador 1: ");
				player1.setNome(teclado.next());
			} while(!player1.notNullString(player1.getNome()));
			System.out.println("Insira o nome do jogador 2: ");
			player2.setNome(teclado.next());
			do {
				System.out.println("Nome repetido ou nulo!");
				System.out.println("Insira o nome do jogador 1: ");
				player2.setNome(teclado.next());
			} while (!player2.notNullString(player2.getNome()) && player2.equalsNamejogador(player1));
			

			for (Pergunta questao : bancoQuestoes){
				questao.PrintPergunta();
				String resposta = teclado.next();

				System.out.println("\n==========");
				System.out.println(player1.getNome());
				
				// if (resposta.equalsIgnoreCase(questao.getResposta())){
				// 	player1.adicionarPontuacao(player1.getPontuacao()+1);

				// 	System.out.println("Certo! " + player1.getNome() + " tem " + player1.getPontuacao() + " pontos.\n");
				// } else {
				// 	System.out.println("Errooou! " + player1.getNome() + " tem " +  player1.getPontuacao() + " pontos.\n");
				// }
			}

			teclado.close();

			if(player1.getPontuacao() > player2.getPontuacao()) {
				System.out.print("Player 1 wins!");
			} else {
				System.out.print("Player 2 wins!");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	// public static void printPergunta(Pergunta pergunta) {
	// 	System.out.println(pergunta.pergunta);
	// 	System.out.println(pergunta.respostaUm);
	// 	System.out.println(pergunta.respostaDois);
	// 	System.out.println(pergunta.respostaTres);
	// 	System.out.println(pergunta.respostaQuatro);
	// }
}
