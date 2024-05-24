package at.fhv.sysarch.lab3.pipeline.filters;

public interface IFilter<I, O> {

    public void setSuccessor(IFilter<O,?> successor);

    public void write(I input);
}
