<?php
header('Content-Type: multipart/form-data; charset=UTF-8'); //設定資料類型 json 編碼 utf-8

$accept = true; //如果接收資料格式正確才接收
//----------接收資料並檢查送進來的資料是否有問題--------------------//
if ($_POST["uid"] != "") {
    $uid = $_POST["uid"];
} else { //不接受沒有資料的內容
    $accept = false;
    $error_msg = "uid資料為空"; //錯誤訊息
}
if ($_POST["invoice_number"] != "") {
    $invoice_number = $_POST["invoice_number"];
} else { //不接受沒有資料的內容
    $accept = false;
    $error_msg = "invoice_number資料為空"; //錯誤訊息
}
echo $_POST["uid"];
echo $accept;

if($accept){
    //-------------存取資料庫--------------//
    $severname = "192.168.2.200"; //SQL位置
    $username = "hoshiso1_system"; //帳號
    $password = "system123456"; //密碼
    $dbname = "hoshiso1_project"; //SQL名稱
    $link = mysqli_connect($severname, $username, $password, $dbname); // 建立MySQL的資料庫連結

    if ($link->connect_error) {
        wh_log("Connection failed: " . $link->connect_error); // 記錄連接失敗的錯誤訊息
        $dataarray = [];
        $message = returnmsg($dataarray, "500", "內部伺服器錯誤"); //回傳錯誤代碼500，錯誤訊息:內部伺服器錯誤。
        http_response_code(200);
        echo json_encode($message);
        exit();
    }
    $sql1 = "DELETE FROM `member_invoice` WHERE `uid`= '" . $uid ."'AND `invoice_number` = '" . $invoice_number ."'";
    $result=$link->query($sql1); // 執行 SQL 查詢
    $messageArr = array();
    $dataarray = array();
    $amount = $result->num_rows; // 取得查詢結果的列數
    $dataarray = [];
    $link->close(); // 關閉資料庫連結


    $message = returnmsg($dataarray, "200", "Success",$amount); // 呼叫 returnmsg 函式，回傳訊息
    http_response_code(200); // 設定 HTTP 狀態碼為 200
    echo json_encode($message); // 將回傳訊息轉換為 JSON 格式並輸出

    $messageArr["status"]=array();
}else{
    $dataarray = array(
        "uid" => $uid,
        "invoice_number" => $invoice_number
    );
    $message = returnmsg($dataarray, "404", "Error");
}
// -------------其他函式定義--------------//

// 產生回傳訊息的函式
function returnmsg($dataarray, $re_code, $re_msg,$amount)
{

    $messageArr["data"] = $dataarray; // 設定回傳訊息的資料部分為查詢結果的陣列
    $messageArr["status"] = array();
    $today = date('Y-m-dH:i:s(p)'); // 取得當前日期和時間
    $datetime = array(
        "amount" => $amount,
        "code" => $re_code,
        "message" => $re_msg,
        "datetime" => $today
    );
    $messageArr["status"] = $datetime; // 設定回傳訊息的狀態部分為包含相關資訊的陣列
    return $messageArr; // 回傳完整的訊息陣列
}
// 記錄錯誤訊息的函式
function wh_log($log_msg)
{

    $log_time = date('Y-m-d H:i:s');
    $log_filename = "error_log";
    $log_msg = '[' . $log_time . '] ' . $log_msg;

    if (!file_exists($log_filename)) {
        // 建立資料夾
        mkdir($log_filename, 0777, true); // mkdir(pathname[, mode[, recursive[, context]]])
    }
    $log_file_data = $log_filename . '/log_' . date('m-d-H-i-s') . '.log';
    file_put_contents($log_file_data, $log_msg . "\n", FILE_APPEND);
}
?>