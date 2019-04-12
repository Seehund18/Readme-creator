mkdir deployment
call mvn clean package
xcopy "target\*jar-with-dependencies.jar" "deployment"
echo java -jar Desktop_client-0.1.0-jar-with-dependencies.jar > deployment\start.bat
pause
