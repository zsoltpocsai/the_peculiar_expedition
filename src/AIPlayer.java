
public class AIPlayer implements Famous, Comparable<Famous> {

    private String name;
    private int fame;

    public AIPlayer(String name) {
        this.name = name;
        fame = 0;
    }

    public String getName() {
        return name;
    }

    public void gainFame(int fame) {
        this.fame += fame;
    }

    public int getFame() {
        return fame;
    }

    public int compareTo(Famous player) {
        return Famous.super.compareTo(player);
    }
}
