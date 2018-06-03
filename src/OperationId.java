public class OperationId {
    private int numeroJob;
    private int numeroOperation;

    public OperationId(int numeroJob, int numeroOperation) {
        this.numeroJob = numeroJob;
        this.numeroOperation = numeroOperation;
    }

    public int getNumeroJob() {
        return numeroJob;
    }

    public void setNumeroJob(int numeroJob) {
        this.numeroJob = numeroJob;
    }

    public int getNumeroOperation() {
        return numeroOperation;
    }

    public void setNumeroOperation(int numeroOperation) {
        this.numeroOperation = numeroOperation;
    }

    @Override
    public String toString() {
        return "numeroJob=" + numeroJob +
                ", numeroOperation=" + numeroOperation;
    }
}
