<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//ES" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<?php 
session_start();
?>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="es" lang="es">
<head>

<meta name="Description" content="Servicio Web Turnomatic." />
<meta name="Keywords" content="documentación, turnomatic, ingenería, software, ucm" />
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
					<li><a href="mensajes.php?cod=2">Mensajes no leídos</a></li>
					<li><a href="mensajesenviados.php">Mensajes enviados</a></li>
				</ul>
				<h3>Consultar horarios</h3>
				<ul class="sidemenu">
					<li>Toda la semana</li>
					<li>Todo el mes</li>
				</ul>
			</div>

			<div id="main">

				<table>
				<form action="crearnuevomensaje.php" method="post">
				<input type="hidden" name="remitente" value=<?php echo $_SESSION['codigo']; ?>>
				
				<?php
				@$db=new mysqli('localhost','root','','turnomat_bd');
				if (mysqli_connect_errno())
					echo "No se puede conectar con la base de datos.";
				else
				{
					$sql="select numvendedor,nombre,apellido1,apellido2 from usuario where numvendedor in (select numvendedor from departamentousuario where nombredepartamento = \"DatosFijos\")";
//      \"".$_SESSION['departamento']."\")";
					$registro=$db->query($sql);
					$nreg=$registro->num_rows;
					if($nreg == 0)
					{
						echo "No hay usuarios";
					}
					else
					{
						echo "<tr align=left><td>Destinatario</td><td><select name=destinatario width=50>";
						for ($i=0;$i<$nreg;$i++)
						{
							$reg=$registro->fetch_assoc();
							echo "<option value=".$reg['numvendedor'].">".$reg['numvendedor']." - ".$reg['nombre']." ".$reg['apellido1']." ".$reg['apellido2']."</option>";
						}
						echo "</select></td></tr>";
						echo "<tr align=\"left\"><td>Asunto</td><td><input name=\"asunto\" type=\"text\" size=50></td></tr>";
						echo "<tr align=\"left\" valign=\"top\"><td>Texto</td><td><textarea name=\"texto\" cols=\"50\" rows=\"25\"></textarea></td></tr>";
						echo "<tr><td align=right colspan=2><input type=\"reset\" value=\"Borrar\"> <input type=\"submit\" value=\"Enviar mensaje\"></td></tr>";

					}
					$registro->free();
					$db->close();
				}
				?>
				</form>
			<tr><td colspan=2 align=center><a href=menu.php>Volver al menu</a></tr></td></table></center>
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




