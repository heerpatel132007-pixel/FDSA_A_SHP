#include <iostream>
using namespace std;

int main()
{
    int table[10];

    for (int i = 0; i < 10; i++)
    {
        table[i] = -1;
    }

    int registrations[] = {123, 456, 789, 236, 896, 345, 678};
    int n = 7;

    for (int i = 0; i < n; i++)
    {
        int registration = registrations[i];
        int slot = registration % 10;
        int count = 0;

        while (table[slot] != -1 && count < 10)
        {
            slot = (slot + 1) % 10;
            count++;
        }

        if (count == 10)
        {
            cout << "Parking lot is full. Vehicle "
                 << registration << " cannot be parked." << endl;
        }
        else
        {
            table[slot] = registration;
            cout << "Vehicle " << registration
                 << " parked at slot " << slot << endl;
        }
    }

    cout << "\nFinal Parking Slots:\n";

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