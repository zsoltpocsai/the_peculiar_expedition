
public class TeamMember extends Explorer {

    private static int count = 1;
    public TeamMemberType type;

    public TeamMember(TeamMemberType type) {
        super(type.getName() + count);
        this.type = type;
        count++;
    }
}