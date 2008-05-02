<?php 
session_start();

if (!$_SESSION['codigo']) {
	header("Location:identificarse.php");
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0063)http://javascripts.astalaweb.com/Calendarios/Calendario%204.htm -->
<HTML><HEAD><meta name="Description" content="Servicio Web Turnomatic." />
<meta name="Keywords" content="documentación, turnomatic, ingenería, software, ucm" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="Distribution" content="Global" />
<meta name="Author" content="IS 2007-2008. UCM Madrid, Spain" />
<meta name="Robots" content="index,follow" />

<link rel="stylesheet" href="images/Envision.css" type="text/css" />

<title>Servicio Web Turnomatic. IS 07-08 UCM</title></HEAD>
<BODY style="FONT-FAMILY: Verdana" onload=thisMonth()>
<div id="wrap100">
		
		<!--header -->
		<div id="header"></div>

		<div id="content-wrap">
			<div id="main100">

<SCRIPT language=JavaScript>

//Form button calendar (author unknown)
//For this script and more
//Visit http://javascriptkit.com

function setDate(str) {

 if (str == "   ") {
  return;
 }

 mnth1 = document.forms[0].month.value;
 mnth = mnth1;
 mnth++;
 year = document.forms[0].year.value;
 dateStr = str+" / "+mnth+" / "+year;

 dateStr = trim(dateStr);
 
 //document.forms[1].dateField.value = dateStr;
 document.forms.formulario.dia.value= str;

  document.forms.formulario.mes.value= mnth;
  document.forms.formulario.ano.value= year;



}//setDate()

/**
 * The function removes spaces from the selected date.
 */

function trim(str) {

 res="";

 for(var i=0; i< str.length; i++) {
   if (str.charAt(i) != " ") {
     res +=str.charAt(i);
  }
 }
   
 return res;

}//trim()
 
/**
 * The method to get the Month name given the Month number of the year.
 */

function getMonthName(mnth) {

 if (mnth == 0) {
  name = "Enero";
 }else if(mnth==1) {
  name = "Febrero";
 }else if(mnth==2) {
  name = "Marzo";
 }else if(mnth==3) {
  name = "Abril";
 }else if(mnth==4) {
  name = "Mayo";
 } else if(mnth==5) {
  name = "Junio";
 } else if(mnth==6) {
  name = "Julio";
 } else if(mnth==7) {
  name = "Agosto";
 } else if(mnth==8) {
  name = "Septiembre";
 } else if(mnth==9) {
  name = "Octubre";
 } else if(mnth==10) {
  name = "Noviembre";
 } else if(mnth==11) {
  name = "Diciembre";
 }

 return name;

}//getMonthName()

/**
 * Get the number of days in the month based on the year.
 */

function getNoOfDaysInMnth(mnth,yr) {
 
 rem = yr % 4;

 if(rem ==0) {
   leap = 1;
 } else {
  leap = 0;
 }

 noDays=0;

 if ( (mnth == 1) || (mnth == 3) || (mnth == 5) ||
      (mnth == 7) || (mnth == 8) || (mnth == 10) ||
      (mnth == 12)) {
  noDays=31;
 } else if (mnth == 2) {
           noDays=28+leap;
        } else {
           noDays=30;
 }

 //alert(noDays);
 return noDays;
 
      
}//getNoOfDaysInMnth()
  
/**
 * The function to reset the date values in the buttons of the 
 * slots.
 */

function fillDates(dayOfWeek1,noOfDaysInmnth) {

 for(var i=1; i<43; i++) {
   str = "s"+i;
   document.forms[0].elements[str].value="   ";
 }


 startSlotIndx = dayOfWeek1;
 slotIndx = startSlotIndx;

 for(var i=1; i<(noOfDaysInmnth+1); i++) {
  slotName = "s"+slotIndx;

  val="";
  if (i<10) {
    val = " "+i+" ";
  } else {
    val = i;
  }

  document.forms[0].elements[slotName].value = val;
  slotIndx++;
 }
  
}//fillDates()
 

/**
 * The function that is called at the time of loading the page.
 * This function displays Today's date and also displays the 
 * the calendar of the current month.
 */

function thisMonth() {

  dt = new Date();
  mnth  = dt.getMonth(); /* 0-11*/
  dayOfMnth = dt.getDate(); /* 1-31*/
  dayOfWeek = dt.getDay(); /*0-6*/
  yr = dt.getFullYear(); /*4-digit year*/
 


  //alert("month:"+(mnth+1)+":dayofMnth:"+dayOfMnth+":dayofweek:"+dayOfWeek+":year:"+yr);

  mnthName = getMonthName(mnth)+ " ";
  document.forms[0].month.value = mnth;
  document.forms[0].year.value = yr;
  document.forms[0].currMonth.value = mnth;
  document.forms[0].currYear.value = yr;
  
  document.forms[0].monthYear.value = mnthName+yr;
  //document.forms[1].dateField.value = dayOfMnth+"/"+(mnth+1)+"/"+yr;
  document.forms.formulario.dia.value= dayOfMnth;
    document.forms.formulario.mes.value= (mnth+1);
  document.forms.formulario.ano.value= yr;
  

  startStr = (mnth+1)+"/1/"+yr;
  dt1 = new Date(startStr);
  dayOfWeek1 = dt1.getDay(); /*0-6*/

  noOfDaysInMnth = getNoOfDaysInMnth(mnth+1,yr);
  fillDates(dayOfWeek1+1,noOfDaysInMnth);
 

}//thisMonth()

/**
 * The function that will be used to display the calendar of the next month.
 */

function nextMonth() {

 var currMnth = document.forms[0].month.value;
 currYr = document.forms[0].year.value;

 if (currMnth == "11") {
    nextMnth = 0;
    nextYr = currYr;
    nextYr++;
 } else {
   nextMnth=currMnth;
   nextMnth++;
   nextYr = currYr;
 }

 mnthName = getMonthName(nextMnth);
 document.forms[0].month.value=nextMnth;
 document.forms[0].year.value=nextYr;
 document.forms[0].monthYear.value= mnthName+" "+nextYr;

 str = (nextMnth+1)+"/1/"+nextYr;
 dt = new Date(str);
 dayOfWeek = dt.getDay();

 noOfDays = getNoOfDaysInMnth(nextMnth+1,nextYr);

 fillDates(dayOfWeek+1,noOfDays);
 

}//nextMonth()

/**
 * The method to display the calendar of the previous month.
 */

function prevMonth() {

 var currMnth = document.forms[0].month.value;
 currYr = document.forms[0].year.value;

 if (currMnth == "0") {
    prevMnth = 11;
    prevYr = currYr;
    prevYr--;
 } else {
   prevMnth=currMnth;
   prevMnth--;
   prevYr = currYr;
 }

 str = (prevMnth+1)+"/1/"+prevYr;
 dt = new Date(str);
 dayOfWeek = dt.getDay();

 /***********************************************
  * Remove the comment if do not want the user to 
  * go to any previous month than this current month.
  ***********************************************/

 /*

 runningMonth = document.forms[0].currMonth.value;
 rMonth=runningMonth;
 rMonth++;
 runningYear = document.forms[0].currYear.value;
 rYear=runningYear;

 str = (rMonth)+"/1/"+rYear;
 dt1 = new Date(str);
 
 if (dt.valueOf() < dt1.valueOf()) {
   alert('Cannot Go Before Current Month');
   return;
 }
 
 */

 /**************************************************
 * End of comment
 **************************************************/

 mnthName = getMonthName(prevMnth);
 document.forms[0].month.value=prevMnth;
 document.forms[0].year.value=prevYr;
 document.forms[0].monthYear.value= mnthName+" "+prevYr;

 noOfDays = getNoOfDaysInMnth(prevMnth+1,prevYr);
 fillDates(dayOfWeek+1,noOfDays);
 
}//prevMonth()

</SCRIPT>
 
<TABLE align=center bgColor=#baa0a0 border=1>
  <TBODY>
  <TR>
    <TD ALIGN=CENTER>
      <FORM><!-- Hidden fields ---><INPUT type=hidden name=month> <INPUT 
      type=hidden name=year> <INPUT type=hidden name=currMonth> <INPUT 
      type=hidden name=currYear> <!-- End of Hidden fields --->
      <INPUT onclick=prevMonth() type=button value="<<" name=prev> 
<INPUT size=15 name=monthYear> <INPUT onclick=nextMonth() type=button value=">>" name=next>
      
	<TABLE cellSpacing=0 cellPadding=0 width=150 align=center bgColor=#d4d4d4 
      border=1>
        <TBODY>
        <TR bgColor=#10a0a0>
          <TD width="14%"><FONT size=1><STRONG>DOM</STRONG></FONT></TD>
          <TD width="14%"><FONT size=1><STRONG>LUN</STRONG></FONT></TD>
          <TD width="14%"><FONT size=1><STRONG>MAR</STRONG></FONT></TD>
          <TD width="14%"><FONT size=1><STRONG>MIÉ</STRONG></FONT></TD>
          <TD width="14%"><FONT size=1><STRONG>JUE</STRONG></FONT></TD>
          <TD width="15%"><FONT size=1><STRONG>VIE</STRONG></FONT></TD>
          <TD width="15%"><FONT size=1><STRONG>SÁB</STRONG></FONT></TD></TR>
        <TR>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=" 1 " name=s1></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=" 2 " name=s2></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=" 3 " name=s3></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=" 4 " name=s4></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=" 5 " name=s5></TD>
          <TD align=middle width="15%"><INPUT onclick=setDate(this.value); type=button value=" 6 " name=s6></TD>
          <TD align=middle width="15%"><INPUT onclick=setDate(this.value); type=button value=" 7 " name=s7></TD></TR>
        <TR>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=" 8 " name=s8></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=" 9 " name=s9></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=10 name=s10></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=11 name=s11></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=12 name=s12></TD>
          <TD align=middle width="15%"><INPUT onclick=setDate(this.value); type=button value=13 name=s13></TD>
          <TD align=middle width="15%"><INPUT onclick=setDate(this.value); type=button value=14 name=s14></TD></TR>
        <TR>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=15 name=s15></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=16 name=s16></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=17 name=s17></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=18 name=s18></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=19 name=s19></TD>
          <TD align=middle width="15%"><INPUT onclick=setDate(this.value); type=button value=20 name=s20></TD>
          <TD align=middle width="15%"><INPUT onclick=setDate(this.value); type=button value=21 name=s21></TD></TR>
        <TR>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=22 name=s22></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=23 name=s23></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=24 name=s24></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=25 name=s25></TD>
          <TD align=middle width="14%"><INPUT onclick=setDate(this.value); type=button value=26 name=s26></TD>
          <TD align=middle width="15%"><INPUT onclick=setDate(this.value); type=button value=27 name=s27></TD>
          <TD align=middle width="15%"><INPUT onclick=setDate(this.value); type=button value=28 name=s28></TD></TR>
        <TR>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value=29 name=s29></TD>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value=30 name=s30></TD>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value="   " name=s31></TD>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value="   " name=s32></TD>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value="   " name=s33></TD>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value="   " name=s34></TD>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value="   " name=s35></TD></TR>
        <TR>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value="   " name=s36></TD>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value="   " name=s37></TD>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value="   " name=s38></TD>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value="   " name=s39></TD>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value="   " name=s40></TD>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value="   " name=s41></TD>
          <TD align=middle><INPUT onclick=setDate(this.value); type=button value="   " name=s42></TD></TR></TBODY></TABLE></FORM>
      <FORM name=formulario action=CALENDARIO.php method=post>
      <TABLE cellSpacing=1 cellPadding=1 border=0>
        <TBODY>
        <TR>
          <TD align="center">Seleccionado el dia <input type=text name=dia size=2 maxlength="2"> de <input type=text name=mes size=2> del año <input type=text name=ano size=4></TD>
         </TR>
		 <tr><td ALIGN=CENTER><input type=submit name=enviar value="Mostrar Horario"></form></td></tr>
		 <tr><td ALIGN=CENTER><a href=menu.php>Volver al menu</a></td></tr></TBODY></TABLE>



<?
if($_POST)
{

$a=$_POST['ano'];

$m=$_POST['mes'];

$d=$_POST['dia'];

$f=$a.'-'.$m.'-'.$d;


if ($m == 1) {
  $MES = "Enero";
 }else if($m==2) {
  $mes = "Febrero";
 }else if($m==3) {
  $mes = "Marzo";
 }else if($m==4) {
  $mes = "Abril";
 }else if($m==5) {
  $mes = "mayo";
 } else if($m==6) {
  $mes = "Junio";
 } else if($m==7) {
  $mes = "Julio";
 } else if($m==8) {
  $mes = "Agosto";
 } else if($m==9) {
  $mes = "Septiembre";
 } else if($m==10) {
  $mes = "Octubre";
 } else if($m==11) {
  $mes = "Noviembre";
 } else if($m==12) {
  $mes = "Diciembre";
 }



ECHO '<table ALIGN=CENTER><TR><TD COLSPAN=2 ALIGN=CENTER BGCOLOR=#C7C7C7>Día '.$d.' de '.$mes.' del '.$a.' </TD></TR>';
@$db=new mysqli('localhost','root','is0708','turnomat_bd');
if (mysqli_connect_errno())
echo "No se puede conectar con la base de datos";
else {

$sql="select * from trabaja where numvendedor=".$_SESSION['codigo']." AND FECHA = '".$f."'";
$registro=$db->query($sql);
$nreg=$registro->num_rows;
}

if($nreg == 0) {
echo " <tr><TD COLSPAN=2 ALIGN=CENTER BGCOLOR=#C7C7C7>NO TIENE HORARIO ASIGNADO O NO TRABAJA ESE DIA</TD></TR>";
}
 else 
{
$reg=$registro->fetch_assoc();

echo " <TR><TD ALIGN=CENTER BGCOLOR=#C7C7C7> HORA DE ENTRADA ".$reg['HoraEntrada']."</td><td ALIGN=CENTER BGCOLOR=#C7C7C7>HORA DE SALIDA ".$reg['HoraSalida'].'</td></TR></TABLE>';
}
}
?>
</table></TD></TR></TABLE></TBODY>


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

</BODY></HTML>
