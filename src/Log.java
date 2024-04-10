public class Log {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[34m";
    public static final String ANSI_PINK = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    //info
    public static void info(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }
	public static void green(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }
	public static void green(char ch) {
        System.out.print(ANSI_GREEN + ch + ANSI_RESET);
    }
    //error
    public static void error(String className, String message) {
        System.out.println(ANSI_PINK + className + " : " + message + ANSI_RESET);
    }

    //debug
    public static void debug(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }

    //warning
    public static void warning(String className, String message) {
        System.out.println(ANSI_YELLOW + className + " : " + message + ANSI_RESET);
    }
	public static void yellow(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }
	public static void yellow(char ch) {
        System.out.print(ANSI_YELLOW + ch + ANSI_RESET);
    }
	// prompt
	public static void prompt(String name, int tries) {
        System.out.print(ANSI_PURPLE + "╭─ " + ANSI_RESET + ANSI_YELLOW + " " + ANSI_RESET +
		ANSI_CYAN + " MazeGame" + ANSI_RESET + 
		ANSI_PURPLE + " ~ " + ANSI_RESET +
		ANSI_GREEN + name + " " + ANSI_RESET +
		ANSI_CYAN + tries + " tries\n" + ANSI_RESET +
		ANSI_PURPLE + "╰ " + ANSI_RESET
		);
    }
	public static void pink(String message) {
        System.out.print(ANSI_PINK + message + ANSI_RESET);
    }
	public static void pink(char ch) {
        System.out.print(ANSI_PINK + ch + ANSI_RESET);
    }
	public static void cyan(String message){
		System.out.print(ANSI_CYAN + message + ANSI_RESET);
	}
	public static void cyan(char ch) {
        System.out.print(ANSI_CYAN + ch + ANSI_RESET);
    }
	public static void purple(char ch) {
        System.out.print(ANSI_PURPLE + ch + ANSI_RESET);
    }
} 
