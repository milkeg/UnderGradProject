/*
 * \file Integration_1.h
 * 
 */
#ifndef INTEGRATION_1_H_
#define INTEGRATION_1_H_

/*!
 * \def unsigned long word
 * Variable de 4 octets
 */
typedef unsigned long word;
/*!
 * \def unsigned long dword
 * Variable de 4 octets
 */
typedef unsigned long dword;



// Structure
/**
 * \struct WAVE
 * \brief Structure d'un fichier WAVE standart
 * Structure codé grace aux structures RIFF, fmt et data d'un fichier audio .wav 
 *
 *
 *\ingroup wave
 */

typedef struct WAVE {
	
	/**
     *\struct RIFF
     *\brief Description principal du fichier
	 * "RIFF" (chunk descriptor) structure décrivant la nature du fichier.
	 */
	struct RIFF {
		char ChunkID[4];	// contient les lettres "RIFF" pour indiquer que le fichier est codé selon la norme RIFF
		word ChunkSize;		// taille du fichier entier en octets (sans compter les 8 octets de ce champ (4o) et le champ précédent CunkID (4o)
		char Format[4];		// correspond au format du fichier donc ici, contient les lettres "WAVE" car fichier est au format wave
	} RIFF;
	
	/**
	 *\struct fmt
     *\brief Sprécifications du format audio
     * "fmt " (sub-chunk) structure décrivant le format des données audio
	 */
	struct fmt {
		char Subchunk1ID[4];	// contient les lettres "fmt " pour indiquer les données à suivre décrivent le format des données audio
		dword Subchunk1Size;	// taille en octet des données à suivre (qui suivent cette variable) 16 Pour un fichier PCM
		short AudioFormat;		// format de compression (une valeur autre que 1 indique une compression)
		short NumChannels;		// nombre de canaux: Mono = 1, Stereo = 2, etc..
		dword SampleRate;		// fréquence d'échantillonage, ex 44100, 44800 (nombre d'échantillons par secondes)
		dword ByteRate;			// nombre d'octects par secondes
		short Blockalign;		// nombre d'octects pour coder un échantillon
		short BitsPerSample;	// nombre de bits pour coder un échantillon
	} fmt;
	
	/**
	 *
     *\struct data
     *\brief Données du fichier audio
     * "data" (sub-chunk) contient les données audio, les échantillons et le nombre d'octets qu'ils représentent.
	 */
	struct data {
		char Subchunk2ID[4];	// contient les lettres "data" pour indiquer que les données à suivre sont les données audio (les échantillons et)
		dword Subchunk2Size;	// taille des données audio (nombre total d'octets codant les données audio)
		short *data;			// données audio... les échantillons
	} data;
} WAVE;

void Synthese_Vocale();

#endif /* INTEGRATION_1_H_ */
