#include <iostream>
using namespace std;

int main()
{
    int n, h;

    cout << "Enter number of items: ";
    cin >> n;

    int arr[n];

    cout << "Enter items: ";
    for (int i = 0; i < n; i++)
    {
        cin >> arr[i];
    }

    cout << "Enter number of hours: ";
    cin >> h;

    h = h % n;  

    for (int k = 0; k < h; k++)
    {
        int first = arr[0];

        for (int i = 0; i < n - 1; i++)
        {
            arr[i] = arr[i + 1];
        }

        arr[n - 1] = first;
    }

    cout << "Final display order: ";
    for (int i = 0; i < n; i++)
    {
        cout << arr[i] << " ";
    }

  return 0;
}
