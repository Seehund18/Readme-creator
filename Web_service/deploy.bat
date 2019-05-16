mkdir deployment
call mvn clean package
xcopy "target\*.jar" "deployment"
move conf deployment
echo java -jar Web_service-1.0.jar --spring.config.location=conf/application.properties > deployment\start.bat
echo pause >> deployment\start.bat
pause
