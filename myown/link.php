<?php
$servername = "localhost";
$root = "root";
$password = "";

$conn = mysqli_connect($servername, $root, $password);
//if(mysqli_connect_errno()) {
//    echo "�������ݿ�ʧ�ܣ�" + mysqli_connect_error();
//} else {
//    echo "�������ݿ�ɹ�"."<br>";
//}

$select = mysqli_select_db($conn, "myown");
//if($select) {
//    echo "�л����ݿ�ɹ�"."<br>";
//}

mysqli_set_charset($conn, "utf8");
