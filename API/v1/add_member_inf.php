<?php
header('Content-Type: application/json; charset=UTF-8'); //設定資料類型 json 編碼 utf-8
date_default_timezone_set("Asia/Taipei"); //設定時間時區

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


if($accept){
    //-------------存取資料庫--------------//
    $severname = "192.168.2.200"; //SQL位置
    $username = "hoshiso1_system"; //帳號
    $password = "system123456"; //密碼
    $dbname = "hoshiso1_project"; //SQL名稱
    $link = mysqli_connect($severname, $username, $password, $dbname); // 建立MySQL的資料庫連結

    if ($link->connect_error) {
        wh_log("Connection failed: " . $link->connect_error);
    }

    //-------------檢查發票號碼是否重複-------------//

    // 檢查發票號碼是否已存在於資料庫中
    $sql1 = "SELECT * FROM `member` WHERE `uid`= '" . $uid ."'";
    $result1 = mysqli_query($link,$sql1);
    $row = mysqli_num_rows($result1) ;
    if ($row==0) { 
        //--------上傳資料到資料庫--------//
        $sql = "INSERT INTO `member`(`uid`, `nickname`, `mobile_barcode`) VALUES ('" . $uid . "', '" . $nickname . "','" . $mobile_barcode . "') ";
        echo $sql;
        try {
            $result = $link->query($sql);
            if ($_POST["uid"] != "") {
                // 回傳成功的訊息
                $dataarray = array(
                    "uid" => $uid,
                );
                $message = returnmsg($dataarray, "201", "Success");

            } else {
                // 回傳錯誤的訊息
                $dataarray = [];
                $message = returnmsg($dataarray, "404", "Error");
            }
            mysqli_close($link); // 關閉資料庫連結
            http_response_code(200);
            echo json_encode($message);
        } catch (Exception $e) {
            wh_log('Message: ' . $e->getMessage() . "\n當前使用SQL語法:" . $sql); //將錯誤訊息通過呼叫函式的方式寫入error_log
            $dataarray = [];
            $message = returnmsg($dataarray, "500", "內部伺服器錯誤"); //回傳錯誤代碼500，錯誤訊息:內部伺服器錯誤。
            http_response_code(200);
            echo json_encode($message);
        }
    }else{            
        $dataarray = [];
        $message = returnmsg($dataarray, "404", "無法重複新增會員資料"); //回傳錯誤代碼404，錯誤訊息:重複的發票號碼。
        http_response_code(200);
        echo json_encode($message);

    }


}else{ //對於資料POST不完整的處理
    $dataarray = [];
    $message = returnmsg($dataarray, "400", "資料有缺漏或資料格式錯誤(" . $error_msg .")"); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
    http_response_code(200);
    echo json_encode($message);
}


// -------------其他函式定義--------------//
// 產生回傳訊息的函式
function returnmsg($dataarray, $re_code, $re_msg)
{

    $messageArr["data"] = $dataarray; // 設定回傳訊息的資料部分為查詢結果的陣列
    $messageArr["status"] = array();
    $today = date('Y-m-d-H:i:s(p)'); // 取得當前日期和時間
    $datetime = array(
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