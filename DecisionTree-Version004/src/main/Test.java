package main;

/*
 *data url :: http://archive.ics.uci.edu/ml/datasets/Chess+%28King-Rook+vs.+King-Pawn%29
 */

import tree.TreeBuilder;

public class Test {
	public static void main(String[] args) {
		TreeBuilder decisionTree = new TreeBuilder("data2.txt");
		decisionTree.inputData();
		decisionTree.process();

	}


}
