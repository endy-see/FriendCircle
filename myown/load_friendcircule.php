<?php
/**
 * Created by PhpStorm.
 * User: lenovo
 * Date: 2015/11/7
 * Time: 19:14
 */
include ('link.php');
$allItems = $_POST["allItems"];
if($allItems) {         //allItems��Ϊnull����ֱ�ӷ�������pengyouquan�б�������
    $query = mysqli_query($conn, "select * from pengyouquan");
    $allDatas = array();

    while($allShuoShuos = mysqli_fetch_array($query, MYSQL_ASSOC)) {

        $arr = array('nickname' => $allShuoShuos['nickname'],
            'shuoshuo' => $allShuoShuos['shuoshuo'],
            'photos' => $allShuoShuos['photos']);
        array_push($allDatas, $allShuoShuos['id'], json_encode($arr));              //�˴�����װjson����������ʲôЧ��
    }
    echo json_encode($allDatas);
} else {                //�ӿͻ��˷���������ʧ�ܣ������û����Ӧ
    echo "failure";
}

//$aShuoShuo = mysqli_fetch_array($query, MYSQL_ASSOC);
//if($aShuoShuo) {
//    $nickname = $aShuoShuo['nickname'];
//    $shuoshuo = $aShuoShuo['shuoshuo'];
//    $photos = $aShuoShuo['photos'];
//    $arr = array();
//
//    //����ֱ�����
//    echo json_encode($arr);
//} else {
//    echo "failure";
//}


