package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DecisionTree {
	
	public void test(String filePath) throws IOException{
		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		BufferedReader breader = new BufferedReader(fileReader);
		
		int counter = 0;
		ArrayList<String> allData = new ArrayList<>();
		
		String line;
		while ((line = breader.readLine()) != null) {
			allData.add(line);
			//counter++;
		}
		
		System.out.println(allData.size());
		
		
	}
}
