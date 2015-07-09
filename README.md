#PoC Session management with Cassandra.

PoC for a json session manager based on Casssandra 2.0.
Rather than storing all session data everytime - only changes are stored as a time serie.
At read, all changes are merged to get the last state.


Sample Code
```
Please have a look at the tests :

* AppSessionTest: to see how to add attributes to the session.
* AppSessionDAOTest: to see how to save/load session.
```
