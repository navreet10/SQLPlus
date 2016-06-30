package SQLPlus;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


import DBUtility.DBUtility;

public class DBApp {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Welcome!");
		System.out.println("Enter Username:");
		String userName = in.next();
		System.out.println("Enter Password:");
		String pwd = in.next();
		
		in.nextLine();
		try {
			while(true) {
				System.out.println("Please enter the query you want to execute or "
						+ "enter 'files' to add and run previously saved queries "
						+ "or enter 1 to exit:");
				String query = in.nextLine();
				if (query.equals("1")) {
					break;
				} else if (query.equals("files")) {
					processFiles(in,userName, pwd);
					continue;
				} else {
					runQuery(in,query,userName,pwd);
				}
				
				
			}
		} catch (IOException e) {
			System.out.println("IO exception");
			e.printStackTrace();
		}
		
		
		in.close();
	}

	private static void runQuery(Scanner in, String query, String userName, String pwd) {
		query.trim();
		System.out.println("Please enter the parameters and press Q when finished:");
		List<String> params = new ArrayList<String>();
		String responce = in.nextLine();
		int i =0;
		while(!responce.equals("Q")) {
			params.add(i,responce);
			i++;
			responce = in.nextLine();
		}
		System.out.println("Please enter the parameter types and press Q when finished:");
		List<String> types = new ArrayList<String>();
		responce = in.nextLine();
		i =0;
		while(!responce.equals("Q")) {
			types.add(i,responce);
			i++;
			responce = in.nextLine();
		}
		if (query.split(" ")[0].equalsIgnoreCase("select")) {
			Map<Integer,List<String>> map = DBUtility.selectData(query, params, types, userName, pwd);
			int size = map.size();
			if (map.size() > 10) {
				size =10;
			}
			for(int y=0; y<size;y++) {
				for (String s: map.get(y)) {
					System.out.printf("%s\t",s);
				}
				System.out.println();
			}
			if (map.keySet().size() >10) {
				File file = new File("Data");
				if(!file.exists()) {
					try {
						file.createNewFile();
						FileWriter fwr = new FileWriter(file);
						BufferedWriter bwr = new BufferedWriter(fwr);
						for(int y=0; y<map.keySet().size();y++) {
							for (String s: map.get(y)) {
								bwr.write(s+ "\t");
							}
							bwr.write("\n");
						}
						bwr.close();
						fwr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			} else {
			int res = DBUtility.updateData(query, params, types, userName, pwd);
			if (res ==0) {
				System.out.println("No row changed");
			} else {
				System.out.println("Successful");
			}
		}
		
	}

	private static void processFiles(Scanner in, String userName, String pwd) throws IOException {
		System.out.println("Please enter (1) to add file or enter (2) to run previous files");
		String response = in.next();
		in.nextLine();
		if (response.equals("1")) {
			System.out.println("Please enter the file name:");
			response = in.next();
			in.nextLine();
			File file = new File("/home/oracle/workspace/SQLPlus/SQLFiles/"+response + ".txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fwr = new FileWriter(file, true);
			BufferedWriter bwr = new BufferedWriter(fwr);
			while (true) {
				System.out.println("please enter the name of the query and the query separated by (|)"
						+ "or enter (1) to exit");
				response = in.nextLine();
				if (response.equals("1")) 
					break;				
				bwr.write("|" + response);				
			}
			bwr.close();
			fwr.close();
			
		} else {
			System.out.println("Please enter the file name you want to run:");
			File folder = new File("/home/oracle/workspace/SQLPlus/SQLFiles");
			File[] files = folder.listFiles();
			for (File file: files) {
				System.out.println(file.getName());
			}
			response = in.next();
			in.nextLine();
			Scanner readFile = new Scanner(new File("/home/oracle/workspace/SQLPlus/SQLFiles/"+response + ".txt"));
			
			readFile.useDelimiter("\\|");
			Map<String,String> mapQuery = new HashMap<String,String>();
			while(readFile.hasNext()) {	
				/*String[] line = readFile.nextLine().split("|");
				String key = line[0];
				String query = line[1];*/	
				String key = readFile.next();
				if (key.equals("")) {
					continue;
				}
				String query = readFile.next();
				if (query.equals("")) {
					continue;
				}
				mapQuery.put(key, query);
				System.out.println(key);
			}
			System.out.println("Enter the query name:");
			response = in.next();
			in.nextLine();
			runQuery(in,mapQuery.get(response),userName,pwd);
			readFile.close();
		}
	}

}
