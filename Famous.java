public interface Famous {

    String getName();
    int getFame();
    void gainFame(int fame);

    default int compareTo(Famous player) {
        if (this.getFame() > player.getFame()) {
            return -1;
        } else if (this.getFame() == player.getFame()) {
            return 0;
        } else {
            return 1;
        }
    }
}
