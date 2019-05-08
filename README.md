# NASA 

Desarrollar una aplicación de escritorio que permita el consumo de servicios web de la NASA. Los
servicios web a considerar son los siguientes:

1. APOD (Astronomy Picture of the Day).
	1. Este servicio permite obtener la imagen del día así como información relacionada a ésta
	como es: Fecha, explicación, tipo de medio (imagen, audio, video), versión del servicio,
	título y url.
		1. La pantalla inicial del sistema a desarrollar, debe mostrar la información de éste
		servicio.
		2. Se debe dar posibilidad de descargar o guardar la información en una base de datos local
		para su posterior consulta. Construir pantalla que permita dicha consulta de información
		y permita generar un reporte en formato PDF de las imágenes de la última semana.

2. NASA Image and Video Library.
	1. Este servicio permite definir una cadena de búsqueda para encontrar imágenes, audios o
	videos de la biblioteca de la NASA. Una vez proporcionada la cadena de búsqueda, el
	servicio web regresa un conjunto de resultados relacionados. Algunos de los campos que
	regresa son: fecha de creación, descripción, palabras clave, tipo de medio(imagen, audio,
	video), enlace de la imagen o video, metadatos, etc. (consultar documentación para mayor
	información).
		1. Construir una pantalla dentro del sistema que permite capturar la cadena de búsqueda y
		posteriormente mostrar los resultados de dicha búsqueda. La pantalla debe ser similar al
		ejemplo mostrado en la página del servicio de la NASA (https://images.nasa.gov).
		2. Se debe dar posibilidad de descargar o guardar la información en una base de datos local
		para su posterior consulta (construir pantalla correspondiente).
		3. Al realizar cualquier búsqueda, guardar en la base de datos un historial de búsquedas en
		el sistema: cadena, fecha, hora y usuario. Construir una pantalla donde se pueda
		visualizar esta información y permitir generar reporte en formato PDF.

3. Elegir un tercer servicio web que ofrece la NASA e implementar en el sistema.
4. Enlaces importantes:
	1. API NASA: https://api.nasa.gov/
	2. APOD: https://api.nasa.gov/api.html#apod
	3. NASA Image and Video Library: https://api.nasa.gov/api.html#Images
	4. Ejemplos consumo de servicios web en Java:
	https://www.javacodegeeks.com/2012/09/simple-rest-client-in-java.html
5. Permitir el acceso al sistema en modo invitado o usuario registrado, para éste último, solicitar
nombre de usuario y contraseña para autenticarse al sistema.
