; TEMPLATES

(deftemplate joueur	; Identifiant du joueur	(slot id (type INTEGER) (allowed-integers 1 2) )	; Nom du joueur	(slot nom (type SYMBOL) (default ordinateur))	; Les graines qu'il garde	(slot sesGains (type INTEGER) (range 0 48))	; Les graines qu'il a dans sa partie du plateau	(slot sesGraines (type INTEGER) (range 0 48))	; Ses trous	(multislot sesTrous (type INTEGER) (range 1 12))	; A son tour de joue	(slot joue (type SYMBOL) (allowed-symbols non oui))		; Son premier trou	(slot init (type INTEGER) ))(deftemplate jeu	; Graines se trouvant sur le plateau		(slot nbGraines (type INTEGER) (range 2 48))	; Trous du plateau	(multislot lesTrous (type INTEGER) (range 1 12))	; Numéro du tour actuel	(slot nbTours (type INTEGER) (default 0)))(deftemplate trou	; ID du trou	(slot idTrou (type INTEGER) (range 1 12))		; Nombre de graines dans le trou	(slot contenu (type INTEGER) (range 0 48)))

; FAIT
   
; Definit l'étape initiale   
(deffacts bf
	(etape init)
)


; VARIABLES GLOBALES

; Variable qui contient 
; la liste des trous que le joueur doit jouer pour nourrir l'adversaire
; recoupé avec les trous non-vides
(defglobal ?*listeAJouer* = (create$))

; Variable globale qui contient le trou que le joueur veut joueur
(defglobal ?*AJouer* = 0)

; Variable globale qui contient le nombre de graines contenus dans le trou que le joueur veut jouer
(defglobal ?*contenuAJouer* = 0)

; Contient la liste des trous qui peuvent être joués.
(defglobal ?*coupPossible* = (create$))

; Permet de savoir on est a qulle trou dans la repartition ou recupératin de graines
(defglobal ?*trouActuel* = 0)

; Permet de savoir le nombre de graines restantes a distribuer
(defglobal ?*grainesRestantes* = 0)


; REGLES
; Gestion JEUX

; Permet de mettre a jour la BF
(defrule debutDePartie
	(declare (salience 10))
	(not (etape ?a))
=>
	(reset))
; Initialisation de la partie :; Création du plateau; Création de 12 trous et repartir les graines à raison de 4 graines par trou; Creéation joueur, attribution trou, chaque joueur a 24 graines; Recupérer le nom du joueur(defrule initialisationJeu
	?e <- (etape init)	=>	(loop-for-count (?i 1 12) do		( assert (trou (idTrou ?i) (contenu 4)) )	)	(assert (jeu ( nbGraines 48) (lesTrous 1 2 3 4 5 6 7 8 9 10 11 12) (nbTours 0) ) )	(printout t "Nom du joueur affrontant l'ordinateur ? ")	(assert (joueur (id 1) (nom (read)) (sesGains 0) (sesGraines 24) (sesTrous 1 2 3 4 5 6) (joue oui) (init 1) ) )	(assert (joueur (id 2) (sesGains 0) (sesGraines 24) (sesTrous 7 8 9 10 11 12) (joue non) (init 7) ) )
	
	(retract ?e)
	(assert (etape Affichage))
	
	)

; Regle affichant le plateau
(defrule affichagePlateau

	?e <- (etape Affichage)

	(trou (idTrou 1) (contenu ?c1))
	(trou (idTrou 2) (contenu ?c2))
	(trou (idTrou 3) (contenu ?c3))
	(trou (idTrou 4) (contenu ?c4))
	(trou (idTrou 5) (contenu ?c5))
	(trou (idTrou 6) (contenu ?c6))
	(trou (idTrou 7) (contenu ?c7))
	(trou (idTrou 8) (contenu ?c8))
	(trou (idTrou 9) (contenu ?c9))
	(trou (idTrou 10) (contenu ?c10))
	(trou (idTrou 11) (contenu ?c11))
	(trou (idTrou 12) (contenu ?c12))
	
	(joueur (id 1) (nom ?nom1) (sesGains ?gain1) )
	(joueur (id 2) (nom ?nom2) (sesGains ?gain2) )

=>

(printout t "Trou" tab tab tab "1" tab "2" tab "3" tab "4" tab "5 " tab "6" crlf)
(printout t "Graines J1" tab ?c1 tab ?c2 tab ?c3 tab ?c4 tab ?c5 tab ?c6 crlf)
(printout t "Graines J2" tab ?c12 tab ?c11 tab ?c10 tab ?c9 tab ?c8 tab ?c7 crlf)
(printout t "Trou" tab tab tab "12" tab "11" tab "10" tab "9" tab "8" tab "7" crlf)

(printout t "Gains : " tab ?nom1" : " ?gain1 " - " ?gain2 " : " ?nom2 crlf)

(retract ?e)
(assert (etape Jouer))

)

; Cas de victoire > 25 Graines
(defrule plusDe25Gains
	
	(declare (salience 3))
	?e <- (etape entreDeuxTour)
	?joueur <- (joueur (id ?idJ) (sesGains ?gains&:(> ?gains 25)) )

=>
	(retract ?e)
	(assert (etape gagne))
)

; Cas de victoire <  6 graines sur le plateau
(defrule moinsDe6Graines
	
	(declare (salience 2))
	?e <- (etape entreDeuxTour)
	?jeu <- (jeu (nbGraines ?graines&:(<= ?graines 6)) )
=>
	
	(retract ?e)
	(assert (etape gagne))
)

; Ajout d'un tour
(defrule ajoutDUnTour
	
	(declare (salience 1))
	?e <- (etape entreDeuxTour)
	?jeu <- (jeu (nbTours ?nbTours))
=>
	(modify ?jeu (nbTours (+ ?nbTours 1)) )

	(retract ?e)
	(assert (etape entreDeuxTour1))
)

; Definti le prochaien joueur a jouer;Vide les variables globales qui vont etre réutilisées
(defrule inversionJoue

	?e <- (etape entreDeuxTour1)

?joueur1 <- (joueur (id 1) (nom ?nom1) (sesGains ?gains1) (sesGraines ?graines1) (sesTrous $?trous1) (joue ?joue1) (init ?init1))
?joueur2 <- (joueur (id 2) (nom ?nom2) (sesGains ?gains2) (sesGraines ?graines2) (sesTrous $?trous2) (joue ?joue2) (init ?init2))
=>

(retract ?joueur1)
(retract ?joueur2)

(assert (joueur (id 1) (nom ?nom1) (sesGains ?gains1) (sesGraines ?graines1) (sesTrous $?trous1) (joue ?joue2) (init ?init1)))
(assert (joueur (id 2) (nom ?nom2) (sesGains ?gains2) (sesGraines ?graines2) (sesTrous $?trous2) (joue ?joue1) (init ?init2)))

(retract ?e)
(assert (etape Affichage))

; Vide les variables globales

(bind ?*listeAJouer* (create$))
(bind ?*AJouer* 0)
(bind ?*contenuAJouer*  0)
(bind ?*coupPossible* (create$))
(bind ?*trouActuel* 0)
(bind ?*grainesRestantes* 0)

	
)

; Regle qui permet de gerer la fin de partie
(defrule finDePartie

?e <- (etape gagne)

?joueur1 <- (joueur (id 1) (nom ?nom1) (sesGains ?gains1))
?joueur2 <- (joueur (id 2) (nom ?nom2) (sesGains ?gains2))

=>

(printout t "Fin de partie : Le score est de " ?nom1 " : " ?gains1 " à " ?gains2 " : " ?nom2 " ! " crlf )
(printout t "Appuyez sur entrée pour rejouer" clrf)

(while (read) do
	(clear)
	(reset)
)

)



; Règle qui regarde si l'ordinateur n'a plus de graines dans son camp(defrule jeuPourJoueur
		?j1<- (joueur (id 1) (sesGraines ?gr1) (nom ?n1)(joue oui))	?j2<-(joueur (id 2) (sesGraines ?gr2) (nom ?n2) (joue non))	?e<-(etape Jouer)=>	; si l'adversaire n'a plus de graines dans aucun des ses trous	(if (= ?gr2 0)	then 		;on passe a l'étape suivante dans le test   		(printout t "aucunes graine dans le camps du joueur " ?n2)		(assert (etape trouAJouerPourJoueur) )		(retract ?e)	else		;passage a l'étape joue		(assert (etape nonVidePourJoueur))
		(bind ?*listeAJouer* (create$ 1 2 3 4 5 6) )		(retract ?e)	))
; Règle qui regarde si joueur1 n'a plus de graines dans son camp(defrule jeuPourOrdinateur	?j1<- (joueur (id 2) (sesGraines ?gr1) (nom ?n1) (joue oui))	?j2<-(joueur (id 1) (sesGraines ?gr2) (nom ?n2) (joue non))	?e <- (etape Jouer)=>	; si l'adversaire n'a plus de graines dans aucun des ses trous	(if (= ?gr2 0)	then 		;on passe a l'étape suivante dans le test   		(printout t "aucunes graine dans le camps du joueur " ?n2)		(assert (etape trouAJouerPourOrdinateur) )		(retract ?e)	else		;passage a l'étape joue		(assert (etape nonVidePourOrdinateur))
		(bind ?*listeAJouer* (create$ 7 8 9 10 11 12) )		(retract ?e)	))
; Règle qui stocke dans trouAJouer les trous qui peuvent nourrir l'adversaire 
(defrule coupObligatoiresPourJoueur
  (trou (idTrou 1) (contenu ?c1))
  (trou (idTrou 2) (contenu ?c2))
  (trou (idTrou 3) (contenu ?c3))
  (trou (idTrou 4) (contenu ?c4))
  (trou (idTrou 5) (contenu ?c5))
  (trou (idTrou 6) (contenu ?c6))
  ?e<- (etape trouAJouerPourJoueur)
  (joueur (id 1) (nom ?n))
=>
  ; si le coup  peut nourrir l'adversaire on le met dans listeAJouer
  (if (> ?c1 5) 
  then 
   (bind ?*listeAJouer* (insert$ ?*listeAJouer* (+ (length$ ?*listeAJouer*) 1) 1))
  )
  (if (> ?c2 4)    
  then 
   (bind ?*listeAJouer* (insert$ ?*listeAJouer* (+ (length$ ?*listeAJouer*) 1) 2))
  )
  (if (> ?c3 3) 
  then 
   (bind ?*listeAJouer* (insert$ ?*listeAJouer* (+ (length$ ?*listeAJouer*) 1) 3))
  )
  (if (> ?c4 2) 
  then 
   (bind ?*listeAJouer* (insert$ ?*listeAJouer* (+ (length$ ?*listeAJouer*) 1) 4))
  )
  (if (> ?c5 1) 
  then 
     (bind ?*listeAJouer* (insert$ ?*listeAJouer* (+ (length$ ?*listeAJouer*) 1) 5))
  )
  (if (> ?c6 0) 
  then 
   (bind ?*listeAJouer* (insert$ ?*listeAJouer* (+ (length$ ?*listeAJouer*) 1) 6 ))
  )

  ;si la liste est vide le joueur a gagne
  (bind ?var (- (length$ ?*listeAJouer*) 1))
  (if (= ?var 0)
  then 
   (assert (etape gagne))
   (retract ?e)
  else 
   ;sinon on passée a l'etape suivante
     (assert (etape nonVidePourJoueur))
   (retract ?e)
  )
   
)


; Règle qui stocke dans trouAJouer les trous qui peuvent nourrir l'adversaire 
(defrule coupObligatoiresPourOrdinateur
  (trou (idTrou 7) (contenu ?c1))
  (trou (idTrou 8) (contenu ?c2))
  (trou (idTrou 9) (contenu ?c3))
  (trou (idTrou 10) (contenu ?c4))
  (trou (idTrou 11) (contenu ?c5))
  (trou (idTrou 12) (contenu ?c6))
  ?e<- (etape trouAJouerPourOrdinateur)
  (joueur (id 2) (nom ?n) (joue non))
=>
  ; si le coup  peut nourrir l'adversaire on le met dans listeAJouer
  (if (> ?c1 5) 
  then 
   (bind ?*listeAJouer* (insert$ ?*listeAJouer* (+ (length$ ?*listeAJouer*) 1) 1))
  )
  (if (> ?c2 4)    
  then 
   (bind ?*listeAJouer* (insert$ ?*listeAJouer* (+ (length$ ?*listeAJouer*) 1) 2))
  )
  (if (> ?c3 3) 
  then 
   (bind ?*listeAJouer* (insert$ ?*listeAJouer* (+ (length$ ?*listeAJouer*) 1) 3))
  )
  (if (> ?c4 2) 
  then 
   (bind ?*listeAJouer* (insert$ ?*listeAJouer* (+ (length$ ?*listeAJouer*) 1) 4))
  )
  (if (> ?c5 1) 
  then 
     (bind ?*listeAJouer* (insert$ ?*listeAJouer* (+ (length$ ?*listeAJouer*) 1) 5))
  )
  (if (> ?c6 0) 
  then 
   (bind ?*listeAJouer* (insert$ ?*listeAJouer* (+ (length$ ?*listeAJouer*) 1) 6 ))
  )

  ;si la liste est vide le joueur a gagne
  (bind ?var (- (length$ ?*listeAJouer*)1))
  (if (= ?var 0)
  then 
   (assert (étape gagne))
   (retract ?e)
  else 
   ;sinon on passée a l'etape suivante
     (assert (etape nonVidePourOrdinateur))
   (retract ?e)
  )
)


; Verifie que les trous sont non vides pour le Joueur1
(defrule trouNonVidePourJoueur

	?e <- (etape nonVidePourJoueur)
	?trou1 <- (trou (idTrou 1) (contenu ?g1) )
	?trou2 <- (trou (idTrou 2) (contenu ?g2) )
	?trou3 <- (trou (idTrou 3) (contenu ?g3) )
	?trou4 <- (trou (idTrou 4) (contenu ?g4) )
	?trou5 <- (trou (idTrou 5) (contenu ?g5) )
	?trou6 <- (trou (idTrou 6) (contenu ?g6) )

=>

(printout t "Trou nourrissant l'adversaire : " ?*listeAJouer* crlf)

(
	if (= ?g1 0)
	then 
		(bind ?*listeAJouer* (delete-member$ ?*listeAJouer* 1))
)
(
	if (= ?g2 0)
	then 
		(bind ?*listeAJouer* (delete-member$ ?*listeAJouer* 2))
)
(
	if (= ?g3 0)
	then 
		(bind ?*listeAJouer* (delete-member$ ?*listeAJouer* 3))
)
(
	if (= ?g4 0)
	then 
		(bind ?*listeAJouer* (delete-member$ ?*listeAJouer* 4))
)
(
	if (= ?g5 0)
	then 
		(bind ?*listeAJouer* (delete-member$ ?*listeAJouer* 5))
)
(
	if (= ?g6 0)
	then 
		(bind ?*listeAJouer* (delete-member$ ?*listeAJouer* 6))
)

	(retract ?e)
	(assert (etape choixTrouPourJoueur))
)


; Verifie que les trous sont non vides pour l'ordinateur
(defrule trouNonVidePourOrdinateur

	?e <- (etape nonVidePourOrdinateur)
	?trou1 <- (trou (idTrou 7) (contenu ?g1) )
	?trou2 <- (trou (idTrou 8) (contenu ?g2) )
	?trou3 <- (trou (idTrou 9) (contenu ?g3) )
	?trou4 <- (trou (idTrou 10) (contenu ?g4) )
	?trou5 <- (trou (idTrou 11) (contenu ?g5) )
	?trou6 <- (trou (idTrou 12) (contenu ?g6) )

=>
(
	if (= ?g1 0)
	then 
		(bind ?*listeAJouer* (delete-member$ ?*listeAJouer* 7))
)
(
	if (= ?g2 0)
	then 
		(bind ?*listeAJouer* (delete-member$ ?*listeAJouer* 8))
)
(
	if (= ?g3 0)
	then 
		(bind ?*listeAJouer* (delete-member$ ?*listeAJouer* 9))
)
(
	if (= ?g4 0)
	then 
		(bind ?*listeAJouer* (delete-member$ ?*listeAJouer* 10))
)
(
	if (= ?g5 0)
	then 
		(bind ?*listeAJouer* (delete-member$ ?*listeAJouer* 11))
)
(
	if (= ?g6 0)
	then 
		(bind ?*listeAJouer* (delete-member$ ?*listeAJouer* 12))
)

	(retract ?e)
	(assert (etape choixTrouPourOrdinateur))
)


; Selection du trou pour le joueur
(defrule choixTrouPourJoueur

?e <- (etape choixTrouPourJoueur)

=>

(printout t "Veuillez choisir un trou parmis : " ?*listeAJouer* crlf)
(bind ?trouChoisi (read))

(while (not(member$ ?trouChoisi ?*listeAJouer*) ) do
	(printout t "Vous n'avez pas respecté les règles !" crlf "Choississez un trou dans la liste : " ?*listeAJouer* crlf)
	(bind ?trouChoisi (read))
)

(bind ?*AJouer* ?trouChoisi)

(assert (trouAJouer ?trouChoisi))
(retract ?e)
(assert (etape trouChoisiPourJoueur))
) 

; Permet à l'ordinateur de choisir son trou
(defrule choixTrouPourOrdinateur

?e <- (etape choixTrouPourOrdinateur)
?jeu <- (jeu (nbTours ?tour))

?trou1 <- (trou (idTrou 7) (contenu ?g1) )
?trou2 <- (trou (idTrou 8) (contenu ?g2) )
?trou3 <- (trou (idTrou 9) (contenu ?g3) )
?trou4 <- (trou (idTrou 10) (contenu ?g4) )
?trou5 <- (trou (idTrou 11) (contenu ?g5) )
?trou6 <- (trou (idTrou 12) (contenu ?g6) )

=>

; Le meilleur premier coup est toujours de choisir le dernier trou
(if (or (eq ?tour 0) (eq ?tour 1)) then
	(bind ?*AJouer* 12)
	(assert (trouAJouer 12))
else	 
	(bind ?*AJouer* 0)
	( while (not(member$ ?*AJouer* ?*listeAJouer*)) do
		(bind ?*AJouer* (random 7 12))
	)
	
	(assert (trouAJouer ?*AJouer*))

)

(retract ?e)
(assert (etape trouChoisiPourOrdinateur))
) 


; Vide le trou que le joueur a selectionné et diminue le nombre de graines qu'il possède
(defrule trouChoisiPourJoueur
	
	?e <- ( etape trouChoisiPourJoueur)
	; On recupère le trouAJouer
	?trouAJouer <- (trouAJouer ?idT)
	; On recupère le nombre de graines du trou et on le passe a 0
	?trou <- (trou (idTrou ?idT) (contenu ?nbGraines))

	; On retire les nombre de graines du trou a sesGraines du joueur qui possède le trou initial
	?j <- (joueur (id 1) (sesGraines ?grainesJ) )

=>

	(bind ?*trouActuel* ?*AJouer*)
	(bind ?*grainesRestantes* ?nbGraines)
	(bind ?*contenuAJouer* ?nbGraines)
	
	; On recupère le nombre de graines du trou et on le passe a 0
	(modify ?trou (contenu 0) )
	; On retire les nombre de graines du trou a sesGraines du joueur qui possède le trou initial
	(modify ?j (sesGraines (- ?grainesJ ?nbGraines) ))
	
	;On passe à l'étape de repartition des graines
	(retract ?trouAJouer)
	(if (= ?idT 1) then
		(bind ?idT 12) 
	else
		(bind ?idT (- ?idT 1))
	)
	(assert (trouAJouer ?idT))
	(retract ?e)
	(assert (etape repartirGrainePourJoueur))
	
)

; Vide le trou que l'ordinateur a selectionné et diminue le nombre de graines qu'il possède
(defrule trouChoisiPourOrdinateur
	
	?e <- ( etape trouChoisiPourOrdinateur)
	; On recupère le trouAJouer
	?trouAJouer <- (trouAJouer ?idT)
	; On recupère le nombre de graines du trou et on le passe a 0
	?trou <- (trou (idTrou ?idT) (contenu ?nbGraines))

	; On retire les nombre de graines du trou a sesGraines du joueur qui possède le trou initial
	?j <- (joueur (id 2) (sesGraines ?grainesJ) )

=>

	(bind ?*trouActuel* ?*AJouer*)
	(bind ?*grainesRestantes* ?nbGraines)
	(bind ?*contenuAJouer* ?nbGraines)
	
	; On recupère le nombre de graines du trou et on le passe a 0
	(modify ?trou (contenu 0) )
	; On retire les nombre de graines du trou a sesGraines du joueur qui possède le trou initial
	(modify ?j (sesGraines (- ?grainesJ ?nbGraines) ))
	
	;On passe à l'étape de repartition des graines
	(retract ?trouAJouer)
	(if (= ?idT 1) then
		(bind ?idT 12) 
	else
		(bind ?idT (- ?idT 1))
	)
	(assert (trouAJouer ?idT))
	(retract ?e)
	(assert (etape repartirGrainePourOrdinateur))
)


; Repartie les graines pour le joueur dans le sens inverse des aiguilles d'une montre
(defrule repartirGrainesPourJoueur

	?e <- (etape repartirGrainePourJoueur)
	
	; On recupère le trouAJouer
	?trouAJouer <- (trouAJouer ?idT)	
	
	; On recupère le nombre de grainez du i-ème trou
	?trou <- (trou (idTrou ?idT) (contenu ?grainesT) )
	
	; On recupère le joueur possèdant le trou
	?joueur <- (joueur (id ?id) (nom ?nom) (sesGains ?gains) (sesGraines ?sesGraines) (sesTrous $?sesTrous&:(member$ ?idT $?sesTrous)) (joue ?joue) (init ?init))

	

=>

	; Verifie si ce n'est pas le trou de départ
	(if (neq ?*AJouer* ?idT) then
		; On depose une graine dans le trou suivant et on ajoute une graine a sesGraines du joueur possèdant le trou, on diminue ses graiens du joueur actuel de 1 
		
		(retract ?trou)
		(assert (trou (idTrou ?idT) (contenu (+ ?grainesT 1))))
		
		(retract ?joueur)
		(assert (joueur (id ?id) (nom ?nom) (sesGains ?gains) (sesGraines (+ ?sesGraines 1)) (sesTrous $?sesTrous) (joue ?joue) (init ?init)) )

	)
	
	;On réduit le nombre de graine de 1 de la varglobale ?*graineRestantes*
	(bind ?*grainesRestantes* (- ?*grainesRestantes* 1))

	
	(if (= ?*grainesRestantes* 0) then
		;Changement d'étape
		(retract ?e)
		(assert (etape testAssechePourJoueur))
	else 
		; Calcul du trou precedent		
		(if (= ?idT 1) then
			(bind ?idT 12) 
		else
			(bind ?idT (- ?idT 1))
		)
		
		(retract ?trouAJouer)
		(assert (trouAJouer ?idT))
		(bind ?*trouActuel* ?idT)
	)
)

; Repartie les graines pour l'ordinateur dans le sens inverse des aiguilles d'une montre
(defrule repartirGrainesPourOrdinateur
	
	?e <- (etape repartirGrainePourOrdinateur)
	
	; On recupère le trouAJouer
	?trouAJouer <- (trouAJouer ?idT)	
	
	; On recupère le nombre de grainez du i-ème trou
	?trou <- (trou (idTrou ?idT) (contenu ?grainesT) )
	
	; On recupère le joueur possèdant le trou
	?joueur <- (joueur (id ?id) (nom ?nom) (sesGains ?gains) (sesGraines ?sesGraines) (sesTrous $?sesTrous&:(member$ ?idT $?sesTrous)) (joue ?joue) (init ?init))

=>

	; Verifie si ce n'est pas le trou de départ
	(if (neq ?*AJouer* ?idT) then
		; On depose une graine dans le trou suivant et on ajoute une graine a sesGraines du joueur possèdant le trou, on diminue ses graiens du joueur actuel de 1 
		
		(retract ?trou)
		(assert (trou (idTrou ?idT) (contenu (+ ?grainesT 1))))

		(retract ?joueur)
		(assert (joueur (id ?id) (nom ?nom) (sesGains ?gains) (sesGraines (+ ?sesGraines 1)) (sesTrous $?sesTrous) (joue ?joue) (init ?init)) )

	)
		
	
	;On réduit le nombre de graine de 1 de la varglobale ?*graineRestantes*
	(bind ?*grainesRestantes* (- ?*grainesRestantes* 1))

	
	(if (= ?*grainesRestantes* 0) then
		;Changement d'étape
		(retract ?e)
		(assert (etape testAssechePourJoueur))
	else 
		; Calcul du trou precedent		
		(if (= ?idT 1) then
			(bind ?idT 12) 
		else
			(bind ?idT (- ?idT 1))
		)
		
		(retract ?trouAJouer)
		(assert (trouAJouer ?idT))
		(bind ?*trouActuel* ?idT)
	)
)


; Règle qui vérifie si le coup assèche ou non l'adversaire(defrule assechePourJoueur
   (trou (idTrou 7) (contenu ?c1))   (trou (idTrou 8) (contenu ?c2))   (trou (idTrou 9) (contenu ?c3))   (trou (idTrou 10) (contenu ?c4))   (trou (idTrou 11) (contenu ?c5))   (trou (idTrou 12) (contenu ?c6))   ?e<- (etape testAssechePourJoueur)=>   ;si chacun des trous contient maintenant 2 ou 3 graines     (if (and (or (= ?c1 2) (= ?c1 3)) (or (= ?c3 2) (= ?c3 3)) (or (= ?c4 2) (= ?c4 3)) (or (= ?c5 2) (= ?c5 3)) (or (= ?c6 2) (= ?c6 3)))    then	
	(printout t "1")
		;s'il y a moins de 12 graines dans le trou a jouer	(if (< ?*contenuAJouer* 12)	then
		(printout t "2")
		;si la derniËre graine est tombée dans le douxieme trou		(
		if (=(+ ?*AJouer* ?*contenuAJouer*)12)		then	
			(printout t "3")
			(printout t "votre coup va assecher l'adversaire")			;passage a l'Ètape coup sans rÈcupÈration			(assert (etape entreDeuxTour))			(retract ?e)		else
			;rÈpartition normale				(printout t "coup normal")			; passage a l'Ètape coup normal			(assert (etape coupNormalPourJoueur))			(retract ?e)
		)	else
		(printout t "4")
		(bind ?sol (div ?*contenuAJouer* 12))		(switch ?sol		(case 1 then (bind ?arrive (+(-(+ ?*contenuAJouer* ?*AJouer*) 12) 1)))		(case 2 then (bind ?arrive (+(-(+ ?*contenuAJouer* ?*AJouer*)24)2)))		(case 3 then (bind ?arrive (+(-(+ ?*contenuAJouer* ?*AJouer*)36)3)))		(case 4 then (bind ?arrive (+(-(+ ?*contenuAJouer* ?*AJouer*)48)4)))		)		(if (= ?arrive 12)		then 
			(printout t "5")			(printout t "votre coup va assecher l'adversaire")			;passage a l'Ètape coup sans rÈcupÈration			(assert (etape entreDeuxTour))			(retract ?e)		else
			(printout t "6")
		)	)    else	 ;rÈpartition normale		(printout t "coup normal")	; passage a l'Ètape coup normal	(assert (etape coupNormalPourJoueur))	(retract ?e)    ))
; Règle qui vérifie si le coup assèche ou non l'adversaire(defrule assechePourOrdi   (trou (idTrou 1) (contenu ?c1))   (trou (idTrou 2) (contenu ?c2))   (trou (idTrou 3) (contenu ?c3))   (trou (idTrou 4) (contenu ?c4))   (trou (idTrou 5) (contenu ?c5))   (trou (idTrou 6) (contenu ?c6))   ?e<- (etape testAssechePourOrdinateur)=>   ;si chacun des trous contient maintenant 2 ou 3 graines     (if (and (or (= ?c1 2) (= ?c1 3)) (or (= ?c3 2) (= ?c3 3)) (or (= ?c4 2) (= ?c4 3)) (or (= ?c5 2) (= ?c5 3)) (or (= ?c6 2) (= ?c6 3)))    then		;s'il y a moins de 12 graines dans le trou a jouer	(if (< ?*contenuAJouer* 12)	then		;si la derniËre graine est tombée dans le douxieme trou		(if (=(+ ?*AJouer* ?*contenuAJouer*)6)		then				(printout t "votre coup va assecher l'adversaire")			;passage a l'Ètape coup sans rÈcupÈration			(assert (etape entreDeuxTour))			(retract ?e)		else
			;rÈpartition normale				(printout t "coup normal")			; passage a l'Ètape coup normal			(assert (etape coupNormalPourJoueur))			(retract ?e)
		)	else		(bind ?sol (div ?*contenuAJouer* 12))		(switch ?sol		(case 1 then (bind ?arrive (+(-(+ ?*contenuAJouer* ?*AJouer*) 12) 1)))		(case 2 then (bind ?arrive (+(-(+ ?*contenuAJouer* ?*AJouer*)24)2)))		(case 3 then (bind ?arrive (+(-(+ ?*contenuAJouer* ?*AJouer*)36)3)))		(case 4 then (bind ?arrive (+(-(+ ?*contenuAJouer* ?*AJouer*)48)4)))		)		(if (= ?arrive 6)		then 			(printout t "votre coup va assecher l'adversaire")			;passage a l'Ètape coup sans rÈcupÈration			(assert (etape entreDeuxTour))			(retract ?e)		)	)    else	 ;rÈpartition normale		(printout t "coup normal")	; passage a l'Ètape coup normal	(assert (etape coupNormalPourOrdinateur))	(retract ?e)    ))

; Règle qu irecupère les graines pour le joueur
(defrule recupererGrainePourJoueur

	
	?e <- (etape coupNormalPourJoueur)
	?joueur1 <- (joueur (id 1) (sesGains ?gains) )
	?joueur2 <- (joueur (id 2) (sesGraines ?grainesAdv) )

	?trouAJouer <- (trouAJouer ?idTrou)
	?trou <- (trou (idTrou ?idTrou) (contenu ?graines) )

=>

	; Si le trou appartient à l'adversaire et si le trou possède 2 ou 3 graines
	(if (and (and (>= ?idTrou 7) (<= ?idTrou 12)) (or (eq ?graines 2) (eq ?graines 3)) ) then
			;Alors on vide le trou
			(modify ?trou (contenu 0))
			; On ajoute le contenu du trou à sesGains du joueur 1
			(modify ?joueur1 (sesGains (+ ?gains ?graines)) )
			; On diminue le nombre de sesGraines de l'adversaire
			(modify ?joueur2 (sesGraines (- ?grainesAdv ?graines)) )
			
			(bind ?idTrou (mod (+ ?idTrou 1) 12) )
			(bind ?*trouActuel* ?idTrou )
	else 
		;Modifier etape : on passe dans entreDeuxJoueur
		(retract ?e)
		(assert (etape entreDeuxTour))

		
	)	
)

; Règle qu irecupère les graines pour l'ordinateur
(defrule recupererGrainePourOrdinateur

	?e <- (etape coupNormalPourOrdinateur)
	?joueur1 <- (joueur (id 2) (sesGains ?gains) )
	?joueur2 <- (joueur (id 1) (sesGraines ?grainesAdv) )
	?trou <- (trou (idTrou ?idTrou&:(eq ?idTrou ?*trouActuel*)) (contenu ?graines) )

=>

	; Si le trou appartient à l'adversaire et si le trou possède 2 ou 3 graines
	(if (and (and (>= ?idTrou 1) (<= ?idTrou 6)) (or (eq ?graines 2) (eq ?graines 3)) ) then
			;Alors on vide le trou
			(modify ?trou (contenu 0))
			; On ajoute le contenu du trou à sesGains du joueur 1
			(modify ?joueur1 (sesGains (+ ?gains ?graines)) )
			; On diminue le nombre de sesGraines de l'adversaire
			(modify ?joueur2 (sesGraines (- ?grainesAdv ?graines)) )
			
			(bind ?idTrou (mod (+ ?idTrou 1) 12) )
			(bind ?*trouActuel* ?idTrou )
	else 
		;Modifier etape : on passe dans entreDeuxJoueur
		(retract ?e)
		(assert (etape entreDeuxTour))

	)	
)


