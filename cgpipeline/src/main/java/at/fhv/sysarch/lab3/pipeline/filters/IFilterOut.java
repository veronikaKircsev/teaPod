package at.fhv.sysarch.lab3.pipeline.filters;

public interface IFilterOut<O> {

    public void setSuccessor(IFilterIn<O> successor);


}
