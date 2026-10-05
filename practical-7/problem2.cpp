#include <iostream>
using namespace std;

struct Node {
    int patient;
    Node* next;
};

int main() {
    Node* front = NULL;
    Node* rear = NULL;

    int operations;
    cout << "Enter number of operations: ";
    cin >> operations;

    for (int i = 0; i < operations; i++) {
        int choice, patient;

        cout << "\n1. Arrive\n2. Attend\n";
        cin >> choice;

        if (choice == 1) {
            cout << "Enter patient number: ";
            cin >> patient;

            Node* newNode = new Node;
            newNode->patient = patient;
            newNode->next = NULL;

            if (front == NULL) {
                front = newNode;
                rear = newNode;
            } else {
                rear->next = newNode;
                rear = newNode;
            }

            cout << "Front patient: " << front->patient << endl;
        }
        else if (choice == 2) {
            if (front == NULL) {
                cout << "Error: No patients waiting\n";
            } else {
                cout << "Attended patient: " << front->patient << endl;

                Node* temp = front;
                front = front->next;
                delete temp;

                if (front == NULL)
                    rear = NULL;
                else
                    cout << "Front patient: " << front->patient << endl;
            }
        }
        else {
            cout << "Invalid operation\n";
        }
    }

    return 0;
}