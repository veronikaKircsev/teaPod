package at.fhv.sysarch.lab3.pipeline.filters;

public interface IFilterPull<I, O> {

    void setPipePredecessor(Pipe<I> predecessor);
    O read();
}
