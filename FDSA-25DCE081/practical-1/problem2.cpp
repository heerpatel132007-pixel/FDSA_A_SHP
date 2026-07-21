#include <iostream>
using namespace std;

int main()
{
    int n;

    cout << "Enter number of borrow records: ";
    cin >> n;

    int arr[n];

    cout << "Enter Book IDs: ";
    for(int i = 0; i < n; i++)
    {
        cin >> arr[i];
    }

    cout << "Books borrowed more than once are: ";

    for(int i = 0; i < n; i++)
    {
        int count = 1;

        
        bool checked = false;
        for(int k = 0; k < i; k++)
        {
            if(arr[i] == arr[k])
            {
                checked = true;
                break;
            }
        }

        if(checked)
            continue;

        for(int j = i + 1; j < n; j++)
        {
            if(arr[i] == arr[j])
            {
                count++;
            }
        }

        if(count > 1)
        {
            cout << arr[i] << " ";
        }
    }

    return 0;
}