#include <iostream>
#include <vector>
#include <queue>
using namespace std;

vector<int> graph[10];
bool visited[10] = {false};


void DFS(int building)
{
    visited[building] = true;
    cout << building << " ";

    for (int neighbour : graph[building])
    {
        if (!visited[neighbour])
        {
            DFS(neighbour);
        }
    }
}


void BFS(int start)
{
    queue<int> q;
    bool visitedBFS[10] = {false};

    visitedBFS[start] = true;
    q.push(start);

    while (!q.empty())
    {
        int building = q.front();
        q.pop();

        cout << building << " ";

        for (int neighbour : graph[building])
        {
            if (!visitedBFS[neighbour])
            {
                visitedBFS[neighbour] = true;
                q.push(neighbour);
            }
        }
    }
}

int main()
{
    graph[0].push_back(1);
    graph[0].push_back(2);
    graph[1].push_back(3);
    graph[1].push_back(4);
    graph[2].push_back(5);
    graph[2].push_back(6);

    int start = 0;

    cout << "DFS Traversal: ";
    DFS(start);

    cout << endl;

    cout << "BFS Traversal: ";
    BFS(start);

    cout << endl;

    return 0;
}