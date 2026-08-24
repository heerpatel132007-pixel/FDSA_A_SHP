#include <iostream>
using namespace std;

struct Node {
    int data;
    Node* next;
};


void insertFront(Node*& head, int value) {
    Node* newNode = new Node;
    newNode->data = value;
    newNode->next = head;
    head = newNode;
}


void insertEnd(Node*& head, int value) {
    Node* newNode = new Node;
    newNode->data = value;
    newNode->next = NULL;

    if (head == NULL) {
        head = newNode;
        return;
    }

    Node* temp = head;

    while (temp->next != NULL) {
        temp = temp->next;
    }

    temp->next = newNode;
}


void insertPosition(Node*& head, int value, int position) {
    if (position == 1) {
        insertFront(head, value);
        return;
    }

    Node* temp = head;

    for (int i = 1; i < position - 1 && temp != NULL; i++) {
        temp = temp->next;
    }

    if (temp == NULL) {
        cout << "Invalid position!" << endl;
        return;
    }

    Node* newNode = new Node;
    newNode->data = value;
    newNode->next = temp->next;
    temp->next = newNode;
}


void display(Node* head) {
    Node* temp = head;

    while (temp != NULL) {
        cout << temp->data << " ";
        temp = temp->next;
    }

    cout << endl;
}

int main() {
    Node* head = NULL;

    insertFront(head, 10);
    cout << "After inserting at front: ";
    display(head);

    insertEnd(head, 20);
    cout << "After inserting at end: ";
    display(head);

    insertEnd(head, 30);
    cout << "After inserting at end: ";
    display(head);

    insertPosition(head, 15, 2);
    cout << "After inserting 15 at position 2: ";
    display(head);

    return 0;
}