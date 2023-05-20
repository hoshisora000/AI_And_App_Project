<?php
header('Content-Type: application/json; charset=UTF-8'); //設定資料類型 json 編碼 utf-8
date_default_timezone_set("Asia/Taipei"); //設定時間時區

//-------------接收資料-----------------//
if ($_POST["uid"] != "") {
    $uid = $_POST["uid"];
} else {
    $uid = ""; //不能接受這種內容，需做錯誤回傳(待補)
}
if ($_POST["invoice_number"] != "") {
    $invoice_number = $_POST["invoice_number"];
} else {
    $invoice_number = ""; //不能接受這種內容，需做錯誤回傳(待補)
}
if ($_POST["date"] != "") {
    $date = $_POST["date"];
} else {
    $date = ""; //不能接受這種內容，需做錯誤回傳(待補)
}
if ($_POST["time"] != "") {
    $time = $_POST["time"];
} else {
    $time = ""; //不能接受這種內容，需做錯誤回傳(待補)
}
if ($_POST["money"] != "") {
    $money = $_POST["money"];
} else {
    $money = ""; //不能接受這種內容，需做錯誤回傳(待補)
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

//$sql1 = "SELECT IF( EXISTS( SELECT * FROM member_invoice WHERE invoice_number='" . $invoice_number . "'), 1, 0)";
$sql1 = "SELECT `uid`, `invoice_number`, `date`, `time`, `money` FROM `member_invoice` WHERE `invoice_number` = '" . $invoice_number ."'";
$result1 = mysqli_query($link,$sql1);
$row = mysqli_num_rows($result1) ;
if ($row==0) { 
    $sql = "INSERT INTO `member_invoice` (`uid`, `invoice_number`, `date`, `time`, `money`) VALUES ('" . $uid . "', '" . $invoice_number . "', '" . $date . "', '" . $time . "', '" . $money . "') ";
    try {
        $result = $link->query($sql);
        if ($_POST["uid"] != "") {
            //回傳結果
            $dataarray = array(
                "uid" => $uid,
                "invoice_number" => $invoice_number
            );
            $message = returnmsg($dataarray, "0", "Success");

        } else {
            $dataarray = [];
            
            $message = returnmsg($dataarray, "404", "Error");
        }
        mysqli_close($link); // 關閉資料庫連結
        http_response_code(200);
        echo json_encode($message);
    } catch (Exception $e) {
        echo 'Message: ' . $e->getMessage();
        echo "\n";
        echo $sql;
    }
}else{            
    $dataarray = [];
    $message = returnmsg($dataarray, "404", "重複的發票號碼");
    http_response_code(200);
    echo json_encode($message);

}
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
function wh_log($log_msg)
{

    $log_time = date('Y-m-d H:i:s');
    $log_filename = "error_log";
    $log_msg = '[' . $log_time . '] ' . $log_msg;

    if (!file_exists($log_filename)) {
        // create directory/folder uploads.
        mkdir($log_filename, 0777, true); // mkdir(pathname[, mode[, recursive[, context]]])
    }
    $log_file_data = $log_filename . '/log_' . date('m-d-H-i-s') . '.log';
    file_put_contents($log_file_data, $log_msg . "\n", FILE_APPEND);
}
?>