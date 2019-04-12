call %CATALINA_HOME%/bin/startup.bat
call mvn tomcat7:deploy
START http://localhost:8081/Web_client
