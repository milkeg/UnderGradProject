<?php
/**
* Invocation simple à un Service WEB : la classe SoapClient() comporte des paramètres supplémentaires 
*/
session_start();

// Debug MODE

$limit = ini_get('soap.wsdl_cache_limit');
ini_set('soap.wsdl_cache_limit', 0);
ini_set('soap.wsdl_cache_limit', $limit);

if(isset($_POST['address']) && $_POST['address'] != ''){
	//On doit passer le fichier WSDL du Service en paramètre de l'objet SoapClient()
	$wsdl=$_POST['address'];
	try {
		$service = new SoapClient($wsdl, array('cache_wsdl' => WSDL_CACHE_NONE) );
		$service->initCommunication();
		
		// Scope SESSION
		// Donc on définit et stock le JSESSIONID correspondant à l'ID passé dans les requêtes HTTPs.
		$cookies = $service->_cookies;
		$service->__setCookie('JSESSIONID', $cookies['JSESSIONID'][0]);
		$_SESSION['soapCookieJSESSIONID'] = $cookies['JSESSIONID'][0];
	
	} catch (SoapFault $e) { 
		echo "<h2>Exception Error!</h2>"; 
		echo $e->getMessage(); 
	} 

	session_start();
	$_SESSION['wsdl'] = $_POST['address'];;
	header('Location: ./home.php');
}


?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" >
<head>
	<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
	<title>IF Corporation</title>
<link rel="stylesheet" type="text/css" media="screen" href="style.css" />
</head>
<body>

<div id="container">
	<div id="intro">
		<div id="pageHeader" style="padding: 4px 0 0 50px;">
			<h1><span>WebService : IF Vente</span></h1>
			<h4><span>Everything's Connected !</span></h4>
		</div>

		<div id="preamble">
			<h3><span>Quel WebService ?</span></h3>
			
			<form action="index.php" method="post">
				<label> Veuillez saisir l'adresse du WSDL :</label>
				<input type="text" name="address" />
				<input type="submit" name="Submit" value="Se connecter" />
			</form>
			
		</div>
	</div>

	</div>
</div>

</body>
</html>
