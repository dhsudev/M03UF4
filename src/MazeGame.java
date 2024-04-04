import java.util.*;
import java.io.*;

public class MazeGame{
	private static List<Position> visibleBlocks = new ArrayList<>();
	private static List<Position> walked = new ArrayList<>();
	
	public static void main(String[] args)throws IOException{
		if(args.length < 1){
			System.out.println("No s'ha especificat nom del laberint");
			return;
		}
		Map map = new Map(args[0]);
		if(!map.isValid()){
			return;
		}
		Gamer gamer = new Gamer(map.getDoor());
		Record record = new Record(args[0]);
		if(record.exists() < 1){printHeader(record.laberint, "Encara no resolt");}
		else{
			String[] values = record.previous.split(",");
			printHeader(map.name, String.format("Rècord actual: %s en %s intents", record.laberint, values[2]));
		}
		//record.cleanRecords();
		//printMap(map, gamer);
		//printHelp();
		
	}
	public static void printHeader(String laberint, String intents){
		System.out.printf("Joc del laberint\n================\nH: mostra ajuda\n\nLaberint: %s\n%s\n", laberint, intents);
	}
	public static void printHelp(){
		System.out.println("Les opcions disponibles són:\n" + //
						"H: Mostra aquest text d'ajuda\n" + //
						"L: gira a l'esquerra\n" + //
						"R: gira a la dreta\n" + //
						"F: mou una passa endavant\n" + //
						"nF: mou n passes endavant\n" + //
						"Q: Sortir");
	}
	public static void printMap(Map mapUtils, Gamer gamer){
		char[][] map = mapUtils.getMap();
		//MazeChars MazeChars = new MazeChars;
		for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
				Position pos = new Position(x,y);
				if(pos.equals(gamer.position)){System.out.print(gamer.icon);}
				else if(mapUtils.isExit(pos)){System.out.print(" ");}
				else if(mapUtils.isDoor(pos)){
					System.out.print(' ');
				}else if(mapUtils.isCorner(pos)){
					if(pos.equals(new Position(0,0))){System.out.print(MazeChars.CORNER_UL);}
					else if(pos.equals(new Position(mapUtils.getWidth()-1,0))){System.out.print(MazeChars.CORNER_UR);}
					else if(pos.equals(new Position(0,mapUtils.getHeight()-1))){System.out.print(MazeChars.CORNER_DL);}
					else if(pos.equals(new Position(mapUtils.getWidth()-1,mapUtils.getHeight()-1))){System.out.print(MazeChars.CORNER_DR);}
				}else if(mapUtils.isWall(pos)){
					if(y == 0 || y == mapUtils.getHeight()-1){System.out.print(MazeChars.LIMIT_H);}
					else{System.out.print(MazeChars.LIMIT_V);}
				}else if(mapUtils.isBlock(pos)){
					if(isVisibleBlock(pos)){System.out.print(MazeChars.WALL);}
					else if(isWalked(pos)){System.out.print(' ');}
					else{System.out.print(MazeChars.EMPTY);}
				}else{System.out.print(MazeChars.EMPTY);}
				// Add separations (' ' or '─' deppending if it's in wall or not) to make it look like a square
                if((pos.y == 0 || pos.y == mapUtils.getHeight()-1) && pos.x < mapUtils.getWidth()-1){System.out.print(MazeChars.LIMIT_H);}
				else{System.out.print(" ");}
            }
            System.out.println();
        }
	}
	public static boolean isWalked(Position pos){
		if(walked.size() == 0){return false;}
		for (Position position : walked) {
            if (position.equals(pos)) {return true;}
		}
		return false;
	}
	public static boolean isVisibleBlock(Position block){
		if(visibleBlocks.size() == 0){return false;}
		for (Position position : visibleBlocks) {
            if (position.equals(block)) {return true;}
		}
		return false;
	}
}