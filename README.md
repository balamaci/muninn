![Muninn](https://i.imgur.com/Z0eZlLi.png?1)


Java Alerting Framework for ElasticSearch
=====================
An open source (Apache License) framework in Java, to send alerts based on data stored ElasticSearch.     

The [ELK stack](https://balamaci.ro/java-app-monitoring-with-elk-logstash/) is becoming more and more popular solution for logging. But what we found lacking was an alerting solution 
based on the data saved in ElasticSearch.
 
You might want to take a look at [Elastalert](https://github.com/Yelp/elastalert). However while in ElastAlert most alerting usecases are covered, 
we needed to write some special custom rules and since we're comfortable in Java(and suck at Python), **Muninn** took flight.

**Muninn** is not meant as a translation from python to java of Elastalert. 

I think while **ElastAlert** can be used even 
with a very basic understanding of ElasticSearch, **Muninn** rather provides a framework and tools to write the queries 
to ElasticSearch yourself, process the response and turn it into events for alerting and use the provided modules to
send the alerts to their destinations. 

Of course we'll try to cover most usecases so you can just write a text config file or use such an existing usecase as a base to either tweak it or have the 
 basic tools to write one from scratch.      
            

Sample code
---------------

Rules configuration are loaded from the __/rules__ directory.

````
id=uncaught_exceptions
name="Uncaught Exceptions"   -- name to be used in alerts

type=list
min_events=5       -- minimum number of events to trigger the alerts

frequency=40 s     -- how often the query in ES is being run.  
timeframe=2 days   -- specifies "from" and "to" timerange

search {           -- this is the search that will be run in ElasticSearch 
    query_result_key_id="_id",
    template {
        name:custom              -- name of the template that can exist in 'conf/search.conf'   
        params {
            query="""            -- the '"""' allows us to assign a multiline value to our "query" prop  
             {
                "match": {"message" : "Uncaught Exception"}
             }
            """
        }
    }
    templates {    -- usually search templates are kept in 'conf/search.conf' for reuse among processors, but you can  
      custom:      -- just a name how we decided to call our template and it's referenced from above 'search.template.name' prop
       """
        {
         "filter": {
           "bool": {
                "must": [
                    {
                      "range" : {
                         "@timestamp" : {
                             "gt": "${from}",    -- parameters to be filled by the specific RuleProcessor   
                             "lte": "${to}",     
                             "format": "date_hour_minute_second_millis"
                         }
                      }
                    },
                    @import(search.template.params.query)   -- the @import for the templates means the multiline value 
                ]                                           -- under the prop 'search.template.params.query'
           }
        }
       }
       """
     }
}

alerter {
    realert {               -- this is the way through which we control not getting too many alerts for the same issues 
       filter_key="_id"     -- this key from the result is being used to determine if it's the same alert for which we already notified
       frequency=15 mins,   -- 
    }
    alert=[log, mail]       -- we log the events and we also alert by email
}
````


Defaults can be specified in **conf/muninn.conf**. 
Notice above we haven't set an email address where to notify, neither did we specify where the ES server is located.
We can set this globally in the **muninn.conf** :

```
elastic {
    endpoint="http://172.17.0.1:9200"   -- ElasticSearch endpoint 
    index_pattern="logstash*"           -- ElasticSearch index
}

alerter {
    email {
        server=smtp.gmail.com
        port=587
        protocol=tls
        ...                
    }
}

```

But also we can specify each value inside the rule config so as to override it if we want say a different email address
where we can notify in case of . 


Concepts
---------------

### Rule
Rules are just holders for configuration data - the ES query to run, how often to run the query, how to alert(just log, email, http-post), etc
The relation with the type .

### Rule Processor      
Every Rule has a **RuleProcessor**. They handle the important work of translating the JSON response from ES into **Events**.

Should you want to create your own rule processor it's good to extend **BaseRuleProcessor** which does the following:
  - Loads the search template defined in the rule 'search.template.name' and injects into the template, the parameters returned by 
  
    ```java
      public Map<String, String> searchParameters() {
      }
     ```
     
    Most rules probably are dependent of a timeframe - you want to get events that happened between a ${from} and ${to}-
so you can just make a call to **TimeframeParamsAware.searchParameters()**     
  

  - Performs the search in ES and return a JSonObject. How this data can be manipulated and extracted can be controlled
by returning a **Function<JsonObject, EventsHolder>** that takes as input the JsonObject parses it  
and produces a list of **Events**(held in **EventsHolder**).       
    **Events** - hold the data that will be sent in alerts.

     ```java
      protected abstract Function<JsonObject, EventsHolder> processResponseFunction();
     ```

  - Events can be further filtered and transformed before being sent inside Alerts
  
     ```java
     protected Function<EventsHolder, EventsHolder> processEventsFunction() {
        return Function.identity(); //identity means we leave the events unchanged
     }
     ```


### Alerting 
  - Alerters - different implementations about how to send the alerts - simplest is **log** 


### Realerting - preventing alerts for the same events 
We may want to run rules frequently so as to be notified very quickly that a high number of exception occurred and react quickly.   
But on the other hand we  To prevent alerting for the same , f we run it's very possible to Events have  


### Converters 
Take care of converting a group of **Events** to text. 

For example if we choose to alert by email, the email content is just a text, so we can use the 
**muninn-converter-freemarker** and specify a Freemarker template into which we pass the **Events**.



Implementing your own Rules
---------------

Muninn uses at it's core 