import java.util.*;

public class Card
{
	public static String[] findSubstrings(ArrayList<String> hand)
	{
		int[] indexs = findSpaces(hand);
		String[] stringlist = new String[hand.size()];
		String strvalue;
		for(int i = 0; i < hand.size(); i++)
		{
			strvalue = hand.get(i);
			strvalue = strvalue.substring(0,indexs[i]);
			stringlist[i] = strvalue;
		}
		return stringlist;
	}
//-------------------------------------------------------------------------------------------------------

	public static String readfirstCard(ArrayList<String> hand)
	{
		String[] stringlist = findSubstrings(hand);
		return stringlist[0];

	}

//-------------------------------------------------------------------------------------------------------

	public static int[] findSpaces(ArrayList<String> hand)
	{
		int[] indexs = new int[hand.size()];
		for(int i = 0; i < hand.size(); i++)
		{
			String strvalue = hand.get(i);
			for(int j = 0; j < strvalue.length(); j++)
			{

				if(strvalue.charAt(j) == ' ')
				{
					indexs[i] = j;
					break;
				}
			}
		}
		return indexs;		
	}		

}