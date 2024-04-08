import java.util.*;
import java.io.*;

public class MazeGame {
	private static List<Position> visibleBlocks = new ArrayList<>();
	public static List<Position> walked = new ArrayList<>();
	private static int tries = 1;
	private static final String TAG = "Moviment invàlid";

	public static void main(String[] args) throws IOException {
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
			printHeader(map.name, String.format("Rècord actual: %s en %s intents", record.laberint, values[2]));
		}
		printMap(map, gamer);
		while (true) {
			String[] moves = getMove(record.laberint);
			// Log.d(Arrays.asList(moves).toString());
			if (moves.length == 1 && moves[0].matches("[0hq]||-1")) { // No era un moviment molt bo
				if (moves[0].equals("0")) {
					Log.e(TAG, "No vols fer res? Així no guanyaras!");
				} else if (moves[0].equals("h")) {
					printHelp();
				} else if (moves[0].equals("q")) {
					Log.exit("Quina poca paciéncia!\n");
					break;
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
					System.out.println(gamer.direction);
					if (moves[i].equals("f")) {
						Position ant = gamer.position;
						// Check if it goes outside the map or the walls
						if(!gamer.move(times) || (map.isWall(gamer.position))){
							Log.w(TAG, "El laberint no és tan gran flipat");
							//gamer.setPosition(ant, false);
							break;
						} else if(map.isBlock(gamer.position)){
							Log.w("", "Xoc!");
							tries ++;
							if(!isVisibleBlock(gamer.position)){visibleBlocks.add(new Position(gamer.position.x,gamer.position.y));}
							gamer = new Gamer(map);
						}
					} else if (moves[i].equals("l")) {
						gamer.turnLeft(times);
					} else if (moves[i].equals("r")) {
						gamer.turnRight(times);
					}
					Log.d(gamer.position.toString());
					if(!isWalked(gamer.position)){walked.add(new Position(gamer.position.x,gamer.position.y));}
					System.out.println(Arrays.asList(walked));
					i++;
					
				}
				System.out.println(gamer.direction);
				printMap(map, gamer);
				
			}
		}

		// printMap(map, gamer);

		// record.cleanRecords();
	}

	public static String[] getMove(String name) throws IOException {
		String validMoves = "[0-9rlf]*";
		Log.p(String.format("╭─   MazeGame ~ %s\n" + //
				"╰ ", name));
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
					// If we are cheching num and we reach final string (there is no move for the
					// last num)
					if (i >= moves.length) {
						Log.e(TAG, "Que fas posant números sense un moviment darrere?");
						return (new String[] { "-1" });
					}
				}
				// If we have something that is not a valid move
				if (!moves[i].matches(validMoves)) {
					Log.e(TAG, "Que fas posant números sense un moviment darrere?");
					return (new String[] { "-1" });
				}
				i++;
			}
			return (moves);
		} else if (move.equals("h") || move.equals("q")) {
			return (move.split(""));
		}
		Log.e(TAG,
				"Hi ha caràcters o combinacions no permeses\n==============================================================\n");
		printHelp();
		return (new String[] { "-1" });
	}

	public static void printHeader(String laberint, String intents) {
		Log.i(String.format("Joc del laberint\n================\nH: mostra ajuda\n\nLaberint: %s\n%s\n", laberint,
				intents));
	}

	public static void printHelp() {
		Log.d("Les opcions disponibles són:\n" + //
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
					System.out.print(gamer.icon);
				} else if (mapUtils.isExit(pos)) {
					System.out.print(" ");
				} else if (mapUtils.isDoor(pos)) {
					System.out.print(' ');
				} else if (mapUtils.isCorner(pos)) {
					if (pos.equals(new Position(0, 0))) {
						System.out.print(MazeChars.CORNER_UL);
					} else if (pos.equals(new Position(mapUtils.getWidth() - 1, 0))) {
						System.out.print(MazeChars.CORNER_UR);
					} else if (pos.equals(new Position(0, mapUtils.getHeight() - 1))) {
						System.out.print(MazeChars.CORNER_DL);
					} else if (pos.equals(new Position(mapUtils.getWidth() - 1, mapUtils.getHeight() - 1))) {
						System.out.print(MazeChars.CORNER_DR);
					}
				} else if (mapUtils.isWall(pos)) {
					if (y == 0 || y == mapUtils.getHeight() - 1) {
						System.out.print(MazeChars.LIMIT_H);
					} else {
						System.out.print(MazeChars.LIMIT_V);
					}
				} else if (mapUtils.isBlock(pos)) {
					if (isVisibleBlock(pos)) {
						System.out.print(MazeChars.WALL);
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
					System.out.print(MazeChars.LIMIT_H);
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