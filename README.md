CodeLab
=======

CodeLab is a Scala-based web application built on top of Core project for experimental purpose.

It covers but not limits to the technologies below.

- Spring MVC
- Spring Boot
- JPA
- Thymeleaf
- HTML5 + LESS
- jQuery + Dojo + AngularJS
- Ajax
- WebSocket

To launch the web application, type the following in command line console:

<pre>
git clone https://github.com/tcliu/codelab.git
git clone https://github.com/tcliu/core.git
gradle -PmainClass=codelab.WebLauncher -Dserver.port=8080 execute
</pre>

Visit http://localhost:8080/ to access the application
