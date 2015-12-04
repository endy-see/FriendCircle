<?php
/**
 * Created by PhpStorm.
 * User: lenovo
 * Date: 2015/10/27
 * Time: 16:48
 */
require('link.php');

//$visitSql = "select username from user where userID='1'";
//$value = mysqli_query($conn, $visitSql);
//while($result = mysqli_fetch_array($value, MYSQL_ASSOC))
//    echo $result['username'];


$userID = $_POST["userID"];
$nickName = $_POST["nickName"];
$shuoshuo = $_POST["shuoshuo"];
$avatar = $_POST["avatar"];
$photos = $_POST["photos"];
//$shuoshuoString = $_POST["userInfoJson"];         //这个字符串中包含有id,nickname,avatar绝对路径，
$target_path = "./upload/";
$target_path = $target_path.basename($_FILES['file']['name']);

//将从客户端传过来的图片名称字符串分割为各个名字
//$userInfoArr =json_decode($shuoshuoString, true);
//$aShuoShuoInfo = $userInfoArr['aShuoShuo'];
//$infArr = json_decode($aShuoShuoInfo, true);
//$userID = $infArr['id'];
//$nickName = $infArr['nickName'];
//$avatar = $infArr['avatar'];
//$shuoshuo = $infArr['shuoshuo'];
//$photos = $infArr['photos'];


if($userID) {
//if(move_uploaded_file($_FILES['file']['tmp_name'], $target_path)) {
    $selectUser = "select * from user where userID='$userID'";
    if(mysqli_query($conn, $selectUser)) {
        $insertUser = "UPDATE user SET avatar='$avatar' WHERE userID='$userID'";
    } else {
        $insertUser = "insert into user (userID, avatar) values ('$userID', '$avatar')";
    }
    //先判断数据库中的pengyouquan表中有没有这个id。如果有，则更新，如果没有，则插入
    $selectPyq = "select * from pengyouquan where id in (select userID from user)";
    if(mysqli_query($conn, $selectPyq)) {
        $updatePyq = "UPDATE pengyouquan SET nickname='$nickName', shuoshuo='$shuoshuo', photos='$photos' WHERE id='$userID'" ;
    } else {
        $updatePyq = "insert into pengyouquan (id, nickname, shuoshuo, photos) values ('$userID', '$nickName', '$shuoshuo', '$photos')";
    }
    $get_first = mysqli_query($conn, $insertUser);
    $getPyq = mysqli_query($conn, $updatePyq);

    if($get_first ) {
//    if($result = mysqli_fetch_array($get_first, MYSQL_ASSOC))
        if($getPyq) {
            $arr = array('code' => 5, 'userID' => $userID,'nickName' => $nickName, 'shuoshuo' => $shuoshuo, 'photos' => $photos);
            echo JSON($arr);
        } else {
            $arr = array('code' => 1, 'userID' => $userID,'nickName' => $nickName, 'shuoshuo' => $shuoshuo, 'photos' => $photos);
            echo JSON($arr);
        }

    } else {
        $arr = array('code' => 2, 'userID' => $userID,'nickName' => $nickName, 'shuoshuo' => $shuoshuo, 'photos' => $photos);
        echo JSON($arr);
    }

//    if($getPyq) {
//        $arr = array('code' => 5, 'userID' => $userID,'nickName' => $nickName, 'shuoshuo' => $shuoshuo, 'photos' => $photos);
//        echo JSON($arr);
//    } else {
//        $arr = array('code' => 6, 'userID' => $userID,'nickName' => $nickName, 'shuoshuo' => $shuoshuo, 'photos' => $photos);
//        echo JSON($arr);
//    }
} else {
    $arr = array('code' => 3, 'userID' => $userID,'nickName' => $nickName, 'shuoshuo' => $shuoshuo, 'photos' => $photos);
    echo JSON($arr);
}

function JSON($array1) {
    arrayRecursive($array1, 'urlencode', true);
    $json1 = json_encode($array1);
    return urldecode($json1);
}

function arrayRecursive(&$array, $function, $apply_to_keys_also = false) {
    static $recursive_counter = 0;
    if(++$recursive_counter > 1000) {
        die('possible deep recursion attack');
    }

    foreach($array as $key => $value) {
        if(is_array($value)) {
            arrayRecursive($array[$key], $function, $apply_to_keys_also);
        } else {
            $array[$key] = $function($value);
        }

        if(is_string($key) && $apply_to_keys_also) {
            $new_key = $function($key);
            if($new_key != $key) {
                $array[$new_key] = $array[$key];
                unset($array[$key]);
            }
        }
    }

    $recursive_counter--;
}


