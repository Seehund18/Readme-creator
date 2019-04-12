mkdir deployment
call mvn clean package
xcopy "target\*.jar" "deployment"
echo java -jar Web_service-0.1.0.jar > deployment\start.bat
pause
