#include <iostream>
using namespace std;

int main()
{
    int stack[5];
    int top = -1;
    int choice, value;

    cout << "Stack Capacity = 5\n";

   
    cout << "\n--- Place Trays ---\n";

    for (int i = 0; i < 3; i++)
    {
        cout << "Enter tray number: ";
        cin >> value;

        if (top == 4)
        {
            cout << "Error: Stack is full\n";
        }
        else
        {
            top++;
            stack[top] = value;

            cout << "Tray placed successfully\n";
            cout << "Top tray: " << stack[top] << endl;
        }
    }

  
    cout << "\n--- Take Trays ---\n";

    for (int i = 0; i < 4; i++)
    {
        if (top == -1)
        {
            cout << "Error: Stack is empty\n";
        }
        else
        {
            cout << "Taken tray: "
                 << stack[top] << endl;

            top--;

            if (top == -1)
                cout << "Stack is empty\n";
            else
                cout << "Top tray: "
                     << stack[top] << endl;
        }
    }

    return 0;
}