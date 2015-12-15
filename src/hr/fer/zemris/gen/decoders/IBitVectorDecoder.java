package hr.fer.zemris.gen.decoders;

public interface IBitVectorDecoder<C> {
	
	public byte[] encode(C chromosome);
	
	public void encode(C chromosome, byte[] bytes);
	
	public C decode(byte[] bytes);
}