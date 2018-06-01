import java.util.ArrayList;

public class Operation {
    private OperationId id;
    private int nbMachine;
    private int dateFin;
    private ArrayList<DoubletMachinesTemps> machinesTemps = new ArrayList<>() ;

    public Operation(){

    }

    public Operation(int nbMachine, ArrayList<DoubletMachinesTemps> machinesTemps) {
        this.nbMachine = nbMachine;
        this.machinesTemps = machinesTemps;
    }

    public Operation(OperationId id, int nbMachine, ArrayList<DoubletMachinesTemps> machinesTemps) {
        this.id = id;
        this.nbMachine = nbMachine;
        this.machinesTemps = machinesTemps;
    }

    public int getNbMachine() {
        return nbMachine;
    }

    public void setNbMachine(int nbMachine) {
        this.nbMachine = nbMachine;
    }

    public int getDateFin() {
        return dateFin;
    }

    public void setDateFin(int dateFin) {
        this.dateFin = dateFin;
    }

    public ArrayList<DoubletMachinesTemps> getMachinesTemps() {
        return machinesTemps;
    }

    public void setMachinesTemps(ArrayList<DoubletMachinesTemps> machinesTempsToAdd) {
        this.machinesTemps = machinesTempsToAdd;
    }

    public OperationId getId() {
        return id;
    }

    public void setId(OperationId id) {
        this.id = id;
    }

    public int dureeFromMachine (int numeroMachine) {
        int i =0;
        int duree = 0;
        boolean trouve = false;
        while(i<machinesTemps.size()&&!trouve){
            if(machinesTemps.get(i).getNumeroMachine()==numeroMachine){
                duree = machinesTemps.get(i).getDuree();
                trouve = true;
            }
            i++;
        }
        return duree;
    }

    public int indexMachine (int numeroMachine) {
        int i =0;
        int index = 0;
        boolean trouve = false;
        while(i<machinesTemps.size()&&!trouve){
            if(machinesTemps.get(i).getNumeroMachine()==numeroMachine){
                index = i;
                trouve = true;
            }
            i++;
        }
        return index;
    }

    @Override public String toString(){
        String str="";
        for(DoubletMachinesTemps dmt:machinesTemps){
            str=str+" id: "+this.getId().getNumeroJob()+","+this.getId().getNumeroOperation()+" Date fin : "+this.getDateFin()+" "+dmt.toString();
        }
        return "["+str+"]\n";
    }

}
