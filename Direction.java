
public enum Direction {

    NORTH (new String[] {"north", "n"}),
    EAST (new String[] {"east", "e"}),
    WEST (new String[] {"west", "w"}),
    SOUTH (new String[] {"south", "s"});


    public String[] names;

    Direction(String[] names) {
        this.names = names;
    }
}