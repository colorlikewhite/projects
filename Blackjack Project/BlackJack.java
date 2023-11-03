import java.util.*;

public class BlackJack
{
	public static void main(String[] args)
	{
		ArrayList<String> deck = new ArrayList<String>();
		ArrayList<String> shuffleddeck = new ArrayList<String>();
		ArrayList<String> nameofplayerslist = new ArrayList<String>();

		ArrayList<Player> conditionindexlist = new ArrayList<Player>();

		ArrayList<Hand> playershands = new ArrayList<Hand>();
		ArrayList<Hand> playershandssplit = new ArrayList<Hand>();

		int balance = 2500;
		int betamount = 0;
		int endgame = 0;


		int insbetamount = 0;
		int splbetamount = 0;
		int doubledownenter = 0;
		int notbusted = 0;
		int gotblackjack = 0;
		int tempbetamount = 0;
		int blackjackers = 0;
		int pushers = 0;
		int losers = 0;
		int surrenderers = 0;
		int winners = 0;
		int demobetamount = 0;
		int noamount = 0;	

		boolean split = false;
		boolean insurance = false;
		boolean surrender = false;
		boolean doubledown = false;

		boolean[] surrddown = new boolean[2];

		String didwin = "won";
		String didpush = "push";
		String didlost = "lost";
		String didunable = "unable";
		String surrenderhand = "surrender";
		String insurehand = "insurance";
		String blackjackhand = "blackjack";
		String splithand = "split";
		String yesornoins = "";
		String yesornospl = "";
		String yesornobal = "";
		String yesornosplbal = "";
		String yesornospldown = "";
		String surrenders = "";
		String doubledowns = "";
		String hitorstand = "";
		String checkinsurance = "";	
		String playgame = "";
		String currentplayerhand;		

		Hand dealinghand = new Hand("Dealer", 0, 0);
		Hand demohand = new Hand("Demo", balance, betamount);

		Deck.deckofCards(deck);
		Deck.ShuffleDeck(deck,shuffleddeck);


	//Introduction
//---------------------------------------------------------------------------------------------------		
			System.out.println("Hello! Welcome to BlackJack!");
			System.out.println("Please answer any questions that are prompted to you.");
			System.out.println("In order to advance text please remember to always press the enter key.");

		IO.readString(); //x

			System.out.println("Some quick information about this Blackjack table.");

			IO.readString(); //x

			System.out.println("You will place your bets before being dealt your hand.");

			IO.readString(); //x

			System.out.println("You start off with $2500 in your balance.");

			IO.readString(); //x							

			System.out.println("The minimum number of players is 1 and the maximum number of players is 7.");

			IO.readString(); //x

			System.out.println("You can only bet in increments of $50.");

			IO.readString(); //x

			System.out.println("Minimum bet amount is $200.");

			IO.readString(); //x

			System.out.println("However, if it is the case you do not have $200 in your balance we will accept the remainder of your balance.");

			IO.readString(); //x

			System.out.println("If you want to see a quick demo on how to play the game type \"0\" for the number of players.");
			
			IO.readString(); //x

			System.out.println("How many players are they?");
				

		int numberofplayers = IO.readInt();
		numberofplayers = makenumberofplayersProper(numberofplayers);

		if(numberofplayers == 0) {

			do{

					System.out.println("Okay, we will now commence the demo.");

				IO.readString(); //x

				numberofplayers = doDemo(demohand, dealinghand, shuffleddeck);

			}while(numberofplayers == 0);
		}

		String[] conditionlist = new String[numberofplayers];

		conditionlist = makeconditionList(numberofplayers, conditionlist);

		int indexofname = -1;

		for(int i = 0; i < numberofplayers; i++)
		{
				System.out.println("Okay! Tell me the name of player " + (i+1) + ".");

			String nameofplayers = IO.readString();

			if(nameofplayers.equals("") == true)
			{

				nameofplayers = makenameofplayersProper(nameofplayers, i);

			}

			if(i > 0)
			{
				indexofname = checkrepeatName(nameofplayers,nameofplayerslist);

				if(indexofname > -1)
				{
					nameofplayers = makesamenameProper(nameofplayers, nameofplayerslist, indexofname, i);
				}
			}

			nameofplayerslist.add(nameofplayers);			
		}

		makePlayers(playershands, nameofplayerslist, balance, betamount);
		makePlayers(playershandssplit, nameofplayerslist, balance, betamount);

		if(numberofplayers == 0) {


		}else {

			while(endgame != numberofplayers)
			{

					for(int i = 0; i < numberofplayers; i++)
					{
						if(playershands.get(i).balance != 0)
						{

								System.out.println(playershands.get(i).name + "\'s balance is $" + playershands.get(i).balance + ". How much money would you like to bet?");
								System.out.print("$");
						

							betamount = IO.readInt();
							betamount = giverightAmount(betamount,playershands.get(i).balance);
							playershands.get(i).betamount = betamount;
							playershands.get(i).balance -= betamount;

						}else {

							noamount++;
						}

					}



					if(noamount != numberofplayers)
					{
						Hand.addHand(shuffleddeck, dealinghand.hand);
					}

				for(int i = 0; i < numberofplayers; i++)
				{


				if(playershands.get(i).balance == 0)
				{
					if(playershands.get(i).betamount == 0)
					{
						conditionlist[i] = didunable;

						System.out.println("Looks like player: " + playershands.get(i).name + " has no more money in there balance.");

						IO.readString(); //x

						if(numberofplayers > 1)
						{
							System.out.println("We will continue with other players.");
						}

						IO.readString(); //x

						if(conditionlist[i].equals(didunable) == true)
						{
							endgame++;
						}
					}
							
				}

				if(conditionlist[i].equals("unable") == false)
				{
					balance = playershands.get(i).balance;
					betamount = playershands.get(i).betamount;

					isEmpty(shuffleddeck.size(), deck, shuffleddeck);
					Hand.addHand(shuffleddeck, (playershands.get(i)).hand);


						System.out.println(playershands.get(i).name + "\'s cards are: " + playershands.get(i).hand + "\n" + playershands.get(i).name + 
												"\'s card value is " + Hand.giveHandvalue(playershands.get(i).hand));
						IO.readString(); //x

						if(gotblackjack == 0)
						{
							System.out.println("The Dealer\'s card is: " + dealinghand.hand.get(0));

							IO.readString(); //x	
						}

			//Insurance
		//---------------------------------------------------------------------------------------------------
					if((Card.readfirstCard(dealinghand.hand)).equals("A") == true && (Player.isBlackJack(playershands.get(i).hand) == false) && gotblackjack == 0)
					{
							System.out.println("Looks like the Dealer has an A showing. Would you like to get insurance? Please type yes/no.");

						yesornoins = IO.readString();
						yesornoins = makeyesnoProper(yesornoins);

						if(yesornoins.equals("yes") == true)
						{

							insurance = true;
							checkinsurance = doInsurance(playershands.get(i).balance, betamount);


							if(checkinsurance.equals("insurance") == true)
							{

								tempbetamount = betamount;

								insbetamount = betamount/2;
								balance -= insbetamount;
								betamount += insbetamount;

								playershands.get(i).balance = balance;



								if(Player.isBlackJack(dealinghand.hand) == false)
								{
									betamount -= insbetamount;

								}else{

									betamount -= tempbetamount;
								}


								

							}else if(checkinsurance.equals("balanceinsurance") == true){

								tempbetamount = betamount;

								insbetamount += balance;
								betamount += insbetamount;
								balance -= balance;

								playershands.get(i).balance = balance;

								if(Player.isBlackJack(dealinghand.hand) == false)
								{
									betamount -= insbetamount;

								}else{

									betamount -= tempbetamount;
								}
							}


						}else{
								System.out.println("Okay! Let's move on!");

								IO.readString(); //x

						}
					}


			//BlackJack
		//---------------------------------------------------------------------------------------------------

					if(Player.isBlackJack(playershands.get(i).hand) == true)
					{
							conditionlist[i] = blackjackhand;

							System.out.println("Looks like player " + playershands.get(i).name + " got blackjack."); 

							IO.readString(); //x


							if(gotblackjack == 0 && (i != numberofplayers-1))
							{
								System.out.println("We will now check if any of the other players got blackjack.");

								IO.readString(); //x
							}						

							gotblackjack++;
							

					}

					

			//Split
		//---------------------------------------------------------------------------------------------------

					if(Player.isSplit(playershands.get(i).hand) == true && gotblackjack == 0)
					{
							System.out.println("Looks like you have two cards of the same value.");

							IO.readString();

							System.out.println("Would you like to split them into two hands? Please type yes/no.");

						yesornospl = IO.readString();
						yesornospl = makeyesnoProper(yesornospl);	

						if(yesornospl.equals("yes") == true)
						{
							if(balance == 0 || balance-betamount < 0)
							{
								split = false;

								System.out.println("Bummer! You have no more cash in your balance to be able to split your hand.");

								IO.readString(); //x


							}else{

								split = true;
								conditionlist[i] = splithand;
								doubledownenter = doSplit(balance, betamount, playershands.get(i), playershandssplit.get(i), shuffleddeck);

								do {

									balance -= betamount;
									betamount = 2*betamount;
									doubledownenter--;

								}while(doubledownenter >= 0);
				
							}
								

						}else if(yesornospl.equals("no") == true){

								split = false;
								System.out.println("Okay we will keep your hand as is.");

								
							}
							
					}

			//Regular Play
		//---------------------------------------------------------------------------------------------------

					if(split == false && gotblackjack == 0)
					{

						surrddown = doregularPlay(insurance, split, playershands.get(i).balance, betamount, playershands.get(i), playershandssplit.get(i), shuffleddeck);

						for(int j = 0; j < surrddown.length; j++)
						{
							switch(j){

								case 0: surrender = surrddown[j];
										break;
								case 1: doubledown = surrddown[j];
										break;
								
							}
						}


						if(surrender == true)
						{
							betamount = betamount/2;
							balance += betamount;
							conditionlist[i] = surrenderhand;


						}else if(doubledown == true){

							balance -= betamount;
							betamount = 2*betamount;

						}
					
							

							playershands.get(i).betamount = betamount;
						}
			}
							if(Player.isBusted(playershands.get(i).hand) == false || (Player.isBusted(playershandssplit.get(i).hand) == false
								 && playershandssplit.get(i).hand.size() != 0))
							{
								notbusted++;
							}

							if(notbusted > 0 && (i == numberofplayers - 1) && gotblackjack == 0 && conditionlist[i].equals(surrenderhand) == false)
							{
								Dealer.dealerPlaying(dealinghand.hand, shuffleddeck);		
							}

							if(i == numberofplayers-1 && gotblackjack == 0 && noamount == 0)
							{
								System.out.println("Dealer's cards are: " + dealinghand.hand + "\n" +
												"Dealer's card value is " + Hand.giveHandvalue(dealinghand.hand));	

								IO.readString(); //x	
							}

			insurance = false;
			split = false;
			surrender = false;
			doubledown = false;
			playershands.get(i).betamount = betamount;
			playershands.get(i).balance = balance;			
		}

				if(endgame == numberofplayers)
				{
					System.out.println("Looks like there is no more money left in all the players balance.");

					break;
				}

			//Regular play finishing the game
		//---------------------------------------------------------------------------------------------------
			if(gotblackjack > 0)
			{
					System.out.println("Dealer's cards are: " + dealinghand.hand + "\n" +
														"Dealer's card value is " + Hand.giveHandvalue(dealinghand.hand));	
					IO.readString(); //x

				for(int i = 0; i < numberofplayers; i++)
				{
					if(conditionlist[i].equals("blackjack") == true && Player.isBlackJack(dealinghand.hand) == false)
					{
						betamount = playershands.get(i).betamount/2;
						betamount = 3*betamount;
						playershands.get(i).balance += betamount;
						playershands.get(i).betamount = betamount;

					}else if(conditionlist[i].equals("blackjack") == true && Player.isBlackJack(dealinghand.hand) == true){

						playershands.get(i).balance += betamount;
						conditionlist[i] = didpush;

					}
				}

			}



			if(gotblackjack == 0)
			{

				for(int i = 0; i < numberofplayers; i++)
				{

						if(conditionlist[i].equals(surrenderhand) == false)
						{

							if(conditionlist[i].equals(splithand) == false)
							{

								if(whoWins(Hand.giveHandvalue(playershands.get(i).hand),Hand.giveHandvalue(dealinghand.hand)) == true && 
									Player.isBusted(playershands.get(i).hand) == false)
								{
									playershands.get(i).balance += 2*playershands.get(i).betamount;
									conditionlist[i] = didwin;

								}else if(Player.isPush(Hand.giveHandvalue(playershands.get(i).hand),Hand.giveHandvalue(dealinghand.hand)) == true &&
										Player.isBusted(playershands.get(i).hand) == false){

										playershands.get(i).balance += playershands.get(i).betamount;
										conditionlist[i] = didpush;

								}else if(whoWins(Hand.giveHandvalue(playershands.get(i).hand),Hand.giveHandvalue(dealinghand.hand)) == false ||
											Player.isBusted(playershands.get(i).hand) == true){

										conditionlist[i] = didlost;
								}
								

							}else if(conditionlist[i].equals(splithand) == true){

									if((whoWins(Hand.giveHandvalue(playershands.get(i).hand),Hand.giveHandvalue(dealinghand.hand)) == true && 
										whoWins(Hand.giveHandvalue(playershandssplit.get(i).hand),Hand.giveHandvalue(dealinghand.hand)) == true) &&
										(Player.isBusted(playershands.get(i).hand) == false && Player.isBusted(playershandssplit.get(i).hand) == false))
									{

										playershands.get(i).balance += 2*playershands.get(i).betamount;
										conditionlist[i] = didwin;

									}else if((whoWins(Hand.giveHandvalue(playershands.get(i).hand),Hand.giveHandvalue(dealinghand.hand)) == true || 
										whoWins(Hand.giveHandvalue(playershandssplit.get(i).hand),Hand.giveHandvalue(dealinghand.hand)) == true) &&
											(Player.isBusted(playershands.get(i).hand) == false || Player.isBusted(playershandssplit.get(i).hand) == false))
									{
										if(Player.isPush(Hand.giveHandvalue(playershands.get(i).hand), Hand.giveHandvalue(dealinghand.hand)) == true ||
											 Player.isPush(Hand.giveHandvalue(playershandssplit.get(i).hand),Hand.giveHandvalue(dealinghand.hand)) == true)
										{

											playershands.get(i).balance += ((2*playershands.get(i).betamount) + playershands.get(i).betamount);
											conditionlist[i] = didwin;										



										}else{

											playershands.get(i).balance += playershands.get(i).betamount;
											conditionlist[i] = didwin;

										}


									}else if(Player.isPush(Hand.giveHandvalue(playershands.get(i).hand), Hand.giveHandvalue(dealinghand.hand)) == true &&
											 Player.isPush(Hand.giveHandvalue(playershandssplit.get(i).hand),Hand.giveHandvalue(dealinghand.hand)) == true){

											playershands.get(i).balance += playershands.get(i).betamount;
											conditionlist[i] = didpush;

									}else if((whoWins(Hand.giveHandvalue(playershands.get(i).hand),Hand.giveHandvalue(dealinghand.hand)) == false &&
										whoWins(Hand.giveHandvalue(playershandssplit.get(i).hand),Hand.giveHandvalue(dealinghand.hand)) == false) ||
											(Player.isBusted(playershands.get(i).hand) == true && Player.isBusted(playershandssplit.get(i).hand) == true)){

											conditionlist[i] = didlost;

									}
							}				
						}
					}	
					
				}
						for(int i = 0; i < numberofplayers; i++)
						{
							Player indexcondition = new Player(i, conditionlist[i]);
							conditionindexlist.add(indexcondition);
						}

						conditionindexlist.sort(new ConditionComparator());																					

							for(int i = 0; i < numberofplayers; i++)
							{
								if(gotblackjack > 0) {

									if(conditionindexlist.get(i).condition.equals(blackjackhand) == true)
									{
										if(blackjackers == 0)
										{
											System.out.println("Blackjack winners:");

											IO.readString(); //x

										}

										System.out.println(playershands.get(conditionindexlist.get(i).index).name + " won $" 
																+ playershands.get(conditionindexlist.get(i).index).betamount + ".");

										System.out.print(playershands.get(conditionindexlist.get(i).index).name + "\'s cards were: " + playershands.get(conditionindexlist.get(i).index).hand 
										+ ", " + playershands.get(conditionindexlist.get(i).index).name + "\'s card value was " + Hand.giveHandvalue(playershands.get(conditionindexlist.get(i).index).hand) + ".\n");


										System.out.println();

										blackjackers++;

									}							
								}else {
								
								if(conditionindexlist.get(i).condition.equals(didlost) == true) {

									if(losers == 0)
									{
										System.out.println("Losers:");

										IO.readString(); //x
									}

									System.out.println(playershands.get(conditionindexlist.get(i).index).name + " lost $" 
														+ playershands.get(conditionindexlist.get(i).index).betamount + ".");

									System.out.print(playershands.get(conditionindexlist.get(i).index).name + "\'s cards were: " + playershands.get(conditionindexlist.get(i).index).hand 
										+ ", " + playershands.get(conditionindexlist.get(i).index).name + "\'s card value was " + Hand.giveHandvalue(playershands.get(conditionindexlist.get(i).index).hand) + ".\n");

									System.out.println();

									if(playershandssplit.get(conditionindexlist.get(i).index).hand.size() > 0) {

										System.out.print(playershandssplit.get(conditionindexlist.get(i).index).name + "\'s split cards were: " + playershandssplit.get(conditionindexlist.get(i).index).hand 
										+ ", " + playershandssplit.get(conditionindexlist.get(i).index).name + "\'s split card value was " + Hand.giveHandvalue(playershandssplit.get(conditionindexlist.get(i).index).hand) + ".\n");
									}

									System.out.println();

									losers++;
									

								}else if(conditionindexlist.get(i).condition.equals(didpush) == true) {

									if(pushers == 0)
									{
										IO.readString(); //x

										System.out.println("People who pushed:");

										IO.readString(); //x
									}

									System.out.println(playershands.get(conditionindexlist.get(i).index).name + " got their bet back $" 
														+ playershands.get(conditionindexlist.get(i).index).betamount + ".");

									System.out.print(playershands.get(conditionindexlist.get(i).index).name + "\'s cards were: " + playershands.get(conditionindexlist.get(i).index).hand 
										+ ", " + playershands.get(conditionindexlist.get(i).index).name + "\'s card value was " + Hand.giveHandvalue(playershands.get(conditionindexlist.get(i).index).hand) + ".\n");

									System.out.println();

									if(playershandssplit.get(conditionindexlist.get(i).index).hand.size() > 0) {

										System.out.print(playershandssplit.get(conditionindexlist.get(i).index).name + "\'s split cards were: " + playershandssplit.get(conditionindexlist.get(i).index).hand 
										+ ", " + playershandssplit.get(conditionindexlist.get(i).index).name + "\'s split card value was " + Hand.giveHandvalue(playershandssplit.get(conditionindexlist.get(i).index).hand) + ".\n");
									}

									System.out.println();

									pushers++;
									

								}else if(conditionindexlist.get(i).condition.equals(surrenderhand) == true) { 

									if(surrenderers == 0)
									{
										IO.readString(); //x

										System.out.println("People who surrendered:");

										IO.readString(); //x
									}

									System.out.println(playershands.get(conditionindexlist.get(i).index).name + " lost $" 
														+ playershands.get(conditionindexlist.get(i).index).betamount + ".");

									System.out.print(playershands.get(conditionindexlist.get(i).index).name + "\'s cards were: " + playershands.get(conditionindexlist.get(i).index).hand 
										+ ", " + playershands.get(conditionindexlist.get(i).index).name + "\'s  card value was " + Hand.giveHandvalue(playershands.get(conditionindexlist.get(i).index).hand) + ".\n");
									
									System.out.println();


									surrenderers++;


								}else if(conditionindexlist.get(i).condition.equals(didwin) == true) {

									if(winners == 0)
									{
										IO.readString(); //x

										System.out.println("Winners:");

										IO.readString(); //x
									}

									System.out.println(playershands.get(conditionindexlist.get(i).index).name + " won $" 
														+ playershands.get(conditionindexlist.get(i).index).betamount + ".");

									System.out.print(playershands.get(conditionindexlist.get(i).index).name + "\'s cards were: " + playershands.get(conditionindexlist.get(i).index).hand 
										+ ", " + playershands.get(conditionindexlist.get(i).index).name + "\'s card value was " + Hand.giveHandvalue(playershands.get(conditionindexlist.get(i).index).hand) + ".\n");

									System.out.println();

									if(playershandssplit.get(conditionindexlist.get(i).index).hand.size() > 0) {

										System.out.print(playershandssplit.get(conditionindexlist.get(i).index).name + "\'s split cards were: " + playershandssplit.get(conditionindexlist.get(i).index).hand 
										+ ", " + playershandssplit.get(conditionindexlist.get(i).index).name + "\'s split card value was " + Hand.giveHandvalue(playershandssplit.get(conditionindexlist.get(i).index).hand) + ".\n");
									}

									System.out.println();

									winners++;
								

								}
							}

						}

			for(int i = 0; i < numberofplayers; i++) {

				removeCards(playershands.get(i).hand);

				if((playershandssplit.get(i).hand).size() > 0)
					removeCards(playershandssplit.get(i).hand);

				playershands.get(i).betamount = 0;


			}

			IO.readString(); //x

			removeCards(dealinghand.hand);
			removeconditionindexList(conditionindexlist);

			conditionlist = makeconditionList(numberofplayers, conditionlist);

						notbusted = 0;
						gotblackjack = 0;
						endgame = 0;
						blackjackers = 0;
						pushers = 0;
						losers = 0;
						surrenderers = 0;
						winners = 0;
						noamount = 0;

			}
		}System.out.println("Thanks for playing BlackJack!");
		System.out.println();
	}
//---------------------------------------------------------------------------------------------------

	public static ArrayList<Player> removeconditionindexList(ArrayList<Player> conditionindexlist)
	{
		for(int i = conditionindexlist.size()-1; i >= 0; i--)
		{
			conditionindexlist.remove(i);
		}
		return conditionindexlist;
	}
//--------------------------------------------------------------------------------------------------

	public static String[] makeconditionList(int numberofplayers, String[] conditionlist)
	{

		for(int i = 0; i < numberofplayers; i++) 
		{

			conditionlist[i] = "";
			
		}

		return conditionlist;
	}


//--------------------------------------------------------------------------------------------------
	
	public static int giverightAmount(int betamount, int balance)
	{
		while(betamount > balance || betamount%50 != 0 || betamount < 200)
		{

			if(betamount > balance && balance == 0)
			{

				System.out.println("Sorry, you are betting more than you have in your balance. Please bet a proper amount");

				IO.readString(); //x

				System.out.println("You have $" + balance + " left in your balance.");
					System.out.print("$");												

					betamount = IO.readInt();
				
			}else if(betamount < 200){

				System.out.println("Sorry, the minimum bet amount is $200. Please bet a proper amount.");

				IO.readString(); //x

				System.out.println("You have $" + balance + " left in your balance.");
					System.out.print("$");

				betamount = IO.readInt();
			}else if(betamount%50 != 0){

					System.out.println("Sorry, you can only bet in increments of $50. Please bet a proper amount.");

					IO.readString(); //x

					System.out.println("You have $" + balance + " left in your balance.");
						System.out.print("$");

				betamount = IO.readInt();

			}

			if(betamount%50 == 0 && betamount > 200 && betamount < balance)
			{
				break;

			}else if(balance < 200){

				betamount = balance;
				break;
			}

		}

		return betamount;	
	}
//---------------------------------------------------------------------------------------------------

	public static ArrayList<Hand> makePlayers(ArrayList<Hand> playershands, ArrayList<String> nameofplayerslist, int balance, int betamount)
	{

		String currentplayerhand;

		for(int i = 0; i < nameofplayerslist.size(); i++)
		{
			currentplayerhand = nameofplayerslist.get(i);

			Hand players = new Hand(currentplayerhand, balance, betamount);

			playershands.add(players);
		}

		return playershands;
	}

//---------------------------------------------------------------------------------------------------

	public static String makenameofplayersProper(String nameofplayers, int currentname)
	{

		String propernamesofplayers;

		while(nameofplayers.equals("") == true)
		{
			System.out.println("Looks like you entered nothing for player " + (currentname+1) + ".");

				IO.readString(); //x

				System.out.println("Was this intended? Please type yes/no");
				

			propernamesofplayers = IO.readString();
			propernamesofplayers = makeyesnoProper(propernamesofplayers);

			if(propernamesofplayers.equals("no") == true)
			{
				System.out.println("Okay! Please retype player " + (currentname+1) + "\'s name now.");

				nameofplayers = IO.readString();

			}else{

				break;
			}

		}

		return nameofplayers;
	}

//--------------------------------------------------------------------------------------------------

	public static int checkrepeatName(String nameofplayers, ArrayList<String> nameofplayerslist)
	{
		int indexofname = -1;

		for(int j = 0; j < nameofplayerslist.size(); j++)
		{
			if(nameofplayerslist.get(j).equals(nameofplayers) == true)
			{
				indexofname = j;
				break;
			}
		}

		return indexofname;
	}
//--------------------------------------------------------------------------------------------------

	public static String makesamenameProper(String nameofplayers, ArrayList<String> nameofplayerslist, int pastname, int currentname)
	{
		String propernamesofplayers;

			while(nameofplayerslist.get(pastname).equals(nameofplayers) == true)
			{
				System.out.println("Looks like you entered the same name for player " + (pastname+1) + " and player " + (currentname+1));

				IO.readString(); //x

				System.out.println("Was this intended? Please type yes/no");

				propernamesofplayers = IO.readString();
				propernamesofplayers = makeyesnoProper(propernamesofplayers);

				if(propernamesofplayers.equals("yes") == true)
				{
					System.out.println("Okay! We will move on.");

					IO.readString(); //x

					break;

				}else{

						System.out.println("Okay, what is the new name of player " + (currentname+1) + "?");

					nameofplayers = IO.readString();

					if(nameofplayers.equals("") == true)
					{
						nameofplayers = makenameofplayersProper(nameofplayers, currentname);
					} 
				}
			}		
		
		return nameofplayers;
	}

//---------------------------------------------------------------------------------------------------

	public static int makenumberofplayersProper(int numberofplayers) {

		if(numberofplayers > 7 || numberofplayers < 0)
		{
			while(numberofplayers > 7 || numberofplayers < 0)
			{

				if(numberofplayers > 7)
				{
					System.out.println("Maximum number of players is 7.");

					IO.readString(); //x

					System.out.println("Please, put a proper number of players now.");

					numberofplayers = IO.readInt();
				}else{

				System.out.println("Minimum number of players is 1. If you want to see a demo of how to play the game type \"0\"");

				IO.readString(); //x

				System.out.println("Please, put a proper number of players now.");

				numberofplayers = IO.readInt();

				}
				
			}
		}

		return numberofplayers;

	}
	

//---------------------------------------------------------------------------------------------------
	
	public static String makeyesnoProper(String string)
	{
		string = string.toLowerCase();
		while(!(string.equals("yes") || string.equals("no")))
		{
				System.out.println("Sorry, please enter yes or no.");
			string = IO.readString();
			string = string.toLowerCase();
		}
		return string;				
	}
//---------------------------------------------------------------------------------------------------

	public static String makehitstandProper(String string)
	{
		string = string.toLowerCase();
		while(!(string.equals("hit") || string.equals("stand")))
		{
				System.out.println("Sorry, please enter hit or stand.");
			string = IO.readString();
			string = string.toLowerCase();
		}
		return string;
	}
//---------------------------------------------------------------------------------------------------
	
	public static boolean whoWins(int playerhand, int dealerhand)
	{
		if(playerhand > dealerhand && playerhand <= 21 && dealerhand < 21)
			return true;
		else if(dealerhand > playerhand && playerhand < 21 && dealerhand <= 21)
			return false;
		else 
			return (dealerhand > playerhand && playerhand <= 21 && dealerhand > 21);


	}
//---------------------------------------------------------------------------------------------------

	public static ArrayList<String> removeCards(ArrayList<String> hand)
	{
		for(int i = hand.size()-1; i >= 0; i--)
		{
			hand.remove(i);
		}
		return hand;
	}
//---------------------------------------------------------------------------------------------------

	public static ArrayList<String> isEmpty(int decksize, ArrayList<String> deck, ArrayList<String> shuffleddeck)
	{
		Random rnd = new Random();

		int deckshuffle = rnd.nextInt(16)+60;

		if(decksize <= deckshuffle)
			return Deck.ShuffleDeck(deck,shuffleddeck);

		return shuffleddeck;

	}

//---------------------------------------------------------------------------------------------------
	public static void doSurrender(int balance, int betamount)
	{
			betamount = betamount/2;
			balance += betamount;
			

				System.out.println("Okay, you lost half your orginial bet: $" + betamount + ". Your balance is now: $" + balance);

				IO.readString(); //x

	}

//---------------------------------------------------------------------------------------------------

	public static int doDemo(Hand demohand, Hand dealinghand, ArrayList<String> shuffleddeck)
	{
		String playgame = "";

		int demobetamount;
		int numberofplayers = 0;
		int stophand = 0;
		int revolution = 0;
		int keepgoing = 0;

		do {

			Hand.addHand(shuffleddeck, demohand.hand);
			Hand.addHand(shuffleddeck, dealinghand.hand);

			Random demornd = new Random();
			Random stopdemo = new Random(); 

			demobetamount = (demornd.nextInt(6)*50)+200;
			stophand = (stopdemo.nextInt(7) + 15);

			System.out.println("Demo player betted $" + demobetamount + ".");

			IO.readString(); //x

			System.out.println(demohand.name + "\'s cards are: " + demohand.hand + ".\n" + demohand.name + 
												"\'s card value is " + Hand.giveHandvalue(demohand.hand) + ".");

			IO.readString(); //x

			System.out.println("The Dealer\'s card is: " + dealinghand.hand.get(0));

			IO.readString(); //x

			while(Hand.giveHandvalue(demohand.hand) < 21)
			{

					if(revolution == 0) {

					System.out.println("Now the demo hand will add a card to his hand if it is less than 21.");

					IO.readString(); //x

					System.out.println("Stopping either if the demo hand is busted which means the hand's value is more than 21.");

					IO.readString(); //x

					System.out.println("Or stopping when the hand's value is between 15-21.");

					IO.readString(); //x

					}


					if(Hand.giveHandvalue(demohand.hand) == 21 || Hand.giveHandvalue(demohand.hand) >= stophand)
					{					
						break;
					}


					Hand.addHand(shuffleddeck, demohand.hand);

					System.out.println(demohand.name + "\'s cards are: " + demohand.hand + ".\n" + demohand.name + 
													"\'s card value is " + Hand.giveHandvalue(demohand.hand) + ".");

					IO.readString(); //x

					if(Player.isBusted(demohand.hand) == true)
					{
							System.out.println("Demo busted!");

						break;
					}

					revolution++;

			}

			demohand.balance = 2500;

			if(keepgoing == 0) {

				System.out.println("Once the Demo is done playing or busted, the Dealer begins to play. Once the Dealer is done playing, the Dealer will display their hand.");

				IO.readString(); //x

				if(Player.isBusted(demohand.hand) == false) {

				Dealer.dealerPlaying(dealinghand.hand, shuffleddeck);

				}

				System.out.println("Dealer's cards are: " + dealinghand.hand + "\n" +
															"Dealer's card value is " + Hand.giveHandvalue(dealinghand.hand));	

				IO.readString(); //x

				System.out.println("If the Demo has a higher card value they win their orginial bet amount at a 1:1 ratio.");

				IO.readString(); //x

				System.out.println("Or if the Dealer busted their hand, which means they went over 21, they win their orginial bet amount at a 1:1 ratio.");

				IO.readString(); //x

				System.out.println("If the Demo has the same card value they just take their orginial bet amount back.");

				IO.readString(); //x

				System.out.println("If the Demo has a lower card value they lose their orginial bet amount.");

				IO.readString(); //x

				System.out.println("Or if the Demo busted their hand, which means they went over 21, they win their orginial bet amount.");

				IO.readString(); //x

			}else {


				if(Player.isBusted(demohand.hand) == false) {

				Dealer.dealerPlaying(dealinghand.hand, shuffleddeck);

				}

				System.out.println("Dealer's cards are: " + dealinghand.hand + "\n" +
															"Dealer's card value is " + Hand.giveHandvalue(dealinghand.hand));

				IO.readString(); //x

			}

				if(whoWins(Hand.giveHandvalue(demohand.hand),Hand.giveHandvalue(dealinghand.hand)) == true && Player.isBusted(demohand.hand) == false) {

					demobetamount = 2*demobetamount;
					demohand.balance += demobetamount;

					System.out.println("Demo player won: $" + demobetamount + ".\nDemo's balance is: $" + demohand.balance + ".");

				}else if(Player.isPush(Hand.giveHandvalue(demohand.hand), Hand.giveHandvalue(dealinghand.hand)) == true && Player.isBusted(demohand.hand) == false) {

					demohand.balance += demobetamount;

					System.out.println("Demo player won their bet back: $" + demobetamount + ".\nDemo's balance is: $" + demohand.balance + ".");

				}else if(whoWins(Hand.giveHandvalue(demohand.hand),Hand.giveHandvalue(dealinghand.hand)) == false && Player.isBusted(dealinghand.hand) == false) {

					demohand.balance -= demobetamount;
					System.out.println("Demo player lost: $" + demobetamount + ".\nDemo's balance is: $" + demohand.balance + ".");
				}


				removeCards(dealinghand.hand);
				removeCards(demohand.hand);

				if(keepgoing == 0) {

					IO.readString(); //x

					System.out.println("Would you like to continue this demo? However, a lot of the functionalities is missing from this demo.");

					IO.readString(); //x

					System.out.println("Please, type yes for the demo to stop playing or type no for the demo to keep playing.");

				playgame = IO.readString(); 
				playgame = makeyesnoProper(playgame);				

				}else{

						IO.readString(); //x 

						System.out.println("Please, type yes for the demo to stop playing or type no for the demo to keep playing.");

					playgame = IO.readString(); //x
					playgame = makeyesnoProper(playgame);				

				}





				if(playgame.equals("yes") == true)
				{

					System.out.println("Okay! We are moving on how many players are they? You can always go back to playing this demo. Just type \"0\" for the number of players.");

					numberofplayers = IO.readInt();		
					numberofplayers = makenumberofplayersProper(numberofplayers);

					if(numberofplayers > 0)
					{
						System.out.println("We will now start to regularly play the game");

						IO.readString(); //x

						break;
					}

				}

			keepgoing++;

		}while(true);

		return numberofplayers;

	}
//---------------------------------------------------------------------------------------------------	

	public static boolean[] doregularPlay(boolean insurance, boolean split, int balance, int betamount, Hand playershands, Hand playershandssplit, ArrayList<String> shuffleddeck)
	{

 		boolean surrender = false;
		boolean doubledown = false;
		String surrenders = "";
		String doubledowns = "";
		String hitorstand = "";

			if(insurance == false)
			{
					System.out.println("Would you like to surrender your hand? Please type yes/no.");
			

				surrenders = IO.readString();
				surrenders = makeyesnoProper(surrenders);

			}

						
		

		if(surrenders.equals("yes") == true)
		{

			surrender = Player.isSurrender(surrenders);

			doSurrender(balance, betamount);

		}else if(surrenders.equals("no") == true || insurance == true){

				if(insurance == false)
					System.out.println("Okay! You will not be surrendering your hand.");

				IO.readString(); //x

				System.out.println("Would you like to double down on your hand? Please type yes/no");
					
			doubledowns = IO.readString();
			doubledowns = makeyesnoProper(doubledowns);

			if(doubledowns.equals("yes") == true)
			{

				if((balance - betamount) < 0 && doubledowns.equals("yes") == true)
				{

					System.out.println("Looks like you don't have enough money in your balance to double down. We will move on.");

					IO.readString(); //x

				}else{		

					balance -= betamount;
					betamount = 2*betamount;
					dodoubleDown(split, balance, betamount, playershands, shuffleddeck);

					doubledown = Player.isdoubleDown(doubledowns);
				}
			}else{

					System.out.println("Okay! Let's move on.");

				IO.readString(); //x

			}
		}

		if(doubledown == false && surrender == false && split == false)
		{

				System.out.println("Would you like to hit or stand?");

			hitorstand = IO.readString();
			hitorstand = makehitstandProper(hitorstand);

			if(Player.ishitorstand(hitorstand) == true)
			{
				while(Hand.giveHandvalue(playershands.hand) < 21)
				{
					if(Player.ishitorstand(hitorstand) == false)
					{
						break;
					}

					Hand.addHand(shuffleddeck, playershands.hand);
						System.out.println(playershands.name + "\'s cards are: " + playershands.hand + "\n" + playershands.name + 
										"\'s card value is " + Hand.giveHandvalue(playershands.hand));

					if(Hand.giveHandvalue(playershands.hand) == 21)
					{
						break;
					}

					if(Player.isBusted(playershands.hand) == true)
					{
							System.out.println("Oh no! You busted!");

						IO.readString(); //x

						break;
					}

						System.out.println("Would you like to hit or stand?");
					hitorstand = IO.readString();
					hitorstand = makehitstandProper(hitorstand);
				}
			}
		}

		boolean[] surrddown = {surrender,doubledown};

		return surrddown;
	}

//--------------------------------------------------------------------------------------------------- 

	public static String doInsurance(int balance, int betamount)
	{
		int insbetamount = betamount/2;
		boolean insurance = true;
		String yesornobal;

		if(balance == 0)
		{

				System.out.println("Bummer! You have no cash left in your balance to buy insurance.");


			return "noinsurance";

		}else if((balance - insbetamount) < 0){

			insbetamount = 0;

			System.out.println("Looks like the bet amount for insurance is higher than the amount in your balance."
								+ " Would you like to bet your leftover balance instead?");

			IO.readString(); //x

			System.out.println("Your balance is: $" + balance + ". Please type yes/no.");

			yesornobal = IO.readString();
			yesornobal = makeyesnoProper(yesornobal);

			if(yesornobal.equals("yes") == true)
			{

				insbetamount += balance;
					System.out.println("Okay! You betted: $" + balance);


				return "balanceinsurance";

			}else if(yesornobal.equals("no") == true){

				System.out.println("Alright, you will keep your money in your balance.");


				return "insurance";

			}
		}else if((balance - insbetamount) >= 0){

				System.out.println("Okay! You betted: $" + insbetamount);

				return "insurance";	
		}
									
		return "";		
	}
//---------------------------------------------------------------------------------------------------bang


	public static boolean dodoubleDown(boolean split, int balance, int betamount, Hand newplayershands, ArrayList<String> shuffleddeck)
	{
		if(balance != 0 || balance - betamount > 0)
		{
			System.out.println("Okay! You will now bet 2x the amount you orginally betted! You are now betting: $" + betamount + ".");

			IO.readString(); //x

			System.out.println("Your balance is now: $" + balance);
							
			IO.readString(); //x

			System.out.println("We will now give you one additonal card.");

			IO.readString(); //x

		Hand.addHand(shuffleddeck, newplayershands.hand);

			System.out.println(newplayershands.name + "\'s cards are: " + newplayershands.hand + "\n" + newplayershands.name + 
							"\'s card value is " + Hand.giveHandvalue(newplayershands.hand));

			IO.readString(); //x


			if(Player.isBusted(newplayershands.hand) == true)
			{
				if(split == true)
				{
					System.out.println("Oh no! You busted! Let's move onto another hand.");

					IO.readString(); //x	

				}else{

					System.out.println("Oh no! You busted!");

					IO.readString(); //x

				}
			}
		}
		return true;

	}
//--------------------------------------------------------------------------------------------------- 

	public static int doSplit(int balance, int betamount, Hand playershands, Hand playershandssplit, ArrayList<String> shuffleddeck)
	{
		int insbetamount = 0;
		int splbetamount = 0;
		int doubledownenter = 0;
		boolean split = true;
		boolean surrender = false;
		boolean doubledown = false;		
		String yesornospl = "";
		String yesornosplbal = "";
		String yesornospldown = "";
		String doubledowns = "";
		String hitorstand = "";
		
		balance -= betamount;
		betamount = 2*betamount;

							if((balance - betamount) >= 0)
							{

								System.out.println("Okay! You will now bet 2x the amount you orginally betted! You are now betting: $" + betamount + ".");

								IO.readString(); //x

								System.out.println("Your balance is now: $" + balance);

								IO.readString(); //x

							for(int i = 0; i < 2; i++)
							{
								switch(i){
									case 0: (playershandssplit.hand).add(playershands.hand.get(i+1));
											break;
									case 1: (playershands.hand).remove(i);
											break;
								}
							}

							Hand.addHand(shuffleddeck, playershands.hand);						
							Hand.addHand(shuffleddeck, playershandssplit.hand);					

								System.out.println("Okay! Here is your first hand: " + playershands.hand + "\n" + playershands.name + "\'s card value is " +
													Hand.giveHandvalue(playershands.hand));

								IO.readString(); //x

							if(Player.isBlackJack(playershands.hand) == false)
							{
								
								

									System.out.println("Would you like to double down on this hand? Please type yes/no.");

									yesornospldown = IO.readString();
									yesornospldown = makeyesnoProper(yesornospldown);


									if((balance - betamount) < 0 && yesornospldown.equals("yes") == true)
									{

										System.out.println("Looks like you don't have enough money in your balance to double down. We will move on.");

										IO.readString(); //x

									}else{									

										if(yesornospldown.equals("yes") == true)
										{
											doubledown = true;
											doubledownenter++;
											balance -= betamount;
											betamount = 2*betamount;

											dodoubleDown(split, balance, betamount, playershands, shuffleddeck);

										}else if(yesornospldown.equals("yes") == false || doubledown == false){

												

												System.out.println("Would you like to hit or stand?");
										
															 
											hitorstand = IO.readString();
											hitorstand = makehitstandProper(hitorstand);

											if(Player.ishitorstand(hitorstand) == true)
											{
												while(Hand.giveHandvalue(playershands.hand) < 21)
												{

													if(Player.ishitorstand(hitorstand) == false)
													{
														break;
													}

													Hand.addHand(shuffleddeck,playershands.hand);
														System.out.println(playershands.name + "\'s cards are: " + playershands.hand + "\n" + playershands.name + 
																	"\'s card value is " + Hand.giveHandvalue(playershands.hand));

													

													if(Hand.giveHandvalue(playershands.hand) == 21)
													{
														break;
													}		
																		
													if(Player.isBusted(playershands.hand) == true)
													{

															System.out.println("Oh no! You busted! Let's move on.");
															IO.readString(); //x

														break;
													}

												

														System.out.println("Would you like to hit or stand?");

													hitorstand = IO.readString();
													hitorstand = makehitstandProper(hitorstand);
												}
											}
										}doubledown = false;
									}
								}

							
								if(Player.isBlackJack(playershandssplit.hand) == false)
								{

									System.out.println("Okay! Here is your other hand: " + playershandssplit.hand + "\n" + playershands.name + "\'s card value is " +
														Hand.giveHandvalue(playershandssplit.hand));

									IO.readString(); //x


									


										System.out.println("Would you like to double down on this hand? Please type yes/no.");

										yesornospldown = IO.readString();
										yesornospldown = makeyesnoProper(yesornospldown);

										if((balance - betamount) < 0 && yesornospldown.equals("yes") == true)
										{

											System.out.println("Looks like you don't have enough money in your balance to double down. We will move on.");

											IO.readString(); //x							

										}else{									

											if(yesornospldown.equals("yes") == true)
											{
												doubledown = true;
												doubledownenter++;
												balance -= betamount;
												betamount = 2*betamount;

												dodoubleDown(split, balance, betamount, playershandssplit, shuffleddeck);

											}else if(yesornospldown.equals("yes") == false || doubledown == false){



													System.out.println("Would you like to hit or stand?");
											
																 
												hitorstand = IO.readString();
												hitorstand = makehitstandProper(hitorstand);

												if(Player.ishitorstand(hitorstand) == true)
												{
													while(Hand.giveHandvalue(playershandssplit.hand) < 21)
													{

														if(Player.ishitorstand(hitorstand) == false)
														{
															break;
														}

														Hand.addHand(shuffleddeck,playershandssplit.hand);
															System.out.println(playershandssplit.name + "\'s cards are: " + playershandssplit.hand + "\n" + playershandssplit.name + 
																		"\'s card value is " + Hand.giveHandvalue(playershandssplit.hand));

														

														if(Hand.giveHandvalue(playershandssplit.hand) == 21)
														{
															break;
														}		
																			
														if(Player.isBusted(playershandssplit.hand) == true)
														{

																System.out.println("Oh no! You busted! Let's move on.");

																IO.readString(); //x

															break;
														}

															System.out.println("Would you like to hit or stand?");

														hitorstand = IO.readString();
														hitorstand = makehitstandProper(hitorstand);
												}
											}
										}
									}
								}
						}

		return doubledownenter;
	}	

}	


