import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MSWAgentImp2 implements MSWAgent{

  public static final int LEADER = 0;
  public static final int LEFT = 1;
  public static final int RIGHT = 2;
  public int order;
  public List<Card> hand;

  private boolean isHaveToRemove = true;
  private String agentLeft;
  private String agentRight;


  public int getOrder() {
    return order;
  }



  /**
   * Tells the agent the names of the competing agents, and their relative position.
   * */
  public void setup(String agentLeft, String agentRight){
    this.agentLeft = agentLeft;
    this.agentRight = agentRight;
  }

  /**
   * Starts the round with a deal of the cards.
   * The agent is told the cards they have (16 cards, or 20 if they are the leader)
   * and the order they are playing (0 for the leader, 1 for the left of the leader, and 2 for the right of the leader).
   * */
  public void seeHand(List<Card> hand, int order) {
    this.hand = hand;
    /*for(Card c: hand) {
    		System.out.print(c + ", ");
    }*/
    this.order = order;
  }

  /**
   * This method will be called on the leader agent, after the deal.
   * If the agent is not the leader, it is sufficient to return an empty array.
   */
  public Card[] discard() {
    Card[] cards = new Card[4];
    //sortCards();
    for(Card c: hand) {
		System.out.print(c + ", ");
}
    int j = 0;
    for (int i = 0; j < 4; i++) {
      if(hand.get(i).suit != Suit.SPADES) {
        cards[j] = hand.get(i);
        j++;
      }
    }
    return cards;
  }

  //sorting cards
  public void sortCards() {
    Card min;
    for ( int j = 0; j < hand.size(); j++) {
      min = hand.get(j);
      for ( int i = j; i < hand.size(); i++) {
        if (  hand.get(i).rank < hand.get(j).rank ) {
          min =  hand.get(i);
          hand.set(i, hand.get(j));
          hand.set(j, min);
        }
      }
    }
  }

  /**
   * Agent returns the card they wish to play.
   * A 200 ms timelimit is given for this method
   * @return the Card they wish to play.
   * */
  public Card playCard() {
    Card card = hand.get(hand.size() - 1);
    return card;
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
    if(lead.rank > next.rank && lead.suit == next.suit)
      maxCard = lead;
    else if(lead.rank < next.rank && lead.suit == next.suit)
      maxCard = next;
    else
      maxCard = lead;
    isHaveToRemove = false;
    Card card = playCard(maxCard);
    isHaveToRemove = true;
    hand.remove(card);
    return card;
  }

  public Card getSpadeOrLow() {
    Card spade;
    for(Card c : hand) {
      if(c.suit == Suit.SPADES)
        spade = c;
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
    if(hand.contains(card))
      hand.remove(card);
    //System.out.println(agent + ": " + card);
    try {
      Thread.sleep(50);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * See the result of the trick. 
   * A 50 ms timelimit is given to this method.
   * This method will be called on each eagent at the end of each trick.
   * @param winner, the player who played the winning card.
   * */
  public void seeResult(String winner) {
    //System.out.println("Winner of trick is " + winner);
    try {
      Thread.sleep(50);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * See the score for each player.
   * A 50 ms timelimit is givien to this method
   * @param scoreboard, a Map from agent names to their score.
   **/
  public void seeScore(Map<String, Integer> scoreboard) {
    //System.out.println("Score:");
    for (Map.Entry pair : scoreboard.entrySet())
      //System.out.println(pair);
    try {
      Thread.sleep(50);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the Agents name.
   * A 10ms timelimit is given here.
   * This method will only be called once.
   * */
  public String sayName() {
	  return "ooo";
  }

}
