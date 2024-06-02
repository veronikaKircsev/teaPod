package at.fhv.sysarch.lab3.pipeline.filters;

public interface IFilter<I, O> {

    public void write(I input);

    O read();
}
