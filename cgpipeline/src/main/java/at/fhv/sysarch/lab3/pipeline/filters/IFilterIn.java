package at.fhv.sysarch.lab3.pipeline.filters;

public interface IFilterIn<I> {

    void write(I input);
    void setPipePredecessor(Pipe<I> successor);
}
