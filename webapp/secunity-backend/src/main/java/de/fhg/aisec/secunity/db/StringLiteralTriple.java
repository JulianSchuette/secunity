package de.fhg.aisec.secunity.db;

public class StringLiteralTriple {
	String subject;
	String predicate;
	String object;

	public StringLiteralTriple(String subject, String predicate, String object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public void setObject(String object) {
		this.object = object;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getPredicate() {
		return predicate;
	}
	
	public String getObject() {
		return object;
	}

}
