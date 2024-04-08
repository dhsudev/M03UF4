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
			Log.w("a", "updated pos");
			this.position.x = pos.x;
			this.position.y = pos.y;
			return(true);

		}
		Log.w("a","not updated");
		return (false);
	}

	public void setDirection(int n) {
		direction += n;
		direction %= 4;
		//if(n < 0){direction = (direction + n) % 4;}
		//else{direction = (direction + n) % 4;}
		if (direction == 0) {
			icon = MazeChars.ARROW_UP;
		} else if (direction == 1) {
			icon = MazeChars.ARROW_LEFT;
		} else if (direction == 2) {
			icon = MazeChars.ARROW_DOWN;
		} else if (direction == 3) {
			icon = MazeChars.ARROW_RIGHT;
		}
	}

	public void turnLeft(int n) {
		setDirection(n);
	}

	public void turnRight(int n) {
		setDirection(-n);
	}

	public boolean move(int n) {
		if(n==0){n=1;}
		Position pos = this.position;
		if (direction == 0) { // UP
			pos.y += n;
		} else if (direction == 1) { // Left
			pos.x -= n;
		} else if (direction == 2) { // Down
			pos.y -= n;
		} else if (direction == 3) { // Right
			pos.x += n;
		}
		return(setPosition(pos, false));
		//Log.e("h", pos.toString());
		//return false;
	}
}