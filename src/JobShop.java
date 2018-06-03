import java.util.ArrayList;

public class JobShop {
    public static ArrayList<MachineAssignement> vectorMachine = new ArrayList<>();
    public static ArrayList<Operation> operations = new ArrayList<>();


    /*Permet d'afficher l'array liste vector machine*/
    public static void afficherVectorMachine() {
        System.out.println("Affichage Vector Machine");
        for (int i = 0; i < vectorMachine.size(); i++) {
            if (i == vectorMachine.size() - 1) {
                System.out.println(vectorMachine.get(i));
            } else {
                System.out.println(vectorMachine.get(i) + ", ");
            }
        }
    }

    /*Permet d'affichier l'array liste operations*/

    /**
     *
     */
    public static void afficherOperations() {
        System.out.println("Affichage Operations");
        for (int i = 0; i < operations.size(); i++) {
            if (i == operations.size() - 1) {
               System.out.println(operations.get(i).toString());
            } else {
                System.out.println(operations.get(i).toString() + ", ");
            }
        }
    }

    /**
     * Permet de remplir une table calculant le temps d'utilisation total d'une machine
     * @param dmts table contenant les machines utilisé et leur temps d'utilisation
     * @param dmt machine a ajouter avec un nouveau temps
     * @param pireDateFin pire temps entre l'operation precedente et l'operation precedente utilisant la meme machine
     * @return la table complete
     */
    public static ArrayList<MachineFin> fusionnerDoublet3(ArrayList<MachineFin> dmts, DoubletMachinesTemps dmt, int pireDateFin) {
        int flag = 0;
        MachineFin machineFinToAdd = new MachineFin(dmt.getNumeroMachine(),pireDateFin);
        for (int i = 0; i < dmts.size(); i++) {
            if (dmts.get(i).getNumeroMachine() == dmt.getNumeroMachine()) {
                dmts.get(i).setTempsUtilisation(pireDateFin);
                flag = 1;
            }
        }
        if (flag == 0) {
            dmts.add(machineFinToAdd);
        }
        return dmts;
    }

    /**
     * Meme fonction que precedemment mais sans le temps des operations precedents celle ci
     * @param dmts
     * @param dmt
     * @return
     */
    public static ArrayList<MachineFin> fusionnerDoublet(ArrayList<MachineFin> dmts, DoubletMachinesTemps dmt) {
        MachineFin machineFinToAdd = new MachineFin(dmt.getNumeroMachine(),dmt.getDuree());
        int flag = 0;
        for (int i = 0; i < dmts.size(); i++) {
            if (dmts.get(i).getNumeroMachine() == dmt.getNumeroMachine()) {
                dmts.get(i).setTempsUtilisation(dmt.getDuree());
                flag = 1;
            }
        }
        if (flag == 0) {
            dmts.add(machineFinToAdd);
        }
        return dmts;
    }

    /**
     * Permet de trouver la pire date de fin entre l'operation precedent et l'operation precedente utilisant la meme machine
     * @param dmts table contenant les temps d'utilisation des differentes machines
     * @param opPrecedente operation precedente (pour 1,2 ce serait 1,1 par exemple)
     * @param dmt machine pour laquelle on veut trouver le pire temps
     * @return la pire date de fin
     */
    public static int trouverPireDateFin(ArrayList<MachineFin> dmts, Operation opPrecedente, DoubletMachinesTemps dmt) {
        int pireDateFin = 0;
        int flag = 0;
        for (int i = 0; i < dmts.size(); i++) {
            if (dmts.get(i).getNumeroMachine() == dmt.getNumeroMachine()) {
                flag = 1;
                if (dmts.get(i).getTempsUtilisation() > opPrecedente.getDateFin()) {
                    pireDateFin = dmts.get(i).getTempsUtilisation() + dmt.getDuree();
                } else {
                    pireDateFin = opPrecedente.getDateFin() + dmt.getDuree();
                }
            }
        }
        if (flag == 0) {
            if(opPrecedente.getId()==null) {
                pireDateFin = dmt.getDuree();
            } else {
                pireDateFin = opPrecedente.getDateFin() + dmt.getDuree();
            }
        }
        return pireDateFin;
    }

    /**
     * permets de trouver la machine utilisant le plus de temps
     * @param dmts tableau contenant les machines et leur temps d'utilisation
     * @return la plus grande duree d'utilisation
     */
    public static int trouverPlusGrandeDuree(ArrayList<MachineFin> dmts) {
        int duree = 0;
        for (int i = 0; i < dmts.size(); i++) {
            if (duree < dmts.get(i).getTempsUtilisation()) {
                duree = dmts.get(i).getTempsUtilisation();
            }
        }
        return duree;
    }

    /**
     * fonction permettant de choisir quelle machine va etre utilisee pour quelle operation
     * @param dmts tableau contenant les possibilites d'utilisation de machine pour un operation et les temps associes
     * @return un doublet machine et duree d'utilisation
     */
    public static DoubletMachinesTemps choisirMachinePourOperation(ArrayList<DoubletMachinesTemps> dmts) {
        int dureeMin = 100000;
        DoubletMachinesTemps toReturn = new DoubletMachinesTemps();
        for (int i = 0; i < dmts.size(); i++) {
            if (dmts.get(i).getDuree() < dureeMin) {
                dureeMin = dmts.get(i).getDuree();
                toReturn = dmts.get(i);
            }
        }
        return toReturn;
    }

    /**
     * Permet de trouver l'operation precedent une autre (exemple 1,2 1,1)
     * @param operationActuelle Operation pour laquelle on veut trouver la precedente
     * @param aideCalculOperations Table dans laquelle on va chercher l'operation precedente
     * @return l'operation precedente
     */
    public static Operation getOperationPrecedente(Operation operationActuelle, ArrayList<Operation> aideCalculOperations) {
        Operation operationPrecedente = new Operation();

        for (Operation op : aideCalculOperations) {
            if ((op.getId().getNumeroJob() == operationActuelle.getId().getNumeroJob() && (op.getId().getNumeroOperation() == (operationActuelle.getId().getNumeroOperation() - 1)))) {
                operationPrecedente = op;
            }
        }
        return operationPrecedente;
    }


    /**
     * Fonction permettant a partir d'une liste de jobs de remplir les 2 arrays listes vectorMachine et operations et de calculer l'heuristique gloutonne
     * @param jobs liste des jobs
     * @return la valeur de l'heuristique
     */
    public static int remplissageVecteursLogiqueGloutonne(ArrayList<Job> jobs) {
        ArrayList<Operation> aideCalculOperations = new ArrayList<>();
        int nbIteration = 0;
        int nbJobRestants = jobs.size();
        ArrayList<MachineFin> machineTempsTotal = new ArrayList<>();
        ArrayList<DoubletMachinesTemps> trouverDoubletIteration = new ArrayList<>();
        DoubletMachinesTemps machineUtiliseOperation;
        while (nbJobRestants != 0) {
            for (int i = 0; i < jobs.size(); i++) {
                if (nbIteration < jobs.get(i).getNbOperation()) {
                    trouverDoubletIteration = jobs.get(i).getOperations().get(nbIteration).getMachinesTemps();
                    machineUtiliseOperation = choisirMachinePourOperation(trouverDoubletIteration);

                    Operation opToAdd = jobs.get(i).getOperations().get(nbIteration);

                    int pireDateFin = trouverPireDateFin(machineTempsTotal, getOperationPrecedente(opToAdd,aideCalculOperations), machineUtiliseOperation);
                    machineTempsTotal = fusionnerDoublet3(machineTempsTotal, machineUtiliseOperation, pireDateFin);
                    opToAdd.setDateFin(pireDateFin);
                    operations.add(opToAdd);

                    aideCalculOperations.add(opToAdd);
                    MachineAssignement machineAssignementToAdd = new MachineAssignement(opToAdd.getId(), machineUtiliseOperation.getNumeroMachine(),machineUtiliseOperation.getDuree(),pireDateFin-machineUtiliseOperation.getDuree());
                    vectorMachine.add(machineAssignementToAdd);

                } else if (nbIteration == jobs.get(i).getNbOperation()) {
                    nbJobRestants--;
                }
            }
            nbIteration++;
        }
        return trouverPlusGrandeDuree(machineTempsTotal);

    }


    /**
     * Fonction permettant de calculer la valeur de l'heuristique a partir de vector machine et operations
     * @return la valeur de l'heuristique
     */
    public static int calculObjectif() {
        ArrayList<MachineFin> machineTempsTotal = new ArrayList<>();
        int indexMachine=0;
        DoubletMachinesTemps doubletMachinesTempsToAdd;
        int vm = 0;
        for (int op = 0; op < operations.size(); op++) {
            while ((operations.get(op).getId()!=vectorMachine.get(vm).getOperationId())&&vm<vectorMachine.size()) {
                vm++;
            }
            indexMachine = operations.get(op).indexMachine(vectorMachine.get(vm).getNumeroMachine());
            doubletMachinesTempsToAdd = new DoubletMachinesTemps(vectorMachine.get(vm).getNumeroMachine(),
                        trouverPireDateFin(machineTempsTotal,getOperationPrecedente(operations.get(op),operations), operations.get(op).getMachinesTemps().get(indexMachine)));
            machineTempsTotal = fusionnerDoublet(machineTempsTotal,doubletMachinesTempsToAdd);
            vm=0;
        }
        return trouverPlusGrandeDuree(machineTempsTotal);
    }

    /**
     * Permet de recuperer l'operation utilisant la meme machine avant celle donnee en argument
     * @param numeroMachine numero de la machine utilise
     * @param id id de l'operation pour laquelle on veut trouver l'operation precedente utilisant la meme machine
     * @return id de l'operation cherchee
     */
    public static OperationId getOperationIdPrecedenteUtilisantMemeMachine (int numeroMachine, OperationId id) {
        int op=0;
        int ma=0;
        boolean flagOp=false;
        boolean flagMa=false;
        OperationId idPrecedent = new OperationId(0,0);
        while (op<operations.size()&&!flagOp) {
            while(ma<vectorMachine.size()&&!flagMa) {
                if(operations.get(op).getId()==id){
                    flagOp = true;
                } else {
                    if(vectorMachine.get(ma).getOperationId()==operations.get(op).getId()) {
                        if (vectorMachine.get(ma).getNumeroMachine() == numeroMachine) {
                            idPrecedent = vectorMachine.get(ma).getOperationId();
                            flagMa = true;
                        }
                    }
                }
                ma++;
            }
            flagMa = false;
            ma=0;
            op++;
        }
        return idPrecedent;
    }

    /**
     * Permet de recuperer l'id de l'operation precedente
     * @param operationActuelle operation pour laquelle on veut trouver l'id de l'operation precedente
     * @param aideCalculOperations table des operations
     * @return l'id de l'operation chercher
     */

    public static OperationId getOperationIdPrecedente(Operation operationActuelle, ArrayList<Operation> aideCalculOperations) {
        Operation operationPrecedente = new Operation();

        for (Operation op : aideCalculOperations) {
            if ((op.getId().getNumeroJob() == operationActuelle.getId().getNumeroJob() && (op.getId().getNumeroOperation() == (operationActuelle.getId().getNumeroOperation() - 1)))) {
                operationPrecedente = op;
            }
        }
        return operationPrecedente.getId();
    }

    /**
     * Permet de calculer le temps maximal a partir duquel l'operation peut se lancer
     * @param id id de l'operation
     * @param tempsMaxDepartPere temps de depart maximal d'une des operations suivantes
     */
    public static void calculerTempsDepartMaximal (OperationId id,int tempsMaxDepartPere) {
        int ma=0;
        boolean flagMa=false;
        while (ma<vectorMachine.size()&&!flagMa) {
            if(vectorMachine.get(ma).getOperationId()==id) {
                flagMa = true;
                int tempsMaxCalcule = tempsMaxDepartPere-vectorMachine.get(ma).getDuree();
                if(tempsMaxCalcule<vectorMachine.get(ma).getDateDepartMaximal()) {
                    vectorMachine.get(ma).setDateDepartMaximal(tempsMaxCalcule);
                }
            }
            ma++;
        }
    }

    /**
     * Permet de calcul le temps minimal a partir duquel l'operation peut se lancer
     * @param machine numero de la machine que l'operation utilise pour retrouver le temps minimal de depart de l'operation precedente utilisant la meme
     * @param op operation precedente pour retrouver le temps minimal de depart de l'operation precedente
     * @return le temps minimal
     */
    public static int calculerTempsDepartMinimal (int machine, Operation op) {
        int pireDateFin;
        Operation operationPrecedente = getOperationPrecedente(op,operations);
        OperationId operationIdPrecedenteMemeMachine = getOperationIdPrecedenteUtilisantMemeMachine(machine,op.getId());
        Operation  operationPrecedenteMemeMachine = getOperationFromOperationId(operationIdPrecedenteMemeMachine);
        pireDateFin = maxDateFinEntreDeuxOperations(operationPrecedente,operationPrecedenteMemeMachine);
        return pireDateFin;
    }

    /**
     * Fonction permettant de retourver le temps maximal de depart a partir d'une operation id
     * @param id operation id
     * @return temps maximal de depart pour cette operation
     */
    public static int getTempsMaxFromOperationId (OperationId id) {
        int ma=0;
        boolean flagMa=false;
        int tempsMax = 0;
        while (ma<vectorMachine.size()&&!flagMa) {
            if(vectorMachine.get(ma).getOperationId()==id) {
                flagMa = true;
                tempsMax = vectorMachine.get(ma).getDateDepartMaximal();
            }
            ma++;
        }
        return tempsMax;
    }


    /**
     * Permet de recuperer un numero de machine dans l'array liste vector machine a partir d'un operation id
     * @param id id de l'operation
     * @return numero machine utilise par l'operation
     */
    public static int getNumeroMachineFromOperationId (OperationId id) {
        int ma=0;
        boolean flagMa=false;
        int numMachine = 0;
        while (ma<vectorMachine.size()&&!flagMa) {
            if(vectorMachine.get(ma).getOperationId()==id) {
                flagMa = true;
                numMachine = vectorMachine.get(ma).getNumeroMachine();
            }
            ma++;
        }
        return numMachine;
    }

    /**
     * Permet de changer le temps maximal a partir duquel peut demarer une operation
     * @param id id de l'operation
     * @param departMax temps maximum de depart pour l'operation
     */

    public static void setTempsMaxFromOperationId (OperationId id, int departMax) {
        int ma=0;
        boolean flagMa=false;
        while (ma<vectorMachine.size()&&!flagMa) {
            if(vectorMachine.get(ma).getOperationId()==id) {
                flagMa = true;
                vectorMachine.get(ma).setDateDepartMaximal(departMax);
            }
            ma++;
        }
    }

    /**
     * Permet de trouver dans la table operations le temps de l'operation qui finit la plus tard
     * @return le temps de l'operation que finit la plus tard
     */
    public static int getPlusGrandeDateFin () {
        int plusGrandeDateFin = 0;
        for(int op=0;op<operations.size();op++){
            if(operations.get(op).getDateFin()>plusGrandeDateFin){
                plusGrandeDateFin = operations.get(op).getDateFin();
            }
        }
        return plusGrandeDateFin;
    }

    /**
     *  Permet pour tout les machinesAssignement de l'array liste vector machine de calculer le temps de depart maximal
     */
    public static void calculerTousTempsDepartMaximal () {
        int tempsPere ;
        int plusGrandeDateFin = 0;
        plusGrandeDateFin = getPlusGrandeDateFin();
        /*On remplie d'abords les dates de depart maximal avec la pire date minimale*/
        for (int op=operations.size()-1;op>=0;op--){
            setTempsMaxFromOperationId(operations.get(op).getId(),plusGrandeDateFin);
        }
        /*Ensuite on calcul les temps de depart maximal en fonction des pères*/
        for (int op=operations.size()-1;op>=0;op--){
            /*Si l'opreation n'a pas ete atteinte par un pere on remplie sa date de depart maimal avec sa date de depart minimal*/
            if (getTempsMaxFromOperationId(operations.get(op).getId())==plusGrandeDateFin){
              calculerTempsDepartMaximal(operations.get(op).getId(),plusGrandeDateFin);
            }
            tempsPere= getTempsMaxFromOperationId(operations.get(op).getId());
            /*calcul des temps de depart maximal pour les 2 fils soit provenant de la machine soit provenant du job*/
            calculerTempsDepartMaximal(getOperationIdPrecedente(operations.get(op),operations),tempsPere);
            calculerTempsDepartMaximal(getOperationIdPrecedenteUtilisantMemeMachine(getNumeroMachineFromOperationId(operations.get(op).getId()),operations.get(op).getId()),tempsPere);
        }
    }

    /**
     *  Permet pour tout les machinesAssignement de l'array liste vector machine de calculer le temps de depart minimal
     */
    public static void calculerTousTempsDepartMinimal () {
        int departMinimal = 0;
        for(int vm=0;vm<vectorMachine.size();vm++){
            vectorMachine.get(vm).setDateDepartMinimal(departMinimal);
        }
        for(int vm=0;vm<vectorMachine.size();vm++){
            departMinimal = calculerTempsDepartMinimal(vectorMachine.get(vm).getNumeroMachine(),getOperationFromOperationId(vectorMachine.get(vm).getOperationId()));
            vectorMachine.get(vm).setDateDepartMinimal(departMinimal);
        }
    }

    /**
     * Permet de retrouvedr le chemin critique parmis tous les machinesAssignement
     * @return un array liste contenant les machinesAssignement dans le chemin critique
     */
    public static ArrayList<MachineAssignement> getCheminCritique () {
        ArrayList<MachineAssignement>  cheminCritique = new ArrayList<>();
        for(int vm=0;vm<vectorMachine.size();vm++){
            /*Si la date de depart maximale et minimale sont egals alors le machineAssignement fait parti du chemin critique*/
            if(vectorMachine.get(vm).getDateDepartMinimal()==vectorMachine.get(vm).getDateDepartMaximal()){
                cheminCritique.add(vectorMachine.get(vm));
            }
        }
        return cheminCritique;
    }

    /**
     * Permet de trouver l'operation qui termine le plus tard parmis deux operation donnees
     * @param a
     * @param b
     * @return le temps de l'operation qui termine le plus tard
     */
    public static int maxDateFinEntreDeuxOperations (Operation a,Operation b){
        int pireDateFin = 0;
        if(a.getDateFin()>b.getDateFin()){
            pireDateFin = a.getDateFin();
        } else {
            pireDateFin = b.getDateFin();
        }
        return pireDateFin;
    }

    /**
     * Permet de retrouver une operation dans l'array liste operations a partir de son id
     * @param id l'id de l'operation souhaitee
     * @return l'operation souhaitee
     */
    public static Operation getOperationFromOperationId(OperationId id) {
        Operation operationTrouve = new Operation();
        int op=0;
        boolean flagOp = false;
        while(op<operations.size()&&!flagOp){
            if(operations.get(op).getId()==id){
                operationTrouve = operations.get(op);
            }
            op++;
        }
        return operationTrouve;
    }

    /**
     * Permet de retrouver la position d'un machineAssignement dans l'array liste vector machine
     * @param machineAssignementAvecIndexATrouve
     * @return la position du machineAssignement dans l'array liste vector machine
     */
    public static int getIndexFromMachineAssignement (MachineAssignement machineAssignementAvecIndexATrouve){
        int vm = 0;
        boolean flagVm = false;
        while(vm<vectorMachine.size()&&!flagVm){
            if(vectorMachine.get(vm)==machineAssignementAvecIndexATrouve){
                flagVm = true;
            }
            else {
                vm++;
            }
        }
        return vm;
    }

    /**
     * Permet de retrouver la machine assigné a une certaine operation
     * @param op operation pour laquelle on veut retourver la machine qui l'a utilise
     * @return le machineAssignement de l'operation
     */
    public static MachineAssignement findMachineAssignedToOperation (Operation op) {
        MachineAssignement machineAssignedOperation = new MachineAssignement();
        int vm = 0;
        boolean flagVm = false;
        while (vm<vectorMachine.size()&&!flagVm){
            if(op.getId()==vectorMachine.get(vm).getOperationId()){
                machineAssignedOperation = vectorMachine.get(vm);
                flagVm = true;
            }
            vm++;
        }
        return machineAssignedOperation;
    }

    /**
     * Permet apres avoir finit de remplacer les machines utilisees pour les operations de calculer les dates de fin de chaque operation
     */
    public static void calculerToutesPireDateFin () {
        int pireDateFin;
        MachineAssignement machineUtiliserPourOperation = new MachineAssignement();
        for(int op=0;op<operations.size();op++){
            machineUtiliserPourOperation = findMachineAssignedToOperation(operations.get(op));
            pireDateFin = maxDateFinEntreDeuxOperations(getOperationFromOperationId(getOperationIdPrecedente(operations.get(op),operations)), getOperationFromOperationId(getOperationIdPrecedenteUtilisantMemeMachine(machineUtiliserPourOperation.getNumeroMachine(),operations.get(op).getId())));
            pireDateFin = pireDateFin + machineUtiliserPourOperation.getDuree();
            operations.get(op).setDateFin(pireDateFin);
        }
    }

    /**
     * Permet de tenter toutes les changement de machine possible pour une operation donne
     * @param positionMachineAssignementVectorMachine  Position dans l'array liste vector machine de la machineAssignement que l'on veut modifier
     * @param machineAssignementATenteDeChange MachineAssignement pour lequel on veut changer la machine utilisee
     * @return le resultat de l'heuristique apres permutation
     */
    public static int permuterMachineUtilise (int positionMachineAssignementVectorMachine ,MachineAssignement machineAssignementATenteDeChange) {
        MachineAssignement machineAssignementAuxiliaire = new MachineAssignement(machineAssignementATenteDeChange.getOperationId(),machineAssignementATenteDeChange.getNumeroMachine(),machineAssignementATenteDeChange.getDuree(),machineAssignementATenteDeChange.getDateDepartMinimal());
        MachineAssignement meilleurMachineAUtilise = new MachineAssignement(machineAssignementATenteDeChange.getOperationId(),machineAssignementATenteDeChange.getNumeroMachine(),machineAssignementATenteDeChange.getDuree(),machineAssignementATenteDeChange.getDateDepartMinimal());
        boolean flagOp = false;
        int pireDateFin = 0;
        int op=0;
        int resultatCalculObjectif;
        int resultatHeuristique = calculObjectif();
        while(op<operations.size()&&!flagOp) {
            if (operations.get(op).getId()==machineAssignementATenteDeChange.getOperationId()){
                flagOp = true;
            } else {
                op++;
            }

        }
        if (flagOp){
            for (int dmt=0;dmt<operations.get(op).getMachinesTemps().size();dmt++){
                machineAssignementAuxiliaire.setNumeroMachine(operations.get(op).getMachinesTemps().get(dmt).getNumeroMachine());
                machineAssignementAuxiliaire.setDuree(operations.get(op).getMachinesTemps().get(dmt).getDuree());
                pireDateFin = maxDateFinEntreDeuxOperations(getOperationFromOperationId(getOperationIdPrecedente(operations.get(op),operations)), getOperationFromOperationId(getOperationIdPrecedenteUtilisantMemeMachine(machineAssignementAuxiliaire.getNumeroMachine(),operations.get(op).getId())));
                machineAssignementAuxiliaire.setDateDepartMinimal(pireDateFin);
                vectorMachine.set(positionMachineAssignementVectorMachine,machineAssignementAuxiliaire);
                calculerToutesPireDateFin();
                resultatCalculObjectif = calculObjectif();
                if (resultatCalculObjectif<resultatHeuristique){
                    resultatHeuristique = resultatCalculObjectif;
                    meilleurMachineAUtilise.setDuree(machineAssignementAuxiliaire.getDuree());
                    meilleurMachineAUtilise.setDateDepartMinimal(machineAssignementAuxiliaire.getDateDepartMinimal());
                    meilleurMachineAUtilise.setNumeroMachine(machineAssignementAuxiliaire.getNumeroMachine());
                    meilleurMachineAUtilise.setOperationId(machineAssignementAuxiliaire.getOperationId());
                } else {
                    vectorMachine.set(positionMachineAssignementVectorMachine,meilleurMachineAUtilise);
                }
            }
        }
        return resultatHeuristique;
    }

    /***
     * Permet de tenter toutes les permutations de machine possibles sur le chemin critique donne
     * @param cC chemin critique donne
     * @return le resultat de l'heuristique
     */
    public static int cheminCritiquePermutationMachines (ArrayList<MachineAssignement> cC) {
        int meilleurResultat = 0;
        for (int ma=0; ma<cC.size();ma++){
            meilleurResultat = permuterMachineUtilise(getIndexFromMachineAssignement(cC.get(ma)),cC.get(ma));
        }
        return meilleurResultat;
    }

    /**
     * Permet d'effectuer toutes les permutions de machine possibles sur les chemin critique successif jusqu'a que la modification n'ameliore plus le resultat
     * @return le resultat de l'heuristique
     */
    public static int heuristiqueVoisinageParChangementMachine () {
        int resultatHeuristique = 0;
        int resultatIntermediaire = 0;
        boolean finPermutations = false;
        /*On effectue le calcul sur le chemin critique une premiere fois*/
        ArrayList<MachineAssignement> cheminCritique = getCheminCritique();
        calculerTousTempsDepartMinimal();
        calculerTousTempsDepartMaximal();
        /* On obtient le resultat de la premier heuristique*/
        resultatHeuristique=cheminCritiquePermutationMachines(cheminCritique);
        /*Tant que l'on trouve une meilleure solution*/
        while (!finPermutations){
            /*On recalcul le chemin critique et les temps de depart minimums et maximums de tous les machinesAssignement de vector machine*/
            cheminCritique=getCheminCritique();
            calculerTousTempsDepartMinimal();
            calculerTousTempsDepartMaximal();
            resultatIntermediaire = cheminCritiquePermutationMachines(cheminCritique);
            if (resultatHeuristique==resultatIntermediaire){
                /*Si le resultat de l'heuristique precedente est egale a la nouvelle alors le calcul est termine*/
                finPermutations = true;
            } else {
                /*Sinon on retiens la nouvelle valeur de l'heuristique calculee*/
                resultatHeuristique = resultatIntermediaire;
            }
        }
        return resultatHeuristique;
    }

    /**
     * Le fichier main contient les differents test que l'on a pu effectuer pour verifier nos resultats
     * @param args
     */
    public static void main(String[] args){
        ArrayList<MachineAssignement> arrayTest = new ArrayList<>();
        operations.clear();
        vectorMachine.clear();
        Parseur parseur=new Parseur();
        ArrayList<Job> jobs = new ArrayList<Job>();
        jobs = parseur.parseFile("mk06.fjs");
        //System.out.println(jobs.toString());
        System.out.println("L'heuristique gloutonne qui créé les vecteurs trouve "+remplissageVecteursLogiqueGloutonne(jobs));
        //afficherOperations();
        calculerTousTempsDepartMaximal();
        calculerTousTempsDepartMinimal();
        //afficherVectorMachine();
        //afficherOperations();
        System.out.println("L'heuristique gloutonne trouve "+calculObjectif());
        //arrayTest = getCheminCritique();
        //System.out.println("Voici le chemin critique : "+arrayTest);
        //System.out.println("Apres permutation on trouve : "+permuterMachineUtilise(getIndexFromMachineAssignement(arrayTest.get(2)),arrayTest.get(2)));
        //ystem.out.println("Apres avoir fait toutes les permutations possibles dans le chemin critique on obtient : "+cheminCritiquePermutationMachines(arrayTest));
        //arrayTest = getCheminCritique();
        //System.out.println("Apres avoir fait toutes les permutations possibles un deuxieme fois dans le nouveau chemin critique on obtient : "+cheminCritiquePermutationMachines(arrayTest));
        System.out.println("Le résultat obtenu par notre superbe heuristique est : "+heuristiqueVoisinageParChangementMachine());
        afficherVectorMachine();
        afficherOperations();
    }
}
