 -- Profe cuidado con el nombre de la base de datos que se esta utilizando por si no te va
use tienda;
-- Test n1 Lista los nombres y los precios de todos los productos de la tabla producto
SELECT nombre, precio FROM producto;

-- Test n2 lista de Producto completa con el precio de euros convertido a dólares .
SELECT nombre,  ROUND(precio * 1.08 ,2) FROM producto;

-- Test n3  Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula
SELECT UPPER(nombre), precio FROM producto;

-- Test n4 Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre
SELECT nombre, UPPER(SUBSTRING(nombre, 1, 2)) as Iniciales FROM fabricante;

-- Test n5 Lista el código de los fabricantes que tienen productos.
SELECT DISTINCT codigo_fabricante FROM producto;

-- Test n6 Lista los nombres de los fabricantes ordenados de forma descendente.
SELECT nombre FROM fabricante ORDER BY nombre DESC;

-- Test n7 Lista los nombres de los productos ordenados en primer lugar por el nombre de forma ascendente y en segundo lugar por el precio de forma descendente.
SELECT nombre, precio FROM producto ORDER BY nombre ASC, precio DESC;

-- Test n8 Lista los 5 primeros fabricantes.
SELECT nombre FROM fabricante LIMIT 5;

-- Test n9 Devuelve una lista con 2 fabricantes a partir del cuarto fabricante. El cuarto fabricante también se debe incluir en la respuesta
SELECT nombre FROM fabricante LIMIT 2 OFFSET 3;

-- Test n10 Lista el nombre y el precio del producto más barato
SELECT nombre, precio FROM producto ORDER BY precio ASC LIMIT 1;

-- Test n11 Lista el nombre y el precio del producto más caro
SELECT nombre, precio FROM producto ORDER BY precio DESC LIMIT 1;

-- Test n12  Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.
SELECT nombre FROM producto WHERE codigo_fabricante = 2;

-- Test n13 Lista el nombre de los productos que tienen un precio menor o igual a 120€.
SELECT nombre FROM producto WHERE precio <= 120;

-- Test n14 Lista los productos que tienen un precio mayor o igual a 400€.
SELECT * FROM producto WHERE precio >= 400;

-- Test n15 Lista todos los productos que tengan un precio entre 80€ y 300€.
SELECT * FROM producto WHERE precio BETWEEN 80 AND 300;

-- Test n16 Lista todos los productos que tengan un precio mayor que 200€ y que el código de fabricante sea igual a 6.
SELECT * FROM producto WHERE precio > 200 AND codigo_fabricante = 6;

-- Test n17 Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de codigos de fabricantes para filtrar.
SELECT * FROM producto WHERE codigo_fabricante IN (1, 3, 5);

-- Test n18   Lista el nombre y el precio de los productos en céntimos
SELECT nombre, precio * 100 FROM producto;

-- Test n19 Lista los nombres de los fabricantes cuyo nombre empiece por la letra S
SELECT nombre FROM fabricante WHERE nombre LIKE 'S%';

-- Test n20 Devuelve una lista con los productos que contienen la cadena Portátil en el nombre.
SELECT nombre FROM producto WHERE nombre LIKE '%Portátil%';

-- Test n21 Devuelve una lista con el nombre de todos los productos que contienen la cadena Monitor en el nombre y tienen un precio inferior a 215 €.
SELECT  nombre FROM producto WHERE  nombre LIKE '%Monitor%' AND precio < 215;

-- Test n22  Lista el nombre y el precio de todos los productos que tengan un precio mayor o igual a 180€.
-- Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre (en orden ascendente)
SELECT nombre, precio FROM producto WHERE precio >= 180 ORDER BY precio DESC, nombre ASC;

-- Test n23 Devuelve una lista con el nombre del producto, precio y nombre de fabricante de todos los productos de la base de datos.
-- Ordene el resultado por el nombre del fabricante, por orden alfabético.
SELECT p.nombre, p.precio, f.nombre  FROM producto p  JOIN fabricante f
    ON p.codigo_fabricante = f.codigo ORDER BY f.nombre ASC;


-- Test n24 Devuelve el nombre del producto, su precio y el nombre de su fabricante, del producto más caro.
 SELECT p.nombre , p.precio , f.nombre FROM producto p JOIN
     fabricante f ON p.codigo_fabricante = f.codigo ORDER BY p.precio
 DESC LIMIT 1;

-- Test n25 Devuelve una lista de todos los productos del fabricante Crucial que tengan un precio mayor que 200€
SELECT p.nombre, p.precio, f.nombre FROM producto p JOIN fabricante f
ON p.codigo_fabricante = f.codigo WHERE f.nombre = 'Crucial' AND p.precio > 200;


-- Test n26 Devuelve un listado con todos los productos de los fabricantes Asus, Hewlett-Packard y Seagate
SELECT p.nombre, f.nombre FROM producto p JOIN fabricante f
ON p.codigo_fabricante = f.codigo WHERE f.nombre IN ('Asus', 'Hewlett-Packard', 'Seagate');

-- Test n27 Devuelve un listado de los nombres fabricantes que existen en la base de datos, junto con los nombres productos que tiene cada uno de ellos
-- El listado deberá mostrar también aquellos fabricantes que no tienen productos asociados.
SELECT f.nombre, p.nombre   FROM fabricante f LEFT JOIN producto p
ON f.codigo = p.codigo_fabricante;

-- Test n28 . Devuelve un listado de los nombres fabricantes que existen en la base de datos, junto con los nombres productos que tiene cada uno de ellos.
-- El listado deberá mostrar también aquellos fabricantes que no tienen productos asociados.
SELECT f.nombre, p.nombre   FROM fabricante f LEFT JOIN producto p
ON f.codigo = p.codigo_fabricante;


-- Test n29  Devuelve un listado donde sólo aparezcan aquellos fabricantes que no tienen ningún producto asociado
 SELECT f.nombre FROM fabricante f LEFT JOIN producto p
ON f.codigo = p.codigo_fabricante WHERE p.codigo_fabricante IS NULL;

-- Test n30 Calcula el número total de productos que hay en la tabla productos.
SELECT COUNT(*) as numProd FROM producto;

-- Test n31 Calcula el número de fabricantes con productos, utilizando un stream de Productos
SELECT COUNT(DISTINCT codigo_fabricante) as numFab FROM producto;

-- Test n32 Calcula la media del precio de todos los productos
SELECT  ROUND(AVG(precio),2) as mediaPrecio FROM producto;

-- Test n33 Calcula el precio más barato de todos los productos. No se puede utilizar ordenación de stream.
SELECT MIN(precio) as precioMin FROM producto;

-- Test n34 Calcula la suma de los precios de todos los productos
SELECT SUM(precio) as sumaPrecio FROM producto;

-- Test n35 Calcula el número de productos que tiene el fabricante Asus
SELECT COUNT(*) as numProd FROM producto p JOIN tienda.fabricante f on f.codigo = p.codigo_fabricante
WHERE lower(f.nombre) like 'Asus';

-- Test n36 Calcula la media del precio de todos los productos del fabricante Asus
SELECT ROUND(AVG(precio),2) as mediaPrecio FROM producto p  join tienda.fabricante f on f.codigo = p.codigo_fabricante
WHERE lower(f.nombre) like 'Asus';

-- Test n37 Muestra el precio máximo, precio mínimo, precio medio y el número total de productos que tiene el fabricante Crucial
SELECT MAX(p.precio) as precioMax,MIN(p.precio) as precioMin,ROUND(AVG(p.precio),2) as mediaPrecio,COUNT(p.codigo_fabricante) as numProd
FROM producto p JOIN fabricante f ON f.codigo = p.codigo_fabricante WHERE f.nombre = 'Crucial';


-- Test n38 Muestra el número total de productos que tiene cada uno de los fabricantes.
-- El listado también debe incluir los fabricantes que no tienen ningún producto.
--  El resultado mostrará dos columnas, una con el nombre del fabricante y otra con el número de productos que tiene.
-- Ordene el resultado descendentemente por el número de productos
SELECT f.nombre, COUNT(p.codigo_fabricante) as numProd FROM fabricante f LEFT JOIN producto p
ON f.codigo = p.codigo_fabricante GROUP BY f.nombre ORDER BY numProd DESC;


-- Test n39   Muestra el precio máximo, precio mínimo y precio medio de los productos de cada uno de los fabricantes.
SELECT f.nombre, MAX(p.precio) as precioMax, MIN(p.precio) as precioMin, ROUND(AVG(p.precio),2) as mediaPrecio FROM producto p JOIN fabricante f
ON f.codigo = p.codigo_fabricante GROUP BY f.nombre;

-- Test n40 uestra el precio máximo, precio mínimo, precio medio y el número total de productos de los fabricantes que tienen un precio medio superior a 200€
SELECT f.nombre, MAX(p.precio) as precioMax, MIN(p.precio) as precioMin, (AVG(p.precio)) as mediaPrecio,
       COUNT(p.codigo_fabricante) as numProd FROM producto p JOIN fabricante f
ON f.codigo = p.codigo_fabricante GROUP BY f.nombre HAVING AVG(p.precio) > 200;

-- Test n41 Devuelve un listado con los nombres de los fabricantes que tienen 2 o más productos.
SELECT f.nombre FROM fabricante f JOIN producto p
ON f.codigo = p.codigo_fabricante GROUP BY f.nombre HAVING COUNT(p.codigo_fabricante) >= 2;

-- Test n42 evuelve un listado con los nombres de los fabricantes y el número de productos que tiene cada uno con un precio superior o igual a 220 €.
-- Ordenado de mayor a menor número de productos.

select f.nombre, count(p.codigo_fabricante) as numProd from fabricante f join producto p
on f.codigo = p.codigo_fabricante where p.precio >= 220 group by f.nombre order by numProd desc;

-- Test n43 Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
SELECT f.nombre FROM fabricante f JOIN producto p
ON f.codigo = p.codigo_fabricante GROUP BY f.nombre HAVING SUM(p.precio) > 1000;


-- Test n44 Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
-- Ordenado de menor a mayor por cuantía de precio de los productos.
SELECT f.nombre FROM fabricante f JOIN producto p
ON f.codigo = p.codigo_fabricante GROUP BY f.nombre HAVING SUM(p.precio) > 1000 ORDER BY SUM(p.precio) ASC;

-- Test n45 Devuelve un listado con el nombre del producto más caro que tiene cada fabricante,
-- El resultado debe tener tres columnas: nombre del producto, precio y nombre del fabricante,
-- El resultado tiene que estar ordenado alfabéticamente de menor a mayor por el nombre del fabricante.
SELECT p.nombre, p.precio, f.nombre FROM producto p JOIN fabricante f
ON p.codigo_fabricante = f.codigo WHERE p.precio = (SELECT MAX(precio) FROM producto WHERE codigo_fabricante = f.codigo) ORDER BY f.nombre ASC;

-- Test n46 Devuelve un listado de todos los productos que tienen un precio mayor o igual a la media de todos los productos de su mismo fabricante,
-- Se ordenará por fabricante en orden alfabético ascendente y los productos de cada fabricante tendrán que estar ordenados por precio descendente.
SELECT p.nombre, p.precio, f.nombre FROM producto p JOIN fabricante f
ON p.codigo_fabricante = f.codigo WHERE p.precio >= (SELECT AVG(precio) FROM producto WHERE codigo_fabricante = f.codigo) ORDER BY f.nombre ASC, p.precio DESC;


