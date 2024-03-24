import java.util.*;
import java.io.*;
public class Validador{
	private  char[] mapa;
	private File fitxer;
	private int[][] obstacles;
	private int[] inici;
	private int[][] sortides;
	public Validador(String file)throws IOException{
		if(setFitxer(file)){
			setMapa(getFitxer());
		}
		else{ // No era un nom valid
			System.out.println("\"" + file + "\"" + " no es un laberint o no está al directori corresponent.");
		}
	}
	public boolean setMapa(File fitxer)throws IOException{
		if(checkSize(fitxer)){
			//return(checkSolution());
			return true;
		}
		else{return false;}
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