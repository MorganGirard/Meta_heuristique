import java.util.ArrayList;

public class Job {
    private int nbOperation;
    private ArrayList<Operation> operations = new ArrayList<Operation>();

    public Job(){

    }

    public Job(int nbOperation, ArrayList<Operation> operations) {
        this.nbOperation = nbOperation;
        this.operations = operations;
    }

    public int getNbOperation() {
        return nbOperation;
    }

    public void setNbOperation(int nbOperation) {
        this.nbOperation = nbOperation;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
    }

    @Override public String toString(){
        String str="";
        for(Operation op:operations){
            str=str+op.toString();
        }
        return str;
    }
}
