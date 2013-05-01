<?php
ini_set("soap.wsdl_cache_enabled", "0");

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

if(isset($_GET['viderPanier']) ){
	try {
		$service->viderPanier();	
	} catch (SoapFault $e) { 
		echo "<h2>Exception Error : </h2>"; 
		echo $e->getMessage(); 
	} 

}



if(isset($_GET['ref']) && isset($_GET['cat'])){
	try {
		$service->ajouterPanier(array('cat' => $_GET['cat'],'ref' => $_GET['ref'],'quantite' => 1));	
	} catch (SoapFault $e) { 
		echo "<h2>Exception Error : </h2>"; 
		echo $e->getMessage(); 
	} 

}

try {
	$listeProduit = $service->getPanier();
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
			
				echo "<pre>";
					print_r($listeProduit);
					echo "</pre>";
					echo "<br>";
					
					echo "<pre>";
						print_r($listeProduit->getPanierReturn);
						echo "</pre>";
						
						echo "<pre>";
							echo "</pre>";
							
						echo "<pre>";
							var_dump(is_array($listeProduit->getPanierReturn));
							echo "</pre>";
								
	
							
							if(isset($listeProduit->getPanierReturn) && $listeProduit->getPanierReturn != NULL){
														
								?>			
								<table>
									<thead>
										<tr>
											<th>Nom</th>
											<th>Quantite</th>
											<th>
												<form action="listeCategories.php" method="post">
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
										if(is_array($listeProduit->getPanierReturn)){
												
											for($i=0; isset($listeProduit->getPanierReturn[$i]); $i++){
																														
												$listePrixProduit[$i] = $service->consulterPrix(array('ref' => $listeProduit->getPanierReturn[$i]->reference));
																
												?>
												<tr>
													<td>
														<a href="">
															<?php
															echo $listeProduit->getPanierReturn[$i]->reference;
															?>
														</a>
													</td>
													<td>
														<?php
													echo $listeProduit->getPanierReturn[$i]->quantite;
														?>
													</td>
													<td>
														<?php
														echo $listePrixProduit[$i]->consulterPrixReturn;
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
														echo $listeProduit->getPanierReturn->reference;
														?>
													</a>
												</td>
												<td>
													<?php
													echo $listeProduit->getPanierReturn->quantite;
													?>
												</td>
												<td>
													<?php
													echo $listeProduit->getPanierReturn->quantite;
													?>
												</td>
												<td>
													<?php
													echo $listeProduit->getPanierReturn->prix;
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

