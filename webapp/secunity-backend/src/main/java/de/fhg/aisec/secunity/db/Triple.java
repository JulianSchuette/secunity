package de.fhg.aisec.secunity.db;

import org.openrdf.model.IRI;
import org.openrdf.model.Value;

public class Triple {
	protected String subject;
	protected IRI predicate;
	protected Value object;

	public Triple(String subject, IRI predicate, Value object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setPredicate(IRI predicate) {
		this.predicate = predicate;
	}

	public void setObject(Value object) {
		this.object = object;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public Value getPredicate() {
		return predicate;
	}
	
	public Value getObject() {
		return object;
	}
	
	public boolean isEntityTriple() {
		return false;
	}

	public boolean isStringLiteralTriple() {
		return false;
	}
}