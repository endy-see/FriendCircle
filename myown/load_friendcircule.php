<?php
/**
 * Created by PhpStorm.
 * User: lenovo
 * Date: 2015/11/7
 * Time: 19:14
 */
include ('link.php');
$allItems = $_POST["allItems"];
if($allItems) {         //allItems不为null，则直接返回所有pengyouquan列表项数据
    $query = mysqli_query($conn, "select * from pengyouquan");
    $allDatas = array();

    while($allShuoShuos = mysqli_fetch_array($query, MYSQL_ASSOC)) {

        $arr = array('nickname' => $allShuoShuos['nickname'],
            'shuoshuo' => $allShuoShuos['shuoshuo'],
            'photos' => $allShuoShuos['photos']);
        array_push($allDatas, $allShuoShuos['id'], json_encode($arr));              //此处不封装json，看看会有什么效果
    }
    echo json_encode($allDatas);
} else {                //从客户端发来的请求失败，服务端没有响应
    echo "failure";
}

//$aShuoShuo = mysqli_fetch_array($query, MYSQL_ASSOC);
//if($aShuoShuo) {
//    $nickname = $aShuoShuo['nickname'];
//    $shuoshuo = $aShuoShuo['shuoshuo'];
//    $photos = $aShuoShuo['photos'];
//    $arr = array();
//
//    //试着直接输出
//    echo json_encode($arr);
//} else {
//    echo "failure";
//}


