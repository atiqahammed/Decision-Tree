#include <iostream>
#include <vector>
#include <map>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <cmath>
#include <functional>


using namespace std;

struct node{
	int index;
	string name;
	int isLeaf;
	string value;
	vector<string> condition;
	vector<node> child;
};

map<string, int> indexMaping;

double calculateInformation(int row, vector<string> classValue)
{
    map<string, bool> previoulyTaken;
    vector<string> values;
    map<string, int> count;
    
    for(int i = 0; i < row; i++){
    	if(!previoulyTaken[classValue[i]]){
    	 values.push_back(classValue[i]);
    	 previoulyTaken[classValue[i]] = true;
    	}
    	count[classValue[i]]++;
    }

    double entropy = 0.0;
    if(values.size() == 1) return 0;
    for(int i = 0; i < values.size(); i++)
    {
    	double probability = (double) count[values[i]] / row;
    	entropy += probability * log2(probability);
    }
    entropy *= -1;
    return entropy;
}

void builtTree(int row, int col, string **a, node &currentNode, string *header)
{
    vector<string> classValue;

    for(int i = 0; i < row; i++)
    	classValue.push_back(a[i][col - 1]);
    double previousInformation = calculateInformation(row, classValue);
    double smallestInformation = previousInformation;
    vector<string> valuesOfThisNode;
    map<string, int> rowCount;
    int index = -1;
    
    for(int i = 0; i <col -1; i++)  // col - 1
    {	
    	double postInformation = 0.0;
    	map<string, int> rowCountTemp;
    	map<string, bool> taken;
    	vector<string> values;
    	for(int j = 0; j < row; j++)
    		if(!taken[a[j][i]])
    		{
    			taken[a[j][i]] = true;
    			values.push_back(a[j][i]);
    		}


    	for(int j = 0; j < values.size(); j++)
    	{
    		vector<string> tempClassValue;
    		for(int k = 0; k < row; k++)
    			if(a[k][i] == values[j]) tempClassValue.push_back(a[k][col - 1]);
    		postInformation += (double)tempClassValue.size() / row * calculateInformation(tempClassValue.size(), tempClassValue);
    		rowCountTemp[values[j]] = tempClassValue.size();
    	}
    	if(postInformation < smallestInformation)
    	{
    		index = i;
    		smallestInformation = postInformation;
    		valuesOfThisNode = values;
    		rowCount = rowCountTemp;
    	}
    }

    if(index != -1)
    {
    	currentNode.name = header[index];
    	currentNode.index = indexMaping[currentNode.name];
    	currentNode.condition = valuesOfThisNode;
    //	node tempNode;
    	cout << "attribute name " << currentNode.name << endl;
    	
    	for(int i = 0; i < currentNode.condition.size(); i++)     //currentNode.condition.size()
    	{
    		node tempNode;
    		string *tempHeader;
    		tempHeader = new string[col - 1];
    		int count = 0;
    		for(int j = 0; j < col; j++)
    		{
    			if(j == index) continue;
    			tempHeader[count] = header[j];
    			count++;
    		}

    		string **arrTemp;
    		arrTemp = new string *[rowCount[currentNode.condition[i]]];
    		for(int j = 0; j < rowCount[currentNode.condition[i]]; j++)
    			arrTemp[j] = new string[col - 1];
    		
    		int jj =0;
    		for(int j = 0; j < row; j++)
    		{
    			int kk = 0;
    			bool test;
    			for(int k = 0; k < col; k++)
    			{
    				if(k != index && a[j][index] == currentNode.condition[i])
	    			{
	    				arrTemp[jj][kk] = a[j][k];
	    				kk++;
	    			}		
    			}
    			if(a[j][index] == currentNode.condition[i])//if(test) continue;
    			jj++;
    			
    		}
    		cout << " >>>> >> condition name " << currentNode.condition[i] << endl;
    		builtTree(rowCount[currentNode.condition[i]], col - 1, arrTemp, tempNode, tempHeader);
    		currentNode.child.push_back(tempNode);

    	}
    }

    else 
    {
    	currentNode.isLeaf = 1;
    	cout << "- - - - - - - - - - " << endl;
    	vector<string> v;
    	map<string, int> map;
    	for(int i = 0; i <row; i++)
    	{
    		if(map[a[i][col - 1]] == 0) v.push_back(a[i][col - 1]);
    		map[a[i][col - 1]]++;
    	}
    	string finalValue = v[0];
    	for(int i = 0; i < v.size(); i++)
    		if(map[v[i]] > map[finalValue]) finalValue = map[v[i]];
    	currentNode.value = finalValue;
    	cout <<"result  == == " <<  currentNode.value << endl;
    	cout << "----------------------------------" << endl;
	}
}

string testResult(node currentNode, string *a)
{
	if(currentNode.condition.size() == 0) return currentNode.value;
	
	int index = -1;
	for(int i = 0; i < currentNode.condition.size(); i++)
		if(a[currentNode.index] ==  currentNode.condition[i]){
			index = i;
			break;
		}

	if(index != -1 ) //cout << "paichi" << endl;
    	return testResult(currentNode.child[index], a);

    return "hello";
}

int main(void)
{
	freopen("data.txt", "r", stdin);
	int row, col;
	cin >> row >> col;

	string **array, *header;
	array = new string *[row];
	header = new string[col];
	for(int i = 0; i < col; i++){
		cin >> header[i];
		indexMaping[header[i]] = i;
	}
	
	for(int i = 0; i < row; i++)
    	array[i] = new string[col];

    for(int i = 0; i < row; i++)
    	for(int j = 0; j < col; j++)
    		cin >> array[i][j];

    node root;
    builtTree(row, col, array, root, header);  	
  	string testData[4] = {"rainy", "hot", "high", "FALSE"};
  	cout << testResult(root, testData) << endl;

    for(int i = 0; i < row; i++)
    	delete []array[i];
    delete []array; 
    delete []header;

	return 0;
}

