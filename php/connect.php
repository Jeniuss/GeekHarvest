<?php
    $servername = "localhost"; //replace it with your database server name
    $username = "root";  //replace it with your database username
    $password = "GeekHarvest57";  //replace it with your database password
    $dbname = "GeekHavest";


    // Create connection
    $conn = mysqli_connect($servername, $username, $password, $dbname);

  
    // Check connection
    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }
    

    $val = $_GET['plant_id'];

    $sql="SELECT * FROM `plant_info` WHERE plant_id = '$val'";  
    $result = $conn->query($sql);
    if ($result->num_rows > 0) {
        // output data of each row
        while($row = $result->fetch_assoc()) {
            echo $row[phase1]. " " . $row[phase2]. " " . $row[phase3];
        }
    } else {
        echo "0 results";
    }

    $conn->close();
    //print(json_encode($output));   
?>