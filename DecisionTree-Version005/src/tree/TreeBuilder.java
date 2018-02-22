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

public class TreeBuilder {
	private double TP;
	private double FP;
	private double TN;
	private double FN;
	private double recall;
	private double F1score;
	private double accuracy;
	private double precision;

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


	private Node root;
	private Map<String, Integer> index_maping;


	public TreeBuilder(String path) {
		dataFile = new File(path);
	}



	public void inputData() {
		initializeDataStructure();
		String s = null;
		try {

			s= bufferedReader.readLine();
			String data[] = s.split(" ");
			attributeName = new Data(data);
			index_maping = new HashMap<>();

			for(int i = 0; i < data.length; i++)
				index_maping.put(data[i], i);

			String no = "no";
			String yes = "yes";


			while ((s = bufferedReader.readLine()) != null) {

				String dataX[] = s.split(" ");
				if (dataX[dataX.length - 1].equals(yes))
					yesCollection.add(new Data(dataX));
				else
					noCollection.add(new Data(dataX));
			}
			//System.out.println(yesCollection.size());
			//System.out.println(noCollection.size());

			//totalYes = (yesCollection.size() / 10) * 9;
			//totalNo = (noCollection.size() / 10) * 9;
			/*
			for(int i = 0; i < yesCollection.size(); i++)
			{
				for(int j = 0; j < yesCollection.get(i).getColumnSize(); j++)
					System.out.print(yesCollection.get(i).getValueInIndex(j)+", ");
				System.out.println();
			}*/



		} catch (IOException e) {
			System.out.println("got error in reading data using bufferReader");
		}
	}

	private void result() {
		accuracy = (TP + TN) / (TP + TN + FP + FN);
		precision = TP / (TP + FP);
		recall = TP / (TP + FN);
		F1score = (2 * precision * recall) / (precision + recall);
		System.out.println();
		System.err.println("Total TRUE-POSITIVE   : " + TP);
		System.err.println("Total TRUE-NEGATIVE   : " + TN);
		System.err.println("Total FALSE-POSITIVE  : " + FP);
		System.err.println("Total FALSE-NEGATIVE  : " + FN);
		System.out.println("-------------------------------------");
		System.err.println("Accuracy              : " + accuracy);
		System.err.println("precision             : " + precision);
		System.err.println("recall                : " + recall);
		System.err.println("F1 score              : " + F1score);
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
		//for (int i = 0; i < 10; i++) {
			//System.out.println("validation no " + i + " is processing.");
			trainingData = new ArrayList<>();
			testingData = new ArrayList<>();

			//for(int index = 0; index < yesCollection.size(); index++)
			//	trainingData.add(yesCollection.get(index));
			trainingData.addAll(yesCollection);
			trainingData.addAll(noCollection);

			//for(int i = 0; i < attributeName.getColumnSize(); i++)
			//	System.err.println(index_maping.get(attributeName.getValueInIndex(i)));


			root =  builtTree(trainingData, attributeName);



			//System.out.println(trainingData.size());


			/*
			for(int index = 0; index < noCollection.size(); index++) {
				trainingData.add(noCollection.get(index));
			}*/

			//System.out.println(trainingData.size());



			//selectRandomTrainData(allTestedYes, yesCollection);
			//selectRandomTrainData(allTestedNo, noCollection);


			/*
			writeDataIntoTempFile();

			decisionTree = new com.machine.learning.decisiontrees.DecisionTree();
			decisionTree.train(new File("dataTemp.psv"));
			System.out.println("tree completed");
			testWithData();

			*/
		//}
		//result();
	}

	private Node builtTree(ArrayList<Data> data_set, Data header) {
		int my_row = data_set.size();
		int my_colomn = data_set.get(0).getColumnSize();

		ArrayList<String> class_values = new ArrayList<>();

		for(int i = 0; i < my_row; i++)
			class_values.add(data_set.get(i).getValueInIndex(my_colomn - 1));
		double previous_information = calculateInformation(my_row, class_values);
		double smallest_information = previous_information;

		ArrayList<String> value_of_this_node = new ArrayList<>();
		Map<String, Integer> row_count = new HashMap<>();
		int selected_index = -1;


		for(int i = 0; i < my_colomn - 1; i++) {  //col - 1
			double post_information = 0.0;
			Map<String, Integer> row_count_temp = new HashMap<String, Integer>();
			ArrayList<String> values = new ArrayList<String>();
			for(int j = 0; j < my_row; j++)
				if(!values.contains(data_set.get(j).getValueInIndex(i)))
					values.add(data_set.get(j).getValueInIndex(i));

			for(int j = 0; j < values.size(); j++)
			{
				ArrayList<String> temp_class_value = new ArrayList<>();

				for(int k = 0; k < my_row; k++)
					if(values.get(j).equals(data_set.get(k).getValueInIndex(i)))
						temp_class_value.add(data_set.get(k).getValueInIndex(my_colomn - 1));
				//System.out.println(temp_class_value.size());
				post_information += (double) temp_class_value.size()/my_row * calculateInformation(temp_class_value.size(), temp_class_value);
				row_count_temp.put(values.get(j), temp_class_value.size());
			}

			//System.out.println(post_information);

			if(post_information < smallest_information) {
				selected_index = i;
				smallest_information = post_information;
				value_of_this_node = values;
				row_count = row_count_temp;
			}
		}


		if (selected_index != -1) {

			Node temp_node = new Node();
			temp_node.index_of_attribute = index_maping.get(header.getValueInIndex(selected_index));
			temp_node.name_of_attribute = header.getValueInIndex(selected_index);
			temp_node.collection_of_condition = value_of_this_node;

			for(int i = 0; i < temp_node.collection_of_condition.size(); i++)
				System.out.println(temp_node.collection_of_condition.get(i));

			//System.out.println(temp_node.name_of_attribute);
			//System.out.println(temp_node.index_of_attribute);
			String[] temp_header= new String[my_colomn - 1];
			int index_count = 0;
			for(int i = 0; i < header.getColumnSize(); i++)
			{
				if(i == selected_index) continue;
				temp_header[index_count] = header.getValueInIndex(i);
				index_count++;
			}

			Data header_of_nested_tree = new Data(temp_header);
			ArrayList<Data> nested_data;// = new ArrayList<>();


			//for(int ll = 0; ll < )

			for(int i = 0; i < my_row; i++)
			{
				String[] temp_arr = new String[my_colomn-1];
				int col_count = 0;
				for(int j = 0; j < my_colomn; j++)
				{
					if(col_count == selected_index) continue;
					temp_arr[col_count] = data_set.get(i).getValueInIndex(j);
					col_count++;
				}

				//nested_data.add(new Data(temp_arr));
			}

			//System.out.println(nested_data.size());
			/*for(int i = 0; i < nested_data.size(); i++)
			{
				for(int j =0; j < )
			}*/



		}



		//System.out.println(previous_information);


		//System.out.println(class_values.size());
		/*for(int i = 0; i < my_row; i++)
			System.out.println(class_values.get(i));
		*/



		return null;
	}



	/*

	private void testWithData() {
		for (int i = 0; i < testingData.size(); i++) {
			String arrTemp[] = convertArray(testingData.get(i));
			String previousAns = testingData.get(i).getValueInIndex(testingData.get(i).getColumnSize() - 1);
			String predectedAns = decisionTree.classify(arrTemp);
			if (previousAns.equals("won") && predectedAns.equals("won"))
				TP++;
			if (previousAns.equals("won") && predectedAns.equals("nowin"))
				FN++;
			if (previousAns.equals("nowin") && predectedAns.equals("nowin"))
				TN++;
			if (previousAns.equals("nowin") && predectedAns.equals("won"))
				FP++;
		}
	}*/

	private double calculateInformation(int number_of_values, ArrayList<String> values) {
		ArrayList<String> unique_values = new ArrayList<>();
		Map<String, Integer> count_of_unique_value = new HashMap<String, Integer>();

		for(int i = 0; i < number_of_values; i++)
		{
			String this_string = values.get(i);
			if(!unique_values.contains(this_string)){
				count_of_unique_value.put(this_string, 1);
				unique_values.add(this_string);
			}
			else count_of_unique_value.put(this_string, count_of_unique_value.get(this_string)+1);
		}

		double entropy = 0.0;
		if(values.size() == 1) return entropy;
		for(int i = 0; i < unique_values.size(); i++)
		{
		    	double probability = (double) count_of_unique_value.get(unique_values.get(i)) / number_of_values;
		    	entropy += probability * log(probability, 2);
		 }
		 entropy *= -1;
		 return entropy;
	}


	private double log(double x, int base) {
	    return  (Math.log(x) / Math.log(base));
	}


	private String[] convertArray(Data tempData) {
		String[] arrTemp = new String[tempData.getColumnSize() - 1];
		for (int i = 0; i < tempData.getColumnSize() - 1; i++)
			arrTemp[i] = tempData.getValueInIndex(i);
		return arrTemp;
	}
	/*

	private void writeDataIntoTempFile() {
		tempFile = new File("dataTemp.psv");
		try {
			fileWriter = new FileWriter(tempFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		printWriter = new PrintWriter(fileWriter);
		writeData(new Data(arr));
		for (int i = 0; i < trainingData.size(); i++)
			writeData(trainingData.get(i));

		System.out.println("data has been written in temp file :)");
		printWriter.close();
		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	/*
	private void writeData(Data myData) {
		printWriter.print("name|");
		for (int i = 0; i < myData.getColumnSize() - 1; i++)
			printWriter.print(myData.getValueInIndex(i) + "|");
		printWriter.println(myData.getValueInIndex(myData.getColumnSize() - 1));

	}*/


	/*

	private void selectRandomTrainData(ArrayList<Integer> allTested, ArrayList<Data> dataList) {
		ArrayList<Integer> temp = new ArrayList<>();
		while (temp.size() < dataList.size() / 10 - 9) {
			int x = new Random().nextInt(dataList.size());
			if (!allTested.contains(x)) {
				temp.add(x);
				allTested.add(x);
			}
		}

		for (int i = 0; i < dataList.size(); i++) {
			if (temp.contains(i))
				testingData.add(dataList.get(i));
			else
				trainingData.add(dataList.get(i));
		}
	}*/
}
