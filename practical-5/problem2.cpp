#include <iostream>
#include <string>
using namespace std;

struct Node
{
    string name;
    Node* next;

    Node(string n)
    {
        name = n;
        next = NULL;
    }
};

class CircularList
{
    Node* head;

public:

    CircularList()
    {
        head = NULL;
    }

  
    void add(string name)
    {
        Node* newNode = new Node(name);

       
        if (head == NULL)
        {
            head = newNode;
            newNode->next = head;
            return;
        }

        Node* temp = head;

        while (temp->next != head)
        {
            temp = temp->next;
        }

        temp->next = newNode;
        newNode->next = head;
    }

    
    void remove(string name)
    {
        if (head == NULL)
        {
            cout << "Circle is empty." << endl;
            return;
        }

        Node* current = head;
        Node* previous = NULL;

        do
        {
            if (current->name == name)
            {
               
                if (current == head &&
                    current->next == head)
                {
                    delete current;
                    head = NULL;
                    return;
                }

              
                if (current == head)
                {
                    Node* last = head;

                    while (last->next != head)
                    {
                        last = last->next;
                    }

                    head = head->next;
                    last->next = head;

                    delete current;
                    return;
                }

               
                previous->next = current->next;

                delete current;
                return;
            }

            previous = current;
            current = current->next;

        } while (current != head);

        cout << name << " not found." << endl;
    }

   
    void display()
    {
        if (head == NULL)
        {
            cout << "Circle is empty." << endl;
            return;
        }

        Node* temp = head;

        cout << "Circle: ";

        do
        {
            cout << temp->name;

            temp = temp->next;

            if (temp != head)
            {
                cout << " -> ";
            }

        } while (temp != head);

        cout << " -> back to " << head->name << endl;
    }
};

int main()
{
    CircularList students;

    students.add("A");
    students.display();

    students.add("B");
    students.display();

    students.add("C");
    students.display();

    students.remove("B");
    students.display();

    students.remove("A");
    students.display();

    return 0;
}