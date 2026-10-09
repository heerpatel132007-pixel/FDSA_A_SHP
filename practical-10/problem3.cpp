#include <iostream>
using namespace std;

int main()
{
    int table[10];

    for (int i = 0; i < 10; i++)
    {
        table[i] = -1;
    }

    int studentIDs[] = {123, 456, 789, 236, 896, 345, 678};
    int n = 7;

    for (int i = 0; i < n; i++)
    {
        int studentID = studentIDs[i];

        int firstHash = studentID % 10;
        int secondHash = 7 - (studentID % 7);
        int count = 0;
        int inserted = 0;

        while (count < 10)
        {
            int slot = (firstHash + count * secondHash) % 10;

            if (table[slot] == -1)
            {
                table[slot] = studentID;

                cout << "Student " << studentID
                     << " stored at slot " << slot << endl;

                inserted = 1;
                break;
            }

            count++;
        }

        if (inserted == 0)
        {
            cout << "Hash table is full. Student "
                 << studentID << " cannot be stored." << endl;
        }
    }

    cout << "\nFinal Hash Table:\n";

    for (int i = 0; i < 10; i++)
    {
        cout << "Slot " << i << ": ";

        if (table[i] == -1)
            cout << "Empty";
        else
            cout << table[i];

        cout << endl;
    }

    return 0;
}