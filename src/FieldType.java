public enum FieldType {

    GRASS ("grass", ',', 1, true),
    OCEAN ("ocean", '~', 1, false),
    BUSH ("bush", ';', (float)1.4, true),
    LAVA ("lava", '$', 1, true),
    WET_FIELD ("wet field", '&', (float)1.8, true),
    LAKE ("lake", '#', 1, false),
    MOUNTAIN ("mountain", '/', 1, false),
    JUNGLE ("jungle", '@', 2, true),

    EXPEDITION_SHIP ("expedition ship", 'E', 1, true),
    VILLAGE ("village", 'V', 1, true),
    CAVE ("cave", 'C', 1, true),
    ALTAR ("altar", 'A', 1, true),
    MOUNTAIN_SANCTUARY ("mountain sanctuary", 'S', 1, true),
    GOLDEN_PYRAMID ("golden pyramid", 'G', 1, true),
    VOLCANO ("volcano", 'O', 1, false),
    GEYSER ("geyser", '+', 1, false);

    public String name;
    public char mapRepresentation;
    public float costOfMoveModifier;
    public boolean passable;

    FieldType(String name, char mapRepresentation, float costOfMoveModifier,
              boolean passable) {

        this.name = name;
        this.mapRepresentation = mapRepresentation;
        this.costOfMoveModifier = costOfMoveModifier;
        this.passable = passable;
    }
}
