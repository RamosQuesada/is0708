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
echo "no te puedes conectar a la base de datos";
else
{

$sql="select m.IdMensaje,m.Fecha,m.Asunto,u.nombre,u.apellido1,u.apellido2,m.texto,d.numvendedor from MENSAJE m,usuario u,destinatario d where  d.idmensaje=m.idmensaje and u.numvendedor=d.numvendedor and d.idmensaje=".$m;
$registro=$db->query($sql);
$nreg=$registro->num_rows;

if($nreg == 0)
{
echo " no tiene mensajes";
echo "<br>volver a <a href=menu.php>Menu</a>";
}
else
{
$reg=$registro->fetch_assoc();


echo "<form action= method=post><table border=2 align=center><table align=center width=100%>";
echo "<tr><td>Codigo:</td><td>".$reg['IdMensaje']."</td></tr>";
echo "<tr><td>Enviado a:</td><td>".$reg['numvendedor']." || Nombre :". $reg['apellido1']." ".$reg['apellido2']." - ".$reg['nombre']."</td></tr>";
echo "<tr><td>Fecha :</td><td>".htmlspecialchars(stripslashes($reg['Fecha']))."</td></tr>";
echo "<tr><td>Asunto:</td><td>".$reg['Asunto']."</td></tr>";
echo "<tr><td valign=top>Texto :</td><td><textarea cols=50 rows=10 name=t readonly=true>".$reg['texto']."</textarea></td></tr>";
?>

</table></form>
<table align="center" border="2"><tr><td><a href="menu.php">Ir a Menu</a></td><td><a href="mensajesenviados.php?cod=1">Ver Mensajes Enviados</a></td><td align="right"><a href="mensajes.php?cod=1">Mensajes Recibidos</a></td></tr></table>
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