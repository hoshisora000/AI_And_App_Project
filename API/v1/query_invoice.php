<?php
header('Content-Type: application/json; charset=UTF-8'); //設定資料類型 json 編碼 utf-8

//-------------接收資料-----------------//
if ($_GET["uid"] != "") {
    $uid = $_GET["uid"];
} else {
    $uid = ""; //不能接受這種內容，需做錯誤回傳(待補)
}

//-------------存取資料庫--------------//
$severname = "192.168.2.200"; //SQL位置
$username = "hoshiso1_system"; //帳號
$password = "system123456"; //密碼
$dbname = "hoshiso1_project"; //SQL名稱
$link = mysqli_connect($severname, $username, $password, $dbname); // 建立MySQL的資料庫連結

if ($link->connect_error) {
    wh_log("Connection failed: " . $link->connect_error);
}
$sql1 = "SELECT `uid`, `invoice_number`, `date`, `time`, `money` FROM `member_invoice` WHERE `uid` = '" . $uid ."'";
$result=$link->query($sql1);
$messageArr = array();
$dataarray = array();


if($result->num_rows > 0){
    while ($row = $result->fetch_assoc()) {
        $dataarray[]=$row;        
    }
}
$link->close();


$message = returnmsg($dataarray, "0", "Success");
http_response_code(200);
echo json_encode($message);

$messageArr["status"]=array();
function returnmsg($dataarray, $re_code, $re_msg)
{

    $messageArr["data"] = $dataarray;
    $messageArr["status"] = array();
    $today = date('Y-m-dH:i:s(p)');
    $datetime = array(
        "code" => $re_code,
        "message" => $re_msg,
        "datetime" => $today
    );
    $messageArr["status"] = $datetime;
    return $messageArr;
}
?>