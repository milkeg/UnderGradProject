/*!
 * \file WSOLA.c
 *
 *
 * module qui modifie la "vitesse de lecture" de la structure WAVE
 *
 * \defgroup WSOLA WSOLA : modification de vitesse
 * Le module de modification de vitesse modifie une structure WAVE d'un fichier son .wav de telle sorte que la structure corresponde à un fichier son soit plus long, soit plus court.
 * L'algorithme utilisé est appelé WSOLA (Wave-form Similarity based Over-Lap Add). Cet algorithme créé une version modifiée (soit étendue soit rétrécie) de l'onde originale d'un son en concaténant en permanence des segments de temps court, ici d'une durée de 20 ms (soit N échantillons), extraits de l'onde originale.
 * Une explication détaille du fonctionnement de cet algorithme est disponible dans le fichier "documentation interne du code.pdf"
 *
 * \ingroup synthese
 */

#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include <math.h>

#include "Synthese_Vocale.h"

/*!
 * \brief ré-écriture de la structure WAVE du fichier .wav désiré
 *
 * fonction qui modifie les champs ChunkSize et Subchunk2Size et qui copie les échantillons modifiés (par la fonction wsola) dans le tableau d'échantillons de la structure WAVE.
 *
 * \param WAVE* output, structure WAVE du fichier .wav à modifier.
 * \param short *tab_sample, tableau d'échantillons à copier/écrire dans la structure.
 * \param dword nbr_sample, nombre d'échantillons du tableau.
 *
 * \ingroup WSOLA
 */
void write_structure(WAVE *output, short *tab_sample, dword nbr_sample) {

	/* calcul des SubchunkSizes:
	 *
	 * Subchunk1Size vaut toujours 16 pour un fichier PCM.
	 *
	 * Blockalign: nombre d'octects pour coder un échantillon.
	 * Subchunk2Size: nombre d'octects que représente tous les échantillons.
	 * on a donc: Subchunk2Size = nb samples * Blockalign
	 *
	 * d'après la formule: ChunkSize = 4 + (8 + SubChunk1Size) + (8 + SubChunk2Size).
	 */
	dword sbck1size = 16;
	dword sbck2size = nbr_sample * output->fmt.Blockalign;
	word cksize = 4 + (8 + sbck1size) + (8 + sbck2size);

	//affectation de valeurs calculés au champs correspondants de la structure de fichier wav output
	output->RIFF.ChunkSize = cksize;
	output->data.Subchunk2Size = sbck2size;

	/* ré-alocation de la taille du tableau data[] contenant les échantillons de la structure du fichier son
	 *
	 * Blockalign: nombre d'octects pour coder un sample
	 * Subchunk2Size: nombre d'octects que représente tous les échantillons.
	 * on a donc le nombre d'échantillons en faisant l'opération Subchunk2Size/Blockalign
	 * un échantillon est contenable dans un short donc chaque case doit être de la taille d'un short.
	 */
	output->data.data = realloc(output->data.data, (unsigned int)(output->data.Subchunk2Size / output->fmt.Blockalign) * sizeof(short));


	//remplissage du tableau d'échantillons data[] de la structure de fichier wav, à partir du tableau d'échantillons modifiés tab_sample[]
	int i = 0;
	for (i=0; i < nbr_sample ; i++) {
		output->data.data[i] = tab_sample[i];
	}
}

/*!
 * \brief application de l'algorithme WSOLA sur la structure WAVE
 *
 * cette fonction modifie la structre WAVE passé, cela en utilisant l'algorithme WSOLA,
 * suivant le coeficient de changement de vitesse, l'algorithme applique une augmentation ou une diminution du nombre d'échantillons
 * de la structure WAVE, ce qui se traduit par un élargissement ou rétrécissement du fichier son .wav (que représente la structure WAVE).
 *
 * \param float speed_factor, coeficient de changement de vitesse, > 1 : diminution, < 1 : augmentation.
 * \param WAVE *son, structure de fichier wav à modifier.
 *
 * \ingroup WSOLA
 */
void wsola (float speed_factor, WAVE *son) {

	dword nbr_sample;

	/* création d'un tableau pour contenir les échantillons
	 *
	 * dans le cas ou la somme des deltas(k) serait positive, on aurait un peu plus que (nb d'échantillons * coefficient de vitesse) échantillons
	 * nb d'échantillons total = Subchunk2Size / Blockalign, seulement ici on ne divise pas Subchunk2Size par Blockalign(qui vaut 2), donc
	 * on créé un tableau d'environ deux fois plus de case qu'il ne serait théoriquement nécessaire pour éviter quelconque risque de dépassement de plage mémoire
	 */
	short *tocopy;
	tocopy = malloc(((unsigned int)(( son->data.Subchunk2Size * speed_factor)+1)) * sizeof(short));
	if (tocopy == NULL) {
		printf("tocopy malloc problem...\n");
		exit(0);
	}

	const float t_window = 0.020; //20ms
	const float N = son->fmt.SampleRate * t_window; //nombre d'échantillons dans une fenêtre de taille N (de 20ms)

	//nombre total de fenêtre à parcourir dans le fichier
	const float data_length	= son->data.Subchunk2Size / N;

	const float t_segment = 0.010; //10ms
	const float L = son->fmt.SampleRate * t_segment; //nombre d'échantillons dans un segment de taille L (de 10ms)

	const int step = 64;

	//création et initialisation des variable de parcours de la boucles
	int k = 1;
	int n = 0;

	int delta = 0; //création et initialisation du delta k
	int previous_delta = 0; //création et initialisation du delta (k-1)
	int previous_delta_temp = 0; //création et initialisation du delta (k-1) temporaire (pour les boucles)

	//création et initialisation de "curseurs"
	int previous_position = 0;
	int current_position = 0;
	int desired_position = 0;

	//création et initialisation du coefficient de similitude (recalculé en permanence dans la boucle)
	float coef_similitude = 0;

	//création et initialisation (à la valeur max d'un float) du coefficient de similitude minimal
	float min = 3.40282e+038;

	//création d'un tableau de taille L (utilisé uktérieursement pour effectuer les calculs de fades)
	short temp[(int)L];

	//remplissage du le tableau d'échantillons avec les N premiers échantillons
	int i = 0;
	for (i=0; i<N; i++) {
		tocopy[i] = son->data.data[i]; //copie l'échantillon i dans le tableau tocopy à la position i
	}


	/* boucle de recherche du segment le plus adapté
	 * utilisant la cross-AMDF-coefficient pour calculer le coefficient de similitude
	 * pour chaque k, on cherche le delta qui correspond au segment dans la zone de k le plus similaire au segment qui
	 * suit le segment précédent copié [previous_position   previous_position + L]
	 */

	//parcours de toutes les fenêtres (totalité du fichier)
	while (k < data_length * speed_factor) {

		/* à chaque ré-iteration de la boucle while, il faut ré-initialsier quelques valeurs
		 *
		 * ré-initialisation du minimum à la valeur maximum d'un float
		 * ré-initialisation du coeficient de similitude à 0
		 * affectation du delta(k-1) par le delta(k-1) précédent (càd le delta(k-2))
		 */
		min = 3.40282e+038;
		coef_similitude = 0;
		previous_delta = previous_delta_temp;

		/* pour chaque delta, on va comparer le segment (1') [previous_position   previous_position+L]
		 * avec le segment (2) [current_position   current_position+L]
		 * cela pour trouver le segment (2) le plus similaire au segment (1)
		 */
		for (delta = -3*step; delta < 3*step; delta++) {

			//échantillon à la position (k-1)L/speed_factor  + delta(k-1) + L
			previous_position = (k - 1) * L / speed_factor + previous_delta + L;
			//échantillon à la position (k)L/speed_factor + delta(k)
			current_position = k * L / speed_factor + delta;

			/* pour chaque échantillon n (de 0 à L-1) des segments (1') et (2) on calcule le coefficient de similitude avec la formule:
			 * valeur absolue de la diférence de l'échantillon n du segment (1') et de l'échantillon n du segment (2)
			 */
			for (n=0; n<L; n++) {
				coef_similitude = abs (son->data.data[n + previous_position] - son->data.data[n + current_position]);
			}

			/* si le coefficient actuel est le minimum:
			 * on sauvegarde la position à laquelle le "curseur" est
			 * le minimum
			 */
			if (coef_similitude < min) { //si le coeficient minimum (cad similitude max)
				desired_position = current_position; //sauvegarde de la position à  laquelle on doit copier le data (les L samples)
				min = coef_similitude; //affectation du coef
				previous_delta_temp = delta; //affectation du delta qui deviendra le delta(k-1) du lors de la prochaine itteraion de k
			}
		}


		/* boucle d'ajout des segments trouvés au tableau d'échantillons tocopy[]
		 *
		 * chaque segments B sont ajoutés au tableau tocopy[] en effectuant l'over-lap add sur les L derniers échantillons de tocopy[]
		 * (cd. documentation interne du code.pdf)
		 */

		/* création et initialisation des indice de parcours de la boucle for à suivre
		 * la position désirée est affecté à la variable j (pour copier les échantillons de la structure WAVE à partir de position désirée)
		 */
		int j = desired_position;
		int x = 0;

		/* on applique un "cos+1" fade-out sur les L derniers échantillons de tocopy[] (segment précedent: [previous_position+L   previous_position+N]
		 * on applique un "cos+1" fade-in sur les L premiers échantillons du segment courant [desired_position   desired_position+L]
		 * on ajoute le deux segements dans tocopy[] (=> over-lap add)
		 */
		for (i= k*L; i< k*L + L; i++) {
			tocopy[i] = (short)(tocopy[i] * (( 1 + cos( (x * M_PI) / L ) ) / 2)); //"cos+1" fade-out
			temp[x] = son->data.data[j]; //copie des échantillons dans le tableau temporaire
			temp[x] = (short) (temp[x] * (1 - ( (1+cos( (x * M_PI) / L ) ) / 2 ) ) ); //"cos+1" fade-in dans le tableau temporaire
			tocopy[i] += temp[x]; //somme des échantillons (add)
			j++;
			x++;
		}

		//on copie les L derniers déchantillons dans tocopy[]
		for (i= k*L + L; i< k*L + N; i++) {
			tocopy[i] = son->data.data[j];
			j++;
		}

		k++;

	}

	/* sauvegarde de la position du dernier échantillon copié dans le tableau tocopy[]
	 * correspond au nombre d'échantillons à écrire dans la structure WAVE à modifier
	 */
	nbr_sample = i;

	/* le tableau d'échantillons tocopy[] maintenant rempli jusque nbr_sample, il faut reconstruire la structure WAVE modifiée.
	 * on appel donc la fonction write_structure pour modifier la structure WAVE passé par adresse.
	 */
	write_structure(son, tocopy, nbr_sample);

	//libération de l'espace mémoire aloué au tableau tocopy[]
	free(tocopy);
}
