package de.fhg.aisec.secunity.db;

import java.net.URL;

import org.openrdf.repository.Repository;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.repository.sail.config.SailRepositoryConfig;
import org.openrdf.sail.inferencer.fc.config.DirectTypeHierarchyInferencerConfig;
import org.openrdf.sail.memory.config.MemoryStoreConfig;

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
		// Use RemoteRepoManager to check if repo exists
	    RemoteRepositoryManager manager = new RemoteRepositoryManager(url.toString());
	    manager.initialize();
	    if (manager.getRepository("secunity")==null) {
	    	//Create repo, if not there
	    	createRemoteRepository(manager);
	    }
		
	    //Connect to repo.
		Repository repo = new HTTPRepository(url.toString(), repoID);
		repo.initialize();
		return repo;
	}

	/**
	 * Creates a repository in a remote OpenRDF server.
	 * 
	 * @param manager
	 */
	private void createRemoteRepository(RemoteRepositoryManager manager) {
	    //Configure new repo
		RepositoryConfig config = new RepositoryConfig();
	    config.setID("secunity");
	    
	    // We make it a normal SAIL (Storage and Inference Layer) repo
	    SailRepositoryConfig epConfig = new SailRepositoryConfig();
	    
	    // We set inference to direct types only, for performance reasons
	    DirectTypeHierarchyInferencerConfig cType = new DirectTypeHierarchyInferencerConfig();
	    
	    // We want the repo to be persisted
	    MemoryStoreConfig memConf = new MemoryStoreConfig();
	    memConf.setPersist(true);
	    cType.setDelegate(memConf);
	    epConfig.setSailImplConfig(cType);
	    config.setRepositoryImplConfig(epConfig);
	    
	    // Give it a title and set it up.
	    config.setTitle("Secunity repo");
	    config.validate();
	    manager.addRepositoryConfig(config);
	}
}