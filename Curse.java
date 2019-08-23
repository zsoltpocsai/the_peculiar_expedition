import java.util.Random;

public class Curse {

    private Map map;
    private Player player;

    public Curse(Player player, Map map, int chanceOfCurse) {
        Random rand = new Random();
        Volcano volcano;
        Geyser geyser;
        Position position;
        int randNum;

        this.map = map;
        this.player = player;

        if (rand.nextInt(100) < chanceOfCurse) {

            System.out.println("A curse has happened!");

            randNum = rand.nextInt(100);

            if (0 <= randNum && randNum < 35) {
                volcano = new Volcano(map);
                position = selectVolcanoPosition();
                if (map.getField(position).isExplored()) {
                    volcano.setExplored();
                }
                map.changeField(position, volcano);
                map.volcanos.add(volcano);
                InputHandler.printMessage("A volcano has revealed somewhere near!");
            } else {
                geyser = new Geyser(map);
                position = selectGeyserPosition();
                if (map.getField(position).isExplored()) {
                    geyser.setExplored();
                }
                map.changeField(position, geyser);
                map.geysers.add(geyser);
                InputHandler.printMessage("A geyser has revealed somewhere near!");
            }
        }
    }

    private Position selectVolcanoPosition() {
        Position closestPos, playerPos, pos;

        playerPos = player.getPosition();
        closestPos = map.mountains.get(0).getPosition();

        for (Field f : map.mountains) {
            pos = f.getPosition();
            if (Math.abs(pos.x - playerPos.x) < Math.abs(closestPos.x - playerPos.x)
                    || Math.abs(pos.y - playerPos.y) < Math.abs(closestPos.y - playerPos.y)) {
                closestPos = pos;
            }
        }

        return closestPos;
    }

    private Position selectGeyserPosition() {
        Random rand = new Random();
        FieldType fieldType;
        Position playerPos, pos;
        int range = 5;
        int x, y;

        playerPos = player.getPosition();

        do {
            x = rand.nextInt((playerPos.x + range) - (playerPos.x - range)) + (playerPos.x - range);
            y = rand.nextInt((playerPos.y + range) - (playerPos.y - range)) + (playerPos.y - range);
            pos = new Position(x, y);
            fieldType = map.getField(pos).getType();
        } while (fieldType != FieldType.GRASS && fieldType != FieldType.BUSH
                && fieldType != FieldType.JUNGLE && fieldType != FieldType.WET_FIELD);

        return pos;
    }
}
