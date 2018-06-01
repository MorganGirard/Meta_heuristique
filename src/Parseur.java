import java.io.*;
import java.util.ArrayList;

public class Parseur {

    public Parseur() {
    }

    public static ArrayList<Job> parseFile(String fileName) {
        int numeroLigne = 0;
        /*Creation d'entier pour l'id de l'operation*/
        int numeroJob = 0;
        int numeroOperation=0;
        File file = new File(fileName);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            ArrayList<Job> jobs = new ArrayList<Job>();
            String temp=null;
            temp = bufferedReader.readLine();
            String[] premier = new String[5];
            //Regexp pour au moins 1 espace
            premier=temp.split("\\p{Space}+");
            int nbMachine = Integer.parseInt(premier[1]);
            String[] donnees = new String[Integer.parseInt(premier[0])];
            for(int j=0;j<3;j++){
                /*System.out.println(premier[j]);*/
            }
            String[] ligne = new String[100];
            int positionLigne = 0;

            /*ITERATION SUR LES JOBS*/
            /*Pourquoi une fois ca marche et une fois ca marche pas sans la deuxieme condition*/
            while(((temp=bufferedReader.readLine())!=null)&&(numeroLigne<Integer.parseInt(premier[0]))){
                /* On passe au job suivant*/
                numeroJob = numeroJob + 1;
                /*Remise a zero de position ligne*/
                positionLigne =0;
                //Recuperation des donnes de la ligne
                donnees[numeroLigne]=temp;
                ligne=donnees[numeroLigne].split("\\p{Space}+");
                //Creation du tableau de jobs
                Job jobToAdd = new Job();

                jobToAdd.setNbOperation(Integer.parseInt(ligne[0]));
                //System.out.println("Le nombre d'opération du job "+(numeroLigne+1)+" est :"+ligne[0]);
                //Recuperation de chaque operation pour chaque job
                int nombreOperationJob=1;
                //Creation tableau d'operation
                ArrayList<Operation> operations = new ArrayList<>();
                positionLigne = 1;
                //System.out.println("Liste des opérations: ");

                /*ITERATION SUR LE NOMBRE D'OPERATION*/
                numeroOperation = 0;
                while(nombreOperationJob<Integer.parseInt(ligne[0])+1){
                    numeroOperation = numeroOperation + 1;
                    //System.out.println("Operation numero :"+nombreOperationJob);
                    Operation operationToAdd = new Operation();
                    int nombreMachinesOperation = Integer.parseInt(ligne[positionLigne]);
                    operationToAdd.setNbMachine(nombreMachinesOperation);
                    operationToAdd.setId(new OperationId(numeroJob,numeroOperation));
                    ArrayList<DoubletMachinesTemps> machinesTempsToAdd = new ArrayList<DoubletMachinesTemps>();
                    for(int i=0;i<nombreMachinesOperation;i++){
                       DoubletMachinesTemps doubletToAdd = new DoubletMachinesTemps(Integer.parseInt(ligne[positionLigne+(i*2)+1]),Integer.parseInt(ligne[positionLigne+(i*2)+2]));
                       //System.out.println("doublet ajouter : machine :"+ligne[positionLigne+(i*2)+1]+" temp :"+ligne[positionLigne+(i*2)+2]);
                       machinesTempsToAdd.add(doubletToAdd);
                       //System.out.println(machinesTempsToAdd.toString());
                    }
                    positionLigne = positionLigne + nombreMachinesOperation*2+1;
                    operationToAdd.setMachinesTemps(machinesTempsToAdd);
                    //System.out.println(operationToAdd.toString());
                    operations.add(operationToAdd);
                    nombreOperationJob++;

                }
                jobToAdd.setOperations(operations);
                jobs.add(jobToAdd);

                numeroLigne++;
            }

        return jobs;
        } catch (Exception e) {
            e.printStackTrace();
        }
    return null;
    }
}
