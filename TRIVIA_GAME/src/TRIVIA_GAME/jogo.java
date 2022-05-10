package TRIVIA_GAME;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class jogo {

	 public static void main(String[] args) throws FileNotFoundException {
		       Scanner ler = new Scanner(new File("perguntas.txt"));
		        ArrayList<Pergunta> perguntas = new ArrayList<>();
		        int pontosPlayerUm = 0;
		        int pontosPlayerDois = 0;

		        Scanner teclado = new Scanner(System.in);
		        
		        
		        int numeroPerguntas = Integer.parseInt(ler.nextLine());
		        for(int i = 0; i < numeroPerguntas; i++) {
		           perguntas.add(new Pergunta(ler.nextLine(), ler.nextLine(),
		                   ler.nextLine(), ler.nextLine(), ler.nextLine(),
		                   ler.nextLine())); 
		        }
		        
		        
		        for(int i = 0; i < perguntas.size(); i ++) {
		            
		            if(i % 2 == 0) {
		                System.out.println("Player 1, por favor responda a seguinte"
		                        + " questão:\n");
		                printPergunta(perguntas.get(i));
		                String resposta = teclado.next();
		                if(resposta.equalsIgnoreCase(perguntas.get(i).respostaCerta)) {
		                    pontosPlayerUm++;
		                    System.out.println("Certo! Você tem "
		                            + pontosPlayerUm + " pontos.\n");
		                } else {
		                    System.out.println("Errooou! Você tem "
		                            + pontosPlayerUm + " pontos.\n");
		                }
		            } else{ 
		                System.out.println("Player 2, por favor responda a seguinte"
		                        + " questão:\n");
		                printPergunta(perguntas.get(i));
		                String resposta = teclado.next();
		                if(resposta.equalsIgnoreCase(perguntas.get(i).respostaCerta)) {
		                    pontosPlayerDois++;
		                    System.out.println("Certo! Você tem "
		                            + pontosPlayerDois + " pontos.\n");
		                } else {
		                    System.out.println("Errooou! Você tem "
		                            + pontosPlayerDois + " pontos.\n");
		                }
		            }
		        }
		        
		        
		        if(pontosPlayerUm > pontosPlayerDois) {
		            System.out.print("Player 1 wins!");
		        } else {
		            System.out.print("Player 2 wins!");
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

