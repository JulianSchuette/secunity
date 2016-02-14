package de.fhg.aisec.secunity.db;

import java.net.URL;
import org.openrdf.repository.Repository;
import org.openrdf.repository.http.HTTPRepository;

public class TripletStore {
	private static TripletStore instance = null;

	public static TripletStore getInstance() {
		if (instance == null) {
			instance = new TripletStore();
		}
		return instance;
	}

	/**
		Connect to remote backend
		@param url http://example.org/openrdf-sesame/
		@param repoID example-db
	*/
	public Repository connect(URL url, String repoID) {
		Repository repo = new HTTPRepository(url.toString(), repoID);
		repo.initialize();
		return repo;
	}
}