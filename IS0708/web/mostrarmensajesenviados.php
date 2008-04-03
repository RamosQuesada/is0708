<?php 
session_start();

if (!$_SESSION['codigo'])
{
header("Location:identificarse.php");
}

?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Documento sin t&iacute;tulo</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<?php
$m=$_GET['codM'];

@$db=new mysqli('localhost','root','','turnomat_bd');
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
echo "<br>volver a identificarse <a href=identificarse.php>Identificarse</a>";
}
else
{
$reg=$registro->fetch_assoc();


echo "<form action= method=post><table border=2 align=center>";
echo "<tr><td>Codigo mensaje :</td><td><input name=cm value=".$reg['IdMensaje']." size = 50 readonly=true></td></tr>";
echo "<tr><td>Destinatario :</td><td><input size = 3 name=r readonly=true value=".$reg['numvendedor'].">  Nombre :". $reg['apellido1']." ".$reg['apellido2']." - ".$reg['nombre']."</td></tr>";
echo "<tr><td>Fecha :</td><td><input name=f value=".$reg['Fecha']." size = 50 readonly=true></td></tr>";
echo "<tr><td>Asunto :</td><td><input name=a value=".$reg['Asunto']." size = 50 readonly=true></td></tr>";
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
 
</body>
</html>