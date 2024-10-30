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

-- Test n27

-- Test n28

-- Test n29

-- Test n30

-- Test n31

-- Test n32

-- Test n33

-- Test n34

-- Test n35

-- Test n36

-- Test n37

-- Test n38

-- Test n39