public enum MapAction {

    MAP (new String[] {"map", "m"}),
    INVENTORY (new String[] {"inventory", "inv", "i"});

    public String[] names;

    MapAction(String[] names) {
        this.names = names;
    }
}
