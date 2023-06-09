<?php
header('Content-Type: application/json; charset=UTF-8'); //設定資料類型 json 編碼 utf-8

$accept = true; //如果接收資料格式正確才接收
$error_msg = ""; //記錄錯誤訊息

//----------接收資料並檢查送進來的資料是否有問題-------------------//
if ($_POST["uid"] != "") {
    $uid = $_POST["uid"];
} else { //不接受沒有資料的內容
    $accept = false;
    $error_msg = "uid資料為空"; //記錄錯誤訊息
}
if ($_POST["year_month"] != ""){
    $year_month = $_POST["year_month"];
} else { //不接受沒有資料的內容
    $accept = false;
    $error_msg = "year_month資料為空"; //記錄錯誤訊息 
}


if($accept){
    $year = ((int) substr($year_month,0,  3) + 1911)."";
    $month = substr($year_month, -2);
    $start_day;// 每期的開始日期(單月1日)
    $end_day;// 每期的結束日期隔天(單月1日)

    //---------------將期數轉換成日期-----------------//
    switch ($month) {
        case "01":
            $start_day= $year ."-" . "01-01" ;
            $end_day= $year ."-" . "02-01" ;
        break;
        case "02":
            $start_day= $year ."-" . "02-01" ;
            $end_day= $year ."-" . "03-01" ;
        break;        
        case "03":
            $start_day= $year ."-" . "03-01" ;
            $end_day= $year ."-" . "04-01" ;
        break;
        case "04":
            $start_day= $year ."-" . "04-01" ;
            $end_day= $year ."-" . "05-01" ;
        break;
        case "05":
            $start_day= $year ."-" . "05-01" ;
            $end_day= $year ."-" . "06-01" ;
        break;
        case "06":
            $start_day= $year ."-" . "06-01" ;
            $end_day= $year ."-" . "07-01" ;
        break;
        case "07":
            $start_day= $year ."-" . "07-01" ;
            $end_day= $year ."-" . "08-01" ;
        break;
        case "08":
            $start_day= $year ."-" . "08-01" ;
            $end_day= $year ."-" . "09-01" ;
        break;
        case "09":
            $start_day= $year ."-" . "09-01" ;
            $end_day= $year ."-" . "10-01" ;
        break;
        case "10":
            $start_day= $year ."-" . "10-01" ;
            $end_day= $year ."-" . "11-01" ;
        break;
        case "11":
            $start_day= $year ."-" . "11-01" ;
            $end_day= $year ."-" . "12-01" ;
        break;
        case "12":
            $start_day= $year ."-" . "12-01" ;
            $end_day= (((int)$year)+1) ."-" . "01-01" ;
        break;
        default:
            $error_msg = "month日期格式錯誤"; //錯誤訊息 
            $dataarray = [];
            $message = returnmsg(0,$dataarray, "400",  $error_msg); //回傳錯誤代碼400與錯誤訊息。
            http_response_code(200);
            echo json_encode($message);
            exit();
        }

    //-------------存取資料庫--------------//
    $severname = "192.168.2.200"; //SQL位置
    $username = "hoshiso1_system"; //帳號
    $password = "system123456"; //密碼
    $dbname = "hoshiso1_project"; //SQL名稱
    $link = mysqli_connect($severname, $username, $password, $dbname); // 建立MySQL的資料庫連結

    if ($link->connect_error) {
        // 如果連接失敗，呼叫函式將錯誤訊息記錄到伺服器中
        wh_log("Connection failed: " . $link->connect_error);
        // 回傳錯誤訊息，通知使用者
        $dataarray = [];
        $message = returnmsg($dataarray, "500", "內部伺服器錯誤"); //回傳錯誤代碼500，錯誤訊息:內部伺服器錯誤。
        http_response_code(200);
        echo json_encode($message);
        //結束程式
        exit();
    }


    //--------------查詢使用者的發票------------------//
    $sql1 = "SELECT `uid`, `invoice_number`, `date` FROM `member_invoice` WHERE `uid` = '" . $uid ."' AND `date`>='". $start_day ."' AND `date`<'". $end_day ."'AND  `time`>'11:59:59' AND `time`< '18:00:00'";
    $result=$link->query($sql1); // 執行 SQL 查詢
    $amount = $result->num_rows; // 取得查詢結果的列數
    echo $sql1;
    if ($amount ==0) { // 該用戶沒有任何發票
        $message = returnmsg($amount,$dataarray, "200", "無資料"); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
        http_response_code(200);
        echo json_encode($message);
        exit;
    }else{ // 若查詢結果有資料
        while ($row = $result->fetch_assoc()) { // 迴圈逐一取得資料列
            $dataarray[] = $row; // 將中獎資料新增到回傳資料中。
        }
    }
    $messageArr = array();
    $link->close(); // 關閉資料庫連結
    // 呼叫函示產生回傳訊息
    $message = returnmsg($amount,$dataarray, "200", "查詢成功");

    http_response_code(200); // 設定 HTTP 狀態碼為 200
    echo json_encode($message); // 將回傳訊息轉換為 JSON 格式並輸出
}else{ //對於資料POST不完整的處理
    $dataarray = [];
    $message = returnmsg(0,$dataarray, "400", "資料有缺漏或資料格式錯誤(" . $error_msg .")"); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
    http_response_code(200);
    echo json_encode($message);
}

// -------------其他函式定義--------------//

// 產生回傳訊息的函式
function returnmsg($record,$dataarray, $re_code, $re_msg)
{
    // 建立回傳訊息的JSON內容
    $messageArr["record"] = $record;
    $messageArr["data"] = $dataarray;
    $messageArr["status"] = array();
    $today = date('Y-m-dH:i:s(p)'); // 取得當前日期和時間
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
    // 將錯誤訊息寫成紀錄檔儲存到伺服器上。
    $log_time = date('Y-m-d H:i:s');
    $log_filename = "error_log";
    $log_msg = '[' . $log_time . '] ' . $log_msg;
    if (!file_exists($log_filename)) {
        mkdir($log_filename, 0777, true);
    }
    $log_file_data = $log_filename . '/log_' . date('m-d-H-i-s') . '.log';
    file_put_contents($log_file_data, $log_msg . "\n", FILE_APPEND);
}
?>