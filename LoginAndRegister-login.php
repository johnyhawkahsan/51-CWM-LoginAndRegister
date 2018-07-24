<?php
require "LoginAndRegister-connection.php";

$user_email = $_POST["identifier_loginEmail"];
$user_pass = $_POST["identifier_loginPassword"];

//test the login
//$user_email = "ahsan@localhost.com";
//$user_pass = "password";

$mysql_query = "SELECT * FROM users WHERE email = '$user_email' AND password = '$user_pass'";
$result = mysqli_query($conn,$mysql_query); //This function receives 2 arguments, connection info received from "LoginAndRegister-connection.php" and query

//check the result- 
$rowcount = mysqli_num_rows($result);
if($rowcount > 0){
	$resultRow = mysqli_fetch_assoc($result); //The mysqli_fetch_assoc() function fetches a result row as an associative array
	
	$name = $resultRow["name"];
	$email = $resultRow["email"];
	
	//Here is the specially formatted string response.. ie: "ServerResponse".
	//It is of the form: "boolean,email,name"
	echo "true,".$email.",".$name;
}
else{
	echo "Login was not successful... :(";
}

?>
