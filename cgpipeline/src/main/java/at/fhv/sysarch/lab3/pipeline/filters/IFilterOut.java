package at.fhv.sysarch.lab3.pipeline.filters;

public interface IFilterOut<O> {

    void setSuccessor(IFilterOut<O> successor);


}
