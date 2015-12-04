<?php
/**
 * Created by PhpStorm.
 * User: lenovo
 * Date: 2015/10/27
 * Time: 16:48
 */

if(move_uploaded_file($_FILES['file']['tmp_name'], "./upload/".$_FILES["file"]["name"])) {
    echo "success";
} else {
    echo "fail";
};


