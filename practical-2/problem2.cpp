#include <iostream>
using namespace std;


int iterativeBinarySearch(int arr[], int n, int target)
{
    int low = 0, high = n - 1;

    while (low <= high)
    {
        int mid = (low + high) / 2;

        if (arr[mid] == target)
            return mid;
        else if (arr[mid] < target)
            low = mid + 1;
        else
            high = mid - 1;
    }

    return -1;
}


int recursiveBinarySearch(int arr[], int low, int high, int target)
{
    if (low > high)
        return -1;

    int mid = (low + high) / 2;

    if (arr[mid] == target)
        return mid;

    if (arr[mid] < target)
        return recursiveBinarySearch(arr, mid + 1, high, target);

    return recursiveBinarySearch(arr, low, mid - 1, target);
}

int main()
{
    int n;

    cout << "Enter number of book codes: ";
    cin >> n;

    int arr[100];

    cout << "Enter sorted book codes: ";
    for (int i = 0; i < n; i++)
        cin >> arr[i];

    int target;
    cout << "Enter target code: ";
    cin >> target;

    int pos = iterativeBinarySearch(arr, n, target);

    if (pos == -1)
        cout << "Iterative: Not Found" << endl;
    else
        cout << "Iterative: Found at position " << pos << endl;

    pos = recursiveBinarySearch(arr, 0, n - 1, target);

    if (pos == -1)
        cout << "Recursive: Not Found" << endl;
    else
        cout << "Recursive: Found at position " << pos << endl;

    return 0;
}