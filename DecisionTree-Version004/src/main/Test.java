package main;

//import tree.DecisionTree;
import tree.TreeBuilder;

public class Test {
	public static void main(String[] args) {
		//System.out.println("Hello world");
		//TreeBuilder
		TreeBuilder decisionTree = new TreeBuilder("data2.txt");
		decisionTree.inputData();
		decisionTree.process();
		//String s = "\"yes\"";
		//System.out.println(s);

	}


}
