import java.util.*;
import java.io.*;
public class Map{
	boolean valid = false;
	private  char[][] matrix;
	private int width;
	private int height;
	private File fitxer;
	private List<Position> blocks = new ArrayList<>();
	private Position door;
	private List<Position> exits = new ArrayList<>();
	private List<Position> walked = new ArrayList<>();
	public Map(String file) throws IOException{
		if(setFile(file)){
			if(setMap(getFitxer())){
				System.out.println(exits);
				if(checkSolution(door, new Position(0,0))){
					System.out.println("Laberint válid!!!!");
					this.valid = true;
				}
				else{System.out.println("Laberint no válid: no té solució");}
			}
		}
		else{ // No era un nom valid
			System.out.println("\"" + file + "\"" + " no es un laberint o no está al directori corresponent.");
		}
	}
	public boolean checkSize(File fitxer) throws IOException{
		BufferedReader input = new BufferedReader(new FileReader(fitxer));
		String legend = input.readLine();
		String[] dimensions = legend.split("x");
		// Comtestr el format de la primera línea
		if(dimensions.length < 2 || !UtilString.esEnter(dimensions[0]) || !UtilString.esEnter(dimensions[1])){
			System.out.println("Laberint no válid: Les dimensions no tenen el format desitjat heightXwidth");
			return false;
		}
		// Guardar valors
		int height = UtilString.aEnter(dimensions[0]);
		int width = UtilString.aEnter(dimensions[1]);
		// Comtestr tamany mínim
		if((height < 2 || width < 2) || (height == 2 && width == 2)){
			System.out.println("Laberint no válid: massa petit");
			return false;
		}
		// Comtestr que el laberint té el tamany especificat
		int count = 0;
		while (true) {
			count++;
			String line = input.readLine();
			// System.out.println(count + ": " + line);
			if (null == line) {
				count--;
				break;
			}
			if(line.length() != width){
				System.out.println("Laberint no válid: el ample ("+ width +") específicat no es correcte");
				return false;
			}
		}
		if(count != height){
			System.out.println("Laberint no válid: l'altura especificada ("+ height +") no es correcte");
			return false;
		}
		setDimensions(height, width);
		return true;
	}
	public boolean checkLineInfo(String line, int y){
		for (int x = 0; x < line.length(); x++){
			Position pos = new Position(x,y);
			switch (line.charAt(x)){
				case 'X':
					// Check if obstacle (no pared)
					if (!isWall(pos)){
						blocks.add(pos);
					}
					// Si es una pared o cantonada, omitim
					break;
				case 'E':
					// Check if esta en el borde(no cantonada) i no esta definida ja
					if (isWall(pos) && !isCorner(pos) && door == null){
						door = pos;
					} else{
						System.out.println("Laberint no válid: Les entrades están a posicions incorrectes o hi ha més d'una");
						return false;
					}
					break;
				case 'G':
					// Check if esta en el borde (no cantonada)
					if (isWall(pos) && !isCorner(pos)){
						exits.add(pos);
					}else {
						System.out.println("Laberint no válid: Les exits están a posicions incorrectes");
						return false;
					}
					break;
				case '.':
					// Check si no esta al borde (tampoc cantonada)
					if (isWall(pos) || isCorner(pos)){
						System.out.println("Laberint no válid: Les pareds estan incompletes");
						return false;
					}
					break;
			}
		}
		return true;
	}
	public boolean checkSolution(Position pos, Position ant){
		if(isExit(pos)){return true;}
		if((isWall(pos) && !isDoor(pos))|| isCorner(pos) || isBlock(pos)){return false;}
		if(pos.x < 0 || pos.y < 0){return false;}
		if (pos.equals(ant)) {return false;}
		if(isWalked(pos)){return false;}
		walked.add(pos);
		boolean up = checkSolution(new Position(pos.x, pos.y+1), pos);
		boolean down = checkSolution(new Position(pos.x, pos.y-1), pos);
		boolean left = checkSolution(new Position(pos.x-1, pos.y), pos);
		boolean right = checkSolution(new Position(pos.x+1, pos.y), pos);
		return(up || down || left || right);
	}
	public boolean isWalked(Position pos){
		for (Position position : walked) {
            if (position.equals(pos)) {return true;}
		}
		return false;
	}
	public boolean isDoor(Position pos){
		return(door.equals(pos));
	}
	public boolean isExit(Position pos){
		for (Position position : exits) {
            if (position.equals(pos)) {return true;}
		}
		return false;
        
	}
	public boolean isBlock(Position pos){
		return(blocks.contains(pos));
	}
	public boolean isCorner(Position pos){
		if(pos.x == 0){
			if(pos.y == 0 || pos.y == height-1){return true;}
		}
		else if(pos.x == width-1){
			if(pos.y == 0 || pos.y == height-1){return true;}
		}
		return false;
	}
	public boolean isWall(Position pos){
		// En mig
		if((pos.x != width -1 && pos.x != 0)&&(pos.y != height -1 && pos.y != 0)){return false;}
		// Borde
		return true;
	}
	public boolean setFile(String filePath){
		filePath = "laberints/" + filePath;
		File test = new File(filePath);
		// Intent 1: s'ha especificat l'extensió del fitxer
		if(test.exists() && !test.isDirectory()){
			this.fitxer = test;
			return true;
		}
		test = new File(filePath + ".dat");
		// Intent 2: no s'ha especificat l'extensió del fitxer
		if(test.exists() && !test.isDirectory()){
			this.fitxer = test;
			return true;
		}
		// No era un fitxer vàlid
		return false;
	}
	public boolean setMap(File fitxer)throws IOException{
		if(checkSize(fitxer)){
			// Mapejar els valors
			this.matrix = new char [height][width];
			BufferedReader input = new BufferedReader(new FileReader(fitxer));
			String line = input.readLine();
			String validChars = "XEG.";
			for(int i = 0; i < height; i++){
				line = input.readLine();
				if (null == line) {break;}
				// Check if all the elements in the line are mazeChars
				for (char c : line.toCharArray()) {
					if(!validChars.contains(""+c)){
						System.out.println("Laberint no válid: no utilitza un charSet adequat");
						return false;
					}
				}
				if(!checkLineInfo(line, i)){return false;}
				matrix[i] = line.toCharArray();
				// Check 
			}
			if(door == null){
				System.out.println("Laberint no válid: No hi ha entrada");
				return false;
			}
			if(exits.isEmpty()){
				System.out.println("Laberint no válid: No hi ha sortida");
				return false;
			}
			return true;
		}
		else{return false;}
	}
	public void setDimensions(int height, int width){
		this.height = height;
		this.width = width;
	}
	public File getFitxer(){return this.fitxer;}
	public char[][] getMap(){return this.matrix;}
	public List<Position> getExits(){return this.exits;}
	public Position getDoor(){return this.door;}
	public List<Position> getBlocks(){return this.blocks;}
	public boolean isValid(){return this.valid;}
	public int getHeight(){return this.height;}
	public int getWidth(){return this.width;}
}