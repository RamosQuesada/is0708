<?php 
session_start();
if (!$_SESSION['codigo'])
{
	header("Location:identificarse.php");
}
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//ES" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

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
					<li><a href="calendario.php">Ver calendario</a></li>
				</ul>
				<h3>Volver a identificarse</h3>
				<ul class="sidemenu">
				<li><a href="Identificarse.php">identificarse</a></li></ul>
			</div>
			<div id="main">
				<?php
					$m=$_GET['codM'];
					@$db=new mysqli('localhost','root','is0708','turnomat_bd');
					if (mysqli_connect_errno())
						echo "No se puede conectar con la base de datos";
					else {
						$act="update mensaje set visto=1 where idmensaje=".$m;
						$registro=$db->query($act);
						$sql="select m.IdMensaje,m.Remitente,m.Fecha,m.Asunto,u.nombre,u.apellido1,u.apellido2,m.texto from MENSAJE m,usuario u where u.numvendedor=m.remitente and m.idmensaje=".$m;
						$registro=$db->query($sql);
						$nreg=$registro->num_rows;

						if($nreg == 0) {
							echo " no tiene mensajes";
							echo "<br>volver a identificarse <a href=identificarse.php>Identificarse</a>";
						} else {
							$reg=$registro->fetch_assoc();
							echo "<form action= method=post>
								<table align=center width=100%>";
							echo "<tr><td>Remitente:</td><td width=100%>". $reg['Remitente'] ." || ".$reg['nombre']." ".$reg['apellido1']." ".$reg['apellido2']."</td></tr>";
							echo "<tr><td>Fecha:</td><td>".$reg['Fecha']."</td></tr>";
							echo "<tr><td>Asunto:</td><td>".$reg['Asunto']."</td></tr>";
							echo "<tr><td valign=top colspan=2><textarea cols=50 rows=10 name=t readonly=true>".$reg['texto']."</textarea></td></tr>";
							echo "<tr><td align=center colspan=2><a href=respondermensaje.php?mensaje=".$reg['IdMensaje']."&remitente=".$reg['Remitente'].">Responder</a></td></tr>";

				?>
							</table></form>
							<table align="center"><tr>
								<td><a href="menu.php">Volver al menú</a></td>
								<td><a href="mensajes.php?cod=1">Ver todos los mensajes</a></td>
								<td align="right"><a href="mensajes.php?cod=2">Ver mensajes no leídos</a></td>
							</tr></table>

<?php
						}
						$registro->free();
						$db->close();
					}
?>




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