package main;

import java.util.ArrayList;
import java.util.Random;

public class Population {
    private ArrayList<Individual> individualsList = new ArrayList<>();
    private int populationMaxSize;
    private int genesLength;
    private int mutationCount;

    Population(int genesLength, int populationSize) {
        for (int i = 0; i < populationSize; i++) {
            this.individualsList.add(new Individual(genesLength));
        }
        this.mutationCount = 0;
        this.populationMaxSize = populationSize;
        this.genesLength = genesLength;
    }

    //compute all fitness of the population
    void calculateFitness(String password) {
        for (Individual individual : individualsList)
            individual.calcFitness(password);
    }

    void newGeneration() {
        ArrayList<Individual> parents;
        ArrayList<Individual> children;
        Individual individual;

        ArrayList<Individual> newPopulation = new ArrayList<>();

        //Top 1 elitism
        individual = bestFitness();
        individual.aging();
        newPopulation.add(individual);

        //5% new blood
        for (int i = 0; i < individualsList.size()/20 + 1; i++) {
            individual = new Individual(genesLength);
            individual.age = -1;
            newPopulation.add(individual);
        }

        //What's left is offsprings
        while (newPopulation.size() < populationMaxSize) {
            parents = rouletteWheelSelection();
            children = uniformCrossover(parents);
            tryMutate(children);
            mutationCount++;
            newPopulation.addAll(children);
        }
        //We replace the old generation by the new one
        individualsList.clear();
        individualsList.addAll(newPopulation);
    }

    private ArrayList<Individual> rouletteWheelSelection() {
        ArrayList<Individual> parents = new ArrayList<>();
        Individual parent;

        //We roulette select the first parent
        parents.add(rouletteWheelSelectionParent());

        //We roulette select another parent until it's a different one.
        do {
            parent = rouletteWheelSelectionParent();
        }
        while (parents.get(0) == parent);
        parents.add(parent);

        return parents;
    }

    private Individual rouletteWheelSelectionParent() {
        float fitnessSum = 0;
        //We calculate the sum of all fitness
        for (Individual individual : individualsList)
            fitnessSum += individual.fitness;

        //We pick a random number between 0 and the sum
        Random r = new Random();
        double value = (fitnessSum) * r.nextDouble();

        //We decrease this value by the fitness until we have a winner.
        for (Individual individual : individualsList) {
            value -= individual.fitness;
            if (value < 0) return individual;
        }
        return null;
    }

    //Generate two children given two parents using uniform crossover.
    private ArrayList<Individual> uniformCrossover(ArrayList<Individual> parents) {
        ArrayList<Individual> children = new ArrayList<>();
        int length = parents.get(0).getLength();
        char temp;

        //We clone the parents as a template for the two children
        for (Individual parent : parents) {
            children.add(parent.copy());
        }

        //Then we randomly exchange gene between children to mix them.
        for (int i = 0; i < length; i++) {
            if (new Random().nextBoolean()) {
                temp = children.get(0).genes[i];
                children.get(0).genes[i] = children.get(1).genes[i];
                children.get(1).genes[i] = temp;
            }
        }
        return children;
    }

    //Try to mutate givens individuals depending on their mutation chances, return true if a mutation happened, else false.
    private void tryMutate(ArrayList<Individual> individuals) {
        for (Individual Indiv : individuals)
            Indiv.tryMutate();
    }

    //Return the individual with the best fitness in the given population. O(n)
    private Individual bestFitness(ArrayList<Individual> population) {
        Individual bestIndiv = null;
        for (Individual individual : population) {
            if (bestIndiv == null)
                bestIndiv = individual;
            else if (individual.fitness > bestIndiv.fitness)
                bestIndiv = individual;
        }
        return bestIndiv;
    }

    //Return the individual with the best fitness in the given population. O(n)
    Individual bestFitness() {
        return bestFitness(individualsList);
    }

    //Return the individual with the best fitness in the population. O(n)
    float bestFitnessInPopulation() {
        return bestFitness(individualsList).fitness;
    }

    int getMutationCount() {
        return mutationCount;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Individual individual : individualsList) {
            str.append("\n\t").append(individual.toString());
        }
        return str.toString();
    }
}
