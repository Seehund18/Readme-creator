mkdir deployment
call mvn clean package
xcopy "target\*.war" "deployment"
pause
