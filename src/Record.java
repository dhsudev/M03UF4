import java.util.*;
import java.io.*;
public class Record {
	public static String fitxer = "records.csv";
	public static String laberint;
	public static String user;
	public static int intents;
	public static String previous;
	public Record(String laberint, String user, int intents)throws IOException{
		setLaberint(laberint);
		setUser(user);
		setIntents(intents);
		if(exists() > intents || (exists() == 0 && intents > 0)){
			deleteRecord();
			saveRecord();
		}
		//cleanRecords();
	}
	public Record(String laberint){
		setLaberint(laberint);
		setUser(",");
		setIntents(0);
	}
	public static  void setLaberint(String name){
		if(name.contains(".dat")){
			name.replace(".dat","");
		}
		// To not have problems reading the csv
		name.replace(",","");
		laberint = name;
	}

	
	public static  void setUser(String name){
		// To not have problems reading the csv
		name.replace(",","");
		user = name;
	}
	public static  void setIntents(int n){
		if(n > 0){intents = n;}
	}
	private void saveRecord() throws IOException{
		FileWriter output = new FileWriter(fitxer, true);
		output.write(laberint + "," + user + "," + intents + "\n");
		output.close();
	}
	private void deleteRecord() throws IOException{
		/*
		 * It's better to use a StringBuffer to store all the lines in the file
		 * because it's mutable
		 * 	String is not mutable and every time we modify it 
		 * 		we create a new object
		 * 	In this case, we could have a lot of data 
		 * 	so create two objects is a lot of memory
		*/
		
		if(previous == null){return;}
		BufferedReader input = new BufferedReader(new FileReader(fitxer));
		StringBuffer inputBuffer = new StringBuffer();
		String line;
		// TO DO: NOT PREVIOUS
		while ((line = input.readLine()) != null) {
			// If we reach the prev record, we skip it
			if(!line.equals(previous)){
				inputBuffer.append(line);
				inputBuffer.append('\n');
			}//else{System.out.println("WIL DELETE THIS LINE ->"+previous);}
			System.out.println(line);
		}
        input.close();
        // write the new string without the line OVER the file
        FileOutputStream output = new FileOutputStream(fitxer);
        output.write(inputBuffer.toString().getBytes());
        output.close();
	}
	public static int exists() throws IOException{
		BufferedReader input = new BufferedReader(new FileReader(fitxer));
		String line = input.readLine();
		while(true){
			line = input.readLine();
			if (line == null) {break;}
			String[] record = line.split(",");
			//System.out.println(Arrays.toString(record));
			if(record[0].equals(laberint)){
				previous = line;
				if(UtilString.esEnter(record[2])){
					input.close();
					return Integer.parseInt(record[2]);
				}
			}
		}
		input.close();
		return 0;
	}
	// Debug Function to clean the records file
	public static void cleanRecords()throws IOException{
		FileWriter output = new FileWriter(fitxer);
		output.write("Laberint,User,Intents\n");
		output.close();
	}
}