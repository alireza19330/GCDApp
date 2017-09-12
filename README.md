Assumptions:
- This application developed and tested on Jboss EAP 7
- A JMS queue with JNDI "java:/jms/GCDQueue" should be available in the local JBoss
- Application uses in memory H2 database to persist data so after every restart data stored in memory will be removed
- In the push method of the RESTful web service there are two parameters called i1 and i2. they are added to the database and JMS queue in the same order. First i1 and after that i2 is added
- In the gcd method of the SOAP web service time-out threshold is 2 seconds for getting messages from the queue, after 2 seconds it will close the connection
- GCD is always positive, therefore if there is no any message in the queue to consume, the result of the gcd method will be -1
- According to the previous point the result of gcdSum method will be -1 if there is no gcd stored in the database
- The method gcdList will return null if there is no gcd stored in the database
-