package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;

public class Source implements IFilterOut<Face>, IFilterIn<Model> {

    private IFilterIn<Face> successor;

    public void setSuccessor(IFilterIn<Face> r) {
        this.successor = r;
    }

    public void write(Model model) {
        model.getFaces().forEach(face -> successor.write(face));
    }
}
