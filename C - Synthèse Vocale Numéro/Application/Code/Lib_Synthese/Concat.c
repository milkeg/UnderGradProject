#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#include "Synthese_Vocale.h"
#include "Concat.h"
#include "WAVE.h"

/*!
 * \file Concatetion.c
 *
 * \author Michael GOLETTO
 *
 * \brief Effectue la concaténation des différents fichiers.
 *
 *\defgroup concat Concatenation 
 * Module qui charge les différents fichiers sons à l'aide de la fonction store(), puis effectue une fade.
 * Enfin, il les concaténe et renvoie une srcutre de fichier WAVE.
 */

/*!
 * \fn void create_structure(WAVE file_struct, short *tab_sample, dword nbr_sample, WAVE *output)
 *
 * \param file_struct Structure d'un fichier WAVE, il va servir de base pour remplir la nouvelle structure.
 * \param tab_sample Tableau contenant les données "data" du fichier WAVE à écrire.
 * \param nbr_sample Nombre d'échantillons présent dans tab_sample.
 * \param output Structure qui va être remplie avec les données des différents fichiers.
 *
 * \brief Remplie une structure à partir d'une structure d'un fichier WAVE déja ouvert.
 *
 * \ingroup concat 
 */
void create_structure(WAVE file_struct, short *tab_sample, dword nbr_sample, WAVE *output) { //en entrée le tab de struct et une struct qui sera utilisé dans write_wave

    // subck2size correspond au nombre de bytes présent dans le block donné du fichier WAVE
	dword sbck2size = nbr_sample * file_struct.fmt.Blockalign;
    // sbck1size correspond au nombre de données du block fmt
	dword sbck1size = 16;
    // cksize correspond à la taille du fichier WAVE moins la taille de sbck1size et sbck2size
	word cksize = 4 + (8 + sbck1size) + (8 + sbck2size);

    /*
     * On copie les données du block RIFF en prenant les données du fichier témoin
     *
     * Pour ChunkSize, on le remplie par la valeur cksize calculée précedement
     */
    strcpy(output->RIFF.ChunkID, file_struct.RIFF.ChunkID);
    output->RIFF.ChunkSize = cksize;
	strcpy(output->RIFF.Format, file_struct.RIFF.Format);

    /*
     * On copie les données de Subchunk1ID (contenant "fmt ") dans la structure générée
     *
     * On affecte les valeurs du témoin à la nouvelle structure générée
     */
	strcpy(output->fmt.Subchunk1ID, file_struct.fmt.Subchunk1ID);
	output->fmt.Subchunk1Size = file_struct.fmt.Subchunk1Size;	output->fmt.AudioFormat = file_struct.fmt.AudioFormat;
	output->fmt.NumChannels = file_struct.fmt.NumChannels;
	output->fmt.SampleRate = file_struct.fmt.SampleRate;
	output->fmt.ByteRate = file_struct.fmt.ByteRate;
	output->fmt.Blockalign = file_struct.fmt.Blockalign;
	output->fmt.BitsPerSample = file_struct.fmt.BitsPerSample;

    /*
     * On copie les données de Subchunk2ID (contenant "WAVE") dans la structure générée
     *
     * Pour Chunk2Size, on le remplie par la valeur cksize calculée précedement
     */
    strcpy(output->data.Subchunk2ID, file_struct.data.Subchunk2ID);
	output->data.Subchunk2Size = sbck2size;

    /*
     * Alocation de la taille du tableau contenant les échantillons
     * Subchunk2Size correspond au nombres de bytes du block data
     * Blockalign ( = Nombres de channel * Nombre de bits par échantillons /8 ) est le nombre de bytes par échantillons (samples)
     * Subchunk2Size / Blockalign correspond au nombre d'échantillons présent
     */
	output->data.data = malloc((unsigned int)(output->data.Subchunk2Size / output->fmt.Blockalign) * sizeof(short*) );

    // Indice pour la boucle for à venir
	int i = 0;

    /*
     * nbr_sample correspond au nombre d'échantillons présent dans le block data
     * On parcours le tableau tab_sample contenant les données du block data générés par les autres fontions
     * On affecte ces valeurs dans le tableau d'échnatillons de la strcuture qui sera générées
     */
	for (i=0; i < nbr_sample ; i++) {
		output->data.data[i] = tab_sample[i];
	}

}

/*!
 * \fn void store(char **path_tab, WAVE *file_struct, int nb_files)
 *
 * \param path-tab Tableau de chaine de caractère contenant les repertoires des fichiers sons
 * \param file_struct Tableau de structures d'un fichier WAVE.
 * \param nb_files Nombre de fichiers à recupérer les données
 *
 * \brief Extrait les données de chaques fichiers WAVE et les sotvcks dans une strcuture appropriée.
 *
 * \ingroup concat 
 */
void store(char **path_tab, WAVE *file_struct, int nb_files){

    // Indice pour parcourir l'ensemble des fichiers
	int i = 0;
	int j = 0;

    // On parcours l'ensemble des fichiers
	while (i < nb_files) {
     // Appel de la fonction qui lit un fichier WAVE et stock les données dans uen structure
		if (Read_Wav(path_tab[i], &file_struct[j]) == 0) { //reads file @i in tab d'analyse syntax and stores it in @j of struct tab file_struct
					// Incrément l'indice j
            j++;
		} else {
			printf("\nerreur: fichier  %s  introuvable\n", path_tab[i]);
			printf("\nsynthèse impossible");
			exit(0);
		}
        		// Incrément l'indice i
		i++;
	}

}


/*!
 * \fn void concatenation(char **path_tab, int nb_files, WAVE *sortie)
 *
 * \param path-tab Tableau de chaine de caractère contenant les repertoires des fichiers sons
 * \param nb_files Nombre de fichiers à recupérer les données et concaténés
 * \param sortie Structure qui va être remplie avec les données des différents fichiers.
 *
 * \brief Fonction qui va lire et stocker les données de chaque fichier, afin de la concaténer.
 *
 * Cette fonction va stocker les données de chaque fichier WAVE, grâce à store(), puis va effectuer un
 * cross-fade (fondu croisé) en modifiant les données de chaque echantillons. Les différents block de
 * données vont être concaténés en subissant un overlap (chevauchement) sur 100 ms, puis ecris dans une strcture
 * de type WAVE, grâce à create_structure(), qui servira à ecrire le fichier WAVE.
 *
 * \ingroup concat 
 */
void concatenation(char **path_tab, int nb_files, WAVE *sortie) {

	// Declaration d'un tableau de strcuture où seront stocké les données.
	WAVE file_struct[nb_files];

	// Appel à la fonction store() pout créer/remplir un tableau de structure
	store(path_tab, file_struct, nb_files);

    // Definit le temps de l'overlap à 100 ms
	const float t_overlap = 0.01;
    // Calcul le nombre de sample sur lequel on va effectuer l'overlap à partir de la valeur eb ms (t_overlap) sur laquel on veut overlaper
	const float overlap = t_overlap * file_struct[0].fmt.SampleRate; //overlap time in samples

	// Taille du tableau pour stocker samples
	dword totalsize = 0;

	int i;
    /*
     * Calcul de totalsize : on additione tous les nombres d'échantillons de chaque fichier
     * sans prendre en compte l'overlap, afin d'être sûr de ne pas avoir un tableau trop petit
     */
	for (i=0; i< nb_files; i++) {
		totalsize += file_struct[i].data.Subchunk2Size / file_struct[i].fmt.Blockalign;
	}

	// Création du tableau pour stocker l'ensemble des échantillons
	short* tab_sample;
	tab_sample = malloc (totalsize * sizeof(short));

    // Nombre d'échantillons dans le tableau final
	unsigned int nb_sample = 0;

    /*
     * Copie l'intégralité des echantillons du premier fichier tab_struct[0],
	 * On parcourt le tableau de 0 jusqu'à la taille totale du nombre d'échantillons (nombre total de samples)
     */
    for (nb_sample = 0; nb_sample < file_struct[0].data.Subchunk2Size / file_struct[0].fmt.Blockalign; nb_sample++) {
		tab_sample[nb_sample] = file_struct[0].data.data[nb_sample];
	}

	// s: sample
	int s = 0;

	// On effectue un fade-in sur les premiers échantillons du premier fichier.
	for (s = 0; s < overlap; s++) {
		tab_sample[s] = (short) (tab_sample[s] * (1 - ( (1+cos( (s * M_PI) / overlap ) ) / 2 ) ) );
	}

	// Création tableau temporaire de la taille d'un overlap
	short temp[(int)overlap];

	// boucle for complete
	int x = 0; //index du tab temp et début de chaque fichier k+1
	int k = 0;
	s = 0;

    //parcours de chaque fichiers, cad chaque case du tab de struct	on commence à 1.
	for (k=1; k < nb_files; k++) {

		//re-inititalise à 1 pour chaque boucle
        x = 0;

		//même calcul que wsola
        /* On applique un "cos+1" fade-out sur les "overlap" derniers échantillons de tab_sample
         * On applique un "cos+1" fade-in sur les "overlap" premiers échantillons du segment courant
         * On ajoute le deux segements dans tab_sample[]
         */
		for (s = nb_sample - overlap; s< nb_sample; s++) {
			tab_sample[s] = (short)(tab_sample[s] * (( 1 + cos( (x * M_PI) / overlap ) ) / 2)); // cos+1 fade-out les derniers
			temp[x] = file_struct[k].data.data[x]; // copie des samples dans le tableau temporaire
			temp[x] = (short) (temp[x] * (1 - ( (1+cos( (x * M_PI) / overlap ) ) / 2 ) ) ); // cos+1 fade-in dans le tab temporaire
			tab_sample[s] += temp[x];
			x++;
		}

		// On commence à la position nb_sample
		// et on copie du reste du fichier k jusque sa fin
		for (s = overlap; s < file_struct[k].data.Subchunk2Size / file_struct[k].fmt.Blockalign; s++) {
			tab_sample[nb_sample] = file_struct[k].data.data[s];
			nb_sample++;
		}
	}

	// Le fade out des "overlap" derniers echantillons est inutile car il se finit par un fichier vide.wav

    // Appel à la fonction qui crée la structure
	create_structure(file_struct[0], tab_sample, nb_sample, sortie);

    // Libère l'espace alloué pour le tableau tab_sample
	free(tab_sample);
}
