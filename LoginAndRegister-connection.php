<?php
//Connection details to establish connection with our server- Note: using username = "root" and password = "" also works
$servername = "localhost";
$username = "ahsan";
$dbname = "sql_connection";
$password = "thedark123";


//Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);

//Check connection
if(!$conn){
	die("Connectioned failed!" . mysqli_connect_error());
}
else{
	echo "<h2>Connect success!</h2>";
}

?>
