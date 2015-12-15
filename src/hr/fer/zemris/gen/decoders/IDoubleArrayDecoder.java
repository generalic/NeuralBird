package hr.fer.zemris.gen.decoders;

public interface IDoubleArrayDecoder<C> {
	
	public double[] encode(C chromosome);
	
	public void encode(C chromosome, double[] values);
	
	public C decode(double[] values);
}