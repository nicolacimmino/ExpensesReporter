
This is my first attempt at an Android application, so I am focusing more on learning and figuring out all the needed tools. the application itself doesn't have a general public interest as it is a simple expenses report integrated with my personal intranet.

The application has a simple input interface that I have kept to a minimum as it will be ofter used while on the move, for instance just after purchasing something in a shop. 

![Screenshot](https://raw.github.com/nicolacimmino/ExpensesReporter/master/documentation/screenshot.png)

User can also get a list of expenses, this is a very simple list for now with no filtering nor sorting.


![Screenshot](https://raw.github.com/nicolacimmino/ExpensesReporter/master/documentation/screenshot2.png)

The application keeps data in sync with the server by means of a sync adapter. The sync service creates a new Android account type in order to authenticate the user with the server. The account can be found under Settings as other accounts are usually found in Android:

![Screenshot](https://raw.github.com/nicolacimmino/ExpensesReporter/master/documentation/screenshot3.png)
