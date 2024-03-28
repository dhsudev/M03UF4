import java.util.*;
import java.io.*;
public class Validador{
	private  char[][] mapa;
	private int ample;
	private int altura;
	private File fitxer;
	private List<Position> obstacles = new ArrayList<>();
	private Position inici;
	private List<Position> sortides = new ArrayList<>();
	public Validador(String file)throws IOException{
		if(setFitxer(file)){
			if(setMapa(getFitxer())){
				printMapa();
				System.out.println(sortides);
				if(checkSolution(inici, new Position(0,0))){System.out.println("Laberint válid!!!!");printMapa();}
				else{System.out.println("Laberint no válid: no té solució");}
			}
		}
		else{ // No era un nom valid
			System.out.println("\"" + file + "\"" + " no es un laberint o no está al directori corresponent.");
		}
	}
	public boolean checkSolution(Position pos, Position ant){
		System.out.println(pos);
		if(esSortida(pos)){return true;}
		if((esPared(pos) && !esInici(pos))|| esCantonada(pos) || esObstacle(pos)){return false;}
		if(pos.x < 0 || pos.y < 0){return false;}
		if (pos.equals(ant)) {
			return false; 
		}
		if(mapa[pos.x][pos.y] == ' '){return false;}
		mapa[pos.x][pos.y] = ' ';
		boolean up = checkSolution(new Position(pos.x, pos.y+1), pos);
		boolean down = checkSolution(new Position(pos.x, pos.y-1), pos);
		boolean left = checkSolution(new Position(pos.x-1, pos.y), pos);
		boolean right = checkSolution(new Position(pos.x+1, pos.y), pos);
		return(up || down || left || right);
	}
	public boolean esInici(Position pos){
		return(inici.x == pos.x && inici.y == pos.y);
	}
	public boolean esSortida(Position pos){
		for (Position position : sortides) {
            if (position.equals(pos)) {return true;}
		}
		return false;
        
	}
	public void printMapa(){
		for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[i].length; j++) {
                System.out.print(mapa[i][j] + " ");
            }
            System.out.println(); // Saltar a la siguiente línea después de imprimir cada fila
        }
	}
	public boolean esObstacle(Position pos){
		return(obstacles.contains(pos));
	}
	public boolean esCantonada(Position pos){
		if(pos.x == 0){
			if(pos.y == 0 || pos.y == altura){return true;}
		}
		else if(pos.x == altura){
			if(pos.y == 0 || pos.y == altura){return true;}
		}
		return false;
	}
	public boolean esPared(Position pos){
		// En mig
		if((pos.x != ample -1 && pos.x != 0)&&(pos.y != altura -1 && pos.y != 0)){return false;}
		// Borde
		return true;
	}
	public boolean checkLineInfo(String line, int y){
		for (int x = 0; x < line.length(); x++){
			Position pos = new Position(x,y);
			switch (line.charAt(x)){
				case 'X':
					// Check if obstacle (no pared)
					if (!esPared(pos)){
						obstacles.add(pos);
					}
					// Si es una pared o cantonada, omitim
					break;
				case 'E':
					// Check if esta en el borde(no cantonada) i no esta definida ja
					if (esPared(pos) && !esCantonada(pos) && inici == null){
						inici = pos;
					} else{
						System.out.println("Laberint no válid: Les entrades están a posicions incorrectes o hi ha més d'una");
						return false;
					}
					break;
				case 'G':
					// Check if esta en el borde (no cantonada)
					if (esPared(pos) && !esCantonada(pos)){
						sortides.add(pos);
					}else {
						System.out.println("Laberint no válid: Les sortides están a posicions incorrectes");
						return false;
					}
					break;
				case '.':
					// Check si no esta al borde (tampoc cantonada)
					if (esPared(pos) || esCantonada(pos)){
						System.out.println("Laberint no válid: Les pareds estan incompletes");
						return false;
					}
					break;
			}
		}
		return true;
	}
	public boolean setMapa(File fitxer)throws IOException{
		if(checkSize(fitxer)){
			// Mapejar els valors
			this.mapa = new char [altura][ample];
			BufferedReader input = new BufferedReader(new FileReader(fitxer));
			String line = input.readLine();
			String validChars = "XEG.";
			for(int i = 0; i < altura; i++){
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
				mapa[i] = line.toCharArray();
				// Check 
			}
			if(inici == null){
				System.out.println("Laberint no válid: No hi ha entrada");
				return false;
			}
			if(sortides.isEmpty()){
				System.out.println("Laberint no válid: No hi ha sortida");
				return false;
			}
			return true;
		}
		else{return false;}
	}
	public void setDimensions(int altura, int ample){
		this.altura = altura;
		this.ample = ample;
	}
	public boolean checkSize(File fitxer) throws IOException{
		BufferedReader input = new BufferedReader(new FileReader(fitxer));
		String legend = input.readLine();
		String[] dimensions = legend.split("x");
		// Comprovar el format de la primera línea
		if(dimensions.length < 2 || !UtilString.esEnter(dimensions[0]) || !UtilString.esEnter(dimensions[1])){
			System.out.println("Laberint no válid: Les dimensions no tenen el format desitjat alturaXample");
			return false;
		}
		// Guardar valors
		int altura = UtilString.aEnter(dimensions[0]);
		int ample = UtilString.aEnter(dimensions[1]);
		// Comprovar tamany mínim
		if((altura < 2 || ample < 2) || (altura == 2 && ample == 2)){
			System.out.println("Laberint no válid: massa petit");
			return false;
		}
		// Comprovar que el laberint té el tamany especificat
		int count = 0;
		while (true) {
			count++;
			String linia = input.readLine();
			// System.out.println(count + ": " + linia);
			if (null == linia) {
				count--;
				break;
			}
			if(linia.length() != ample){
				System.out.println("Laberint no válid: el ample ("+ ample +") específicat no es correcte");
				return false;
			}
		}
		if(count != altura){
			System.out.println("Laberint no válid: l'altura especificada ("+ altura +") no es correcte");
			return false;
		}
		setDimensions(altura, ample);
		return true;
	}
	public File getFitxer(){return this.fitxer;}
	public boolean setFitxer(String filePath){
		filePath = "laberints/" + filePath;
		File prova = new File(filePath);
		// Intent 1: s'ha especificat l'extenció del fitxer
		if(prova.exists() && !prova.isDirectory()){
			this.fitxer = prova;
			return true;
		}
		prova = new File(filePath + ".dat");
		// Intent 2: no s'ha especificat l'extenció del fitxer
		if(prova.exists() && !prova.isDirectory()){
			this.fitxer = prova;
			return true;
		}
		// No era un fitxer vàlid
		return false;
	}
}