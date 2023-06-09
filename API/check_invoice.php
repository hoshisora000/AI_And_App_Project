<?php
header('Content-Type: application/json; charset=UTF-8'); //設定資料類型 json 編碼 utf-8

$accept = true; //如果接收資料格式正確才接收
$error_msg = ""; //記錄錯誤訊息
$date_pattern = "/^[1]\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/"; //使用正規表示法檢查日期格式

//----------接收資料並檢查送進來的資料是否有問題-------------------//
if ($_POST["uid"] != "") {
    $uid = $_POST["uid"];
} else { //不接受沒有資料的內容
    $accept = false;
    $error_msg = "uid資料為空"; //記錄錯誤訊息
}
if ($_POST["period"] != "" && preg_match($date_pattern,$_POST["period"])){
    $period = $_POST["period"];
} else { //不接受沒有資料的內容
    $accept = false;
    $error_msg = "period資料為空或格式錯誤"; //記錄錯誤訊息 
}


if($accept){
    //-----------將發票期數轉換成SQL使用的日期格式------------------//
    $year = ((int) substr($period,0,  3) + 1911)."";
    $period_m_d = substr($period, -4);

    $start_day;// 每期的開始日期(單月1日)
    $end_day;// 每期的結束日期隔天(單月1日)

    //---------------將期數轉換成日期-----------------//
    switch ($period_m_d) {
        case "0102":
            $start_day= $year ."-" . "01-01" ;
            $end_day= $year ."-" . "03-01" ;
        break;
        case "0304":
            $start_day= $year ."-" . "03-01" ;
            $end_day= $year ."-" . "05-01" ;
        break;
        case "0506":
            $start_day= $year ."-" . "05-01" ;
            $end_day= $year ."-" . "07-01" ;
        break;
        case "0708":
            $start_day= $year ."-" . "07-01" ;
            $end_day= $year ."-" . "09-01" ;
        break;
        case "0910":
            $start_day= $year ."-" . "09-01" ;
            $end_day= $year ."-" . "11-01" ;
        break;
        case "1112":
            $start_day= $year ."-" . "11-01" ;
            $end_day= (((int)$year)+1) ."-" . "01-01" ;
        break;
        default:
            $error_msg = "period日期格式錯誤"; //錯誤訊息 
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

    //--------------查詢指定期數的中獎號碼------------------//
    $sql = "SELECT * FROM `winning_numbers` WHERE `period`='" . $period ."'";
    $result=$link->query($sql); // 執行 SQL 查詢
    $amount = $result->num_rows; // 取得查詢結果的列數

    if ($amount <=0) { // 查詢不到內容
        $error_msg = "資料庫尚未新增該期內容或日期格式錯誤"; //錯誤訊息 
        $dataarray = [];
        $message = returnmsg(0,$dataarray, "500",  $error_msg);  //回傳錯誤代碼500，錯誤訊息:資料庫尚未新增該期內容或日期格式錯誤。
        echo json_encode($message);
        exit();
    }else{ // 若查詢結果有資料
        while ($row = $result->fetch_assoc()) { // 迴圈逐一取得資料列
            $super_special= $row['super_special']; // 特別獎
            $special= $row['special']; // 特獎
            $head[3]; // 頭獎有三個號碼
            $head1= $row['head1'];
            $head2= $row['head2'];
            $head3= $row['head3'];
            $head[0]=$head1;
            $head[1]=$head2;
            $head[2]=$head3;
        }
    }

    //--------------查詢使用者的發票------------------//
    $sql1 = "SELECT `uid`, `invoice_number`, `date` FROM `member_invoice` WHERE `uid` = '" . $uid ."' AND `date`>='". $start_day ."' AND `date`<'". $end_day ."'";
    $result=$link->query($sql1); // 執行 SQL 查詢
    $amount = $result->num_rows; // 取得查詢結果的列數
    $awards = array(10000000,2000000,200000, 40000, 10000, 4000, 1000, 200); // 獎項對應的獎金

    if ($amount ==0) { // 該用戶沒有任何發票
        $record=$amount;
        $message = returnmsg($record,$dataarray, "200", "該用戶此期尚未上傳任何發票"); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
        http_response_code(200);
        echo json_encode($message);
        exit;
    }else{ // 若查詢結果有資料
        $record=0;// 紀錄中獎筆數
        while ($row = $result->fetch_assoc()) { // 迴圈逐一取得資料列
            //----------檢查發票號碼是否中獎-----------
            $invoice_number = substr($row['invoice_number'], -8);
            if ($invoice_number == $super_special){
                // 對中特別獎
                $record++;
                $record_array =array(
                    "record" => $record,
                    "invoice_number" =>$row['invoice_number'],
                    "winning_amount" =>$awards[0]
                );
                $dataarray[] = $record_array; // 將中獎資料新增到回傳資料中。
            }
        }

        $result=$link->query($sql1); // 執行 SQL 查詢
        while ($row = $result->fetch_assoc()) { // 迴圈逐一取得資料列.
            $invoice_number_arr[]=$row['invoice_number']; // 將發票號碼加入陣列中 (提供一到六獎使用)
            //----------檢查發票號碼是否中獎-----------
            $invoice_number = substr($row['invoice_number'], -8);
            if ($invoice_number == $special){
                # 對中特獎
                $record++;
                $record_array =array(
                    "record" => $record,
                    "invoice_number" =>$row['invoice_number'],
                    "winning_amount" =>$awards[1]
                );
                $dataarray[] = $record_array; // 將中獎資料新增到回傳資料中。
            }
        }

        $amount = $result->num_rows; // 取得查詢結果的列數
        for($a=-8;$a<=-3;$a++){ // 依據獎金高低檢查一獎到六獎
            for($j=0;$j<$amount;$j++){ // 檢查該用戶所有發票號碼
                for($i=0;$i<3;$i++){ # 頭獎號碼有三個

                    // 依據規定檢查倒數N個號碼
                    $sub_invoice_number = substr($invoice_number_arr[$j], $a);
                    $sub_head = substr($head[$i], $a);

                    if ($sub_invoice_number == $sub_head){
                        # 號碼對中
                        $record++;
                        $record_array =array(
                            "record" => $record,
                            "invoice_number" =>$invoice_number_arr[$j],
                            "winning_amount" =>$awards[($a+10)]
                        );
                        $dataarray[] = $record_array; // 將中獎資料新增到回傳資料中。
                        $invoice_number_arr[$j]="VVVVVVVVVV"; // 因為發票中獎規則，需要將號碼移出array
                    }
                }
            }
        }
    }
    $messageArr = array();
    $link->close(); // 關閉資料庫連結
    // 呼叫函示產生回傳訊息
    $message = returnmsg($record,$dataarray, "200", "查詢成功");

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