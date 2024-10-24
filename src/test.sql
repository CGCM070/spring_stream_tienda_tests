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

-- Test n11

-- Test n12