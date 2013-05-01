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
		else {
			$service->initCommunication();
			// Scope SESSION
			// Donc on définit et stock le JSESSIONID correspondant à l'ID passé dans les requêtes HTTPs.
			$cookies = $service->_cookies;
			$service->__setCookie('JSESSIONID', $cookies['JSESSIONID'][0]);
			$_SESSION['soapCookieJSESSIONID'] = $cookies['JSESSIONID'][0];			
		}
	} catch (SoapFault $e) { 
		echo "<h2>Exception Error : </h2>"; 
		echo $e->getMessage(); 
	} 
}
else {
	header('Location: ./index.php');
}


$listeProduit = null;
	
if(isset($_POST)){
	/**
$ajout = $service->ajouterPanier(array('categorie' => $_POST['categorie'), 'nom' => $_POST['categorie'), 'description' => $_POST['description'), 'quantite' => $_POST['quantite'), 'prix' => $_POST['prix'), 'devise' => $_POST['devise'), 'type' => $_POST['type'));
	var_dump($ajout);
**/
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
				<h4><span>Everything's Connected !</span></h4>
			</div>

			<div id="preamble">
				<h3><span>Ajouter un produit</span></h3>
				<form method="post" action="ajouterProduit.php">		
				
				<table>
				<tr>
				<td>Nom</td>
				<td><input type="text" name="nom" /></td>
				<tr>
				<tr>
				<td>Nom</td>
				<td><input type="text" name="nom" /></td>
				<tr>
				<tr>
				<td>Description</td>
				<td><input type="text" name="description" /></td>
				<tr>
				<tr>
				<td>Quantite</td>
				<td><input type="text" name="quantite" /></td>
				<tr>
				<tr>
				<td>Prix</td>
				<td><input type="text" name="prix" /></td>
				<tr>
				
				<tr>
				<td>Devise</td>
				<td><select name="devise">
						<option value="EUR" >
							Euros
						</option>
						<option value="USD">
							USD
						</option>
						<option value="JPY">
							JPY
						</option>
					</select></td>
				<tr>
				<tr>
				<td>Categorie</td>
<td><select name="categorie">
						<option value="Livre" <?php if($categorieSelectionnee == "Livre") echo 'selected="selected"' ?> >
							Livre
						</option>
						<option value="Musique" <?php if($categorieSelectionnee == "Musique") echo 'selected="selected"' ?>>
							Musique
						</option>
						<option value="Vetement" <?php if($categorieSelectionnee == "Vetement") echo 'selected="selected"' ?>>
							Vetement
						</option>
						<option value="Nourriture" <?php if($categorieSelectionnee == "Nourriture") echo 'selected="selected"' ?>>
							Nourriture
						</option>
						<option value="Papeterie" <?php if($categorieSelectionnee == "Papeterie") echo 'selected="selected"' ?>>
							Papeterie
						</option>
					</select></td>
				</tr>
				
				<tr>
				<td colspan="2">
				<input type="submit" name="ajouter" value="Ajouter">
				
				</td>
				</tr>
				<table>
				</form>
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
