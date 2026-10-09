#include <iostream>
#include <vector>
using namespace std;

int main()
{
    vector<int> table[10];

    int bookCodes[] = {123, 456, 789, 236, 896, 345, 678, 223, 346};
    int n = 9;

    for (int i = 0; i < n; i++)
    {
        int bookCode = bookCodes[i];
        int slot = bookCode % 10;

        table[slot].push_back(bookCode);

        cout << "Book " << bookCode
             << " added to shelf " << slot << endl;
    }

    cout << "\nFinal Shelf Contents:\n";

    for (int i = 0; i < 10; i++)
    {
        cout << "Shelf " << i << ": ";

        if (table[i].empty())
        {
            cout << "Empty";
        }
        else
        {
            for (int book : table[i])
            {
                cout << book << " ";
            }
        }

        cout << endl;
    }

    return 0;
}