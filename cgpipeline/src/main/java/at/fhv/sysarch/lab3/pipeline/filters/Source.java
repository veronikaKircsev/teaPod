package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Model;

public class Source implements IFilterPull<Model, Model> {

    private Model model;

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public Model read() {
        return model;
    }

    @Override
    public void setPipePredecessor(Pipe<Model> predecessor) {

    }



}
