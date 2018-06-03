import java.util.ArrayList;

public class JobShop {
    public static ArrayList<MachineAssignement> vectorMachine = new ArrayList<>();
    public static ArrayList<Operation> operations = new ArrayList<>();


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


    public static int trouverPireDateFin(ArrayList<MachineFin> dmts, Operation opPrecedente, DoubletMachinesTemps dmt) {
        int pireDateFin = 0;
        int flag = 0;
        //System.out.println("Voici l'operation precedente : "+ opPrecedente);
        for (int i = 0; i < dmts.size(); i++) {
            if (dmts.get(i).getNumeroMachine() == dmt.getNumeroMachine()) {
                flag = 1;
                if (dmts.get(i).getTempsUtilisation() > opPrecedente.getDateFin()) {
                    pireDateFin = dmts.get(i).getTempsUtilisation() + dmt.getDuree();
                    //System.out.println("La machine était pire cette fois pour : " +dmt);
                } else {
                    pireDateFin = opPrecedente.getDateFin() + dmt.getDuree();
                    //System.out.println("L'operation précédente était pire cette fois pour : "+dmt+" avec pour operation precedente : "+opPrecedente+"avec pour date de fin : "+opPrecedente.getDateFin());
                }
            }
        }
        if (flag == 0) {
            //System.out.println("Je suis passer dans le cas la machine n'est pas trouvé ");
            if(opPrecedente.getId()==null) {
                //System.out.println("On creer a partir de la duree du dmt : "+dmt.getDuree());
                pireDateFin = dmt.getDuree();
            } else {
                //System.out.println("On creer a partir de l'operation precedente : "+opPrecedente+" dmt actuel : " +dmt + " voici sa date de fin "+ opPrecedente.getDateFin());
                pireDateFin = opPrecedente.getDateFin() + dmt.getDuree();
            }
        }
        return pireDateFin;
    }

    public static int trouverPlusGrandeDuree(ArrayList<MachineFin> dmts) {
        int duree = 0;
        for (int i = 0; i < dmts.size(); i++) {
            if (duree < dmts.get(i).getTempsUtilisation()) {
                duree = dmts.get(i).getTempsUtilisation();
            }
        }
        return duree;
    }

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

    public static Operation getOperationPrecedente(Operation operationActuelle, ArrayList<Operation> aideCalculOperations) {
        Operation operationPrecedente = new Operation();
        //operationPrecedente.setDateFin(0);

        for (Operation op : aideCalculOperations) {
            if ((op.getId().getNumeroJob() == operationActuelle.getId().getNumeroJob() && (op.getId().getNumeroOperation() == (operationActuelle.getId().getNumeroOperation() - 1)))) {
                operationPrecedente = op;
            }
        }
        return operationPrecedente;
    }


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
                    //System.out.println("Machine utilisait : "+machineUtiliseOperation);

                    Operation opToAdd = jobs.get(i).getOperations().get(nbIteration);

                    int pireDateFin = trouverPireDateFin(machineTempsTotal, getOperationPrecedente(opToAdd,aideCalculOperations), machineUtiliseOperation);
                    machineTempsTotal = fusionnerDoublet3(machineTempsTotal, machineUtiliseOperation, pireDateFin);
                    //System.out.println("Pire date fin : "+pireDateFin+" Machine tems total remplissage : "+machineTempsTotal);
                    opToAdd.setDateFin(pireDateFin);
                    operations.add(opToAdd);
                    //System.out.println("Avec un temps minimal d'utilisation de : "+(pireDateFin-machineUtiliseOperation.getDuree())+ " sachant que la pire date de fin est : "+pireDateFin+" et a duree da la machine est : " + machineUtiliseOperation.getDuree());
                    aideCalculOperations.add(opToAdd);
                    //System.out.println("Nouveau machine assignement; IdOp :  "+opToAdd.getId()+" numero machine : "+machineUtiliseOperation.getNumeroMachine()+" duree : "+machineUtiliseOperation.getDuree()+" date depart minimal : "+(pireDateFin-machineUtiliseOperation.getDuree()));
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



    public static int calculObjectif() {
        ArrayList<MachineFin> machineTempsTotal = new ArrayList<>();
        int indexMachine=0;
        DoubletMachinesTemps doubletMachinesTempsToAdd;
        int vm = 0;
        for (int op = 0; op < operations.size(); op++) {
            //System.out.println(" op : "+op+" operation size : "+operations.size());
            while ((operations.get(op).getId()!=vectorMachine.get(vm).getOperationId())&&vm<vectorMachine.size()) {
                //System.out.println("operationId : "+operations.get(op).getId()+" vectorMachine id : "+vectorMachine.get(vm).getOperationId());
                //System.out.println(" vm: "+vm+" vectorMachine size : "+vectorMachine.size());
                vm++;
            }
            indexMachine = operations.get(op).indexMachine(vectorMachine.get(vm).getNumeroMachine());
            doubletMachinesTempsToAdd = new DoubletMachinesTemps(vectorMachine.get(vm).getNumeroMachine(),
                        trouverPireDateFin(machineTempsTotal,getOperationPrecedente(operations.get(op),operations), operations.get(op).getMachinesTemps().get(indexMachine)));
            //System.out.println("machine temps total : "+machineTempsTotal+" op precedente "+getOperationPrecedente(operations.get(op),operations)+" doublet machine temps "+doubletMachinesTempsToAdd+" operation actuelle : "+operations.get(op)+" index machine : "+indexMachine);
            //System.out.println("machine temps to add : "+doubletMachinesTempsToAdd);
            //System.out.println("Machine temps to add : "+doubletMachinesTempsToAdd+" Machine temps total avant : "+machineTempsTotal);
            machineTempsTotal = fusionnerDoublet(machineTempsTotal,doubletMachinesTempsToAdd);
            //System.out.println("Machine temps total apres : "+machineTempsTotal);
            //System.out.println("");
            vm=0;
        }
        //System.out.println("Machine temps total a la fin : "+machineTempsTotal);
        return trouverPlusGrandeDuree(machineTempsTotal);
    }

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

    public static OperationId getOperationIdPrecedente(Operation operationActuelle, ArrayList<Operation> aideCalculOperations) {
        Operation operationPrecedente = new Operation();
        //operationPrecedente.setDateFin(0);

        for (Operation op : aideCalculOperations) {
            if ((op.getId().getNumeroJob() == operationActuelle.getId().getNumeroJob() && (op.getId().getNumeroOperation() == (operationActuelle.getId().getNumeroOperation() - 1)))) {
                operationPrecedente = op;
            }
        }
        return operationPrecedente.getId();
    }

    public static void calculerTempsDepartMaximal (OperationId id,int tempsMaxDepartPere) {
        int ma=0;
        boolean flagMa=false;
        while (ma<vectorMachine.size()&&!flagMa) {
            if(vectorMachine.get(ma).getOperationId()==id) {
                flagMa = true;
                int tempsMaxCalcule = tempsMaxDepartPere-vectorMachine.get(ma).getDuree();
                //System.out.println("Operation id : "+id+" Temps max depart pere : "+tempsMaxDepartPere+" duree de l'operation : "+vectorMachine.get(ma).getDuree()+" TemsMaxCalcule : "+tempsMaxCalcule);
                if(tempsMaxCalcule<vectorMachine.get(ma).getDateDepartMaximal()) {
                    vectorMachine.get(ma).setDateDepartMaximal(tempsMaxCalcule);
                }
            }
            ma++;
        }
    }

    public static int calculerTempsDepartMinimal (int machine, Operation op) {
        int pireDateFin;
        Operation operationPrecedente = getOperationPrecedente(op,operations);
        OperationId operationIdPrecedenteMemeMachine = getOperationIdPrecedenteUtilisantMemeMachine(machine,op.getId());
        Operation  operationPrecedenteMemeMachine = getOperationFromOperationId(operationIdPrecedenteMemeMachine);
        pireDateFin = maxDateFinEntreDeuxOperations(operationPrecedente,operationPrecedenteMemeMachine);
        return pireDateFin;
    }

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

    public static void setTempsMaxFromOperationId (OperationId id, int departMax) {
        int ma=0;
        boolean flagMa=false;
        int numMachine = 0;
        while (ma<vectorMachine.size()&&!flagMa) {
            if(vectorMachine.get(ma).getOperationId()==id) {
                flagMa = true;
                vectorMachine.get(ma).setDateDepartMaximal(departMax);
            }
            ma++;
        }
    }

    public static int getPlusGrandeDateFin () {
        int plusGrandeDateFin = 0;
        for(int op=0;op<operations.size();op++){
            if(operations.get(op).getDateFin()>plusGrandeDateFin){
                plusGrandeDateFin = operations.get(op).getDateFin();
            }
        }
        return plusGrandeDateFin;
    }

    public static void calculerTousTempsDepartMaximal () {
        int tempsPere ;
        int plusGrandeDateFin = 0;
        plusGrandeDateFin = getPlusGrandeDateFin();
        //System.out.println("Plus grande date fin : "+plusGrandeDateFin);
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

    public static ArrayList<MachineAssignement> getCheminCritique () {
        ArrayList<MachineAssignement>  cheminCritique = new ArrayList<>();
        for(int vm=0;vm<vectorMachine.size();vm++){
            if(vectorMachine.get(vm).getDateDepartMinimal()==vectorMachine.get(vm).getDateDepartMaximal()){
                cheminCritique.add(vectorMachine.get(vm));
            }
        }
        return cheminCritique;
    }

    public static int maxDateFinEntreDeuxOperations (Operation a,Operation b){
        int pireDateFin = 0;
        if(a.getDateFin()>b.getDateFin()){
            pireDateFin = a.getDateFin();
        } else {
            pireDateFin = b.getDateFin();
        }
        return pireDateFin;
    }

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

    public static int getIndexFromMachineAssignement (MachineAssignement machineAssignementAvecIndexATrouve){
        int vm = 0;
        boolean flagVm = false;
        while(vm<vectorMachine.size()&&!flagVm){
            if(vectorMachine.get(vm)==machineAssignementAvecIndexATrouve){
                flagVm = true;
            }
            vm++;
        }
        vm--;
        return vm;
    }

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


    public static int permuterMachineUtilise (int positionMachineAssignementVectorMachine ,MachineAssignement machineAssignementATenteDeChange) {
        //System.out.println("On commence la fonction de permutation avec comme machineAssignement a permute : "+machineAssignementATenteDeChange);
        MachineAssignement machineAssignementAuxiliaire = new MachineAssignement(machineAssignementATenteDeChange.getOperationId(),machineAssignementATenteDeChange.getNumeroMachine(),machineAssignementATenteDeChange.getDuree(),machineAssignementATenteDeChange.getDateDepartMinimal());
        MachineAssignement meilleurMachineAUtilise = new MachineAssignement(machineAssignementATenteDeChange.getOperationId(),machineAssignementATenteDeChange.getNumeroMachine(),machineAssignementATenteDeChange.getDuree(),machineAssignementATenteDeChange.getDateDepartMinimal());
        boolean flagOp = false;
        int pireDateFin = 0;
        int op=0;
        int resultatCalculObjectif;
        int resultatHeuristique = calculObjectif();
        while(op<operations.size()&&!flagOp) {
            if (operations.get(op).getId()== machineAssignementATenteDeChange.getOperationId()){
                flagOp = true;
            }
            op++;
        }
        op--;
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
                    //System.out.println("On a trouve une meilleure machine a utiliser cela donne ce machina assignement : "+machineAssignementAuxiliaire);
                    resultatHeuristique = resultatCalculObjectif;
                    meilleurMachineAUtilise = machineAssignementAuxiliaire;
                } else {
                    //System.out.println("Remise a zero");
                    vectorMachine.set(positionMachineAssignementVectorMachine,meilleurMachineAUtilise);
                }
            }
        }
        //System.out.println("Pour ce machineAssignement on retourne : "+resultatHeuristique);
        return resultatHeuristique;
    }

    public static int cheminCritiquePermutationMachines (ArrayList<MachineAssignement> cC) {
        int meilleurResultat = 0;
        for (int ma=0; ma<cC.size();ma++){
            meilleurResultat = permuterMachineUtilise(getIndexFromMachineAssignement(cC.get(ma)),cC.get(ma));
        }
        return meilleurResultat;
    }

    public static int heuristiqueVoisinageParChangementMachine () {
        int resultatHeuristique = 0;
        int resultatIntermediaire = 0;
        boolean finPermutations = false;
        ArrayList<MachineAssignement> cheminCritique = getCheminCritique();
        resultatHeuristique=cheminCritiquePermutationMachines(cheminCritique);
        calculerTousTempsDepartMinimal();
        calculerTousTempsDepartMaximal();
        while (!finPermutations){
            System.out.println("Je refais de nouveau le calcul avec un nouveau chemin critique");
            cheminCritique=getCheminCritique();
            resultatIntermediaire = cheminCritiquePermutationMachines(cheminCritique);
            calculerTousTempsDepartMinimal();
            calculerTousTempsDepartMaximal();
            if (resultatHeuristique==resultatIntermediaire){
                finPermutations = true;
            } else {
                resultatHeuristique = resultatIntermediaire;
            }
        }
        return resultatHeuristique;
    }

    public static void main(String[] args){
        /*FAIRE EN SORTE QUE LES CHOIX DE MACHINE SE FASSE ALEATOIREMENT*/
        ArrayList<MachineAssignement> arrayTest = new ArrayList<>();
        operations.clear();
        vectorMachine.clear();
        Parseur parseur=new Parseur();
        ArrayList<Job> jobs = new ArrayList<Job>();
        jobs = parseur.parseFile("Mk01.fjs");
        //System.out.println(jobs.toString());
        System.out.println("L'heuristique gloutonne qui créé les vecteurs trouve "+remplissageVecteursLogiqueGloutonne(jobs));
        //afficherOperations();
        //calculerTousTempsDepartMaximal();
        afficherVectorMachine();
        //afficherOperations();
        //System.out.println("L'heuristique gloutonne trouve "+calculObjectif());
        //arrayTest = getCheminCritique();
        //System.out.println("Voici le chemin critique : "+arrayTest);
        //System.out.println("Apres permutation on trouve : "+permuterMachineUtilise(getIndexFromMachineAssignement(arrayTest.get(2)),arrayTest.get(2)));
        //System.out.println("Apres avoir fait toutes les permutations possibles dans le chemin critique on obtient : "+cheminCritiquePermutationMachines(arrayTest));
        //calculerTousTempsDepartMaximal();
        //calculerTousTempsDepartMinimal();
        //arrayTest = getCheminCritique();
        //System.out.println("Voici le chemin critique : "+arrayTest);
        System.out.println("Le résultat obtenu par notre superbe heuristique est : "+heuristiqueVoisinageParChangementMachine());
        afficherVectorMachine();
        //afficherOperations();
    }
}
