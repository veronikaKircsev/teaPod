package at.fhv.sysarch.lab3.pipeline.filters;


public class Pipe<T> {

    private IFilter<T, ?> filterSuccessor;
    private IFilter<?, T> filterPredecessor;
    //public I input;


    public void setSuccessor(IFilter<T, ?> filter) {
        this.filterSuccessor = filter;
    }

    public void setPredecessor(IFilter<?, T> filter) {
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
