#include <iostream>
#include <stack>
#include <string>
using namespace std;


int priority(char op)
{
    if (op == '+' || op == '-')
        return 1;

    if (op == '*' || op == '/')
        return 2;

    return 0;
}

int main()
{
    string infix;
    string postfix = "";

    stack<char> s;

    cout << "Enter infix expression: ";
    getline(cin, infix);

    for (int i = 0; i < infix.length(); i++)
    {
        char ch = infix[i];

       
        if (ch == ' ')
            continue;

        if (isalnum(ch))
        {
            postfix += ch;
            postfix += ' ';
        }


        else if (ch == '(')
        {
            s.push(ch);
        }

   
        else if (ch == ')')
        {
            while (!s.empty() && s.top() != '(')
            {
                postfix += s.top();
                postfix += ' ';
                s.pop();
            }

    
            if (!s.empty())
                s.pop();
        }


        else
        {
            while (!s.empty() &&
                   s.top() != '(' &&
                   priority(s.top()) >= priority(ch))
            {
                postfix += s.top();
                postfix += ' ';
                s.pop();
            }

            s.push(ch);
        }
    }

    while (!s.empty())
    {
        postfix += s.top();
        postfix += ' ';
        s.pop();
    }

    cout << "Postfix expression: "
         << postfix << endl;

    return 0;
}