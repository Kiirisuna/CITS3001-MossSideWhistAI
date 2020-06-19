import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class MSWAgentImp3 implements MSWAgent{

	public static final int LEADER = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public int order;
	public List<Card> hand;
	public Card playedCardL = null;
	public Card playedCardR = null;

	private boolean isHaveToRemove =false;
	private int totalScore = 0;
	private int agentLeftTS = 0;
	private int agentRightTS = 0;
	private int rounds = 1;
	private int first = 0;
	private int second = 0;
	private int third = 0;
	private String[] winnerPerTrick = new String[16];
	private int trickRound = 0;

	public String agentLeft;
	public String agentRight;
	public String botName;


	/**
	 * Tells the agent the names of the competing agents, and their relative position.
	 * */
	public void setup(String agentLeft, String agentRight){
		this.agentLeft = agentLeft;
		this.agentRight = agentRight;
		// Code here
	}

	/**
	 * Starts the round with a deal of the cards.
	 * The agent is told the cards they have (16 cards, or 20 if they are the leader)
	 * and the order they are playing (0 for the leader, 1 for the left of the leader, and 2 for the right of the leader).
	 * */
	public void seeHand(List<Card> hand, int order) {
		this.hand = hand;
		this.order = order;

		// Code here
	}

	/**
	 * This method will be called on the leader agent, after the deal.
	 * If the agent is not the leader, it is sufficient to return an empty array.
	 */
	public Card[] discard() {
		Card[] disposable = new Card[4];
		sortCards();
		System.out.println("Card on hand: ");
		for(Card c : hand) {
			System.out.print(c + ", ");
		}
		System.out.println("");

		if(hand.size() == 20) {
			int j = 0; //counter for cards not SPADES, we throw away the lowest cards
			for (int i = 0; j < 4; i++) {
				if(hand.get(i).suit != Suit.SPADES) {
					if(hand.get(i).rank != 14 || hand.get(i).rank != 13 || hand.get(i).rank != 12 || hand.get(i).rank != 11) {
						disposable[j] = hand.get(i);
						j++;
					}
				}
			}
		}
		sortSuits();
		System.out.println("Card on hand: ");
		for(Card c : hand) {
			System.out.print(c + ", ");
		}
		System.out.println("");
		return disposable;
		// Code here
	}

	// Sorting from smallest to biggest based on rank
	private void sortCards() {
		Card min;
		for (int i = 0; i < hand.size(); i++) {
			for ( int j = i; j>0 ; j--) {
				if (  hand.get(j).rank < hand.get(j-1).rank ) {
					min =  hand.get(j);
					hand.set(j, hand.get(j-1));
					hand.set(j-1, min);
				}
			}
		}
	}

	// Sorting based on Suits
	private void sortSuits(){
		// Sorts the cards in the hand so that cards of the same suit are
		List<Card> Diamonds = new ArrayList<Card>();
		List<Card> Clubs = new ArrayList<Card>();
		List<Card> Hearts = new ArrayList<Card>();
		List<Card> Spades = new ArrayList<Card>();

		int i = 0;

		while (!hand.isEmpty()) {
			Card tempCard = hand.remove(0);
			if (tempCard.suit == Suit.DIAMONDS) {
				Diamonds.add(tempCard);
			} else if (tempCard.suit == Suit.CLUBS) {
				Clubs.add(tempCard);
			} else if (tempCard.suit == Suit.HEARTS) {
				Hearts.add(tempCard);
			} else {
				Spades.add(tempCard);
			}
			i++;
		}
		hand.addAll(Diamonds);
		hand.addAll(Clubs);
		hand.addAll(Hearts);
		hand.addAll(Spades);
	}

	/**
	 * Agent returns the card they wish to play.
	 * A 200 ms timelimit is given for this method
	 * @return the Card they wish to play.
	 * */

	public Card playCard() {
		sortCards();
		Card temp = null;
		// Last playing agent
		if(playedCardL != null && playedCardR != null) {
			temp = playCard(playedCardL, playedCardR);
			playedCardL = null;
			playedCardR = null;
			return temp;
		}
		// Second playing agent
		else if(playedCardL != null || playedCardR!= null) {
			if(playedCardL == null) {
				temp = playCard(playedCardR);
				playedCardR = null;
				return temp;
			}
			else if(playedCardR == null){
				temp = playCard(playedCardL);
				playedCardL = null;
				return temp;
			}
		}
		// If im the first to play
		return hand.remove(hand.size() - 1);  // Throw out my strongest card on hand
		// Code here
	}

	public Card playCard(Card lead) {
		boolean isContainSuit = false;
		ArrayList<Card> cards = new ArrayList<>();
		for (Card c: hand) {
			if(c.suit == lead.suit) {
				cards.add(c);
				isContainSuit = true;
			}
		}
		Card card = getSpadeOrLow();
		if(!isContainSuit) {
			if(isHaveToRemove) {
				hand.remove(card);
			}
			return card;
		}

		for(Card c: cards) {
			if( c.rank > lead.rank) {
				if (isHaveToRemove) {
					hand.remove(c);
				}
				return c;
			}
		}

		if(isHaveToRemove) {
			hand.remove(cards.get(0));
		}
		return cards.get(0);
	}

	public Card playCard(Card lead, Card next) {
		Card maxCard;
		if(lead.rank > next.rank && lead.suit == next.suit) {
			maxCard = lead;
		}
		else if(lead.rank < next.rank && lead.suit == next.suit) {
			maxCard = next;
		}
		else {
			maxCard = lead;
		}
		isHaveToRemove = false;
		Card card = playCard(maxCard);
		isHaveToRemove = true;
		hand.remove(card);
		return card;
	}

	// Uses any spade card or lowest rank and suits card on hand
	public Card getSpadeOrLow() {
		Card spade;
		for(Card c : hand) {
			if(c.suit == Suit.SPADES) {
				spade = c;
				return spade;
			}
		}
		return hand.get(0);
	}

	/**
	 * Sees an Agent play a card.
	 * A 50 ms timelimit is given to this function.
	 * @param card, the Card played.
	 * @param agent, the name of the agent who played the card.
	 * */
	public void seeCard(Card card, String agent) {
		if(agent == agentLeft) {
			playedCardL = card;
		}
		else if(agent == agentRight) {
			playedCardR = card;
		}

	}

	/**
	 * See the result of the trick. 
	 * A 50 ms timelimit is given to this method.
	 * This method will be called on each eagent at the end of each trick.
	 * @param winner, the player who played the winning card.
	 * */
	public void seeResult(String winner) {
		winnerPerTrick[trickRound++] = winner;
		// Code here
	}

	/**
	 * See the score for each player.
	 * A 50 ms timelimit is givien to this method
	 * @param scoreboard, a Map from agent names to their score.
	 **/
	public void seeScore(Map<String, Integer> scoreboard) {
		
		// For validation purposes
		System.out.println(botName);
		int s1 = 0;
		int s2 = 0;
		int s3 = 0;
		for (Map.Entry pair : scoreboard.entrySet()) {
			//System.out.println(pair);
			if((String) pair.getKey() == botName) {
				s1 = (int) pair.getValue();
			}
			else if((String) pair.getKey() == agentLeft) {
				s2 = (int) pair.getValue();
			}
			else {
				s3 = (int) pair.getValue();
			}
		}
		if(s1 > s2 && s1 > s3) {	// my agent beats all other agent
			first++;
		}
		else if(s1 > s2 || s1 > s3) { // my agent only beat 1 agent
			second++;
		}
		else {	// my agent lose to all other agent
			third++;
		}
		System.out.println(rounds + " Rounds played");
		System.out.println("First: " + first + ", Second: " + second + ", Third: " + third);
		rounds++;
		trickRound = 0;

		// Code here
	}

	/**
	 * Returns the Agents name.
	 * A 10ms timelimit is given here.
	 * This method will only be called once.
	 * */
	public String sayName() {
		botName = "ooo";
		return botName;
	}

}
