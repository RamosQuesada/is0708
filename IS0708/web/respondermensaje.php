<?php 
session_start();

if (!$_SESSION['codigo'])
{
header("Location:identificarse.php");
}
$men=$_GET['mensaje'];
$re=$_GET['remitente'];
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Documento sin t&iacute;tulo</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<?
@ $db=new mysqli('localhost','root','','test');
if (mysqli_connect_errno())
{
echo 'no te has connectado a la bd'; 
}
else
{
$sql="select * from  mensaje where IdMensaje=".$men;
$registros=$db->query($sql);

$reg=$registros->fetch_assoc();
$m=$reg['IdMensaje'];
$r=$reg['Remitente'];
$a=$reg['Asunto'];
$f=$reg['Fecha'];
$t=$reg['Texto'];
echo "<center><table align=center border=2><tr><td colspan=2 bgcolor=red>Respuesta al mensaje con la siguiente informacion</td></tr>";
echo "<tr><td>Idmensaje</td><td>".$m."</td></tr>";
echo "<tr><td>Remitente</td><td>".$r."</td></tr>";
echo "<tr><td>Asunto</td><td>".$a."</td></tr>";
echo "<tr><td>Fecha</td><td>".$f."</td></tr>";
echo "<tr><td>Texto</td><td>".$t."</td></tr>";
echo "<tr><td colspan=2><a href=menu.php>Ir al menu</a></tr></td>";
echo "</center></table>";
$registros->free();
}

$db->close();
?>

<table border="2" align="center">

<form action="crearnuevomensaje.php" method="post">
<input type="hidden" name="remitente" value=<?php echo $_SESSION['codigo']; ?>>
<?
@$db=new mysqli('localhost','root','','test');
if (mysqli_connect_errno())
echo "no te puedes conectar a la base de datos";
else
{
$sql="select numvendedor,nombre,apellido1,apellido2 from usuario";
$registro=$db->query($sql);
$nreg=$registro->num_rows;
if($nreg == 0)
{
echo " no hay usuarios";
}
else
{
echo "<tr align=left><td>Destinatario</td><td><select name=destinatario width=50>";

for ($i=0;$i<$nreg;$i++)
{
$reg=$registro->fetch_assoc();
if (htmlspecialchars(stripslashes($reg['numvendedor']))==$re)
{
echo "<option selected value=".$reg['numvendedor'].">".$reg['numvendedor']." - ".$reg['nombre']." ".$reg['apellido1']." ".$reg['apellido2']."</option>";
}
else
{
echo "<option value=".$reg['numvendedor'].">".$reg['numvendedor']." - ".$reg['nombre']." ".$reg['apellido1']." ".$reg['apellido2']."</option>";
}
}
echo "</select></td></tr>";
}
$registro->free();
$db->close();

}
?>
<tr align="left"><td>Asunto</td><td><input name="asunto" type="text" value="asunto" size=50></td></tr>
<tr align="left" valign="top"><td>Texto</td><td><textarea name="texto" cols="50" rows="25">Texto del mensaje</textarea></td></tr>
<tr><td><input type="reset" value="borrar"></td><td><input type="submit" value="enviar mensaje a destinatarios"></td></tr>
</form>
<tr><td colspan=2 align="center"><a href=menu.php>Ir al menu</a></tr></td></table></center>




</body>
</html>
