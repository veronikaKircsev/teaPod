package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Model;

public interface IFilterIn<I> {

    void write(I input);

}
