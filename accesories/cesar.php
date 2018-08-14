<?php
$servername = "localhost";
$username = "root";
$password = "root";
$database = "cesar";

header('Content-Type: text/plain; charset=utf-8');


class ajax{
	private $con;
	private $response;

	function __construct(){

		$this->connect();
		$this->response=array();

		if(!isset($_GET['action']))
			exit(0);
		$action = $_GET['action'];

		if($action=='signup')
			$this->set_user();
		elseif($action=="login")
			$this->get_user();
		elseif($action=='addfriends')
			$this->set_friend();
		elseif($action=='getallfriends')
			$this->get_friends();
		elseif($action=='add_msg')
			$this->add_msg();
		elseif($action=="get_msg")
			$this->get_msg();
		elseif($action=="getpeople")
			$this->get_people();
		else{
			exit(0);
		}

	}

	private function set_user(){
		$username=$_GET['username'];
		$password=$_GET['password'];

		$sql="SELECT username FROM users WHERE username='$username'";
		$query=$this->con->query($sql);
		if($query===false){
		    $this->response['status'] ='false';
		    $this->sendResponse();
		}

		$row=mysqli_fetch_array($query);
		if(sizeOf($row)>0){
		    $this->response['status'] ='false';
		    $this->sendResponse();			
		}
		
		$sql="INSERT INTO users(username, password) VALUES ('$username','$password')";
		$query=$this->con->query($sql);
		if($query===false){
		    $this->response['status'] ='false';
		    $this->sendResponse();
		}

	    $this->response['status']    = 'true';
	    $this->sendResponse();
	}

	private function get_user(){
		global $con, $response;
		$username=$_GET['username'];
		$password=$_GET['password'];
		$sql="SELECT username FROM users WHERE username='$username' AND password='$password'";
		$query=$this->con->query($sql);
		if($query===false){
		    $this->response['status'] ='false';
		    $this->sendResponse();
		}

		$row=mysqli_fetch_array($query);
		if(sizeOf($row)>0){
		    $this->response['status'] = 'true';
	    } else {
		    $this->response['status'] ='false';
	    }

	    $this->sendResponse();	    	
	}
	private function set_friend(){
		global $con, $response;

		$from=$_GET['from'];
		$to=$_GET['to'];

		$sql="INSERT INTO friends(`from`, `to`) VALUES ('$from', '$to')";
		$query=$this->con->query($sql);
		if($query===false)
		    $this->response['status'] ='false';
		else
	    	$this->response['status'] ='true';
			
	    $this->sendResponse();
	}
	private function get_friends(){
		global $con, $response;
		$username=$_GET['username'];

		$sql="SELECT `to` FROM friends WHERE `from`='$username'";
		$query=$this->con->query($sql);
		if($query===false){
		    $this->response['status'] ='false';
		    $this->response['error']  ='erroeInExecution';
		    $this->sendResponse();
		}

		$x=array();
		while($row=mysqli_fetch_array($query)){
			$t=array();
			$t['username']=$row['to'];
			$x[]=$t;
		}

	    $this->response['status'] ='true';
	    $this->response['friends'] = $x;
	    $this->sendResponse();	    			
	}
	private function get_people(){
		global $con, $response;
		$username=$_GET['username'];
		$myname=$_GET['myname'];

		$sql="SELECT DISTINCT `username` FROM users WHERE `username` NOT IN (SELECT `to` FROM friends WHERE `from` = '$myname') AND `username` LIKE '%$username%'";
		$query=$this->con->query($sql);
		if($query===false){
		    $this->response['status'] ='false';
		    $this->sendResponse();
		}

		$x=array();
		while($row=mysqli_fetch_array($query)){
			$t=array();
			$t['username']=$row['username'];
			$x[]=$t;
		}

	    $this->response['status'] ='true';
	    $this->response['friends'] = $x;
	    $this->sendResponse();	    			
	}
	private function add_msg(){
		global $con, $response;
		if(!isset($_GET['from']) || !isset($_GET['to']) || !isset($_GET['msg'])){
		    $this->response['status'] ='false';
		    $this->response['error']  ='WrongParams';
		    $this->sendResponse();
		}
		$from=$_GET['from'];
		$to=$_GET['to'];
		$msg=$_GET['msg'];

		
		$sql="INSERT INTO msgs(`from`, `to`, `msg`) VALUES ('$from', '$to', '$msg')";
		$query=$this->con->query($sql);
		if($query===false){
		    $this->response['status'] ='false';
		    $this->response['error']  ='erroeInExecution';
		    $this->sendResponse();
		}

	    $this->response['status'] ='true';
	    $this->sendResponse();		
	}
	private function get_msg(){
		$from=$_GET['from'];
		$to=$_GET['to'];
		$sql="SELECT * FROM msgs WHERE (`from`='$from' AND `to`='$to') or (`from`='$to' AND `to`='$from') ORDER BY `id` DESC";
		$query=$this->con->query($sql);
		if($query===false){
		    $this->response['status'] ='false';
		    $this->response['error']  ='erroeInExecution';
		    $this->sendResponse();
		}

		$str="";
		while($row=mysqli_fetch_array($query)){
			$str.=$row['from'].":".$row['msg']."\n";
		}
	    $this->response['status'] ='true';
	    $this->response['msg']  =$str;
	    $this->sendResponse();	    	
	}


	private function sendResponse(){
		echo json_encode($this->response);
		die("");
	}
	private function connect(){
		global $servername, $username, $password, $database;
		$this->con = new mysqli($servername, $username, $password, $database);

		if ($this->con->connect_error){
		    $this->response['status'] ='false';
		    $this->response['error']  ='databaseConnection';
		    $this->sendResponse();
		}
	}

}


new ajax();