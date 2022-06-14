package trivia_bot;

import java.util.Comparator;
import java.util.ArrayList;

/**
 * compara jogadores por pontuação. 
 * @param o1 primeiro jogador
 * @param o2 segundo jogador
 * @return  ordem que o jogador ocupa no rank 
 */
public class Rank implements Comparator<Jogador>{

    public int compare(Jogador o1, Jogador o2)
    {
 
        return (int) (o2.getPontuacao() - o1.getPontuacao());
    }
	
}
