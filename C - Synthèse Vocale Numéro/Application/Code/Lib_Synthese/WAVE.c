/*!
 * \file wave.c
 *
 *
 *
 *\defgroup wave Wave
 *\ingroup synthese
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>


#include "Synthese_Vocale.h"
#include "WAVE.h"



/*!
 * \brief module assurant l'écriture d'une structure WAVE et reconstitue à partir de ses données un fichier son .wav.
 * \param WAVE son, structure WAVE à partir de laquelle on va écrire le fichier .wav
 * \param FILE *sortie fichier à écrire (.wav)
 *\ingroup wave
 *
 */
void write_wavfile (WAVE son, FILE *sortie) {

    fwrite(&son.RIFF.ChunkID, 4, 1, sortie);
    fwrite(&son.RIFF.ChunkSize, 4, 1, sortie);
    fwrite(&son.RIFF.Format, 4, 1, sortie);
    fwrite(&son.fmt.Subchunk1ID, 4, 1, sortie);
    fwrite(&son.fmt.Subchunk1Size, 4, 1, sortie);
    fwrite(&son.fmt.AudioFormat, 2, 1, sortie);
    fwrite(&son.fmt.NumChannels, 2, 1, sortie);
    fwrite(&son.fmt.SampleRate, 4, 1, sortie);
    fwrite(&son.fmt.ByteRate, 4, 1, sortie);
    fwrite(&son.fmt.Blockalign, 2, 1, sortie);
    fwrite(&son.fmt.BitsPerSample, 2, 1, sortie);
    fwrite(&son.data.Subchunk2ID, 4, 1, sortie);
    fwrite(&son.data.Subchunk2Size, 4, 1, sortie);


    int i=0;
    for (i=0; i<son.data.Subchunk2Size/son.fmt.Blockalign; i++) {
        fwrite(&son.data.data[i], 2, 1, sortie);
    }
}


/*!
 * \brief module assurant la lecture d'un fichier son .wav et stocke toutes ses données dans une structure WAVE.
 * \param char *wave, nom du fichier .wav à lire
 * \param WAVE *son, structure WAVE à écrire
 *\ingroup wave
 *
 */
int Read_Wav (char *wave_file, WAVE *son) {
	
	//création d'un fichier
	FILE *file;
	
	//ouverture/lecture du fichier wave_file en mode binaire (rb) : lecture en octets
	file = fopen(wave_file, "rb");

	/* gestion d'erreurs
	 *
	 * si l'un des fichiers chargés est null, càd introuvable, on envoie une erreur et on arrête le programme.
	 */
	if (file == NULL) {
		printf("\nerreur: fichier  %s  introuvable\n", wave_file);
		printf("\nsynthèse impossible");
		exit(0);
	}
	
	/* lecture des données du fichier .wav et stockage de celle-ci dans les champs adéquats de la structure WAVE */
	fread(&son->RIFF.ChunkID, 4, 1, file);
	fread(&son->RIFF.ChunkSize, 4, 1, file);
	fread(&son->RIFF.Format, 4, 1, file);
	fread(&son->fmt.Subchunk1ID, 4, 1, file);
	fread(&son->fmt.Subchunk1Size, 4, 1, file);
	fread(&son->fmt.AudioFormat, 2, 1, file);
	fread(&son->fmt.NumChannels, 2, 1, file);
	fread(&son->fmt.SampleRate, 4, 1, file);
	fread(&son->fmt.ByteRate, 4, 1, file);
	fread(&son->fmt.Blockalign, 2, 1, file);
	fread(&son->fmt.BitsPerSample, 2, 1, file);
	fread(&son->data.Subchunk2ID, 4, 1, file);
	fread(&son->data.Subchunk2Size, 4, 1, file);
	
	/* allocation de la taille du tableau data[] contenant les échantillons de la structure WAVE
	 *
	 * Blockalign: nombre d'octects pour coder un sample
	 * Subchunk2Size: nombre d'octects que représente tous les échantillons.
	 * on a donc le nombre d'échantillons en faisant l'opération Subchunk2Size/Blockalign
	 * un échantillon est contenable dans un short donc chaque case doit être de la taille d'un short.
	 */
	son->data.data = malloc( (int)(son->data.Subchunk2Size + 1) / (int)son->fmt.Blockalign * sizeof(short) );
	
	int i;
	
	//remplissage du tableau d'échantillons data[] de la structure WAVE
	for (i=0; i < (int)son->data.Subchunk2Size/son->fmt.Blockalign; i++){
		fread(&son->data.data[i], 2, 1, file);
	}
	
	//fermeture du fichier
	fclose(file);
	
	return 0;
}
