import java.util.*;
import java.io.*;

public class MazeGame{
	public static void main(String[] args)throws IOException{
		if(args.length < 1){
			System.out.println("No s'ha especificat nom del laberint");
			return;
		}
		Map map = new Map(args[0]);
		if(!map.isValid()){
			return;
		}
		
	}

}