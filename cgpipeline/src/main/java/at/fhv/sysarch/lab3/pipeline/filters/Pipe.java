package at.fhv.sysarch.lab3.pipeline.filters;


public class Pipe<T> {

    private IFilterPush<T, ?> filterSuccessor;
    private IFilterPull<?, T> filterPredecessor;


    public void setSuccessor(IFilterPush<T, ?> filter) {
        this.filterSuccessor = filter;
    }

    public void setPredecessor(IFilterPull<?, T> filter) {
        this.filterPredecessor = filter;
    }

    public void write(T input) {
        if (filterSuccessor != null) {
            this.filterSuccessor.write(input);
        }
    }

    public T read() {
        if (filterPredecessor != null) {
            T input = filterPredecessor.read() != null ? filterPredecessor.read() : null;
            if (input != null) {
                return input;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }


}
