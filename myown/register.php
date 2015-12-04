<?php
include 'link.php';

$username = $_POST['username'];
$password = $_POST['password'];

$sql = "select * from user where username = '$username'";
$query = mysqli_query($conn, $sql);
if($result = mysqli_fetch_array($query)) {
    echo "-1";
} else {
    $insert = mysqli_query($conn, "insert into user (username, password) values ('$username', '$password')");
    if($insert)
        echo "1";
    else
        echo "0";
}



//
//$data = $_POST;
//$data = json_decode($data);
//echo "receive app info!";
//if($data) {
//    var_dump($data);
//    $username = $data['username'];
//    $password = $data['password'];
//
//    $arr = array('username' => $username);
//    $arr = array('password' => $password);
//    echo json_decode($arr);
//
//} else {
//    $arr = array('name' => 'err');
//    echo  json_encode($arr);
//}


//$getJson = file_get_contents('php://input');
//$data = json_decode($getJson);
//var_dump($data);
//if($data) {
//    return "request success";
//}

//$sql = "insert into user (username, password) values ('zhym','111')";
//if(mysqli_query($conn, $sql))
//    echo "insert success";


