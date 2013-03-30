/**
 *\mainpage Synthese vocale de numeros telephoniques
 *Voici la documentation de la synthese de nueros telephoniques en langage             C. Il apporte une explication globale de la librairie. L'architecture de la librairie ainsi q'une description succincte de chaque modules figurent dans ce document.
Il est demande de livrer une librairie code en langage C permettant la synthese vocale de numero telephoniques ainsi qu'une application qui teste cette librairie. La librairie qu'il est demande de fournir doit comporter une fonction permettant la reconstruction vocale d'un numero telephonique.
 *
 *
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "SDL/SDL.h"

#include "interface_utilisateur.h"
#include "Synthese_Vocale.h"
#include "WAVE.h"
#include "Traitement_Chaine.h"
#include "Analyse_Syntaxique.h"
#include "Concat.h"
#include "WSOLA.h"
#include "Jouer.h"


void Synthese_Vocale(){

	char numero[256];
	int voix;
	float vitesse;
	int error = 0;

	interface_utilisateur(numero, &voix, &vitesse, &error);

	char *tab_numero[128]; //128 cases max
	int nb_cases_tab_numero;
	
	traitement_chaine(numero, tab_numero, &nb_cases_tab_numero, &error);

	char *tab_path[640]; // 640 max
	int nb_case_tab_path; // nombre de cases de ce tableau
	
	analyse_syntaxique(voix, tab_numero, nb_cases_tab_numero, &nb_case_tab_path, tab_path);
	
	WAVE sortie;
	
	concatenation(tab_path, nb_case_tab_path, &sortie);
	
	dword nb_samples = 0;
	char *tmp_nom;
	
	//création d'un fichier temporaire
	tmp_nom = tmpnam(NULL);
	
	FILE *tmp_fichier = fopen(tmp_nom, "wb");
	
	if (vitesse != 1.0) { // s'il y a modification de vitesse, besoin de WSOLA
		wsola(vitesse, &sortie);
		write_wavfile(sortie, tmp_fichier);
	} else {
		write_wavfile(sortie, tmp_fichier);
	}

	fclose(tmp_fichier);

	//************ AVERTISSEMENTS ******************
	if (error/10) {
		printf("\navertissement: les caractères dépassant le 255ième ont étés supprimés\n");
	}
	if (error%10) {
		printf("\navertissement: certains caractères entrés ne sont pas reconnus, ils ont étés supprimés\n");
	}

	Jouer_Son(tmp_nom);
	
	printf("\nsynthèse correctement effectuée\n");

}
