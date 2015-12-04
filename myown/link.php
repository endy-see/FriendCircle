<?php
$servername = "localhost";
$root = "root";
$password = "";

$conn = mysqli_connect($servername, $root, $password);
//if(mysqli_connect_errno()) {
//    echo "连接数据库失败：" + mysqli_connect_error();
//} else {
//    echo "连接数据库成功"."<br>";
//}

$select = mysqli_select_db($conn, "myown");
//if($select) {
//    echo "切换数据库成功"."<br>";
//}

mysqli_set_charset($conn, "utf8");
