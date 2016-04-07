package de.fhg.aisec.secunity.db;

import org.openrdf.model.IRI;
import org.openrdf.model.Value;
import org.openrdf.sail.memory.model.MemLiteral;

public class StringLiteralTriple extends Triple {

	public StringLiteralTriple(String subject, IRI predicate, String object) {
		super(subject, predicate, new MemLiteral(null, object));
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setPredicate(IRI predicate) {
		this.predicate = predicate;
	}

	public void setObject(String object) {
		this.object = new MemLiteral(this, object);
	}
	
	public String getSubject() {
		return subject;
	}
	
	public IRI getPredicate() {
		return predicate;
	}
	
	public Value getObject() {
		return object;
	}

}
