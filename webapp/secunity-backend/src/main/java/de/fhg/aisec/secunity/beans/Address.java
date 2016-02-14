package de.fhg.aisec.secunity.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Address {
	public String address;
	
	public Address() {} // JAXB needs this
	
	public Address(String address) {
		this.address = address;
	}

}
