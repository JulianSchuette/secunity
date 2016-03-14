#!/usr/bin/python
import sys,os
import csv
import urllib
import uuid

def main():
#	rcn;reference;acronym;status;programme;topics;frameworkProgramme;title;startDate;endDate;projectUrl;objective;totalCost;ecMaxContribution;call;fundingScheme;coordinator;coordinatorCountry;participants;participantCountries;subjects
	
	with open('cordis-fp7organizations.csv') as f:
  		spamreader = csv.reader(f, delimiter=';', quotechar='"')
		for data in spamreader:
			print('<http://secunity/%s> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://secunity/Organisation> .'%urllib.quote(data[5]))
			print('<http://secunity/%s> <http://secunity/has_short_name> "%s"^^xsd:string .'%(urllib.quote(data[5]),urllib.quote(data[6])))
			print('<http://secunity/%s> <http://secunity/has_full_name> "%s"^^xsd:string .'%(urllib.quote(data[5]),urllib.quote(data[5])))
			print('<http://secunity/%s> <http://secunity/address_country> "%s"^^xsd:string .'%(urllib.quote(data[5]),urllib.quote(data[10])))
			print('<http://secunity/%s> <http://secunity/address_street> "%s"^^xsd:string .'%(urllib.quote(data[5]),urllib.quote(data[11])))
			print('<http://secunity/%s> <http://secunity/address_city> "%s"^^xsd:string .'%(urllib.quote(data[5]),urllib.quote(data[12])))
			print('<http://secunity/%s> <http://secunity/address_postcode> "%s"^^xsd:string .'%(urllib.quote(data[5]),urllib.quote(data[13])))
			print('<http://secunity/%s> <http://secunity/participated_in_project> <http://secunity/%s> .'%(urllib.quote(data[5]),urllib.quote(data[2])))
			if data[3] == 'coordinator':
				print('<http://secunity/%s> <http://secunity/coordinated_project> <http://secunity/%s> .'%(urllib.quote(data[5]),urllib.quote(data[2])))

			print('<http://secunity/%s> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://secunity/Person> .'%(uuid.uuid4()))
			print('<http://secunity/%s> <http://secunity/has_title> "%s"^^xsd:string .'%(urllib.quote(data[5]),urllib.quote(data[16])))
			print('<http://secunity/%s> <http://secunity/has_first_name> "%s"^^xsd:string .'%(urllib.quote(data[5]),urllib.quote(data[17])))
			print('<http://secunity/%s> <http://secunity/has_last_name> "%s"^^xsd:string .'%(urllib.quote(data[5]),urllib.quote(data[18])))
			print('<http://secunity/%s> <http://secunity/has_phone_number> "%s"^^xsd:string .'%(urllib.quote(data[5]),urllib.quote(data[20])))
			print('<http://secunity/%s> <http://secunity/has_fax_number> "%s"^^xsd:string .'%(urllib.quote(data[5]),urllib.quote(data[21])))
			#print('<has_rcn>%s</has_rcn>'%data[0])
			#<http://secunity/Fraunhofer AISEC> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://secunity/Institution> .


if __name__ == '__main__':
	main()