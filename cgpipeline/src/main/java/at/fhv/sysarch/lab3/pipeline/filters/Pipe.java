package at.fhv.sysarch.lab3.pipeline.filters;

public class Pipe<I> {

    private IFilter<I, ?> filter;

    public void setSuccessor(IFilter<I, ?> filter) {
        this.filter = filter;
    }

    public void write(I input) {
        this.filter.write(input);
    }

}
