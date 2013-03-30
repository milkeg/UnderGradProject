<?php

	/*$rb = new ReseauBayesien();
	$a = new Variable('a');
	$b = new Variable('b');
	$e = new Variable('e');
	$j = new Variable('j');
	$m = new Variable('m');
	
	$a->liste_fils = array($j, $m);
	$b->liste_fils = array($a);
	$e->liste_fils = array($a);
	$j->liste_parents = array($a);
	$m->liste_parents = array($a);
	$a->liste_parents = array($b, $e);
	
	$rb->liste_variables = array($a, $b, $e, $j, $m);

	//__construct($nb_variables, $variables, $matrice, $valeurs)
	$data = array();
	$data[] = new MatriceProbabilite(1, array($e), array(array(true)), array(0.002));
	$data[] = new MatriceProbabilite(1, array($b), array(array(true)), array(0.001));
	$data[] = new MatriceProbabilite(2, array($j, $a), array(array(true, true)), array(0.9));
	$data[] = new MatriceProbabilite(2, array($j, $a), array(array(true, false)), array(0.05));
	$data[] = new MatriceProbabilite(2, array($m, $a), array(array(true, true)), array(0.7));
	$data[] = new MatriceProbabilite(2, array($m, $a), array(array(true, false)), array(0.01));
	$data[] = new MatriceProbabilite(3, array($a, $b, $e), array(array(true, true, true)), array(0.95));
	$data[] = new MatriceProbabilite(3, array($a, $b, $e), array(array(true, true, false)), array(0.94));
	$data[] = new MatriceProbabilite(3, array($a, $b, $e), array(array(true, false, true)), array(0.29));
	$data[] = new MatriceProbabilite(3, array($a, $b, $e), array(array(true, false, false)), array(0.001));

	$rb->data = $data;
	$rb->initData();
	$req = new MatriceProbabilite(2, array($e, $j, $m), array(true, true, true), array());

	var_dump($rb->eliminationAsk($req));
	*/
	

	class ReseauBayesien
	{
		//public $liste_variables; //List<Variable>
		
		public $data;
		
		
		public function __construct()
		{
			//$this->liste_variables = array();
			$this->data = array();
		}
		
		
		// A partir du réseau, il suffit de lui donner la var requete, et les variables observées pour sortir les variables cachées
		public function variableCachees($var_req, $var_observees)
		{
			if(!is_array($var_observees) || is_array($var_req)) return array();

			$var_cachees = array();
			$var_cachees = $this->ancetres($var_req);
			foreach($var_observees as $vo)
				$var_cachees = array_merge($var_cachees, $this->ancetres($vo));
			//$var_cachees = array_unique(array_merge($var_cachees, array_map('ReseauBayesien::ancetres', $var_observees)));
			$var_cachees = array_diff($var_cachees, $var_observees);
			$var_cachees = array_diff($var_cachees, array($var_req));
			return array_unique($var_cachees);
		}
		
		public static function ancetres($variable)
		{
			$ret = array();
			if($variable->liste_parents != null || count($variable->liste_parents) > 0)
			{
				foreach($variable->liste_parents as $parent)
				{
					$ret[] = $parent;
					$ret = array_merge($ret, ReseauBayesien::ancetres($parent));
				}
			}
			return $ret;
		}
		
		
		public function initData()
		{
			$data1 = array();
			foreach($this->data as $item)
			{
				$temp = new MatriceProbabilite($item->nb_variables, $item->variables, $item->matrice, $item->valeurs);
				$temp->matrice[0][0] = !$temp->matrice[0][0];
				$temp->valeurs[0] = 1 - $temp->valeurs[0];
				$data1[] = $temp;
			}
			$this->data = array_merge($this->data, $data1);
			
			
			foreach($this->data as $item)
			{
				for($j=0; $j < count($item->matrice); $j++)
				{					
					$i=0;
					foreach ($item->matrice[$j] as $k => $v) {
						if($i == count($item->matrice[$j])) break;
						unset ($item->matrice[$j][$k]);
						$new_key =  $item->variables[$i]->nom;
					    $item->matrice[$j][$new_key] = $v;
						$i++;
					}
				}
			}
		}
		
		public function eliminationAsk($req) // param : MatriceProbabilite
		{
			$var_req = $req->variables[0];
			$var_observees = array_diff_key($req->variables, array(0));
			
			//echo 'Vous voulez calculer la probabilité de '.$var_req.' soit '.$req->matrice[0].' sachant : ';
			//var_dump($var_observees);
			
			$factors = array();
			$var_cachees = $this->variableCachees($var_req, $var_observees);
			$i = 0;
			//echo '~~~vars cachees'; var_dump($var_cachees); echo '////vars cachees';
			$union_vars = array_merge(array_merge(array($var_req ), $var_observees), $var_cachees);
			
			if(in_array($var_req, $var_observees))
			{
				//$temp = array_search($var_req, $var_observees);
				/*for($i=0; $i < count($req->matrice); $i++)
					$req->matrice[(string)$req->variables[$i]] = $req->matrice[$i];
				var_dump($req->matrice);
				var_dump($req->valeurs);*/
				//return new MatriceProbabilite(count($req->variables), $req->variables, $req->matrice, array((int) ($req->matrice[0] == $req->matrice[$temp])));
				return null;
			}
			
			foreach($union_vars as $item)
			{
				$factors[$i] = $this->makeFactor($item, $req->matrice[array_search($item, $req->variables)] , $var_observees, $req);
				/*
				echo 'facteur '.$i.' : '.$item;
				var_dump($factors[$i]);
				echo '<hr />'; //*/
				$i++;
			}
			
			$product = $factors[0];
			for($j=1; $j < count($factors); $j++)
				$product = $this->product($product, $factors[$j]);
			
			foreach($union_vars as $item)
			{
				if(in_array($item, $var_cachees))
				{
					$product = $this->sumOut($item, $product);
					//echo 'somme par rapport à '.$item.'<br />';
				}
			}
			/*echo 'Résultat :<pre>';
			var_dump($product);
			echo '</pre>';*/
			//$product = $this->eliminerVarObs($product, $var_observees, $req);
			
			//var_dump($product);
			
			return $this->normalise($product);
		}
		
		public static function normalise($product)
		{
			$somme = 0;
			$product->valeurs = array_values($product->valeurs );
			foreach($product->valeurs as $val)
				$somme += $val;
			
			if($somme == 0) return 0;
			
			for($i=0; $i < count($product->valeurs); $i++)
				$product->valeurs[$i] /= $somme;
			return $product;
		}
		
		
		public function sumOut($item, $factor)
		{
			if(!in_array($item, $factor->variables)) 
			{
				/*echo $item;
				var_dump($factor->variables);*/
				return 0;
			}
				
			if($factor->nb_variables == 1) return 1;
			
			$matrix = new MatriceProbabilite($factor->nb_variables-1, null, null, null);
			$matrix->variables = array_diff($factor->variables, array($item));
			$matrix->matrice = array();
			
			$indice_var = array_search($item, $factor->variables);
			$i = 0;
			
			foreach($factor->matrice as $ligne)
			{
				$ligne_temp = array_diff_assoc($ligne, array((string)$factor->variables[$indice_var] => $ligne[(string)$factor->variables[$indice_var]]));
				/*echo '###################################';
				var_dump($ligne);
				var_dump($ligne_temp);
				echo '###################################';*/
				
				if(in_array($ligne_temp, $matrix->matrice)) 
				{
					$indice_ligne = array_search($ligne_temp, $matrix->matrice);
					//var_dump($matrix->valeurs[$indice_ligne]); echo '<hr />';
					$matrix->valeurs[$indice_ligne] += $factor->valeurs[$i];
				}
				else 
				{
					//echo ' __'.$i.' __ ';
					$matrix->matrice[] = $ligne_temp;
					//echo 'var dump '.$i.' : '; var_dump($matrix->matrice);
					$matrix->valeurs[] = $factor->valeurs[$i];
					
					/*echo '$$$$$$$$$$$$$$ '.$i;
					var_dump($ligne_temp); echo 'est introuvable';
					var_dump($matrix->matrice);*/
				}
				$i++;
			}
			
			return $matrix;
			/*echo $item. ' : <pre>';
			print_r($matrix);
			echo '</pre><hr />';
			echo '<pre>';
			print_r($factor);
			echo '</pre><hr />';*/
		}
		
		
		public function makeFactor($noeud, $val_noeud, $var_observees, $req)
		{
			$matrix = new MatriceProbabilite(0, null, null, null);
			
			$matrix->variables[] = $noeud;
			$matrix->variables = array_unique(array_merge($matrix->variables, $noeud->liste_parents));
			
			//$matrix->variables = array_unique(array_merge(array($noeud), array_diff($this->ancetres($noeud), $var_observees)));
			
			$matrix->nb_variables = count($matrix->variables);
			$i=0;
			//echo '######'.$noeud;var_dump($matrix->variables);echo '//////';
			foreach($this->data as $item)
			{
				$i++;
				if(count($item->variables) != count($matrix->variables)) continue;
				if($item->variables[0] != $matrix->variables[0]) continue;
				
				$pass = false;
				for($i = 1; $i < count($matrix->variables); $i++)
				{
					if(!in_array($matrix->variables[$i], $item->variables)) 
					{
						$pass = true;
						break;
					}
				}
				
				if(!$pass)
				{
					//echo 'valNoeud='.$val_noeud.', matrice='.$item->matrice[(string)$noeud].'<br />';
					if((in_array($noeud, $var_observees) && array_key_exists((string)$noeud, $item->matrice[0]) && 
						$val_noeud == $item->matrice[0][(string)$noeud]) 
						|| !in_array($noeud, $var_observees))
					{
						if(!is_array($matrix->matrice)) $matrix->matrice = array();
						$matrix->matrice = array_merge($matrix->matrice, $item->matrice);
						foreach($item->valeurs as $value)
							$matrix->valeurs[] = $value;
					}
				}
			}
			//var_dump($matrix->variables);
			//if($matrix->nb_variables != 1 || !in_array($noeud, $var_observees))
			//{
				$this->eliminerVarObs($matrix, $var_observees, $req);
				/*
				// On élimine les lignes qui ne servent à rien par rapport aux variables observées :		
				$indices_var_obs = array();
				$num_var_obs = array();
				$valeus_var_obs = array();
				for($i=0; $i < count($matrix->variables); $i++)
				{
					if(in_array($matrix->variables[$i], $var_observees))
					{
						$indices_var_obs[] = (string)$matrix->variables[$i];
						$num_var_obs[] = $i;
						$valeus_var_obs[] = $req->matrice[array_search($matrix->variables[$i], $req->variables)];
					}
				}
				$to_del = array();
				for($k=0; $k < count($indices_var_obs); $k++)
				{
					
					for($i=0; $i < count($matrix->matrice); $i++)
					{
						$ligne = $matrix->matrice[$i];
						
						if($ligne[$indices_var_obs[$k]] != $valeus_var_obs[$k]) 
							$to_del[] = $i;
						unset($matrix->matrice[$i][$indices_var_obs[$k]]);
					}
					
				}
				for($k=0; $k < count($indices_var_obs); $k++)
				{
					foreach($to_del as $indice)
					{
						unset($matrix->matrice[$indice]);
						unset($matrix->valeurs[$indice]);
					}
					//echo '### je vais supp : '.$indices_var_obs[$k];
					unset($matrix->variables[$num_var_obs[$k]]);
				}*/
			//}
			
			return $matrix;
		}
		
		
		public function eliminerVarObs($matrix, $var_observees, $req)
		{
			$var_observees = array_values($var_observees);
			
			$val_var_observees = $to_del = array();
			
			for($i=0; $i < count($var_observees); $i++)
			{
				if(in_array($var_observees[$i], $matrix->variables))
					$val_var_observees[] = $req->matrice[array_search($var_observees[$i], $req->variables)];
				else
					$to_del[] = $i;
			}
			foreach($to_del as $indice)
				unset($var_observees[$indice]);
			
			$var_observees = array_values($var_observees);
			
			///*
			$to_del = array();
			$j = 0;
			for($i=0; $i < count($var_observees); $i++)
			{
				foreach($matrix->matrice as $ligne)
				{
					/*
					echo 'vaaaaaaaaaaaaaaaaaar: '.$var_observees[$i];
					var_dump($var_observees);
					var_dump($val_var_observees);
					echo '///vaaaaaaaaaaaaaaaaar'; //*/
					
					if($ligne[(string)$var_observees[$i]] != $val_var_observees[$i])
						$to_del[] = $j;
					$j++;
				}
				foreach($to_del as $indice)
				{
					unset($matrix->matrice[$indice]);
					unset($matrix->valeurs[$indice]);
				}
			}
			//*/
			
			/*
			echo 'vaaaaaaaaaaaaaaaaaar';
			var_dump($var_observees);
			var_dump($matrix);echo '///vaaaaaaaaaaaaaaaaar<hr />';
			//*/
		}
		
		public function eliminerVarObs1($matrix, $var_observees, $req)
		{
			// On élimine les lignes qui ne servent à rien par rapport aux variables observées :	
			
			$temp = array_diff($matrix->variables, $var_observees);
			if(count($temp) == 0)
			{
				$i=0;
				$val_var_observees = array();
				foreach($var_observees as $var)
					$val_var_observees[] = $req->matrice[array_search($var, $req->variables)];
				
				foreach($this->data as $item)
				{
					if($matrix->variables == $item->variables)
					{
						$break = false;
						foreach($matrix->matrice as $ligne)
						{
							for($j=0; $j < count($ligne); $j++)
							{
								if($ligne[$j] != $val_var_observees[array_search($matrix->variables[$j], $var_observees)])
									$break = true;
							}
							if(!$break) 
							{
								$matrix->matrice = array($ligne);
								$matrix->valeurs = array($matrix->valeurs[$i]);
								return $matrix;
							}
							$i++;
						}
					}
					
					/*$i++;
					if(count($item->variables) != count($matrix->variables)) continue;
					if($item->variables[0] != $matrix->variables[0]) continue;
					
					$pass = false;
					for($i = 1; $i < count($matrix->variables); $i++)
					{
						if(!in_array($matrix->variables[$i], $item->variables)) 
						{
							$pass = true;
							break;
						}
					}
					
					if(!$pass)
					{
						foreach($matrix->variables as $noeud)
						{
							$val_noeud = $req->matrice[array_search($item, $req->variables)];
							
							if((in_array($noeud, $var_observees) && array_key_exists((string)$noeud, $item->matrice[0]) && 
								$val_noeud == $item->matrice[0][(string)$noeud]) 
								|| !in_array($noeud, $var_observees))
							{
								if(!is_array($matrix->matrice)) $matrix->matrice = array();
								$matrix->matrice = $item->matrice;
								foreach($item->valeurs as $value)
									$matrix->valeurs[] = $value;
								return $matrix;
							}
						}
					}*/
				}
			}
			
			$matrix->variables = array_values($matrix->variables);
			
			$indices_var_obs = array();
			$num_var_obs = array();
			$valeus_var_obs = array();
			for($i=0; $i < count($matrix->variables); $i++)
			{
				if(in_array($matrix->variables[$i], $var_observees))
				{
					$indices_var_obs[] = (string)$matrix->variables[$i];
					$num_var_obs[] = $i;
					$valeus_var_obs[] = $req->matrice[array_search($matrix->variables[$i], $req->variables)];
				}
			}			
			$to_del = array();
			for($k=0; $k < count($indices_var_obs); $k++)
			{
				for($i=0; $i < count($matrix->matrice); $i++)
				{
					$ligne = $matrix->matrice[$i];
						
					if($ligne[$indices_var_obs[$k]] != $valeus_var_obs[$k]) 
						$to_del[] = $i;
					unset($matrix->matrice[$i][$indices_var_obs[$k]]);
				}
			}
			for($k=0; $k < count($indices_var_obs); $k++)
			{
				foreach($to_del as $indice)
				{
					unset($matrix->matrice[$indice]);
					unset($matrix->valeurs[$indice]);
				}
				//echo '### je vais supp : '.$indices_var_obs[$k];
				unset($matrix->variables[$num_var_obs[$k]]);
			}
			if($matrix->matrice != null)
			{
			$matrix->variables = array_values($matrix->variables);
			$matrix->nb_variables = count($matrix->variables);
			$matrix->matrice = array_values($matrix->matrice);
			$matrix->valeurs = array_values($matrix->valeurs);
			}
			return $matrix;
		}
		
		
		public function product($m1, $m2)
		{
			// on récupère les colonnes en commun
			$var_commun = array_intersect($m1->variables, $m2->variables);
			
			/*echo '~~~~~~variables en commun :';
			foreach($m1->variables as $var1)
				echo $var1.'-';
			echo '<br />';
			foreach($m2->variables as $var1)
				echo $var1.'-';
			var_dump($var_commun);
			echo '~~~~~';*/
			//echo '##### produit de '; var_dump($m1);
			//echo '##### avec :'; var_dump($m2); echo '##### FIN';
			
			
			// On trie les deux matrices par rapport aux colonnes communes			
			$i = 0;
			$c1 = array();
			foreach ($m1->matrice as $key => $row) 
			{
				foreach($var_commun as $item)
					$c1[$i++][$key]  = $row["$item"];
				$i = 0;
			}
			foreach($c1 as $item)
				array_multisort($item, SORT_DESC, $m1->matrice, $m1->valeurs);
			
				
			$i = 0;
			$c2 = array();
			foreach ($m2->matrice as $key => $row) 
			{
				foreach($var_commun as $item)
					$c2[$i++][$key]  = $row["$item"];
				$i = 0;
			}
			foreach($c2 as $item)
				array_multisort($item, SORT_DESC, $m2->matrice, $m2->valeurs);
			
			
			$product = new MatriceProbabilite($m1->nb_variables + $m2->nb_variables - count($var_commun));
			$product->variables = array_unique(array_merge($m1->variables, $m2->variables));
			
			/*echo '**************** <pre>';
			print_r($procuct->variables);
			echo '</pre>**************** ';*/
			
			$m1->valeurs = array_values($m1->valeurs);
			$m2->valeurs = array_values($m2->valeurs);
			$i = $j = 0;
			foreach($m1->matrice as $item1)
			{
				$j = 0;
				foreach($m2->matrice as $item2)
				{
					$pass = false;
					foreach($var_commun as $comm)
					{
						if($item1["$comm"] != $item2["$comm"]) $pass = true;
					}
					if(!$pass)
					{
						$product->matrice[] = array_merge($item1, $item2);
						$product->valeurs[] = $m1->valeurs[$i] * $m2->valeurs[$j];
						//echo 'M1 : v1<'.$item1['v1'].'> x v2<'.$item1['v2'].'>. M2 : v2<'.$item2['v2'].'> u1<'.$item2['u1'].'> u2<'.$item2['u2'].'><br />';
					}
					$j++;
				}
				$i++;
			}
			
			return $product;
		}
	}
	
	class MatriceProbabilite
	{
		public $nb_variables; //int 
		public $variables; // array 
		public $matrice; // array 
		public $valeurs; // array 
		
		public function __construct($nb_variables, $variables=null, $matrice=null, $valeurs=null)
		{
			$this->nb_variables = $nb_variables;
			
			$this->valeurs = $valeurs; // de taille 2^nb_var
			$this->matrice = $matrice; // de taille 3 x (2^nb_var)
			$this->variables = $variables;
		}
		
		
		// retourne un jeu de test...
		public static function fillMatrix()
		{
			$m1 = new MatriceProbabilite(2);
			$m1->variables = array('v1', 'v2');

			$m1->matrice[] = array($m1->variables[0] => true, $m1->variables[1] => true);
			$m1->matrice[] = array($m1->variables[0] => true, $m1->variables[1] => false);
			$m1->matrice[] = array($m1->variables[0] => false, $m1->variables[1] => true);
			$m1->matrice[] = array($m1->variables[0] => false, $m1->variables[1] => false);
			
			$m1->valeurs = array(0.1, 0.2, 0.3, 0.4);
			
			$m2 = new MatriceProbabilite(3);
			$m2->variables = array('u3', 'u1', 'u2');

			$m2->matrice[] = array($m2->variables[0] => false, $m2->variables[1] => false, $m2->variables[2] => false);
			$m2->matrice[] = array($m2->variables[0] => false, $m2->variables[1] => false, $m2->variables[2] => true);
			$m2->matrice[] = array($m2->variables[0] => false, $m2->variables[1] => true, $m2->variables[2] => false);
			$m2->matrice[] = array($m2->variables[0] => false, $m2->variables[1] => true, $m2->variables[2] => true);
			$m2->matrice[] = array($m2->variables[0] => true, $m2->variables[1] => false, $m2->variables[2] => false);
			$m2->matrice[] = array($m2->variables[0] => true, $m2->variables[1] => false, $m2->variables[2] => true);
			$m2->matrice[] = array($m2->variables[0] => true, $m2->variables[1] => true, $m2->variables[2] => false);
			$m2->matrice[] = array($m2->variables[0] => true, $m2->variables[1] => true, $m2->variables[2] => true);
			
			$m2->valeurs = array(0.5, 0.6, 0.7, 0.8, 0.9, 0.11, 0.12, 0.13);
			
			return array($m1, $m2);
		}
	
	}
	
	class Variable
	{
		public $nom; //string
		public $liste_parents; //List<Variable>
		//public $liste_fils; //List<Variable>, to delete ?
		
		public function __construct($nom)
		{
			$this->nom = $nom;
			$this->liste_parents = array();
			//$this->liste_fils = array();
		}
		
		public function __toString()
		{
			return $this->nom;
		}
	}
	
	
	/*$rb = new ReseauBayesien();
	$fn = new Variable('fn');
	$sa = new Variable('sa');
	$cp = new Variable('cp');
	$zo = new Variable('zo');
	
	$fn->liste_fils = array($sa, $cp);
	$zo->liste_fils = array($cp);
	$sa->liste_parents = array($fn);
	$cp->liste_parents = array($zo, $fn);
	
	$rb->liste_variables = array($sa, $fn, $cp, $zo);

	//__construct($nb_variables, $variables, $matrice, $valeurs)
	$data = array();
	$data[] = new MatriceProbabilite(1, array($fn), array(true), array(0.95));
	$data[] = new MatriceProbabilite(1, array($zo), array(true), array(0.7));
	$data[] = new MatriceProbabilite(2, array($sa, $fn), array(true, true), array(0.95));
	$data[] = new MatriceProbabilite(2, array($sa, $fn), array(true, false), array(0.1));
	$data[] = new MatriceProbabilite(3, array($cp, $fn, $zo), array(true, true, true), array(0.9));
	$data[] = new MatriceProbabilite(3, array($cp, $fn, $zo), array(true, true, false), array(0.05));
	$data[] = new MatriceProbabilite(3, array($cp, $fn, $zo), array(true, false, true), array(0));
	$data[] = new MatriceProbabilite(3, array($cp, $fn, $zo), array(true, false, false), array(0));

	$rb->data = $data;
	$rb->initData();
	$req = new MatriceProbabilite(2, array($zo, $cp), array(true, false), array());
	//$rb->makeFactor($fn, false, array($cp));
	
	$rb->eliminationAsk($req);*/
	
?>