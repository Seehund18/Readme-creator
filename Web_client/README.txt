Running web client is a bit tricky. As usual, you need to run "mvn clean package" which wil create target directory with .war file.
Also you need to have one of the servlet containers (Tomcat, Glassfish or other) and deploy war file to it. I will describe how to do it with Tomcat:
1) Install Tomcat from tomcat.apache.org to any directory;
2) Set %CATALINA_HOME% system variable to your apache-tomcat-{version} file;
3) You need to set an user in apache-tomcat-{version}/conf/tomcat-users.xml. For example:
	<role rolename="manager-gui"/>
	<role rolename="manager-script"/>
	<user username="admin" password="admin" roles="manager-gui, manager-script"/>
4) Start Tomcat through apache-tomcat-{version}/bin/startup.bat;
4) Now you can go (by default) to "localhost:8080" -> Manager App, enter username and password for user from step 3 and deploy war from "target" directory.

NOTE that 8080 port may already be used by another app and in that case you have to change the port of Tomcat to another. For my machine, I set it to 8081, because at 8080 port is used by web service

NOTE: all logs for this client are written to %CATALINA_HOME%/logs/Web_client

deploy.bat for web client only move war file to deployment directory.

startWithTomcat.bat will run Tomcat, deploy war file to it and start browser. NOTE that startWithTomcat works only if you set your Tomcat to 8081 port as mine.

