package tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.sound.midi.Synthesizer;

//import All.DecisionTree;

public class TreeBuilder {
	private File dataFile;
	private int totalYes;
	private int totalNo;
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	private ArrayList<Data> yesCollection;
	private ArrayList<Data> noCollection;
	private Data attributeName;
	private ArrayList<Integer> allTestedYes;
	private ArrayList<Integer> allTestedNo;
	private ArrayList<Data> trainingData;
	private ArrayList<Data> testingData;
	private File tempFile;
	private FileWriter fileWriter;
	private PrintWriter printWriter;
	private String[] arr = {"bkblk","bknwy","bkon8","bkona","bkspr","bkxbq","bkxcr","bkxwp","blxwp","bxqsq","cntxt","dsopp","dwipd",
			 "hdchk","katri","mulch","qxmsq","r2ar8","reskd","reskr","rimmx","rkxwp","rxmsq","simpl","skach","skewr",
			 "skrxp","spcop","stlmt","thrsk","wkcti","wkna8","wknck","wkovl","wkpos","wtoeg", "result"};
	private com.machine.learning.decisiontrees.DecisionTree decisionTree;
	//private Node root;

	public TreeBuilder(String filePath) {
		dataFile = new File(filePath);
	}

	public void inputData() {
		initializeDataStructure();
		String s = null;
		try {
			/*s = bufferedReader.readLine();
			String data[] = s.split(";");
			attributeName = new Data(data);*/
			/*String no = "\"no\"";
			String yes = "\"yes\"";*/
			String no = "won";
			String yes = "nowin";

			while ((s = bufferedReader.readLine()) != null) {
				String dataX[] = s.split(",");
				if(dataX[dataX.length - 1].equals(yes)) yesCollection.add(new Data(dataX));
				else noCollection.add(new Data(dataX));
			}
			totalYes = (yesCollection.size()/10)*9;
			totalNo = (noCollection.size()/10)*9;

		} catch (IOException e) {
			System.out.println("got error in reading data using bufferReader");
		}
	}

	private void initializeDataStructure() {
		yesCollection = new ArrayList<Data>();
		noCollection = new ArrayList<Data>();
		try {
			fileReader = new FileReader(dataFile);
		} catch (FileNotFoundException e) {
			System.out.println("file not Found");
		}
		bufferedReader = new BufferedReader(fileReader);
	}

	public void process() {
		allTestedYes = new ArrayList<>();
		allTestedNo = new ArrayList<>();
		crossValidation();
	}

	private void crossValidation() {

		/////////////
		trainingData = new ArrayList<>();
		testingData = new ArrayList<>();

		selectRandomTrainData(allTestedYes, yesCollection);
		selectRandomTrainData(allTestedNo, noCollection);
		System.out.println(trainingData.size());
		System.out.println(testingData.size());

		writeDataIntoTempFile();

		/*com.machine.learning.decisiontrees.DecisionTree*/ decisionTree = new com.machine.learning.decisiontrees.DecisionTree();
		decisionTree.train( new File("dataTemp.psv"));

		//DecisionTree decisionTree = new DecisionTree();
		//decisionTree.train(new File("dataTemp.psv"));
		//System.out.println(arr.length);
		System.out.println("tree completed");

		testWithData();
		//for(int i = 0; i < )


	}


	private void testWithData() {

		//
		//Data tempData = testingData.get(0);
		//String[] arrTemp = new String[tempData.getColumnSize() - 1];
		//for(int i = 0 ;i < tempData.getColumnSize()- 1; i++)
		//	arrTemp[i] = tempData.getValueInIndex(i);

		//for(int i = 0 ;i < tempData.getColumnSize()- 1; i++)
		//	System.out.print(arrTemp[i] + " ");
		//System.out.println(arrTemp.length);


		for(int i = 0; i < testingData.size(); i++)
		{
			String arrTemp[] = convertArray(testingData.get(i));
			System.out.println(decisionTree.classify(arrTemp) + ", real ans > " + testingData.get(i).getValueInIndex(testingData.get(i).getColumnSize()-1));
		}

	}

	private String[] convertArray(Data tempData) {
		String[] arrTemp = new String[tempData.getColumnSize() - 1];
		for(int i = 0 ;i < tempData.getColumnSize()- 1; i++)
			arrTemp[i] = tempData.getValueInIndex(i);
		return arrTemp;

	}

	private void writeDataIntoTempFile() {
		tempFile  = new File("dataTemp.psv");
		try {
			fileWriter = new FileWriter(tempFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printWriter = new PrintWriter(fileWriter);
		// write
		writeData(new Data(arr));
		for(int i = 0; i < trainingData.size(); i++)
			writeData(trainingData.get(i));

		System.out.println("data has been written in temp file :)");
		printWriter.close();
		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeData(Data myData) {
		printWriter.print("name|");
		for(int i = 0; i < myData.getColumnSize() - 1; i++)
			printWriter.print(myData.getValueInIndex(i)+"|");
		printWriter.println(myData.getValueInIndex(myData.getColumnSize()-1));

	}

	/*
	private void builtTree(int row, int columnSize, ArrayList<Data> trainingData, Node currentNode, Data attributeName) {
		currentNode = new Node();
		currentNode.setIndex(6);
		System.out.println(currentNode.index);

	}*/

	private void selectRandomTrainData(ArrayList<Integer> allTested, ArrayList<Data> dataList) {
		ArrayList<Integer> temp = new ArrayList<>();
		while(temp.size() < dataList.size()/10-9){
			int x = new Random().nextInt(dataList.size());
			if(!allTested.contains(x)) {
				temp.add(x);
				allTested.add(x);
			}
		}

		for(int i = 0; i < dataList.size(); i++) {
			if(temp.contains(i)) testingData.add(dataList.get(i));
			else trainingData.add(dataList.get(i));
		}
	}
}
