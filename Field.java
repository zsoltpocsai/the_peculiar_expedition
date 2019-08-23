
public class Field {

    private FieldType type;
    private Position pos;
    private boolean explored;

    public Field(FieldType type) {
        this.type = type;
        this.pos = new Position();
        explored = false;
    }

    public FieldType getType() {
        return type;
    }

    public String getName() {
        return type.name;
    }

    public char getMapRepresentation() {
        return type.mapRepresentation;
    }

    public Position getPosition() {
        return pos;
    }

    public void setPosition(Position p) {
        this.pos = p;
    }

    public float getCostOfMoveModifier() {
        return type.costOfMoveModifier;
    }

    public boolean isPassable() {
        return type.passable;
    }

    public boolean isExplored() {
        return explored;
    }

    public void setExplored() {
        explored = true;
    }
}