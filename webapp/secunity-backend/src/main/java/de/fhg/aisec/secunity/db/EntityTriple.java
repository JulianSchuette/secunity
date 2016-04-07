package de.fhg.aisec.secunity.db;

import org.openrdf.model.IRI;

public class EntityTriple extends Triple {

	public EntityTriple(String subject, IRI predicate, IRI object) {
		super(subject, predicate, object);
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setPredicate(IRI predicate) {
		this.predicate = predicate;
	}

	public void setObject(IRI object) {
		this.object = object;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public IRI getPredicate() {
		return predicate;
	}
	
	public IRI getObject() {
		return (IRI) object;
	}
	
	public boolean isEntityTriple() {
		return true;
	}
}
