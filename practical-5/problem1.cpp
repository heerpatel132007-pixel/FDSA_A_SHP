#include <iostream>
#include <string>
using namespace std;

struct Node
{
    string song;
    Node* prev;
    Node* next;

    Node(string s)
    {
        song = s;
        prev = NULL;
        next = NULL;
    }
};

class Playlist
{
    Node* head;

public:

    Playlist()
    {
        head = NULL;
    }

   
    void addFirst(string song)
    {
        Node* newNode = new Node(song);

        if (head != NULL)
        {
            newNode->next = head;
            head->prev = newNode;
        }

        head = newNode;
    }

    
    void addLast(string song)
    {
        Node* newNode = new Node(song);

        if (head == NULL)
        {
            head = newNode;
            return;
        }

        Node* temp = head;

        while (temp->next != NULL)
        {
            temp = temp->next;
        }

        temp->next = newNode;
        newNode->prev = temp;
    }

   
    void insertAfter(string existingSong, string newSong)
    {
        Node* temp = head;

        while (temp != NULL &&
               temp->song != existingSong)
        {
            temp = temp->next;
        }

        if (temp == NULL)
        {
            cout << existingSong << " not found." << endl;
            return;
        }

        Node* newNode = new Node(newSong);

        newNode->next = temp->next;
        newNode->prev = temp;

        if (temp->next != NULL)
        {
            temp->next->prev = newNode;
        }

        temp->next = newNode;
    }


    void removeFirst()
    {
        if (head == NULL)
        {
            cout << "Playlist is empty." << endl;
            return;
        }

        cout << "Removed: " << head->song << endl;

        Node* temp = head;
        head = head->next;

        if (head != NULL)
        {
            head->prev = NULL;
        }

        delete temp;
    }


    int count()
    {
        int count = 0;
        Node* temp = head;

        while (temp != NULL)
        {
            count++;
            temp = temp->next;
        }

        return count;
    }

  
    void display()
    {
        Node* temp = head;

        cout << "Playlist: ";

        while (temp != NULL)
        {
            cout << temp->song;

            if (temp->next != NULL)
            {
                cout << " <-> ";
            }

            temp = temp->next;
        }

        cout << endl;
        cout << "Count: " << count() << endl;
    }
};

int main()
{
    Playlist playlist;

    playlist.addFirst("Song A");
    playlist.display();

    playlist.addLast("Song C");
    playlist.display();

    playlist.insertAfter("Song A", "Song B");
    playlist.display();

    playlist.insertAfter("Song C", "Song D");
    playlist.display();

    playlist.removeFirst();
    playlist.display();

    playlist.insertAfter("Song X", "Song E");
    playlist.display();

    return 0;
}
