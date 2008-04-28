<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//ES" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<?php 
session_start();
if ($_SESSION)
{
$_SESSION['codigo']=NULL;
$_SESSION['nombre']=NULL;
$_SESSION['apellido1']=NULL;
$_SESSION['apellido2']=NULL;

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
if (!($_POST)) 
{
?>

				<table align="center">
					<tr>
						<td colspan="2">Por favor, identifíquese:</td>
					</tr>
					<form action="identificarse.php" method="post">
						<tr>
							<td>Numero de vendedor</td>
							<td> <input type="text" name="codigo" maxlength="8"></td>
						</tr>
						<tr>
							<td>Contraseña</td>
							<td> <input type="password" name="password"></td>
						</tr>
						<tr>
							<td align="center"><input name="reset" type="reset" value="Borrar"></td>
							<td align="center"> <input name="aceptar" type="submit" value="Aceptar"></td>
						</tr>
					</form>
				</table>

<?php 
}
else 
{
	$cod=trim($_POST['codigo']);
	$pas=trim($_POST['password']);

	@$db=new mysqli('localhost','root','is0708','turnomat_bd');
	//@$db=new mysqli('72.34.56.241','turnomat_user','is0708','turnomat_bd');
	//$bd=mysql_connect('localhost','turnomat_user','is0708');
	//mysql_select_db('turnomat_bd');
	if (mysqli_connect_errno())
		echo "No se puede conectar a la base de datos.";
	else
	{
		$sql="select * from USUARIO where NumVendedor=".$cod." and Password='".$pas."'";
		//$fila=mysql_query($sql,$bd);
		$registro=$db->query($sql);

		$nreg=$registro->num_rows;

		if($nreg == 0)
		{
			echo "Los datos introducidos no se corresponden con los de ningún usuario.";
			echo "<br>Intente volver a <a href=identificarse.php>identificarse</a>.";
		}
		else
		{
			$reg=$registro->fetch_assoc();
			$_SESSION['codigo']=$reg['NumVendedor'];
			$_SESSION['nombre']=$reg['Nombre'];
			$_SESSION['apellido1']=$reg['Apellido1'];
			$_SESSION['apellido2']=$reg['Apellido2'];

	
			$registro->free;
			//mysql_close($bd);
			$db->close;
			header("Location: menu.php");
		}
	//mysql_close($bd);

	//$registro->free;
	//$db->close;
	}
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




