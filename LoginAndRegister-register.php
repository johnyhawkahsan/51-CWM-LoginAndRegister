<?php
require "LoginAndRegister-connection.php";

$user_name = $_POST["identifier_name"];
$user_pass = $_POST["identifier_password"];
$user_email = $_POST["identifier_email"];

//test variables for testing if data is inserted into databse table "users"
//$user_name = "Ahsan";
//$user_pass = "password";
//$user_email = "ahsan@localhost.com";

$query = "INSERT INTO users(name,email,password) VALUES ('$user_name','$user_email','$user_pass')";

if(mysqli_query($conn,$query)){
	echo "<h2>Data Successfully Inserted in User's table!</h2>" ;
}
else{
	echo "<h2>Data was unable to be inserted into database :(.</h2>" . mysqli_error($conn);
	//I was facing error "Field 'id' doesn't have a default value". Now I changed ID to auto-increment and now it works.
}


?>
