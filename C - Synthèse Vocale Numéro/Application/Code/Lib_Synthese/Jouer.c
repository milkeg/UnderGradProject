/*!
 * \file jouer.c
 *
 * \brief 
 * \defgroup sdl Jouer-son : description longue
 *description detaille du module
 * \ingroup synthese
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "SDL/SDL.h"
#include "Synthese_Vocale.h"

#include "Jouer.h"



/*!
 * \brief Fonction de rappel qui copie les donn√©es sonores dans le tampon audio
 *
 * \param void * userdata
 * \param Uint8 * stream
 * \param int len
 *
 *\ingroup sdl
 *
 */
void mixaudio(void * userdata, Uint8 * stream, int len)
{
	/* Attention √† ne pas d√©border lors de la copie */
	Uint32 tocopy = soundlength - soundpos > len ? len : soundlength - soundpos;
	
	//printf("%d\n", tocopy);
	
	/* Copie des donn√©es sonores dans le tampon audio... */
	memcpy(stream, sounddata + soundpos, tocopy);
	
	/* Mise √† jour de la position de lecture */
	soundpos += tocopy;
}

/*!
 * \brief Fonction de rappel qui copie les donn√©es sonores dans le tampon audio
 *
 * \param char * nom
 *
 *\ingroup sdl
 *
 */
int Jouer_Son(char * nom) {

	SDL_AudioSpec desired, obtained, soundfile;
	SDL_AudioCVT cvt;
	
	if (SDL_Init(SDL_INIT_AUDIO) == -1) {
		printf("Erreur lors de l'initialisation de SDL!\n");
		return 1;
	}
	
	/* Son 16 bits mono √† 44100 Hz */
	desired.freq = 44100;
	desired.format = AUDIO_S16SYS;
	desired.channels = 1;
	
	/* Le tampon audio contiendra 512 √©chantillons */
	desired.samples = 512;
	
	/* Mise en place de la fonction de rappel et des donn√©es utilisateur */
	desired.callback = &mixaudio;
	desired.userdata = NULL;
	
	if (SDL_OpenAudio(&desired, &obtained) != 0) {
		printf("Erreur lors de l'ouverture du p√©riph√©rique audio: %s\n", SDL_GetError());
		return 1;
	}
	
	/* Chargement du fichier .wav */
	if (SDL_LoadWAV(nom, &soundfile, &sounddata, &soundlength) == NULL) {
		printf("Erreur lors du chargement du fichier son: %s\n", SDL_GetError());
		return 1;
	}
	
	/* Conversion vers le format du tampon audio */
	if (SDL_BuildAudioCVT(&cvt, soundfile.format, soundfile.channels, soundfile.freq,
						  obtained.format, obtained.channels, obtained.freq) < 0) {
		printf("Impossible de construire le convertisseur audio!\n");
		return 1;
	}
	
	/* Cr√©ation du tampon utilis√© pour la conversion */
	cvt.buf = malloc(soundlength * cvt.len_mult * sizeof(double*));
	cvt.len = soundlength;
	memcpy(cvt.buf, sounddata, soundlength);
	
	/* Conversion... */
	if (SDL_ConvertAudio(&cvt) != 0) {
		printf("Erreur lors de la conversion du fichier audio: %s\n", SDL_GetError());
		return 1;
	}
	
	/* Lib√©ration de l'ancien tampon, cr√©ation du nouveau,
     copie des donn√©es converties, effacement du tampon de conversion */
	SDL_FreeWAV(sounddata);
	sounddata = malloc(cvt.len_cvt * sizeof(int*));
	memcpy(sounddata, cvt.buf, cvt.len_cvt);
	free(cvt.buf);
	
	soundlength = cvt.len_cvt;
	soundpos = 0;
	
	/* La fonction de rappel commence √† √™tre appel√©e √† partir de maintenant. */
	SDL_PauseAudio(0);
	
	/* On attend que l'autre thread ait fini la lecture du son... */
	while (soundpos < soundlength);
	
	/* On cesse d'appeler la fonction de rappel */
	SDL_PauseAudio(1);
	
	/* Fermer le p√©riph√©rique audio */
	SDL_CloseAudio();
	
	SDL_Quit();
	return 0;
}
