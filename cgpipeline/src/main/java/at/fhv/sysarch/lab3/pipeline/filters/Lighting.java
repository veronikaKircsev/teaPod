package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Lighting implements IFilterPush<Pair<Face,Color>, Pair<Face,Color>>,
        IFilterPull<List<Pair<Face,Color>>, List<Pair<Face,Color>>> {

    private final Vec3 unitVector;
    private Pipe<Pair<Face,Color>> pipeSuccessor;
    private Pipe<List<Pair<Face, Color>>> predecessor;

    public Lighting(Vec3 unitVector) {
        this.unitVector = unitVector;
    }

    @Override
    public void setPipeSuccessor(Pipe<Pair<Face, Color>> pipe) {
        this.pipeSuccessor = pipe;
    }

    @Override
    public void write(Pair<Face, Color> input) {
        pipeSuccessor.write(transform(input));
    }

    @Override
    public List<Pair<Face, Color>> read() {
        try {
            List<Pair<Face, Color>> input = predecessor.read();
            List<Pair<Face, Color>> output = new ArrayList<>();
            for (Pair<Face, Color> pair : input) {
                output.add(transform(pair));
            }
            return output;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setPipePredecessor(Pipe<List<Pair<Face, Color>>> predecessor) {
        this.predecessor = predecessor;
    }

    private Pair<Face, Color> transform(Pair<Face, Color> input){
        Face face = input.fst();  // Verwende getKey() für das erste Element
        Color color = input.snd();  // Verwende getValue() für das zweite Element
        float shading = face.getN1().toVec3().dot(unitVector.getUnitVector());

        // Normalisiere shading zwischen 0 und 1
        shading = Math.max(0, Math.min(1, shading));

        if(shading <= 0){
            return new Pair<>(face, Color.BLACK);
        } else {
            // Derive the color with the shading factor
            Color shadedColor = color.deriveColor(0, 1, shading, 1);
            return new Pair<>(face, shadedColor);
        }
    }


}
