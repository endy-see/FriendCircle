<?php
include 'link.php';
$username = $_GET['username'];
$password = $_GET['password'];

$sql = "select * from user where username = '$username' and password = '$password'";

$query = mysqli_query($conn, $sql);

if($result = mysqli_fetch_array($query, MYSQL_ASSOC)) {
    echo "exist";
} else {
    echo "error";
}



//while($result = mysqli_fetch_array($query, MYSQL_ASSOC)) {
//    if(strcmp($result['username'], $username) == 0) {
//        echo "exist";
//        break;
//    }
//
//}


//echo "error";

//$arr = mysqli_fetch_array($query, MYSQL_ASSOC);