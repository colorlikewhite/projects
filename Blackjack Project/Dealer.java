import java.util.*;

public class Dealer
{
	public static ArrayList<String> dealerPlaying(ArrayList<String> hand, ArrayList<String> shuffleddeck)
	{
		while(Hand.giveHandvalue(hand) < 21)
		{
			if(Hand.giveHandvalue(hand) <= 16)
			{
				Hand.addHand(shuffleddeck,hand);

			}else if (Hand.giveHandvalue(hand) > 16) 
			{
				break;
			}
		}
		return hand;
	}
}