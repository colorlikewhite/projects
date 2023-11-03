import java.util.*;

public class Player
{
	public int index;
	public String condition;
		
	public Player(int newindex, String newcondition)	
	{

		this.index = newindex;
		this.condition = newcondition;
	}
//---------------------------------------------------------------------------------------------------

	public static boolean ishitorstand(String hitorstand)
	{
		hitorstand.toLowerCase();
		return hitorstand.equals("hit");
	}

//---------------------------------------------------------------------------------------------------

	public static boolean isPush(int playershand, int dealinghand)
	{
		return (playershand == dealinghand);
	}
//---------------------------------------------------------------------------------------------------

	public static boolean isBlackJack(ArrayList<String> hand)
	{
		return ((Hand.giveHandvalue(hand) == 21) && hand.size() == 2);
	}
//---------------------------------------------------------------------------------------------------

	public static boolean isBusted(ArrayList<String> hand)
	{
		return Hand.giveHandvalue(hand) > 21;
	}
//---------------------------------------------------------------------------------------------------

	public static boolean isSplit(ArrayList<String> hand)
	{
		if(hand.size() == 2)
		{
			String[] stringlist = Card.findSubstrings(hand);			
			if(stringlist[0].equals(stringlist[1]))
			{
				return true;
			}
		}
		return false;
	}	
//---------------------------------------------------------------------------------------------------

	public static boolean isdoubleDown(String doubledown)
	{
		doubledown.toLowerCase();
		return doubledown.equals("yes");
	}
//---------------------------------------------------------------------------------------------------
	
	public static boolean isSurrender(String surrenders)
	{
		surrenders.toLowerCase();
		return surrenders.equals("yes");
	}

		
}