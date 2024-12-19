<?php 
require_once 'db_connect.php';

$id = $_GET['id'];

$all = $connect->query("DELETE FROM Friends WHERE `Friends`.`id` = $id");
//header('Location: /index.php');
?>