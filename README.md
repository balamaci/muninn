Muninn Java Alerting Framework for ElasticSearch
=====================
An open source (Apache License) framework in Java, to send alerts based on data stored Elastic Search.     

The [ELK stack](https://balamaci.ro/java-app-monitoring-with-elk-logstash/) is becoming more and more popular solution for logging. But what we found lacking was an alerting solution 
based on the data saved in ElasticSearch.
 
You might want to take a look at [Elastalert](). However while in ElastAlert most alerting usecases are covered, 
we needed to write some special custom rules and since we're more confortable in Java(and suck at Python), **Muninn** took flight.
This is not meant as a translation from python to java of Elastalert. 

I think while **ElastAlert** can be used even 
with basic understanding of ElasticSearch, Muninn rather provides a framework with which you can write the queries to ElasticSearch, process the response and turn it into 

Of course we'll try to cover most usescase so that maybe you can register . We'll give some examples on how you can tweek 
with little modification an existing processor or write one from scratch.
      
Concepts
---------------

  - Rules - they hold the information 
  - RuleProcessor - 
  - Converters - they take care of the emi. 
  - Alerters - 


Sample code
---------------
wip.




