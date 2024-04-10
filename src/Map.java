import java.util.*;
import java.io.*;
public class Map{
	private final String TAG = "Laberint invàlid";
	public boolean valid = false;
	private  char[][] matrix;
	// Dimensions
	private int width;
	private int height;
	// Nom laberint 
	private File fitxer;
	// Variables per guardar posicions rellevants
	private static List<Position> blocks = new ArrayList<>();
	private Position door;
	private List<Position> exits = new ArrayList<>();
	private List<Position> walked = new ArrayList<>();
	public String name = "";
	public Map(String file) throws IOException{
		if(setFile(file)){
			if(setMap(getFitxer())){
				if(checkSolution(door)){
					//System.out.println("Laberint válid!");
					this.valid = true;
				}
				else{Log.error(TAG, "No té solució");}
			}
		}
		else{ // No era un nom valid
			Log.error(TAG, String.format("%s no és un laberint o no està al directory corresponent\n", file));
		}
	}
	public boolean checkSize(File fitxer) throws IOException{
		BufferedReader input = new BufferedReader(new FileReader(fitxer));
		String legend = input.readLine();
		String[] dimensions = legend.split("x");
		// Comtestr el format de la primera línea
		if(dimensions.length < 2 || !UtilString.esEnter(dimensions[0]) || !UtilString.esEnter(dimensions[1])){
			Log.error(TAG,"Les dimensions no tenen el format desitjat heightXwidth");
			return false;
		}
		// Guardar valors
		int height = UtilString.aEnter(dimensions[0]);
		int width = UtilString.aEnter(dimensions[1]);
		// Comprovar tamany mínim
		if((height < 2 || width < 2) || (height == 2 && width == 2)){
			Log.error(TAG,"massa petit");
			return false;
		}
		// Comprovar que el laberint té el tamany especificat
		int count = 0;
		while (true) {
			count++;
			String line = input.readLine();
			// System.out.println(count + ": " + line);
			if (null == line) {
				count--;
				break;
			}
			// Ample inválid (no és l'indicat o hi ha línies incompletes)
			if(line.length() != width){
				Log.error(TAG, "el ample ("+ width +") específicat no es correcte");
				return false;
			}
		}
		// Alçada inválida
		if(count != height){
			Log.error(TAG, "l'altura especificada ("+ height +") no es correcte");
			return false;
		}
		setDimensions(height, width);
		return true;
	}
	/*
	 * Rep una línea del fitxer i guarda les dades rellevants d'aquesta
	 */
	public boolean checkLineInfo(String line, int y){
		for (int x = 0; x < line.length(); x++){
			Position pos = new Position(x,y);
			switch (line.charAt(x)){
				case 'X': // Paret o obstacle
					// Check if obstacle (no pared)
					if (!isWall(pos)){
						blocks.add(pos);
					}
					// Si es una pared o cantonada, omitim
					break;
				case 'E': // Entrada
					// Check if esta en el borde(no cantonada) i no esta definida ja
					if (isWall(pos) && !isCorner(pos) && door == null){
						door = pos;
					} else{
						Log.error(TAG, "Les entrades están a posicions incorrectes o hi ha més d'una");
						return false;
					}
					break;
				case 'G': // Sortides
					// Check if esta en el borde (no cantonada)
					if (isWall(pos) && !isCorner(pos)){
						exits.add(pos);
					}else {
						Log.error(TAG, "Les exits están a posicions incorrectes");
						return false;
					}
					break;
				case '.': // Lliure
					// Check si no esta al borde (tampoc cantonada)
					if (isWall(pos) || isCorner(pos)){
						Log.error(TAG, "Les pareds estan incompletes");
						return false;
					}
					break;
			}
		}
		return true;
	}
	/*
	 * Funció recursiva per comprovar si un laberint té sol·lucions 
	 * mitjançant backtracking
	 * La primera trucada es desde la entrada. 
	 * Des d'allà prova totes les pocicions adjaçents i es crida a si mateixa
	 * En cas de acabar en una casella no válida, torna a l'última trucada válida
	 * El cas casos base són:
	 * 	 - Arribar a la sortida
	 * 	 - Haber recorregut tot el mapa (no té sol·lució)
	 */
	public boolean checkSolution(Position pos){
		if(isExit(pos)){return true;}
		if((isWall(pos) && !isDoor(pos))|| isCorner(pos) || isBlock(pos)){return false;}
		if(pos.x < 0 || pos.y < 0){return false;}
		//if (pos.equals(ant)) {return false;}
		if(isWalked(pos)){return false;}
		walked.add(pos);
		boolean up = checkSolution(new Position(pos.x, pos.y+1));
		boolean down = checkSolution(new Position(pos.x, pos.y-1));
		boolean left = checkSolution(new Position(pos.x-1, pos.y));
		boolean right = checkSolution(new Position(pos.x+1, pos.y));
		return(up || down || left || right);
	}
	// Per comprovar si la funció per checkejar la sol·lució 
	// ja ha passat per una posició
	public boolean isWalked(Position pos){
		for (Position position : walked) {
            if (position.equals(pos)) {return true;}
		}
		return false;
	}
	/*
	 * Funcions que comproven si una posició es rellevant,
	 * paret, block, sortida, entrada ...
	 */
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
		if((pos.x < width-1  && pos.x > 0)&&(pos.y < height-1 && pos.y > 0)){return false;}
		// Borde
		return true;
	}
	/*
	 * Comprova si el nom del laberint es válid
	 */
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
	/*
	 * Lectura del laberint, comprova que:
	 * 		- hi hagi un charset adecuat
	 * 		- Les sortides están a les parets (amb la func lineinfo)
	 * 		- Hi ha una única entrada ubicada a una paret (amb la func lineinfo)
	 * Va llegint i si troba algun error deixa de llegir.
	 */
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
						Log.error(TAG, "no utilitza un charSet adequat");
						return false;
					}
				}
				if(!checkLineInfo(line, i)){return false;}
				matrix[i] = line.toCharArray();
				// Check 
			}
			if(door == null){
				Log.error(TAG, "No hi ha entrada");
				return false;
			}
			if(exits.isEmpty()){
				Log.error(TAG, "No hi ha sortida");
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
	/*
	 * Getters de les variables de classe privades necessitades a fora de la classe
	 */
	public File getFitxer(){return this.fitxer;}
	public char[][] getMap(){return this.matrix;}
	public List<Position> getExits(){return this.exits;}
	public Position getDoor(){return this.door;}
	public static List<Position> getBlocks(){return blocks;}
	public boolean isValid(){return this.valid;}
	public int getHeight(){return this.height;}
	public int getWidth(){return this.width;}
}