<?php 
require_once 'db_connect.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Проверяем, что все необходимые параметры присутствуют
    if (
        isset($_POST['id_acc']) && 
        isset($_POST['title']) && 
        isset($_POST['date']) && 
        isset($_POST['time'])
    ) {
        // Получаем параметры из POST-запроса
        $id_acc = $_POST['id_acc'];
        $title = $_POST['title'];
        $date = $_POST['date'];
        $time = $_POST['time'];

        // Подготавливаем запрос для получения id для данного title
        $stmt = $connect->prepare("SELECT `id` FROM `price_list` WHERE `title` = ?");
        $stmt->bind_param("s", $title); // Привязываем параметр title как строку
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            // Получаем id_title из результата
            $row = $result->fetch_assoc();
            $id_title = $row['id'];

            // Проверяем, если уже есть запись с таким временем
            $check_stmt = $connect->prepare("SELECT * FROM `booking` WHERE `time` = ?");
            $check_stmt->bind_param("s", $time); // Привязываем параметр time как строку
            $check_stmt->execute();
            $check_result = $check_stmt->get_result();

            // Если время не занято, вставляем новую запись
            if ($check_result->num_rows == 0) {
                $insert_stmt = $connect->prepare("INSERT INTO `booking` (`id`, `id_acc`, `id_title`, `date`, `time`) VALUES (NULL, ?, ?, ?, ?)");
                $insert_stmt->bind_param("iiss", $id_acc, $id_title, $date, $time); // Привязываем параметры
                if ($insert_stmt->execute()) {
                    $response["success"] = true;
                    $response["message"] = "You booked a service";
                } else {
                    $response["success"] = false;
                    $response["message"] = "Oops! Change time or data. Error: " . $insert_stmt->error;
                }
            } else {
                // Если время уже занято
                $response["success"] = false;
                $response["message"] = "Such time is already booked. Please choose another time.";
            }
        } else {
            // Если не нашли title в price_list
            $response["success"] = false;
            $response["message"] = "Service title not found.";
        }

    } else {
        // Если не все необходимые параметры переданы
        $response["success"] = false;
        $response["message"] = "Missing parameters.";
    }
} else {
    $response["success"] = false;
    $response["message"] = "Error: Invalid request method.";
}

echo json_encode($response);
?>
