<?php
header('Content-Type: application/json; charset=UTF-8'); //設定資料類型 json 編碼 utf-8

$accept = true; //如果接收資料格式正確才接收

$error_msg = ""; //記錄錯誤訊息


//----------接收資料並檢查送進來的資料是否有問題-------------------//
if ($_POST["uid"] != "") {
    $uid = $_POST["uid"];
} else { //不接受沒有資料的內容
    $accept = false;
    $error_msg = "uid資料為空"; //錯誤訊息
}
if ($_POST["nickname"] != "") {
    $nickname = $_POST["nickname"];
} else { //不接受沒有資料的內容
    $accept = false;
    $error_msg = "nickname資料為空"; //錯誤訊息
}
if ($_POST["mobile_barcode"] != "") {
    $mobile_barcode = $_POST["mobile_barcode"]; 
} else { //不接受沒有資料的內容ㄋ
    $accept = false;
    $error_msg = "mobile_barcode資料為空"; //錯誤訊息
}


if($accept){
    //-------------存取資料庫--------------//
    $severname = "192.168.2.200"; //SQL位置
    $username = "hoshiso1_system"; //帳號
    $password = "system123456"; //密碼
    $dbname = "hoshiso1_project"; //SQL名稱
    $link = mysqli_connect($severname, $username, $password, $dbname); // 建立MySQL的資料庫連結

    if ($link->connect_error) {
        wh_log("Connection failed: " . $link->connect_error); // 記錄連接失敗的錯誤訊息
    }
    $sql1 = "UPDATE `member` SET `nickname`='". $nickname ."',`mobile_barcode`='".$mobile_barcode ."' WHERE `uid` ='" . $uid ."'";
    $result=$link->query($sql1); // 執行 SQL 查詢
    $messageArr = array();
    $dataarray = array();
    $link->close(); // 關閉資料庫連結
    $dataarray = array(
        "uid" => $uid,
        "nickname" => $nickname,
        "mobile_barcode" => $mobile_barcode
    );

    $message = returnmsg($dataarray, "0", "Success",$amount); // 呼叫 returnmsg 函式，回傳訊息
    http_response_code(200); // 設定 HTTP 狀態碼為 200
    echo json_encode($message); // 將回傳訊息轉換為 JSON 格式並輸出

    $messageArr["status"]=array();




}else{ //對於資料POST不完整的處理
    $dataarray = [];
    $message = returnmsg($dataarray, "400", "資料有缺漏或資料格式錯誤(" . $error_msg .")"); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
    http_response_code(200);
    echo json_encode($message);
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
?>