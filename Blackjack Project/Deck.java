import java.util.*;

public class Deck
{
	public static ArrayList<String> deckofCards(ArrayList<String> deck)
	{
		int startcard = 2;
		int othercards = 0;
		String[] jkqa = {"J", "Q", "K", "A"};
		for(int k = 0; k < 5; k++)
		{
			for(int i = 0; i < 13; i++)
			{

				for (int j = 0; j < 4 ; j++)
				{

					if(i <= 8)
					{
						switch(j){
							case 0: deck.add(startcard + " of diamond");
									break;
							case 1: deck.add(startcard + " of club");
									break;
							case 2: deck.add(startcard + " of heart");
									break;
							case 3: deck.add(startcard + " of spade");
									break;				
							}
					}				
					if(i >= 9)
					{
						switch(j){
							case 0: deck.add(jkqa[othercards] + " of diamond");
									break;
							case 1: deck.add(jkqa[othercards] + " of club");
									break;
							case 2: deck.add(jkqa[othercards] + " of heart");
									break;
							case 3: deck.add(jkqa[othercards] + " of spade");
									break;				
							}
					}
				}
				if(i < 8)
					startcard++;

				if(i > 8 && i < 12)
					othercards++;
			}startcard = 2; othercards = 0;
		}
		return deck;
	}
//-----------------------------------------------------------------------------------------------------
	public static ArrayList<String> ShuffleDeck(ArrayList<String> deck, ArrayList<String> shuffleddeck)
	{
		for(int i = 0; i < deck.size(); i++)
		{
			shuffleddeck.add(deck.get(i));
		}
		Collections.shuffle(shuffleddeck);
		return shuffleddeck;
	}	
}