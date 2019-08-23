public class Volcano extends MapDestroyer {

    public Volcano(Map map) {
        super(map, FieldType.VOLCANO);
        super.setEventDescribtion("A volcano has erupted!");
        super.setGeneratedFieldType(FieldType.LAVA);
        super.setIfRecovers(true);
        super.setMaxRangeOfDestructedArea(3);
    }

    public void eruption() {
        super.event();
    }
}
