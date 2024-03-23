import java.util.*;
import java.io.*;
public class Validador{
	private  char[] mapa;
	private File fitxer;
	private int[][] obstacles;
	private int[] inici;
	private int[][] sortides;
	public Validador(String file){
		if(setFitxer(file)){
			//setMapa(getFitxer());
			
		}
		else{
			System.out.println("\"" + file + "\"" + " no es un laberint.");
		}
	}
	//public boolean setMapa(File fitxer){

	//}
	public File getFitxer(){return this.fitxer;}
	public boolean setFitxer(String filePath){
		filePath = "Laberints/" + filePath;
		File prova = new File(filePath);
		if(prova.exists() && !prova.isDirectory()){
			this.fitxer = prova;
			return true;
		}
		prova = new File(filePath + ".dat");
		if(prova.exists() && !prova.isDirectory()){
			this.fitxer = prova;
			return true;
		}
		return false;
	}

}