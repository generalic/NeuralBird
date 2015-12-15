package hr.fer.zemris.gen.operators.interfaces;

import hr.fer.zemris.gen.population.interfaces.ITestData;

public interface ITestDataProvider<T> {
	
	public ITestData<T> getTestData();
}