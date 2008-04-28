<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//ES" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<?php 
session_start();
if (!$_SESSION['codigo'])
{
	header("Location:identificarse.php");
}
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
<div id="wrap100">
		
		<!--header -->
		<div id="header"></div>

		<div id="content-wrap">
			<div id="main100">
			<?php
				$r=$_POST['remitente'];
				$d=$_POST['destinatario'];
				$a=$_POST['asunto'];
				$t=$_POST['texto'];
				@$db=new mysqli('localhost','root','is0708','turnomat_bd');
				if (mysqli_connect_errno())
				{
					echo 'No se puede conectar con la base de datos'; 
				}
				else
				{
				$consulta="insert into mensaje(remitente,asunto,texto,fecha,visto) values(".$r.",'".$a."','".$t."','".date("Y-m-d H:i:s")."',0)";
				$registros=$db->query($consulta);
				$sql="select max(idmensaje) numero from  mensaje";
				$registros=$db->query($sql);
				$reg=$registros->fetch_assoc();
				$m=$reg['numero'];
				$registros->free();
				$consulta="insert into destinatario values(".$d.",".$m." )";
				$registros=$db->query($consulta);
				$db->close();

				echo "<p align=center>Mensaje enviado correctamente a ".$d."</p>";
				echo "<p align=center><a href=menu.php>Volver al menu</a></p>";
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