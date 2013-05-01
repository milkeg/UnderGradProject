<?php
session_start();
// Récupération du WDSL stocké dnas la variable SESSION
if(isset($_SESSION['wsdl'])){
	//On doit passer le fichier WSDL du Service en paramètre de l'objet SoapClient()
	$wsdl=$_SESSION['wsdl'];
	try {
		$service = new SoapClient($wsdl, array('trace' => 1));
		
		if (isset($_SESSION['soapCookieJSESSIONID'])){
			$service->__setCookie('JSESSIONID', $_SESSION['soapCookieJSESSIONID']);
		}
	} catch (SoapFault $e) { 
		echo "<h2>Exception Error : </h2>"; 
		echo $e->getMessage(); 
	} 
}
else {
	header('Location: ./index.php');
}
	
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" >
<head>
	<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
	<title>IF Corportation</title>
<link rel="stylesheet" type="text/css" media="screen" href="style.css" />
</head>
<body>

<div id="container">
	<div id="intro">
		<div id="pageHeader" style="padding: 4px 0 0 50px;">
			<h1><span>WebService : IF Vente</span></h1>
			<h4><span>Everything's Connected !'</span></h4>
		</div>

		<div id="preamble">
			<h3><span>What is Lorem Ipsum?</span></h3>
			<p class="p1"><span>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.</span></p>
		</div>
	</div>

	</div>
</div>

	
	
<div id="linkList">
	<div id="linkList2">
		<div id="lselect">
			<ul>
				<li><a href="home.php">Accueil</a></li>
				<li><a href="listeProduits.php">Tous les produits</a></li>
				<li><a href="listeCategories.php">Produits par Catégories</a></li>
				<li><a href="listeTypes.php">Produits par Types </a></li>
				<li><a href="voirPanier.php">Voir le panier</a></li>
				<li><a href="ajouterProduit.php">Ajouter un produit</a></li>
			</ul>
		</div>
	</div>
</div>




</body>
</html>
