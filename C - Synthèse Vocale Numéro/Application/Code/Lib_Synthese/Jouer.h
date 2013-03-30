/*
 * Jouer.h
 *
 *  Created on: 13 avr. 2010
 *      Author: michael
 */

#ifndef JOUER_H_
#define JOUER_H_

/* Les donn√©es du fichier son charg√© */
Uint8 * sounddata;
/* La taille du fichier son charg√©, en octets */
Uint32 soundlength;
/* Position courante de lecture dans le fichier son */
Uint32 soundpos;

int Jouer_Son(char *);
void mixaudio(void * , Uint8 *, int );


#endif /* JOUER_H_ */
