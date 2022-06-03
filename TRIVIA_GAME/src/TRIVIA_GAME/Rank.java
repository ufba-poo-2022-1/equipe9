package trivia_bot;

import java.util.Comparator;
import java.util.ArrayList;

public class Rank implements Comparator<Jogador>{

    public int compare(Jogador o1, Jogador o2)
    {
 
        return (int) (o2.getPontuacao() - o1.getPontuacao());
    }
	
}
