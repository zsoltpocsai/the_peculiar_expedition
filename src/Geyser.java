public class Geyser extends MapDestroyer {

    public Geyser(Map map) {
        super(map, FieldType.GEYSER);
        super.setEventDescribtion("A geyser has erupted!");
        super.setGeneratedFieldType(FieldType.WET_FIELD);
        super.setIfRecovers(false);
        super.setMaxRangeOfDestructedArea(4);
    }

    public void eruption() {
        super.event();
    }
}
