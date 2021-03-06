s1-ejemplo-rest
---------------------------------

MODULO IntelliJ que contiene un recurso rest (ClienteResource) que permite:

 - añadir: POST http://localhost:8080/s1-ejemplo-rest/rest/clientes/
             (hay que incluir los datos del cliente en formato xml en el cuerpo de la petición),
             
 - borrar: DELETE http://localhost:8080/s1-ejemplo-rest/rest/clientes/id
             (id es el identificador de un cliente existente en el sistema),
  
 - consultar:  GET http://localhost:8080/s1-ejemplo-rest/rest/clientes/id
             (id es el identificador de un cliente existente en el sistema),
             
 - y modificar los datos de un cliente en formato xml,
             PUT http://localhost:8080/s1-ejemplo-rest/clientes/rest/id
             (id es el identificador de un cliente existente en el sistema)
             (hay que incluir los datos del cliente en formato xml en el cuerpo de la petición)
   El servicio devuelve un código de estado "404 Not Found" si el cliente no se encuentra en la base de datos

Instrucciones para construir y desplegar el MÓDULO intelliJ s1-ejemplo-rest
===================

 - Arrancamos el servidor de aplicaciones desde línea de comandos
  
  > $HOME/wildfly-8.2.1.Final/bin/standalone.sh
  
 - Construimos el módulo y generamos el fichero s1-ejemplo-rest.war
Este nombre (sin la extensión) es el que figura en la etiqueta *finalName* del fichero pom.xml

  > mvn package

 - Desplegamos el fichero *s1-ejemplo-rest.war* en el servidor de aplicaciones

  > mvn wildfly:deploy

La ruta del servicio rest es : http://localhost:8080/s1-ejemplo-rest/rest/clientes/

Se proporcionan los ficheros *cliente1.xml*, *cliente2.xml* y *cliente3.xml* como ejemplos para probar el servicio, ya que nuestros servicio trabaja con información xml (en el directorio *src/main/resources*)

Formas de probar nuestro servicio
=======================
Podemos probar nuestro servicio de varias formas:

 1. Utilizando **CURL** desde línea de comandos:

 > curl -H "Accept: application/xml" -H "Content-Type: application/xml" -X POST -d @cliente1.xml --trace-ascii - http://localhost:8080/ejemplo-rest/rest/clientes/

  > curl -H "Accept: application/xml" -X GET --trace-ascii - http://localhost:8080/ejemplo-rest/rest/clientes/1

2. Utilizando POSTMAN desde nuestro navegador Chrome. Es la opción más flexible y cómoda.
Podemos "guardar" las peticiones y agruparlas en colecciones para ejecutarlas en cualquier otro momento

3. Utilizando el CLIENTE REST desde IntelliJ

  >Tools->Test RESTFul Web Service

Ejemplo:
HTTP method: POST
Host/port: http://localhost:8080
Path: /ejemplo-rest/rest/clientes

Request Headers
Accept: application/xml
Content-Type: application/xml

Request body
Seleccionamos File contents
File to sent: /Ruta/del/fichero/de/prueba/cliente1.xml

> Written with [StackEdit](https://stackedit.io/).