<?php 
require_once 'db_connect.php';

$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
        $all = $connect->query("SELECT * FROM `price_list`");
        if ($all) {
            $response["success"] = true;
            $response["message"] = "price list";
            $response["list"] = array();
            $row = mysqli_fetch_all($all);
            foreach ($row as $row) {
            $list = array();
            $list["title"] = $row["1"];
            $list["price"]=$row["2"];
        
            array_push($response["list"], $list);
            }
            echo json_encode($response, JSON_UNESCAPED_UNICODE);
        } else {
            $response["success"] = false;
            $response["message"] = "Oops! An error occurred.";
        
            echo json_encode($response);
        }
}
else{
$response["success"] = false;
$response["message"] = "Error.";

echo json_encode($response);}

//header('Location: /index.php');
?>