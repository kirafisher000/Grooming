<?php 
require_once 'db_connect.php';

$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
    if(isset($_POST['phone']) and isset($_POST['pass'])){
        $phone = $_POST['phone'];
        $pass = $_POST['pass'];
        $all = $connect->query("SELECT * FROM `account` WHERE `phone` LIKE '$phone' AND `password` LIKE '$pass'");
        if ($all) {
            $response["success"] = true;
            $response["message"] = "Welcome";
            $response["phone"] = $phone;
            while ($row = mysqli_fetch_array($all,MYSQLI_ASSOC)) {
            
            $response["id"] = $row["id"];
            $response["role"]=$row["role"];
        
            echo json_encode($response);
            }
        } else {
            $response["success"] = false;
            $response["message"] = "Oops! An error occurred.";
        
            echo json_encode($response);
        }
    }
}
else{
$response["success"] = false;
$response["message"] = "Error.";

echo json_encode($response);}

//header('Location: /index.php');
?>