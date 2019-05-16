<h1>Readme creator v1.0</h1>

 Consists of:
 * RESTfull web service;
 * Desktop client;
 * Web client.

<h3>RESTfull web service</h3>
Generates readme file in RTF format. For generating .rtf files Jrtf library is used.

<h3>Desktop client</h3>
Desktop application based on JavaFX, which connects to web service and downloads file from it.

<h3>Web client</h3>
MVC aplication based on JSF webframework, which also connects to web service and downloads file.

<h2>Deployment</h2>
For deployment purposes "deploy.bat" scripts are written for every application. They creates "deployment" folder with all that is needed to run application. Additional information about deploying and starting of each application is described in README file for each application.

<h2>v1.0</h2>
Through desktop client and web client users enters information about patch and url of web service. If everything is valid, data is sent to web service in JSON format. Web service generates patch file based on Template.rtf, saves it to MySQL database and sends file to clients.