package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

public class Coloring implements IFilter<Face, Pair<Face, Color>> {

    private IFilter<Pair<Face, Color>, ?> successor;
    private Color coloring;

    public Coloring(Color coloring) {
        this.coloring = coloring;
    }

    @Override
    public void setSuccessor(IFilter<Pair<Face, Color>, ?> successor) {
        this.successor = successor;
    }


    @Override
    public void write(Face input) {
        Pair<Face, Color> data = new Pair<>(input, coloring);
       successor.write(data);
    }


}
