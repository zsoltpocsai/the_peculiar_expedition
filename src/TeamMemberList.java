import java.util.ArrayList;

public class TeamMemberList extends ArrayList<TeamMember> {

    public boolean contains(String name) {
        return (indexOf(name) >= 0);
    }

    private int indexOf(String name) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public int numberOf(TeamMemberType type) {
        int number = 0;

        for (TeamMember t : this) {
            if (t.type == type) {
                number++;
            }
        }
        return number;
    }

    public TeamMember get(String name) {
        for (TeamMember t : this) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    private String getMemberData(int index) {
        TeamMember tm = this.get(index);
        String memberData;

        memberData = "[" + tm.getName() + "], " + tm.type.getName()
                + ", status: " + tm.getStatus()
                + ", energy: " + tm.getEnergy();

        return memberData;
    }

    public void listMembers() {
        if (this.isEmpty()) {
            System.out.println("No members");
        } else {
            for (int i = 0; i < this.size(); i++) {
                System.out.println(getMemberData(i));
            }
        }
    }
}
