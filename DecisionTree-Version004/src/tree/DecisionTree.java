package tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DecisionTree {
	private File dataFile;
	private int totalYes;
	private int totalNo;
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	private ArrayList<Data> yesCollection;
	private ArrayList<Data> noCollection;
	private Map<String, ArrayList<Data>> listMap;
	private Data attributeName;

	public DecisionTree(String filePath) {
		dataFile = new File(filePath);
	}

	public void inputData() {
		initializeDataStructure();
		String s = null;
		try {
			s = bufferedReader.readLine();
			String data[] = s.split(";");
			attributeName = new Data(data);
			//for(int i = 0; i < data.length; i++)
			//	System.out.print(data[i] + " ");
			//System.out.println();

			s = bufferedReader.readLine();
			String dataX[] = s.split(";");
			//for(int i = 0; i < data.length; i++)
			//	System.out.print(dataX[i] + " ");
			String test = "\"no\"";

			if(dataX[20].equals(test)) System.out.println("yes");

			/*
			while ((s = bufferedReader.readLine()) != null) {
				String dataX[] = s.split(";");
				listMap.get(data[data.length - 1]).add(new Data(dataX));
			}*/
			//totalHam = (hamCollection.size()/10)*9;
			//totalSpam = (spamCollection.size()/10)*9;
			//System.out.println(yesCollection.size());

		} catch (IOException e) {
			System.out.println("got error in reading data using bufferReader");
		}
	}

	private void initializeDataStructure() {
		yesCollection = new ArrayList<Data>();
		noCollection = new ArrayList<Data>();
		listMap = new HashMap<String, ArrayList<Data>>();
		listMap.put("\"yes\"", yesCollection);
		listMap.put("\"no\"", noCollection);

		try {
			fileReader = new FileReader(dataFile);
		} catch (FileNotFoundException e) {
			System.out.println("file not Found");
		}
		bufferedReader = new BufferedReader(fileReader);
	}




}
