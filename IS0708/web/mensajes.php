<?php 
session_start();

if (!$_SESSION['codigo'])
{
header("Location:identificarse.php");
}
$opcion=$_GET['cod'];

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
					<li><a href="calendario.php">Ver calendario</a></li>
				</ul>
				<h3>Volver a identificarse</h3>
				<ul class="sidemenu">
				<li><a href="Identificarse.php">identificarse</a></li></ul>
			</div>
			<div id="main">
				<?php
					@$db=new mysqli('localhost','root','is0708','turnomat_bd');
					if (mysqli_connect_errno())
						echo "No se puede conectar con la base de datos";
					else
					{
						//Crea el Indice de la Pagina Actual
						if(!isset($_GET['pag'])){
/*Si la Variable Pag no esta en la URL, la setea en  por defecto
Primera Pagina
*/
							$pag=1;
						}else{
/*
De lo contrario la Variable de Sistema $_GET captura la variable pasada por
la Url 'pag' y la asiga la Asigna a la Variable $pag 
*/
							$pag=$_GET['pag'];
						}
						/*
Esta Variable es la q indica la cantidad de registros a mostrarse en
cada una de las paginas del sistema de Paginacion
*/
						$hasta=8;	

/*
es aqui donde se indicara la primera posision del cursor para
recoger lso datos en la base de datos
*/
						$desde=($hasta*$pag)-$hasta;
						$linea="select count(*) num from destinatario  where numvendedor=".$_SESSION['codigo'];
						$todo=$db->query($linea);
						$r=$todo->fetch_assoc();
						$r2=$r['num'];
						$paginas=ceil($r2/$hasta);
						$todo->free();
						
						
						
						if ($opcion==1)
							$sql="select m.IdMensaje,m.Remitente,m.Fecha,m.Asunto,m.visto from MENSAJE m,DESTINATARIO d where d.IdMensaje=m.IdMensaje and d.NumVendedor=".$_SESSION['codigo']." order by Fecha desc LIMIT $desde,$hasta";
						elseif ($opcion==2)
						{
							$sql="select m.IdMensaje,m.Remitente,m.Fecha,m.Asunto,m.visto from MENSAJE m,DESTINATARIO d where d.IdMensaje=m.IdMensaje and (m.visto<>1 OR m.visto is NULL)  and d.NumVendedor=".$_SESSION['codigo']." order by Fecha desc LIMIT $desde,$hasta";
							//LIMIT ".$desde.",".$hasta;
						}

						$registro=$db->query($sql);
						$nreg=$registro->num_rows;

						if($nreg == 0)
						{
							echo "Usted no tiene mensajes";
							echo "<br>Volver a <a href=menu.php>Menu</a>";
						}
						else
						{

if($pag>1)
{
$prim="<th><a href=mensajes.php?pag=1&cod=".$opcion.">Primera</a></th><th> <a href=\"mensajes.php?pag=".($pag-1)."&cod=".$opcion."\"><< Ant</a></th>";
}
else
{
$prim="<th><b>Primera Página</B></th>";
}


if($pag<$paginas)
{
$ultm="<th><a href=\"mensajes.php?pag=".($pag+1)."&cod=".$opcion."\">Sig >></a></th><th> <a href=\"mensajes.php?pag=".($paginas)."&cod=".$opcion."\">Ultima</a></th>";
}
else
{
$ultm="<th><b>Ultima Página</B></th>";
}



echo '<table border=2 align=center valign=middle>';

echo "<tr>".$prim."<th>P&aacute;gina [".$pag."] de [".$paginas."]</th>".$ultm."</tr></table><br>";




							echo '<table width=100% align=center valign=middle><tr>
									<th></th>
									<th>Remitente</th>
									<th>Fecha</th>
									<th>Asunto</th></tr>';
									
							for ($i=0;$i<$nreg;$i++)
							{
								$reg=$registro->fetch_assoc();
								if ($reg['visto']==1) {
									$s1='';
									$s2='';
								} else {
									$s1='<b>';
									$s2='</b>';
								}

								echo '<tr>';
								echo '<td>'.$s1.'<a href=mostrarmensaje.php?codM='.$reg['IdMensaje'].'>Leer</a>'.$s2.'</td>
									<td>'.$s1.htmlspecialchars(stripslashes($reg['Remitente'])).$s2.'</td>
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