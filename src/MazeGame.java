import java.util.*;
import java.io.*;

public class MazeGame{
	public static void main(String[] args){
		if(args.length < 1){
			System.out.println("No s'ha especificat nom del laberint");
			return;
		}
		Validador validador = new Validador(args[0]);
	}

}