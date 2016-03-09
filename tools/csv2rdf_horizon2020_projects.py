import sys,os
import csv
import urllib


def main():
#	rcn;reference;acronym;status;programme;topics;frameworkProgramme;title;startDate;endDate;projectUrl;objective;totalCost;ecMaxContribution;call;fundingScheme;coordinator;coordinatorCountry;participants;participantCountries;subjects
	
	with open('data.csv') as f:
  		spamreader = csv.reader(f, delimiter=';', quotechar='"')
		for data in spamreader:
			print('<http://secunity/%s> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://secunity/Project> .'%urllib.quote(data[0]))
			print('<http://secunity/%s> <http://secunity/has_reference> <http://secunity/%s> .'%(urllib.quote(data[0]),urllib.quote(data[1])))
			print('<http://secunity/%s> <http://secunity/has_acronym> <http://secunity/%s> .'%(urllib.quote(data[0]),urllib.quote(data[2])))
			print('<http://secunity/%s> <http://secunity/has_status> <http://secunity/%s> .'%(urllib.quote(data[0]),urllib.quote(data[3])))
			print('<http://secunity/%s> <http://secunity/in_programme> <http://secunity/%s> .'%(urllib.quote(data[0]),urllib.quote(data[4])))
			print('<http://secunity/%s> <http://secunity/has_topics> <http://secunity/%s> .'%(urllib.quote(data[0]),urllib.quote(data[5])))
			print('<http://secunity/%s> <http://secunity/in_framework_programme> <http://secunity/%s> .'%(urllib.quote(data[0]),urllib.quote(data[6])))
			print('<http://secunity/%s> <http://secunity/has_title> "%s"^^xsd:string .'%(urllib.quote(data[0]),data[7]))
			print('<http://secunity/%s> <http://secunity/has_start_date> "%s"^^xsd:date .'%(urllib.quote(data[0]),data[8]))
			print('<http://secunity/%s> <http://secunity/has_end_date> "%s"^^xsd:date .'%(urllib.quote(data[0]),data[9]))
			print('<http://secunity/%s> <http://secunity/has_url> <http://secunity/%s> .'%(urllib.quote(data[0]),urllib.quote(data[10])))
			print('<http://secunity/%s> <http://secunity/has_objective> "%s"^^xsd:string .'%(urllib.quote(data[0]),data[11]))
			print('<http://secunity/%s> <http://secunity/has_total_cost> <http://secunity/%s> .'%(urllib.quote(data[0]),urllib.quote(data[12])))
			print('<http://secunity/%s> <http://secunity/in_call> <http://secunity/%s> .'%(urllib.quote(data[0]),urllib.quote(data[14])))
			print('<http://secunity/%s> <http://secunity/has_funding_scheme> <http://secunity/%s> .'%(urllib.quote(data[0]),urllib.quote(data[15])))
			print('<http://secunity/%s> <http://secunity/has_coordinator> <http://secunity/%s> .'%(urllib.quote(data[0]),urllib.quote(data[16])))
			for participant in data[18].split(';'):
				print('<http://secunity/%s> <http://secunity/has_participant> <http://secunity/%s> .'%(urllib.quote(data[0]),urllib.quote(participant)))
			#print('<has_rcn>%s</has_rcn>'%data[0])
			#<http://secunity/Fraunhofer AISEC> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://secunity/Institution> .


if __name__ == '__main__':
	main()