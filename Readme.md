<h1>Readme creator v0.1</h1>

 Consists of:
 * RESTfull web service;
 * Desktop client;
 * Web client.

Information about deploying and starting of each application is described in README.txt file to each application.

<h3>RESTfull web service</h3>
Generates readme file in RTF format. For generating .rtf files Jrtf library is used.

<h3>Desktop client</h3>
Desktop application on JavaFX, which connects to web service and downloads file from it.

<h3>Web client</h3>
MVC aplication based on JSF webframework, which also connects to web service and downloads file.

<h2>v0.1</h2>
Web service generates Hello World.rtf file with "Hello World!" phrase written in it.
Desktop client and web client can download this file from service. User can choose to which URL to connect. Last URL is saved into .property file in desktop client and cookies in web client.