/*!
 * \file interface_utilisateur.c
 *
 *
 * \defgroup interface Interface_utilsateur : module s'assurant une saisie propre des variables
 * Le module interface utilisateur consiste à saisir les variables et à s'assurer que la saisie de celles-ci est effectuée correctement.
 * Celui-ci demande d'entrer les trois variables : numéro, voix et vitesse.
 * \ingroup synthese
 */

#include<stdio.h>
#include<stdlib.h>
#include<string.h>

#include "interface_utilisateur.h"

#define NMAX 3

/*!
 * \brief fonction permettant la saisie d'une chaîne limitée à un caractère
 *
 * Si la chaîne est composé de plus d'un caractères, celui-ci est stocké dans une variable et le tampon d'entrée clavier stdin est vidé.
 *
 * \ingroup interface
 *
 * \return char*
 */
char *saisie() {
    char *s ;
    char buffer [NMAX] ;
    int ch;

    //saisie dans la chaîne buffer de maximum 2 caractères entrées (3ième = espace alloué pour le '\n')
    fgets (buffer, NMAX, stdin) ;
    /* si la chaîne contient plus d'un caractère ET si la chaîne n'est pas vide
     * on veut vider le tampon d'entrée clavier stdin
     */
    if ( (buffer[1] != '\n') && (buffer[0] != '\n') ) {
		s = (char *) malloc ( NMAX *sizeof(char) ) ;//allocation mémoire pour une chaine de NMAX=3 caractères
		if (!s) {
			   printf("probleme de memoire");
			   exit (0);
		}
		strcpy(s, buffer); //copie de la chaîne entrée dans la chaîne s

		//vidage du tampon d'entrée clavier stdin (pour ne pas avoir les caractères suivant le NMAX=3ième caractère dans le tampon stdin)
		while ((ch = getchar()) != '\n' && ch != EOF); //empty stdin
	} else {
		/* si la chaîne entrée (buffer) est composé d'un seul caractère
		 * on créé une chaine s de la taille 1, et on copie buffer dans s
		 */
		s = (char *) malloc ( 1 *sizeof(char) ) ;
		if (!s) {
			   printf("probleme de memoire");
			   exit (0);
		}
		strcpy(s, buffer); //copie de la chaîne entrée dans la chaîne s
	}

    //on retourne la chaîne s
    return s ;
}

/*!
 * \brief fonction permettant la saisie d'une chaîne limitée à 255 caractères
 *
 * Si la chaîne est composé de plus de 255 caractères, les 255 premiers caractères sont stockant dans une variable et le tampon d'entrée clavier stdin est vidé.
 *
 *\ingroup interface
 *
 *\return char*
 */
char *saisie_numero (int *error) {
    char *s ;
    char buffer [256] ;
    int ch;

    //saisie dans la chaîne buffer de maximum 256 caractères entrées (256ième = espace alloué pour le '\n')
    fgets (buffer, 256, stdin) ;
    /* si la chaine entrée est composé de plus de 255 caractères se traduit par:
     *
     * si la chaîne buffer est rempli (taille = 255) ET que dernier caractère n'est pas fin de saisie
     * (ce qui voudrait dire que la chaîne contient pil 255 caractères)
     * on veut prendre les 255 premiers caractères et vider le tampon d'entrée clavier stdin.
     */
	if ((strlen(buffer) == 255) && (buffer[255] != '\n')) {
		/* gestion d'avertissement:
		 *
		 * il y a plus de 255 caractères entrés pour le numéro; donc on affecte 10 à la variable error
		 * cela impliquera que (error/10) ne soit pas nul ce qui permettra un printf ultérieur de l'avertissement:
		 * "les caractères dépassant le 255ième ont étés supprimés"
		 */
		*error = 10;

		s = (char *) malloc ( (256) *sizeof(char) ); //allocation mémoire pour une chaîne de 256 caractères
		if (!s) {
			printf("probleme de memoire");
			exit (0);
		}
		strcpy(s, buffer); //copie de la chaîne entrée dans la chaîne s

		//vidage du tampon d'entrée clavier stdin (pour ne pas avoir les caractères suivant le 255ième dans le stdin)
		while ((ch = getchar()) != '\n' && ch != EOF);
	} else {
		/* si la chaîne entrée (buffer) est composé de moins de 255 caractères
		 * on créé une chaine s de la taille du nombre de caractères entrées, et on copie buffer dans s
		 */
		s = (char *) malloc ( (strlen(buffer)+1) * sizeof(char) ) ;
		if (!s) {
			printf("probleme de memoire");
			exit (0);
		}
		strcpy(s, buffer); //copie la chaîne entrée dans la chaîne s
	}

	//on retourne la chaîne s
    return s ;
}


/*!
 * \brief fonction permettant la saisie des variables numero, voix et vitesse
 *
 * Cette fonction s'assure que les saisies sont conformes aux attentes et met à jour une variable error (utile pour indiquer des erreurs à l'utilisateur)
 *
 *\ingroup interface
 *
 */
void interface_utilisateur(char *numero, int *voix, float * vitesse, int *error){

	int speed = 0;
	char *v, *s, *num;

	//saisie du numéro
	printf("numero : ");
	num = saisie_numero(error);
	//si la chaîne saisie num est vide, on relance la saisie et cela tant que la chaîne saisie n'est pas vide.
	if (num[0] == '\n') {
		do {
			printf("numero vide...\n");
			printf("numero : ");
			num = saisie_numero(error);
		} while (num[0] == '\n');
	}

	//on copie la chaine num saisie dans la chaine numero
	strcpy(numero, num);

	//on libère l'espace alloué à la chaine num
	free(num);

	//saisie voix
	printf("voix (0: féminine | 1: masculine) : ");
	v = saisie();
	/* si la chaine saisie v comporte plus d'un caractère !(s[1] == '\n')
	 * ET que ce caractère n'est ni '0' ni '1', on relance la saisie tant que les conditions ne sont pas vérifiées
	 */
	if ( ! ( ((v[0] == '0') || (v[0] == '1')) && (v[1] == '\n')) ) {
		do {
			printf("voix non reconnu, veuillez ré-entrer celle-ci\n");
			printf("voix (0: féminine | 1: masculine) : ");
			v = saisie();
		} while( ! ( ((v[0] == '0') || (v[0] == '1')) && (v[1] == '\n')) );
	}
	/* une fois la saisie réussi, on transforme le caractère saisie en entier
	 * en soustrayant la valeur ASCII du caarctère '0' et on affecte cette valeur à la variable voix
	 */
	*voix = v[0] - '0';

	//on libère l'espace alloué à la chaine v
	free(v);


	//saisie vitesse
	printf("vitesse (0: normale | 1: lente | 2: rapide) : ");
	s = saisie();
	/* si la chaine saisie s comporte plus d'un caractère !(s[1] == '\n')
	 * ET que ce caractère n'est ni '0' ni '1' ni '2', on relance la saisie tant que les conditions ne sont pas vérifiées
	 */
	if ( ! ( ((s[0] == '0') || (s[0] == '1') || (s[0] == '2')) && (s[1] == '\n')) ) {
		do {
			printf("vitesse non reconnu, veuillez ré-entrer celle-ci\n");
			printf("vitesse (0: normale | 1: lente | 2: rapide) : ");
			s = saisie();
		} while ( ! ( ((s[0] == '0') || (s[0] == '1') || (s[0] == '2')) && (s[1] == '\n')) );
	}
	/* une fois la saisie réussi, on transforme le caractère saisie en entier
	 * en soustrayant la valeur ASCII du caarctère '0' et on affecte cette valeur à la variable temporaire speed
	 */
	speed = s[0] - '0';

	//on libère l'espace alloué à la chaine s
	free(s);

	/* d'après les valeurs de speed (entier 0, 1 ou 2)
	 * on affecte les coefficients de changements de vitesse à la variable vitesse.
	 */
	switch(speed) {
		case 0: *vitesse = 1.0;
			break;
		case 1: *vitesse = 1.3;
			break;
		case 2: *vitesse = 0.7;
			break;
	}
}
