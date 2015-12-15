package hr.fer.zemris.gen.population.abstracts;

import java.util.Arrays;
import hr.fer.zemris.gen.decoders.IBitVectorDecoder;

public abstract class AbstractBitVectorChromosome<T, C> extends AbstractChromosome<T> {
	
	protected C chromosome;
	private IBitVectorDecoder<C> decoder;
	private byte[] bytes;
	
	public AbstractBitVectorChromosome(C chromosome, IBitVectorDecoder<C> decoder) {
		
		this.decoder = decoder;
		this.chromosome = chromosome;
		
		bytes = decoder.encode(chromosome);
	}
	
	public C getChromosome() {
		
		return chromosome;
	}
	
	public void setChromosome(C chromosome) {
		
		this.chromosome = chromosome;
		
		decoder.encode(chromosome, bytes);
	}
	
	public byte[] getBytes() {
		
		return Arrays.copyOf(bytes, bytes.length);
	}
	
	public void setBytes(byte[] bytes) {
		
		this.bytes = Arrays.copyOf(bytes, bytes.length);
		
		chromosome = decoder.decode(bytes);
	}
}