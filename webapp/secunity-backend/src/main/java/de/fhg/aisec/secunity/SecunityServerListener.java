package de.fhg.aisec.secunity;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.fhg.aisec.secunity.db.TripleStore;

public class SecunityServerListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Initializing Database");
		TripleStore.getInstance();

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}



}
