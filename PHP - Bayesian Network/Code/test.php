<?php
	// Initialisation du réseau bayésien :
	
	include('BayesianNetwork.v4.class.php');
	
	$rb = new ReseauBayesien();
	
	$variables = array();
	$variables['h'] = new Variable('h'); // hardware
	$variables['p'] = new Variable('p'); // Code 
	$variables['e'] = new Variable('e'); // Editeur
	$variables['i'] = new Variable('i'); // Interpréteur
	$variables['c'] = new Variable('c'); // Curseur
	$variables['ic'] = new Variable('ic'); // Invite de commande
	
	//$rb->liste_variables = array_values($variables);
	
	/*
	$variables['h']->liste_fils = array($variables['e'], $variables['i']);
	$variables['p']->liste_fils = array($variables['i']);
	$variables['e']->liste_fils = array($variables['c']);
	$variables['i']->liste_fils = array($variables['ic']);
	//*/
	
	$variables['e']->liste_parents = array($variables['h']);
	$variables['i']->liste_parents = array($variables['h'], $variables['p']);
	$variables['c']->liste_parents = array($variables['e']);
	$variables['ic']->liste_parents = array($variables['i']);
	
	$data = array();
	$data[] = new MatriceProbabilite(1, array($variables['p']), array(array(true)), array(0.6)); // P (Programme OK)
	$data[] = new MatriceProbabilite(1, array($variables['h']), array(array(true)), array(0.99)); // P (Hardware OK)
	$data[] = new MatriceProbabilite(2, array($variables['e'], $variables['h']), array(array(true, true)), array(0.95)); // P (Editeur OK / Hardware OK)
	$data[] = new MatriceProbabilite(2, array($variables['e'], $variables['h']), array(array(true, false)), array(0.1)); // P (Editeur OK / ⌐Hardware OK)
	$data[] = new MatriceProbabilite(2, array($variables['c'], $variables['e']), array(array(true, true)), array(0.95)); // P (Curseur clignotant/Editeur OK)
	$data[] = new MatriceProbabilite(2, array($variables['c'], $variables['e']), array(array(true, false)), array(0)); // P (Curseur clignotant/ ⌐Editeur OK)
	//P (Interpréteur LispOK / Hardware OK ˄ Programme OK)
	$data[] = new MatriceProbabilite(3, array($variables['i'], $variables['h'], $variables['p']), array(array(true, true, true)), array(0.97)); 
	//P (Interpréteur LispOK / ⌐Hardware OK ˄ Programme OK)
	$data[] = new MatriceProbabilite(3, array($variables['i'], $variables['h'], $variables['p']), array(array(true, false, true)), array(0.05)); 
	//P (Interpréteur LispOK / Hardware OK ˄ ⌐Programme OK)
	$data[] = new MatriceProbabilite(3, array($variables['i'], $variables['h'], $variables['p']), array(array(true, true, false)), array(0.8)); 
	//P (Interpréteur LispOK / ⌐Hardware OK ˄ ⌐Programme OK)
	$data[] = new MatriceProbabilite(3, array($variables['i'], $variables['h'], $variables['p']), array(array(true, false, false)), array(0.01)); 
	// P (Invite de commande OK / Interpréteur Lisp OK)
	$data[] = new MatriceProbabilite(2, array($variables['ic'], $variables['i']), array(array(true, true)), array(0.8)); 
	// P (Invite de commande OK / ⌐Interpréteur Lisp OK)
	$data[] = new MatriceProbabilite(2, array($variables['ic'], $variables['i']), array(array(true, false)), array(0.05)); 
	
	$rb->data = $data;
	$rb->initData();
?>



<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>Projet RPC</title>
	</head>

	<body>
	<div style="display:none;">
	<h1>Projet de représentation des connaissances : le problème de Fred</h1>
	<div id = "enonce">
		<p>Fred est en train de débugger un programme LISP. Il vient de taper une expression pour l'interpréteur LISP, et mainteannt l'intérpréteur LISP ne répond plus à aucune frappe du clavier. Fred ne peut pas voir l'invite de commande qui indique habituellement que l'interpréteur est en attende d'une entrée supplémentaire. D'après ce que sait Fred, il y a seulement deux situations qui pourraient causer l'arret de l'interpréteur : </p>
		<p>(1) Il y a des problèmes avec le harware de l'ordinateur</p>
		<p>(2) Il y a une erreur dans le code de Fred</p>
		<p> Fred est également en train d'utiliser un éditeur pour écrire et modifier son code. Si le harware fonctionne correctement, alors l'éditeur doit fonctionner. Si l'éditeur fonctionne, son curseur doit clignoter. on sait de plus que le hardware est assez fiable, et qu'il fonctionne 99 fois sur 100, alors que le code Lisp de Fred contient des erreurs 40 fois sur 100.</p>
		</div>
		<div id = "arbre">
		<p>Ici on met une image du truc baysien</p>
		</div>
		<div id = "probas">
			<table style = "border:solid;">
				<tr>
				<td>P(H)</td>
				<td></td>
				</tr>
				<tr>
				<td>P(Code)</td>
				<td></td>
				</tr>
				<tr>
				<td>P(E)</td>
				<td></td>
				</tr>
				<tr>
				<td>P(I)</td>
				<td></td>
				</tr>
				<tr>
				<td>P(C)</td>
				<td></td>
				</tr>
				<tr>
				<td>P(IC)</td>
				<td></td>
				</tr>
			</table>
		</div>
		<div id="legende">
		<p>Variable H : représente la probabilité que le hardware fonctionne.</p>
		<p>Variable Code : représente la probabilité que le code de Fred ne contienne pas d'erreur.</p>
		<p>Variable E : représente la probabilité que l'éditeur fonctionne.</p>
		<p>Variable I : représente la probabilité que l'interpréteur fonctionne.</p>
		<p>Variable C : représente la probabilité que le curseur clignote.</p>
		<p>Variable IC : représente la probabilité que l'invite de commande fonctionne</p>
		</div>
		</div>
		
		<div id="resultats">
			<?php
				function varEmpty($var)
				{
					return empty($_POST[$var]);
				}
				function varUnknown($var)
				{
					return $_POST[$var]=='inconnue';
				}
				
				if(isset($_POST['envoyer']))
				{
					$err = array();
					
					$vars = array('h', 'e', 'c', 'p', 'i', 'ic');
					
					// On teste les valeurs récupérées ...
					if(empty($_POST['requete']) || empty($_POST['reqval']))
						$err[] = 'Vous devez spécifier une variable de requête et sa valeur';
					if(in_array(true, array_map('varEmpty', $vars)))
						$err[] = 'Vous devez renseigner au moins une variable observée';
						
					$isVarUnknown = array_map('varUnknown', $vars);
					if(!in_array(false, $isVarUnknown))
						$err[] = 'Vous devez renseigner au moins une variable observée';
					
					// On commence le traitement
					if(count($err) != 0)
						var_dump($err); // TO-DO: améliorer l'affichage des erreurs
					else
					{
						$var_req = $variables[$_POST['requete']];
						$val_var_req = $_POST['reqval'] == 'vraie' ? true : false;;
						
						$var_obs = array();
						$val_var_obs = array();
						for($i=0; $i < count($isVarUnknown); $i++)
						{
							if(!$isVarUnknown[$i]) 
							{
								$var_obs[] = $variables[$vars[$i]];
								$val_var_obs[] = $_POST[(string)$variables[$vars[$i]]] == 'vraie' ? true : false;
							}
						}
						$req = new MatriceProbabilite(count($var_obs)+1, array_merge(array($var_req), $var_obs), array_merge(array($val_var_req), $val_var_obs), array());
						var_dump($rb->eliminationAsk($req));
						
						/*echo $var_req.' _ ';
						echo $val_var_req;
						var_dump($var_obs);
						var_dump($val_var_obs);*/
					}
				}
				//*
				else
				{
					$reqOK1 = new MatriceProbabilite(3, array($variables['ic'], $variables['h'], $variables['p']), array(true, true, true), array());
					$reqOK2 = new MatriceProbabilite(4, array($variables['ic'], $variables['h'], $variables['c'], $variables['i']), 
					array(true, false, true, false), array());
					$reqOK3 = new MatriceProbabilite(4, array($variables['h'], $variables['e'], $variables['c'], $variables['p'], $variables['i'], $variables['ic']), 
					array(true, false, true, false, true, false), array());
					$req = new MatriceProbabilite(2, array($variables['c'], $variables['h']), array(true, false), array());
					echo 'Résultat : '; var_dump($rb->eliminationAsk($req));
					//var_dump(ReseauBayesien::ancetres($variables['ic']));
				}
				//*/
			?>
		</div>
		
		
		<div id = "listevariables">
			<form method="post" action="#">
				<table>
					<tr><td>Quelle probabilité voulez vous évaluer?</td></tr>
					<tr><td>
						<select name="requete" id="requete">
						<option value="h">Hardware</option>
						<option value="e">Editeur</option>
						<option value="c">Curseur</option>
						<option value="p">Code</option>
						<option value="i">Interpreteur</option>
						<option value="ic">Invite de commande</option>
					</td><td>

					       est :
					       <input type="radio" name="reqval" value="vraie" id="reqvalvraie" checked="checked" /> <label for="vraie">Vraie</label>
					       <input type="radio" name="reqval" value="faux" id="reqvalfaux" /> <label for="faux">Fausse</label>
       					
				
					
					</td></tr>
					
					
				</table>
				<table>
				
					<tr><td>Variable hardware</td>
					<td>
						<select name="h" id="hardware">
						<option value="inconnue">Inconnue</option>
						<option value="vraie">Vrai</option>
						<option value="faux">Faux</option>
					</td>
				
					<tr><td>Variable editeur</td>
					<td>
						<select name="e" id="editeur">
						<option value="inconnue">Inconnue</option>
						<option value="vraie">Vrai</option>
						<option value="faux">Faux</option>
					</td></tr>
					
					<tr><td>Variable curseur</td>
					<td>
						<select name="c" id="curseur">
						<option value="inconnue">Inconnue</option>
						<option value="vraie">Vrai</option>
						<option value="faux">Faux</option>
					</td></tr>
					
					<tr><td>Variable code</td>
					<td>
						<select name="p" id="code">
						<option value="inconnue">Inconnue</option>
						<option value="vraie">Vrai</option>
						<option value="faux">Faux</option>
					</td></tr>
					
					<tr><td>Variable interpreteur</td>
					<td>
						<select name="i" id="interpreteur">
						<option value="inconnue">Inconnue</option>
						<option value="vraie">Vrai</option>
						<option value="faux">Faux</option>
					</td></tr>
					
					<tr><td>Variable invite de commande</td>
					<td>
						<select name="ic" id="invite">
						<option value="inconnue">Inconnue</option>
						<option value="vraie">Vrai</option>
						<option value="faux">Faux</option>
					</td></tr>
					
					<tr>
						<td><input type="submit" name="envoyer" value="Faire la requete !" /></td>
					</tr>
				</table>
					
				
			</form>
	    	</div>
	</body>
</html>
