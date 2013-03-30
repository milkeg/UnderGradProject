/**
 *\file Traitement_Chaine.c
 *\defgroup traitement Traitement_chaine 
 */

#include<stdio.h>
#include<stdlib.h>
#include<string.h>

/**
 * \brief Traitement de la chaine
 *
 * Le module de traitement de la chaine a pour principal but de remplir un tableau de chaines.
 * La chaine de caractère  en entrée est d'abord analysée et filtrée.
 * Les caractères entre '0' et '9' sont acceptés de même que les caractères de séparation : '-', '.', ',', ' '.
 * Des caractères autre que ceux décrit ci-dessus seront ignorés.
 * Le '+' est reconnu en début de chaine.
 * La chaine sera découpée en plusieurs segments (ne dépassant pas quatres caractères) délimités par les séparateurs.
 * S'il n'y a pas de séparateurs, la chaine est segmentée par paire.
 * Ces segments seront ensuites stockés dans le tableau de chaines.
 *
 *
 * \param char * chaine, représente le numéro de téléphone.
 * \param char ** t, le tableau de chaines contenant les segments de la chaine.
 * \param int *nbe, la taille du tableau de chaines.
 *
 * \ingroup traitement
 *
 * \return none
 */
void traitement_chaine(char *chaine,char **t,int *nbe, int *error) {
	char ptr[256],ptr1[256];
	int i,longueur_chaine,longueur_souschaine,k,j,reste;
	int err = 0;

	/* On récupère la longueur de la chaine */
	longueur_chaine=strlen(chaine);

	/*
	 * On initialise i et j à 0 pour parcourir les différentes chaine.
	 * i pour la chaine principale et j pour la chaine secondaire (sous-chaine).
	 */
	j=0;i=0;

	/* On initialise k à 0 pour parcourir le tableau de chaine qui contiendra les différentes sous-chaines qui proviennent
	 * de la segmentation de la chaine principale.
	 */
	k=0;

	/* Boucle principale.
	 * On parcourt la chaine tant que le compteur i est strictement inférieur à la longueur de la chaine.
	 * A chaque tour de boucle, les caractères de la chaine sont analysés.
	 */
	while(i<longueur_chaine){

		/* Lorsque le caractère rencontré est un '+', on le stoke dans la chaine secondaire.
		 * Cette condition est vérifiée seulement lorsque l'on rencontre un '+' en début de chaine et que la lampe est éteinte
		 * Ensuite, on incrémente la valeur de j pour completer la chaine ultérieurement.
		 * La lampe prend la valeur TRUE.
		 * Le but est donc de prendre en compte le '+' en début de chaine seulement une seule fois, ce qui est fait en utilisant la lampe.
		 */
		if ((chaine[i]=='+') && (i==0)){
			ptr[0]=chaine[i];  /* Le '+' est stocké dans ptr la chaine secondaire (uniquement en début). */
			j++;
		}
		/* Si la condition précédente n'est pas vérifiée, on traite les caractères entre '0' et '9' (chiffres) et les caractères de séparation.
		 * Les caractères entre '0' et '9' seront stockés dans la chaine secondaire.
		 */
		else
		{
			/* Boucle secondaire
			 * Tant que l'on ne recontre pas un caractère de séparation ou que l'on a pas atteint la fin de chaine,
			 * on parcourt la chaine à partir de i, position du caractère courant.
			 * Le fait de rencontrer ces caractères de séparation signifie que l'on a une sous-chaine formée et qui a été stockée dans ptr.
			 * Les '+' ne ne sont pas pris en compte.
			 */
			while ((chaine[i]!=' ') && (chaine[i]!=',') && (chaine[i]!='.') && (chaine[i]!='-')&&(i<longueur_chaine)) {

				//printf("/t%d\n", i);
				if ((chaine[i]>='0')&&(chaine[i]<='9')) { /* Seulement les caractères entre '0' et '9' sont traités. */
					ptr[j]=chaine[i]; /* La sous-chaine est formée. */
					j++; /* On incrémente j pour completer la chaine ptr. */
				} else {
					/* le caractère n'est pas compris entre '0' et '9' => carractère non reconnu
					 * si le caractère n'est pas '\n' càd désignant la fin de la chaine (acceptable)
					 *     alors incrémentation de err
					 */
					if(chaine[i] != '\n') {
						err++;
					}
				}
				/*
				 * On incrémente i jusqu'a ce que l'on rencontre un caractère de séparation ou jusqu'a la fin de la chaine.
				 * On aura ainsi une sous-chaine extraite.
				 */
				i++;
			}
		}

		ptr[j]='\0'; /* On complete la sous-chaine par un '\0' pour indiquer la fin de sous-chaine. */

		/* ptr est donc une chaine de caractère qui a été extraite de la chaine principale
		 * Notons ici que la sous-chaine peut soit etre composé d'un '+' ou d'une suite de caractère entre '0' et '9'.
		 */
		if (strcmp(ptr,"")!=0){ /*On commence le traitement de la sous-chaine si elle n'est pas vide. */

			longueur_souschaine=strlen(ptr); /*  on recupère la longueur de la sous-chaine extraite de la chaine principale. */

			/* Selon la longueur de la sous-chaine, on va effectuer deux traitements différents sur la sous-chaine.
			 * Dans le cas ou cette longueur est supérieure à quatres caractères, la sous-chaine va être segmentée par paire.
			 * On fera intervenir une deuxieme variable qui prendra pour valeur un segment de la chaine principale.
			 * Chaque paire de caractères sera ensuite copiée dans le tableau de chaines.
			 */
			if (longueur_souschaine>4){

			/* Selon la parité de la longueur de la chaine, il y aura deux cas.
			 * Si la longueur est impaire, le premier caractère de cette sous-chaine est isolé. On fait appel à une nouvelle variable: ptr1.
			 * On copie ce caractère dans la chaine temporaire qui est copié à son tour dans le tableau de chaines.
			 * Enfin, puisque la longueur de chaine est impaire, le reste de la division est à 1. Cette valeur est stockée dans l1.
			 * l1 qui sera utilisé pour traiter les segments suivants de la sous-chaine.
			 */
				if ((longueur_souschaine%2)!=0){ /* La longueur de la chaine est impaire. */
		    		ptr1[0]=ptr[0];   /* On copie le premier caractère de la sous-chaine (on l'isole). */
		    		ptr1[1]='\0';	 /* On complete la sous-chaine par un '\0' pour indiquer la fin de chaine. */
		    		t[k] = (char*) malloc( 5*sizeof(char));
		    		strcpy(t[k],ptr1);
		    		k++;
		    		reste=1;  /* La chaine est impaire. Le reste est à 1. */
		    	}
				else /* La longueur de la chaine est paire */
				{
					reste=0; /* Le reste est à 0. */
				}

			/* Tant que le reste est strictement inférieur à la longueur de la chaine, on parcourt la sous-chaine.
			 * On continue de segmenter la sous-chaine. Le reste est soit à 0 ou 1.
			 * On utilisera cette valeur comme indice à partir duquel il faut parcourir et segmenter la sous-chaine.
			 * Lorsque deux caractères ont été copié dans la chaine temporaire, on stoke cette chaine temporaire (ptr1) dans le tableau de chaine.
			 * Enfin on ajoute 2 à l1, puisqu'on a copié deux caractères.
			 */
				while (reste<longueur_souschaine){
					ptr1[0]=ptr[reste]; /* On copie deux caractères dans la chaine ptr1 */
					ptr1[1]=ptr[reste+1];
					ptr1[2]='\0';  /* On complete la sous-chaine par un '\0' pour indiquer la fin de chaine. */
					t[k] = (char*) malloc( 5*sizeof(char));
					strcpy(t[k],ptr1);   /* On copie la chaine temporaire dans le tableau de chaines de caractères */
					k++;
					reste=reste+2;
				}
			}

			/* Sinon, quand la longueur de la sous-chaine est inférieur à 4, on stoke directement cette chaine dans le tableau de chaines. */
			else
			{
		    	t[k] = (char*) malloc( 5*sizeof(char));
			strcpy(t[k],ptr);   /** On copie la sous-chaine dans le tableau de chaines de caractères. */
			k++;   /* On incrémente k pour compléter ultérieurement le tableau de chaines.  */
		    }
		    strcpy(ptr,"       "); /* On supprime le contenu de la sous-chaine pour pouvoir traiter la sous-chaine suivante, lorsque i sera incrémenté. */
		}
		j=0; /* On remet l'indice de la sous-chaine à 0 pour pouvoir y stocker la prochaine sous-chaine.*/
		i++; /* On incrémente i pour passer au caractère suivant de la chaine principale. */
	}

	/* gestion d'erreur:
	 *
	 * si la première case du tableau est vide, cela implique qu'aucun des caractères de chaine[] n'est reconnu
	 * on a donc une erreur d'entrée (numero non valide)
	 * On arrête le programme
	 */
	if (k == 0) {
		printf("\nerreur: aucun caractère reconnu\n");
		printf("\nsynthèse impossible");
		exit(0);
	}

	/* gestion d'avertissement:
	 *
	 * si err est supérieur à 0, (càd qu'il a été incrémenté au moins une fois => au moins un caractère non reconnu)
	 * alors on incrémente la variable error
	 * cela impliquera que (error%10) ne soit pas nul ce qui permettra un printf ultérieur de l'avertissement:
	 * "certains caractères entrées ne sont pas reconnus, ils ont étés supprimés"
	 */
	if ( err > 0 ) {
		*error += 1;
	}

	*nbe=k; /*taille du tableau de chaine, à utiliser après traitement_chaine */
}
