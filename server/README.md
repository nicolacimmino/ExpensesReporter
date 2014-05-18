
The Sync Server
==============

For the purpose of backing this application I have defined a very simple sync API. I am not claiming by no means that this is a safe implementation, it has just been put up to support the application development.

If I had to develop an API to actually use in production I think I would have gone for a REsTful API and OAuth to sign the requests. That being said this example API is reasonably safe only if HTTPS is used as transport.

In any case it is not safer than just sending login and passwords at every request, over HTTPS. The token is there just to experiment better with the Android Authentication manager ability to store authentication tokens.

The diagram below depicts the authentication and subsequent sync requests.

![API](https://raw.github.com/nicolacimmino/ExpensesReporter/master/documentation/api.png)

It's basically the same way cookies work for web browsers. The client first requests to be authenticated and sends login and password, if these are correct the server will reply with a token. 

From here on the client will send sync requests (push new data) always providing the token which is proof of client identity. 

