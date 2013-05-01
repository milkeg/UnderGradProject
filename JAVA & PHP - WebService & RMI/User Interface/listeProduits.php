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

if(isset($_POST['devise'])){
	$deviseSelectionnee = $_POST['devise'];
	$_SESSION['deviseSelectionnee'] = $deviseSelectionnee;
	$service->changerDevise(array('nouvDev' => $deviseSelectionnee));
}
elseif (isset($_SESSION['deviseSelectionnee']) && $_SESSION['deviseSelectionnee'] != NULL){
	$deviseSelectionnee = $_SESSION['deviseSelectionnee'];
} 
else {
	$deviseSelectionnee = "EUR";
}

try {
	$listeProduit = $service->getAllProduits();
} catch (SoapFault $e) { 
	echo "<h2>Exception Error : </h2>"; 
	echo $e->getMessage(); 
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
				<h3><span>Tous les produits</span></h3>
								
				
				<br>
				<br>
		
				<?php
				/**
				echo "<pre>";
					var_dump($listeProduit);
					echo "</pre>";
					echo "<br>";
					echo "<pre>";
						var_dump($listeProduit->getAllProduitsReturn);
						echo "</pre>";
						echo "<pre>";
							var_dump(is_array($listeProduit->getAllProduitsReturn));
							echo "</pre>";
							**/	
	
									
													if(isset($listeProduit->getAllProduitsReturn) && $listeProduit->getAllProduitsReturn != NULL){
														
														?>			
														<table>
															<thead>
																<tr>
																	<th>Nom</th>
																	<th>Description</th>
																	<th>Quantite</th>
																	<th>
																		<form acion="listeCategories.php" method="post">
																		<label for="devise">Prix</label>
																			<select name="devise" onchange='this.form.submit()'>
																				<option value="USD" <?php if($deviseSelectionnee == "USD") echo 'selected="selected"' ?> >
																					USD
																				</option>
																				<option value="EUR" <?php if($deviseSelectionnee == "EUR") echo 'selected="selected"' ?>>
																					EUR
																				</option>
																			</select>
																		</form>
																	</th>
																	<th></th>
																</tr>	
															</thead>
															<tbody>
																<?php
															
														// Plusieurs produits
														if(is_array($listeProduit->getAllProduitsReturn)){
															
															for($i=0; isset($listeProduit->getAllProduitsReturn[$i]->description); $i++){
																
																?>
																<tr>
																	<td>
																		<a href="">
																			<?php
																			echo $listeProduit->getAllProduitsReturn[$i]->nom;
																			?>
																		</a>
																	</td>
																	<td>
																		<?php
																		echo $listeProduit->getAllProduitsReturn[$i]->description;
																		?>
																	</td>
																	<td>
																		<?php
																		echo $listeProduit->getAllProduitsReturn[$i]->quantite;
																		?>
																	</td>
																	<td>
																		<?php
																		echo $listeProduit->getAllProduitsReturn[$i]->prix;
																		?>
																	</td>
																	<td>
																		<?php
echo '<a href="voirPanier.php?ref='.$listeProduit->getAllProduitsReturn[$i]->nom.'&cat='.$listeProduit->getAllProduitsReturn[$i]->categorie->nom.'"> Ajouter au panier </a>';																		
?>
																	</td>
																</tr>
																<?php
															}
														}
														// Un produit
														else {
															?>
															<tr>
																<td>
																	<a href="">
																		<?php
																		echo $listeProduit->getAllProduitsReturn->nom;
																		?>
																	</a>
																</td>
																<td>
																	<?php
																	echo $listeProduit->getAllProduitsReturn->description;
																	?>
																</td>
																<td>
																	<?php
																	echo $listeProduit->getAllProduitsReturn->quantite;
																	?>
																</td>
																<td>
																	<?php
																	echo $listeProduit->getAllProduitsReturn->prix;
																	?>
																</td>
																<td>
																	<?php
																	echo '<a href="voirPanier.php?ref='.$listeProduit->getAllProduitsReturn->nom.'&cat='.$listeProduit->getAllProduitsReturn->categorie->nom.'"> Ajouter au panier </a>';
																	?>
																</td>
															</tr>
															<?php
														}
														?>
													</tbody>
												</table>
														<?php
													}
													else {
														?>
														<p> Aucun produit </p>
															<?php
													}
													?>
							
						</div>
			
					</div>

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

