
public class Position {
	
	public int x;
	public int y;
	
	public Position() {
		this.x = 0;
		this.y = 0;
	}
	
	public Position(int x, int y) {
		if (x < 0) {
			x = 0;
		}
		if (x >= Map.WIDTH) {
			x = Map.WIDTH - 1;
		}
		if (y < 0) {
			y = 0;
		}
		if (y >= Map.HEIGHT) {
			y = Map.HEIGHT - 1;
		}
		this.x = x;
		this.y = y;
	}

	public int toInt() {
	    return (x * 1000) + y;
    }

	public String toString() {
		return "X:" + this.x + " Y:" + this.y;
	}
}