; Un atome Config est cree
; Celui-ci prendra comme propriete l'ensemble des valeurs dont nous avons besoins telles que
; la propriete heuristique a true pour l'heuristique
(setq config 'config)


(defun presentation ()
	(print "**********************************************************")
   	(print "***                  PROJET DE LISP                    ***")
   	(print "***                  Protagonistes :                   ***")
   	(print "***                    ARFI Victor                     ***")
   	(print "***                  GOLETTO Michael                   ***")
   	(print "***                  GUENNOUNI Salim                   ***")
	(print "**********************************************************")
   	(print "***	           Bienvenue sur notre projet IA !         ***")
	(print "**********************************************************")
	(terpri)
	(terpri)
	(terpri)	
	(noel)
	(terpri)
	(terpri)
)

(defun noel ()
	(princ "               X   X   XXXXX   XXXXX   XXXXX   X   X")(terpri)
	(princ "               XX XX   X       X   X   X   X    X X")(terpri)
	(princ "               X X X   XXX     XXXXX   XXXXX     X")(terpri)
	(princ "               X   X   X       X  X    X  X      X")(terpri)
	(princ "               X   X   XXXXX   X   X   X   X     X")(terpri)
	(terpri)
	(terpri)
	(princ "XXXXX   X   X   XXXXX   XXX   XXXXX   XXXXX   X   X   XXXXX   XXXXX")(terpri)
	(princ "X       X   X   X   X    X    X         X     XX XX   X   X   X")(terpri)
	(princ "X       XXXXX   XXXXX    X    XXXXX     X     X X X   XXXXX   XXXXX")(terpri)
	(princ "X       X   X   X  X     X        X     X     X   X   X   X       X")(terpri)
	(princ "XXXXX   X   X   X   X   XXX   XXXXX     X     X   X   X   X   XXXXX")(terpri)
)

(defun lancement ()
	
	(presentation)
	;Selection de la base de regle
	(choix_base_regles)
		; Selection du chainage (avant ou arrière)
	(Princ "Souhaitez-vous utiliser le chainage avant (AV) ou arrière (AR) ?")
	(terpri)
	(setq chainage (read))
	(cond
		((member chainage '(AV)) (choix_avant))
		((member chainage '(AR)) (choix_arriere))
		(t 
			(princ "Quel dommage ! Nous n'avons malheuresement pas compris ! Vous pouvez re-essayer (On est bien gentil quand meme!)")
			(terpri)
			(lancement)
		)
	)
)

(defun choix_avant ()
	
	; Selection de l'heuristique (lexicographique ou plus grand nombre de premisse)
	(Princ "Souhaitez-vous utiliser l'heuristique lexicographique (LX) ou l'heuristique sur le plus grand nombre de premisses (PR) ?")
	(terpri)
	(setq heuristique (read))
	(if (member heuristique '(PR)) (activer_heurisitque 'config) (desactiver_heurisitque 'config))
	
	; Selection des faits
	(Princ "Bon, maintenant il va falloir saisir la base de faits initiale !")
	(terpri)
	(Princ "(Sous la forme (fait_1 fait_2 ... fait_N). Merci de votre compréhension.)")
	(terpri)
	(setq liste_de_prop (read))
	(terpri)
	(chainer_avant liste_de_prop)
)

(defun choix_arriere ()
	
	; Selection de l'heuristique (lexicographique ou plus grand nombre de premisse)
	(Princ "Souhaitez-vous utiliser l'heuristique lexicographique (LX) ou l'heuristique sur le plus petit nombre de premisses inconnues (PR) ?")
	(terpri)
	(setq heuristique (read))
	(if (member heuristique '(PR)) (activer_heurisitque 'config) (desactiver_heurisitque 'config))
	
	; Selection des faits
	(Princ "Bon, maintenant il va falloir saisir le fait qu'on veut démontrer !")
	(terpri)
	(Princ "(Sous la forme fait_a_demontrer. Merci de votre compréhension.)")
	(terpri)
	(setq a_demontrer (read))
	(terpri)
	(fait_vraie)
	(terpri)
	(fait_demandable)
	(terpri)
	(probleme a_demontrer)
)

(defun fait_demandable ()
	(Princ "Saisissez les faits demandables sous la forme (fait_1 fait_2 ... fait_N).")
	(terpri)
	(Princ "S'il n'y en a pas, saisissez non.")
	(terpri)
	(setq faits_demandable (read))
	(cond
		((member faits_demandable '(non no n NON NO N)) nil)
		(t 
			(marquer_demandable_liste_de_prop faits_demandable)
		)
	)
)

(defun fait_vraie ()
	(Princ "Saisissez les faits à initialiser à vrais sous la forme (fait_1 fait_2 ... fait_N).")
	(terpri)
	(Princ "S'il n'y en a pas, saisissez non.")
	(terpri)
	(setq fait_vraie (read))
	(cond
		((member fait_vraie '(non no n NON NO N)) nil)
		(t 
			(marquer_vraie_liste_de_prop fait_vraie)
		)
	)
)

; Renvoie le nombre de premisses gauches de la regle r
(defun nbr_premiss (r)
	(length (partie_gauche_regle r))
)

; Compte le nombre de prémisses non-vraies dans une regle r (premisses pas forcement fausses, mais dont la veracite n'a pas ete defini)
(defun nbr_premiss_inconnues(r)
	(count_prop_inconnues (partie_gauche_regle r))
)

; Compte le nombre de prémisses non-vraies dans liste_de_prop (pas forcement fausses)
(defun count_prop_inconnues (liste_de_prop)
	(cond
		((null liste_de_prop) 0)
		((not(vraie_prop (car liste_de_prop)))
			(+ 1 (count_prop_inconnues (cdr liste_de_prop)))
		)
		((vraie_prop (car liste_de_prop))
			(+ 0 (count_prop_inconnues (cdr liste_de_prop)))
		)
	)	
)

; Renvoie le nombre maximal des premisses gauche de listeregle
(defun nbr_max_premiss (listeregle)
	(if (null listeregle)
		0
		(max (nbr_premiss (car listeregle))
			(nbr_max_premiss (cdr listeregle))
		)
	)
)

; Heuristique : retourne la regle ayant le nombre maximal de premisses
(defun heuristique (rlist)
	(if (null rlist)
	NIL
	(if (equal (nbr_premiss (car rlist)) (nbr_max_premiss rlist) )
		(car rlist)
		(heuristique (cdr rlist))
		)
	)
)

; Heuristique : retourne la regle ayant le nombre minimal de premisses
(defun heuristique_ARR (rlist)
	(if (null rlist)
	NIL
	(if (equal (nbr_premiss (recuperer_regle_entiere (car rlist) regles)) (nbr_min_premiss_ARR rlist) )
		(recuperer_regle_entiere (car rlist) regles)
		(heuristique_ARR (cdr rlist))
		)
	)
)

; Renvoie le nombre maximal des premisses gauche de listeregle
(defun nbr_min_premiss_ARR (listeregle)
	(if (null listeregle)
		; Nombre suffisamment grand, taille de la base de regles + 1
		(+ 1 (length regles))
		(min (nbr_premiss (recuperer_regle_entiere (car listeregle) regles))
			(nbr_min_premiss_ARR (cdr listeregle))
		)
	)
)

(defun choix_base_regles ()

	; Selection de la base de règle
	(Princ "Souhaitez-vous utiliser la base de regle de Winston (WI winston Winston WINSTON) ou la notre (PE perso Perso PERSO) ?")
	(terpri)
	(setq choix_regles (read))
	(cond
		((member choix_regles '(WI winston Winston WINSTON)) (regle_winston)(afficher_BR_WI))
		((member choix_regles '(PE perso Perso PERSO)) (regle_perso)(afficher_BR_Perso))
		(t 
			(princ "Quel dommage ! Nous n'avons malheuresement pas compris ! Vous pouvez re-essayer (On est bien gentil quand même!)")
			(terpri)
			(choix_base_regles)
		)
	)
)

(defun regle_perso ()
	(setq regles
		'(
		(R1 si (avoir-le-Bac Vouloir-Travailler) alors (Inscrire-BTS-IUT))
		(R2 si (avoir-le-Bac Vouloir-Etudier) alors (Inscrire-Licence))
		(R3 si (Inscrire-Licence avoir-le-Bac Bac-S) alors (Inscrire-Licence-Scientifique))
		(R4 si (Inscrire-Licence avoir-le-Bac Bac-STG) alors (Inscrire-Licence-Gestion))
		(R5 si (Inscrire-Licence avoir-le-Bac Bac-ES) alors (Inscrire-Licence-Economie))
		(R6 si (Inscrire-Licence avoir-le-Bac Bac-Litterature) alors (Inscrire-Licence-Litterature))
		(R7 si (avoir-la-Licence Vouloir-Travailler) alors (Inscrire-Master-Professionnel))
		(R8 si (avoir-la-Licence Vouloir-Etudier) alors (Inscrire-Master-Recherche))
		(R9 si (avoir-le-Master Vouloir-Travailler) alors (Inscrire-Entretien-Entreprise))
		(R10 si (avoir-le-Master Vouloir-Etudier) alors (Inscrire-These-Laboratoire))
		)
	)
)

(defun regle_winston ()
	(setq regles'(
		(r1		si 	(a-des-poils)
				alors (est-un-mamifere))
		(r2		si 	(donne-du-lait)
				alors (est-un-mamifere))
		(r3		si 	(a-des-plumes)
				alors (est-un-oiseau))
		(r4		si 	(vole 
					donne-des-oeufs)
				alors (est-un-oiseau))
		(r5 	si 	(mange-viande)
				alors (est-un-carnivore))
		(r6		si 	(a-des-dents-pointues 
					a-des-griffes 
					a-des-yeux-frontaux)
				alors (est-un-carnivore))
		(r7		si  (est-un-mamifere 
					a-des-sabots)
				alors (est-ongule))
		(r8		si	(est-un-mamifere
					est-un-ruminant)
				alors (est-ongule))
		(r9		si 	(est-un-mamifere 
					a-une-couleur-fauve 
					est-un-carnivore)
				alors (est-un-guepard))
		(r10	si 	(est-un-mamifere 
					a-une-couleur-fauve 
					est-un-carnivore 
					a-des-rayures-noires)
				alors (est-un-tigre))
		(r11	si	(a-des-taches-noires
					a-de-longues-pattes
					est-ongule
					a-un-long-cou)
				alors (est-une-girafe))
		(r12	si	(a-des-rayures-noires
					est-ongule)
				alors (est-un-zebre))
		(r13	si	(a-un-long-cou
					est-un-oiseau
					ne-vole-pas
					est-noir-et-blanc)
				alors (est-une-autruche))
		(r14	si (ne-vole-pas
					est-un-oiseau
					est-noir-et-blanc
					nage)
				alors (est-un-pingouin))
		(r15	si (est-un-oiseau
					vole-bien)
				alors (est-un-albatros))
		)
	)
)


(defun afficher_BR_WI ()
	(princ "Voici l'ensemble des règles :")(terpri)
	(princ "(r1  si (a-des-poils) alors (est-un-mamifere))")(terpri)
	(princ "(r2  si (donne-du-lait) alors (est-un-mamifere))")(terpri)
	(princ "(r3  si (a-des-plumes) alors (est-un-oiseau))")(terpri)
	(princ "(r4  si (vole donne-des-oeufs) alors (est-un-oiseau))")(terpri)
	(princ "(r5  si (mange-viande) alors (est-un-carnivore))")(terpri)
	(princ "(r6  si (a-des-dents-pointues a-des-griffes a-des-yeux-frontaux) alors (est-un-carnivore))")(terpri)
	(princ "(r7  si (est-un-mamifere a-des-sabots) alors (est-ongule))")(terpri)
	(princ "(r8  si (est-un-mamifere est-un-ruminant) alors (est-ongule))")(terpri)
	(princ "(r9  si (est-un-mamifere a-une-couleur-fauve est-un-carnivore) alors (est-un-guepard))")(terpri)
	(princ "(r1  si (est-un-mamifere a-une-couleur-fauve est-un-carnivore a-des-rayures-noires) alors (est-un-tigre))")(terpri)
	(princ "(r11 si (a-des-taches-noires a-de-longues-pattes est-ongule a-un-long-cou) alors (est-une-girafe))")(terpri)
	(princ "(r12 si (a-des-rayures-noires est-ongule) alors (est-un-zebre))")(terpri)
	(princ "(r13 si (a-un-long-cou est-un-oiseau ne-vole-pas est-noir-et-blanc) alors (est-une-autruche))")(terpri)
	(princ "(r14 si (ne-vole-pas est-un-oiseau est-noir-et-blanc nage) alors (est-un-pingouin))")(terpri)
	(princ "(r15 si (est-un-oiseau vole-bien) alors (est-un-albatros))")(terpri)
	(terpri)
)

(defun afficher_BR_Perso ()
	(princ "Voici l'ensemble des règles :")(terpri)
	(princ "(R1  si (avoir-le-Bac Vouloir-Travailler) alors (Inscrire-BTS-IUT))")(terpri)
	(princ "(R2  si (avoir-le-Bac Vouloir-Etudier) alors (Inscrire-Licence))")(terpri)
	(princ "(R3  si (Inscrire-Licence avoir-le-Bac Bac-S) alors (Inscrire-Licence-Scientifique))")(terpri)
	(princ "(R4  si (Inscrire-Licence avoir-le-Bac Bac-STG) alors (Inscrire-Licence-Gestion))")(terpri)
	(princ "(R5  si (Inscrire-Licence avoir-le-Bac Bac-ES) alors (Inscrire-Licence-Economie))")(terpri)
	(princ "(R6  si (Inscrire-Licence avoir-le-Bac Bac-Litterature) alors (Inscrire-Licence-Litterature))")(terpri)
	(princ "(R7  si (avoir-la-Licence Vouloir-Travailler) alors (Inscrire-Master-Professionnel))")(terpri)
	(princ "(R8  si (avoir-la-Licence Vouloir-Etudier) alors (Inscrire-Master-Recherche))")(terpri)
	(princ "(R9  si (avoir-le-Master Vouloir-Travailler) alors (Inscrire-Entretien-Entreprise))")(terpri)
	(princ "(R10 si (avoir-le-Master Vouloir-Etudier) alors (Inscrire-These-Laboratoire))")(terpri)
	(terpri)
)

; LES FONCTIONS _PROP ONT ETE TESTEES SUR UN ATOME : (setq f 'atome)

; Permet determiner la liste des premisses relative a la regle passee en parametre OK
(defun partie_gauche_regle (r)(caddr r ))

; Retourne la liste des conclusions d'une regle OK
(defun partie_droite_regle (r) (cadr(cdddr r )))
 
; Retourne le nom d'une regle OK
(defun nom_regle (r) (car r ))

; Permet de savoir si une regle a deja ete utilisee OK
(defun appliquee (r) (get (nom_regle r) 'appliquee))
 
; Positionne a vrai la regle -appliquee- pour la regle OK
(defun marquer_appliquee (r) (putprop (nom_regle r) 't 'appliquee))
 
; Donne, pour un fait donne, la liste des regles ou il fait partie de la conclusion
(defun r_concluent_sur_prop (p) (get p 'concl)) ;OK

; Un atome est dit terminal si la liste retournee par r_concluent_sur est vide
(defun terminal (p) (null (get p 'concl))) ;OK

; Permet de connaitre la valeur de la propriete demandable d'un fait OK
(defun demandable (p) (get p 'demandable) )
 
; Met a true la propriete demandable d'un fait OK
(defun marquer_demandable_prop (p) (putprop p 't 'demandable) ) 

; Met a true la propriete demandable de la liste de faits OK
(defun marquer_demandable_liste_de_prop (lp) (mapcar 'marquer_demandable_prop lp ))
 
; Traite la proposition -recherchee-, equivalent a Get OK
(defun recherchee_prop (p) (get p 'recherchee) )

; Met a true la propriete recherchee d'un fait  OK
(defun marquer_recherchee_prop (p) (putprop p 't 'recherchee) ) 

; Vérifie la proposition -vraie-, equivalent a Get OK
(defun vraie_prop (p) (get p 'vraie))

; Met a true la propriete vrai d'un fait  OK
(defun marquer_vraie_prop (p) (putprop p 't 'vraie))

; Verifie si l'atome est marqué recherchee et qu'il ne soit pas marqué vraie
(defun faux_prop (p) 
	(and 
		(recherchee_prop p)
		(not
			(vraie_prop p)
		)
	)
)

; Met a true la propriete vraie de la liste de faits OK
(defun marquer_vraie_liste_de_prop (lp) (mapcar 'marquer_vraie_prop lp ))

; Traite la proposition -presentee-, equivalent a Get OK
(defun presentee_prop (p) (get p 'presentee) )

; Met a true la propriete presentee d'un fait  OK
(defun marquer_presentee_prop (p) (putprop p 't 'presentee) ) 

; TERPI : Retour Chariot
; Pose une question => Ne recupère pas la reponse !
(defun poser_question_prop (p) 
	(terpri)
	(princ "Est-ce vrai(e) : ")
	(princ p)
	(princ " ? ")
	(terpri)
)

; A ETE MODIFIE
; Fonction inconnue :P
(defun some_classique (pred liste)
	(cond
		(
			(null liste)
			nil
		)
		(
			(apply pred 
				(list (car liste))
			)
				(car liste)	
		)
		(
		t
			(some_classique pred
				(cdr liste)
			)
		)
	)
)

(defun some_heuristique (pred liste)
	(cond
		(
			(null liste)
			nil
		)
		(
			(apply pred 
				(list (heuristique liste))
			)
				(heuristique liste)	
		)
		(
		t
			(some_heuristique pred
				; on retire la regles déjà testée
				(remove (heuristique liste) liste)
			)
		)
	)
)


;
(defun vraie_pg (liste_de_prop)
	(not
		(member nil 
			(mapcar 'vraie_prop liste_de_prop)
		)
	)
)	

(defun marquer_recherchee (p)(putprop p 't 'recherchee))

; putprop
(defun putprop (symbole valeur propriete)
		(setf
			(get symbole propriete)
			valeur
		)
		symbole
)


; Permet de connaitre la valeur de la propriete heuristique d'un fait OK
(defun verif_heurisitque (p) (get p 'heuristique) )
 
; Met a true la propriete heuristique d'un fait OK
(defun activer_heurisitque (p) (putprop p 't 'heuristique) )

; Met a false la propriete heuristique d'un fait OK
(defun desactiver_heurisitque (p) (putprop p 'nil 'heuristique) )

(defun recuperer_regle_entiere (r base_de_regles)
	(if (equal (nom_regle (car base_de_regles))  r ) 
		(car base_de_regles)
		(recuperer_regle_entiere r (cdr base_de_regles))
	)
)

; LES FONCTIONS _PROP ONT ETE TESTEES SUR UN ATOME : (setq f 'atome)

; Permet determiner la liste des premisses relative a la regle passee en parametre OK
(defun partie_gauche_regle (r)(caddr r ))

; Retourne la liste des conclusions d'une regle OK
(defun partie_droite_regle (r) (cadr(cdddr r )))
 
; Retourne le nom d'une regle OK
(defun nom_regle (r) (car r ))

; Permet de savoir si une regle a deja ete utilisee OK
(defun appliquee (r) (get (nom_regle r) 'appliquee))
 
; Positionne a vrai la regle -appliquee- pour la regle OK
(defun marquer_appliquee (r) (putprop (nom_regle r) 't 'appliquee))
 
; Donne, pour un fait donne, la liste des regles ou il fait partie de la conclusion
(defun r_concluent_sur_prop (p) (get p 'concl)) ;OK

; Un atome est dit terminal si la liste retournee par r_concluent_sur est vide
(defun terminal (p) (null (get p 'concl))) ;OK

; Permet de connaitre la valeur de la propriete demandable d'un fait OK
(defun demandable (p) (get p 'demandable) )
 
; Met a true la propriete demandable d'un fait OK
(defun marquer_demandable_prop (p) (putprop p 't 'demandable) ) 

; Met a true la propriete demandable de la liste de faits OK
(defun marquer_demandable_liste_de_prop (lp) (mapcar 'marquer_demandable_prop lp ))
 
; Traite la proposition -recherchee-, equivalent a Get OK
(defun recherchee_prop (p) (get p 'recherchee) )

; Met a true la propriete recherchee d'un fait  OK
(defun marquer_recherchee_prop (p) (putprop p 't 'recherchee) ) 

; Vérifie la proposition -vraie-, equivalent a Get OK
(defun vraie_prop (p) (get p 'vraie))

; Met a true la propriete vrai d'un fait  OK
(defun marquer_vraie_prop (p) (putprop p 't 'vraie))

; Verifie si l'atome est marqué recherchee et qu'il ne soit pas marqué vraie
(defun faux_prop (p) 
	(and 
		(recherchee_prop p)
		(not
			(vraie_prop p)
		)
	)
)

; Met a true la propriete vraie de la liste de faits OK
(defun marquer_vraie_liste_de_prop (lp) (mapcar 'marquer_vraie_prop lp ))

; Traite la proposition -presentee-, equivalent a Get OK
(defun presentee_prop (p) (get p 'presentee) )

; Met a true la propriete presentee d'un fait  OK
(defun marquer_presentee_prop (p) (putprop p 't 'presentee) ) 

; TERPI : Retour Chariot
; Pose une question => Ne recupère pas la reponse !
(defun poser_question_prop (p) 
	(terpri)
	(princ "Est-ce vrai(e) : ")
	(princ p)
	(princ " ? ")
	(terpri)
)

; A ETE MODIFIE
; Fonction inconnue :P
(defun some_classique (pred liste)
	(cond
		(
			(null liste)
			nil
		)
		(
			(apply pred 
				(list (car liste))
			)
				(car liste)	
		)
		(
		t
			(some_classique pred
				(cdr liste)
			)
		)
	)
)

(defun some_heuristique (pred liste)
	(cond
		(
			(null liste)
			nil
		)
		(
			(apply pred 
				(list (heuristique liste))
			)
				(heuristique liste)	
		)
		(
		t
			(some_heuristique pred
				; on retire la regles déjà testée
				(remove (heuristique liste) liste)
			)
		)
	)
)


;
(defun vraie_pg (liste_de_prop)
	(not
		(member nil 
			(mapcar 'vraie_prop liste_de_prop)
		)
	)
)	

(defun marquer_recherchee (p)(putprop p 't 'recherchee))

; putprop
(defun putprop (symbole valeur propriete)
		(setf
			(get symbole propriete)
			valeur
		)
		symbole
)


; Permet de connaitre la valeur de la propriete heuristique d'un fait OK
(defun verif_heurisitque (p) (get p 'heuristique) )
 
; Met a true la propriete heuristique d'un fait OK
(defun activer_heurisitque (p) (putprop p 't 'heuristique) )

; Met a false la propriete heuristique d'un fait OK
(defun desactiver_heurisitque (p) (putprop p 'nil 'heuristique) )

(defun recuperer_regle_entiere (r base_de_regles)
	(if (equal (nom_regle (car base_de_regles))  r ) 
		(car base_de_regles)
		(recuperer_regle_entiere r (cdr base_de_regles))
	)
)

(defun initialiser()
 	(cond 
		; DEBUT COND 1
		(
		; CONDITION
			(not (boundp 'regles))
        ; RESULT
			(princ "Vous ne m avez pas donne de regles")
        	(error)
		) ; FIN COND 1
	) ; FIN COND
	
	(cond 
		(
		(not (boundp 'propositions))
		(compile_regles)
		)
	)
  
	(Princ "Voulez vous que j'affiche mes pas d'inferences ?")
	(terpri)
	(setq reponse (read))
	
	(cond 
		(
			(member reponse '(o y oui))
			(setq md t)
		)
		(
		; Pas de gestions des caracteres bizares
		; Creer une fonction recursive
			t
			(setq md nil)
		)
	)
 
	(mapc 'rafraichir_prop propositions)
	(mapc 'rafraichir_regle regles)	
)


(defun compile_regles ()
  (setq propositions nil)
  (mapc 'extraire_regles regles)
  'Compilees
)

(defun extraire_regles(r)
	(mapc
		#'(lambda(p)
			(cond
				(
					(not(member p propositions))
					(setq propositions (cons p propositions) )
				)
			)

       		(cond
				(
					(not (member (nom_regle r) (get p 'concl) ) )
					(putprop p (cons (nom_regle r) (get p 'concl) ) 'concl)
				)
			)
		) ; FIN DU LAMBDA
	
		(partie_droite_regle r)
	)
	
	(mapc
		#'(lambda (p)
			(cond
				(
					(not(member p propositions))
					(setq propositions (cons p propositions) )
				)
			)
		) ; FIN DU LAMBDA
		
		(partie_gauche_regle r)
	)
)


(defun rafraichir_prop (p)

  (remprop p 'recherchee)
  (remprop p 'vraie)
  (remprop p 'presentee)
 )

(defun rafraichir_regle (r)
  (remprop (nom_regle r) 'appliquee)
)

(defun chainer_avant (liste_de_prop)
	(initialiser)
	(mapc 'marquer_vraie_prop liste_de_prop)
	(chainer_avant1)
	(presenter_resultats)
)

(defun chainer_avant1 ()
	(if (verif_heurisitque 'config)
		(setq rc (some_heuristique 'appliquer_regle regles))
		(setq rc (some_classique 'appliquer_regle regles))
		)
	(cond
		(
		( null rc ) () )
		( t (chainer_avant1) )
	)
)

(defun appliquer_regle (r)
	(cond
	((appliquee r) nil)
	((vraie_pg (partie_gauche_regle r))
	(marquer_appliquee r)
	(cond (
		(and (boundp 'md) md)
		(terpri)
		(princ "J'applique la règle : ")
		(terpri)
		(princ r)
		(terpri)
		)
	)
	(applique_partie_droite (partie_droite_regle r))
	)
	)
)

(defun presenter_resultats ()
	(terpri)(terpri)(terpri)
	(princ "Voici ce que j'en conclut")
	(terpri)
	(mapc 'presenter_prop propositions)
	'Cest_tout
)

(defun presenter_prop (p)
	(cond
		(  (presentee_prop p) nil)
		(t (marquer_presentee_prop p)
			(cond ((vraie_prop p) (print (list p 'est 'vraie)))
				  ((faux_prop p)  (print (list p 'est 'faux)))
					(md 
						nil
						;(princ "Je n'arrive à déduire rien de : ")(princ p )(terpri)
					)
			)
		)
	)
)

(defun questionner_et_conclure_prop (p)
	(poser_question_prop p)
	(setq reponse (read))
	(cond (( member reponse '(y yes oui o))
	(marquer_vraie_prop p)
	t)
	((member reponse '(non ? no n))
	nil)
	(t
	(princ "Repondez oui ou non ou ? (si vous ne savez pas)")
	(terpri)
	(questionner_et_conclure_prop p)))
)

(defun inferer_prop (p)	
	(let
	((regles_nouvelles (r_concluent_sur_prop p)))
	(inferer1 p regles_nouvelles))
)

(defun inferer1 (p lisregl)
	(princ "J'ai le choix entre les règles suivantes : ") (princ lisregl)
	(terpri)
 	(cond 
		( (null lisregl)  nil)
		(t
			; Si l'heurisitique ets activé, on en passe pas car lisregl mais la regle ayant le plus de premisses
			(if (verif_heurisitque 'config)
				(setq rc (heuristique_ARR lisregl))
				(setq rc (recuperer_regle_entiere (car lisregl) regles))
			)
			(princ "Je selectionne cette règle : ") (princ rc)
			(terpri)				
			(essaye_regle rc)
			(cond 
				((vraie_prop p) t)
		  		(t 
					(inferer1 p (cdr lisregl))
				)
			)
		)
	)
)


(defun applique_partie_droite (lp)
	(mapc 'marquer_vraie_prop lp)
	(mapc 'montre_deduction_prop lp)
)

(defun montre_deduction_prop (p)
	(cond 
		(md  
			(princ "J'arrive à en déduire : ")(princ p)
		)
	)
)


(defun essaye_regle (r)
	(cond
		(md 
		(princ "Je vais essayer cette regle : ")(princ r) (terpri) ))
		
	(cond (
		(appliquee r) nil)
		(t
			(let 
				(
					(v (etablir_partie_gauche (partie_gauche_regle r) ) )
				)		
				(cond 
					( (null v) nil) ; La partie gauche est fausse ? La regle echoue
					(t (applique_partie_droite (partie_droite_regle r)))
				)
				(marquer_appliquee r)
			)
		)
	)
)


(defun etablir_partie_gauche (liste_de_prop)
	(cond
		; Y a-t-il une prop deja connue fausse ?
		((some_classique 'faux_prop liste_de_prop) nil) ; Si oui, on s'arrette la et on retourne nil
		(t (etablir_pg1 liste_de_prop) ) ; Sinon a la suivante
	)
)


(defun etablir_pg1 (liste_de_prop)
	(cond
		((null liste_de_prop) t)
		((null (etablir_prop (car liste_de_prop))) nil)
		(t (etablir_pg1 (cdr liste_de_prop)))))

(defun probleme (p)
	(initialiser)
	(etablir_prop p)
	(presenter_resultats)
)

(defun etablir_prop (p)	
	(cond ( (recherchee_prop p)
				( cond 
					( (vraie_prop p) t)
					(t nil)
				)
			)
	      ( (terminal p) 
				(marquer_recherchee_prop p)
				(questionner_et_conclure_prop p)
			)
	      ( (demandable p)
			( cond
				( (questionner_et_conclure_prop p) t)
	      		( (equal reponse '?) (inferer_prop p ) )  
			)
	      )
	      (t 
			(marquer_recherchee_prop p)
			(inferer_prop p)
		)
	)
)