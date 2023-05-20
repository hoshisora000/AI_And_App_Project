<?php 


date_default_timezone_set("Asia/Taipei"); //設定時間時區


//-------------存取資料庫--------------//
    $severname = "192.168.2.200"; //SQL位置
    $username = "hoshiso1_system";  //帳號
    $password = "system123456"; //密碼
    $dbname = "hoshiso1_project"; //SQL名稱
    $link = mysqli_connect($severname, $username, $password , $dbname ); // 建立MySQL的資料庫連結
    if($link->connect_error){
        wh_log("Connection failed: ". $link->connect_error);
    }

    $sql = "INSERT INTO `member_invoice` (`uid`, `invoice_number`, `date`, `time`, `money`) VALUES ('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', 'NQ60414081', '2023-04-20', '18:57:30', '20') ";
    try {
        if ( $result = mysqli_query($link, $sql) ) { 
            mysqli_free_result($result); // 釋放佔用的記憶體   
    
        }
        mysqli_close($link); // 關閉資料庫連結
    } catch (Exception $e) {
        echo 'Message: ' .$e->getMessage();
    }

function wh_log($log_msg)
{
    $log_time = date('Y-m-d H:i:s');
    $log_filename = "error_log";
    $log_msg='['.$log_time.'] '.$log_msg;

    if (!file_exists($log_filename)) 
    {
        // create directory/folder uploads.
        mkdir($log_filename, 0777, true); // mkdir(pathname[, mode[, recursive[, context]]])
    }
    $log_file_data = $log_filename.'/log_' . date('m-d-H-i-s') . '.log';
    file_put_contents($log_file_data, $log_msg . "\n", FILE_APPEND);
}


?>