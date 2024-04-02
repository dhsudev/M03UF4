public class Gamer {
	public Position position = new Position(0,0);
	/*
	 * 0 - N
	 * 1 - O
	 * 2 - S
	 * 3 - E
	 */
	public int direction = 0;
	public char icon;
	public Gamer(Position door){
		setPosition(door);
		setDirection(3);
	}
	public void setPosition(Position pos){
		this.position.x = pos.x;
		this.position.y = pos.y;
	}
	public void setDirection(int n){
		direction += n%4;
		if(direction == 0){icon = MazeChars.ARROW_UP;}
		else if(direction == 1){icon = MazeChars.ARROW_LEFT;}
		else if(direction == 2){icon = MazeChars.ARROW_DOWN;}
		else if(direction == 3){icon = MazeChars.ARROW_RIGHT;}
	}
	public void turnLeft(int n){
		setDirection(n);
	}
	public void turnRight(int n){
		setDirection(-n);
	}
}