import java.util.*;
import java.io.*;

public class MazeGame {
	private static List<Position> visibleBlocks = new ArrayList<>();
	public static List<Position> walked = new ArrayList<>();
	private static int tries = 1;
	private static final String TAG = "Moviment invàlid";
	public static void main(String[] args) throws IOException {
		boolean win = false;
		if (args.length < 1) {
			System.out.println("No s'ha especificat nom del laberint");
			return;
		}
		Map map = new Map(args[0]);
		if (!map.isValid()) {
			return;
		}
		Gamer gamer = new Gamer(map);
		//Log.d("Gamer:" + gamer.position + "Door" + map.getDoor());
		Record record = new Record(args[0]);
		if (record.exists() < 1) {
			printHeader(record.laberint, "Encara no resolt");
		} else {
			String[] values = record.previous.split(",");
			printHeader(map.name, String.format("Rècord actual: %s en %s intents", record.user, values[2]));
		}
		//gamer.setDirection(0);
		//gamer.turn(0);
		//gamer.move(tries);
		//System.out.println(gamer.direction);
		printMap(map, gamer);
		while (true) {
			String[] moves = getMove(record.laberint);
			// Log.d(Arrays.asList(moves).toString());
			if (moves.length == 1 && moves[0].matches("[0hq]||-1")) { // No era un moviment molt bo
				if (moves[0].equals("0")) {
					Log.error(TAG, "No vols fer res? Així no guanyaras!");
				} else if (moves[0].equals("h")) {
					printHelp();
				} else if (moves[0].equals("q")) {
					Log.yellow("Quina poca paciéncia!\n");
					System.exit(0);
				}
			} else {
				int times = 1;
				int digit = 1;
				int i = 0;
				//for (int i = 0; i < moves.length; i++) {
				while(i < moves.length){
					times = 1;
					digit = 0;
					while(UtilString.esEnter(moves[i])){
						times *= digit;
						times += UtilString.aEnter(moves[i]);
						digit *= 10;
						i++;
					}
					//System.out.println(gamer.direction);
					if (moves[i].equals("f")) {
						Position ant = new Position(gamer.position.x, gamer.position.y);
						// Check if it goes outside the map or the walls
						if(!gamer.move(times)){
							Log.warning(TAG, "El laberint no és tan gran flipat");
							//gamer.setPosition(ant, false);
							break;
						} else{
							List<Position> currentWalk = calcWalkedPos(ant, gamer);
							for(Position pos : currentWalk){
								if(!isWalked(pos)){walked.add(pos);}
							}
							if(checkColapse(currentWalk, map)){
								Log.cyan("Xoc!\n");
								tries ++;
								gamer = new Gamer(map);
							}
						}
					} else if (moves[i].equals("l")) {
						gamer.turn(times);
					} else if (moves[i].equals("r")) {
						gamer.turn(-times);
					}
					//Log.d(gamer.position.toString());	
					//System.out.println(Arrays.asList(walked));
					i++;
					
				}
				//System.out.println(gamer.direction);
				printMap(map, gamer);
				if(map.isExit(gamer.position)){
					win = true;
					break;
				}
			}
		}
		if(win){
			if(tries == 1){Log.green("Has resolt el laberint en 1 intent! Quin crack!");}
			else {Log.green("Has resolt el laberint en "+ tries +" intents!");}
			
		}
		if(record.maxRecord < 0 ||  tries < record.maxRecord){
			Log.cyan("\nNou rècord! Indica el teu nom: ");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String name = reader.readLine();
			reader.close();
			record = new Record(args[0], name, tries);
		}else{
			Log.yellow("No has superat el rècord. Potser la següent vegada.");
		}
		// printMap(map, gamer);

		// record.cleanRecords();
	}
	public static List<Position> calcWalkedPos(Position ant, Gamer gamer){
		int i;
		int max;
		List<Position> walkedPos = new ArrayList<>();
		if(ant.equals(gamer.position)){return walkedPos;}
		else if(ant.x != gamer.position.x){
			i = Math.min(ant.x, gamer.position.x);
			max = Math.max(ant.x, gamer.position.x);
			while(i <= max){
				Position pos = new Position(i, gamer.position.y);
				walkedPos.add(pos);
				i++;
			}
		} else {
			i = Math.min(ant.y, gamer.position.y);
			max = Math.max(ant.y, gamer.position.y);
			while(i <= max){
				Position pos = new Position(gamer.position.x, i);
				walkedPos.add(pos);
				i++;
			}
		}
		return walkedPos;
	}
	public static Boolean checkColapse(List<Position> walkedPos, Map map){
		//System.out.println("BLOCKS:"+Arrays.asList(Map.getBlocks()));
		//System.out.println("WALKED:"+Arrays.asList(walkedPos));
		for(Position pos : walkedPos){
			for(Position block : Map.getBlocks()){
				if(pos.equals(block)){
					visibleBlocks.add(block);
					return true;
				}
			}
		}
		return false;
	}
	public static String[] getMove(String name) throws IOException {
		String validMoves = "[0-9rlf]*";
		Log.prompt(name,tries);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String move = reader.readLine();
		// Clean line
		move = move.replace(" ", "");
		move = move.replace("	", "");
		move = move.toLowerCase();
		if (move.isEmpty() || move.isBlank()) {
			return new String[] { "0" };
		} else if (move.matches(validMoves)) {
			String[] moves = move.split("");
			// Check if a numbers are followed by a moves
			int i = 0;
			while (i < moves.length) {
				// Move current number
				while (moves[i].matches("[0-9]")) {
					i++;
					// If we are cheching num and we reach final string 
					// (there is no move for the last num)
					if (i >= moves.length) {
						Log.error(TAG, "Que fas posant números sense un moviment darrere?");
						return (new String[] { "-1" });
					}
				}
				// If we have something that is not a valid move
				if (!moves[i].matches(validMoves)) {
					Log.error(TAG, "Que fas posant números sense un moviment darrere?");
					return (new String[] { "-1" });
				}
				i++;
			}
			return (moves);
		} else if (move.equals("h") || move.equals("q")) {
			return (move.split(""));
		}
		Log.error(TAG,
				"Hi ha caràcters o combinacions no permeses\n==============================================================\n");
		printHelp();
		return (new String[] { "-1" });
	}

	public static void printHeader(String laberint, String intents) {
		Log.info(String.format("Joc del laberint\n================\nH: mostra ajuda\n\nLaberint: %s\n%s\n", laberint,
				intents));
	}

	public static void printHelp() {
		Log.info("Les opcions disponibles són:\n" + //
				"H: Mostra aquest text d'ajuda\n" + //
				"L: gira a l'esquerra\n" + //
				"R: gira a la dreta\n" + //
				"F: mou una passa endavant\n" + //
				"nF: mou n passes endavant\n" + //
				"Q: Sortir");
	}

	public static void printMap(Map mapUtils, Gamer gamer) {
		char[][] map = mapUtils.getMap();
		// MazeChars MazeChars = new MazeChars;
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				Position pos = new Position(x, y);
				if (pos.equals(gamer.position)) {
					Log.cyan(gamer.icon);
					//System.out.print(gamer.icon);
				} else if (mapUtils.isExit(pos)) {
					System.out.print(" ");
				} else if (mapUtils.isDoor(pos)) {
					System.out.print(' ');
				} else if (mapUtils.isCorner(pos)) {
					if (pos.equals(new Position(0, 0))) {
						Log.purple(MazeChars.CORNER_UL);
					} else if (pos.equals(new Position(mapUtils.getWidth() - 1, 0))) {
						Log.purple(MazeChars.CORNER_UR);
					} else if (pos.equals(new Position(0, mapUtils.getHeight() - 1))) {
						Log.purple(MazeChars.CORNER_DL);
					} else if (pos.equals(new Position(mapUtils.getWidth() - 1, mapUtils.getHeight() - 1))) {
						Log.purple(MazeChars.CORNER_DR);
					}
				} else if (mapUtils.isWall(pos)) {
					if (y == 0 || y == mapUtils.getHeight() - 1) {
						Log.purple(MazeChars.LIMIT_H);
					} else {
						Log.purple(MazeChars.LIMIT_V);
					}
				} else if (mapUtils.isBlock(pos)) {
					if (isVisibleBlock(pos)) {
						Log.pink(MazeChars.WALL);
					} else {
						System.out.print(MazeChars.EMPTY);
					}
				} else if(isWalked(pos)){ 
					System.out.print(MazeChars.WALKED);
				}else {
					System.out.print(MazeChars.EMPTY);
				}
				// Add separations (' ' or '─' deppending if it's in wall or not) to make it
				// look like a square
				if ((pos.y == 0 || pos.y == mapUtils.getHeight() - 1) && pos.x < mapUtils.getWidth() - 1) {
					Log.purple(MazeChars.LIMIT_H);
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}

	public static boolean isWalked(Position pos) {
		if (walked.size() == 0) {
			return false;
		}
		for (Position position : walked) {
			if (position.equals(pos)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isVisibleBlock(Position block) {
		if (visibleBlocks.size() == 0) {
			return false;
		}
		for (Position position : visibleBlocks) {
			if (position.equals(block)) {
				return true;
			}
		}
		return false;
	}
}