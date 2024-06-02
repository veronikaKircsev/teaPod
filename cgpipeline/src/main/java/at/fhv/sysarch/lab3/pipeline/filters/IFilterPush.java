package at.fhv.sysarch.lab3.pipeline.filters;

public interface IFilterPush<I, O> {

    void setPipeSuccessor(Pipe<O> successor);
    void write(I input);


}
