## Test de Pruebas Unitarias y Consultas SQL

### Descripción del Proyecto

Este proyecto es una aplicación de tienda que utiliza Spring Boot, Maven y JPA para gestionar productos y fabricantes. 
Hemos implementado una serie de pruebas unitarias para verificar el correcto funcionamiento de las operaciones de consulta sobre los datos de productos y fabricantes.


### Importancia del Uso de Records

En este proyecto, hemos utilizado `records` para facilitar el acceso y manipulación de ciertos valores.
Los `records` en Java son una forma concisa de definir clases inmutables que solo contienen datos. Esto nos permite:

- **Simplificar el código**: Los `records` eliminan la necesidad de escribir código repetitivo como getters, setters, `equals`, `hashCode` y `toString`.
- **Mejorar la legibilidad**: Al utilizar `records`, el código se vuelve más legible y fácil de entender.
- **Facilitar el acceso a los datos**: Los `records` proporcionan una forma rápida y sencilla de acceder a los valores almacenados en ellos.

### Consultas SQL

Además de las pruebas unitarias, hemos realizado las mismas consultas utilizando sentencias SQL
Estas consultas se encuentran en el archivo `src/main/resources/test.sql`. Las consultas SQL permiten verificar los mismos resultados directamente en la base de datos, asegurando la consistencia y precisión de los datos.

