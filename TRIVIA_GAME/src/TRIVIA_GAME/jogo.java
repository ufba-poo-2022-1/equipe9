package TRIVIA_GAME;

import TRIVIA_GAME.exeptions.OpcaoInvalidaExeption;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class jogo {

	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<Opcao> opcoes = new ArrayList<Opcao>();

		opcoes.add(new Opcao("Cadastrar Pergunta", jogo::cadastrarPergunta));
		opcoes.add(new Opcao("Jogar", jogo::jogar));
		System.out.println("Digite o número de uma das opções a seguir:");

		for (int i = 0; i < opcoes.size(); i++) {
			System.out.println(i + 1 + " - " + opcoes.get(i).getLabel());
		}

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
			//Cadastro de um novo usuário
			Scanner read = new Scanner(System.in);
			Usuario admin = new Usuario();
			System.out.println("Qual seu nome?");
			admin.setNome(read.nextLine());
			System.out.println("Login: ");
			admin.setLogin(read.nextLine());
			System.out.println("Senha: ");
			admin.setSenha(read.nextLine());

			System.out.println("Seja bem-vindo ao TRIVIA " + admin.getNome());
		

			//Leitura das perguntas
			
			File perguntasFile = new File("./ufba_Oop_Project/TRIVIA_GAME/perguntas.txt");
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

			Scanner teclado = new Scanner(System.in);
			int pontosPlayerUm = 0;
			int pontosPlayerDois = 0;

			for (Pergunta questao : bancoQuestoes){
				questao.PrintPergunta();
				String resposta = teclado.next();

				System.out.println("\n==========");
				System.out.println("Player 1");
				
				if (resposta.equalsIgnoreCase(questao.getResposta())){
					pontosPlayerUm++;
					System.out.println("Certo! Você tem " + pontosPlayerUm + " pontos.\n");
				} else {
					System.out.println("Errooou! Você tem " + pontosPlayerUm + " pontos.\n");
				}
			}

			teclado.close();

			if(pontosPlayerUm > pontosPlayerDois) {
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
