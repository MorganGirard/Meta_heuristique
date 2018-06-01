public class MachineAssignement {
    private OperationId operationId;
    private int numeroMachine;
    private int duree;
    private int dateDepartMinimal;
    private int dateDepartMaximal;

    public MachineAssignement(OperationId operationId, int numeroMachine, int duree, int dateDepartMinimal) {
        this.operationId = operationId;
        this.numeroMachine = numeroMachine;
        this.duree = duree;
        this.dateDepartMinimal = dateDepartMinimal;
    }

    public MachineAssignement(OperationId operationId, int numeroMachine, int duree, int dateDepartMinimal, int dateDepartMaximal) {
        this.operationId = operationId;
        this.numeroMachine = numeroMachine;
        this.duree = duree;
        this.dateDepartMinimal = dateDepartMinimal;
        this.dateDepartMaximal = dateDepartMaximal;
    }

    public OperationId getOperationId() {
        return operationId;
    }

    public void setOperationId(OperationId operationId) {
        this.operationId = operationId;
    }

    public int getNumeroMachine() {
        return numeroMachine;
    }

    public void setNumeroMachine(int numeroMachine) {
        this.numeroMachine = numeroMachine;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public int getDateDepartMinimal() {
        return dateDepartMinimal;
    }

    public void setDateDepartMinimal(int dateDepartMinimal) {
        this.dateDepartMinimal = dateDepartMinimal;
    }

    public int getDateDepartMaximal() {
        return dateDepartMaximal;
    }

    public void setDateDepartMaximal(int dateDepartMaximal) {
        this.dateDepartMaximal = dateDepartMaximal;
    }

    @Override
    public String toString() {
        return "OperationId: " + operationId.toString() + " Numero machine: " + numeroMachine+ " Duree : "+duree+" Date debut minimal : "+dateDepartMinimal+" Date debut maximal : "+dateDepartMaximal;
    }
}
