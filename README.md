Expenses Reporter
==========

This is my first attempt at an Android application, so I am focusing more on learning and figuring out all the needed tools. the application itself doesn't have a general public interest as it is a simple expenses report integrated with my personal intranet.

The application has a simple input interface that I have kept to a minimum as it will be ofter used while on the move, for instance just after purchasing something in a shop. 

![Screenshot](https://raw.github.com/nicolacimmino/ExpensesReporter/master/documentation/screenshot.png)

User can also get a list of expenses, this is a very simple list for now with no filtering nor sorting.


![Screenshot](https://raw.github.com/nicolacimmino/ExpensesReporter/master/documentation/screenshot2.png)

The application keeps data in sync with the server by means of a sync adapter. The sync service creates a new Android account type in order to authenticate the user with the server. The account can be found under Settings as other accounts are usually found in Android.
Additionally at the first start the application asks the user to authenticate with the server and creates automatically a sync account.

![Screenshot](https://raw.github.com/nicolacimmino/ExpensesReporter/master/documentation/screenshot0.png)

Software Architecture
===========

Below is an oversimplified view of the architecture of the application. Data is stored locally on the phone in a SQLite database. This allows to use the application even when there is no internet connection. The Sync Service allows to leverage Android highly optimized Sync framework that allows to keep sync operations at a minimum and to have them scheduled oppurtunistically in order to reduce power consuption. On the server side is a ReSTful HTTP service that accepts the new data. There is no provision for sync of data from the server to the mobile since the main purpose of the application is to allow user to enter expenses in the system. Should the application be expanded to sync also in this direction GCM (Google Cloud Messaging) service would be a better choice from a power consumption standpoint, at least to notify the presence of new data.

![Screenshot](https://raw.github.com/nicolacimmino/ExpensesReporter/master/documentation/structure.png)
