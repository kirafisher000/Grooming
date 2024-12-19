<?php 
require_once 'db_connect.php';

$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    if(
        isset($_POST['phone']) and
        isset($_POST['pass'])
    ){
        $phone = $_POST['phone'];
        $pass = $_POST['pass'];
        $check = $connect->query("SELECT * FROM `account` WHERE `phone` LIKE '$phone' OR `password` LIKE '$pass'");
        if($check->num_rows==null){
        
        $all = $connect->query("INSERT INTO `account` (`id`, `phone`, `password`, `role`) VALUES (NULL, '$phone', '$pass', '2');");
        if ($check->num_rows == 0) {
            $response["success"] = 1;
            $response["message"] = "Acc successfully created.";
        
            echo json_encode($response);
        } else{
            $response["success"] = false;
            $response["message"] = "Oops! An error occurred.";
        
            echo json_encode($response);
        }
        }
        else{
            $response["success"] = false;
            $response["message"] = "Such user is exists in the system";
        }
    }
}
else{
$response["success"] = false;
$response["message"] = "Error.";

echo json_encode($response);}

//header('Location: /index.php');
?>