public class DoubletMachinesTemps {

    private int numeroMachine;
    private int Duree;

    public DoubletMachinesTemps() {
    }

    public DoubletMachinesTemps(int numeroMachine, int duree) {
        this.numeroMachine = numeroMachine;
        Duree = duree;
    }

    public int getNumeroMachine() {
        return numeroMachine;
    }

    public void setNumeroMachine(int numeroMachine) {
        this.numeroMachine = numeroMachine;
    }

    public int getDuree() {
        return Duree;
    }

    public void setDuree(int duree) {
        Duree = duree;
    }

    @Override public String toString(){
        return "Machine : "+numeroMachine+" durée : "+Duree+" ";
    }
}
