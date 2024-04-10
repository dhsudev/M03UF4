public class Gamer {
	public Position position = new Position(0, 0);
	public int direction = 0; // 0 - Nord, 1 - Oest, 2 - Sud, 3 - Est
	public char icon;
	private Map map;
	public Gamer(Map map) {
		this.map = map;
		setPosition(map.getDoor(), false);
		setDirection(3);
	}

	public boolean setPosition(Position pos, boolean add) {
		if(add){
			pos.x += this.position.x;
			pos.y += this.position.y;

		}
		if(pos.x >= 0 && pos.y >= 0 && (!map.isWall(pos) || map.isDoor(pos) || map.isExit(pos))){
			Log.d("updated pos");
			this.position.x = pos.x;
			this.position.y = pos.y;
			return(true);
		}
		Log.d("pos not updated");
		return (false);
	}
	// Method to turn left
    public void turnLeft() {
		setDirection((this.direction - 1 + 4) % 4);
	}
	
	// Method to turn right
	public void turnRight() {
		setDirection((this.direction + 1) % 4);
	}
	
	// Method to turn move the gamer depending on 
    public void setDirection(int n) {
       if(n > -1 && n<4){
		this.direction = n;
		switch (direction) {
			case 0:
				this.icon = MazeChars.ARROW_UP;
				break;
			case 1:
				this.icon = MazeChars.ARROW_LEFT;
				break;
			case 2:
				this.icon = MazeChars.ARROW_DOWN;
				break;
			case 3:
				this.icon = MazeChars.ARROW_RIGHT;
				break;
		}
	}
    }

    // Method for turning in a specific direction (90 * n degrees)
    public void turn(int n) {
        if (n < 0) {
			// If n is negative, turn left |n| times
			for (int i = 0; i < Math.abs(n); i++) {
				turnLeft();
			}
		} else {
			// If n is positive, turn right n times
			for (int i = 0; i < n; i++) {
				turnRight();
			}
		}
    }

	public boolean move(int n) {
		if(n==0){n=1;}
		Position pos = new Position(position.x, position.y);
		if (direction == 0) { // UP
			pos.y -= n;
		} else if (direction == 1) { // Left
			pos.x -= n;
		} else if (direction == 2) { // Down
			pos.y += n;
		} else if (direction == 3) { // Right
			pos.x += n;
		}
		return(setPosition(pos, false));
		//Log.e("h", pos.toString());
		//return false;
	}
}