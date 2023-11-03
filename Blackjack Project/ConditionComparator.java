import java.util.*;

public class ConditionComparator implements Comparator<Player> {

	public int compare(Player a, Player b) {

		return a.condition.compareTo(b.condition);
	}
}