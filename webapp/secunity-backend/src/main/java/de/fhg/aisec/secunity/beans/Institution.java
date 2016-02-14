package de.fhg.aisec.secunity.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Institution {
	public String name;
	public Address address;
	
	public Institution() {} // JAXB needs this
	
	public Institution(String name, Address address) {
		this.name = name;
		this.address = address;
	}
}
