//јхтунг!!! Ѕесполезные комменты!!!
#include <iostream>
#include <vector>
#include <string>
using namespace std;

//само поле
vector<vector<char>> field;


//функци€, определ€юща€, победил ли тот или иной игрок
bool win(char player) {
	for (int i = 0; i < 3; i++) {
		int count_row = 0;
		int count_column = 0;
		for (int j = 0; j < 3; j++) {
			if (field[i][j] == player) {
				count_row++;
			}
			if (field[j][i] == player) {
				count_column++;
			}
		}
		if ((count_row == 3) || (count_column == 3)) {
			return true;
		}
	}
	
	int count_right = 0;
	int count_left = 0;
	for (int i = 0; i < 3; i++) {
		if (player == field[i][i]) {
			count_right++;
		} 
		if (player == field[i][2 - i]) {
			count_left++;
		}
	}
	
	if ((count_right == 3) || (count_left == 3)) {
		return true;
	}
	return false;
}

int main() {
	int count_x = 0;
	int count_o = 0;
	bool have_empty = false;
	for (int i = 0; i < 3; i++) {
		field.push_back(vector<char>());
		string cur;
		getline(cin, cur);
		for (int j = 0; j < 3; j++) {
			field[i].push_back(cur[j * 2]);
			if (cur[j * 2] == 'X') {
				count_x++;
			}
			else if (cur[j * 2] == 'O'){
				count_o++;
			}
			else if (cur[j * 2] == 'E') {
				have_empty = true;
			}
		}
	}


	//крестиков должно быть больше на один, или такое количество, как и ноликов
	if (count_x - count_o <= 1) {	
		bool win_x = win('X');
		bool win_o = win('O');
		//если крестики победили, то их должно быть на один больше
		if (win_x && !(count_x - count_o == 1)) {
			cout << "NO";
		}
		//если нолики победили, то их должно быть такое же количество, как и крестиков
		else if (win_o && !(count_x == count_o)) {
			cout << "NO";
		}
		else {
			cout << "YES";
		}
	}
	else {
		cout << "NO";
	}

	system("pause");
	return 0;
}
