/*
 * WAVE.h
 *
 *  Created on: 5 avr. 2010
 *      Author: michael
 */

#ifndef WAVE_H_
#define WAVE_H_

// Fonction qui lit individuellement chaque fichier WAVE
int Read_Wav (char *, WAVE *);

void write_wavfile (WAVE , FILE *);
//void write_wavfile (WAVE son, FILE *sortie) {


#endif /* WAVE_H_ */
