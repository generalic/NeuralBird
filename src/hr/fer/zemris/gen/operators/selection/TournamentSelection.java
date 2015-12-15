package hr.fer.zemris.gen.operators.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import hr.fer.zemris.gen.operators.abstracts.AbstractSelectionOperator;
import hr.fer.zemris.gen.population.interfaces.IChromosome;

public class TournamentSelection<T, U extends IChromosome<T>> extends AbstractSelectionOperator<T, U> {
    
    private int tournamentSize;
    
    public TournamentSelection(int numOfSelections, int tournamentSize) {
        super(numOfSelections);
        
        if (tournamentSize < 1) {
            throw new IllegalArgumentException("Invalid tournament size: " + tournamentSize);
        }
        
        this.tournamentSize = tournamentSize;
    }
    
    @Override
    public SortedSet<U> performSelection(SortedSet<U> population) {
        
        if (tournamentSize > population.size()) {
            throw new IllegalArgumentException(
                    "Tournament size is bigger than population size: " + tournamentSize + " >> " + population.size());
        }
        
        List<U> populationList = new ArrayList<>(population);
        SortedSet<U> selected = new TreeSet<>();
        
        for (int i = 0; i < numOfSelections; i++) {
            doTournament(populationList, selected);
        }
        
        return selected;
    }
    
    private void doTournament(List<U> population, SortedSet<U> selectedParents) {
        
        Collections.shuffle(population);
        
        SortedSet<U> tournament = new TreeSet<>();
        
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(population.get(i));
        }
        
        selectedParents.add(tournament.first());
    }
}
