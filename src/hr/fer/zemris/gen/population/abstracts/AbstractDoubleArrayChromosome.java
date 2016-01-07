package hr.fer.zemris.gen.population.abstracts;

import java.util.Arrays;
import hr.fer.zemris.gen.decoders.IDoubleArrayDecoder;

public abstract class AbstractDoubleArrayChromosome<T, C> extends AbstractChromosome<T> {
    
    protected C chromosome;
    protected IDoubleArrayDecoder<C> decoder;
    protected double[] values;
    
    public AbstractDoubleArrayChromosome(C chromosome, IDoubleArrayDecoder<C> decoder) {
        this.decoder = decoder;
        this.chromosome = chromosome;
        
        values = decoder.encode(chromosome);
    }
    
    public AbstractDoubleArrayChromosome(double[] values, IDoubleArrayDecoder<C> decoder) {
        this.decoder = decoder;
        this.values = values;
        
        chromosome = decoder.decode(values);
    }
    
    public C getChromosome() {
        
        return chromosome;
    }
    
    public void setChromosome(C chromosome) {
        
        this.chromosome = chromosome;
        
        decoder.encode(chromosome, values);
    }
    
    public double[] readValues() {
        
        return values;
    }
    
    public double[] getValues() {
        
        return Arrays.copyOf(values, values.length);
    }
    
    public void setValues(double[] values) {
        
        this.values = Arrays.copyOf(values, values.length);
        
        chromosome = decoder.decode(values);
    }
    
    public int getArraySize() {
        
        return values.length;
    }
    
    public abstract AbstractDoubleArrayChromosome<T, C> newLikeThis(double[] values);
}
