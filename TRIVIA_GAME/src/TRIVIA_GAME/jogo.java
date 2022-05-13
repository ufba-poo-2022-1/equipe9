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
		System.out.println("Digite a pergunta: ");
		String p = teclado.nextLine();
		ArrayList<String> alternativas = new ArrayList<>();
		for (int i = 1; i <= 4; i++) {
			System.out.println("Digite a alternativa " + i + ":");
			alternativas.add(teclado.nextLine());
		}
		System.out.println();
		System.out.println("Qual das alternativas é a correta? Digite um número de 1 a " + alternativas.size());
		int correta = teclado.nextInt();
		if (correta < 1 || correta > alternativas.size())
			throw new OpcaoInvalidaExeption();
		Pergunta pergunta = new Pergunta(p, alternativas.get(0), alternativas.get(1), alternativas.get(2),
				alternativas.get(3), alternativas.get(correta - 1));
		teclado.nextLine();
		teclado.close();
		printPerguntaCadastrada(pergunta);

	}

	public static void printPerguntaCadastrada(Pergunta pergunta) {
		System.out.println();
		System.out.println("----------------------------------");
		System.out.println("A pergunta cadastrada foi:");
		System.out.println();
		printPergunta(pergunta);
		System.out.println();
		System.out.println("A Resposta correta é: " + pergunta.getRespostaCerta());

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
			//read.close();

			//Leitura das perguntas
		
			File perguntasFile = new File("./ufba_Oop_Project/TRIVIA_GAME/perguntas.txt");
			Scanner ler = new Scanner(perguntasFile);

			ArrayList<Pergunta> perguntas = new ArrayList<>();
			int pontosPlayerUm = 0;
			int pontosPlayerDois = 0;

			
			int numeroPerguntas = Integer.parseInt(ler.nextLine());
			for(int i = 0; i < numeroPerguntas; i++) {
				perguntas.add(new Pergunta(ler.nextLine(), ler.nextLine(),
				ler.nextLine(), ler.nextLine(), ler.nextLine(),
				ler.nextLine()));
			}
			
			ler.close();
			Scanner teclado = new Scanner(System.in);

			for(int i = 0; i < perguntas.size(); i ++) {

				if(i % 2 == 0) {
					System.out.println("Player 1, por favor responda a seguinte"
							+ " quest�o:\n");
					printPergunta(perguntas.get(i));
					String resposta = teclado.next();

					if(resposta.equalsIgnoreCase(perguntas.get(i).respostaCerta)) {
						pontosPlayerUm++;
						System.out.println("Certo! Voc� tem "
								+ pontosPlayerUm + " pontos.\n");
					} else {
						System.out.println("Errooou! Voc� tem "
								+ pontosPlayerUm + " pontos.\n");
					}
				} else{
					System.out.println("Player 2, por favor responda a seguinte"
							+ " quest�o:\n");
					printPergunta(perguntas.get(i));
					String resposta = teclado.next();
					if(resposta.equalsIgnoreCase(perguntas.get(i).respostaCerta)) {
						pontosPlayerDois++;
						System.out.println("Certo! Voc� tem "
								+ pontosPlayerDois + " pontos.\n");
					} else {
						System.out.println("Errooou! Voc� tem "
								+ pontosPlayerDois + " pontos.\n");
					}
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

	public static void printPergunta(Pergunta pergunta) {
		System.out.println(pergunta.pergunta);
		System.out.println(pergunta.respostaUm);
		System.out.println(pergunta.respostaDois);
		System.out.println(pergunta.respostaTres);
		System.out.println(pergunta.respostaQuatro);
	}
}
