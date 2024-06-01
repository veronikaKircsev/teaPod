package at.fhv.sysarch.lab3.pipeline.filters;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

public class ModelViewTransformationFilter implements IFilter<Face, Face>, IFilterOut<Face>{
    private Mat4 transMatrix;
    //successor es sich um den nächsten Filter in einer Kette von Filtern handelt
    private IFilter<Face, ?> successor;
    private Pipe<Face> pipeSuccessor;

    @Override
    public void setSuccessor(IFilter<Face, ?> successor) {
        this.successor = successor;
    }

    public void setPipeSuccessor(Pipe<Face> pipe) {

        this.pipeSuccessor = pipe;
    }

    public void setTransMatrix(Mat4 matrix) {
        this.transMatrix = matrix;
    }

    public void write(Face input) {
        // Transformation der Eckpunkte des Gesichts
        Vec4 v1new = transMatrix.multiply(input.getV1());
        //Vec4: Eine Klasse, die einen vierdimensionalen Vektor darstellt,
        // wahrscheinlich mit Methoden zur Matrixmultiplikation.
        Vec4 v2new = transMatrix.multiply(input.getV2());
        //transMatrix: Eine Instanz einer Transformationsmatrix,
        // die auf die Vektoren angewendet wird.
        Vec4 v3new = transMatrix.multiply(input.getV3());

        // Transformation der Normalen des Gesichts
        Vec4 v1NormalNew = transMatrix.multiply(input.getN1());
        //Normalvektoren sind in der Computergrafik und in der 3D-Modellierung von großer Bedeutung,
        // da sie für die Berechnung von Beleuchtung, Schattierung und Reflexionen verwendet werden.
        Vec4 v2NormalNew = transMatrix.multiply(input.getN2());
        Vec4 v3NormalNew = transMatrix.multiply(input.getN3());

        // Erstellung eines neuen Face-Objekts mit den transformierten Werten
        //Face: Eine Klasse, die ein Gesicht eines 3D-Objekts darstellt, mit Methoden
        // zum Abrufen der Eckpunkte (getV1(), getV2(), getV3())
        // und Normalen (getN1(), getN2(), getN3()).
        Face transFace = new Face(v1new, v2new, v3new, v1NormalNew, v2NormalNew, v3NormalNew);



        // Weitergabe des transformierten Face-Objekts an den nächsten Filter in der Kette
        pipeSuccessor.write(transFace);

    }
}