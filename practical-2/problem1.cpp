#include <iostream>
using namespace std;


int iterativeSearch(int arr[], int n, int target)
{
    int low = 0, high = n - 1;

    while (low <= high)
    {
        int mid = (low + high) / 2;

        if (arr[mid] == target)
            return mid;
        else if (target > arr[mid])
            low = mid + 1;
        else
            high = mid - 1;
    }

    return -1;
}


int recursiveSearch(int arr[], int low, int high, int target)
{
    if (low > high)
        return -1;

    int mid = (low + high) / 2;

    if (arr[mid] == target)
        return mid;

    if (target > arr[mid])
        return recursiveSearch(arr, mid + 1, high, target);

    return recursiveSearch(arr, low, mid - 1, target);
}

int main()
{
    int n;
    cout << "Enter size of array: ";
    cin >> n;

    int arr[100];

    cout << "Enter sorted elements: ";
    for (int i = 0; i < n; i++)
        cin >> arr[i];

    int target;
    cout << "Enter element to search: ";
    cin >> target;

    int result = iterativeSearch(arr, n, target);

    if (result == -1)
        cout << "Iterative: Not Found" << endl;
    else
        cout << "Iterative: Found at Position " << result << endl;

    result = recursiveSearch(arr, 0, n - 1, target);

    if (result == -1)
        cout << "Recursive: Not Found" << endl;
    else
        cout << "Recursive: Found at Position " << result << endl;

    return 0;
}