package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

public class Lighting implements IFilter<Pair<Face, Color>,Pair<Face,Color> > {

    private IFilter<Pair<Face, Color>, ?> successor;
    private final Vec3 unitVector;
    private Pipe<Pair<Face,Color>> pipe;

    public Lighting(Vec3 unitVector) {
        this.unitVector = unitVector;
    }


    @Override
    public void setSuccessor(IFilter<Pair<Face, Color>, ?> successor) {
        this.successor = successor;
    }

    public void setPipe(Pipe<Pair<Face, Color>> pipe) {
        this.pipe = pipe;
    }

    @Override
    public void write(Pair<Face, Color> input) {
        Face face = input.fst();  // Verwende getKey() für das erste Element
        Color color = input.snd();  // Verwende getValue() für das zweite Element
        float shading = face.getN1().toVec3().dot(unitVector.getUnitVector());

        // Normalisiere shading zwischen 0 und 1
        shading = Math.max(0, Math.min(1, shading));

        if(shading <= 0){
            pipe.write(new Pair<>(face, Color.BLACK));
        } else {
            // Derive the color with the shading factor
            Color shadedColor = color.deriveColor(0, 1, shading, 1);
            pipe.write(new Pair<>(face, shadedColor));
        }
    }


}
