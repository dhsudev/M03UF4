class Position {
    int x;
    int y;
	// Position entrada = new Position(0,0);
	// entrada.x
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
	
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || this == null) {
            return false;
        }
        Position position = (Position) obj;
        return this.x == position.x && this.y == position.y;
    }
	
}