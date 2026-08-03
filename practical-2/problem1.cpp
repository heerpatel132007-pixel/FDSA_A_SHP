#include <iostream>
#include <string>
using namespace std;

// Iterative Search
int iterativeSearch(string arr[], int n, string target)
{
    for (int i = 0; i < n; i++)
    {
        if (arr[i] == target)
            return i;
    }
    return -1;
}

// Recursive Search
int recursiveSearch(string arr[], int n, string target, int index)
{
    if (index == n)
        return -1;

    if (arr[index] == target)
        return index;

    return recursiveSearch(arr, n, target, index + 1);
}

int main()
{
    int n;
    cout << "Enter number of vehicles: ";
    cin >> n;

    string plate[n];

    cout << "Enter license plates:\n";
    for (int i = 0; i < n; i++)
    {
        cin >> plate[i];
    }

    string target;
    cout << "Enter target plate: ";
    cin >> target;

    int pos = iterativeSearch(plate, n, target);

    if (pos == -1)
        cout << "Iterative: Not Found" << endl;
    else
        cout << "Iterative: Found at position " << pos << endl;

    pos = recursiveSearch(plate, n, target, 0);

    if (pos == -1)
        cout << "Recursive: Not Found" << endl;
    else
        cout << "Recursive: Found at position " << pos << endl;

    return 0;
}
