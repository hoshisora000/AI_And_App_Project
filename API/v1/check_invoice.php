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
    $error_msg = "uid資料為空"; //錯誤訊息
}
if ($_POST["period"] != "" && preg_match($date_pattern,$_POST["period"])){
    $period = $_POST["period"];
} else { //不接受沒有資料的內容
    $accept = false;
    $error_msg = "period資料為空或格式錯誤"; //錯誤訊息 
}


if($accept){
    $year = ((int) substr($period,0,  3) + 1911)."";
    $period_m_d = substr($period, -4);
    $start_day;
    $end_day;
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
            $end_day= $year ."-" . "01-01" ;
        break;
        default:
            $error_msg = "period日期格式錯誤"; //錯誤訊息 
            $dataarray = [];
            $message = returnmsg(0,$dataarray, "400",  $error_msg); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
            http_response_code(200);
            echo json_encode($message);
            exit;
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

    $sql = "SELECT * FROM `winning_numbers` WHERE `period`='" . $period ."'";

    $result=$link->query($sql); // 執行 SQL 查詢
    $amount = $result->num_rows; // 取得查詢結果的列數
    
    if ($amount <=0) {
        $accept = false;
        $error_msg = "資料庫尚未新增該期內容或日期格式錯誤"; //錯誤訊息 
        $dataarray = [];
        $message = returnmsg(0,$dataarray, "400",  $error_msg); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
        http_response_code(200);
        echo json_encode($message);
        exit;
    }else{ // 若查詢結果有資料
        while ($row = $result->fetch_assoc()) { // 迴圈逐一取得資料列
            $super_special= $row['super_special'];
            $special= $row['special'];
            $head[3];
            $head1= $row['head1'];
            $head2= $row['head2'];
            $head3= $row['head3'];
            $head[0]=$head1;
            $head[1]=$head2;
            $head[2]=$head3;
        }
    }

    $sql1 = "SELECT `uid`, `invoice_number`, `date` FROM `member_invoice` WHERE `uid` = '" . $uid ."' AND `date`>='". $start_day ."' AND `date`<'". $end_day ."'";
    $result=$link->query($sql1); // 執行 SQL 查詢
    $amount = $result->num_rows; // 取得查詢結果的列數
    $awards = array(10000000,2000000,200000, 40000, 10000, 4000, 1000, 200);


    if ($amount ==0) {
        $record=$amount;

        $message = returnmsg($record,$dataarray, "200", "該用戶此期尚未上傳任何發票"); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
        http_response_code(200);
        echo json_encode($message);
        exit;
    }else{ // 若查詢結果有資料
        $record=0;// 中獎筆數
        while ($row = $result->fetch_assoc()) { // 迴圈逐一取得資料列
            $win = 0;
            $winning_amount=0;

            //----------檢查發票號碼是否中獎-----------
            $invoice_number = substr($row['invoice_number'], -8);
            if ($invoice_number == $super_special){
                # 對中特別獎
                $win = 1;
                $winning_amount=$awards[0];
            }
            // 只回傳中獎發票
            if($win == 1){
                $record++;
                $record_array =array(
                    "record" => $record,
                    "invoice_number" =>$row['invoice_number'],
                    "winning_amount" =>$winning_amount
                );
                $dataarray[] = $record_array;
            }
        }
        $result=$link->query($sql1); // 執行 SQL 查詢
        while ($row = $result->fetch_assoc()) { // 迴圈逐一取得資料列
            $win = 0;
            $winning_amount=0;

            //----------檢查發票號碼是否中獎-----------
            $invoice_number = substr($row['invoice_number'], -8);
        
            if ($invoice_number == $special){
                # 對中特獎
                $win = 1;
                $winning_amount=$awards[1];
            }
            
            // 只回傳中獎發票
            if($win == 1){
                $record++;
                $record_array =array(
                    "record" => $record,
                    "invoice_number" =>$row['invoice_number'],
                    "winning_amount" =>$winning_amount
                );
                $dataarray[] = $record_array;
            }
        }
        $result=$link->query($sql1); // 執行 SQL 查詢
        $amount = $result->num_rows; // 取得查詢結果的列數
       /* while ($row = $result->fetch_assoc()) { // 迴圈逐一取得資料列
            $win = 0;
            $winning_amount=0;
            //----------檢查發票號碼是否中獎-----------
            $invoice_number = substr($row['invoice_number'], -8);
            $trace = 2;
            for($i=0;$i<3;$i++){ # 頭獎號碼
                for($a=-8;$a<=-3;$a++){
                    $sub_invoice_number = substr($invoice_number, $a);
                    $sub_head = substr($head[$i], $a);
                    echo $sub_invoice_number . "--" . $sub_head."\r\n";
                    if ($sub_invoice_number == $sub_head){
                        $win = 1;
                        $winning_amount=$awards[$trace];
                        // 只回傳中獎發票
                        if($win == 1){
                            $record++;
                            $record_array =array(
                                "record" => $record,
                                "invoice_number" =>$row['invoice_number'],
                                "winning_amount" =>$winning_amount
                            );
                            $dataarray[] = $record_array;
                        }
                    }
                }
                $trace++;
            }

        }*/
        while ($row = $result->fetch_assoc()) { // 迴圈逐一取得資料列
            $invoice_number_arr[]=$row['invoice_number']; // 將資料加入陣列中  
        }
        $trace = 2;
        for($a=-8;$a<=-3;$a++){
            for($j=0;$j<$amount;$j++){
                for($i=0;$i<3;$i++){ # 頭獎號碼
                    $win = 0;
                    $winning_amount=0;

                    $sub_invoice_number = substr($invoice_number_arr[$j], $a);
                    $sub_head = substr($head[$i], $a);
                
                    if ($sub_invoice_number == $sub_head){
                        $win = 1;
                        $winning_amount=$awards[$trace];
                        // 只回傳中獎發票
                        if($win == 1){
                            
                            $record++;
                            $record_array =array(
                                "record" => $record,
                                "invoice_number" =>$invoice_number_arr[$j],
                                "winning_amount" =>$winning_amount
                            );
                            $dataarray[] = $record_array;
                            $invoice_number_arr[$j]="VVVVVVVVVV";
                        }
                    }
                }
            }
            $trace++;
        }

}
    


    $messageArr = array();

    $link->close(); // 關閉資料庫連結
 

    $message = returnmsg($record,$dataarray, "200", "Success"); // 呼叫 returnmsg 函式，回傳訊息
    http_response_code(200); // 設定 HTTP 狀態碼為 200
    echo json_encode($message); // 將回傳訊息轉換為 JSON 格式並輸出

    $messageArr["status"]=array();




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
    $messageArr["record"] = $record; // 設定回傳訊息的資料部分為查詢結果的陣列
    $messageArr["data"] = $dataarray; // 設定回傳訊息的資料部分為查詢結果的陣列
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