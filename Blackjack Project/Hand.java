import java.util.*;

public class Hand
{
	public ArrayList<String> hand;
	public String name;
	public int balance;
	public int betamount;


	public Hand(String newname, int newbalance, int newbetamount)
	{
		this(newname, newbalance, newbetamount, new ArrayList<String>());
	}

	public Hand(String newname, int newbalance, int newbetamount, ArrayList<String> newhand)
	{
		this.name = newname;
		this.balance = newbalance;
		this.betamount = newbetamount;
		this.hand = newhand;
	}
 //-----------------------------------------------------------------------------------------------------

	public static int giveHandvalue(ArrayList<String> hand)
	{
		int handvalue = 0;
		int[] indexs = Card.findSpaces(hand);
		int acecounter = 0;
		String[] stringlist = Card.findSubstrings(hand);
		for(int i = 0; i < hand.size(); i++)
		{
			if(stringlist[i].equals("J") || stringlist[i].equals("Q") || stringlist[i].equals("K"))
			{
				handvalue += 10;

			}else if(stringlist[i].equals("A")){

				acecounter++;
				handvalue += 11;
				
			}else
				handvalue += Integer.parseInt(stringlist[i]);
		}
		while(handvalue > 21)
		{
			if(acecounter != 0)
			{
				handvalue -= 10;
				acecounter--;

			}else{
				break;
			}


		}			
	
		return handvalue;
	}
 //-----------------------------------------------------------------------------------------------------
   	
	public static ArrayList<String> addHand(ArrayList<String> shuffleddeck, ArrayList<String> hand)
	{
		if(hand.size() == 0)
		{
			for(int i = 0; i < 2; i++)
			{
				hand.add(shuffleddeck.get(i));
				shuffleddeck.remove(i);
			}
		}else if(shuffleddeck.size() > 0)
		{
			hand.add(shuffleddeck.get(0));
			shuffleddeck.remove(0);
		}	
		return hand;
	}
}