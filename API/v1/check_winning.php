<?php
header('Content-Type: application/json; charset=UTF-8'); //設定資料類型 json 編碼 utf-8

$accept = true; //如果接收資料格式正確才接收
$error_msg = ""; //記錄錯誤訊息
$date_pattern = "/^[1]\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/"; //使用正規表示法檢查日期格式
//----------接收資料並檢查送進來的資料是否有問題--------------------//

if ($_GET["period"] != "" && preg_match($date_pattern,$_GET["period"])){
    $period = $_GET["period"];
} else { //不接受沒有資料的內容
    $accept = false;
    $error_msg = "period資料為空或格式錯誤"; //錯誤訊息 
}
if ($_GET["invoice_number"] != "") {
    $invoice_number = $_GET["invoice_number"];
} else { //不接受沒有資料的內容
    $accept = false;
    $error_msg = "invoice_number資料為空"; //錯誤訊息 
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
    $sql1 = "SELECT * FROM `winning_numbers` WHERE `period`='" . $period ."'";

    $result=$link->query($sql1); // 執行 SQL 查詢
    $amount = $result->num_rows; // 取得查詢結果的列數
    

    if ($amount <=0) {
        $accept = false;
        $error_msg = "資料庫尚未新增該期內容或日期格式錯誤"; //錯誤訊息 
        $dataarray = [];
        $message = returnmsg($dataarray, "400",  $error_msg); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
        http_response_code(200);
        echo json_encode($message);
        exit;
    }else{ // 若查詢結果有資料
        while ($row = $result->fetch_assoc()) { // 迴圈逐一取得資料列
            $dataarray[]=$row; // 將資料加入陣列中  
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
    $awards = array(10000000,2000000,200000, 40000, 10000, 4000, 1000, 200);
    $link->close(); // 關閉資料庫連結
    $win = 0;
    $winning_amount=0;


    if(strlen($invoice_number)==10){
        //發票號碼含英文
        $number_pattern_10 = "/^[A-Z][A-Z]\d{8}$/"; //使用正規表示法檢查發票格式
        if (!preg_match($number_pattern_10,$invoice_number)){
            //不接受的格式
            $dataarray = [];
            $message = returnmsg($dataarray, "400", "發票號碼格式錯誤"); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
            http_response_code(200);
            echo json_encode($message);
            exit();
        }else{
            $invoice_number = substr($invoice_number, -8);
            if ($invoice_number == $super_special){
                # 對中特別獎
                $win = 1;
                $winning_amount=$awards[0];
            }
            if ($invoice_number == $special){
                 # 對中特獎
                 $win = 1;
                 $winning_amount=$awards[1];
            }
            for($i=0;$i<3;$i++){ # 頭獎號碼
                $trace = 7;
                for($a=-3;$a>=-8;$a--){
                    $sub_invoice_number = substr($invoice_number, $a);
                    $sub_head = substr($head[$i], $a);
                    if ($sub_invoice_number == $sub_head){
                        $win = 1;
                        $winning_amount=$awards[$trace];
                   }
                   $trace--;
                }
            }
        } 
    }else if(strlen($invoice_number)==8){
        //發票號碼只有數字
        $number_pattern_8 = "/^\d{8}$/"; //使用正規表示法檢查發票格式
        if (!preg_match($number_pattern_8,$invoice_number)){
            //不接受的格式
            $dataarray = [];
            $message = returnmsg($dataarray, "400", "發票號碼格式錯誤"); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
            http_response_code(200);
            echo json_encode($message);
            exit();
        } else{
            if ($invoice_number == $super_special){
                # 對中特別獎
                $win = 1;
                $winning_amount=$awards[0];
            }
            if ($invoice_number == $special){
                 # 對中特獎
                $win = 1;
                $winning_amount=$awards[1];
            }
            for($i=0;$i<3;$i++){ # 頭獎號碼
                $trace = 7;
                for($a=-3;$a>=-8;$a--){
                    $sub_invoice_number = substr($invoice_number, $a);
                    $sub_head = substr($head[$i], $a);
                    if ($sub_invoice_number == $sub_head){
                        $win = 1;
                        $winning_amount=$awards[$trace];
                   }
                   $trace--;
                }
            }
        } 
    }else if(strlen($invoice_number)==3){
        //發票號碼只有末三碼
        $number_pattern_3 = "/^\d{3}$/"; //使用正規表示法檢查發票格式
        if (!preg_match($number_pattern_3,$invoice_number)){
            //不接受的格式
            $dataarray = [];
            $message = returnmsg($dataarray, "400", "發票號碼格式錯誤"); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
            http_response_code(200);
            echo json_encode($message);
            exit();
        } else{
            $sub_super_special = substr($super_special, -3);
            if($invoice_number==$sub_super_special){
                $win = 2;
                $winning_amount=$awards[0];
                $return_for_user = $super_special;
            }
            $sub_special = substr($special, -3);
            if($invoice_number==$sub_special){
                $win = 2;
                $winning_amount=$awards[1];
                $return_for_user = $special;
            }
            for($i=0;$i<3;$i++){ # 頭獎號碼
                $sub_head = substr($head[$i], -3);
                if ($invoice_number == $sub_head){
                    $win = 2;
                    $winning_amount=$awards[7];
                    $return_for_user = $head[$i];
                }
            }
        } 
    }else{
        //不接受的格式
        $dataarray = [];
        $message = returnmsg($dataarray, "400", "發票號碼格式錯誤"); //回傳錯誤代碼400，錯誤訊息:資料有缺漏或資料格式錯誤。
        http_response_code(200);
        echo json_encode($message);
        exit();
    }

    $dataarray = array(
        "win" => $win,
        "winning_amount" => $winning_amount,
        "return_for_user" => $return_for_user
    );

    $message = returnmsg($dataarray, "200", "Success",$amount); // 呼叫 returnmsg 函式，回傳訊息
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
function returnmsg($dataarray, $re_code, $re_msg)
{

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
?>