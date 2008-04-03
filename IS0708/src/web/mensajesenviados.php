<?php 
session_start();

if (!$_SESSION['codigo'])
{
header("Location:identificarse.php");
}
?>

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
				<?php
					@$db=new mysqli('localhost','root','','turnomat_bd');
					if (mysqli_connect_errno())
						echo "No se puede conectar con la base de datos.";
					else {
						$sql="select m.IdMensaje,d.numvendedor,m.Fecha,m.Asunto,m.visto from MENSAJE m,DESTINATARIO d where d.IdMensaje=m.IdMensaje  and m.Remitente=".$_SESSION['codigo']." order by Fecha desc";

						$registro=$db->query($sql);
						$nreg=$registro->num_rows;

						if($nreg == 0) {
							echo "No hay mensajes enviados por usted.";
							echo "<br>Volver a <a href=identificarse.php>identificarse</a>";
						} else {

							echo '<table align=center valign=middle><tr>
								<td>IdMensaje</td>
								<td>Destinatario</td>
								<td>Fecha</td>
								<td>Asunto</td></tr>';
							for ($i=0;$i<$nreg;$i++) {
								$reg=$registro->fetch_assoc();
								if ($reg['visto']==1) {
									$s1='';
									$s2='';
								} else {
									$s1='<b>';
									$s2='</b>';
								}

								echo '<tr>';
								echo '<td>'.$s1.'<a href=mostrarmensajesenviados.php?codM='.$reg['IdMensaje'].'>Leer</a>'.$s2.'</td>
									<td>'.$s1.htmlspecialchars(stripslashes($reg['numvendedor'])).$s2.'</td>
									<td>'.$s1.htmlspecialchars(stripslashes($reg['Fecha'])).$s2.'</td>';
								echo '<td>'.$s1.htmlspecialchars(stripslashes($reg['Asunto'])).$s2.'</td></tr>';
							}
							echo "<tr><td colspan=2><a href=menu.php>Ir a Menu</a></td><td colspan=3 align=center><a href=nuevomensaje.php>Nuevo Mensaje</a></td></tr></table>";
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