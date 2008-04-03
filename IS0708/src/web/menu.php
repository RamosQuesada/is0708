<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//ES" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<?php 
session_start();
?>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="es" lang="es">
<head>

<meta name="Description" content="Servicio Web Turnomatic." />
<meta name="Keywords" content="documentaci�n, turnomatic, ingener�a, software, ucm" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="Distribution" content="Global" />
<meta name="Author" content="IS 2007-2008. UCM Madrid, Spain" />
<meta name="Robots" content="index,follow" />

<link rel="stylesheet" href="images/Envision.css" type="text/css" />

<title>Servicio Web Turnomatic. IS 07-08 UCM</title>
	
</head>
<body>
<!-- wrap starts here -->
<div id="wrap">
		
		<!--header -->
		<div id="header"></div>

		<div id="content-wrap">
			<div id="sidebar">
				<h3>Consultar mensajes</h3>
				<ul class="sidemenu">
					<li><a href="nuevomensaje.php">Nuevo mensaje</a></li>
					<li><a href="mensajes.php?cod=1">Ver todos</a></li>
					<li><a href="mensajes.php?cod=2">Mensajes no le�dos</a></li>
					<li><a href="mensajesenviados.php">Mensajes enviados</a></li>
				</ul>
				<h3>Consultar horarios</h3>
				<ul class="sidemenu">
					<li>Toda la semana</li>
					<li>Todo el mes</li>
				</ul>
			</div>
			<div id="main">
				<h1>Bienvenido/a <?php echo $_SESSION['nombre'] ." ".$_SESSION['apellido1']." ".$_SESSION['apellido2']." Departamento ".$_SESSION['departamento'];?>
				</h1>
			</div>
		
		<!-- content-wrap ends here -->	
		</div>
					
		<!--footer starts here-->
		<div id="footer">
			
			<p>
			<strong>IS 2007-2008</strong> | 
			Design by: <a href="http://www.styleshout.com/">styleshout</a> | 
			Valid <a href="http://validator.w3.org/check?uri=referer">XHTML</a> | 
			<a href="http://jigsaw.w3.org/css-validator/check/referer">CSS</a>
			
   		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			
		<a href="contacto.html">Contacto</a> 
   		</p>
		</div>
</div>
</body>
</html>




