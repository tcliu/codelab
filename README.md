CodeLab
=======

CodeLab is a Scala-based web application built on top of Core project for experimental purpose.

It covers but not limited to the technologies below.

- Spring MVC
- Spring Boot
- JPA
- Thymeleaf
- HTML5 + LESS
- jQuery + Dojo + AngularJS
- Ajax
- WebSocket

Launching the web application

git clone https://github.com/tcliu/codelab.git
git clone https://github.com/tcliu/core.git
gradle -PmainClass=codelab.WebLauncher -Dserver.port=8080 execute

Accessing the web application

http://localhost:8080/