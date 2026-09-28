#include <iostream>
#include <string>
using namespace std;

struct Node
{
    string name;
    Node* prev;
    Node* next;

    Node(string n)
    {
        name = n;
        prev = NULL;
        next = NULL;
    }
};

class DoublyCircularList
{
    Node* head;

public:

    DoublyCircularList()
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
            newNode->prev = head;

            return;
        }

        Node* last = head->prev;

        newNode->next = head;
        newNode->prev = last;

        last->next = newNode;
        head->prev = newNode;
    }

    
    void remove(string name)
    {
        if (head == NULL)
        {
            cout << "Circle is empty." << endl;
            return;
        }

        Node* current = head;

        do
        {
            if (current->name == name)
            {
              
                if (current->next == current)
                {
                    delete current;
                    head = NULL;
                    return;
                }

              
                current->prev->next = current->next;
                current->next->prev = current->prev;

                
                if (current == head)
                {
                    head = current->next;
                }

                delete current;
                return;
            }

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
                cout << " <-> ";
            }

        } while (temp != head);

        cout << " <-> back to "
             << head->name << endl;
    }
};

int main()
{
    DoublyCircularList students;

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