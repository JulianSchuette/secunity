# SecUnity Web Application

This is the SecUnity Web Application. It is a place to collect and share information about the security research landscape in Europe. 
In the first place it includes information about relevant security research institutes and companies across Europe, with the aim of bringing together partners in the context of European funded research in the Horizon2020 programe.


The web application consists of several parts:

* __TripleStore__: A triplestore database stores data in RDF format
* __Backend__: A REST API backend provides CRUD operations on the triplestore. Users are authenticated via OAuth2.0/OpenID Connect tokens
* __Frontend__: The frontend is a HTML/JS application which operates against the REST API and provides forms to users to enter data for CRUD operations. In a later step, the frontend will provide various advanced ways to search and browse through data in the backend.