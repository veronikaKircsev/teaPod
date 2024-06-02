package at.fhv.sysarch.lab3.pipeline.filters;

public interface IFilterIn<I,O> {

    O read();
    void setPipePredecessor(Pipe<I> predecessor);
}
