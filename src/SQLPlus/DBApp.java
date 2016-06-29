package SQLPlus;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
		while(true) {
			System.out.println("Please enter the query you want to execute or 1 to exit:");
			String query = in.nextLine();
			if (query.equals("1")) {
				break;
			}
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
				for(int y=0; y<10;y++) {
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
		
		in.close();
	}

}
