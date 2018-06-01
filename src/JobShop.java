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
        MachineFin machineFinToAdd = new MachineFin(dmt.getNumeroMachine(),dmt.getDuree());
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
            if(opPrecedente==null) {
                pireDateFin = dmt.getDuree();
            } else {
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
                    System.out.println("Machine tems total remplissage : "+machineTempsTotal);
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



    public static int calculObjectif(ArrayList<Job> jobs) {
        System.out.println("GLOUTONNE A VERIFIER");
        ArrayList<MachineFin> machineTempsTotal = new ArrayList<>();
        int indexMachine=0;
        DoubletMachinesTemps doubletMachinesTempsToAdd;
        int vm = 0;
        for (int op = 0; op < operations.size(); op++) {
            while (operations.get(op).getId()!=vectorMachine.get(vm).getOperationId()) {
                vm++;
                indexMachine = operations.get(op).indexMachine(vectorMachine.get(vm).getNumeroMachine());
            }
            doubletMachinesTempsToAdd = new DoubletMachinesTemps(vectorMachine.get(vm).getNumeroMachine(),
                        trouverPireDateFin(machineTempsTotal,getOperationPrecedente(operations.get(op),operations), operations.get(op).getMachinesTemps().get(indexMachine)));
            //System.out.println("machine temps total : "+machineTempsTotal+" op precedente "+getOperationPrecedente(operations.get(op),operations)+" doublet machine temps "+operations.get(op).getMachinesTemps().get(indexMachine));
            //System.out.println("machine temps to add : "+doubletMachinesTempsToAdd);
            System.out.println("Machine temps to add : "+doubletMachinesTempsToAdd+" Machine temps total avant : "+machineTempsTotal);
            machineTempsTotal = fusionnerDoublet(machineTempsTotal,doubletMachinesTempsToAdd);
            System.out.println("Machine temps total apres : "+machineTempsTotal);
            System.out.println("");
            vm=0;
        }
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
                //System.out.println("Temps max depart pere : "+tempsMaxDepartPere+" duree de l'operation : "+vectorMachine.get(ma).getDuree());
                if(tempsMaxCalcule<vectorMachine.get(ma).getDateDepartMaximal()) {
                    //System.out.println("Le temps max calculé est : "+tempsMaxCalcule+" comparé au temps actuel qui est : "+vectorMachine.get(ma).getDateDepartMaximal());
                    vectorMachine.get(ma).setDateDepartMaximal(tempsMaxCalcule);
                }
            }
            ma++;
        }
    }

    public static void tempsDepartMaxPremier (OperationId id) {
        int ma=0;
        boolean flagMa=false;
        while (ma<vectorMachine.size()&&!flagMa) {
            if(vectorMachine.get(ma).getOperationId()==id) {
                flagMa = true;
                vectorMachine.get(ma).setDateDepartMaximal(vectorMachine.get(ma).getDateDepartMinimal());
            }
            ma++;
        }
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

    public static int getTempsMinFromOperationId (OperationId id) {
        int ma=0;
        boolean flagMa=false;
        int tempsMax = 0;
        while (ma<vectorMachine.size()&&!flagMa) {
            if(vectorMachine.get(ma).getOperationId()==id) {
                flagMa = true;
                tempsMax = vectorMachine.get(ma).getDateDepartMinimal();
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

    public static void calculerTousTempsDepartMaximal () {
        int tempsPere ;
        for (int op=operations.size()-1;op>=0;op--){
            if (getTempsMaxFromOperationId(operations.get(op).getId())==0){
              setTempsMaxFromOperationId(operations.get(op).getId(),getTempsMinFromOperationId(operations.get(op).getId()));
              //System.out.println("Bonjour");
            }
            tempsPere= getTempsMaxFromOperationId(operations.get(op).getId());
            //System.out.println("Operation actuelle : "+operations.get(op)+" Operation precedent : "+getOperationIdPrecedente(operations.get(op),operations)+" Operation precedente machine :"+getOperationIdPrecedenteUtilisantMemeMachine(getNumeroMachineFromOperationId(operations.get(op).getId()),operations.get(op).getId()));
            //System.out.println("Temps max du pere : "+ tempsPere);
            //System.out.println("Operation actuelle : "+operations.get(op));
            /*calcul des temps maximum pour les fils*/
            calculerTempsDepartMaximal(getOperationIdPrecedente(operations.get(op),operations),tempsPere);
            calculerTempsDepartMaximal(getOperationIdPrecedenteUtilisantMemeMachine(getNumeroMachineFromOperationId(operations.get(op).getId()),operations.get(op).getId()),tempsPere);
        }
    }

    /*public static ArrayList<MachineAssignement> cheminCritique () {

    }*/
    public static void main(String[] args){
        /*FAIRE EN SORTE QUE LES CHOIX DE MACHINE SE FASSE ALEATOIREMENT*/
        operations.clear();
        vectorMachine.clear();
        Parseur parseur=new Parseur();
        ArrayList<Job> jobs = new ArrayList<Job>();
        jobs = parseur.parseFile("t1.fjs");
        //System.out.println(jobs.toString());
        System.out.println("L'heuristique gloutonne qui créé les vecteurs trouve "+remplissageVecteursLogiqueGloutonne(jobs));
        //afficherOperations();
        calculerTousTempsDepartMaximal();
        afficherVectorMachine();
        System.out.println("L'heuristique gloutonne trouve "+calculObjectif(jobs));


    }
}
