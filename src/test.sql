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
SELECT nombre FROM producto WHERE precio >= 400;

-- Test n15 Lista todos los productos que tengan un precio entre 80€ y 300€.
SELECT nombre FROM producto WHERE precio BETWEEN 80 AND 300;

-- Test n16

-- Test n17

-- Test n18

-- Test n19

-- Test n20

-- Test n21

-- Test n22

-- Test n23

-- Test n24

-- Test n25

-- Test n26

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