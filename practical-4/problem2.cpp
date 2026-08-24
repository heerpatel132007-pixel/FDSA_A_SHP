#include <iostream>
using namespace std;

struct Node {
    int data;
    Node* next;
};


void insert(Node*& head, int value) {
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


void deleteValue(Node*& head, int value) {
    if (head == NULL)
        return;

    if (head->data == value) {
        Node* temp = head;
        head = head->next;
        delete temp;
        return;
    }

    Node* temp = head;

    while (temp->next != NULL) {
        if (temp->next->data == value) {
            Node* del = temp->next;
            temp->next = del->next;
            delete del;
            return;
        }

        temp = temp->next;
    }
}


void forward(Node* head) {
    cout << "Front to back: ";

    while (head != NULL) {
        cout << head->data << " ";
        head = head->next;
    }

    cout << endl;
}

void reverse(Node* head) {
    if (head == NULL)
        return;

    reverse(head->next);
    cout << head->data << " ";
}

int main() {
    Node* head = NULL;

    insert(head, 101);
    insert(head, 102);
    insert(head, 103);
    insert(head, 104);

    cout << "Original queue: ";
    forward(head);

    deleteValue(head, 102);

    cout << "After deleting 102: ";
    forward(head);

    cout << "Reverse order: ";
    reverse(head);
    cout << endl;

    cout << "Forward order: ";
    forward(head);

    return 0;
}