/*!
 *\file Analyse_Syntaxique.c
 *\defgroup analyse Analyse-syntaxique
 *Le module d'analyse syntaxique consiste à décomposer le numéro entré en chiffres ou numéros qui le constituent.
 *exemple: 5243 => "cinq-mille-deux-cent-quarante-trois".
 *\ingroup synthese
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

/*!
 * \brief fonction construire
 *
 * A partir d'une chaine de caractères (qui ne contient que des chiffre) de taille longueur (max 4)
 *trouver le millier, le centaine le dizaine et l unité
 *
 * Exemple:
 * si t="1234" alors millier=1,centaine=2, dizaine=3,unite=4
 * dans le cas ai la longeur est <4 on mets les valeurs qui manquent a -1
 * exemple t="08" alors millier =-1,centaine=-1;dizaine=0,unite=8;
 *
 * \param char *t : une chaine de caractère qui contient que des chiffres
 * \param int longueur : longueur de la chaine t
 * \param int *millier  : a remplir par les milliers extrait de la chaine t
 * \param int *centaine : a remplir par les centaines extrait de la chaine t
 * \param int *dizaine  : a remplir par les dizaines extrait de la chaine t
 * \param int *unite    : a remplir par les unites extrait de la chaine t
 *
 *
 * \ingroup analyse
 */

void construire(char *t,int longueur,int *milier,int *cent,int*dizaine,int*unit)
{


	/* ici on fait la conversion caractere-entier en faisant la soustraction
	 entre le code ascci du caractere et  le code ascci de 0 et ca dans chacun des cas suivant
	 on peut utiliser la fonction atoi qui a le meme effet */

	if (longueur==4 )    /* si la longueur de la chaine est 4 alors on milier,centaine,dizaine,untie*/
	{

		*milier=t[0]-'0';
		*cent=t[1]-'0';
		*dizaine=t[2]-'0';
		*unit=t[3]-'0';
	}
	else
	{
		if (longueur==3 ) /* si la longueur de la chaine est 3 alors on a pas miliere dans ce cas on
						   met milier a -1 et on le traite pas dans la suite par contre on a centaine,dizaine,untie*/
		{
			*milier=-1;
			*cent=t[0]-'0';
			*dizaine=t[1]-'0';
			*unit=t[2]-'0';
		}

		else
		{ if (longueur==2 )/* si la longueur de la chaine est 2 alors on a pas ni millier ni
							centianes et dans ce cas on met milier,centaine a -1 et on les traite
							pas dans la suite par contre on a dizaine,untie*/
		{
			*milier=-1;
			*cent=-1;
			*dizaine=t[0]-'0';
			*unit=t[1]-'0';
		}


		else /* si la longueur de la chaine est 1 alors on a que l unite et le reste a -1 */
		{
			*milier=-1;
			*cent=-1;
			*dizaine=-1;
			*unit=t[0]-'0';
		}
		}
	}


}


/*!
 * \brief ecriture_case (path file)
 *
 * \param char * voix
 * \param char **T
 * \param int nb
 *
 *\ingroup analyse
 *
 */
void ecriture_case(char *voix, char **T, int nb){

	char path[30]; //cr√©ation d'un path
    char temp[30];

    int i;
	strcpy(path, "sons/");
	strcat(path, voix);
	strcat(path, "/");
	for (i=0;i<nb;i++)
	{
		strcpy(temp,path);
		strcat(temp, T[i]);
		strcat(temp, ".wav");
		//printf("%s\t%s\t%s\n", temp, T[i], *(T+i));
		//T[i] = malloc (50 * sizeof(char*));
		T[i] = malloc (30 * sizeof(char));
		strcpy( T[i], temp);
	}
}

/*!
 *\brief Construction tableau de noms de fichiers
 *
 * Cette fonction a pour but de construire un tableau des nom des fichiers de nom qui est T_nom
 * dans notre cas a partir d'un tableau de chaine qui contient que des chiffres
 * exemple si on a t[1]="123" et t[2]="004", le reslutat de T_nom sera :
 * '\n'T_nom[1]="cent"
 * '\n'T_nom[1]="vingt"
 * '\n'T_nom[1]="trois"
 * '\n'T_nom[1]="vide"      ici on separe les chaine par un vide(silence)
 * '\n'T_nom[1]="zero"
 * '\n'T_nom[1]="zero"
 * '\n'T_nom[1]="quatre"
 *
 * \param char **t: tableau de chaîne de caractères contenant
 * \param int nbet: le nombre d'éléments du tableau t
 * \param int *nbe : le nombre d'éléments du tableau T_Nom
 * \param char **T_Nom tableau de chaîne de caractères à remplir par les chemins vers les enregistreemnts des fichiers audio
 *
 *\ingroup analyse
 */
void analyse_syntaxique(int int_voix, char **t, int nbet, int *nbe, char **T_Nom)
{

	char *voix;

	if (int_voix == 0) {
		voix = "feminin";
	} else if (int_voix == 1) {
		voix = "masculin";
	}

	int longueur;
	int k,milier,cent,dizaine,unite,nb_elem;
	int oups=0;

	nb_elem=0;

	/* pour chaque element du tableau t c-a-d on va traiter chacune des chaines de caracteres
	 *
	 */
	for (k=0;k<nbet;k++)
	{ oups=0;

		longueur=strlen(t[k]);/* on clacule la longueur de la chaine courante da la case du tableau t*/

		/*
		 * si lachaine est le caractere + on la met directement dans le tableau T_nom
		 */
		if (strcmp(t[k],"+")==0)
		{
			T_Nom[nb_elem]="plus";
			T_Nom[nb_elem+1] ="vide";
			nb_elem = nb_elem+2;

		}

		/* si non on appel la fonction construire qui nous donne les miliers,centaines
		 * dizaine et unites
		 */


		else
		{


			construire(t[k],longueur,&milier,&cent,&dizaine,&unite);


			/* selon la valeur de miliers on remplie le tableau de nom
			 * exemple si on a milier=0 on met la chaine "zero" dans le tableau T_nom
			 * 		  si on a milier=1 on met la chaine "un" puis la chaine "mille" dans le tableau T_nom
			 * 		  si on a milier=2 on met la chaine "deux" puis la chaine "mille" dans le tableau T_nom
			 *         etc.... jusqu a 9
			 */
			switch(milier)
			{

				case 0: {
					T_Nom[nb_elem]="zero";
					//ecriture_case (voix, T_Nom, nb_elem, "zero");
					nb_elem++;
					break;
				}
				case 1: { T_Nom[nb_elem]="mille";
					//ecriture_case (voix, T_Nom, nb_elem, "mille");
					nb_elem++;
					break;
				}
				case 2: { T_Nom[nb_elem]="deux";
					//ecriture_case (voix, T_Nom, nb_elem, "deux");
					T_Nom[nb_elem+1]="mille";
					//ecriture_case (voix, T_Nom, nb_elem+1, "mille");
					nb_elem=nb_elem+2;
					break;
				}
				case 3: { T_Nom[nb_elem]="trois";
					//ecriture_case (voix, T_Nom, nb_elem, "trois");
					T_Nom[nb_elem+1]="mille";
					//ecriture_case (voix, T_Nom, nb_elem+1, "mille");
					nb_elem=nb_elem+2;
					break;
				}
				case 4: { T_Nom[nb_elem]="quatre";
					//ecriture_case (voix, T_Nom, nb_elem, "quatre");
					T_Nom[nb_elem+1]="mille";
					//ecriture_case (voix, T_Nom, nb_elem+1, "mille");
					nb_elem=nb_elem+2;
					break;
				}
				case 5: { T_Nom[nb_elem]="cinq";
					//ecriture_case (voix, T_Nom, nb_elem, "cinq");
					T_Nom[nb_elem+1]="mille";
					//ecriture_case (voix, T_Nom, nb_elem+1, "mille");
					nb_elem=nb_elem+2;
					break;
				}
				case 6: { T_Nom[nb_elem]="si";
					//ecriture_case (voix, T_Nom, nb_elem, "six");
					T_Nom[nb_elem+1]="mille";
					//ecriture_case (voix, T_Nom, nb_elem+1, "mille");
					nb_elem=nb_elem+2;
					break;
				}
				case 7: { T_Nom[nb_elem]="sept";
					//ecriture_case (voix, T_Nom, nb_elem, "sept");
					T_Nom[nb_elem+1]="mille";
					//ecriture_case (voix, T_Nom, nb_elem+1, "mille");
					nb_elem=nb_elem+2;
					break;
				}
				case 8: { T_Nom[nb_elem]="hui";
					//ecriture_case (voix, T_Nom, nb_elem, "hui");
					T_Nom[nb_elem+1]="mille";
					//ecriture_case (voix, T_Nom, nb_elem+1, "mille");
					nb_elem=nb_elem+2;
					break;
				}
				case 9: { T_Nom[nb_elem]="neuf";
					//ecriture_case (voix, T_Nom, nb_elem, "neuf");
					T_Nom[nb_elem+1]="mille";
					//ecriture_case (voix, T_Nom, nb_elem+1, "mille");
					nb_elem=nb_elem+2;
					break;
				}
			}

			// cas_millier(milier,&nb_elem,&T_Nom);

			/* on traite les centaines ds la meme maniere que les milliers */
			switch(cent)
			{
				case 0: {
					/* dans le cas si centaine est 0 on doit verifier si les miliers est 0 ou n'esxiste pas
					 * pourqu on remplie le tableau T_Nom par zero si non on fait rien
					 * exemple si la chaine est 0001 dans ce cas on doit lire le zero du centaine
					 * par contre si la chaine est 1001 on fait rien pour les centaines
					 */
					if (((milier==0) || (milier==-1)))
					{
						T_Nom[nb_elem]="zero";
						nb_elem++;
					}
					break;
				}
				case 1: { T_Nom[nb_elem]="cent";
					//ecriture_case (voix, T_Nom, nb_elem, "cent");
					nb_elem++;
					break;
				}
				case 2: { T_Nom[nb_elem]="deux";
					//ecriture_case (voix, T_Nom, nb_elem, "deux");
					T_Nom[nb_elem+1]="cent";
					//ecriture_case (voix, T_Nom, nb_elem+1, "cent");
					nb_elem=nb_elem+2;
					break;
				}
				case 3: { T_Nom[nb_elem]="trois";
					//ecriture_case (voix, T_Nom, nb_elem, "trois");
					T_Nom[nb_elem+1]="cent";
					//ecriture_case (voix, T_Nom, nb_elem+1, "cent");
					nb_elem=nb_elem+2;
					break;
				}
				case 4: { T_Nom[nb_elem]="quatre";
					//ecriture_case (voix, T_Nom, nb_elem, "quatre");
					T_Nom[nb_elem+1]="cent";
					//ecriture_case (voix, T_Nom, nb_elem+1, "cent");
					nb_elem=nb_elem+2;
					break;
				}
				case 5: { T_Nom[nb_elem]="cinq";
					//ecriture_case (voix, T_Nom, nb_elem, "cinq");
					T_Nom[nb_elem+1]="cent";
					//ecriture_case (voix, T_Nom, nb_elem+1, "cent");
					nb_elem=nb_elem+2;
					break;
				}
				case 6: { T_Nom[nb_elem]="si";
					//ecriture_case (voix, T_Nom, nb_elem, "six");
					T_Nom[nb_elem+1]="cent";
					//ecriture_case (voix, T_Nom, nb_elem+1, "cent");
					nb_elem=nb_elem+2;
					break;
				}
				case 7: { T_Nom[nb_elem]="sept";
					//ecriture_case (voix, T_Nom, nb_elem, "sept");
					T_Nom[nb_elem+1]="cent";
					//ecriture_case (voix, T_Nom, nb_elem+1, "cent");
					nb_elem=nb_elem+2;
					break;
				}
				case 8: { T_Nom[nb_elem]="hui";
					//ecriture_case (voix, T_Nom, nb_elem, "hui");
					T_Nom[nb_elem+1]="cent";
					//ecriture_case (voix, T_Nom, nb_elem+1, "cent");
					nb_elem=nb_elem+2;
					break;
				}
				case 9: { T_Nom[nb_elem]="neuf";
					//ecriture_case (voix, T_Nom, nb_elem, "neuf");
					T_Nom[nb_elem+1]="cent";
					//ecriture_case (voix, T_Nom, nb_elem+1, "cent");
					nb_elem=nb_elem+2;
					break;
				}
			}



			/*
			 *
			 */

			switch(dizaine)
			{
				case 0: {


					/* Pareil comme le cas des centaine si dizaine est 0 et pour mettre "Zero" dans le tableau T_Nom
					 * il faut que les miliers et les centaines soient 0 ou n'existent pas
					 */

					if (((milier==0) || (milier==-1)) &&((cent==0)||(cent==-1)))
					{
						T_Nom[nb_elem]="zero";
						nb_elem++;
					}
					break;
				}
				case 1: { 	/* si dizaine =1 alros on doit siganler aux unite que le 1 devient onze,le 2 devien douze .....
							 donc j utilise la varialble booleene oups que je le mets a 1 dans ce cas */
					oups=1;

					break;
				}
				case 2: {     	/* si dizaine =2 on a trois cas a etudier
								 1- si unite =0 alors on doit doit dire vingt
								 2- si unite =1 alors on doit dire vingt teh un
								 3- si non alors on doit ditre vingtt (deux,trois .......)
								 */

					if (unite==0)
					{T_Nom[nb_elem]="vingt";
						//ecriture_case (voix, T_Nom, nb_elem, "vingt");
						nb_elem++;
					}
					else
					{if (unite==1)
					{T_Nom[nb_elem]="vingtt";
						//ecriture_case (voix, T_Nom, nb_elem, "vingt");
						T_Nom[nb_elem+1]="eh";
						//ecriture_case (voix, T_Nom, nb_elem+1, "teh");
						nb_elem=nb_elem+2;
					}

					else
					{T_Nom[nb_elem]="vingtt";
						//ecriture_case (voix, T_Nom, nb_elem, "vingtt");
						nb_elem++;
					}
					}
					break;
				}
				case 3: {      /*si dizaine =3 on a deux cas a etudier
								1-si unite =1 alors on doit dire trente eh un
								2-si non alors on doit dire trente (deux,trois .......)
								*/
					T_Nom[nb_elem]="trente";
					//ecriture_case (voix, T_Nom, nb_elem, "trente");

					if (unite==1)
					{T_Nom[nb_elem+1]="eh";
						//ecriture_case (voix, T_Nom, nb_elem+1, "eh");
						nb_elem++;
					}
					nb_elem++;
					break;
				}
				case 4: {
					/* si dizaine =4 on a deux cas a etudier
					 1- si unite =1 alors on doit dire quarante eh un
					 2- si non alors on doit dire quarante (deux,trois .......)
					 */
					T_Nom[nb_elem]="quarante";
					//ecriture_case (voix, T_Nom, nb_elem, "quarante");
					if (unite==1)
					{T_Nom[nb_elem+1]="eh";
						//ecriture_case (voix, T_Nom, nb_elem+1, "eh");
						nb_elem++;
					}
					nb_elem++;
					break;
				}

				case 5: {	/* si dizaine =5 on a deux cas a etudier
							 1- si unite =1 alors on doit dire cinquante eh un
							 2- si non alors on doit dire cinquante (deux,trois .......)
							 */
					T_Nom[nb_elem]="cinquante";
					//ecriture_case (voix, T_Nom, nb_elem, "cinquante");

					if (unite==1)
					{T_Nom[nb_elem+1]="eh";
						//ecriture_case (voix, T_Nom, nb_elem, "eh");
						nb_elem++;
					}
					nb_elem++;
					break;
				}
				case 6: {
					/* si dizaine =6 on a deux cas a etudier
					 1- si unite =1 alors on doit dire soixante eh un
					 2- si non alors on doit dire soixante (deux,trois .......)
					 */
					T_Nom[nb_elem]="soixante";
					//ecriture_case (voix, T_Nom, nb_elem, "soixante");

					if (unite==1)
					{T_Nom[nb_elem+1]="eh";
						//ecriture_case (voix, T_Nom, nb_elem+1, "eh");
						nb_elem++;
					}
					nb_elem++;
					break;
				}
				case 7: {
					/*
					 si dizaine =7 on a deux cas a etudier
					 1- si unite =1 alors on doit dire soixante eh onze
					 2- si non alors on doit dire soixante (douze,trize .......)
					 */
					T_Nom[nb_elem]="soixante";
					//ecriture_case (voix, T_Nom, nb_elem, "soixante");

					if (unite==1)
					{T_Nom[nb_elem+1]="eh";
						//ecriture_case (voix, T_Nom, nb_elem+1, "eh");
						nb_elem++;
					}
					nb_elem++;
					/* oups est une variable booleene qui indique au unite de faire
					 * attention dans le cas de 7 et neuf les unite seront plus un,deux trois
					 * ca devient onze douze trize ,,,,,
					 */
					oups=1;
					break;
				}
				case 8: {
					/* si dizaine =8 on doit remplir deux case du tableau par les deux elements quatree et vingt
					 ici on doit faire la difference entre quatre et quatree
					 */
					T_Nom[nb_elem]="quatree";
					T_Nom[nb_elem+1]="vingt";
					nb_elem=nb_elem+2;
					break;
				}
				case 9: { 	/* si dizaine =9 on le traite de la meme maniere de 8 mais en plus de mettre la variable boolen a 1
							 pour traiter les unites differement
							 */
					T_Nom[nb_elem]="quatree";
					T_Nom[nb_elem+1]="vingt";
					nb_elem=nb_elem+2;
					oups=1;
					break;
				}
			}


			switch(unite)
			{

				/* Pareil comme le cas des dizaine si unité est 0 et pour mettre "Zero" dans le tableau T_Nom il faut que les milliers et 					 *les centaines et dizaine soient 0 ou n'existent pas
				 */
				case 0: { if (((milier==0) || (milier==-1)) &&((cent==0)||(cent==-1)) &&  ((dizaine==0)||(dizaine==-1)))
				{
					T_Nom[nb_elem]="zero";
					nb_elem++;
				}
					if (oups==1)
					{T_Nom[nb_elem]="dix";
						//ecriture_case (voix, T_Nom, nb_elem, "dix");
						nb_elem++;
					}
					break;
				}

				/*si unite=1 on a deux cas ou bien on ajoute "un" ou "onze" dans le tableau de t_nom
				on ajoute "onze" dans le cas dizaine=1,7 ou 9 c-a-d oups=1 sinon on ajoute "un" dans le tableau de t_nom
				*/
				case 1: { if (oups==1)
				{T_Nom[nb_elem]="onze";
					//ecriture_case (voix, T_Nom, nb_elem, "onze");
					nb_elem++;
				}
				else
				{T_Nom[nb_elem]="un";
					//ecriture_case (voix, T_Nom, nb_elem, "un");
					nb_elem++;
				}
					break;
				}

				/*si unité=2 on a deux cas ou bien on ajoute "deux" ou "douze" dans le tableau de t_nom
				on ajoute "douze" dans le cas dizaine=1,7 ou 9 c-a-d oups=1 sinon on ajoute "deux" dans le tableau de t_nom
				*/
				case 2: {
					if (oups==1)
					{T_Nom[nb_elem]="douze";
						//ecriture_case (voix, T_Nom, nb_elem, "douze");
						nb_elem++;
					}
					else
					{T_Nom[nb_elem]="deux";
						//ecriture_case (voix, T_Nom, nb_elem, "deux");
						nb_elem++;
					}
					break;
				}

				/*si unité=3 on a deux cas ou bien on ajoute "trois" ou "treize" dans le tableau de t_nom
				 *on ajoute "treize" dans le cas dizaine=1,7 ou 9 c-a-d oups=1 sinon on ajoute "trois" dans le tableau de t_nom
    				*/
				case 3: {
					if (oups==1)
					{T_Nom[nb_elem]="treize";
						//ecriture_case (voix, T_Nom, nb_elem, "treize");
						nb_elem++;
					}
					else
					{T_Nom[nb_elem]="trois";
						//ecriture_case (voix, T_Nom, nb_elem, "trois");
						nb_elem++;
					}
					break;
				}

				/*si unité=4 on a deux cas ou bien on ajoute "quatre" ou "quatorze" dans le tableau de t_nom
				on ajoute "quatorze" dans le cas dizaine=1,7 ou 9 c-a-d oups=1 sinon on ajoute "quatre" dans le tableau de t_nom
				*/
				case 4: {
					if (oups==1)
					{T_Nom[nb_elem]="quatorze";
						//ecriture_case (voix, T_Nom, nb_elem, "quatorze");
						nb_elem++;
					}
					else
					{T_Nom[nb_elem]="quatre";
						//ecriture_case (voix, T_Nom, nb_elem, "quatre");
						nb_elem++;
					}
					break;
				}

				/*si unité=5 on a deux cas ou bien on ajoute "cinq" ou "quinze" dans le tableau de t_nom
				*on ajoute "quinze" dans le cas dizaine=1,7 ou 9 c-a-d oups=1 sinon on ajoute "cinq" dans le tableau de t_nom*/
				case 5: {
					if (oups==1)
					{T_Nom[nb_elem]="quinze";
						//ecriture_case (voix, T_Nom, nb_elem, "quinze");
						nb_elem++;
					}
					else
					{T_Nom[nb_elem]="cinq";
						//ecriture_case (voix, T_Nom, nb_elem, "cinq");
						nb_elem++;
					}
					break;
				}

				/*si unité=6 on a deux cas ou bien on ajoute "six" ou "seize" dans le tableau de t_nom
				*on ajoute "seize" dans le cas dizaine=1,7 ou 9 c-a-d oups=1 sinon on ajoute "six" dans le tableau de t_nom
				*/
				case 6: {
					if (oups==1)
					{T_Nom[nb_elem]="seize";
						//ecriture_case (voix, T_Nom, nb_elem, "seize");
						nb_elem++;
					}
					else
					{T_Nom[nb_elem]="six";
						//ecriture_case (voix, T_Nom, nb_elem, "six");
						nb_elem++;
					}
					break;
				}

				/*si unité=7 on a deux cas ou bien on ajoute "sept" ou ("di" et "sept") dans le tableau de t_nom
				 *on ajoute ("di" et "sept")  dans le cas dizaine=1,7 ou 9 c-a-d oups=1 sinon on ajoute "sept" dans le tableau de t_nom
				*/
				case 7: { if (oups==1)
				{T_Nom[nb_elem]="di";
					//ecriture_case (voix, T_Nom, nb_elem, "di");
					nb_elem++;
				}
					T_Nom[nb_elem]="sept";
					//ecriture_case (voix, T_Nom, nb_elem, "sept");
					nb_elem++;

					break;
				}

				/*si unité=8 on a deux cas ou bien on ajoute "huit" ou ("diz" et "huit") dans le tableau de t_nom
				on ajoute ("diz" et "huit")  dans le cas dizaine=1,7 ou 9 c-a-d oups=1 sinon on ajoute "huit" dans le tableau de t_nom
				*/
				case 8: {
					if (oups==1)
					{T_Nom[nb_elem]="diz";
						//ecriture_case (voix, T_Nom, nb_elem, "diz");
						nb_elem++;
					}
					T_Nom[nb_elem]="huit";
					//ecriture_case (voix, T_Nom, nb_elem, "huit");
					nb_elem++;
					break;
				}

				/*si unité=9 on a deux cas ou bien on ajoute "neuf" ou ("diz" et "neuf") dans le tableau de t_nom
  				on ajoute ("diz" et "neuf")  dans le cas dizaine=1,7 ou 9 c-a-d oups=1 sinon on ajoute "neuf" dans le tableau de t_nom
				*/

				case 9: {
					if (oups==1)

					{T_Nom[nb_elem]="diz";
						//ecriture_case (voix, T_Nom, nb_elem, "diz");
						nb_elem++;
					}
					T_Nom[nb_elem]="neuf";
					nb_elem++;

					break;
				}
			}
		/*les vide dans le t_nom sert a séparer les chaines*/
			T_Nom[nb_elem]="vide";
			nb_elem++;
		}
	}
	*nbe=nb_elem;


	ecriture_case(voix, T_Nom, nb_elem);

}


