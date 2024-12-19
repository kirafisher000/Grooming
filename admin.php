<?php 
require_once 'db_connect.php';

$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
        $all = $connect->query("SELECT booking.id, account.phone ,price_list.title, price_list.price, date,time 
        FROM `booking`, `account`, `price_list` 
        where id_acc = account.id and id_title = price_list.id;");
        if ($all) {
            $response["success"] = true;
            $response["message"] = "booking_list";
            $response["list"] = array();
            $row = mysqli_fetch_all($all);
            foreach ($row as $row) {
            $list = array();
            $list["id"] = $row["0"];
            $list["phone"]=$row["1"];
            $list["title"]=$row["2"];
            $list["price"]=$row["3"];
            $list["date"]=$row["4"];
            $list["time"]=$row["5"];
        
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