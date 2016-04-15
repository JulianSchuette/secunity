# SecUnity Web Application

This is the SecUnity Web Application. It is a place to collect and share information about the security research landscape in Europe. 
In the first place it includes information about relevant security research institutes and companies across Europe, with the aim of bringing together partners in the context of European funded research in the Horizon2020 programe.


The web application consists of several parts:

* __TripleStore__: A triplestore database stores data in RDF format
* __Backend__: A REST API backend provides CRUD operations on the triplestore. Users are authenticated via OAuth2.0/OpenID Connect tokens
* __Frontend__: The frontend is a HTML/JS application which operates against the REST API and provides forms to users to enter data for CRUD operations. In a later step, the frontend will provide various advanced ways to search and browse through data in the backend.


Each component is hosted in a docker environment, and the composition of them is managed by docker-compose.

# Building

* Make sure you have installed [docker](https://docs.docker.com/machine/get-started/) and [docker-compose](https://docs.docker.com/compose/).

* (Optional) Create an initial dataset of institutions from the CORDIS dataset of the 7th EU framework programme:

<pre><b>cd tools;python csv2rdf_fp7organizations.py > ../webapp/secunity-backend/src/main/resources/datasets/fp7organizations.ttl</b></pre> 
(this takes a while, get a small coffee)

The command takes the original data from CORDIS in csv format and converts it to Turtle Triplets (TTL), which will be imported into the database.

* Build the REST backend:
<pre>
<b>$ webapp/secunity-backend/gradlew -p webapp/secunity-backend/ clean war</b>

:clean
:compileJava
:processResources
:classes
:war

BUILD SUCCESSFUL

Total time: 24.393 secs
</pre>

# Running

* Start all three microservices: `docker-compose up` (get another, larger coffee when running this command for the first time)

* Open <http://localhost/index.html>

# Stopping and clearing

* Stopping the whole thing is simple: Press `Ctrl-C` if you are still in docker-compose console, then run `docker-compose stop`. It can be restarted with `docker-compose up` again.

* If you made changes to the code, remember to recompile it with gradle and <i>delete the existing docker image</i> (at least of the REST API) before starting the application again: `docker rm webapp_rest_api`.

# How to use it/Features

At the moment, the webapplication is still in a very incomplete and prelimiary state. It supports adding new institutions via the frontend, converts given addresses to locations and display institutions either in an infinite-scrolling list, or on a map. That's it.