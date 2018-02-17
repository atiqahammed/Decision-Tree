package tree;

import java.util.ArrayList;

public class Node {
	public int index;
	public String name;
	public int isLeaf;
	public String value;
	public ArrayList<String> condition;
	public ArrayList<Node> child;

	public void setIndex(int index) {
		this.index = index;
	}
}
