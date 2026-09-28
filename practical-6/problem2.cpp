#include <iostream>
#include <string>
using namespace std;

struct Node
{
    string page;
    Node* next;
};

int main()
{
    Node* top = NULL;

    string page;

   
    cout << "--- Browser History ---\n";

    for (int i = 0; i < 3; i++)
    {
        cout << "Enter page: ";
        cin >> page;

        Node* newNode = new Node;

        newNode->page = page;
        newNode->next = top;

        top = newNode;

        cout << "Current page: "
             << top->page << endl;
    }


    cout << "\n--- Back Button ---\n";

    for (int i = 0; i < 4; i++)
    {
        if (top == NULL)
        {
            cout << "No history left. "
                 << "Cannot go back.\n";
        }
        else
        {
            cout << "Going back from: "
                 << top->page << endl;

            Node* temp = top;

            top = top->next;

            delete temp;

            if (top == NULL)
            {
                cout << "No history left.\n";
            }
            else
            {
                cout << "Current page: "
                     << top->page << endl;
            }
        }
    }

    return 0;
}