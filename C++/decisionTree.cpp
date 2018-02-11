#include <iostream>
#include<stdlib.h>
#include<stdio.h>
#include<conio.h>
#include<math.h>
#include <cmath>
#include <functional>
#include <string>
#include <vector>

using namespace std;

struct node{
	bool isLeaf;
	string value;
	vector<string> condition;
	vector<node> child;
};


node root;


void constructTree(node root, string **data, string *header)
{
	cout << root.isLeaf << endl;
}



int main(void)
{
	freopen("data.txt", "r", stdin);
	int numberOfInstances, numberOfAttribute;
	cin >> numberOfInstances >> numberOfAttribute;
	string header[numberOfAttribute];
	string data[numberOfAttribute][numberOfInstances];
	
	for(int i = 0; i < numberOfAttribute; i++)
		cin >> header[i];
	
	for(int i = 0; i < numberOfInstances; i++)
		for(int j = 0; j < numberOfAttribute; j++)
			cin >> data[j][i];
	
	constructTree(root, data, header);
	
	

	
	
	return 0;
}



/*


for(int i = 0; i < numberOfAttribute; i++)
		cout << header[i] << " ";
	cout << endl;





	for(int i = 0; i < 14; i++)
	{
		for(int j = 0; j < 5; j++)
			cout << data[j][i] << " ";
		cout << endl;
	}




	double x = 4;
	double y = 10;
	//cout << log2(0.678) << endl;
	//cout<<log2(x/y)<<endl;



*/
