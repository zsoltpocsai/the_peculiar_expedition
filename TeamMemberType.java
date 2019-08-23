public enum TeamMemberType {

    MERCHANT ("merchant", (float)0.1),
    SOLDIER ("soldier", (float)0.2),
    DONKEY ("donkey", (float)2),
    SCOUT ("scout", (float)1),
    SHAMAN ("shaman", (float)0.2),
    SAGE ("sage", (float)3);

    private String name;
    public float bonus;

    TeamMemberType(String name, float bonus) {
        this.name = name;
        this.bonus = bonus;
    }

    public String getName() {
        return name;
    }
}
