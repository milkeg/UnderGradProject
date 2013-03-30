/*
 * Concat.h
 *
 *  Created on: 5 avr. 2010
 *      Author: michael
 */

#ifndef CONCAT_H_
#define CONCAT_H_


// Fonction qui lit TOUS les WAVE
void Read(char **, WAVE*, int *);

//void create_structure(WAVE, WAVE*, short, dword);
void create_structure(WAVE ,short *, dword,WAVE*);
void store(char**, WAVE*, int);

void concatenation(char**, int, WAVE*);

#endif /* CONCAT_H_ */
