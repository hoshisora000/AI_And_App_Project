<?php
session_start();

if (isset($_POST['userid']) && isset($_POST['passwd'])) {
    
    $_SESSION['level']=0;

    $account = "admin";
    $password = "admin123";

    if($account==$_POST['userid'] && $_POST['passwd'] == $password){
        $_SESSION['level'] = 9; 
        $check=1;

    }else if($account==$_POST['userid'] && $_POST['passwd'] != $password){
        echo "<script> alert('密碼輸入錯誤'); </script>";
        $check=1;
    }
    if($check == 0){
        echo "<script> alert('帳號輸入錯誤'); </script>";
    }
    if($check == 1){
        if( $_SESSION['level']==9){
            header("Refresh:0;url=admin.php");
        }
    }
}

?>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>發票助手</title>
        <!-- Favicon-->
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        <!-- Bootstrap icons-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link href="css/styles.css" rel="stylesheet" />
    </head>
    <body class="d-flex flex-column">
        <main class="flex-shrink-0">
            <!-- Navigation-->
            <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                <div class="container px-5">
                    <a class="navbar-brand" href="index.html">發票助手</a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                            <li class="nav-item"><a class="nav-link" href="index.html">首頁</a></li>
                            <li class="nav-item"><a class="nav-link" href="about.html">關於我們</a></li>
                            <li class="nav-item"><a class="nav-link" href="contact.php">聯絡我們</a></li>
                            <li class="nav-item"><a class="nav-link" href="admin.php">管理員後台</a></li>
                        </ul>
                    </div>
                </div>
            </nav>
            <!-- Pricing section-->
            <section class="bg-light py-5">
                <div class="container px-5 my-5">
                    <div class="text-center mb-5">
                      
                    </div>
                    <div class="row gx-5 justify-content-center">
                        <!-- Pricing card free-->
                        <!---內容加在裡面-->
                        <div class="col-md-10 mx-auto col-lg-5">
                            <form class="p-4 p-md-5 border rounded-3 bg-light" role="form001" name="form001" id="form001" action="./login.php" method="POST">
                            登入
                            <div class="form-floating mb-3">

                                <input class="form-control" type="text" name="userid" size="20" placeholder="userid">
                                <label for="floatingInput">帳號<label for="userid" class="error"></label></label>
                            </div>
                            <div class="form-floating mb-3">
                                <input class="form-control" type="password" name="passwd" id="passwd" size="20" placeholder="限2-12個字">
                                <label for="floatingInput">密碼<label for="passwd" class="error"></label></label>
                            </div>
                            <button class="btn btn-primary btn-lg px-4 me-md-2" type="submit">登入</button>
                            <button class="btn btn-outline-secondary btn-lg px-4" type="reset">清除</button>
                            <hr class="my-4">
                            </form>
                        </div>
                    </div>
                </div>
            </section>
        </main>
        <!-- Footer-->
        <footer class="bg-dark py-4 mt-auto">
            <div class="container px-5">
                <div class="row align-items-center justify-content-between flex-column flex-sm-row">
                    <div class="col-auto"><div class="small m-0 text-white">Copyright &copy; ncue_ai.app.project 2023</div></div>
                    <div class="col-auto">
                        <a class="link-light small" href="#!">隱私宣告</a>
                        <span class="text-white mx-1">&middot;</span>
                        <a class="link-light small" href="#!">開發團隊</a>
                        <span class="text-white mx-1">&middot;</span>
                        <a class="link-light small" href="mailto:ai.app.project.ncue@gmail.com">聯絡我們</a>
                    </div>
                </div>
            </div>
        </footer>
        <!-- Bootstrap core JS-->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
        <!-- Core theme JS-->
        <script src="js/scripts.js"></script>
    </body>
</html>
