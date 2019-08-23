public class MapDestroyer extends Field {

    private static final int DESTRUCTION_SPEED = 1;

    private Map map;
    private int phase = 1;
    private boolean active;

    private String eventDescribtion;
    private FieldType generatedFieldType;
    private boolean recovers;
    private int maxRangeOfDestructedArea;

    public MapDestroyer(Map map, FieldType type) {
        super(type);
        this.map = map;

        active = true;
    }

    public void setEventDescribtion(String text) {
        eventDescribtion = text;
    }

    public void setGeneratedFieldType(FieldType type) {
        generatedFieldType = type;
    }

    public void setIfRecovers(boolean recovering) {
        recovers = recovering;
    }

    public void setMaxRangeOfDestructedArea(int value) {
        maxRangeOfDestructedArea = value;
    }

    public boolean isActive() {
        return active;
    }

    public void event() {
        int holdingAreaDestructed = 4;

        if (phase <= maxRangeOfDestructedArea) {
            convertFieldsTo(generatedFieldType, DESTRUCTION_SPEED * phase);
            InputHandler.printMessage(eventDescribtion);
            phase++;
        } else if (phase <= (maxRangeOfDestructedArea + holdingAreaDestructed)) {
            phase++;
        } else {
            if (recovers) {
                convertFieldsTo(FieldType.GRASS, DESTRUCTION_SPEED * maxRangeOfDestructedArea);
            }
            active = false;
        }
    }

    private void convertFieldsTo(FieldType type, int distance) {
        Position selfPos = getPosition();
        Position pos;
        Field field;
        Field newField;
        int range = distance;

        if (range > maxRangeOfDestructedArea) {
            range = maxRangeOfDestructedArea;
        }

        for (int i = (selfPos.y - range); i <= (selfPos.y + range); i++) {
            for (int j = (selfPos.x - range); j <= (selfPos.x + range); j++) {
                pos = new Position(j, i);
                field = map.getField(pos);
                if (field.getType() == FieldType.GRASS
                        || field.getType() == FieldType.JUNGLE
                        || field.getType() == FieldType.BUSH
                        || field.getType() == FieldType.LAVA) {
                    newField = new Field(type);
                    if (field.isExplored()) {
                        newField.setExplored();
                    }
                    map.changeField(pos, newField);
                }
            }
        }
    }
}
