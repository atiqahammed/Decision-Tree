package tree;

import java.util.ArrayList;

public class Node {
	public String name_of_attribute;
	public String aalue;
	public boolean is_leaf;
	public ArrayList<Node> collection_of_child;
	public ArrayList<String> collection_of_condition;
	public int index_of_attribute;
}
