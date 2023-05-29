<?php
header('Content-Type: application/json; charset=UTF-8'); //設定資料類型 json 編碼 utf-8

//----------接收資料並檢查送進來的資料是否有問題--------------------//
if ($_GET["uid"] != "") {
    $uid = $_GET["uid"];
} else { //不接受沒有資料的內容
    $accept = false;
    $error_msg = "uid資料為空"; //錯誤訊息 
}

//-------------存取資料庫--------------//
$severname = "192.168.2.200"; //SQL位置
$username = "hoshiso1_system"; //帳號
$password = "system123456"; //密碼
$dbname = "hoshiso1_project"; //SQL名稱
$link = mysqli_connect($severname, $username, $password, $dbname); // 建立MySQL的資料庫連結

if ($link->connect_error) {
    wh_log("Connection failed: " . $link->connect_error); // 記錄連接失敗的錯誤訊息
}
$sql1 = "SELECT * FROM `member` WHERE `uid` ='" . $uid ."'";
$result=$link->query($sql1); // 執行 SQL 查詢
$messageArr = array();
$dataarray = array();
$amount = $result->num_rows; // 取得查詢結果的列數

if($result->num_rows > 0){ // 若查詢結果有資料
    while ($row = $result->fetch_assoc()) { // 迴圈逐一取得資料列
        $dataarray[]=$row; // 將資料加入陣列中         
    }
}
$link->close(); // 關閉資料庫連結


$message = returnmsg($dataarray, "0", "Success",$amount); // 呼叫 returnmsg 函式，回傳訊息
http_response_code(200); // 設定 HTTP 狀態碼為 200
echo json_encode($message); // 將回傳訊息轉換為 JSON 格式並輸出

$messageArr["status"]=array();

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
?>