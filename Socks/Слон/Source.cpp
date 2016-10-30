//������!!! ����������� ��������!!!
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

struct event { // �������� � ������� ���� ������ �����, ���� ��� �����
	int time;
	bool start;

	bool operator < (const event &other) const { // ��������������� ������ ���������, ����� ������� ����� ���� ������������� �� �������
		if (time == other.time) {
			return start > other.start;
		}
		return time < other.time;
	}
};



int main() {
	vector<event> socks;

	int N;
	cin >> N;

	for (int i = 0; i < N; i++) {
		int time_s, time_e;
		cin >> time_s >> time_e;
		event e;
		e.time = time_s;
		e.start = true;
		socks.push_back(e);

		e.time = time_e;
		e.start = false;
		socks.push_back(e);
	}

	sort(socks.begin(), socks.end());

	long long count = 0;
	long long max = 0;
	for (int i = 0; i < socks.size(); i++) { // ���� ���������� ����� +1, ���� ������������� -1
		if (socks[i].start) {
			count++;
			if (max < count) {
				max = count;
			}
		}
		else {
			count--;
		}
	}

	cout << max;
	system("pause"); //��� ��� ��������
	return 0;
}