#include <iostream>
using namespace std;

int main() {
    int n;
    cout << "Enter queue size: ";
    cin >> n;

    int queue[100];
    int front = -1;
    int rear = -1;

    int operations;
    cout << "Enter number of operations: ";
    cin >> operations;

    for (int i = 0; i < operations; i++) {
        int choice, token;

        cout << "\n1. Join\n2. Serve\n";
        cin >> choice;

        if (choice == 1) {
            cout << "Enter token: ";
            cin >> token;

            if (rear == n - 1) {
                cout << "Error: Queue is full\n";
            } else {
                if (front == -1)
                    front = 0;

                rear++;
                queue[rear] = token;

                cout << "Front token: " << queue[front] << endl;
            }
        }
        else if (choice == 2) {
            if (front == -1 || front > rear) {
                cout << "Error: Queue is empty\n";
            } else {
                cout << "Served token: " << queue[front] << endl;
                front++;

                if (front > rear) {
                    front = -1;
                    rear = -1;
                } else {
                    cout << "Front token: " << queue[front] << endl;
                }
            }
        }
        else {
            cout << "Invalid operation\n";
        }
    }

    return 0;
}