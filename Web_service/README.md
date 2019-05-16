<h1>Web service</h1>

<h2>How to run</h2>
Thanks to Spring Boot, all that is needed is to package app into jar via Maven using "mvn clean package" and just run "Web_service.jar" (better to run jar via console to see all messages and control application). Note that Spring Boot starts Tomcat under the hood on "localhost:8080", so it's possible that some app is already running on your 8080 port and Tomcat will clashes with it.


Also to run web service you need MySQL database server with Template.rtf in it. Template is placed into 'additional' folder with 'serviceDB.sql' file which is MySQL 'service' database with 'files' table. You need to only import serviceDB into your database using this command (to use this command, path to mysql program should be specifed in the 'Path' system variable):

mysql -u <username> -p <databasename> < <filename.sql>

<h2>Scripts</h2>
deploy.bat in this folder will create deployment directory with start.bat and 'conf' folder with 'application.property'. In 'application.property' you can set properties for MySQL connection such as:
* url of database; 
* username;
* password and etc.


**NOTE**: url, username and password are mandatory. Some default info already written to this fields. You can also specify other properties for database connection.

**NOTE**: it's highly advised to insert '?serverTimezone=UTC' after your url just like in default url or write your database server time.


'additional' folder contains of Template.rtf file for generating patch readme files and serviceDB.sql which is MySQL 'service' database with 'files' table. Template.rtf already written to that table so it is  