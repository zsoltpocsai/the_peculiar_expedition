import java.io.File;
import java.util.Random;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Map {

    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;

    private Field[][] map;
    private ArrayList<Position> shipLandingLocations;
    public ArrayList<Field> mountains;
    public ArrayList<Volcano> volcanos;
    public ArrayList<Geyser> geysers;
    private Position startingLocation;

    public Map() {
        map = new Field[HEIGHT][WIDTH];
        shipLandingLocations = new ArrayList<>();
        mountains = new ArrayList<>();
        volcanos = new ArrayList<>();
        geysers = new ArrayList<>();
        createMap();
        setStartingLocation();
    }

    private void createMap() {
        Random rand = new Random();
        FileReader in;
        char c;

        try {
            switch (rand.nextInt(3)) {
                case 0:
                    in = new FileReader("maps" + File.separator + "map1.txt");
                    break;
                case 1:
                    in = new FileReader("maps" + File.separator + "map2.txt");
                    break;
                default:
                    in = new FileReader("maps" + File.separator + "map3.txt");
                    break;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Map is not found!");
            return;
        }

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {

                try {
                    do {
                        c = (char) in.read();
                    } while (c == ' ' || c == '\n' || c == '\r');

                } catch (IOException e) {
                    System.err.println("Problem with reading the file!");
                    return;
                }

                for (FieldType type : FieldType.values()) {
                    if (c == type.mapRepresentation) {
                        switch (type) {
                            case ALTAR:
                                map[i][j] = new Altar();
                                break;
                            case CAVE:
                                map[i][j] = new Cave();
                                break;
                            case MOUNTAIN_SANCTUARY:
                                map[i][j] = new MountainSanctuary();
                                break;
                            case VILLAGE:
                                map[i][j] = new Village();
                                break;
                            case GOLDEN_PYRAMID:
                                map[i][j] = new GoldenPyramid();
                                break;
                            default:
                                map[i][j] = new Field(type);
                                break;
                        }
                        map[i][j].setPosition(new Position(j, i));
                        break;
                    }
                }

                if (map[i][j].getType() == FieldType.EXPEDITION_SHIP) {
                    shipLandingLocations.add(map[i][j].getPosition());
                } else if (map[i][j].getType() == FieldType.MOUNTAIN) {
                    mountains.add(map[i][j]);
                }
            }
        }
    }

    public void revealMapAround(Position p, int range) {
        Position pos;

        for (int i = (p.y - range); i <= (p.y + range); i++) {
            for (int j = (p.x - range); j <= (p.x + range); j++) {
                pos = new Position(j, i);
                getField(pos).setExplored();
            }
        }
    }

    public String listSurroundings(Position p) {
        String list;

        list = "[north] " + getField(new Position(p.x, p.y - 1)).getName()
                + System.lineSeparator()
                + "[east] " + getField(new Position(p.x + 1, p.y)).getName()
                + System.lineSeparator()
                + "[west] " + getField(new Position(p.x - 1, p.y)).getName()
                + System.lineSeparator()
                + "[south] " + getField(new Position(p.x, p.y + 1)).getName();

        return list;
    }

    public String drawMap(Position playerPosition) {
        String mapRep;

        mapRep = "";
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (j == 0) {
                    mapRep = mapRep.concat(" ");
                }
                if (playerPosition.x == j && playerPosition.y == i) {
                    mapRep = mapRep.concat(" " + 'X');
                } else {
                    if (map[i][j].isExplored()) {
                        mapRep = mapRep.concat(" " + map[i][j].getMapRepresentation());
                    } else {
                        mapRep = mapRep.concat(" ?");
                    }
                }
                if (j == WIDTH - 1) {
                    mapRep = mapRep.concat(System.lineSeparator());
                }
            }
        }

        return mapRep;
    }

    public Field getField(Position pos) {

        return map[pos.y][pos.x];
    }

    public void changeField(Position pos, Field newField) {
        map[pos.y][pos.x] = newField;
        newField.setPosition(pos);
    }

    public Position getStartingLocation() {
        return startingLocation;
    }

    private void setStartingLocation() {
        Random rand = new Random();

        startingLocation = shipLandingLocations.get(rand.nextInt(shipLandingLocations.size()));

        convertBackLandingLocations();
    }

    private void convertBackLandingLocations() {

        for (Position p : shipLandingLocations) {
            if (p.toInt() != startingLocation.toInt()) {
                changeField(p, new Field(FieldType.OCEAN));
            }
        }
    }
}